# ShardingSphere - MySQL 单库分片 DEMO

> 参考资料：
> - [ShardingSphere 标准分片算法 - 行表达式分片算法](https://shardingsphere.apache.org/document/5.5.0/cn/user-manual/common-config/builtin-algorithm/sharding/#行表达式分片算法)
> - [ShardingSphere 标准分片算法 - 时间范围分片算法](https://shardingsphere.apache.org/document/5.5.0/cn/user-manual/common-config/builtin-algorithm/sharding/#时间范围分片算法)
> - [ShardingSphere 自定义类分片算法](https://shardingsphere.apache.org/document/5.5.0/cn/user-manual/common-config/builtin-algorithm/sharding/#自定义类分片算法)

## 内容概览

| 示例名称        |        逻辑表名称        |                          配置                           |                                             单元测试                                             |           备注            |
|:------------|:-------------------:|:-----------------------------------------------------:|:--------------------------------------------------------------------------------------------:|:-----------------------:|
| 单表不分片       |       t_book        | [:white_check_mark:](./src/main/resources/config.yml) |         [:white_check_mark:](./src/test/java/org/shiloh/single/NoShardingTests.java)         |                         |
| 按单个普通字段分片   |       t_user        | [:white_check_mark:](./src/main/resources/config.yml) | [:white_check_mark:](./src/test/java/org/shiloh/single/SingleNormalColumnShardingTests.java) |       dept_id % 2       |
| 按单个日期字段按月分片 |       t_order       | [:white_check_mark:](./src/main/resources/config.yml) |  [:white_check_mark:](./src/test/java/org/shiloh/single/SingleDateColumnShardingTests.java)  |         yyyyMM          |
| 按多个字段分片     | t_attendance_record | [:white_check_mark:](./src/main/resources/config.yml) |    [:white_check_mark:](./src/test/java/org/shiloh/single/MultiColumnShardingTests.java)     | dept_id_yyyyMM（多租户按月分片） |

## 数据库脚本

- [scheme.sql](./src/main/resources/sql/mysql/scheme.sql)
- [data.sql](./src/main/resources/sql/mysql/data.sql)