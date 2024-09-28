package org.shiloh.single;

import org.apache.commons.lang3.time.DateUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.shiloh.constant.DatePatternConstant;
import org.shiloh.entity.Order;
import org.shiloh.entity.User;
import org.shiloh.mapper.OrderMapper;
import org.shiloh.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 单表分片测试
 *
 * @author shiloh
 * @date 2024/9/20 16:01
 */
@SpringBootTest
class SingleTableShardingTests {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private OrderMapper orderMapper;

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

    // region by date

    /**
     * 单表按月分片新增数据测试
     *
     * @author shiloh
     * @date 2024/9/28 9:33
     */
    @Test
    void testInsertShardingByMonth() {
        final Order order = new Order();
        order.setOrderNo(UUID.randomUUID().toString());
        final Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, Calendar.SEPTEMBER);
        order.setOrderTime(calendar.getTime());
        order.setRemark("remark...");
        this.orderMapper.insert(order);
        Assertions.assertThat(order.getId()).isNotNull();
    }

    /**
     * 单表按月分片删除数据测试
     *
     * @author shiloh
     * @date 2024/9/28 9:45
     */
    @Test
    void testDeleteShardingByMonth() throws ParseException {
        final long orderId = 595359823179845L;
        final Date orderTime = DateUtils.parseDate("2024-09-28 09:45:21", DatePatternConstant.NORM_DATETIME_PATTERN);
        this.orderMapper.delete(orderId, orderTime);
        final Order order = this.orderMapper.selectByIdAndOrderTime(orderId, orderTime);
        Assertions.assertThat(order).isNull();
    }

    /**
     * 单表按月分片更新数据测试
     *
     * @author shiloh
     * @date 2024/9/28 9:50
     */
    @Test
    void testUpdateShardingByMonth() throws ParseException {
        Order order = new Order();
        order.setId(595361183445061L);
        order.setOrderTime(DateUtils.parseDate("2024-09-28 09:50:53", DatePatternConstant.NORM_DATETIME_PATTERN));
        final String remark = "updated remark...";
        order.setRemark(remark);
        this.orderMapper.update(order);
        order = this.orderMapper.selectByIdAndOrderTime(order.getId(), order.getOrderTime());
        Assertions.assertThat(order).isNotNull();
        Assertions.assertThat(remark).isEqualTo(order.getRemark());
    }

    /**
     * 单表按月分片查询测试
     * <p>
     * 如果查询范围跨月了，则会使用 union all 联合多张表的查询结果
     *
     * @author shiloh
     * @date 2024/9/20 16:32
     */
    @Test
    void testSelectShardingByMonth() {
        final Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, Calendar.AUGUST);
        calendar.set(Calendar.DATE, 1);
        final Date beginOrderTime = calendar.getTime();
        calendar.set(Calendar.MONTH, Calendar.SEPTEMBER);
        calendar.set(Calendar.DATE, 30);
        final Date endOrderTime = calendar.getTime();
        final List<Order> orders = this.orderMapper.selectAllByOrderTime(beginOrderTime, endOrderTime);
        Assertions.assertThat(orders).isNotEmpty();
    }

    // endregion
}
