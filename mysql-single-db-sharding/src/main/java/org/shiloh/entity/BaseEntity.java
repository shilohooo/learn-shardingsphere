package org.shiloh.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * 实体基类
 *
 * @author shiloh
 * @date 2024/9/20 16:20
 */
@Getter
@Setter
@ToString
public abstract class BaseEntity implements Serializable {
    private static final long serialVersionUID = -1198414201142626279L;

    /**
     * 主键
     */
    private Long id;
}
