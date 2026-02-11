package com.zy.demo.service;

import com.github.pagehelper.Page;
import com.zy.demo.model.IdUserBase;

import java.util.List;

/**
 * UserService
 *
 * @author zy
 */
public interface UserService {

    /**
     * 分页插件查询
     *
     * @param pageNum  页数
     * @param pageSize 每页数据量
     * @return page
     */
    Page<IdUserBase> selectUserByPaging(int pageNum, int pageSize);

    /**
     * 批量新增用户
     *
     * @param idUserBaseList idUserBaseList
     * @return int
     */
    int batchInsertUser(List<IdUserBase> idUserBaseList);

    /**
     * 查询用户是否有指定权限
     *
     * @param userId   用户ID
     * @param authType 指定权限
     * @return boolean
     */
    boolean checkUserAuth(Long userId, Integer authType);
}
