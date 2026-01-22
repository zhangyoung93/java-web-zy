package com.zy.demo.dao.mysql;

import com.zy.demo.model.IdUserJob;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IdUserJobMapper {

    int deleteByPrimaryKey(Long userJobId);

    int insert(IdUserJob record);

    int insertSelective(IdUserJob record);

    IdUserJob selectByPrimaryKey(Long userJobId);

    int updateByPrimaryKeySelective(IdUserJob record);

    int updateByPrimaryKey(IdUserJob record);

    List<IdUserJob> selectByUserId(Long userId);
}