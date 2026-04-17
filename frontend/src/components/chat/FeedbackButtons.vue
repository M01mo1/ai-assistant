<template>
  <div class="feedback-buttons">
    <el-tooltip content="有帮助" placement="top">
      <el-button
        :type="currentFeedback === 1 ? 'success' : 'default'"
        :icon="CircleCheck"
        size="small"
        circle
        @click="handleFeedback(1)"
      />
    </el-tooltip>
    
    <el-tooltip content="无帮助" placement="top">
      <el-button
        :type="currentFeedback === 0 ? 'warning' : 'default'"
        :icon="CircleClose"
        size="small"
        circle
        @click="handleFeedback(0)"
      />
    </el-tooltip>
    
    <el-tooltip content="有错误" placement="top">
      <el-button
        :type="currentFeedback === -1 ? 'danger' : 'default'"
        :icon="Warning"
        size="small"
        circle
        @click="handleFeedback(-1)"
      />
    </el-tooltip>
  </div>
</template>

<script setup lang="ts">
import { CircleCheck, CircleClose, Warning } from '@element-plus/icons-vue'
import { useChatStore } from '@/stores/chat'

const props = defineProps<{
  messageId: string
  currentFeedback?: number
}>()

const chatStore = useChatStore()

const handleFeedback = async (feedback: number) => {
  if (props.currentFeedback === feedback) return
  await chatStore.submitFeedback(props.messageId, feedback)
}
</script>

<style lang="scss" scoped>
.feedback-buttons {
  display: flex;
  gap: 8px;
  
  .el-button {
    opacity: 0.6;
    transition: all 0.3s;
    
    &:hover {
      opacity: 1;
    }
    
    &.el-button--success,
    &.el-button--warning,
    &.el-button--danger {
      opacity: 1;
    }
  }
}
</style>
