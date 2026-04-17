// 知识文档
export interface KnowledgeDocument {
  documentId: string
  feishuDocToken?: string
  title: string
  documentType: string
  rawContent: string
  summary?: string
  source?: string
  url?: string
  createTime: string
  updateTime: string
  syncTime?: string
  version: number
  vectorIds?: string[]
  chunkCount: number
  tags?: string[]
  metadata?: Record<string, string>
  enabled: boolean
}

// 检索结果
export interface RetrievalResult {
  documentId: string
  title: string
  content: string
  similarity: number
  metadata?: Record<string, any>
}

// RAG结果
export interface RagResult {
  answer: string
  retrievalResults: RetrievalResult[]
  tokenUsage?: number
  success: boolean
  errorMessage?: string
}

// 添加知识文档请求
export interface AddKnowledgeRequest {
  title: string
  content: string
  documentType?: string
  tags?: string[]
  metadata?: Record<string, string>
}

// 同步飞书文档请求
export interface SyncFeishuRequest {
  docToken: string
  documentType?: string
  tags?: string[]
}

// 检索请求
export interface SearchRequest {
  query: string
  topK?: number
}
