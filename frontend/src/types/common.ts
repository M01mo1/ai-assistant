// 系统健康状态
export interface HealthStatus {
  status: string
  service: string
  timestamp: string
}

// 系统信息
export interface SystemInfo {
  name: string
  version: string
  description: string
  features: string[]
}

// API响应基础类型
export interface ApiResponse<T = any> {
  success: boolean
  message?: string
  data?: T
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
