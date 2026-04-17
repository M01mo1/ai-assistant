import request from './request'
import type { RetrievalResult, RagResult, SearchRequest } from '@/types/knowledge'

// 语义检索
export const search = (data: SearchRequest): Promise<RetrievalResult[]> => {
  return request.post('/api/rag/search', data)
}

// RAG增强查询
export const ragQuery = (query: string): Promise<RagResult> => {
  return request.post('/api/rag/query', { query })
}
