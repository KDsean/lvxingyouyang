import { http } from '../request'
import type { ApiResponse, PageResponse, Destination, DestinationHeatmapPoint } from '@/types'

// 获取目的地列表
export const getDestinations = (params: {
  country?: string
  tag?: string
  season?: string
  page?: number
  pageSize?: number
}) => {
  return http.get<ApiResponse<PageResponse<Destination>>>('/destinations', { params })
}

// 获取目的地详情
export const getDestinationDetail = (id: string) => {
  return http.get<ApiResponse<Destination>>(`/destinations/${id}`)
}

// 获取热门目的地
export const getPopularDestinations = (limit: number = 10) => {
  return http.get<ApiResponse<Destination[]>>('/destinations/popular', { params: { limit } })
}

// 搜索目的地
export const searchDestinations = (params: { keyword?: string; region?: string }) => {
  return http.get<ApiResponse<Destination[]>>('/destinations/search', { params })
}

// 获取目的地热力图数据
export const getDestinationHeatmap = () => {
  return http.get<ApiResponse<DestinationHeatmapPoint[]>>('/destinations/heatmap')
}
