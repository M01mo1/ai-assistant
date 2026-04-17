<template>
  <div class="tool-invocation-list">
    <el-collapse v-model="activeNames">
      <el-collapse-item name="tools">
        <template #title>
          <div class="collapse-title">
            <el-icon><Tools /></el-icon>
            <span>工具调用 ({{ invocations.length }})</span>
          </div>
        </template>
        
        <div class="tool-items">
          <div
            v-for="(invocation, index) in invocations"
            :key="index"
            class="tool-item"
          >
            <div class="tool-header">
              <el-tag type="primary" size="small">{{ invocation.toolName }}</el-tag>
              <span class="tool-time">耗时: {{ invocation.executionTime }}ms</span>
            </div>
            
            <div class="tool-detail">
              <div class="detail-row">
                <span class="detail-label">输入:</span>
                <code class="detail-value">{{ invocation.input }}</code>
              </div>
              <div class="detail-row">
                <span class="detail-label">输出:</span>
                <code class="detail-value">{{ truncateOutput(invocation.output) }}</code>
              </div>
            </div>
          </div>
        </div>
      </el-collapse-item>
    </el-collapse>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import type { ToolInvocation } from '@/types/chat'

defineProps<{
  invocations: ToolInvocation[]
}>()

const activeNames = ref<string[]>([])

const truncateOutput = (output: string): string => {
  if (output.length > 200) {
    return output.substring(0, 200) + '...'
  }
  return output
}
</script>

<style lang="scss" scoped>
.tool-invocation-list {
  :deep(.el-collapse) {
    border: none;
  }
  
  :deep(.el-collapse-item__header) {
    background: #fef0f0;
    border-radius: 8px;
    padding: 8px 12px;
    height: auto;
    line-height: 1.5;
    border: none;
    
    &.is-active {
      border-radius: 8px 8px 0 0;
    }
  }
  
  :deep(.el-collapse-item__wrap) {
    border: 1px solid #fde2e2;
    border-top: none;
    border-radius: 0 0 8px 8px;
  }
  
  :deep(.el-collapse-item__content) {
    padding: 12px;
  }
}

.collapse-title {
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: 13px;
  color: #f56c6c;
}

.tool-items {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.tool-item {
  background: #fafafa;
  border-radius: 8px;
  padding: 12px;
  
  .tool-header {
    display: flex;
    align-items: center;
    justify-content: space-between;
    margin-bottom: 8px;
  }
  
  .tool-time {
    font-size: 12px;
    color: #909399;
  }
}

.tool-detail {
  font-size: 12px;
  
  .detail-row {
    display: flex;
    gap: 8px;
    margin-bottom: 4px;
    
    &:last-child {
      margin-bottom: 0;
    }
  }
  
  .detail-label {
    color: #909399;
    flex-shrink: 0;
  }
  
  .detail-value {
    background: #f5f7fa;
    padding: 2px 6px;
    border-radius: 4px;
    font-family: Monaco, Consolas, monospace;
    color: #606266;
    word-break: break-all;
  }
}
</style>
