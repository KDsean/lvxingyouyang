import { http } from '../request'
import type { ApiResponse, PageResponse, Hotel, HotelSearchParams } from '@/types'

// 搜索酒店
export const searchHotels = (params: HotelSearchParams) => {
  return http.get<ApiResponse<PageResponse<Hotel>>>('/hotels/search', { params })
}

// 获取酒店详情
export const getHotelDetail = (id: string) => {
  return http.get<ApiResponse<Hotel>>(`/hotels/${id}`)
}

// 获取推荐酒店
export const getRecommendedHotels = (limit: number = 10) => {
  return http.get<ApiResponse<Hotel[]>>('/hotels/recommended', { params: { limit } })
}

// 获取热门酒店
export const getPopularHotels = (city?: string) => {
  return http.get<ApiResponse<Hotel[]>>('/hotels/popular', { params: { city } })
}

// 获取特价酒店
export const getSpecialOfferHotels = () => {
  return http.get<ApiResponse<Hotel[]>>('/hotels/special-offers')
}

// 预订酒店
export const bookHotel = (data: {
  hotelId: string
  checkIn: string
  checkOut: string
  guests: number
  roomType: string
  contactName: string
  contactPhone: string
}) => {
  return http.post<ApiResponse<{ orderId: string }>>('/hotels/book', data)
}
