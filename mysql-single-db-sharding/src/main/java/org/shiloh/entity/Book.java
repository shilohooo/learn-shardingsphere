package org.shiloh.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * 图书实体
 *
 * @author shiloh
 * @date 2024/9/20 17:02
 */
@Setter
@Getter
@ToString(callSuper = true)
public class Book extends BaseEntity {
    private static final long serialVersionUID = 8982224682225846908L;

    /**
     * 图书名称
     */
    private String bookName;

    /**
     * 出版日期
     */
    private Date publishDate;
}
