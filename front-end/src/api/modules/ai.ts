import axios from 'axios'
import type { ApiResponse, AITripRequest, AITripResponse, AIMessage } from '@/types'

const AI_API_URL = import.meta.env.VITE_AI_API_URL || '/ai'

// AI 行程规划对话
export const chatWithAI = async (data: AITripRequest): Promise<ApiResponse<AITripResponse>> => {
  const response = await axios.post(`${AI_API_URL}/chat`, data, {
    headers: {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${localStorage.getItem('token')}`
    }
  })
  return response.data
}

// AI 流式对话（支持 SSE）
export const streamChatWithAI = (
  data: AITripRequest,
  onMessage: (chunk: string) => void,
  onComplete: () => void,
  onError: (error: Error) => void
) => {
  const token = localStorage.getItem('token')
  
  fetch(`${AI_API_URL}/chat/stream`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    },
    body: JSON.stringify(data)
  })
    .then(response => {
      if (!response.ok) {
        throw new Error('Network response was not ok')
      }
      
      const reader = response.body?.getReader()
      const decoder = new TextDecoder()
      
      const readStream = () => {
        reader?.read().then(({ done, value }) => {
          if (done) {
            onComplete()
            return
          }
          
          const chunk = decoder.decode(value, { stream: true })
          onMessage(chunk)
          readStream()
        }).catch(onError)
      }
      
      readStream()
    })
    .catch(onError)
}

// 生成行程计划
export const generateTripPlan = (data: {
  destination: string
  days: number
  budget: number
  interests: string[]
  startDate?: string
}) => {
  return axios.post<ApiResponse<any>>(`${AI_API_URL}/generate-plan`, data, {
    headers: {
      'Authorization': `Bearer ${localStorage.getItem('token')}`
    }
  }).then(res => res.data)
}

// 优化行程
export const optimizeTripPlan = (data: {
  planId: string
  preferences: string[]
}) => {
  return axios.post<ApiResponse<any>>(`${AI_API_URL}/optimize-plan`, data, {
    headers: {
      'Authorization': `Bearer ${localStorage.getItem('token')}`
    }
  }).then(res => res.data)
}
