package com.zy.demo.service.impl;

import com.zy.demo.config.BusinessException;
import com.zy.demo.dao.mysql.ZyUserMapper;
import com.zy.demo.pojo.LoginDto;
import com.zy.demo.service.LoginService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 登录ServiceImpl
 *
 * @author zy
 */
@Slf4j
@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private ZyUserMapper zyUserMapper;

    @Override
    public boolean doLogin(LoginDto loginDto) throws Exception {
        if (loginDto == null || loginDto.getUser() == null) {
            throw new BusinessException("登录信息为空，不处理！");
        }
        //TODO
        return false;
    }
}
