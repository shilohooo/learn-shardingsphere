package org.shiloh.single;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.shiloh.entity.Book;
import org.shiloh.mapper.BookMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * 未分片的表测试
 *
 * @author shiloh
 * @date 2024/9/20 16:01
 */
@SpringBootTest
class NoShardingTests {
    @Autowired
    private BookMapper bookMapper;

    /**
     * 未分片的表查询测试
     *
     * @author shiloh
     * @date 2024/9/20 17:05
     */
    @Test
    void noShardingTableQueryTest() {
        final List<Book> books = this.bookMapper.selectAllByBookName("book1");
        Assertions.assertFalse(books.isEmpty());
    }
}
