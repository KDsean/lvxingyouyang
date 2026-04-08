import { http } from '../request'
import type { ApiResponse, User } from '@/types'

// 登录
export const login = (data: { username: string; password: string }) => {
  return http.post<ApiResponse<{ token: string; user: User }>>('/auth/login', data)
}

// 注册
export const register = (data: { username: string; email: string; password: string; phone?: string }) => {
  return http.post<ApiResponse<{ token: string; user: User }>>('/auth/register', data)
}

// 获取当前用户信息
export const getCurrentUser = () => {
  return http.get<ApiResponse<User>>('/auth/user')
}

// 登出
export const logout = () => {
  return http.post<ApiResponse>('/auth/logout')
}

// 修改密码
export const changePassword = (data: { oldPassword: string; newPassword: string }) => {
  return http.post<ApiResponse>('/auth/change-password', data)
}

// 更新用户信息
export const updateProfile = (data: Partial<User>) => {
  return http.put<ApiResponse<User>>('/auth/profile', data)
}
