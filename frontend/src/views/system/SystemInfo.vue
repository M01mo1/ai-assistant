<template>
  <div class="system-info">
    <div class="page-header">
      <h1 class="page-title">系统信息</h1>
      <el-button @click="refreshAll" :loading="loading" :icon="Refresh">
        刷新
      </el-button>
    </div>

    <!-- 健康状态 -->
    <div class="info-card health-card">
      <div class="card-header">
        <el-icon><Monitor /></el-icon>
        <span>系统状态</span>
      </div>
      <div class="health-status">
        <div class="status-indicator" :class="healthInfo?.status === 'UP' ? 'up' : 'down'">
          <el-icon v-if="healthInfo?.status === 'UP'"><CircleCheck /></el-icon>
          <el-icon v-else><CircleClose /></el-icon>
        </div>
        <div class="status-info">
          <span class="status-text">{{ healthInfo?.status === 'UP' ? '运行正常' : '服务异常' }}</span>
          <span class="status-time">{{ healthInfo?.timestamp }}</span>
        </div>
      </div>
    </div>

    <!-- 服务信息 -->
    <div class="info-card">
      <div class="card-header">
        <el-icon><InfoFilled /></el-icon>
        <span>服务信息</span>
      </div>
      <div class="info-list">
        <div class="info-item">
          <span class="info-label">服务名称</span>
          <span class="info-value">{{ systemInfo?.name || '-' }}</span>
        </div>
        <div class="info-item">
          <span class="info-label">版本号</span>
          <span class="info-value">{{ systemInfo?.version || '-' }}</span>
        </div>
        <div class="info-item">
          <span class="info-label">服务描述</span>
          <span class="info-value">{{ systemInfo?.description || '-' }}</span>
        </div>
      </div>
    </div>

    <!-- 功能列表 -->
    <div class="info-card">
      <div class="card-header">
        <el-icon><Grid /></el-icon>
        <span>功能列表</span>
      </div>
      <div class="feature-grid">
        <div
          v-for="(feature, index) in systemInfo?.features || []"
          :key="index"
          class="feature-item"
        >
          <el-icon><Check /></el-icon>
          <span>{{ feature }}</span>
        </div>
      </div>
    </div>

    <!-- API端点 -->
    <div class="info-card">
      <div class="card-header">
        <el-icon><Connection /></el-icon>
        <span>API端点</span>
      </div>
      <div class="api-list">
        <div
          v-for="endpoint in apiEndpoints"
          :key="endpoint.path"
          class="api-item"
        >
          <el-tag :type="endpoint.method === 'GET' ? 'success' : 'primary'" size="small">
            {{ endpoint.method }}
          </el-tag>
          <span class="api-path">{{ endpoint.path }}</span>
          <span class="api-desc">{{ endpoint.description }}</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { Refresh, CircleCheck, CircleClose, Check } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import * as systemApi from '@/api/system'
import type { HealthStatus, SystemInfo } from '@/api/system'

const loading = ref(false)
const healthInfo = ref<HealthStatus | null>(null)
const systemInfo = ref<SystemInfo | null>(null)

const apiEndpoints = [
  { method: 'POST', path: '/api/chat/send', description: '发送对话消息' },
  { method: 'GET', path: '/api/chat/sessions/{sessionId}/messages', description: '获取会话历史' },
  { method: 'GET', path: '/api/chat/users/{userId}/sessions', description: '获取用户会话列表' },
  { method: 'POST', path: '/api/chat/sessions/{sessionId}/close', description: '关闭会话' },
  { method: 'POST', path: '/api/chat/messages/{messageId}/feedback', description: '提交消息反馈' },
  { method: 'GET', path: '/api/knowledge/documents', description: '获取知识文档列表' },
  { method: 'POST', path: '/api/knowledge/documents', description: '添加知识文档' },
  { method: 'POST', path: '/api/knowledge/feishu/sync', description: '同步飞书文档' },
  { method: 'POST', path: '/api/rag/search', description: '语义检索' },
  { method: 'POST', path: '/api/rag/query', description: 'RAG增强查询' },
  { method: 'GET', path: '/api/health', description: '健康检查' },
  { method: 'GET', path: '/api/info', description: '服务信息' }
]

const loadHealth = async () => {
  try {
    healthInfo.value = await systemApi.getHealth()
  } catch (error) {
    healthInfo.value = { status: 'DOWN', service: '', timestamp: new Date().toISOString() }
  }
}

const loadInfo = async () => {
  try {
    systemInfo.value = await systemApi.getInfo()
  } catch (error) {
    ElMessage.error('获取服务信息失败')
  }
}

const refreshAll = async () => {
  loading.value = true
  await Promise.all([loadHealth(), loadInfo()])
  loading.value = false
  ElMessage.success('刷新成功')
}

onMounted(() => {
  loadHealth()
  loadInfo()
})
</script>

<style lang="scss" scoped>
.system-info {
  max-width: 900px;
  margin: 0 auto;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;

  .page-title {
    margin: 0;
  }
}

.info-card {
  background: #fff;
  border-radius: 12px;
  padding: 24px;
  margin-bottom: 16px;

  .card-header {
    display: flex;
    align-items: center;
    gap: 8px;
    margin-bottom: 20px;
    font-size: 16px;
    font-weight: 500;
    color: #303133;

    .el-icon {
      color: #409eff;
    }
  }
}

.health-card {
  .health-status {
    display: flex;
    align-items: center;
    gap: 16px;
  }

  .status-indicator {
    width: 60px;
    height: 60px;
    border-radius: 50%;
    display: flex;
    align-items: center;
    justify-content: center;

    .el-icon {
      font-size: 32px;
      color: #fff;
    }

    &.up {
      background: linear-gradient(135deg, #67c23a, #95d475);
    }

    &.down {
      background: linear-gradient(135deg, #f56c6c, #f89898);
    }
  }

  .status-info {
    display: flex;
    flex-direction: column;
    gap: 4px;

    .status-text {
      font-size: 20px;
      font-weight: 600;
      color: #303133;
    }

    .status-time {
      font-size: 13px;
      color: #909399;
    }
  }
}

.info-list {
  display: flex;
  flex-direction: column;
  gap: 16px;

  .info-item {
    display: flex;
    align-items: center;

    .info-label {
      width: 100px;
      color: #909399;
      font-size: 14px;
    }

    .info-value {
      flex: 1;
      color: #303133;
      font-size: 14px;
    }
  }
}

.feature-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
  gap: 12px;

  .feature-item {
    display: flex;
    align-items: center;
    gap: 8px;
    padding: 12px 16px;
    background: #f5f7fa;
    border-radius: 8px;
    font-size: 14px;
    color: #606266;

    .el-icon {
      color: #67c23a;
    }
  }
}

.api-list {
  display: flex;
  flex-direction: column;
  gap: 12px;

  .api-item {
    display: flex;
    align-items: center;
    gap: 12px;
    padding: 12px;
    background: #f5f7fa;
    border-radius: 8px;

    .api-path {
      font-family: Monaco, Consolas, monospace;
      font-size: 13px;
      color: #303133;
      flex-shrink: 0;
    }

    .api-desc {
      font-size: 13px;
      color: #909399;
      margin-left: auto;
    }
  }
}
</style>
