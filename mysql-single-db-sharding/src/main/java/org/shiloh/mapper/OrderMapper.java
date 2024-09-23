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
}
