# travel_agent.py
# 旅行 AI Agent，基于 LangChain + LangGraph + MCP
# 集成：高德地图（stdio）、飞常准（stdio）、12306（自定义工具）

import os
import re
import json
import asyncio
import traceback
from datetime import date
from typing import Any, AsyncIterator
from dotenv import load_dotenv

from openai import RateLimitError
from langchain_openai import ChatOpenAI
from langchain_core.messages import BaseMessage, HumanMessage, AIMessage, SystemMessage
from langgraph.errors import GraphRecursionError
from langgraph.prebuilt import create_react_agent, ToolNode
from langchain_mcp_adapters.client import MultiServerMCPClient
from airport_codes import get_airport_code

load_dotenv()

# ─────────────────────────────────────────────
# 环境变量
# ─────────────────────────────────────────────
AMAP_KEY = os.getenv("AMAP_API_KEY", "")
VARIFLIGHT_KEY = os.getenv("VARIFLIGHT_API_KEY", "")
MCP_12306_URL = os.getenv("MCP_12306_URL", "")  # 可选：ModelScope SSE


def get_mcp_servers() -> dict:
    """构建 MCP 服务配置"""
    servers = {}

    # 高德地图 MCP（正确包名：@amap/amap-maps-mcp-server）
    if AMAP_KEY:
        servers["amap"] = {
            "command": "npx",
            "args": ["-y", "@amap/amap-maps-mcp-server"],
            "env": {"AMAP_MAPS_API_KEY": AMAP_KEY},
            "transport": "stdio",
        }
    else:
        print("[TravelAgent] 警告: AMAP_API_KEY 未设置，高德地图 MCP 已禁用")

    # 飞常准 MCP（stdio，已验证可用）
    if VARIFLIGHT_KEY:
        servers["variflight"] = {
            "command": "npx",
            "args": ["-y", "@variflight-ai/variflight-mcp"],
            "env": {"VARIFLIGHT_API_KEY": VARIFLIGHT_KEY},
            "transport": "stdio",
        }
    else:
        print("[TravelAgent] 警告: VARIFLIGHT_API_KEY 未设置，飞常准工具已禁用")

    # 12306 MCP（全局安装：npm install -g 12306-mcp）
    mcp_12306_path = os.path.expanduser(
        r"~\AppData\Roaming\npm\node_modules\12306-mcp\build\index.js"
    )
    if os.path.exists(mcp_12306_path):
        servers["12306"] = {
            "command": "node",
            "args": [mcp_12306_path],
            "transport": "stdio",
        }
    elif MCP_12306_URL:
        servers["12306"] = {"url": MCP_12306_URL, "transport": "sse"}
    else:
        print("[TravelAgent] 警告: 12306 MCP 未找到，请运行 npm install -g 12306-mcp")

    return servers


# ─────────────────────────────────────────────
# 系统提示词
# ─────────────────────────────────────────────
SYSTEM_PROMPT = """你是「旅行有样」平台的专业 AI 旅行规划师。

当用户发来旅行规划请求时，消息末尾会附带【通过工具实时查询到的真实数据】，包含天气预报、景点列表、餐厅推荐、住宿选择、火车票和航班信息。

你的任务：
1. 仔细阅读工具查询到的真实数据
2. 基于真实数据制定每日详细行程（景点、餐饮、住宿均取自真实查询结果）
3. 列出真实的交通方案（班次、时间、价格来自查询数据）
4. 给出基于真实数据的预算估算
5. 数据缺失的部分可用"（数据暂缺，仅供参考）"标注

对于普通问答或无工具数据的对话，直接用你的知识回答。

输出格式：Markdown，结构清晰，内容详尽实用。"""


