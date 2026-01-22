package com.zy.demo.dao.mysql;

import com.zy.demo.model.IdUserJob;

public interface IdUserJobMapper {
    int deleteByPrimaryKey(Long userJobId);

    int insert(IdUserJob record);

    int insertSelective(IdUserJob record);

    IdUserJob selectByPrimaryKey(Long userJobId);

    int updateByPrimaryKeySelective(IdUserJob record);

    int updateByPrimaryKey(IdUserJob record);
}