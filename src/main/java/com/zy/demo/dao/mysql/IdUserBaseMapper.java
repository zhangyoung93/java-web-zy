package com.zy.demo.dao.mysql;

import com.zy.demo.model.IdUserBase;

public interface IdUserBaseMapper {
    int deleteByPrimaryKey(Long userId);

    int insert(IdUserBase record);

    int insertSelective(IdUserBase record);

    IdUserBase selectByPrimaryKey(Long userId);

    int updateByPrimaryKeySelective(IdUserBase record);

    int updateByPrimaryKey(IdUserBase record);
}