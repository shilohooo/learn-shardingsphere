package org.shiloh.sharding.manager;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.shiloh.sharding.config.ShardingTableName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 分片表管理组件
 *
 * @author shiloh
 * @date 2024/10/17 15:29
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor_ = {@Autowired, @Lazy})
public class ShardingTableManager {
    /**
     * 数据库主库
     */
    public static final String DB_MASTER = "sharding_jdbc_single_db";

    /**
     * information_schema
     */
    public static final String DB_INFORMATION_SCHEMA = "information_schema";

    /**
     * SQL - 根据 schema 查询表名称
     */
    private static final String SELECT_TABLE_NAMES_BY_SCHEMA = "select TABLE_NAME as tableName from TABLES where TABLE_SCHEMA = ?";

    /**
     * SQL - 根据 schema 和表名称模糊查询表名称
     */
    private static final String SELECT_TABLE_NAMES_BY_SCHEMA_AND_NAME = "select TABLE_NAME as tableName from information_schema.TABLES where TABLE_SCHEMA = ? and TABLE_NAME like ?";

    private final DataSourceProperties dataSourceProperties;

    /**
     * 刷新分片表数据节点缓存
     *
     * @author shiloh
     * @date 2024/10/17 15:35
     */
    public void refreshShardingTableCache() {
        try (
                final Connection connection = DriverManager.getConnection(
                        dataSourceProperties.getUrl()
                                .replace(DB_MASTER, DB_INFORMATION_SCHEMA),
                        dataSourceProperties.getUsername(),
                        dataSourceProperties.getPassword()
                );
                final PreparedStatement statement = connection.prepareStatement(SELECT_TABLE_NAMES_BY_SCHEMA)
        ) {
            statement.setString(1, DB_MASTER);
            final List<String> shardingTableNames = new ArrayList<>();
            final ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                shardingTableNames.add(resultSet.getString("tableName"));
            }
            for (final ShardingTableName shardingTableName : ShardingTableName.values()) {
                final Set<String> matchedTableNames = shardingTableNames.stream()
                        .filter(tableName -> tableName.startsWith(
                                String.format("%s_", shardingTableName.getLogicTableName())
                        ))
                        .collect(Collectors.toSet());
                log.info("loginTableName: {}, actualDataNodes: {}", shardingTableName.getLogicTableName(),
                        matchedTableNames);
                ShardingTableName.ACTUAL_DATA_NODES_CACHE.put(shardingTableName.getLogicTableName(), matchedTableNames);
            }
        } catch (SQLException e) {
            log.error("分片表数据节点缓存刷新失败", e);
        }
    }

    /**
     * 创建分片表，已存在的则跳过
     *
     * @param logicTableName  逻辑表名称
     * @param actualDataNodes 实际要查询的分片表名称集合
     * @author shiloh
     * @date 2024/10/17 15:35
     */
    public void createShardingTables(String logicTableName, Collection<String> actualDataNodes) {
        final Set<String> actualDataNodeCacheNames = ShardingTableName.ACTUAL_DATA_NODES_CACHE.get(logicTableName);
        final String existingShardingTableNames = actualDataNodeCacheNames.iterator().next();
        for (final String actualDataNode : actualDataNodes) {
            // 缓存中包含分片表名称，表示表已存在
            if (actualDataNodeCacheNames.contains(actualDataNode)) {
                continue;
            }

            this.createShardingTables(existingShardingTableNames, actualDataNode);
            this.updateActualDataNodesCache(logicTableName);
        }
    }

    /**
     * 创建分片表
     * <p>
     * 通过 MYSQL 的 create table new_table like old_table，创建一个与 old_table 具有相同列定义、约束和索引的新表 new_table，
     * 但是不会复制任何数据。
     *
     * @param existingTableName 已存在的分片表名称
     * @param newTableName      新的分片表名称
     * @author shiloh
     * @date 2024/10/17 15:35
     */
    private void createShardingTables(String existingTableName, String newTableName) {
        try (
                final Connection connection = DriverManager.getConnection(
                        this.dataSourceProperties.getUrl(),
                        this.dataSourceProperties.getUsername(),
                        this.dataSourceProperties.getPassword()
                );
                final PreparedStatement statement = connection.prepareStatement(
                        String.format("create table %s like %s", newTableName, existingTableName)
                )
        ) {
            statement.executeUpdate();
            log.info("根据已存在的分片表：{} 创建新的分片表：{}，执行成功", existingTableName, newTableName);
        } catch (SQLException e) {
            log.error("分片表：{} 创建失败", newTableName, e);
        }
    }

    /**
     * 更新分片表名称缓存
     *
     * @param logicTableName 逻辑表名称
     * @author shiloh
     * @date 2024/10/17 15:35
     */
    private void updateActualDataNodesCache(String logicTableName) {
        // 重新在 information_schema 查一次相关的表名称
        try (
                final Connection connection = DriverManager.getConnection(
                        this.dataSourceProperties.getUrl().replace(DB_MASTER, DB_INFORMATION_SCHEMA),
                        this.dataSourceProperties.getUsername(),
                        this.dataSourceProperties.getPassword()
                );
                final PreparedStatement statement = connection.prepareStatement(SELECT_TABLE_NAMES_BY_SCHEMA_AND_NAME)
        ) {
            statement.setString(1, DB_MASTER);
            statement.setString(2, String.format("%s_%%", logicTableName));
            final ResultSet resultSet = statement.executeQuery();
            final Set<String> actualDataNodes = new HashSet<>();
            while (resultSet.next()) {
                actualDataNodes.add(resultSet.getString("tableName"));
            }
            ShardingTableName.ACTUAL_DATA_NODES_CACHE.put(logicTableName, actualDataNodes);
        } catch (SQLException e) {
            log.error("逻辑表：{} 分片数据节点缓存更新失败", logicTableName, e);
        }
    }
}
