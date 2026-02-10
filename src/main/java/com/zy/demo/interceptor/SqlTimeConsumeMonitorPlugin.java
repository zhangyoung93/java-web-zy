package com.zy.demo.interceptor;

import com.zy.demo.config.MybatisPluginProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.util.Properties;

/**
 * SQL耗时监控插件
 *
 * @author zy
 */
@Slf4j
@Intercepts({
        //标记所有的查询SQL
        @Signature(
                type = Executor.class,
                method = "query",
                //拦截的query方法入参类型必须与args一致。
                args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}
        ),
        @Signature(
                type = Executor.class,
                method = "query",
                args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class}
        ),
        //标记所有的增删改SQL
        @Signature(
                type = Executor.class,
                method = "update",
                args = {MappedStatement.class, Object.class}
        )
})
public class SqlTimeConsumeMonitorPlugin implements Interceptor {

    private MybatisPluginProperties mybatisPluginProperties;

    /**
     * set注入插件配置依赖
     *
     * @param mybatisPluginProperties mybatisPluginProperties
     */
    public void setMybatisPluginProperties(MybatisPluginProperties mybatisPluginProperties) {
        this.mybatisPluginProperties = mybatisPluginProperties;
    }

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object[] args = invocation.getArgs();
        MappedStatement ms = (MappedStatement) args[0];
        String sqlId = ms.getId();
        String sqlType = ms.getSqlCommandType().name();
        long startTime = System.currentTimeMillis();
        //慢执行阈值
        int threshold = this.mybatisPluginProperties.getSqlCostTimeThreshold();
        try {
            return invocation.proceed();
        } finally {
            long costTime = System.currentTimeMillis() - startTime;
//            log.info("sql monitor,sqlType={},sqlId={},costTime={}ms", sqlType, sqlId, costTime);
            //慢执行警告
            if (costTime > threshold) {
                log.warn("slow sql==========>sqlType={},sqlId={},costTime={}ms", sqlType, sqlId, costTime);
            }
        }
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {

    }
}
