import { defineStore } from 'pinia'
import { ref } from 'vue'
import type { ChatSession } from '@/types/chat'
import * as chatApi from '@/api/chat'
import { ElMessage } from 'element-plus'

export const useSessionStore = defineStore('session', () => {
  // 会话列表
  const sessions = ref<ChatSession[]>([])
  // 加载状态
  const loading = ref(false)

  // 加载用户会话列表
  const loadUserSessions = async (userId: string) => {
    loading.value = true
    
    try {
      const list = await chatApi.getUserSessions(userId)
      sessions.value = list
    } catch (err: any) {
      ElMessage.error('加载会话列表失败')
    } finally {
      loading.value = false
    }
  }

  // 关闭会话
  const closeSession = async (sessionId: string) => {
    try {
      await chatApi.closeSession(sessionId)
      
      // 更新本地状态
      const session = sessions.value.find(s => s.sessionId === sessionId)
      if (session) {
        session.status = 'CLOSED'
      }
      
      ElMessage.success('会话已关闭')
    } catch (err: any) {
      ElMessage.error('关闭会话失败')
    }
  }

  return {
    sessions,
    loading,
    loadUserSessions,
    closeSession
  }
})
