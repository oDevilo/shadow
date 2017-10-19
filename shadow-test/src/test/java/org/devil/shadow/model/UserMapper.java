package org.devil.shadow.model;

import org.apache.ibatis.annotations.Param;

/**
 * Created by devil on 2017/8/7.
 */
public interface UserMapper {
    UserModel selectByPrimaryKey(@Param("id") Long id);
}
