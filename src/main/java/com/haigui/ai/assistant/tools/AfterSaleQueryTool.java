package com.haigui.ai.assistant.tools;

import com.haigui.ai.assistant.model.entity.AfterSale;
import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 逆向信息查询工具 - 提供售后、退款相关信息查询
 */
@Slf4j
@Component
public class AfterSaleQueryTool {

    /**
     * 查询售后单详情
     */
    @Tool(name = "queryAfterSaleById", value = "根据售后单ID查询售后详情")
    public AfterSale queryAfterSaleById(@P("售后单ID") String afterSaleId) {
        log.info("查询售后单详情, afterSaleId: {}", afterSaleId);

        // TODO: 实际实现需要调用售后服务API
        return AfterSale.builder()
                .afterSaleId(afterSaleId)
                .orderId("ORD202312001")
                .userId("U123456")
                .afterSaleType("RETURN_REFUND")
                .status("RETURNING")
                .reason("尺码不合适")
                .description("买的42码偏大，想换41码")
                .refundAmount(new BigDecimal("899.00"))
                .applyTime(LocalDateTime.now().minusDays(1))
                .auditTime(LocalDateTime.now().minusHours(20))
                .auditor("系统自动审核")
                .auditRemark("符合7天无理由退货条件，已通过")
                .returnTrackingNumber("YT9876543210")
                .returnExpressCompany("圆通速递")
                .build();
    }

    /**
     * 查询订单关联的售后记录
     */
    @Tool(name = "queryAfterSalesByOrderId", value = "查询订单关联的所有售后记录")
    public List<AfterSale> queryAfterSalesByOrderId(@P("订单ID") String orderId) {
        log.info("查询订单售后记录, orderId: {}", orderId);

        return List.of(
                AfterSale.builder()
                        .afterSaleId("AS202312001")
                        .orderId(orderId)
                        .afterSaleType("RETURN_REFUND")
                        .status("COMPLETED")
                        .reason("尺码不合适")
                        .refundAmount(new BigDecimal("899.00"))
                        .applyTime(LocalDateTime.now().minusDays(10))
                        .completeTime(LocalDateTime.now().minusDays(5))
                        .build()
        );
    }

    /**
     * 查询用户售后历史
     */
    @Tool(name = "queryUserAfterSaleHistory", value = "查询用户的售后申请历史记录")
    public String queryUserAfterSaleHistory(@P("用户ID") String userId) {
        log.info("查询用户售后历史, userId: {}", userId);

        return String.format("""
                用户 %s 售后历史统计：
                
                近30天记录：
                - 售后申请总数：2次
                - 仅退款：1次
                - 退货退款：1次
                - 申请成功率：100%%
                
                近90天记录：
                - 售后申请总数：5次
                - 仅退款：2次
                - 退货退款：3次
                - 申请成功率：80%%
                
                用户画像：
                - 售后频率：正常
                - 风险等级：低
                - 建议处理：正常流程处理
                """, userId);
    }

    /**
     * 查询退款进度
     */
    @Tool(name = "queryRefundProgress", value = "查询退款处理进度和预计到账时间")
    public String queryRefundProgress(@P("售后单ID") String afterSaleId) {
        log.info("查询退款进度, afterSaleId: {}", afterSaleId);

        return String.format("""
                售后单 %s 退款进度：
                
                当前状态：退货运输中
                
                进度详情：
                1. ✅ 用户申请退货退款 - 2024-12-17 10:00
                2. ✅ 平台审核通过 - 2024-12-17 10:30
                3. ✅ 用户已寄出退货 - 2024-12-17 14:00
                4. 🔄 退货运输中 - 预计12-19到达
                5. ⏳ 卖家验收 - 待处理
                6. ⏳ 退款到账 - 待处理
                
                预计退款到账时间：2024-12-21
                退款金额：899.00元
                退款方式：原路退回（支付宝）
                """, afterSaleId);
    }

    /**
     * 检查售后申请是否合规
     */
    @Tool(name = "checkAfterSaleCompliance", value = "检查售后申请是否符合平台规则")
    public String checkAfterSaleCompliance(@P("订单ID") String orderId,
                                           @P("售后类型：REFUND_ONLY/RETURN_REFUND/EXCHANGE") String afterSaleType,
                                           @P("申请原因") String reason) {
        log.info("检查售后合规性, orderId: {}, type: {}, reason: {}", orderId, afterSaleType, reason);

        return String.format("""
                订单 %s 售后申请合规检查：
                
                申请类型：%s
                申请原因：%s
                
                检查结果：✅ 符合条件
                
                详细说明：
                1. ✅ 订单状态支持申请（已签收）
                2. ✅ 在售后时效内（签收后第5天，7天内）
                3. ✅ 商品支持该售后类型
                4. ✅ 用户售后信用良好
                
                建议操作：
                - 可以正常受理该售后申请
                - 建议走标准退货退款流程
                """, orderId, afterSaleType, reason);
    }
}
