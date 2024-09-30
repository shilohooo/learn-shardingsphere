create database sharding_jdbc_single_db;

# 按指定字段分片
create table t_user_0
(
    id      bigint primary key comment '主键',
    name    varchar(50) not null comment '用户姓名',
    age     int comment '年龄',
    email   varchar(255) unique comment '邮箱',
    dept_id bigint comment '部门id'
) comment '用户信息表';

-- auto-generated definition
create table t_user_1
(
    id      bigint       not null comment '主键'
        primary key,
    name    varchar(50)  not null comment '用户姓名',
    age     int          null comment '年龄',
    email   varchar(255) null comment '邮箱',
    dept_id bigint       null comment '部门id',
    constraint email
        unique (email)
)
    comment '用户信息表';

# 按指定时间字段分片
create table t_order
(
    id         bigint primary key comment '主键',
    order_no   varchar(50)  not null comment '订单号',
    order_time datetime     not null comment '下单时间',
    remark     varchar(255) null comment '备注'
) comment '订单信息表';

# 按月分片
create table t_order_202408
(
    id         bigint primary key comment '主键',
    order_no   varchar(50)  not null comment '订单号',
    order_time datetime     not null comment '下单时间',
    remark     varchar(255) null comment '备注'
) comment '订单信息表20240801';

create table t_order_202409
(
    id         bigint primary key comment '主键',
    order_no   varchar(50)  not null comment '订单号',
    order_time datetime     not null comment '下单时间',
    remark     varchar(255) null comment '备注'
) comment '订单信息表20240901';


# 按多个字段分片（部门 ID + 打卡时间（按月））
create table t_attendance_record
(
    id            bigint primary key comment '主键',
    user_id       bigint       not null comment '用户id',
    username      varchar(50)  not null comment '用户姓名',
    dept_id       bigint       not null comment '部门id',
    clock_in_time datetime     not null comment '打卡时间',
    remark        varchar(255) null comment '备注'
) comment '打卡记录表';

create table t_attendance_record_1_202408
(
    id            bigint primary key comment '主键',
    user_id       bigint       not null comment '用户id',
    username      varchar(50)  not null comment '用户姓名',
    dept_id       bigint       not null comment '部门id',
    clock_in_time datetime     not null comment '打卡时间',
    remark        varchar(255) null comment '备注'
) comment '打卡记录表';

create table t_attendance_record_1_202409
(
    id            bigint primary key comment '主键',
    user_id       bigint       not null comment '用户id',
    username      varchar(50)  not null comment '用户姓名',
    dept_id       bigint       not null comment '部门id',
    clock_in_time datetime     not null comment '打卡时间',
    remark        varchar(255) null comment '备注'
) comment '打卡记录表';

# 不参与分片的表
create table t_book
(
    id           bigint primary key comment '主键',
    book_name    varchar(50) not null comment '图书名称',
    publish_date date        not null comment '出版日期'
) comment '图书信息表';