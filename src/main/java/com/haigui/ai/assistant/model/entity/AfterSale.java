package com.haigui.ai.assistant.model.entity;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 售后/逆向信息实体类
 */
@Data
@Builder
public class AfterSale {
    
    /**
     * 售后单ID
     */
    private String afterSaleId;
    
    /**
     * 关联订单ID
     */
    private String orderId;
    
    /**
     * 用户ID
     */
    private String userId;
    
    /**
     * 售后类型
     * REFUND_ONLY-仅退款, RETURN_REFUND-退货退款, EXCHANGE-换货
     */
    private String afterSaleType;
    
    /**
     * 售后状态
     * APPLIED-已申请, APPROVED-已通过, REJECTED-已拒绝,
     * RETURNING-退货中, RECEIVED-已收货, REFUNDING-退款中,
     * COMPLETED-已完成, CANCELLED-已取消
     */
    private String status;
    
    /**
     * 申请原因
     */
    private String reason;
    
    /**
     * 详细描述
     */
    private String description;
    
    /**
     * 退款金额
     */
    private BigDecimal refundAmount;
    
    /**
     * 申请时间
     */
    private LocalDateTime applyTime;
    
    /**
     * 审核时间
     */
    private LocalDateTime auditTime;
    
    /**
     * 完成时间
     */
    private LocalDateTime completeTime;
    
    /**
     * 审核人
     */
    private String auditor;
    
    /**
     * 审核备注
     */
    private String auditRemark;
    
    /**
     * 退货物流单号
     */
    private String returnTrackingNumber;
    
    /**
     * 退货物流公司
     */
    private String returnExpressCompany;
    
    /**
     * 图片凭证
     */
    private String[] imageUrls;
}
