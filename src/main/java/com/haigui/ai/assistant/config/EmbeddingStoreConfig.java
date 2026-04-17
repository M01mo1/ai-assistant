package com.haigui.ai.assistant.config;

import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.milvus.MilvusEmbeddingStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class EmbeddingStoreConfig {

    private final MilvusConfig milvusConfig;

    /**
     * 创建向量存储
     */
    @Bean
    public EmbeddingStore<TextSegment> embeddingStore() {
        log.info("初始化Milvus向量存储, host: {}, port: {}, collection: {}",
                milvusConfig.getHost(), milvusConfig.getPort(), milvusConfig.getCollectionName());

        return MilvusEmbeddingStore.builder()
                .host(milvusConfig.getHost())
                .port(milvusConfig.getPort())
                .collectionName(milvusConfig.getCollectionName())
                .dimension(milvusConfig.getDimension())
                .build();
    }
}
