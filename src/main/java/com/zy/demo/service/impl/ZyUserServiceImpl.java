package com.zy.demo.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zy.demo.dao.mysql.ZyUserMapper;
import com.zy.demo.model.ZyUser;
import com.zy.demo.service.ZyUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * ZyUserServiceImpl
 * @author zy
 */
@Slf4j
@Service
public class ZyUserServiceImpl implements ZyUserService {

    private final ZyUserMapper zyUserMapper;

    public ZyUserServiceImpl(ZyUserMapper zyUserMapper){
        this.zyUserMapper = zyUserMapper;
    }

    @Override
    public Page<ZyUser> selectUserByPaging(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<ZyUser> zyUserList = this.zyUserMapper.selectByPaging();
        Page<ZyUser> page = (Page<ZyUser>) zyUserList;
        log.info("selectUserByPaging,page={}", page.toString());
        return page;
    }
}
