// 通用响应类型
export interface ApiResponse<T = any> {
  code: number
  message: string
  data: T
}

// 分页参数
export interface PageParams {
  page: number
  pageSize: number
}

// 分页响应
export interface PageResponse<T> {
  list: T[]
  total: number
  page: number
  pageSize: number
}

// 用户类型
export interface User {
  id: string
  username: string
  email: string
  phone?: string
  avatar?: string
  createdAt: string
}

// 酒店类型
export interface Hotel {
  id: string
  name: string
  location: string
  city: string
  address: string
  rating: number
  reviewCount: number
  price: number
  images: string[]
  description: string
  facilities: string[]
  starLevel: number
  tags: string[]
}

// 酒店搜索参数
export interface HotelSearchParams extends PageParams {
  destination?: string
  checkIn?: string
  checkOut?: string
  guests?: number
  minPrice?: number
  maxPrice?: number
  starLevel?: number[]
  facilities?: string[]
}

// 目的地类型
export interface Destination {
  id: string
  name: string
  country: string
  description: string
  image: string
  popularityScore: number
  tags: string[]
  bestSeason: string[]
  heatCount?: number
  heatRank?: number
}

export interface DestinationHeatmapPoint {
  id: string
  destination: string
  country: string
  count: number
  lat: number
  lng: number
  score: number
}

// 机票类型
export interface Flight {
  id: string
  airline: string
  flightNumber: string
  departure: {
    airport: string
    city: string
    time: string
  }
  arrival: {
    airport: string
    city: string
    time: string
  }
  price: number
  cabinClass: 'economy' | 'business' | 'first'
  availableSeats: number
}

// 火车票类型
export interface Train {
  id: string
  trainNumber: string
  departure: {
    station: string
    city: string
    time: string
  }
  arrival: {
    station: string
    city: string
    time: string
  }
  duration: string
  seatTypes: {
    type: string
    price: number
    available: number
  }[]
}

// 租车类型
export interface CarRental {
  id: string
  brand: string
  model: string
  type: string
  seats: number
  transmission: 'auto' | 'manual'
  pricePerDay: number
  image: string
  features: string[]
  location: string
}

// 景点类型
export interface Attraction {
  id: string
  name: string
  city: string
  type: string
  rating: number
  price: number
  openTime: string
  description: string
  images: string[]
  tags: string[]
}

// 攻略类型
export interface Guide {
  id: string
  title: string
  destination: string
  author: {
    id: string
    name: string
    avatar: string
  }
  content: string
  images: string[]
  tags: string[]
  viewCount: number
  likeCount: number
  createdAt: string
}

// 美食类型
export interface Restaurant {
  id: string
  name: string
  cuisine: string
  city: string
  rating: number
  priceLevel: number
  address: string
  images: string[]
  specialties: string[]
  openHours: string
}

// 商品类型
export interface Product {
  id: string
  name: string
  category: string
  price: number
  originalPrice?: number
  image: string
  description: string
  stock: number
  sales: number
  tags: string[]
}

// AI 对话类型
export interface AIMessage {
  role: 'user' | 'assistant'
  content: string
  timestamp: string
}

export interface AITripRequest {
  message: string
  context?: AIMessage[]
}

export interface AITripResponse {
  reply: string
  suggestions?: {
    destinations?: string[]
    hotels?: Hotel[]
    attractions?: Attraction[]
  }
}

// 行程规划类型（与后端 TripPlan 实体对应）
export interface TripPlan {
  id: string
  userId?: string
  title: string
  origin?: string
  destination: string
  days?: number
  startDate?: string
  endDate?: string
  budget?: number
  interests?: string    // 逗号分隔，如 "美食,历史,户外"
  planDetails?: string
  createdAt: string
  updatedAt?: string
}

// 订单类型
export interface Order {
  id: string
  userId: string
  type: 'hotel' | 'flight' | 'train' | 'car' | 'attraction' | 'product'
  itemId: string
  itemName: string
  amount: number
  status: 'pending' | 'paid' | 'cancelled' | 'completed'
  createdAt: string
  paidAt?: string
}
