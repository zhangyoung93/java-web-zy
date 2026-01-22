package com.zy.demo.dao.mysql;

import com.zy.demo.model.IdUserPwd;
import org.springframework.stereotype.Repository;

@Repository
public interface IdUserPwdMapper {

    int deleteByPrimaryKey(Long userPwdId);

    int insert(IdUserPwd record);

    int insertSelective(IdUserPwd record);

    IdUserPwd selectByPrimaryKey(Long userPwdId);

    int updateByPrimaryKeySelective(IdUserPwd record);

    int updateByPrimaryKey(IdUserPwd record);

    IdUserPwd selectByUserId(Long userId);
}