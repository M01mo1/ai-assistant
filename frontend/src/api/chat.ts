import request from './request'
import type { ChatRequest, ChatResponse, ChatMessage, ChatSession, FeedbackRequest } from '@/types/chat'

// 发送消息
export const sendMessage = (data: ChatRequest): Promise<ChatResponse> => {
  return request.post('/api/chat/send', data)
}

// 获取会话历史
export const getSessionHistory = (sessionId: string): Promise<ChatMessage[]> => {
  return request.get(`/api/chat/sessions/${sessionId}/messages`)
}

// 获取用户会话列表
export const getUserSessions = (userId: string): Promise<ChatSession[]> => {
  return request.get(`/api/chat/users/${userId}/sessions`)
}

// 关闭会话
export const closeSession = (sessionId: string): Promise<{ success: boolean; message: string }> => {
  return request.post(`/api/chat/sessions/${sessionId}/close`)
}

// 提交反馈
export const submitFeedback = (
  messageId: string,
  data: FeedbackRequest
): Promise<{ success: boolean; message: string }> => {
  return request.post(`/api/chat/messages/${messageId}/feedback`, data)
}
