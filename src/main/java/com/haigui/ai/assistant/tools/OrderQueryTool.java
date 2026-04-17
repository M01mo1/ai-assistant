package com.haigui.ai.assistant.tools;

import com.alibaba.dashscope.utils.JsonUtils;
import com.alibaba.fastjson2.JSONObject;
import com.haigui.ai.assistant.model.entity.Order;
import com.haigui.ai.assistant.model.entity.OrderItem;
import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 订单查询工具 - 提供订单相关信息查询能力
 */
@Slf4j
@Component
public class OrderQueryTool {

    /**
     * 根据订单ID查询订单详情
     */
    @Tool(name = "queryOrderById", value = "根据订单ID查询订单详细信息，包括订单状态、支付信息、收货信息等")
    public Order queryOrderById(@P("订单ID") String orderId) {
        log.info("查询订单详情, orderId: {}", orderId);

        // TODO: 实际实现需要调用订单服务API
        // 这里返回模拟数据
        Order order =    Order.builder()
                .orderId(orderId)
                .userId("U123456")
                .orderStatus("SHIPPED")
                .orderAmount(new BigDecimal("999.00"))
                .payAmount(new BigDecimal("899.00"))
                .paymentMethod("支付宝")
                .payTime(LocalDateTime.now().minusDays(2))
                .createTime(LocalDateTime.now().minusDays(3))
                .shippingAddress("上海市浦东新区张江高科技园区")
                .receiverName("张三")
                .receiverPhone("138****8888")
                .trackingNumber("SF1234567890")
                .expressCompany("顺丰速运")
                .shipTime(LocalDateTime.now().minusDays(1))
                .productName("Nike Air Jordan 1 运动鞋")
                .orderItems(List.of(
                        OrderItem.builder()
                                .skuId("SKU001")
                                .productName("Nike Air Jordan 1 运动鞋")
                                .specification("黑红色 42码")
                                .unitPrice(new BigDecimal("999.00"))
                                .quantity(1)
                                .brandName("Nike")
                                .category("运动鞋")
                                .build()
                ))
                .promotionInfo("新用户优惠券减100元")
                .isPresale(false)
                .build();
        return order;
    }

    /**
     * 根据用户ID查询订单列表
     */
    @Tool(name = "queryOrdersByUserId", value = "根据用户ID查询该用户的订单列表")
    public List<Order> queryOrdersByUserId(@P("用户ID") String userId,
                                           @P("订单状态，可选值：ALL/UNPAID/PAID/SHIPPED/DELIVERED/COMPLETED") String status) {
        log.info("查询用户订单列表, userId: {}, status: {}", userId, status);

        // TODO: 实际实现需要调用订单服务API
        return List.of(
                Order.builder()
                        .orderId("ORD202312001")
                        .userId(userId)
                        .orderStatus("COMPLETED")
                        .orderAmount(new BigDecimal("1299.00"))
                        .productName("Adidas Yeezy 350")
                        .createTime(LocalDateTime.now().minusDays(30))
                        .build(),
                Order.builder()
                        .orderId("ORD202312002")
                        .userId(userId)
                        .orderStatus("SHIPPED")
                        .orderAmount(new BigDecimal("899.00"))
                        .productName("Nike Dunk Low")
                        .createTime(LocalDateTime.now().minusDays(5))
                        .trackingNumber("SF9876543210")
                        .build()
        );
    }

    /**
     * 查询订单状态变更历史
     */
    @Tool(name = "queryOrderStatusHistory", value = "查询订单状态变更历史，用于追踪订单流转情况")
    public String queryOrderStatusHistory(@P("订单ID") String orderId) {
        log.info("查询订单状态历史, orderId: {}", orderId);

        // TODO: 实际实现需要调用订单服务API
        return String.format("""
                订单 %s 状态变更历史：
                1. 2024-12-16 10:00:00 - 订单创建
                2. 2024-12-16 10:05:00 - 支付成功
                3. 2024-12-17 14:00:00 - 商家发货
                4. 2024-12-17 16:00:00 - 快递揽收
                当前状态：运输中
                """, orderId);
    }

    /**
     * 检查订单是否支持退款
     */
    @Tool(name = "checkRefundEligibility", value = "检查订单是否满足退款条件，返回是否可退款及原因")
    public String checkRefundEligibility(@P("订单ID") String orderId) {
        log.info("检查订单退款资格, orderId: {}", orderId);

        // TODO: 实际实现需要调用订单服务和售后规则引擎
        return String.format("""
                订单 %s 退款资格检查结果：
                - 可否退款：是
                - 退款类型：仅退款/退货退款均支持
                - 退款金额上限：899.00元
                - 注意事项：
                  1. 商品已发货，如申请仅退款需等待卖家同意
                  2. 如申请退货退款，需在签收后7天内发起
                  3. 商品需保持原包装完好
                """, orderId);
    }
}
