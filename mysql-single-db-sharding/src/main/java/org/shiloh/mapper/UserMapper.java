package org.shiloh.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.shiloh.entity.User;

import java.util.List;

/**
 * 用户信息 DAO
 *
 * @author shiloh
 * @date 2024/9/18 16:54
 */
@Mapper
public interface UserMapper {
    // region CUD

    /**
     * 新增用户信息
     *
     * @param user 用户信息
     * @author shiloh
     * @date 2024/9/21 8:51
     */
    void insert(@Param("user") User user);

    /**
     * 更新用户信息
     *
     * @param user 用户信息
     * @author shiloh
     * @date 2024/9/21 8:51
     */
    void update(@Param("user") User user);

    /**
     * 删除用户信息
     *
     * @param id     用户 ID
     * @param deptId 部门 ID
     * @author shiloh
     * @date 2024/9/21 8:51
     */
    void delete(@Param("id") Long id, @Param("deptId") Long deptId);

    // endregion

    // region R

    /**
     * 根据部门 ID 查询用户列表
     *
     * @param deptId 部门 ID
     * @return 用户列表
     * @author shiloh
     * @date 2024/9/18 16:56
     */
    List<User> selectAllByDeptId(@Param("deptId") Long deptId);

    /**
     * 根据 ID 和部门 ID 查询用户信息
     *
     * @param id     用户 ID
     * @param deptId 部门 ID
     * @author shiloh
     * @date 2024/9/23 15:41
     */
    User selectByIdAndDeptId(@Param("id") Long id, @Param("deptId") Long deptId);

    // endregion
}
