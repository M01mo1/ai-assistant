package com.haigui.ai.assistant.model.entity;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 物流信息实体类
 */
@Data
@Builder
public class Logistics {
    
    /**
     * 物流单号
     */
    private String trackingNumber;
    
    /**
     * 关联订单ID
     */
    private String orderId;
    
    /**
     * 物流公司编码
     */
    private String expressCode;
    
    /**
     * 物流公司名称
     */
    private String expressName;
    
    /**
     * 物流状态
     * COLLECTED-已揽收, IN_TRANSIT-运输中, DELIVERING-派送中, 
     * SIGNED-已签收, REJECTED-已拒收, EXCEPTION-异常
     */
    private String status;
    
    /**
     * 当前位置
     */
    private String currentLocation;
    
    /**
     * 预计送达时间
     */
    private LocalDateTime estimatedDeliveryTime;
    
    /**
     * 实际签收时间
     */
    private LocalDateTime actualDeliveryTime;
    
    /**
     * 物流轨迹列表
     */
    private List<LogisticsTrace> traces;
    
    /**
     * 发货仓库
     */
    private String warehouse;
    
    /**
     * 发货城市
     */
    private String shipCity;
    
    /**
     * 收货城市
     */
    private String receiveCity;
    
    /**
     * 异常原因
     */
    private String exceptionReason;
}
