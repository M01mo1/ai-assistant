import request from './request'
import type {
  KnowledgeDocument,
  AddKnowledgeRequest,
  SyncFeishuRequest
} from '@/types/knowledge'

// 获取所有知识文档
export const getAllDocuments = (): Promise<KnowledgeDocument[]> => {
  return request.get('/api/knowledge/documents')
}

// 根据类型获取知识文档
export const getDocumentsByType = (type: string): Promise<KnowledgeDocument[]> => {
  return request.get(`/api/knowledge/documents/type/${type}`)
}

// 添加知识文档
export const addDocument = (data: AddKnowledgeRequest): Promise<KnowledgeDocument> => {
  return request.post('/api/knowledge/documents', data)
}

// 同步飞书文档
export const syncFeishuDocument = (data: SyncFeishuRequest): Promise<KnowledgeDocument> => {
  return request.post('/api/knowledge/feishu/sync', data)
}

// 禁用知识文档
export const disableDocument = (
  documentId: string
): Promise<{ success: boolean; message: string }> => {
  return request.post(`/api/knowledge/documents/${documentId}/disable`)
}

// 删除知识文档
export const deleteDocument = (
  documentId: string
): Promise<{ success: boolean; message: string }> => {
  return request.delete(`/api/knowledge/documents/${documentId}`)
}
