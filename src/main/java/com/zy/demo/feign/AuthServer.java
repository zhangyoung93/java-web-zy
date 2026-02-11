package com.zy.demo.feign;

import com.zy.demo.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * openFeign接口
 *
 * @author zy
 * name：注册中心的服务名
 * url：非注册中心场景，直接指定目标URL
 * configuration：Feign配置
 */
@FeignClient(name = "auth-web-zy", url = "${feign.url.auth:}", configuration = FeignConfig.class)
public interface AuthServer {

    /**
     * 查询用户权限接口
     *
     * @param authType 权限类型
     * @return boolean
     */
    @GetMapping("/zy/auth/check")
    boolean checkUserAuth(@RequestParam("userId") Long userId, @RequestParam("authType") Integer authType);
}
