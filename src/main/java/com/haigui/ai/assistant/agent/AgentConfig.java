package com.haigui.ai.assistant.agent;

import com.haigui.ai.assistant.memoery.MongoChatMemoryProvider;
import com.haigui.ai.assistant.tools.*;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.service.AiServices;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class AgentConfig {

    @Resource
    private ChatLanguageModel chatLanguageModel;

    @Resource
    private MongoChatMemoryProvider chatMemoryProvider;

    // 注入所有工具
    @Resource
    private OrderQueryTool orderQueryTool;

    @Resource
    private LogisticsTrackingTool logisticsTrackingTool;

    @Resource
    private BusinessRuleTool businessRuleTool;

    @Resource
    private AfterSaleQueryTool afterSaleQueryTool;

    @Resource
    private AlertDetectionTool alertDetectionTool;

    @Resource
    private WorkOrderQueryTool workOrderQueryTool;

    @Bean
    public CustomerServiceAssistant customerServiceAssistant() {
        return AiServices.builder(CustomerServiceAssistant.class)
                .chatLanguageModel(chatLanguageModel)
                .chatMemoryProvider(chatMemoryProvider)
                .tools(
                        orderQueryTool,
                        logisticsTrackingTool,
                        businessRuleTool,
                        afterSaleQueryTool,
                        alertDetectionTool,
                        workOrderQueryTool
                )
                .build();
    }

}