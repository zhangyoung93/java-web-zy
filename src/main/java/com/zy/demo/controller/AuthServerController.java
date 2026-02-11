package com.zy.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 模拟Auth服务接口
 *
 * @author zy
 */
@RestController
@RequestMapping("/auth")
public class AuthServerController {

    @GetMapping("/check")
    public boolean checkUserAuth(@RequestParam("userId") Long userId, @RequestParam("authType") Integer authType) {
        return true;
    }
}
