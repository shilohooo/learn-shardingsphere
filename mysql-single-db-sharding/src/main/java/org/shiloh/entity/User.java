package org.shiloh.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 用户实体
 *
 * @author shiloh
 * @date 2024/9/18 16:52
 */
@Setter
@Getter
@ToString(callSuper = true)
public class User extends BaseEntity {
    private static final long serialVersionUID = -4449425796893700697L;

    /**
     * 用户姓名
     */
    private String name;

    /**
     * 年龄
     */
    private Integer age;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 部门id
     */
    private Long deptId;
}
