<template>
  <div class="session-card" :class="{ active: isActive }" @click="handleClick">
    <div class="card-header">
      <el-icon class="card-icon"><ChatDotRound /></el-icon>
      <span class="card-title">{{ session.title || '新对话' }}</span>
    </div>
    
    <div class="card-info">
      <div class="info-row">
        <span class="info-label">
          <el-icon><ChatLineSquare /></el-icon>
          {{ session.messageCount || 0 }} 条消息
        </span>
        <el-tag :type="statusInfo.type as any" size="small">
          {{ statusInfo.label }}
        </el-tag>
      </div>
      
      <div class="info-row" v-if="session.orderId">
        <span class="info-label">
          <el-icon><ShoppingCart /></el-icon>
          订单: {{ session.orderId }}
        </span>
      </div>
      
      <div class="info-time">
        <el-icon><Clock /></el-icon>
        {{ fromNow(session.lastUpdateTime || session.createTime) }}
      </div>
    </div>
    
    <div class="card-actions" @click.stop>
      <el-dropdown trigger="click" @command="handleCommand">
        <el-button :icon="MoreFilled" text size="small" />
        <template #dropdown>
          <el-dropdown-menu>
            <el-dropdown-item
              v-if="session.status === 'ACTIVE'"
              command="close"
            >
              <el-icon><CircleClose /></el-icon>
              关闭会话
            </el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { MoreFilled, CircleClose } from '@element-plus/icons-vue'
import type { ChatSession } from '@/types/chat'
import { fromNow, sessionStatusMap } from '@/utils'

const props = defineProps<{
  session: ChatSession
  isActive?: boolean
}>()

const emit = defineEmits<{
  click: [session: ChatSession]
  close: [sessionId: string]
}>()

const statusInfo = computed(() => {
  return sessionStatusMap[props.session.status] || { label: '未知', type: 'info' }
})

const handleClick = () => {
  emit('click', props.session)
}

const handleCommand = (command: string) => {
  if (command === 'close') {
    emit('close', props.session.sessionId)
  }
}
</script>

<style lang="scss" scoped>
.session-card {
  background: #fff;
  border-radius: 12px;
  padding: 16px;
  cursor: pointer;
  transition: all 0.3s;
  border: 2px solid transparent;
  position: relative;
  
  &:hover {
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
    
    .card-actions {
      opacity: 1;
    }
  }
  
  &.active {
    border-color: #409eff;
    background: linear-gradient(135deg, #ecf5ff, #f5f9ff);
  }
}

.card-header {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 12px;
  
  .card-icon {
    font-size: 20px;
    color: #409eff;
  }
  
  .card-title {
    font-size: 15px;
    font-weight: 500;
    color: #303133;
    flex: 1;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
  }
}

.card-info {
  .info-row {
    display: flex;
    align-items: center;
    justify-content: space-between;
    margin-bottom: 8px;
  }
  
  .info-label {
    display: flex;
    align-items: center;
    gap: 4px;
    font-size: 13px;
    color: #606266;
    
    .el-icon {
      font-size: 14px;
      color: #909399;
    }
  }
  
  .info-time {
    display: flex;
    align-items: center;
    gap: 4px;
    font-size: 12px;
    color: #909399;
    
    .el-icon {
      font-size: 12px;
    }
  }
}

.card-actions {
  position: absolute;
  top: 12px;
  right: 12px;
  opacity: 0;
  transition: opacity 0.3s;
}
</style>
