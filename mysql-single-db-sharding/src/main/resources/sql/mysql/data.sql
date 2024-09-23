# 按指定字段分片
INSERT INTO sharding_jdbc_single_db.t_user_1 (id, name, age, email, dept_id)
VALUES (1, 'shiloh', 26, 'shiloh595@gmail.com', 1);
INSERT INTO sharding_jdbc_single_db.t_user_0 (id, name, age, email, dept_id)
VALUES (2, 'bruce', 30, 'bruce@qq.com', 2);

# 按指定时间字段分片
# 按月分片
insert into t_order_202408(id, order_no, order_time) value (1, '123', '2024-08-05 15:30:00');
insert into t_order_202409(id, order_no, order_time) value (1, '123', '2024-09-05 15:30:00');

# 不参与分片的表
insert into t_book(id, book_name, publish_date) VALUE (1, 'book1', '2024-08-05');
insert into t_book(id, book_name, publish_date) VALUE (2, 'book2', '2024-09-05');