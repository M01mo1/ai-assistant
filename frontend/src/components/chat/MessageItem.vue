<template>
  <div class="message-item" :class="[`message-${message.role.toLowerCase()}`]">
    <!-- 头像 -->
    <div class="message-avatar">
      <el-avatar v-if="message.role === 'USER'" :size="36" class="avatar-user">
        <el-icon><User /></el-icon>
      </el-avatar>
      <el-avatar v-else :size="36" class="avatar-ai">
        <el-icon><Service /></el-icon>
      </el-avatar>
    </div>
    
    <!-- 消息内容 -->
    <div class="message-content">
      <div class="message-header">
        <span class="message-role">{{ message.role === 'USER' ? '用户' : 'AI助手' }}</span>
        <span class="message-time">{{ formatTime(message.createTime, 'HH:mm:ss') }}</span>
      </div>
      
      <div class="message-body">
        <!-- Markdown渲染 -->
        <div class="markdown-content" v-html="renderedContent"></div>
        
        <!-- 知识来源展示 -->
        <KnowledgeSourceList
          v-if="message.knowledgeSources && message.knowledgeSources.length > 0"
          :sources="message.knowledgeSources"
          class="message-sources"
        />
        
        <!-- 工具调用展示 -->
        <ToolInvocationList
          v-if="message.toolInvocations && message.toolInvocations.length > 0"
          :invocations="message.toolInvocations"
          class="message-tools"
        />
      </div>
      
      <!-- 反馈按钮 (仅AI消息) -->
      <FeedbackButtons
        v-if="message.role === 'ASSISTANT'"
        :message-id="message.messageId"
        :current-feedback="message.feedback"
        class="message-feedback"
      />
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import MarkdownIt from 'markdown-it'
import type { ChatMessage } from '@/types/chat'
import { formatTime } from '@/utils'
import KnowledgeSourceList from './KnowledgeSourceList.vue'
import ToolInvocationList from './ToolInvocationList.vue'
import FeedbackButtons from './FeedbackButtons.vue'

const props = defineProps<{
  message: ChatMessage
}>()

const md = new MarkdownIt({
  html: true,
  linkify: true,
  typographer: true,
  breaks: true
})

const renderedContent = computed(() => {
  return md.render(props.message.content || '')
})
</script>

<style lang="scss" scoped>
.message-item {
  display: flex;
  gap: 12px;
  padding: 16px 0;
  
  &.message-user {
    flex-direction: row-reverse;
    
    .message-content {
      align-items: flex-end;
    }
    
    .message-body {
      background: linear-gradient(135deg, #409eff, #53a8ff);
      color: #fff;
      
      :deep(.markdown-content) {
        a {
          color: #e6f0ff;
        }
        
        code {
          background: rgba(255, 255, 255, 0.2);
          color: #fff;
        }
        
        pre {
          background: rgba(0, 0, 0, 0.1);
        }
      }
    }
    
    .message-header {
      flex-direction: row-reverse;
    }
  }
  
  &.message-assistant {
    .message-body {
      background: #fff;
      border: 1px solid #e4e7ed;
    }
  }
}

.message-avatar {
  flex-shrink: 0;
  
  .avatar-user {
    background: linear-gradient(135deg, #409eff, #53a8ff);
  }
  
  .avatar-ai {
    background: linear-gradient(135deg, #67c23a, #95d475);
  }
}

.message-content {
  display: flex;
  flex-direction: column;
  max-width: 70%;
  min-width: 200px;
}

.message-header {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 6px;
  
  .message-role {
    font-size: 13px;
    font-weight: 500;
    color: #606266;
  }
  
  .message-time {
    font-size: 12px;
    color: #909399;
  }
}

.message-body {
  padding: 12px 16px;
  border-radius: 12px;
  line-height: 1.6;
  word-break: break-word;
  
  :deep(.markdown-content) {
    font-size: 14px;
    
    p {
      margin: 0;
      
      & + p {
        margin-top: 8px;
      }
    }
    
    ul, ol {
      margin: 8px 0;
      padding-left: 20px;
    }
  }
}

.message-sources,
.message-tools {
  margin-top: 12px;
}

.message-feedback {
  margin-top: 8px;
}
</style>
