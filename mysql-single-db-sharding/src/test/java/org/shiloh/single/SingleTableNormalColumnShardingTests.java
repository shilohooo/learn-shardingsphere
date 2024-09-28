package org.shiloh.single;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.shiloh.entity.User;
import org.shiloh.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * 单表分片测试
 *
 * @author shiloh
 * @date 2024/9/20 16:01
 */
@SpringBootTest
class SingleTableNormalColumnShardingTests {
    @Autowired
    private UserMapper userMapper;

    // region by normal column

    /**
     * 单表普通字段分片新增数据测试
     *
     * @author shiloh
     * @date 2024/9/21 9:34
     */
    @Test
    void testInsertShardingByNormalCol() {
        final User user = new User();
        user.setName("test5");
        user.setDeptId(5L);
        user.setAge(26);
        user.setEmail("test5@gmail.com");
        this.userMapper.insert(user);
        Assertions.assertThat(user.getId()).isNotNull();
    }

    /**
     * 单表普通字段分片删除数据测试
     *
     * @author shiloh
     * @date 2024/9/23 15:21
     */
    @Test
    void testDeleteShardingByNormalCol() {
        final long userId = 592913331392581L;
        final long deptId = 5L;
        this.userMapper.delete(userId, deptId);
        final User user = this.userMapper.selectByIdAndDeptId(userId, deptId);
        Assertions.assertThat(user).isNull();
    }

    /**
     * 单表普通字段分片修改数据测试
     *
     * @author shiloh
     * @date 2024/9/23 15:47
     */
    @Test
    void testUpdateShardingByNormalCol() {
        User user = new User();
        user.setId(592912837038149L);
        user.setDeptId(4L);
        final String newName = "update-test";
        user.setName(newName);
        this.userMapper.update(user);
        user = this.userMapper.selectByIdAndDeptId(user.getId(), user.getDeptId());
        Assertions.assertThat(user).isNotNull();
        Assertions.assertThat(user.getName()).isEqualTo(newName);
    }

    /**
     * 单表普通字段分片查询测试
     *
     * @author shiloh
     * @date 2024/9/20 16:05
     */
    @Test
    void testSelectShardingByNormalCol() {
        final List<User> users = this.userMapper.selectAllByDeptId(2L);
        Assertions.assertThat(users).isNotEmpty();
    }

    // endregion
}
