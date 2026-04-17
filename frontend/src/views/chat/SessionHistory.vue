<template>
  <div class="session-history">
    <div class="page-header">
      <h1 class="page-title">会话历史</h1>
      <div class="header-actions">
        <el-button type="primary" @click="router.push('/chat')" :icon="Plus">
          新建对话
        </el-button>
        <el-select v-model="statusFilter" placeholder="筛选状态" clearable style="width: 120px">
          <el-option label="全部" value="" />
          <el-option label="进行中" value="ACTIVE" />
          <el-option label="已结束" value="CLOSED" />
        </el-select>
      </div>
    </div>

    <!-- 加载状态 -->
    <div v-if="sessionStore.loading" class="loading-state">
      <el-skeleton :rows="5" animated />
    </div>

    <!-- 空状态 -->
    <div v-else-if="filteredSessions.length === 0" class="empty-state">
      <el-icon><ChatDotRound /></el-icon>
      <p>暂无会话记录</p>
      <el-button type="primary" @click="router.push('/chat')">开始新对话</el-button>
    </div>

    <!-- 会话列表 -->
    <div v-else class="session-grid">
      <SessionCard
        v-for="session in filteredSessions"
        :key="session.sessionId"
        :session="session"
        :is-active="chatStore.currentSession?.sessionId === session.sessionId"
        @click="handleSessionClick"
        @close="handleCloseSession"
      />
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { Plus } from '@element-plus/icons-vue'
import { useSessionStore } from '@/stores/session'
import { useChatStore } from '@/stores/chat'
import { useUserStore } from '@/stores/user'
import SessionCard from '@/components/session/SessionCard.vue'
import type { ChatSession } from '@/types/chat'

const router = useRouter()
const sessionStore = useSessionStore()
const chatStore = useChatStore()
const userStore = useUserStore()

const statusFilter = ref('')

const filteredSessions = computed(() => {
  if (!statusFilter.value) {
    return sessionStore.sessions
  }
  return sessionStore.sessions.filter(s => s.status === statusFilter.value)
})

const handleSessionClick = async (session: ChatSession) => {
  chatStore.setCurrentSession(session)
  await chatStore.loadSessionHistory(session.sessionId)
  router.push('/chat')
}

const handleCloseSession = async (sessionId: string) => {
  await sessionStore.closeSession(sessionId)
}

onMounted(() => {
  sessionStore.loadUserSessions(userStore.userId)
})
</script>

<style lang="scss" scoped>
.session-history {
  padding: 0;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;

  .page-title {
    margin: 0;
  }

  .header-actions {
    display: flex;
    gap: 12px;
  }
}

.loading-state {
  background: #fff;
  border-radius: 12px;
  padding: 24px;
}

.empty-state {
  background: #fff;
  border-radius: 12px;
  padding: 60px 24px;
  text-align: center;

  .el-icon {
    font-size: 48px;
    color: #c0c4cc;
    margin-bottom: 16px;
  }

  p {
    color: #909399;
    margin-bottom: 20px;
  }
}

.session-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(320px, 1fr));
  gap: 16px;
}
</style>
