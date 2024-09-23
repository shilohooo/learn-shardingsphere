package org.shiloh.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.shiloh.entity.Book;

import java.util.List;

/**
 * 图书实体 DAO
 *
 * @author shiloh
 * @date 2024/9/20 17:03
 */
@Mapper
public interface BookMapper {
    /**
     * 根据图书名称查询图书信息
     *
     * @param bookName 图书名称
     * @return 图书列表
     * @author shiloh
     * @date 2024/9/20 17:04
     */
    List<Book> selectAllByBookName(@Param("bookName") String bookName);
}
