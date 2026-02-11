package com.zy.demo.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import com.zy.demo.dao.mysql.IdUserBaseMapper;
import com.zy.demo.feign.AuthServer;
import com.zy.demo.model.IdUserBase;
import com.zy.demo.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;

/**
 * UserServiceImpl
 *
 * @author zy
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final IdUserBaseMapper idUserBaseMapper;

    private final SqlSessionFactory sqlSessionFactory;

    private final AuthServer authServer;

    public UserServiceImpl(IdUserBaseMapper idUserBaseMapper, SqlSessionFactory sqlSessionFactory, AuthServer authServer) {
        this.idUserBaseMapper = idUserBaseMapper;
        this.sqlSessionFactory = sqlSessionFactory;
        this.authServer = authServer;
    }

    @Override
    public Page<IdUserBase> selectUserByPaging(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<IdUserBase> idUserBaseList = this.idUserBaseMapper.selectByPaging(null);
        Page<IdUserBase> page = (Page<IdUserBase>) idUserBaseList;
        log.info("selectUserByPaging,page={}", page.toString());
        return page;
    }

    /**
     * Transactional注解将sql执行事务交给spring管理，无需手动提交
     *
     * @return int
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchInsertUser(List<IdUserBase> idUserBaseList) {
        Assert.notEmpty(idUserBaseList, "idUserBaseList must not be null");
        //sqlSession批量插入。每1000条数据作为一个批次，分批次插入
        List<List<IdUserBase>> lists = Lists.partition(idUserBaseList, 1000);
        //创建数据库会话，执行模式为批量，关闭自动提交
        SqlSession sqlSession = null;
        int insertRows = 0;
        try {
            sqlSession = this.sqlSessionFactory.openSession(ExecutorType.BATCH, false);
            IdUserBaseMapper idUserBaseMapper = sqlSession.getMapper(IdUserBaseMapper.class);
            for (List<IdUserBase> list : lists) {
                for (IdUserBase user : list) {
                    //单条插入
                    idUserBaseMapper.insertOne(user);
                    insertRows++;
                }
                //刷新缓存给数据库批量执行
                sqlSession.flushStatements();
            }
        } catch (Exception e) {
            if (sqlSession != null) {
                sqlSession.rollback();
            }
            throw new RuntimeException("batch insert fail", e);
        } finally {
            if (sqlSession != null) {
                sqlSession.close();
            }
        }

        //2、<foreach标签>批量插入
        insertRows = this.idUserBaseMapper.insertBatch(idUserBaseList);
        log.info("insertRows={}", insertRows);
        return insertRows;
    }

    @Override
    public boolean checkUserAuth(Long userId, Integer authType) {
        return this.authServer.checkUserAuth(userId, authType);
    }
}
