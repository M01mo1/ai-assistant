package com.haigui.ai.assistant.tools;

import com.haigui.ai.assistant.model.entity.WorkOrder;
import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 工单查询工具 - 提供历史工单检索能力
 */
@Slf4j
@Component
public class WorkOrderQueryTool {

    /**
     * 查询相似历史工单
     */
    @Tool(name = "querySimilarWorkOrders", value = "根据问题描述查询相似的历史工单，用于参考处理方案")
    public List<WorkOrder> querySimilarWorkOrders(@P("问题描述关键词") String keywords,
                                                  @P("工单类型") String workOrderType) {
        log.info("查询相似工单, keywords: {}, type: {}", keywords, workOrderType);

        // TODO: 实际实现需要调用工单服务并结合向量检索
        return List.of(
                WorkOrder.builder()
                        .workOrderId("WO202311001")
                        .orderId("ORD202311001")
                        .workOrderType(workOrderType)
                        .status("RESOLVED")
                        .problemDescription("用户反馈物流显示已签收但未收到货")
                        .resolution("经核实为快递员误操作，已联系快递公司重新派送，用户已收到货物")
                        .createTime(LocalDateTime.now().minusDays(30))
                        .resolveTime(LocalDateTime.now().minusDays(29))
                        .handler("客服小王")
                        .aiSuggestion("1. 核实物流轨迹 2. 联系快递公司核实签收情况 3. 如确认未收到，协调重新派送或退款")
                        .satisfactionScore(5)
                        .build(),
                WorkOrder.builder()
                        .workOrderId("WO202311002")
                        .orderId("ORD202311002")
                        .workOrderType(workOrderType)
                        .status("RESOLVED")
                        .problemDescription("用户反馈包裹显示签收但实际放在驿站")
                        .resolution("已联系驿站确认包裹在柜，短信通知用户取件码，用户已成功取件")
                        .createTime(LocalDateTime.now().minusDays(25))
                        .resolveTime(LocalDateTime.now().minusDays(25))
                        .handler("客服小李")
                        .satisfactionScore(4)
                        .build()
        );
    }

    /**
     * 查询工单详情
     */
    @Tool(name = "queryWorkOrderById", value = "根据工单ID查询工单详细信息")
    public WorkOrder queryWorkOrderById(@P("工单ID") String workOrderId) {
        log.info("查询工单详情, workOrderId: {}", workOrderId);

        return WorkOrder.builder()
                .workOrderId(workOrderId)
                .orderId("ORD202312001")
                .userId("U123456")
                .workOrderType("LOGISTICS_ISSUE")
                .status("PROCESSING")
                .problemDescription("物流3天没有更新")
                .createTime(LocalDateTime.now().minusHours(2))
                .handler("AI助手")
                .priority(2)
                .tags(List.of("物流问题", "超时预警"))
                .aiDiagnosis("物流可能出现中转延迟，建议联系快递公司核实")
                .build();
    }

    /**
     * 统计工单处理情况
     */
    @Tool(name = "getWorkOrderStatistics", value = "获取工单处理统计数据")
    public String getWorkOrderStatistics(@P("统计周期：TODAY/WEEK/MONTH") String period) {
        log.info("获取工单统计, period: {}", period);

        return String.format("""
                工单统计报告（%s）：
                
                总体情况：
                - 新建工单：128件
                - 已解决：102件
                - 处理中：20件
                - 待处理：6件
                - 解决率：79.7%%
                
                问题分类：
                - 物流问题：45件 (35.2%%)
                - 退款问题：32件 (25.0%%)
                - 商品问题：28件 (21.9%%)
                - 订单问题：15件 (11.7%%)
                - 其他：8件 (6.2%%)
                
                处理效率：
                - 平均响应时间：5分钟
                - 平均解决时间：2.3小时
                - 一次解决率：68%%
                
                用户满意度：
                - 满意度评分：4.5/5.0
                - 好评率：89%%
                """, period);
    }

    /**
     * 查询用户历史工单
     */
    @Tool(name = "queryUserWorkOrders", value = "查询指定用户的历史工单记录")
    public List<WorkOrder> queryUserWorkOrders(@P("用户ID") String userId) {
        log.info("查询用户工单历史, userId: {}", userId);

        return List.of(
                WorkOrder.builder()
                        .workOrderId("WO202312001")
                        .userId(userId)
                        .workOrderType("ORDER_INQUIRY")
                        .status("RESOLVED")
                        .problemDescription("咨询订单发货时间")
                        .resolution("已告知预计发货时间，用户表示理解")
                        .createTime(LocalDateTime.now().minusDays(10))
                        .resolveTime(LocalDateTime.now().minusDays(10))
                        .satisfactionScore(5)
                        .build()
        );
    }
}