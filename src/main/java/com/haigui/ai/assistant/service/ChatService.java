package com.haigui.ai.assistant.service;

import com.haigui.ai.assistant.agent.CustomerServiceAssistant;
import com.haigui.ai.assistant.memoery.ChatMemoryService;
import com.haigui.ai.assistant.model.dto.ChatRequest;
import com.haigui.ai.assistant.model.dto.ChatResponse;
import com.haigui.ai.assistant.model.entity.ChatMessage;
import com.haigui.ai.assistant.model.entity.ChatSession;
import com.haigui.ai.assistant.model.entity.KnowledgeSource;
import com.haigui.ai.assistant.rag.RagPipelineService;
import com.haigui.ai.assistant.rag.RagResult;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

/**
 * 对话服务 - 核心业务逻辑
 * 消息的持久化由 LangChain4j 通过 ChatMemoryStore 自动处理
 */
@Slf4j
@Service
public class ChatService {

    @Resource
    private CustomerServiceAssistant customerServiceAssistant;

    @Resource
    private ChatMemoryService chatMemoryService;

    @Resource
    private RagPipelineService ragPipelineService;

    public ChatResponse chat(ChatRequest request) {
        log.info("处理对话请求, userId: {}, sessionId: {}", request.getUserId(), request.getSessionId());

        try {
            // 1. 获取或创建会话
            ChatSession session = chatMemoryService.getOrCreateSession(
                    request.getSessionId(),
                    request.getUserId(),
                    request.getOrderId()
            );

            String sessionId = session.getSessionId();
            String userMessage = request.getMessage();

            // 2. 执行RAG检索（如果启用）
            List<KnowledgeSource> knowledgeSources = null;
            String ragContext = null;

            if (Boolean.TRUE.equals(request.getEnableRag())) {
                RagResult ragResult = ragPipelineService.executeRag(userMessage);
                knowledgeSources = ragResult.getKnowledgeSources();

                if (!ragResult.getRetrievalResults().isEmpty()) {
                    // 获取RAG上下文，作为系统消息的一部分
                    ragContext = ragResult.getAugmentedContext();
                }
            }

            // 3. 调用智能体生成回复
            // 准备增强后的用户消息（包含订单信息）
            String enhancedMessage = userMessage;
            if (StringUtils.isNotBlank(request.getOrderId())) {
                enhancedMessage = userMessage + "\n当前用户正在咨询订单：" + request.getOrderId();
            }

            // 获取精准到秒的当前时间
            String currentTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

            String aiResponse;
            if (ragContext != null) {
                aiResponse = customerServiceAssistant.chatWithRag(sessionId, enhancedMessage, ragContext, currentTime);
            } else {
                aiResponse = customerServiceAssistant.chat(sessionId, enhancedMessage, currentTime);
            }

            // 4. 保存用户消息和AI回复到业务层（chat_messages 集合，用于前端展示）
            chatMemoryService.saveUserMessage(sessionId, userMessage);
            ChatMessage assistantMessage = chatMemoryService.saveAssistantMessage(sessionId, aiResponse);

            // 5. 更新会话标题（使用用户首条消息作为标题）
            String title = userMessage.length() > 30
                    ? userMessage.substring(0, 30) + "..."
                    : userMessage;
            chatMemoryService.updateSessionTitle(sessionId, title);

            // 6. 获取 messageId 用于返回
            String messageId = assistantMessage != null ? assistantMessage.getMessageId() : UUID.randomUUID().toString();

            // 7. 构建响应
            return ChatResponse.builder()
                    .sessionId(sessionId)
                    .messageId(messageId)
                    .content(aiResponse)
                    .responseTime(LocalDateTime.now())
                    .knowledgeSources(knowledgeSources)
                    .success(true)
                    .build();

        } catch (Exception e) {
            log.error("对话处理失败", e);
            return ChatResponse.builder()
                    .sessionId(request.getSessionId())
                    .messageId(UUID.randomUUID().toString())
                    .content("抱歉，处理您的请求时出现了问题，请稍后重试或联系人工客服。")
                    .responseTime(LocalDateTime.now())
                    .success(false)
                    .errorMessage(e.getMessage())
                    .build();
        }
    }

    /**
     * 获取会话历史
     */
    public List<ChatMessage> getSessionHistory(String sessionId) {
        return chatMemoryService.getSessionMessages(sessionId);
    }

    /**
     * 获取用户会话列表
     */
    public List<ChatSession> getUserSessions(String userId) {
        return chatMemoryService.getUserSessions(userId);
    }

    /**
     * 关闭会话
     */
    public void closeSession(String sessionId) {
        chatMemoryService.closeSession(sessionId);
    }

    /**
     * 提交消息反馈
     */
    public void submitFeedback(String messageId, int feedback, String detail) {
        chatMemoryService.updateMessageFeedback(messageId, feedback, detail);
    }
}
