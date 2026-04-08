import { http } from '../request'
import type { ApiResponse, PageResponse, TripPlan, Order } from '@/types'

// 获取我的行程列表
export const getMyTrips = (params?: { page?: number; pageSize?: number }) => {
  return http.get<ApiResponse<PageResponse<TripPlan>>>('/trips', { params })
}

// 获取行程详情
export const getTripDetail = (id: string) => {
  return http.get<ApiResponse<TripPlan>>(`/trips/${id}`)
}

// 创建行程
export const createTrip = (data: Partial<TripPlan>) => {
  return http.post<ApiResponse<{ id: string }>>('/trips', data)
}

// 更新行程
export const updateTrip = (id: string, data: Partial<TripPlan>) => {
  return http.put<ApiResponse<TripPlan>>(`/trips/${id}`, data)
}

// 删除行程
export const deleteTrip = (id: string) => {
  return http.delete<ApiResponse>(`/trips/${id}`)
}

// 获取我的订单
export const getMyOrders = (params?: {
  type?: string
  status?: string
  page?: number
  pageSize?: number
}) => {
  return http.get<ApiResponse<PageResponse<Order>>>('/orders', { params })
}

// 获取订单详情
export const getOrderDetail = (id: string) => {
  return http.get<ApiResponse<Order>>(`/orders/${id}`)
}

// 取消订单
export const cancelOrder = (id: string) => {
  return http.post<ApiResponse>(`/orders/${id}/cancel`)
}
