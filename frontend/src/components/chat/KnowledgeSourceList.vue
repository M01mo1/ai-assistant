<template>
  <div class="knowledge-source-list">
    <el-collapse v-model="activeNames">
      <el-collapse-item name="sources">
        <template #title>
          <div class="collapse-title">
            <el-icon><Document /></el-icon>
            <span>知识来源 ({{ sources.length }})</span>
          </div>
        </template>
        
        <div class="source-items">
          <div
            v-for="(source, index) in sources"
            :key="source.documentId || index"
            class="source-item"
          >
            <div class="source-header">
              <span class="source-title">{{ source.documentTitle || '未知来源' }}</span>
              <el-tag size="small" type="success">
                相似度: {{ formatSimilarity(source.score) }}
              </el-tag>
            </div>
            <div class="source-content">{{ source.contentSnippet }}</div>
            <a
              v-if="source.documentUrl"
              :href="source.documentUrl"
              target="_blank"
              class="source-link"
            >
              <el-icon><Link /></el-icon>
              查看原文
            </a>
          </div>
        </div>
      </el-collapse-item>
    </el-collapse>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import type { KnowledgeSource } from '@/types/chat'
import { formatSimilarity } from '@/utils'

defineProps<{
  sources: KnowledgeSource[]
}>()

const activeNames = ref<string[]>([])
</script>

<style lang="scss" scoped>
.knowledge-source-list {
  :deep(.el-collapse) {
    border: none;
  }
  
  :deep(.el-collapse-item__header) {
    background: #f5f7fa;
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
    border: 1px solid #e4e7ed;
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
  color: #606266;
}

.source-items {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.source-item {
  background: #fafafa;
  border-radius: 8px;
  padding: 12px;
  
  .source-header {
    display: flex;
    align-items: center;
    justify-content: space-between;
    margin-bottom: 8px;
  }
  
  .source-title {
    font-size: 13px;
    font-weight: 500;
    color: #303133;
  }
  
  .source-content {
    font-size: 12px;
    color: #606266;
    line-height: 1.6;
    max-height: 100px;
    overflow: hidden;
    text-overflow: ellipsis;
    display: -webkit-box;
    -webkit-line-clamp: 4;
    -webkit-box-orient: vertical;
  }
  
  .source-link {
    display: inline-flex;
    align-items: center;
    gap: 4px;
    margin-top: 8px;
    font-size: 12px;
    color: #409eff;
    text-decoration: none;
    
    &:hover {
      text-decoration: underline;
    }
  }
}
</style>
