package com.zy.demo.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import com.zy.demo.dao.mysql.ZyUserMapper;
import com.zy.demo.model.ZyUser;
import com.zy.demo.service.ZyUserService;
import com.zy.demo.util.SnowflakeIdGenerator;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * ZyUserServiceImpl
 *
 * @author zy
 */
@Slf4j
@Service
public class ZyUserServiceImpl implements ZyUserService {

    private final ZyUserMapper zyUserMapper;

    public ZyUserServiceImpl(ZyUserMapper zyUserMapper) {
        this.zyUserMapper = zyUserMapper;
    }

    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    @Override
    public Page<ZyUser> selectUserByPaging(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<ZyUser> zyUserList = this.zyUserMapper.selectByPaging(null,null);
        Page<ZyUser> page = (Page<ZyUser>) zyUserList;
        log.info("selectUserByPaging,page={}", page.toString());
        return page;
    }

    @Override
    public int insertUser() {
        List<ZyUser> zyUserList = new ArrayList<>();
        ZyUser zyUser = new ZyUser();
        zyUser.setUserId(SnowflakeIdGenerator.getInstance().nextId());
        zyUser.setUserFullName("user1");
        zyUser.setLoginName("name1");
        zyUser.setLoginPwd("pwd1");
        zyUserList.add(zyUser);
        zyUser = new ZyUser();
        zyUser.setUserId(SnowflakeIdGenerator.getInstance().nextId());
        zyUser.setUserFullName("user2");
        zyUser.setLoginName("name2");
        zyUser.setLoginPwd("pwd2");
        zyUserList.add(zyUser);
        //1、sqlSession批量执行
        SqlSession sqlSession = null;
        try {
            //每1000条数据作为一个批次，分批次插入
            List<List<ZyUser>> lists = Lists.partition(zyUserList, 1);
            //创建数据库会话，执行模式为批量，关闭自动提交
            sqlSession = this.sqlSessionFactory.openSession(ExecutorType.BATCH, false);
            ZyUserMapper zyUserMapper = sqlSession.getMapper(ZyUserMapper.class);
            for (List<ZyUser> list : lists) {
                for (ZyUser user : list) {
                    zyUserMapper.insertOne(user);
                }
            }
            sqlSession.commit();
        } catch (Exception e) {
            if (sqlSession != null) {
                sqlSession.rollback();
            }
            throw e;
        } finally {
            if (sqlSession != null) {
                sqlSession.close();
            }
        }
        //2、<foreach标签>批量插入
        int insertRows = this.zyUserMapper.insertBatch(zyUserList);
        log.info("insertRows={}", insertRows);
        return 0;
    }
}
