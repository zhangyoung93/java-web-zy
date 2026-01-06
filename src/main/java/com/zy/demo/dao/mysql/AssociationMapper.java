package com.zy.demo.dao.mysql;

import com.zy.demo.model.ZyUser;
import org.apache.ibatis.annotations.Param;

/**
 * 关联查询
 *
 * @author zy
 */
public interface AssociationMapper {

    /**
     * 一对一联合查询
     *
     * @param userId userId
     * @return ZyUser
     */
    ZyUser selectJointByOneOnOne(@Param("userId") Long userId);

    /**
     * 一对一嵌套查询
     *
     * @param userId userId
     * @return ZyUser
     */
    ZyUser selectNestByOneOnOne(@Param("userId") Long userId);
}
