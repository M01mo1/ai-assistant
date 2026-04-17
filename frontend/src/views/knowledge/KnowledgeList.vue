<template>
  <div class="knowledge-list">
    <div class="page-header">
      <h1 class="page-title">知识库管理</h1>
      <div class="header-actions">
        <el-button type="primary" @click="router.push('/knowledge/add')" :icon="Plus">
          添加文档
        </el-button>
        <el-button @click="router.push('/knowledge/feishu')" :icon="Connection">
          同步飞书
        </el-button>
        <el-select v-model="typeFilter" placeholder="文档类型" clearable style="width: 140px">
          <el-option label="全部类型" value="" />
          <el-option label="手动添加" value="MANUAL" />
          <el-option label="飞书文档" value="FEISHU_DOC" />
        </el-select>
      </div>
    </div>

    <!-- 统计信息 -->
    <div class="stats-row">
      <div class="stat-card">
        <div class="stat-icon">
          <el-icon><Document /></el-icon>
        </div>
        <div class="stat-info">
          <span class="stat-value">{{ documents.length }}</span>
          <span class="stat-label">文档总数</span>
        </div>
      </div>
      <div class="stat-card">
        <div class="stat-icon success">
          <el-icon><CircleCheck /></el-icon>
        </div>
        <div class="stat-info">
          <span class="stat-value">{{ enabledCount }}</span>
          <span class="stat-label">已启用</span>
        </div>
      </div>
      <div class="stat-card">
        <div class="stat-icon warning">
          <el-icon><Files /></el-icon>
        </div>
        <div class="stat-info">
          <span class="stat-value">{{ totalChunks }}</span>
          <span class="stat-label">知识分块</span>
        </div>
      </div>
    </div>

    <!-- 加载状态 -->
    <div v-if="loading" class="loading-state">
      <el-skeleton :rows="5" animated />
    </div>

    <!-- 空状态 -->
    <div v-else-if="filteredDocuments.length === 0" class="empty-state">
      <el-icon><Folder /></el-icon>
      <p>暂无知识文档</p>
      <el-button type="primary" @click="router.push('/knowledge/add')">添加文档</el-button>
    </div>

    <!-- 文档列表 -->
    <div v-else class="document-grid">
      <DocumentCard
        v-for="doc in filteredDocuments"
        :key="doc.documentId"
        :document="doc"
        @disable="handleDisable"
        @delete="handleDelete"
      />
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { Plus, Connection, CircleCheck } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import * as knowledgeApi from '@/api/knowledge'
import type { KnowledgeDocument } from '@/types/knowledge'
import DocumentCard from '@/components/knowledge/DocumentCard.vue'

const router = useRouter()

const documents = ref<KnowledgeDocument[]>([])
const loading = ref(false)
const typeFilter = ref('')

const filteredDocuments = computed(() => {
  if (!typeFilter.value) {
    return documents.value
  }
  return documents.value.filter(d => d.documentType === typeFilter.value)
})

const enabledCount = computed(() => {
  return documents.value.filter(d => d.enabled).length
})

const totalChunks = computed(() => {
  return documents.value.reduce((sum, d) => sum + (d.chunkCount || 0), 0)
})

const loadDocuments = async () => {
  loading.value = true
  try {
    documents.value = await knowledgeApi.getAllDocuments()
  } catch (error) {
    ElMessage.error('加载文档列表失败')
  } finally {
    loading.value = false
  }
}

const handleDisable = async (documentId: string) => {
  try {
    await knowledgeApi.disableDocument(documentId)
    ElMessage.success('文档已禁用')
    loadDocuments()
  } catch (error) {
    ElMessage.error('禁用文档失败')
  }
}

const handleDelete = async (documentId: string) => {
  try {
    await knowledgeApi.deleteDocument(documentId)
    ElMessage.success('文档已删除')
    loadDocuments()
  } catch (error) {
    ElMessage.error('删除文档失败')
  }
}

onMounted(() => {
  loadDocuments()
})
</script>

<style lang="scss" scoped>
.knowledge-list {
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

.stats-row {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 16px;
  margin-bottom: 24px;
}

.stat-card {
  background: #fff;
  border-radius: 12px;
  padding: 20px;
  display: flex;
  align-items: center;
  gap: 16px;

  .stat-icon {
    width: 48px;
    height: 48px;
    border-radius: 12px;
    background: linear-gradient(135deg, #409eff, #53a8ff);
    display: flex;
    align-items: center;
    justify-content: center;

    .el-icon {
      font-size: 24px;
      color: #fff;
    }

    &.success {
      background: linear-gradient(135deg, #67c23a, #95d475);
    }

    &.warning {
      background: linear-gradient(135deg, #e6a23c, #f0c78a);
    }
  }

  .stat-info {
    display: flex;
    flex-direction: column;

    .stat-value {
      font-size: 28px;
      font-weight: 600;
      color: #303133;
    }

    .stat-label {
      font-size: 13px;
      color: #909399;
    }
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

.document-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(350px, 1fr));
  gap: 16px;
}
</style>
