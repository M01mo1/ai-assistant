package com.haigui.ai.assistant.tools;

import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 预警检测工具 - 提供风险预警和异常检测能力
 */
@Slf4j
@Component
public class AlertDetectionTool {

    private final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * 检测订单风险
     */
    @Tool(name = "detectOrderRisk", value = "检测订单是否存在风险，如欺诈、异常行为等")
    public String detectOrderRisk(@P("订单ID") String orderId) {
        log.info("检测订单风险, orderId: {}", orderId);

        return String.format("""
                订单 %s 风险检测报告：
                                
                检测时间：%s
                                
                风险评估：
                - 综合风险等级：低
                - 欺诈风险：无
                - 刷单风险：无
                - 异常行为：无
                                
                详细分析：
                1. 用户账号正常，无异常登录
                2. 收货地址为常用地址
                3. 支付行为正常
                4. 订单金额在用户消费习惯范围内
                                
                建议：正常处理
                """, orderId, LocalDateTime.now().format(FORMATTER));
    }

    /**
     * 检测用户异常行为
     */
    @Tool(name = "detectUserAnomaly", value = "检测用户是否存在异常行为模式")
    public String detectUserAnomaly(@P("用户ID") String userId) {
        log.info("检测用户异常行为, userId: {}", userId);

        return String.format("""
                用户 %s 行为分析报告：
                                
                检测时间：%s
                                
                用户画像：
                - 注册时长：180天
                - 历史订单：15笔
                - 售后比例：13%% （行业平均10%%）
                - 投诉记录：0次
                                
                异常指标：
                - 高频退款：否
                - 恶意投诉：否
                - 账号共享：否
                - 地址异常：否
                                
                信用评估：
                - 信用等级：良好
                - 信用分：85/100
                                
                建议：
                - 该用户信用良好，可正常处理其请求
                - 售后比例略高于平均，但在合理范围内
                """, userId, LocalDateTime.now().format(FORMATTER));
    }

    /**
     * 检测系统预警
     */
    @Tool(name = "checkSystemAlerts", value = "检查当前是否有影响订单处理的系统预警")
    public String checkSystemAlerts() {
        log.info("检查系统预警");

        return String.format("""
                系统预警检查报告：
                                
                检测时间：%s
                                
                当前预警状态：
                                
                1. 支付系统：✅ 正常
                2. 物流系统：✅ 正常
                3. 库存系统：✅ 正常
                4. 鉴定中心：✅ 正常
                5. 退款系统：✅ 正常
                                
                近期通知：
                - 无影响订单处理的系统公告
                                
                计划维护：
                - 12月25日 02:00-04:00 支付系统例行维护（不影响当前处理）
                """, LocalDateTime.now().format(FORMATTER));
    }

    /**
     * 检测物流预警
     */
    @Tool(name = "checkLogisticsAlerts", value = "检查物流相关的预警信息，如区域限行、天气影响等")
    public String checkLogisticsAlerts(@P("收货省份") String province) {
        log.info("检查物流预警, province: {}", province);

        return String.format("""
                %s 物流预警信息：
                                
                检测时间：%s
                                
                天气影响：
                - 当前无恶劣天气预警
                - 未来3天天气正常，不影响配送
                                
                区域限制：
                - 无限行政策
                - 无疫情管控
                                
                物流运力：
                - 各主要快递公司运力正常
                - 预计配送时效：正常
                                
                节假日提醒：
                - 临近元旦假期，12月30日-1月1日可能出现轻微延迟
                """, province, LocalDateTime.now().format(FORMATTER));
    }

    /**
     * 检测价格异常
     */
    @Tool(name = "detectPriceAnomaly", value = "检测订单价格是否存在异常")
    public String detectPriceAnomaly(@P("订单ID") String orderId,
                                     @P("商品SKU") String skuId,
                                     @P("成交价格") String price) {
        log.info("检测价格异常, orderId: {}, skuId: {}, price: {}", orderId, skuId, price);

        return String.format("""
                订单 %s 价格异常检测：
                                
                商品SKU：%s
                成交价格：%s元
                                
                价格分析：
                - 历史均价：950元
                - 价格波动：-5.3%% （正常范围内）
                - 是否异常：否
                                
                优惠分析：
                - 新用户优惠券：-100元
                - 平台补贴：无
                - 优惠合规：是
                                
                结论：价格正常，无异常风险
                """, orderId, skuId, price);
    }
}
