package com.zy.demo.service;

import com.zy.demo.annotation.JdkPointcut;
import com.zy.demo.pojo.LoginDto;

/**
 * 登录Service
 *
 * @author zy
 */
public interface LoginService {

    /**
     * doLogin
     * 注解此方法被动态代理增强
     *
     * @param loginDto loginDto
     * @return boolean
     * @throws Exception Exception
     */
    @JdkPointcut
    boolean doLogin(LoginDto loginDto) throws Exception;
}
