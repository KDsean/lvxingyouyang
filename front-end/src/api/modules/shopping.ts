import { http } from '../request'
import type { ApiResponse, PageResponse, Product } from '@/types'

// 获取商品列表
export const getProducts = (params: {
  category?: string
  keyword?: string
  minPrice?: number
  maxPrice?: number
  sortBy?: 'price' | 'sales' | 'latest'
  page?: number
  pageSize?: number
}) => {
  return http.get<ApiResponse<PageResponse<Product>>>('/products', { params })
}

// 获取商品详情
export const getProductDetail = (id: string) => {
  return http.get<ApiResponse<Product>>(`/products/${id}`)
}

// 获取热门商品
export const getPopularProducts = (limit: number = 10) => {
  return http.get<ApiResponse<Product[]>>('/products/popular', { params: { limit } })
}

// 获取推荐商品
export const getRecommendedProducts = (limit: number = 10) => {
  return http.get<ApiResponse<Product[]>>('/products/recommended', { params: { limit } })
}

// 添加到购物车
export const addToCart = (data: { productId: string; quantity: number }) => {
  return http.post<ApiResponse>('/cart/add', data)
}

// 获取购物车
export const getCart = () => {
  return http.get<ApiResponse<Array<{ product: Product; quantity: number }>>>('/cart')
}

// 创建订单
export const createOrder = (data: {
  items: Array<{ productId: string; quantity: number }>
  shippingAddress: {
    name: string
    phone: string
    address: string
  }
}) => {
  return http.post<ApiResponse<{ orderId: string }>>('/orders/create', data)
}
