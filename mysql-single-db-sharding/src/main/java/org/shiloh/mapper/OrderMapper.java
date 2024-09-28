package org.shiloh.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.shiloh.entity.Order;

import java.util.Date;
import java.util.List;

/**
 * 订单实体 DAO
 *
 * @author shiloh
 * @date 2024/9/20 16:23
 */
@Mapper
public interface OrderMapper {
    /**
     * 新增订单
     *
     * @param order 订单实体
     * @author shiloh
     * @date 2024/9/28 9:36
     */
    void insert(@Param("order") Order order);

    /**
     * 根据 ID、下单时间删除订单
     *
     * @param id        订单 ID
     * @param orderTime 下单时间
     * @author shiloh
     * @date 2024/9/28 9:37
     */
    void delete(@Param("id") Long id, @Param("orderTime") Date orderTime);

    /**
     * 修改订单（不允许修改下单时间）
     *
     * @param order 订单实体
     * @author shiloh
     * @date 2024/9/28 9:37
     */
    void update(@Param("order") Order order);

    /**
     * 根据下单时间范围查询订单信息
     *
     * @param beginOrderTime 下单开始时间
     * @param endOrderTime   下单结束时间
     * @return 订单列表
     * @author shiloh
     * @date 2024/9/20 16:24
     */
    List<Order> selectAllByOrderTime(
            @Param("beginOrderTime") Date beginOrderTime, @Param("endOrderTime") Date endOrderTime
    );

    /**
     * 根据 ID 和下单时间查询订单信息
     *
     * @param id        订单 ID
     * @param orderTime 下单时间
     * @return 订单实体
     * @author shiloh
     * @date 2024/9/28 9:48
     */
    Order selectByIdAndOrderTime(@Param("id") Long id, @Param("orderTime") Date orderTime);
}
