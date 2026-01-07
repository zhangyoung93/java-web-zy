package com.zy.demo.dao.mysql.impl;

import com.zy.demo.model.ZyUser;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

/**
 * 自定义Mapper接口(老项目用法，现在直接用@MapperScan注解就能实现)
 *
 * @author zy
 */
@Repository
public class CustomizeDaoImpl extends SqlSessionDaoSupport {

    private static final String MAPPER_STATEMENT = "com.zy.demo.dao.mysql.ZyUserMapper.selectByPrimaryKey";

    private final SqlSessionTemplate sqlSessionTemplate;

    public CustomizeDaoImpl(SqlSessionFactory sqlSessionFactory, SqlSessionTemplate sqlSessionTemplate) {
        //注入单例的SqlSessionFactory，创建SqlSessionTemplate对象
        setSqlSessionFactory(sqlSessionFactory);
        //或者直接从IOC注入SqlSessionTemplate
        this.sqlSessionTemplate = sqlSessionTemplate;
    }

    public ZyUser getZyUserById(Long userId) {
        ZyUser zyUser;
        //getSqlSession()获取到的是SqlSessionTemplate，线程安全。spring项目中可直接注入已经注册的SqlSessionTemplate使用。
//      zyUser = getSqlSession().selectOne(MAPPER_STATEMENT, userId);
        zyUser = this.sqlSessionTemplate.selectOne(MAPPER_STATEMENT, userId);
        return zyUser;
    }
}
