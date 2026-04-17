<template>
  <div class="knowledge-search">
    <div class="page-header">
      <h1 class="page-title">知识检索</h1>
    </div>

    <!-- 搜索区域 -->
    <div class="search-section">
      <div class="search-bar">
        <el-input
          v-model="query"
          placeholder="请输入检索内容..."
          size="large"
          clearable
          @keyup.enter="handleSearch"
        >
          <template #prefix>
            <el-icon><Search /></el-icon>
          </template>
        </el-input>
        <el-button
          type="primary"
          size="large"
          @click="handleSearch"
          :loading="loading"
        >
          检索
        </el-button>
      </div>

      <div class="search-options">
        <el-radio-group v-model="searchMode" size="small">
          <el-radio-button value="semantic">语义检索</el-radio-button>
          <el-radio-button value="rag">RAG查询</el-radio-button>
        </el-radio-group>

        <div class="option-item" v-if="searchMode === 'semantic'">
          <span>返回数量:</span>
          <el-select v-model="topK" size="small" style="width: 80px">
            <el-option :value="3" label="3" />
            <el-option :value="5" label="5" />
            <el-option :value="10" label="10" />
          </el-select>
        </div>
      </div>
    </div>

    <!-- RAG回答 -->
    <div v-if="ragResult && searchMode === 'rag'" class="rag-answer">
      <div class="answer-header">
        <el-icon><ChatDotRound /></el-icon>
        <span>AI回答</span>
      </div>
      <div class="answer-content markdown-content" v-html="renderedAnswer"></div>
      <div v-if="ragResult.tokenUsage" class="answer-meta">
        Token消耗: {{ ragResult.tokenUsage }}
      </div>
    </div>

    <!-- 检索结果 -->
    <div v-if="searchResults.length > 0" class="search-results">
      <div class="results-header">
        <h3>检索结果 ({{ searchResults.length }})</h3>
      </div>

      <div class="result-list">
        <div
          v-for="(result, index) in searchResults"
          :key="index"
          class="result-item"
        >
          <div class="result-header">
            <span class="result-title">{{ result.title }}</span>
            <el-tag type="success" size="small">
              相似度: {{ formatSimilarity(result.similarity) }}
            </el-tag>
          </div>
          <div class="result-content">{{ result.content }}</div>
          <div v-if="result.metadata" class="result-meta">
            <el-tag
              v-for="(value, key) in result.metadata"
              :key="key"
              size="small"
              type="info"
            >
              {{ key }}: {{ value }}
            </el-tag>
          </div>
        </div>
      </div>
    </div>

    <!-- 空状态 -->
    <div v-else-if="!loading && searched" class="empty-result">
      <el-icon><SearchOutlined /></el-icon>
      <p>未找到相关内容</p>
    </div>

    <!-- 初始状态 -->
    <div v-else-if="!loading && !searched" class="initial-state">
      <el-icon><Search /></el-icon>
      <h3>输入关键词开始检索</h3>
      <p>支持语义检索和RAG增强查询两种模式</p>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import MarkdownIt from 'markdown-it'
import { ElMessage } from 'element-plus'
import * as ragApi from '@/api/rag'
import type { RetrievalResult, RagResult } from '@/types/knowledge'
import { formatSimilarity } from '@/utils'

const query = ref('')
const searchMode = ref<'semantic' | 'rag'>('semantic')
const topK = ref(5)
const loading = ref(false)
const searched = ref(false)

const searchResults = ref<RetrievalResult[]>([])
const ragResult = ref<RagResult | null>(null)

const md = new MarkdownIt({
  html: true,
  linkify: true,
  breaks: true
})

const renderedAnswer = computed(() => {
  if (ragResult.value?.answer) {
    return md.render(ragResult.value.answer)
  }
  return ''
})

const handleSearch = async () => {
  if (!query.value.trim()) {
    ElMessage.warning('请输入检索内容')
    return
  }

  loading.value = true
  searched.value = true
  searchResults.value = []
  ragResult.value = null

  try {
    if (searchMode.value === 'semantic') {
      // 语义检索
      searchResults.value = await ragApi.search({
        query: query.value,
        topK: topK.value
      })
    } else {
      // RAG查询
      ragResult.value = await ragApi.ragQuery(query.value)
      if (ragResult.value.retrievalResults) {
        searchResults.value = ragResult.value.retrievalResults
      }
    }
  } catch (error) {
    ElMessage.error('检索失败')
  } finally {
    loading.value = false
  }
}
</script>

<style lang="scss" scoped>
.knowledge-search {
  max-width: 900px;
  margin: 0 auto;
}

.page-header {
  margin-bottom: 24px;

  .page-title {
    margin: 0;
  }
}

.search-section {
  background: #fff;
  border-radius: 12px;
  padding: 24px;
  margin-bottom: 24px;
}

.search-bar {
  display: flex;
  gap: 12px;

  .el-input {
    flex: 1;
  }
}

.search-options {
  display: flex;
  align-items: center;
  gap: 24px;
  margin-top: 16px;

  .option-item {
    display: flex;
    align-items: center;
    gap: 8px;
    font-size: 14px;
    color: #606266;
  }
}

.rag-answer {
  background: linear-gradient(135deg, #ecf5ff, #f5f9ff);
  border-radius: 12px;
  padding: 20px;
  margin-bottom: 24px;
  border: 1px solid #d9ecff;

  .answer-header {
    display: flex;
    align-items: center;
    gap: 8px;
    margin-bottom: 12px;
    font-size: 15px;
    font-weight: 500;
    color: #409eff;
  }

  .answer-content {
    font-size: 14px;
    line-height: 1.8;
    color: #303133;
  }

  .answer-meta {
    margin-top: 12px;
    font-size: 12px;
    color: #909399;
  }
}

.search-results {
  background: #fff;
  border-radius: 12px;
  padding: 24px;
}

.results-header {
  margin-bottom: 16px;

  h3 {
    margin: 0;
    font-size: 16px;
    color: #303133;
  }
}

.result-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.result-item {
  background: #f9f9f9;
  border-radius: 8px;
  padding: 16px;
  transition: all 0.3s;

  &:hover {
    background: #f5f7fa;
  }

  .result-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 8px;
  }

  .result-title {
    font-size: 15px;
    font-weight: 500;
    color: #303133;
  }

  .result-content {
    font-size: 14px;
    color: #606266;
    line-height: 1.6;
    display: -webkit-box;
    -webkit-line-clamp: 3;
    -webkit-box-orient: vertical;
    overflow: hidden;
  }

  .result-meta {
    display: flex;
    flex-wrap: wrap;
    gap: 6px;
    margin-top: 12px;
  }
}

.empty-result,
.initial-state {
  background: #fff;
  border-radius: 12px;
  padding: 60px 24px;
  text-align: center;

  .el-icon {
    font-size: 48px;
    color: #c0c4cc;
    margin-bottom: 16px;
  }

  h3 {
    font-size: 18px;
    color: #303133;
    margin: 0 0 8px;
  }

  p {
    color: #909399;
    margin: 0;
  }
}
</style>
