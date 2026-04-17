<template>
  <div class="chat-main">
    <!-- 顶部栏 -->
    <div class="chat-header">
      <div class="header-left">
        <h2>{{ chatStore.currentSession?.title || '新对话' }}</h2>
        <el-tag
          v-if="chatStore.currentSession"
          :type="chatStore.currentSession.status === 'ACTIVE' ? 'success' : 'info'"
          size="small"
        >
          {{ chatStore.currentSession.status === 'ACTIVE' ? '进行中' : '已结束' }}
        </el-tag>
      </div>
      <div class="header-right">
        <el-button @click="handleNewSession" :icon="Plus">
          新建对话
        </el-button>
        <el-button
          v-if="chatStore.currentSession?.status === 'ACTIVE'"
          type="warning"
          @click="handleCloseSession"
          :icon="CircleClose"
        >
          关闭会话
        </el-button>
      </div>
    </div>

    <!-- 消息列表 -->
    <div class="chat-messages" ref="messageListRef">
      <!-- 空状态 -->
      <div v-if="chatStore.messages.length === 0 && !chatStore.loading" class="empty-state">
        <el-icon><ChatDotRound /></el-icon>
        <h3>欢迎使用AI客服助手</h3>
        <p>请在下方输入您的问题，我将为您提供帮助</p>
        <div class="quick-questions">
          <p class="quick-title">您可以试试这些问题：</p>
          <div class="quick-list">
            <el-button
              v-for="(question, index) in quickQuestions"
              :key="index"
              text
              @click="handleQuickQuestion(question)"
            >
              {{ question }}
            </el-button>
          </div>
        </div>
      </div>

      <!-- 加载状态 -->
      <div v-if="chatStore.loading" class="loading-state">
        <el-skeleton :rows="3" animated />
      </div>

      <!-- 消息列表 -->
      <MessageItem
        v-for="message in chatStore.messages"
        :key="message.messageId"
        :message="message"
      />

      <!-- 发送中提示 -->
      <div v-if="chatStore.sending" class="sending-indicator">
        <el-avatar :size="36" class="avatar-ai">
          <el-icon><Service /></el-icon>
        </el-avatar>
        <div class="thinking-dots">
          <span></span>
          <span></span>
          <span></span>
        </div>
      </div>
    </div>

    <!-- 消息输入 -->
    <MessageInput :sending="chatStore.sending" @send="handleSend" />
  </div>
</template>

<script setup lang="ts">
import { ref, nextTick, watch, onMounted } from 'vue'
import { Plus, CircleClose } from '@element-plus/icons-vue'
import { useChatStore } from '@/stores/chat'
import { useUserStore } from '@/stores/user'
import MessageItem from '@/components/chat/MessageItem.vue'
import MessageInput from '@/components/chat/MessageInput.vue'

const chatStore = useChatStore()
const userStore = useUserStore()
const messageListRef = ref<HTMLElement>()

const quickQuestions = [
  '我的订单发货了吗？',
  '如何申请退货退款？',
  '物流显示异常怎么办？',
  '售后服务政策是什么？'
]

// 滚动到底部
const scrollToBottom = () => {
  nextTick(() => {
    if (messageListRef.value) {
      messageListRef.value.scrollTop = messageListRef.value.scrollHeight
    }
  })
}

// 监听消息变化自动滚动
watch(
  () => chatStore.messages.length,
  () => scrollToBottom()
)

// 发送消息
const handleSend = async (data: {
  message: string
  orderId?: string
  enableRag: boolean
  enableTools: boolean
}) => {
  try {
    await chatStore.sendMessage({
      userId: userStore.userId,
      sessionId: chatStore.currentSession?.sessionId,
      message: data.message,
      orderId: data.orderId,
      enableRag: data.enableRag,
      enableTools: data.enableTools
    })
    scrollToBottom()
  } catch (error) {
    console.error('发送消息失败:', error)
  }
}

// 快捷问题
const handleQuickQuestion = (question: string) => {
  handleSend({
    message: question,
    enableRag: true,
    enableTools: true
  })
}

// 新建对话
const handleNewSession = () => {
  chatStore.newSession()
}

// 关闭会话
const handleCloseSession = () => {
  if (chatStore.currentSession) {
    chatStore.closeSession(chatStore.currentSession.sessionId)
  }
}

onMounted(() => {
  scrollToBottom()
})
</script>

<style lang="scss" scoped>
.chat-main {
  height: calc(100vh - 100px);
  display: flex;
  flex-direction: column;
  background: #f5f7fa;
  border-radius: 12px;
  overflow: hidden;
}

.chat-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px 20px;
  background: #fff;
  border-bottom: 1px solid #e4e7ed;

  .header-left {
    display: flex;
    align-items: center;
    gap: 12px;

    h2 {
      font-size: 18px;
      font-weight: 600;
      color: #303133;
      margin: 0;
    }
  }

  .header-right {
    display: flex;
    gap: 12px;
  }
}

.chat-messages {
  flex: 1;
  overflow-y: auto;
  padding: 20px;
  background: linear-gradient(180deg, #f5f7fa 0%, #ffffff 100%);
}

.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 100%;
  text-align: center;

  .el-icon {
    font-size: 64px;
    color: #409eff;
    margin-bottom: 20px;
  }

  h3 {
    font-size: 20px;
    color: #303133;
    margin: 0 0 8px;
  }

  p {
    color: #909399;
    margin: 0;
  }

  .quick-questions {
    margin-top: 32px;

    .quick-title {
      font-size: 14px;
      color: #606266;
      margin-bottom: 12px;
    }

    .quick-list {
      display: flex;
      flex-wrap: wrap;
      gap: 8px;
      justify-content: center;

      .el-button {
        background: #fff;
        border: 1px solid #dcdfe6;
        border-radius: 20px;
        padding: 8px 16px;

        &:hover {
          border-color: #409eff;
          color: #409eff;
        }
      }
    }
  }
}

.loading-state {
  padding: 20px;
}

.sending-indicator {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 16px 0;

  .avatar-ai {
    background: linear-gradient(135deg, #67c23a, #95d475);
  }

  .thinking-dots {
    display: flex;
    gap: 4px;
    padding: 12px 16px;
    background: #fff;
    border-radius: 12px;
    border: 1px solid #e4e7ed;

    span {
      width: 8px;
      height: 8px;
      background: #409eff;
      border-radius: 50%;
      animation: bounce 1.4s ease-in-out infinite both;

      &:nth-child(1) {
        animation-delay: -0.32s;
      }

      &:nth-child(2) {
        animation-delay: -0.16s;
      }
    }
  }
}

@keyframes bounce {
  0%,
  80%,
  100% {
    transform: scale(0);
  }
  40% {
    transform: scale(1);
  }
}
</style>
