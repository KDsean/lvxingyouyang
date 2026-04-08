import type { ApiResponse } from '@/types'

// 非流式走 Spring Boot 代理（处理认证、日志等）
const AI_BASE_URL = import.meta.env.VITE_AI_API_URL || 'http://localhost:8080/api/ai'
// 流式直连 Python，避免 Spring Boot SSE 代理的格式问题
const AI_STREAM_URL = import.meta.env.VITE_AI_STREAM_URL || 'http://localhost:8001'

/**
 * 普通 POST 对话（非流式）
 * 走 Spring Boot → Python，返回包装格式自动解包
 */
export const chat = async (params: {
  message: string
  history?: Array<{ role: string; content: string }>
  sessionId?: string
}): Promise<{ content: string; sessionId?: string }> => {
  const res = await fetch(`${AI_BASE_URL}/chat`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(params)
  })
  if (!res.ok) throw new Error(`AI 服务请求失败: ${res.status}`)
  const json = await res.json()
  // Spring Boot 包装格式：{code, message, data: {content}} → 取 data
  // Python 直连格式：{content} → 直接用
  return json?.data ?? json
}

/**
 * SSE 流式对话（直连 Python 8001，避免 Spring Boot 双重 data: 问题）
 * @returns AbortController，可调用 abort() 中止流
 */
export const chatStream = (
  params: {
    message: string
    history?: Array<{ role: string; content: string }>
    sessionId?: string
  },
  onChunk: (chunk: string) => void,
  onDone: (fullText: string) => void,
  onError: (error: Error) => void
): AbortController => {
  const controller = new AbortController()
  let fullText = ''

  fetch(`${AI_STREAM_URL}/chat/stream`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(params),
    signal: controller.signal
  })
    .then(async (res) => {
      if (!res.ok) throw new Error(`AI 流式请求失败: ${res.status}`)
      if (!res.body) throw new Error('响应体为空')

      const reader = res.body.getReader()
      const decoder = new TextDecoder('utf-8')
      let buffer = ''

      while (true) {
        const { done, value } = await reader.read()
        if (done) break

        buffer += decoder.decode(value, { stream: true })
        const lines = buffer.split('\n')
        // 最后一行可能不完整，保留到下次
        buffer = lines.pop() ?? ''

        for (const line of lines) {
          const trimmed = line.trim()
          if (!trimmed || !trimmed.startsWith('data:')) continue

          // 兼容 "data: " 和 "data:" 两种格式
          const data = trimmed.replace(/^data:\s*/, '').trim()
          if (!data || data === '[DONE]') {
            if (data === '[DONE]') {
              onDone(fullText)
              return
            }
            continue
          }

          try {
            const parsed = JSON.parse(data)
            const chunk = parsed.content ?? parsed.text ?? parsed.token ?? ''
            if (chunk) {
              fullText += chunk
              onChunk(chunk)
            }
          } catch {
            // 非 JSON，直接当文本
            fullText += data
            onChunk(data)
          }
        }
      }
      onDone(fullText)
    })
    .catch((err) => {
      if (err.name !== 'AbortError') {
        onError(err)
      }
    })

  return controller
}

/**
 * 快速生成行程（表单方式，非流式）
 */
export const generatePlan = async (params: {
  origin?: string
  destination: string
  days: number
  budget: string
  startDate?: string
  interests?: string[]
}): Promise<{ content: string }> => {
  const originPart = params.origin ? `从${params.origin}出发，` : ''
  const message = `请帮我规划一个${originPart}去${params.destination}的${params.days}天旅行行程。预算：${params.budget}元。${
    params.startDate ? `出发日期：${params.startDate}。` : ''
  }${params.interests?.length ? `兴趣偏好：${params.interests.join('、')}。` : ''}请提供详细的每日行程安排、推荐景点、美食和住宿建议。`

  return chat({
    message,
    history: []
  })
}
