package com.zy.demo.service;

import com.github.pagehelper.Page;
import com.zy.demo.model.ZyUser;

/**
 * ZyUserService
 *
 * @author zy
 */
public interface ZyUserService {

    /**
     * 分页插件查询
     *
     * @param pageNum  页数
     * @param pageSize 每页数据量
     * @return page
     */
    Page<ZyUser> selectUserByPaging(int pageNum, int pageSize);

    /**
     * 新增用户
     *
     * @return int
     */
    int insertUser();
}
