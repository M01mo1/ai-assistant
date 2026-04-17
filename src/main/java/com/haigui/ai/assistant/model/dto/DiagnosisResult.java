package com.haigui.ai.assistant.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 问题诊断结果DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DiagnosisResult {
    
    /**
     * 问题类型
     */
    private String problemType;
    
    /**
     * 问题描述
     */
    private String problemDescription;
    
    /**
     * 根因分析
     */
    private String rootCause;
    
    /**
     * 影响范围
     */
    private String impactScope;
    
    /**
     * 紧急程度 1-低 2-中 3-高 4-紧急
     */
    private Integer urgency;
    
    /**
     * 建议处理方案列表
     */
    private List<SolutionSuggestion> suggestions;
    
    /**
     * 是否需要人工介入
     */
    private Boolean requireHumanIntervention;
    
    /**
     * 相关工单（如有历史类似问题）
     */
    private List<String> relatedWorkOrders;
    
    /**
     * 置信度 0-100
     */
    private Integer confidence;
}
