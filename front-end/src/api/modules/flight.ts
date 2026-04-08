import { http } from '../request'
import type { ApiResponse, PageResponse, Flight } from '@/types'

// 搜索航班
export const searchFlights = (params: {
  from: string
  to: string
  date: string
  returnDate?: string
  passengers: number
  cabinClass?: string
  page?: number
  pageSize?: number
}) => {
  return http.get<ApiResponse<PageResponse<Flight>>>('/flights/search', { params })
}

// 获取航班详情
export const getFlightDetail = (id: string) => {
  return http.get<ApiResponse<Flight>>(`/flights/${id}`)
}

// 预订机票
export const bookFlight = (data: {
  flightId: string
  passengers: Array<{
    name: string
    idCard: string
    phone: string
  }>
  contactName: string
  contactPhone: string
  contactEmail: string
}) => {
  return http.post<ApiResponse<{ orderId: string }>>('/flights/book', data)
}

// 获取热门航线
export const getPopularRoutes = () => {
  return http.get<ApiResponse<Array<{ from: string; to: string; price: number }>>>('/flights/popular-routes')
}
