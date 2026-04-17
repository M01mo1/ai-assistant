package com.haigui.ai.assistant.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "milvus")
public class MilvusConfig {
    private String host = "localhost";
    private int port = 19530;
    private String collectionName = "kefu_knowledge_base";
    private int dimension = 1024;
}
