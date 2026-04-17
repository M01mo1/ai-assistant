import dayjs from 'dayjs'
import relativeTime from 'dayjs/plugin/relativeTime'
import 'dayjs/locale/zh-cn'

dayjs.extend(relativeTime)
dayjs.locale('zh-cn')

/**
 * 格式化时间
 */
export const formatTime = (time: string | Date, format = 'YYYY-MM-DD HH:mm:ss'): string => {
  return dayjs(time).format(format)
}

/**
 * 相对时间
 */
export const fromNow = (time: string | Date): string => {
  return dayjs(time).fromNow()
}

/**
 * 生成唯一ID
 */
export const generateId = (): string => {
  return `${Date.now()}_${Math.random().toString(36).substring(2, 11)}`
}

/**
 * 防抖函数
 */
export const debounce = <T extends (...args: any[]) => any>(
  fn: T,
  delay: number
): ((...args: Parameters<T>) => void) => {
  let timer: ReturnType<typeof setTimeout>
  return (...args: Parameters<T>) => {
    clearTimeout(timer)
    timer = setTimeout(() => fn(...args), delay)
  }
}

/**
 * 节流函数
 */
export const throttle = <T extends (...args: any[]) => any>(
  fn: T,
  delay: number
): ((...args: Parameters<T>) => void) => {
  let lastTime = 0
  return (...args: Parameters<T>) => {
    const now = Date.now()
    if (now - lastTime >= delay) {
      fn(...args)
      lastTime = now
    }
  }
}

/**
 * 复制文本到剪贴板
 */
export const copyToClipboard = async (text: string): Promise<boolean> => {
  try {
    await navigator.clipboard.writeText(text)
    return true
  } catch {
    // 降级方案
    const textArea = document.createElement('textarea')
    textArea.value = text
    textArea.style.position = 'fixed'
    textArea.style.opacity = '0'
    document.body.appendChild(textArea)
    textArea.select()
    const success = document.execCommand('copy')
    document.body.removeChild(textArea)
    return success
  }
}

/**
 * 文档类型映射
 */
export const documentTypeMap: Record<string, string> = {
  MANUAL: '手动添加',
  FEISHU_DOC: '飞书文档',
  IMPORTED: '导入文档'
}

/**
 * 获取文档类型名称
 */
export const getDocumentTypeName = (type: string): string => {
  return documentTypeMap[type] || type
}

/**
 * 会话状态映射
 */
export const sessionStatusMap: Record<string, { label: string; type: string }> = {
  ACTIVE: { label: '进行中', type: 'success' },
  CLOSED: { label: '已结束', type: 'info' }
}

/**
 * 反馈类型映射
 */
export const feedbackTypeMap: Record<number, { label: string; icon: string }> = {
  1: { label: '有帮助', icon: 'CircleCheck' },
  0: { label: '无帮助', icon: 'CircleClose' },
  [-1]: { label: '有错误', icon: 'Warning' }
}

/**
 * 截断文本
 */
export const truncateText = (text: string, maxLength: number): string => {
  if (text.length <= maxLength) return text
  return text.substring(0, maxLength) + '...'
}

/**
 * 相似度百分比格式化
 */
export const formatSimilarity = (similarity: number | null | undefined): string => {
  if (similarity === null || similarity === undefined || isNaN(similarity)) {
    return 'N/A'
  }
  return (similarity * 100).toFixed(1) + '%'
}
