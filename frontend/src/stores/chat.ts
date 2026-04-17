import { defineStore } from 'pinia'
import { ref } from 'vue'
import type { ChatMessage, ChatRequest, ChatSession } from '@/types/chat'
import * as chatApi from '@/api/chat'
import { ElMessage } from 'element-plus'

export const useChatStore = defineStore('chat', () => {
  // 当前会话
  const currentSession = ref<ChatSession | null>(null)
  // 消息列表
  const messages = ref<ChatMessage[]>([])
  // 加载状态
  const loading = ref(false)
  // 发送中状态
  const sending = ref(false)
  // 错误信息
  const error = ref<string | null>(null)

  // 发送消息
  const sendMessage = async (request: ChatRequest) => {
    sending.value = true
    error.value = null
    
    // 先添加用户消息到列表
    const userMessage: ChatMessage = {
      messageId: `temp_${Date.now()}`,
      sessionId: request.sessionId || '',
      role: 'USER',
      content: request.message,
      createTime: new Date().toISOString()
    }
    messages.value.push(userMessage)
    
    try {
      const response = await chatApi.sendMessage(request)
      
      if (response.success) {
        // 更新sessionId
        if (!currentSession.value) {
          currentSession.value = {
            sessionId: response.sessionId,
            userId: request.userId,
            title: request.message.substring(0, 20) + '...',
            status: 'ACTIVE',
            createTime: new Date().toISOString(),
            lastUpdateTime: new Date().toISOString(),
            messageCount: 2
          }
        }
        
        // 添加AI回复
        const aiMessage: ChatMessage = {
          messageId: response.messageId,
          sessionId: response.sessionId,
          role: 'ASSISTANT',
          content: response.content,
          createTime: response.responseTime,
          knowledgeSources: response.knowledgeSources,
          toolInvocations: response.toolInvocations,
          tokenCount: response.tokenUsage
        }
        messages.value.push(aiMessage)
        
        return response
      } else {
        throw new Error(response.errorMessage || '发送失败')
      }
    } catch (err: any) {
      error.value = err.message
      // 移除临时添加的用户消息
      messages.value.pop()
      throw err
    } finally {
      sending.value = false
    }
  }

  // 加载会话历史
  const loadSessionHistory = async (sessionId: string) => {
    loading.value = true
    error.value = null
    
    try {
      const history = await chatApi.getSessionHistory(sessionId)
      messages.value = history
    } catch (err: any) {
      error.value = err.message
      ElMessage.error('加载会话历史失败')
    } finally {
      loading.value = false
    }
  }

  // 提交反馈
  const submitFeedback = async (messageId: string, feedback: number, detail?: string) => {
    try {
      await chatApi.submitFeedback(messageId, { feedback, detail })
      
      // 更新消息的反馈状态
      const message = messages.value.find(m => m.messageId === messageId)
      if (message) {
        message.feedback = feedback
        message.feedbackDetail = detail
      }
      
      ElMessage.success('反馈已提交')
    } catch (err: any) {
      ElMessage.error('提交反馈失败')
    }
  }

  // 关闭会话
  const closeSession = async (sessionId: string) => {
    try {
      await chatApi.closeSession(sessionId)
      
      if (currentSession.value?.sessionId === sessionId) {
        currentSession.value.status = 'CLOSED'
      }
      
      ElMessage.success('会话已关闭')
    } catch (err: any) {
      ElMessage.error('关闭会话失败')
    }
  }

  // 创建新会话
  const newSession = () => {
    currentSession.value = null
    messages.value = []
    error.value = null
  }

  // 设置当前会话
  const setCurrentSession = (session: ChatSession) => {
    currentSession.value = session
  }

  return {
    currentSession,
    messages,
    loading,
    sending,
    error,
    sendMessage,
    loadSessionHistory,
    submitFeedback,
    closeSession,
    newSession,
    setCurrentSession
  }
})
