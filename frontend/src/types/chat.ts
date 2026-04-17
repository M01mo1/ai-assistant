// 对话请求
export interface ChatRequest {
  sessionId?: string
  userId: string
  message: string
  orderId?: string
  enableRag?: boolean
  enableTools?: boolean
}

// 对话响应
export interface ChatResponse {
  sessionId: string
  messageId: string
  content: string
  responseTime: string
  knowledgeSources?: KnowledgeSource[]
  toolInvocations?: ToolInvocation[]
  tokenUsage?: number
  diagnosisResult?: DiagnosisResult
  success: boolean
  errorMessage?: string
}

// 对话消息
export interface ChatMessage {
  messageId: string
  sessionId: string
  role: 'USER' | 'ASSISTANT' | 'SYSTEM'
  content: string
  createTime: string
  tokenCount?: number
  knowledgeSources?: KnowledgeSource[]
  toolInvocations?: ToolInvocation[]
  feedback?: number
  feedbackDetail?: string
}

// 对话会话
export interface ChatSession {
  sessionId: string
  userId: string
  title: string
  orderId?: string
  status: 'ACTIVE' | 'CLOSED'
  createTime: string
  lastUpdateTime: string
  messageCount: number
  summary?: string
  tags?: string[]
}

// 知识来源
export interface KnowledgeSource {
  documentId: string
  documentTitle: string
  documentType?: string
  contentSnippet: string
  score: number
  documentUrl?: string
  location?: string
}

// 工具调用
export interface ToolInvocation {
  toolName: string
  input: string
  output: string
  executionTime: number
}

// 诊断结果
export interface DiagnosisResult {
  issueType: string
  severity: string
  description: string
  suggestions: string[]
}

// 反馈请求
export interface FeedbackRequest {
  feedback: number
  detail?: string
}
