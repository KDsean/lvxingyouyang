import { http } from '../request'
import type { ApiResponse, PageResponse, Restaurant } from '@/types'

// 搜索餐厅
export const searchRestaurants = (params: {
  city?: string
  cuisine?: string
  priceLevel?: number
  keyword?: string
  page?: number
  pageSize?: number
}) => {
  return http.get<ApiResponse<PageResponse<Restaurant>>>('/restaurants/search', { params })
}

// 获取餐厅详情
export const getRestaurantDetail = (id: string) => {
  return http.get<ApiResponse<Restaurant>>(`/restaurants/${id}`)
}

// 获取推荐餐厅
export const getRecommendedRestaurants = (city?: string) => {
  return http.get<ApiResponse<Restaurant[]>>('/restaurants/recommended', { params: { city } })
}

// 获取热门餐厅
export const getPopularRestaurants = (city?: string) => {
  return http.get<ApiResponse<Restaurant[]>>('/restaurants/popular', { params: { city } })
}

// 预订餐厅
export const bookRestaurant = (data: {
  restaurantId: string
  date: string
  time: string
  guests: number
  contactName: string
  contactPhone: string
  specialRequests?: string
}) => {
  return http.post<ApiResponse<{ orderId: string }>>('/restaurants/book', data)
}
