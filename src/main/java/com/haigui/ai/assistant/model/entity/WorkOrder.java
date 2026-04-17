package com.haigui.ai.assistant.model.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 工单实体类 - 客服工单记录
 */
@Data
@Builder
@Document(collection = "work_orders")
public class WorkOrder {
    
    /**
     * 工单ID
     */
    @Id
    private String workOrderId;
    
    /**
     * 关联订单ID
     */
    private String orderId;
    
    /**
     * 用户ID
     */
    private String userId;
    
    /**
     * 工单类型
     * ORDER_INQUIRY-订单咨询, LOGISTICS_ISSUE-物流问题, 
     * REFUND_REQUEST-退款申请, PRODUCT_QUALITY-商品质量,
     * AFTER_SALE-售后服务, COMPLAINT-投诉建议, OTHER-其他
     */
    private String workOrderType;
    
    /**
     * 工单状态
     * PENDING-待处理, PROCESSING-处理中, RESOLVED-已解决, 
     * CLOSED-已关闭, ESCALATED-已升级
     */
    private String status;
    
    /**
     * 问题描述
     */
    private String problemDescription;
    
    /**
     * 处理结果
     */
    private String resolution;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
    
    /**
     * 解决时间
     */
    private LocalDateTime resolveTime;
    
    /**
     * 处理人
     */
    private String handler;
    
    /**
     * 优先级 1-低 2-中 3-高 4-紧急
     */
    private Integer priority;
    
    /**
     * 标签列表
     */
    private List<String> tags;
    
    /**
     * AI诊断结果
     */
    private String aiDiagnosis;
    
    /**
     * AI建议处理方案
     */
    private String aiSuggestion;
    
    /**
     * 用户满意度评分
     */
    private Integer satisfactionScore;
    
    /**
     * 知识来源（溯源）
     */
    private List<String> knowledgeSources;
}
