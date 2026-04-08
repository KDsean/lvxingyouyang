import { http } from '../request'
import type { ApiResponse, PageResponse, CarRental } from '@/types'

// 搜索租车
export const searchCars = (params: {
  location: string
  pickupDate: string
  returnDate: string
  carType?: string
  transmission?: string
  page?: number
  pageSize?: number
}) => {
  return http.get<ApiResponse<PageResponse<CarRental>>>('/cars/search', { params })
}

// 获取租车详情
export const getCarDetail = (id: string) => {
  return http.get<ApiResponse<CarRental>>(`/cars/${id}`)
}

// 预订租车
export const bookCar = (data: {
  carId: string
  pickupDate: string
  pickupTime: string
  returnDate: string
  returnTime: string
  pickupLocation: string
  returnLocation: string
  driverName: string
  driverLicense: string
  driverPhone: string
}) => {
  return http.post<ApiResponse<{ orderId: string }>>('/cars/book', data)
}

// 获取热门租车地点
export const getPopularLocations = () => {
  return http.get<ApiResponse<Array<{ city: string; count: number }>>>('/cars/popular-locations')
}
