package com.zy.demo.dao.mysql;

import com.zy.demo.model.IdUserBase;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface IdUserBaseMapper {

    int deleteByPrimaryKey(Long userId);

    int insert(IdUserBase record);

    int insertSelective(IdUserBase record);

    IdUserBase selectByPrimaryKey(Long userId);

    int updateByPrimaryKeySelective(IdUserBase record);

    int updateByPrimaryKey(IdUserBase record);

    /**
     * 一对一联合查询
     *
     * @param userId userId
     * @return IdUserBase
     */
    IdUserBase selectJointByOneToOne(@Param("userId") Long userId);

    /**
     * 一对一嵌套查询
     *
     * @param userId userId
     * @return IdUserBase
     */
    IdUserBase selectNestByOneToOne(@Param("userId") Long userId);

    /**
     * 一对多联合查询
     *
     * @param userId userId
     * @return IdUserBase
     */
    IdUserBase selectJointByOneToMany(@Param("userId") Long userId);

    /**
     * 一对多嵌套查询
     *
     * @param userId userId
     * @return IdUserBase
     */
    IdUserBase selectNestByOneToMany(@Param("userId") Long userId);

    List<IdUserBase> selectByPaging(@Param("userName") String userName);

    int insertOne(IdUserBase idUserBase);

    int insertBatch(List<IdUserBase> idUserBaseList);

    List<IdUserBase> selectByIndex(String userFullName, String userName);

    List<IdUserBase> selectByMap(Map<String, Object> map);

    @Select("select user_id,user_full_name from id_user_base where user_id = #{userId}")
    IdUserBase selectByAnnotation(Long userId);
}