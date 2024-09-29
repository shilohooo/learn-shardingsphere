package org.shiloh.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * 打卡记录实体
 *
 * @author shiloh
 * @date 2024/9/29 14:32
 */
@Setter
@Getter
@ToString(callSuper = true)
public class AttendanceRecord extends BaseEntity {
    private static final long serialVersionUID = 5065902954331650117L;

    /**
     * 用户 ID
     */
    private Long userId;

    /**
     * 用户姓名
     */
    private String username;

    /**
     * 部门 ID
     */
    private Long deptId;

    /**
     * 打卡时间
     */
    private Date clockInTime;

    /**
     * 备注
     */
    private String remark;
}
