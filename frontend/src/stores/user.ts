import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useUserStore = defineStore('user', () => {
  // 用户ID - 默认用户
  const userId = ref('default-user-001')
  // 用户名
  const userName = ref('客服工单助手用户')

  // 设置用户ID
  const setUserId = (id: string) => {
    userId.value = id
  }

  // 设置用户名
  const setUserName = (name: string) => {
    userName.value = name
  }

  return {
    userId,
    userName,
    setUserId,
    setUserName
  }
})
