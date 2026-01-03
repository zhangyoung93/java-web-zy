package com.zy.demo.dao.mysql;

import com.zy.demo.model.ZyUser;

public interface ZyUserMapper {

    int deleteByPrimaryKey(Long userId);

    int insert(ZyUser record);

    int insertSelective(ZyUser record);

    ZyUser selectByPrimaryKey(Long userId);

    int updateByPrimaryKeySelective(ZyUser record);

    int updateByPrimaryKey(ZyUser record);
}