# ─────────────────────────────────────────────
# TravelAgent 类
# ─────────────────────────────────────────────
class TravelAgent:
    def __init__(self):
        # 延迟初始化，避免启动时因缺少 API Key 崩溃
        self._llm = None
        self._agent = None
        self._mcp_clients: list = []  # 保持 MCP 客户端长期存活

    def _stringify_content(self, content: Any) -> str:
        """Normalize message content to plain text for Chat Completions."""
        if isinstance(content, str):
            return content
        if content is None:
            return ""
        if isinstance(content, dict):
            for key in ("text", "content", "value"):
                value = content.get(key)
                if isinstance(value, str):
                    return value
            try:
                return json.dumps(content, ensure_ascii=False)
            except TypeError:
                return str(content)
        if isinstance(content, list):
            parts = [self._stringify_content(part) for part in content]
            parts = [part for part in parts if part]
            if parts:
                return "\n".join(parts)
            try:
                return json.dumps(content, ensure_ascii=False)
            except TypeError:
                return str(content)
        return str(content)

    def _normalize_messages(self, messages: list[Any] | None) -> list[Any]:
        """Ensure all message content values are strings before model calls."""
        normalized: list[Any] = []
        for msg in messages or []:
            if isinstance(msg, BaseMessage):
                content = self._stringify_content(msg.content)
                if msg.content == content:
                    normalized.append(msg)
                else:
                    normalized.append(msg.model_copy(update={"content": content}, deep=True))
                continue

            if isinstance(msg, dict):
                cleaned = dict(msg)
                cleaned["content"] = self._stringify_content(cleaned.get("content"))
                normalized.append(cleaned)
                continue

            normalized.append(msg)
        return normalized

    def _normalize_tool_result(self, result: Any) -> Any:
        """Recursively normalize ToolNode outputs that may contain content blocks."""
        if isinstance(result, BaseMessage):
            return self._normalize_messages([result])[0]
        if isinstance(result, dict):
            cleaned = dict(result)
            for key, value in cleaned.items():
                if key == "messages" and isinstance(value, list):
                    cleaned[key] = self._normalize_messages(value)
                else:
                    cleaned[key] = self._normalize_tool_result(value)
            return cleaned
        if isinstance(result, list):
            return [self._normalize_tool_result(item) for item in result]
        return result

    def _extract_text_parts(self, content: Any) -> list[str]:
        """Extract displayable text fragments from model chunk content."""
        if isinstance(content, str):
            return [content] if content else []
        if isinstance(content, dict):
            text = content.get("text") or content.get("content") or content.get("value")
            return [text] if isinstance(text, str) and text else []
        if isinstance(content, list):
            parts: list[str] = []
            for item in content:
                parts.extend(self._extract_text_parts(item))
            return parts
        text = self._stringify_content(content)
        return [text] if text else []

    def _build_synthesis_messages(
        self,
        message: str,
        history: list[dict] | None,
        tool_data: str,
    ) -> list[BaseMessage]:
        """Build the final synthesis prompt from collected live travel data."""
        return [
            SystemMessage(content=SYSTEM_PROMPT),
            *self._build_messages(message, history)[:-1],
            HumanMessage(content=(
                f"{message}\n\n"
                f"---\n"
                f"【以下是通过工具实时查询到的真实数据，请严格基于这些数据规划行程，"
                f"不要编造班次/价格/景点等信息】\n\n"
                f"{tool_data}"
            )),
        ]

    async def _stream_llm_response(self, messages: list[BaseMessage]) -> AsyncIterator[str]:
        """Stream plain-text tokens from a direct LLM call."""
        async for chunk in self._get_llm().astream(messages):
            if hasattr(chunk, "content") and chunk.content:
                for text in self._extract_text_parts(chunk.content):
                    yield text

    def _get_llm(self) -> ChatOpenAI:
        """懒加载 LLM"""
        if self._llm is None:
            api_key = os.getenv("OPENAI_API_KEY", "")
            if not api_key:
                raise ValueError("未设置 OPENAI_API_KEY，请在 agent/.env 中配置")
            self._llm = ChatOpenAI(
                model=os.getenv("MODEL_NAME", "gpt-4o"),
                api_key=api_key,
                base_url=os.getenv("OPENAI_BASE_URL", "https://api.openai.com/v1"),
                temperature=0.7,
                streaming=True,
            )
        return self._llm

    async def _build_agent(self):
        """初始化 MCP 客户端并构建 ReAct Agent，逐个启动避免单点失败"""
        mcp_servers = get_mcp_servers()
        mcp_tools = []
        self._mcp_clients = []

        for name, config in mcp_servers.items():
            try:
                print(f"[TravelAgent] 正在启动 MCP 服务: {name}...")
                client = MultiServerMCPClient({name: config})
                tools = await client.get_tools()
                mcp_tools.extend(tools)
                self._mcp_clients.append(client)  # 保持引用，防止连接被 GC 回收
                print(f"[TravelAgent] ✓ {name} 启动成功，加载 {len(tools)} 个工具")
            except Exception as e:
                print(f"[TravelAgent] ✗ {name} 启动失败: {e}")

        all_tools = mcp_tools
        print(f"[TravelAgent] 共加载 {len(all_tools)} 个工具: {[t.name for t in all_tools]}")

        # 保存工具字典，供主动编排调用
        self._tool_map = {t.name: t for t in all_tools}

        # 创建自定义工具节点，确保 ToolMessage 的 content 在所有返回形态下都是字符串
        outer = self

        class CleanToolNode(ToolNode):
            """自定义 ToolNode，清理 ToolNode 返回结果中的消息 content。"""

            def invoke(self, input, config=None, **kwargs):
                result = super().invoke(input, config, **kwargs)
                return outer._normalize_tool_result(result)

            async def ainvoke(self, input, config=None, **kwargs):
                result = await super().ainvoke(input, config, **kwargs)
                return outer._normalize_tool_result(result)

        def _pre_model_hook(state: dict) -> dict:
            llm_input_messages = state.get("llm_input_messages") or state.get("messages") or []
            return {"llm_input_messages": outer._normalize_messages(llm_input_messages)}

        # handle_tool_errors=True：工具报错时返回错误信息给 LLM 让其重试，而非直接崩溃
        tool_node = CleanToolNode(all_tools, handle_tool_errors=True)

        self._agent = create_react_agent(
            model=self._get_llm(),
            tools=tool_node,
            prompt=SystemMessage(content=SYSTEM_PROMPT),
            pre_model_hook=_pre_model_hook,
        )

    async def ensure_ready(self):
        """确保 Agent 已初始化"""
        if self._agent is None:
            await self._build_agent()


    _MONTH_MAP: dict[str, int] = {
        "Jan": 1, "Feb": 2, "Mar": 3, "Apr": 4, "May": 5, "Jun": 6,
        "Jul": 7, "Aug": 8, "Sep": 9, "Oct": 10, "Nov": 11, "Dec": 12,
    }

    def _parse_travel_params(self, message: str) -> dict | None:
        """从结构化旅行规划消息中提取出发地/目的地/日期/天数"""
        trip_keywords = ("行程", "旅行", "旅游", "度假", "攻略", "规划", "休闲", "放松")
        if not any(keyword in message for keyword in trip_keywords) or not re.search(r"\d+\s*天", message):
            return None

        params: dict = {}

        # 出发地 + 目的地 + 天数
        m = re.search(r'从(.{2,12}?)(?:出发)?(?:到|去)(.{2,12}?)的?(\d+)天', message)
        if m:
            params["origin"] = m.group(1).strip("，,。 ")
            params["destination"] = m.group(2).strip("，,。 ")
            params["days"] = int(m.group(3))
        else:
            m = re.search(r'(?:到|去)(.{2,12}?)的?(\d+)天', message)
            if m:
                params["destination"] = m.group(1).strip("，,。 ")
                params["days"] = int(m.group(2))

        if "destination" not in params:
            return None

        # 日期：YYYY年M月D日 / YYYY-MM-DD
        m = re.search(r'(\d{4})[年\-/](\d{1,2})[月\-/](\d{1,2})', message)
        if m:
            params["date"] = f"{m.group(1)}-{m.group(2).zfill(2)}-{m.group(3).zfill(2)}"
        else:
            # “4月10日”格式：优先推断为最近的未来日期
            m = re.search(r'(?<!\d)(\d{1,2})[月\-/](\d{1,2})日?', message)
            if m:
                try:
                    today = date.today()
                    candidate = date(today.year, int(m.group(1)), int(m.group(2)))
                    if candidate < today:
                        candidate = date(today.year + 1, int(m.group(1)), int(m.group(2)))
                    params["date"] = candidate.isoformat()
                except ValueError:
                    pass

            # "Thu Mar 19 2026 ..." 格式（来自 JS Date.toString）
            m = re.search(r'[A-Z][a-z]{2} ([A-Z][a-z]{2}) (\d{1,2}) (\d{4})', message)
            if m and "date" not in params:
                month = self._MONTH_MAP.get(m.group(1), 0)
                if month:
                    params["date"] = f"{m.group(3)}-{month:02d}-{int(m.group(2)):02d}"

        return params

    async def _collect_travel_data(self, params: dict) -> str:
        """主动编排工具调用，确保天气/景点/火车/航班数据都被查询"""
        origin = params.get("origin", "")
        dest = params.get("destination", "")
        date_str = params.get("date", "")
        tm = getattr(self, "_tool_map", {})
        if not tm:
            return ""

        parts: list[str] = []
        # 每条工具结果保留的最大字符数，防止上下文过大导致 LLM 遗漏信息
        _MAX_CHARS = 3000

        async def _call(tool_name: str, args: dict, label: str) -> str | None:
            """调用工具，返回结果字符串；失败返回 None"""
            if tool_name not in tm:
                return None
            try:
                result = str(await tm[tool_name].ainvoke(args))
                if len(result) > _MAX_CHARS:
                    result = result[:_MAX_CHARS] + "\n...（内容过长已截断）"
                parts.append(f"### {label}\n{result}")
                print(f"[TravelAgent] ✓ 工具调用成功: {tool_name}")
                return result
            except Exception as e:
                print(f"[TravelAgent] ✗ 工具调用失败: {tool_name} → {e}")
                return None

        def _extract_station_code(raw: str | None) -> str | None:
            """从 get-station-code-of-citys 返回结果中提取第一个站点代码"""
            if not raw:
                return None
            # 尝试 JSON 解析：[{"name":"杭州","code":"HZH"}, ...]
            try:
                import json
                data = json.loads(raw)
                if isinstance(data, list) and data:
                    first = data[0]
                    return first.get("code") or first.get("station_code") or first.get("telecode")
                if isinstance(data, dict):
                    return data.get("code") or data.get("station_code") or data.get("telecode")
            except Exception:
                pass
            # 回退：正则匹配大写字母站点代码（如 HZH, DLY）
            m = re.search(r'\b([A-Z]{3})\b', raw)
            return m.group(1) if m else None

        # 1. 目的地天气
        dest_airport = get_airport_code(dest)
        if dest_airport:
            await _call("getFutureWeatherByAirport",
                        {"airport": dest_airport},
                        f"目的地天气预报（{dest}）")

        # 2. 景点 / 餐厅 / 酒店（并行）
        if dest and "maps_text_search" in tm:
            poi_tasks = [
                _call("maps_text_search", {"keywords": kw, "city": dest}, f"{dest}{label}")
                for kw, label in [("热门景点", "热门景点"), ("特色餐厅", "特色餐厅"), ("酒店", "住宿推荐")]
            ]
            await asyncio.gather(*poi_tasks)

        # 3. 火车票：先拿站点代码 → 查直达票 → 无直达则查中转票
        if origin and dest and date_str:
            async def _get_station_code(city: str, label: str) -> str | None:
                """查站点代码，失败后等待 0.5s 自动重试一次"""
                raw = await _call("get-station-code-of-citys", {"citys": city}, label)
                code = _extract_station_code(raw)
                if code:
                    return code
                # 重试
                await asyncio.sleep(0.5)
                raw2 = await _call("get-station-code-of-citys", {"citys": city}, f"{label}（重试）")
                return _extract_station_code(raw2)

            # 并行查两个城市的站点代码（避免与上方 POI 并发时序冲突）
            origin_code_r, dest_code_r = await asyncio.gather(
                _get_station_code(origin, f"出发站点（{origin}）"),
                _get_station_code(dest, f"到达站点（{dest}）"),
            )
            origin_code = origin_code_r or origin
            dest_code = dest_code_r or dest
            print(f"[TravelAgent] 解析站点代码: {origin}→{origin_code}, {dest}→{dest_code}")

            direct = await _call("get-tickets",
                                 {"fromStation": origin_code, "toStation": dest_code, "date": date_str},
                                 f"直达火车票（{origin}→{dest} {date_str}）")
            # 无直达时查中转票
            no_direct = not direct or any(
                kw in str(direct) for kw in ("没有", "未找到", "暂无", "0张", "[]")
            )
            if "get-interline-tickets" in tm and no_direct:
                await _call("get-interline-tickets",
                            {"fromStation": origin_code, "toStation": dest_code, "date": date_str},
                            f"中转火车票（{origin}→{dest} {date_str}）")

        # 4. 航班：必须使用 IATA 三字码，城市名会触发参数校验失败
        if origin and dest and date_str and "searchFlightsByDepArr" in tm:
            dep_code = get_airport_code(origin)
            arr_code = get_airport_code(dest)
            print(f"[TravelAgent] 航班查询: {origin!r}→{dep_code!r}, {dest!r}→{arr_code!r}")
            if dep_code and arr_code:
                await _call(
                    "searchFlightsByDepArr",
                    {"dep": dep_code, "arr": arr_code, "date": date_str},
                    f"航班（{origin} {dep_code}→{dest} {arr_code} {date_str}）"
                )
            else:
                missing = []
                if not dep_code:
                    missing.append(f"{origin}（出发地）")
                if not arr_code:
                    missing.append(f"{dest}（目的地）")
                parts.append(
                    f"### 航班查询\n"
                    f"以下城市暂无机场三字码映射，无法自动查询航班，请手动查询：{', '.join(missing)}"
                )
                print(f"[TravelAgent] ⚠️  航班查询跳过，缺少机场代码: {missing}")

        return "\n\n".join(parts)

    def _build_messages(self, message: str, history: list[dict] | None) -> list:
        """构建消息列表，返回 LangChain Message 对象"""
        msgs = []
        for msg in (history or []):
            if msg.get("role") in ("user", "assistant"):
                content = self._stringify_content(msg.get("content", ""))
                if msg["role"] == "user":
                    msgs.append(HumanMessage(content=content))
                else:
                    msgs.append(AIMessage(content=content))
        # 确保最后的消息也是字符串
        message_str = message if isinstance(message, str) else str(message)
        msgs.append(HumanMessage(content=message_str))
        return msgs

    def _build_messages_dict(self, message: str, history: list[dict] | None) -> list[dict]:
        """构建消息字典列表，用于 LLM API 调用"""
        msgs = []
        for msg in (history or []):
            if msg.get("role") in ("user", "assistant"):
                content = self._stringify_content(msg.get("content", ""))
                msgs.append({
                    "role": msg["role"],
                    "content": content
                })
        # 确保最后的消息也是字符串
        message_str = message if isinstance(message, str) else str(message)
        msgs.append({"role": "user", "content": message_str})
        return msgs

    # recursion_limit：每两步消耗一次（LLM调用 + 工具执行），25 步约支持 12 轮工具调用
    _AGENT_CONFIG = {"recursion_limit": 25}

    # 工具名模式：字母/数字/连字符/下划线，首字符为字母
    _TOOL_NAME_RE = re.compile(r'^[a-zA-Z][a-zA-Z0-9_\-]*$')

    def _is_text_tool_call(self, content: str) -> bool:
        """检测 AIMessage 的 content 是否为文本格式的工具调用（而非最终回答）。
        模型未正确使用 function calling 时，会把工具名+参数直接写进 content。
        格式示例：
            get-stations-code-of-citys
            {"citys": "杭州"}
        """
        if not content:
            return False
        lines = [l for l in content.strip().splitlines() if l.strip()]
        if len(lines) < 2:
            return False
        first = lines[0].strip()
        # 第一行匹配工具名，且后续含 JSON 对象
        if self._TOOL_NAME_RE.match(first):
            rest = '\n'.join(lines[1:]).strip()
            if rest.startswith('{') or rest.startswith('['):
                return True
        return False

    async def _invoke_with_retry(self, messages: list, max_retries: int = 3) -> dict:
        """带指数退避重试的 agent 调用，专门应对 429 速率限制"""
        for attempt in range(max_retries):
            try:
                return await self._agent.ainvoke(
                    {"messages": messages},
                    config=self._AGENT_CONFIG,
                )
            except RateLimitError as e:
                if attempt < max_retries - 1:
                    wait = 5 * (2 ** attempt)  # 5s → 10s → 20s
                    print(f"[TravelAgent] ⚠️  速率限制 (429)，{wait}s 后重试 (第{attempt+1}次)...")
                    await asyncio.sleep(wait)
                else:
                    print(f"[TravelAgent] ❌ 速率限制重试耗尽: {e}")
                    raise
            except Exception as e:
                print(f"[TravelAgent] ❌ agent.ainvoke 异常: {e}")
                traceback.print_exc()
                raise

    async def chat(
        self,
        message: str,
        history: list[dict] | None = None,
        session_id: str | None = None,
    ) -> str:
        """普通对话（非流式）"""
        await self.ensure_ready()
        print(f"[TravelAgent] 收到消息: {message[:50]}...")

        # 检测旅行规划请求，主动编排工具调用，不依赖 LLM 自主决策
        travel_params = self._parse_travel_params(message)
        if travel_params:
            print(f"[TravelAgent] 检测到旅行规划请求: {travel_params}")
            tool_data = await self._collect_travel_data(travel_params)
            if tool_data:
                # 工具数据已就绪：直接调 LLM 合成，跳过 ReAct Agent
                # 避免 Agent 拿到数据后再重复调工具，浪费 token / 触发限速
                print(f"[TravelAgent] 已注入工具数据（{len(tool_data)} 字符），直接合成...")
                synthesis_messages = self._build_synthesis_messages(message, history, tool_data)
                resp = await self._get_llm().ainvoke(synthesis_messages)
                content = self._stringify_content(getattr(resp, "content", ""))
                return content if content else "抱歉，我无法生成回复。"

        # 普通对话：走 ReAct Agent（可自主调工具）
        messages = self._build_messages(message, history)
        try:
            result = await self._invoke_with_retry(messages)
        except GraphRecursionError:
            print("[TravelAgent] 检测到 ReAct 递归超限，回退到直接回答模式")
            fallback = await self._get_llm().ainvoke(messages)
            content = self._stringify_content(getattr(fallback, "content", ""))
            return content if content else "抱歉，我无法生成回复。"
        for m in result["messages"]:
            print(f"[TravelAgent] 消息类型: {type(m).__name__}, content长度: {len(str(m.content))}")
            if hasattr(m, 'tool_calls') and m.tool_calls:
                print(f"[TravelAgent] 工具调用: {[tc['name'] for tc in m.tool_calls]}")
        ai_msgs = [
            m for m in result["messages"]
            if isinstance(m, AIMessage)
            and m.content
            and not m.tool_calls
            and not self._is_text_tool_call(str(m.content))
        ]
        if ai_msgs:
            return self._stringify_content(ai_msgs[-1].content)
        print("[TravelAgent] ⚠️  未找到最终回答，尝试兜底生成...")
        fallback = await self._get_llm().ainvoke(messages)
        content = self._stringify_content(getattr(fallback, "content", ""))
        return content if content else "抱歉，我无法生成回复。"

    async def chat_stream(
        self,
        message: str,
        history: list[dict] | None = None,
        session_id: str | None = None,
    ) -> AsyncIterator[str]:
        """流式对话，逐 token 输出"""
        await self.ensure_ready()

        travel_params = self._parse_travel_params(message)
        if travel_params:
            print(f"[TravelAgent] 流式旅行规划请求: {travel_params}")
            tool_data = await self._collect_travel_data(travel_params)
            if tool_data:
                print(f"[TravelAgent] 流式模式已注入工具数据（{len(tool_data)} 字符），直接合成...")
                synthesis_messages = self._build_synthesis_messages(message, history, tool_data)
                async for token in self._stream_llm_response(synthesis_messages):
                    yield token
                return

        messages = self._build_messages_dict(message, history)
        
        # 调试：打印消息格式
        print(f"[TravelAgent] 流式对话消息数: {len(messages)}")
        for i, msg in enumerate(messages):
            content_type = type(msg.get("content")).__name__
            content_len = len(str(msg.get("content", "")))
            print(f"[TravelAgent]   messages[{i}]: role={msg.get('role')}, content_type={content_type}, len={content_len}")

        yielded_any = False
        try:
            async for event in self._agent.astream_events(
                {"messages": messages},
                config=self._AGENT_CONFIG,
                version="v2",
            ):
                kind = event["event"]
                if kind == "on_chat_model_stream":
                    chunk = event["data"]["chunk"]
                    if hasattr(chunk, "content") and chunk.content:
                        for text in self._extract_text_parts(chunk.content):
                            yielded_any = True
                            yield text
        except GraphRecursionError:
            print("[TravelAgent] 流式 ReAct 递归超限，回退到直接回答模式")
            if not yielded_any:
                fallback_messages = self._build_messages(message, history)
                async for token in self._stream_llm_response(fallback_messages):
                    yield token
                return
            raise
        except Exception as e:
            # 如果是 ToolMessage content 类型错误，尝试重新调用但先清理消息
            if "invalid type: sequence" in str(e):
                print(f"[TravelAgent] 检测到 ToolMessage 格式错误，尝试修复...")
                # 这里无法直接修复，因为错误发生在 LLM API 层
                # 最好的办法是让 LLM 不要调用工具，或者用其他方式处理
                raise
            raise

    async def close(self):
        """关闭资源"""
        self._mcp_clients = []


# ─────────────────────────────────────────────
# 单例
# ─────────────────────────────────────────────
_agent_instance: TravelAgent | None = None


def get_agent() -> TravelAgent:
    global _agent_instance
    if _agent_instance is None:
        _agent_instance = TravelAgent()
    return _agent_instance


# ─────────────────────────────────────────────
# 本地命令行测试
# ─────────────────────────────────────────────
async def _cli_test():
    agent = get_agent()
    print("旅行有样 AI 助手已启动（输入 quit 退出）")
    history = []
    while True:
        user_input = input("\n你：").strip()
        if not user_input or user_input.lower() == "quit":
            break
        print("\nAI：", end="", flush=True)
        full = ""
        async for token in agent.chat_stream(user_input, history):
            print(token, end="", flush=True)
            full += token
        print()
        history.append({"role": "user", "content": user_input})
        history.append({"role": "assistant", "content": full})
    await agent.close()


if __name__ == "__main__":
    asyncio.run(_cli_test())
