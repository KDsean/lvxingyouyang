# server.py
# FastAPI Web 服务，为 Spring Boot 提供 AI 接口
# 运行: uvicorn server:app --host 0.0.0.0 --port 8001

import os
import json
import traceback
from contextlib import asynccontextmanager
from typing import AsyncIterator

from fastapi import FastAPI, HTTPException
from fastapi.responses import StreamingResponse
from fastapi.middleware.cors import CORSMiddleware
from pydantic import BaseModel

from travel_agent import get_agent, TravelAgent


# ─────────────────────────────────────────────
# 请求/响应模型
# ─────────────────────────────────────────────
class ChatRequest(BaseModel):
    message: str
    history: list[dict] | None = None
    sessionId: str | None = None


class ChatResponse(BaseModel):
    content: str
    sessionId: str | None = None


# ─────────────────────────────────────────────
# 应用生命周期：启动时不预热，等首次请求时再初始化
# ─────────────────────────────────────────────
@asynccontextmanager
async def lifespan(app: FastAPI) -> AsyncIterator[None]:
    port = os.getenv("PORT", "8001")
    print(f"[Server] 旅行有样 AI 服务已启动，监听 http://0.0.0.0:{port}")
    print("[Server] TravelAgent 将在首次请求时初始化（需配置 .env）")
    yield
    # 关闭时释放 MCP 连接
    agent = get_agent()
    await agent.close()
    print("[Server] TravelAgent 已关闭")


# ─────────────────────────────────────────────
# FastAPI 应用
# ─────────────────────────────────────────────
app = FastAPI(
    title="旅行有样 AI 服务",
    description="基于 LangChain + LangGraph + MCP 的旅行规划 AI Agent",
    version="1.0.0",
    lifespan=lifespan,
)

app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_methods=["*"],
    allow_headers=["*"],
)


# ─────────────────────────────────────────────
# GET /health  健康检查
# ─────────────────────────────────────────────
@app.get("/health")
async def health():
    return {"status": "ok", "service": "travel-agent"}


# ─────────────────────────────────────────────
# POST /chat  普通对话（非流式）
# ─────────────────────────────────────────────
@app.post("/chat", response_model=ChatResponse)
async def chat(req: ChatRequest):
    agent: TravelAgent = get_agent()
    try:
        content = await agent.chat(
            message=req.message,
            history=req.history,
            session_id=req.sessionId,
        )
        return ChatResponse(content=content, sessionId=req.sessionId)
    except ValueError as e:
        raise HTTPException(status_code=503, detail=str(e))
    except Exception as e:
        traceback.print_exc()
        msg = str(e)
        if "429" in msg or "rate" in msg.lower() or "速率" in msg:
            raise HTTPException(status_code=429, detail="AI 服务请求频率超限，请稍后再试")
        raise HTTPException(status_code=500, detail=f"AI 服务异常: {msg}")


# ─────────────────────────────────────────────
# POST /chat/stream  流式对话（SSE）
# 返回格式: text/event-stream
#   data: {"content": "token"}
#   data: [DONE]
# ─────────────────────────────────────────────
@app.post("/chat/stream")
async def chat_stream(req: ChatRequest):
    agent: TravelAgent = get_agent()

    async def event_generator() -> AsyncIterator[str]:
        try:
            print(f"[Server] 流式请求: message={req.message[:50]}..., history_len={len(req.history or [])}")
            async for token in agent.chat_stream(
                message=req.message,
                history=req.history,
                session_id=req.sessionId,
            ):
                payload = json.dumps({"content": token}, ensure_ascii=False)
                yield f"data: {payload}\n\n"
        except ValueError as e:
            print(f"[Server] ValueError: {e}")
            err = json.dumps({"error": str(e)}, ensure_ascii=False)
            yield f"data: {err}\n\n"
        except Exception as e:
            print(f"[Server] Exception: {type(e).__name__}: {e}")
            traceback.print_exc()
            err = json.dumps({"error": f"AI 服务异常: {str(e)}"}, ensure_ascii=False)
            yield f"data: {err}\n\n"
        finally:
            yield "data: [DONE]\n\n"

    return StreamingResponse(
        event_generator(),
        media_type="text/event-stream",
        headers={
            "Cache-Control": "no-cache",
            "X-Accel-Buffering": "no",
        },
    )


# ─────────────────────────────────────────────
# 直接运行
# ─────────────────────────────────────────────
if __name__ == "__main__":
    import uvicorn
    port = int(os.getenv("PORT", 8001))
    uvicorn.run("server:app", host="0.0.0.0", port=port, reload=False)
