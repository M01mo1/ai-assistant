package com.haigui.ai.assistant.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * 健康检查控制器
 */
@RestController
@RequestMapping("/api")
public class HealthController {

    /**
     * 健康检查
     * GET /api/health
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        return ResponseEntity.ok(Map.of(
                "status", "UP",
                "service", "ai-assistant-langchain4j",
                "timestamp", LocalDateTime.now().toString()
        ));
    }

    /**
     * 服务信息
     * GET /api/info
     */
    @GetMapping("/info")
    public ResponseEntity<Map<String, Object>> info() {
        return ResponseEntity.ok(Map.of(
                "name", "电商交易链路AI客服工单智能助手",
                "version", "1.0.0",
                "description", "基于LangChain4j的智能客服辅助系统",
                "features", new String[]{
                        "订单问题诊断",
                        "物流追踪分析",
                        "售后政策解读",
                        "知识库检索(RAG)",
                        "多轮对话记忆",
                        "知识溯源"
                }
        ));
    }
}
