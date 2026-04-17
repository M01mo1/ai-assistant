<template>
  <div class="document-card">
    <div class="card-header">
      <el-icon class="card-icon"><Document /></el-icon>
      <div class="card-title-wrap">
        <span class="card-title">{{ document.title }}</span>
        <el-tag size="small">{{ getDocumentTypeName(document.documentType) }}</el-tag>
      </div>
    </div>
    
    <div class="card-content">
      <p class="content-summary">{{ document.summary || truncateText(document.rawContent, 100) }}</p>
    </div>
    
    <div class="card-meta">
      <div class="meta-item">
        <el-icon><Files /></el-icon>
        <span>{{ document.chunkCount }} 个分块</span>
      </div>
      <div class="meta-item">
        <el-icon><Clock /></el-icon>
        <span>{{ formatTime(document.createTime, 'YYYY-MM-DD') }}</span>
      </div>
      <el-tag :type="document.enabled ? 'success' : 'info'" size="small">
        {{ document.enabled ? '已启用' : '已禁用' }}
      </el-tag>
    </div>
    
    <div class="card-tags" v-if="document.tags && document.tags.length > 0">
      <el-tag
        v-for="tag in document.tags"
        :key="tag"
        size="small"
        type="info"
      >
        {{ tag }}
      </el-tag>
    </div>
    
    <div class="card-actions">
      <el-button
        v-if="document.enabled"
        type="warning"
        text
        size="small"
        @click="$emit('disable', document.documentId)"
      >
        <el-icon><VideoPause /></el-icon>
        禁用
      </el-button>
      <el-popconfirm
        title="确定要删除这个文档吗？"
        @confirm="$emit('delete', document.documentId)"
      >
        <template #reference>
          <el-button type="danger" text size="small">
            <el-icon><Delete /></el-icon>
            删除
          </el-button>
        </template>
      </el-popconfirm>
    </div>
  </div>
</template>

<script setup lang="ts">
import type { KnowledgeDocument } from '@/types/knowledge'
import { formatTime, getDocumentTypeName, truncateText } from '@/utils'

defineProps<{
  document: KnowledgeDocument
}>()

defineEmits<{
  disable: [documentId: string]
  delete: [documentId: string]
}>()
</script>

<style lang="scss" scoped>
.document-card {
  background: #fff;
  border-radius: 12px;
  padding: 20px;
  transition: all 0.3s;
  
  &:hover {
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  }
}

.card-header {
  display: flex;
  align-items: flex-start;
  gap: 12px;
  margin-bottom: 12px;
  
  .card-icon {
    font-size: 24px;
    color: #409eff;
    flex-shrink: 0;
  }
  
  .card-title-wrap {
    display: flex;
    flex-direction: column;
    gap: 6px;
  }
  
  .card-title {
    font-size: 16px;
    font-weight: 500;
    color: #303133;
    line-height: 1.4;
  }
}

.card-content {
  margin-bottom: 12px;
  
  .content-summary {
    font-size: 13px;
    color: #606266;
    line-height: 1.6;
    margin: 0;
    display: -webkit-box;
    -webkit-line-clamp: 2;
    -webkit-box-orient: vertical;
    overflow: hidden;
  }
}

.card-meta {
  display: flex;
  align-items: center;
  gap: 16px;
  margin-bottom: 12px;
  
  .meta-item {
    display: flex;
    align-items: center;
    gap: 4px;
    font-size: 12px;
    color: #909399;
    
    .el-icon {
      font-size: 14px;
    }
  }
}

.card-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 6px;
  margin-bottom: 12px;
}

.card-actions {
  display: flex;
  gap: 8px;
  padding-top: 12px;
  border-top: 1px solid #f0f0f0;
}
</style>
