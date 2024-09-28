package org.shiloh.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * 订单实体
 *
 * @author shiloh
 * @date 2024/9/20 16:18
 */
@Setter
@Getter
@ToString(callSuper = true)
public class Order extends BaseEntity {
    private static final long serialVersionUID = -7586436619040083900L;

    /**
     * 订单编号
     */
    private String orderNo;

    /**
     * 下单时间
     */
    private Date orderTime;

    /**
     * 备注
     */
    private String remark;
}
