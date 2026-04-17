<template>
  <div class="message-input">
    <div class="input-options">
      <el-input
        v-model="orderId"
        placeholder="关联订单ID (选填)"
        clearable
        size="small"
        class="order-input"
      >
        <template #prefix>
          <el-icon><ShoppingCart /></el-icon>
        </template>
      </el-input>
      
      <el-checkbox v-model="enableRag" size="small">启用RAG</el-checkbox>
      <el-checkbox v-model="enableTools" size="small">启用工具</el-checkbox>
    </div>
    
    <div class="input-main">
      <el-input
        v-model="message"
        type="textarea"
        :rows="3"
        :autosize="{ minRows: 2, maxRows: 6 }"
        placeholder="请输入您的问题..."
        resize="none"
        @keydown.enter="handleEnter"
        :disabled="sending"
      />
      
      <el-button
        type="primary"
        :icon="Promotion"
        :loading="sending"
        :disabled="!canSend"
        @click="handleSend"
        class="send-btn"
      >
        发送
      </el-button>
    </div>
    
    <div class="input-tips">
      <span>按 Enter 发送，Shift + Enter 换行</span>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import { Promotion } from '@element-plus/icons-vue'

const emit = defineEmits<{
  send: [{ message: string; orderId?: string; enableRag: boolean; enableTools: boolean }]
}>()

const props = defineProps<{
  sending?: boolean
}>()

const message = ref('')
const orderId = ref('')
const enableRag = ref(true)
const enableTools = ref(true)

const canSend = computed(() => {
  return message.value.trim().length > 0 && !props.sending
})

const handleSend = () => {
  if (!canSend.value) return
  
  emit('send', {
    message: message.value.trim(),
    orderId: orderId.value || undefined,
    enableRag: enableRag.value,
    enableTools: enableTools.value
  })
  
  message.value = ''
}

const handleEnter = (e: KeyboardEvent) => {
  if (e.shiftKey) return // Shift+Enter 换行
  e.preventDefault()
  handleSend()
}
</script>

<style lang="scss" scoped>
.message-input {
  background: #fff;
  border-radius: 12px;
  padding: 16px;
  box-shadow: 0 -4px 12px rgba(0, 0, 0, 0.05);
}

.input-options {
  display: flex;
  align-items: center;
  gap: 16px;
  margin-bottom: 12px;
  
  .order-input {
    width: 200px;
  }
}

.input-main {
  display: flex;
  gap: 12px;
  
  :deep(.el-textarea) {
    flex: 1;
    
    .el-textarea__inner {
      border-radius: 8px;
      font-size: 14px;
      line-height: 1.6;
      padding: 10px 14px;
      
      &:focus {
        border-color: #409eff;
        box-shadow: 0 0 0 2px rgba(64, 158, 255, 0.1);
      }
    }
  }
  
  .send-btn {
    height: auto;
    min-height: 60px;
    padding: 0 24px;
    border-radius: 8px;
    font-size: 15px;
  }
}

.input-tips {
  margin-top: 8px;
  font-size: 12px;
  color: #909399;
}
</style>
