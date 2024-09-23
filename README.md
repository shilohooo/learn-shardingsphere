# Apache ShardingSphere 分库分表学习记录

> 参考资料：[Apache ShardingSphere官网](https://shardingsphere.apache.org/document/current/en/overview/)

## 开发环境

|      名称       |    版本     |    备注     |
|:-------------:|:---------:|:---------:|
|    OpenJDK    | 1.8.0_402 | Azul Zulu |
| Apache Maven  |   3.6.3   |  项目构建工具   |
| Intellij IDEA | 2024.2.2  |    IDE    |

## 技术栈

|             名称             |   版本    |           备注           |
|:--------------------------:|:-------:|:----------------------:|
|         SpringBoot         | 2.6.13  |          基础框架          |
|          MyBatis           |  2.2.2  |         DAO 框架         |
| Apache ShardingSphere JDBC |  5.5.0  |          分库分表          |
|           Lombok           | 1.18.24 |                        |
|         Snakeyaml          |  1.33   |   覆盖默认版本，避免缺少指定方法报错    |
|        IdGenerator         |  1.0.6  |       雪花 ID 生成库        |
|           MySQL            |  8.3.0  |                        |
|        MSSQL Server        |  2022   | 开发版（Developer Edition） |

## 示例导航

### MySQL

- [x] [单个数据库表分表](./mysql-single-db-sharding)
- [ ] 多个数据库分库分表

### MSSQL Server

- [ ] 单个数据库分表
- [ ] 多个数据库分库分表

## License

[MIT](./LICENSE) Copyright (c) 2024 shiloh
