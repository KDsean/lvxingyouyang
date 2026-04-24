# airport_codes.py
# 运行时按需从 skills/airport_codes.json 加载机场三字码
# 首次调用时读文件，后续使用缓存，无需重启服务即可生效新增条目

import json
from pathlib import Path

_SKILL_FILE = Path(__file__).parent / "skills" / "airport_codes.json"

_cache: dict[str, str] | None = None


def normalize_city_name(city: str | None) -> str:
    """去掉自然语言里的出发/到达修饰词，保留城市名用于查代码。"""
    if not city:
        return ""
    normalized = str(city).strip(" \t\r\n，,。.!！？?；;：:")
    normalized = normalized.removeprefix("从").removeprefix("由")
    for suffix in ("出发地", "目的地", "出发", "到达", "抵达", "机场", "市"):
        if normalized.endswith(suffix):
            normalized = normalized[: -len(suffix)]
    return normalized.strip(" \t\r\n，,。.!！？?；;：:")


def _load() -> dict[str, str]:
    """读取 JSON skill 文件，将分区嵌套结构展开为 {城市: 三字码} 平铺字典。"""
    global _cache
    if _cache is not None:
        return _cache

    with open(_SKILL_FILE, encoding="utf-8") as f:
        raw = json.load(f)

    flat: dict[str, str] = {}
    for key, value in raw.items():
        if key.startswith("_"):   # 跳过注释字段
            continue
        if isinstance(value, dict):
            flat.update(value)    # 展开分区
        elif isinstance(value, str):
            flat[key] = value     # 顶层条目（兼容旧格式）

    _cache = flat
    print(f"[AirportCodes] 已加载 {len(_cache)} 个城市机场代码（来源: {_SKILL_FILE}）")
    return _cache


def get_airport_code(city: str) -> str | None:
    """根据城市名返回 IATA 机场三字码，未收录返回 None。"""
    return _load().get(normalize_city_name(city))


def reload() -> None:
    """强制重新读取文件（热更新，无需重启服务）。"""
    global _cache
    _cache = None
    _load()
