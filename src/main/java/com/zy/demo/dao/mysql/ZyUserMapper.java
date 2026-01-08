package com.zy.demo.dao.mysql;

import com.zy.demo.model.ZyUser;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * ZyUserMapper
 *
 * @author zy
 */
@Repository
public interface ZyUserMapper {

    int deleteByPrimaryKey(Long userId);

    int insert(ZyUser record);

    int insertSelective(ZyUser record);

    ZyUser selectByPrimaryKey(Long userId);

    int updateByPrimaryKeySelective(ZyUser record);

    int updateByPrimaryKey(ZyUser record);

    List<ZyUser> selectByPaging(@Param("userFullName") String userFullName, @Param("loginName") String loginName);

    int insertOne(ZyUser zyUser);

    int insertBatch(List<ZyUser> zyUserList);

    List<ZyUser> selectByIndex(String userFullName, String loginName);

    List<ZyUser> selectByMap(Map<String, Object> map);

    @Select("select user_id,user_full_name from zy_user where user_id = #{userId}")
    ZyUser selectByAnnotation(Long userId);

}