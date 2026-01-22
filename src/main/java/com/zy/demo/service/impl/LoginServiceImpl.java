package com.zy.demo.service.impl;

import com.zy.demo.pojo.LoginDto;
import com.zy.demo.service.LoginService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 登录ServiceImpl
 *
 * @author zy
 */
@Slf4j
@Service
public class LoginServiceImpl implements LoginService {

    @Override
    public boolean doLogin(LoginDto loginDto) throws Exception {
        //TODO
        return false;
    }
}
