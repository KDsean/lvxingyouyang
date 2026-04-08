import { http } from '../request'
import type { ApiResponse, PageResponse, Attraction } from '@/types'

// 搜索景点
export const searchAttractions = (params: {
  city?: string
  type?: string
  keyword?: string
  page?: number
  pageSize?: number
}) => {
  return http.get<ApiResponse<PageResponse<Attraction>>>('/attractions/search', { params })
}

// 获取景点详情
export const getAttractionDetail = (id: string) => {
  return http.get<ApiResponse<Attraction>>(`/attractions/${id}`)
}

// 购买门票
export const buyTicket = (data: {
  attractionId: string
  visitDate: string
  ticketType: string
  quantity: number
  visitorName: string
  visitorPhone: string
  visitorIdCard: string
}) => {
  return http.post<ApiResponse<{ orderId: string }>>('/attractions/buy-ticket', data)
}

// 获取热门景点
export const getPopularAttractions = (city?: string) => {
  return http.get<ApiResponse<Attraction[]>>('/attractions/popular', { params: { city } })
}

// 获取推荐景点
export const getRecommendedAttractions = (limit: number = 10) => {
  return http.get<ApiResponse<Attraction[]>>('/attractions/recommended', { params: { limit } })
}
