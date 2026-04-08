import { http } from '../request'
import type { ApiResponse, PageResponse, Guide } from '@/types'

// 获取攻略列表
export const getGuides = (params: {
  destination?: string
  tag?: string
  sortBy?: 'latest' | 'popular' | 'hot'
  page?: number
  pageSize?: number
}) => {
  return http.get<ApiResponse<PageResponse<Guide>>>('/guides', { params })
}

// 获取攻略详情
export const getGuideDetail = (id: string) => {
  return http.get<ApiResponse<Guide>>(`/guides/${id}`)
}

// 发布攻略
export const publishGuide = (data: {
  title: string
  destination: string
  content: string
  images: string[]
  tags: string[]
}) => {
  return http.post<ApiResponse<{ id: string }>>('/guides', data)
}

// 点赞攻略
export const likeGuide = (id: string) => {
  return http.post<ApiResponse>(`/guides/${id}/like`)
}

// 获取热门攻略
export const getPopularGuides = (limit: number = 10) => {
  return http.get<ApiResponse<Guide[]>>('/guides/popular', { params: { limit } })
}

// 搜索攻略
export const searchGuides = (params: {
  keyword?: string
  destination?: string
  tag?: string
  sortBy?: string
  page?: number
  pageSize?: number
}) => {
  return http.get<ApiResponse<Guide[]>>('/guides/search', { params })
}
