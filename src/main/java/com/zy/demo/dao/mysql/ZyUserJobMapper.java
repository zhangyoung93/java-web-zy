package com.zy.demo.dao.mysql;

import com.zy.demo.model.ZyUserJob;

import java.util.List;

/**
 * ZyUserJobMapper
 * @author zy
 */
public interface ZyUserJobMapper {

    int deleteByPrimaryKey(Long jobId);

    int insert(ZyUserJob record);

    int insertSelective(ZyUserJob record);

    ZyUserJob selectByPrimaryKey(Long jobId);

    int updateByPrimaryKeySelective(ZyUserJob record);

    int updateByPrimaryKey(ZyUserJob record);

    List<ZyUserJob> selectByUserId(Long userId);
}