package com.haigui.ai.assistant.tools;

import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 业务规则检索工具 - 提供平台政策和业务规则查询能力
 */
@Slf4j
@Component
public class BusinessRuleTool {
    /**
     * 查询退款政策
     */
    @Tool(name = "queryRefundPolicy", value = "查询平台退款政策规则，包括退款条件、时效、流程等")
    public String queryRefundPolicy(@P("商品类目，如：运动鞋、服装、数码等") String category,
                                    @P("退款类型：REFUND_ONLY-仅退款, RETURN_REFUND-退货退款") String refundType) {
        log.info("查询退款政策, category: {}, refundType: {}", category, refundType);

        return String.format("""
                【%s类目 - %s 政策】
                
                一、申请条件：
                1. 未发货订单：支持随时申请仅退款
                2. 已发货未签收：需卖家同意后方可退款
                3. 已签收订单：签收后7天内可申请退货退款
                
                二、退款时效：
                1. 仅退款：卖家同意后1-3个工作日到账
                2. 退货退款：卖家确认收货后1-3个工作日到账
                
                三、特殊说明：
                1. 运动鞋类商品需保持原包装完好
                2. 鉴定为正品后才能发货的商品，鉴定后不支持无理由退款
                3. 定制类商品不支持7天无理由退款
                
                四、注意事项：
                1. 请在申请时如实填写退款原因
                2. 如有质量问题请上传相关图片凭证
                3. 退货需使用平台推荐的快递公司
                """, category, refundType.equals("REFUND_ONLY") ? "仅退款" : "退货退款");
    }

    /**
     * 查询售后SOP流程
     */
    @Tool(name = "queryAfterSaleSOP", value = "查询售后服务标准操作流程")
    public String queryAfterSaleSOP(@P("问题类型，如：商品质量问题、尺码不符、发错货、少件漏发等") String issueType) {
        log.info("查询售后SOP, issueType: {}", issueType);

        return String.format("""
                【%s - 标准处理流程】
                
                第一步：问题确认
                - 核实用户描述的问题
                - 查看订单信息和物流状态
                - 如有必要，请用户提供图片/视频凭证
                
                第二步：责任判定
                - 商品质量问题：卖家责任，全额退款+运费补偿
                - 尺码不符（无质量问题）：买家原因，买家承担退货运费
                - 发错货/少件：卖家责任，补发或全额退款
                
                第三步：解决方案
                1. 补偿方案：
                   - 轻微问题：优惠券补偿（5-30元）
                   - 一般问题：部分退款（10%%-30%%）
                   - 严重问题：全额退款+运费补偿
                
                2. 退货流程：
                   - 用户申请退货
                   - 平台审核通过
                   - 用户寄回商品
                   - 卖家确认收货
                   - 退款到账
                
                第四步：跟进回访
                - 处理完成后24小时内回访确认
                - 记录用户满意度
                """, issueType);
    }

    /**
     * 查询履约规则
     */
    @Tool(name = "queryFulfillmentRules", value = "查询订单履约规则，包括发货时效、鉴定流程等")
    public String queryFulfillmentRules(@P("查询类型：SHIPPING-发货规则, AUTHENTICATION-鉴定规则, DELIVERY-配送规则") String ruleType) {
        log.info("查询履约规则, ruleType: {}", ruleType);

        return switch (ruleType) {
            case "SHIPPING" -> """
                    【发货规则】
                    
                    1. 普通商品：
                       - 付款后48小时内发货
                       - 预售商品按预售时间发货
                    
                    2. 需鉴定商品：
                       - 卖家寄往鉴定中心：48小时内
                       - 鉴定时效：1-2个工作日
                       - 鉴定通过后24小时内发出
                    
                    3. 延迟发货处罚：
                       - 超时未发货：赔付订单金额5%
                       - 虚假发货：赔付订单金额30%
                    """;
            case "AUTHENTICATION" -> """
                    【鉴定规则】
                    
                    1. 鉴定流程：
                       - 卖家发货至鉴定中心
                       - 专业鉴定师多维度检验
                       - 鉴定通过后加贴防伪扣
                       - 发往买家
                    
                    2. 鉴定标准：
                       - 外观检查：无明显瑕疵
                       - 材质检验：符合品牌标准
                       - 工艺检查：走线、胶水等
                       - 配件完整性检查
                    
                    3. 鉴定不通过处理：
                       - 退回卖家并扣除保证金
                       - 记录卖家信用
                       - 全额退款买家
                    """;
            case "DELIVERY" -> """
                    【配送规则】
                    
                    1. 配送时效：
                       - 江浙沪：1-2天
                       - 华东华南：2-3天
                       - 其他地区：3-5天
                       - 偏远地区：5-7天
                    
                    2. 签收规则：
                       - 本人签收或代收
                       - 代收需授权
                       - 快递柜/驿站视为已签收
                    
                    3. 配送异常处理：
                       - 地址错误：联系修改后重新派送
                       - 无人签收：保留3天后退回
                       - 拒收：退回并扣除运费
                    """;
            default -> "未找到对应规则类型，请确认查询类型参数";
        };
    }

    /**
     * 查询赔偿标准
     */
    @Tool(name = "queryCompensationStandard", value = "查询各类问题的赔偿标准和计算方式")
    public String queryCompensationStandard(@P("问题类型：DELAY-延迟发货, FAKE-假货, DAMAGE-破损, LOST-丢失") String issueType) {
        log.info("查询赔偿标准, issueType: {}", issueType);

        return switch (issueType) {
            case "DELAY" -> """
                    【延迟发货赔偿标准】
                    - 超时1-3天：订单金额3%，最低5元，最高50元
                    - 超时3-7天：订单金额5%，最低10元，最高100元
                    - 超时7天以上：订单金额10%，最低20元，最高200元
                    - 赔偿形式：平台优惠券
                    """;
            case "FAKE" -> """
                    【假货赔偿标准】
                    - 确认假货：全额退款 + 订单金额3倍赔偿
                    - 最低赔偿：500元
                    - 赔偿形式：现金退还
                    - 卖家处罚：扣除全部保证金 + 永久封禁
                    """;
            case "DAMAGE" -> """
                    【商品破损赔偿标准】
                    - 轻微破损（不影响使用）：订单金额10%-20%
                    - 严重破损（影响使用）：全额退款
                    - 包装破损商品完好：补偿运费
                    - 赔偿形式：现金或优惠券
                    """;
            case "LOST" -> """
                    【包裹丢失赔偿标准】
                    - 快递公司责任：全额退款
                    - 赔偿时效：确认丢失后3个工作日
                    - 保价包裹：按保价金额赔偿
                    - 未保价包裹：按运费3倍赔偿（不超过商品价值）
                    """;
            default -> "未找到对应赔偿标准，请确认问题类型参数";
        };
    }
}
