package com.haigui.ai.assistant.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 物流轨迹节点
 */
@Data
@Builder
public class LogisticsTrace {
    
    /**
     * 轨迹时间
     */
    private LocalDateTime traceTime;
    
    /**
     * 轨迹描述
     */
    private String description;
    
    /**
     * 轨迹位置
     */
    private String location;
    
    /**
     * 操作人
     */
    private String operator;
    
    /**
     * 联系电话
     */
    private String phone;
}
