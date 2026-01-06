package com.zy.demo.dao.mysql;

import com.zy.demo.model.ZyUserIdCard;

public interface ZyUserIdCardMapper {

    int deleteByPrimaryKey(Long cardId);

    int insert(ZyUserIdCard record);

    int insertSelective(ZyUserIdCard record);

    ZyUserIdCard selectByPrimaryKey(Long cardId);

    int updateByPrimaryKeySelective(ZyUserIdCard record);

    int updateByPrimaryKey(ZyUserIdCard record);

    ZyUserIdCard selectByUserId(Long userId);
}