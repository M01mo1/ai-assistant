import request from './request'

export interface HealthStatus {
  status: string
  service: string
  timestamp: string
}

export interface SystemInfo {
  name: string
  version: string
  description: string
  features: string[]
}

// 健康检查
export const getHealth = (): Promise<HealthStatus> => {
  return request.get('/api/health')
}

// 获取服务信息
export const getInfo = (): Promise<SystemInfo> => {
  return request.get('/api/info')
}
