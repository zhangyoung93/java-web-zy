package com.zy.demo.handler;

import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeException;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRuleManager;
import com.alibaba.csp.sentinel.slots.block.flow.FlowException;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowException;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowRuleManager;
import com.alibaba.csp.sentinel.slots.system.SystemBlockException;
import com.alibaba.csp.sentinel.slots.system.SystemRuleManager;
import com.zy.demo.pojo.BaseResponse;
import com.zy.demo.pojo.UserDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

/**
 * sentinel异常处理
 *
 * @author zy
 */
public class SentinelBlockHandler {

    /**
     * 异常处理方法，必须是静态修饰，且参数列表与原方法一致，只是多一个BlockException参数
     *
     * @param userDtoList 原方法参数
     * @param e           规则异常
     * @return 原方法返回值
     */
    public static ResponseEntity<Object> blockHandler(List<UserDto> userDtoList, BlockException e) {
        StringBuilder msg = new StringBuilder("sentinel拦截。");
        //规则资源
        String resource = e.getRule().getResource();
        if (e instanceof FlowException) {
            //限流异常
            msg.append("请求流量过高，请稍后再试。阈值=");
            FlowRule flowRule = FlowRuleManager.getRules().stream().filter(rule -> resource.equals(rule.getResource())).findFirst().orElse(null);
            if (flowRule != null) {
                msg.append(flowRule.getCount());
            } else {
                msg.append("-1");
            }
        } else if (e instanceof DegradeException) {
            //熔断异常
            msg.append("服务熔断降级，请稍后再试。阈值=");
            DegradeRule degradeRule = DegradeRuleManager.getRules().stream().filter(rule -> resource.equals(rule.getResource())).findFirst().orElse(null);
            if (degradeRule != null) {
                msg.append(degradeRule.getCount());
            } else {
                msg.append("-1");
            }
        } else if (e instanceof SystemBlockException) {
            msg.append("系统负载过高，请稍后再试。阈值=");
            double cpuUsageThreshold = SystemRuleManager.getCpuUsageThreshold();
            msg.append(cpuUsageThreshold);
        } else if (e instanceof ParamFlowException) {
            msg.append("热点参数限流，请稍后再试。\"阈值=");
            ParamFlowRule paramFlowRule = ParamFlowRuleManager.getRules().stream().filter(rule -> resource.equals(rule.getResource())).findFirst().orElse(null);
            if (paramFlowRule != null) {
                msg.append(paramFlowRule.getCount());
            } else {
                msg.append("=1");
            }
        }
        return ResponseEntity.ok(BaseResponse.fail(msg.toString()));
    }
}
