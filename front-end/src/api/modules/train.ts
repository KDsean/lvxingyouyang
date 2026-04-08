import { http } from '../request'
import type { ApiResponse, PageResponse, Train } from '@/types'

// 搜索火车票
export const searchTrains = (params: {
  from: string
  to: string
  date: string
  page?: number
  pageSize?: number
}) => {
  return http.get<ApiResponse<PageResponse<Train>>>('/trains/search', { params })
}

// 获取火车票详情
export const getTrainDetail = (id: string) => {
  return http.get<ApiResponse<Train>>(`/trains/${id}`)
}

// 预订火车票
export const bookTrain = (data: {
  trainId: string
  seatType: string
  passengers: Array<{
    name: string
    idCard: string
    phone: string
  }>
  contactName: string
  contactPhone: string
}) => {
  return http.post<ApiResponse<{ orderId: string }>>('/trains/book', data)
}

// 获取车站列表
export const getStations = (keyword?: string) => {
  return http.get<ApiResponse<Array<{ code: string; name: string; city: string }>>>('/trains/stations', {
    params: { keyword }
  })
}
