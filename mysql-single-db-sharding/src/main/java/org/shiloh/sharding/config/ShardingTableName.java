package org.shiloh.sharding.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.shardingsphere.sharding.api.config.rule.ShardingTableRuleConfiguration;
import org.apache.shardingsphere.sharding.api.config.strategy.keygen.KeyGenerateStrategyConfiguration;
import org.apache.shardingsphere.sharding.api.config.strategy.sharding.HintShardingStrategyConfiguration;
import org.apache.shardingsphere.sharding.api.config.strategy.sharding.StandardShardingStrategyConfiguration;
import org.shiloh.common.constant.SymbolConstant;
import org.shiloh.entity.User;
import org.shiloh.sharding.constant.ShardingSphereConstant;

import java.text.ParseException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 分片表枚举
 *
 * @author shiloh
 * @date 2024/9/29 15:37
 */
@Slf4j
@Getter
@RequiredArgsConstructor
public enum ShardingTableName implements BaseShardingTableName {
    // 用户信息表分片配置
    USER("t_user") {
        /**
         * 获取分片表配置
         * <p>
         * 用户表按部门 ID % 2 来分片，实际数据节点为：逻辑数据库名称.逻辑表名称_${分片起始值..分片结束值}
         *
         * @return 分片表配置
         * @author shiloh
         * @date 2024/9/30 11:24
         */
        @Override
        public ShardingTableRuleConfiguration getShardingTableRuleConfiguration() {
            final int[] mods = ACTUAL_DATA_NODES_CACHE.get(
                            this.getLogicTableName()
                    )
                    .stream()
                    .mapToInt(tableName -> {
                        final int idx = tableName.lastIndexOf(SymbolConstant.UNDERLINE);
                        return Integer.parseInt(tableName.substring(idx + 1));
                    })
                    .sorted()
                    .toArray();

            final String actualDataNodes = String.format(
                    "%s.%s_${%d..%d}",
                    ShardingSphereConstant.MASTER_DATA_SOURCE_NAME,
                    this.getLogicTableName(),
                    mods[0],
                    mods[mods.length - 1]
            );
            final ShardingTableRuleConfiguration configuration = new ShardingTableRuleConfiguration(
                    this.getLogicTableName(), actualDataNodes
            );
            // 设置表分片策略
            configuration.setTableShardingStrategy(new StandardShardingStrategyConfiguration(
                    User.SHARDING_COLUMN, ShardingAlgorithmName.USER_INLINE.getAlgorithmName()
            ));
            // 设置分布式序列生成策略
            configuration.setKeyGenerateStrategy(new KeyGenerateStrategyConfiguration(
                    ShardingSphereConstant.KEY_COLUMN, ShardingSphereConstant.KEY_GENERATOR_NAME
            ));
            return configuration;
        }
    },

    // 订单表分片配置
    ORDER("t_order") {
        /**
         * 分片列名称
         */
        private static final String SHARDING_COLUMN = "order_time";

        /**
         * 获取分片表配置
         * <p>
         * 订单表根据下单时间按月分片，实际数据节点为：逻辑数据库名称.逻辑表名称_${起始下单时间yyyyMM..截止下单时间yyyyMM}
         *
         * @return 分片表配置
         * @author shiloh
         * @date 2024/9/30 11:24
         */
        @Override
        public ShardingTableRuleConfiguration getShardingTableRuleConfiguration() {
            final String[] dates = ACTUAL_DATA_NODES_CACHE.get(this.getLogicTableName())
                    .stream()
                    .map(tableName -> {
                        try {
                            final String orderMonth = tableName.substring(
                                    tableName.lastIndexOf(SymbolConstant.UNDERLINE) + 1
                            );
                            return DateUtils.parseDate(orderMonth, "yyyyMM");
                        } catch (ParseException e) {
                            return null;
                        }
                    })
                    .filter(Objects::nonNull)
                    .sorted(Date::compareTo)
                    .map(date -> DateFormatUtils.format(date, "yyyyMM"))
                    .toArray(String[]::new);
            final String actualDataNodes = String.format(
                    "%s.%s_${%s..%s}",
                    ShardingSphereConstant.MASTER_DATA_SOURCE_NAME,
                    this.getLogicTableName(),
                    dates[0],
                    dates[dates.length - 1]
            );
            final ShardingTableRuleConfiguration configuration = new ShardingTableRuleConfiguration(
                    this.getLogicTableName(), actualDataNodes
            );
            // 设置表分片策略
            configuration.setTableShardingStrategy(new StandardShardingStrategyConfiguration(
                    SHARDING_COLUMN, ShardingAlgorithmName.ORDER_INTERVAL.getAlgorithmName()
            ));
            // 设置分布式序列生成策略
            configuration.setKeyGenerateStrategy(new KeyGenerateStrategyConfiguration(
                    ShardingSphereConstant.KEY_COLUMN, ShardingSphereConstant.KEY_GENERATOR_NAME
            ));
            return configuration;
        }
    },

    // 打卡记录表分片配置
    ATTENDANCE_RECORD("t_attendance_record") {
        /**
         * 获取分片表配置
         * <p>
         * 打卡记录按部门 ID 和打卡时间按月进行分片，且使用的是自定义的 Hint 算法，实际的数据节点名称为：
         * 逻辑数据库名称.逻辑表名称_deptId_yyyyMM，多个数据节点按逗号分隔
         *
         * @return 分片表配置
         * @author shiloh
         * @date 2024/9/30 11:24
         */
        @Override
        public ShardingTableRuleConfiguration getShardingTableRuleConfiguration() {
            final List<String> actualDataNodes = ACTUAL_DATA_NODES_CACHE.get(
                            this.getLogicTableName())
                    .stream()
                    .map(tableName -> String.format("%s.%s", ShardingSphereConstant.MASTER_DATA_SOURCE_NAME, tableName))
                    .collect(Collectors.toList());
            final String actualDataNodesStr = String.join(SymbolConstant.COMMA, actualDataNodes
            );
            final ShardingTableRuleConfiguration configuration = new ShardingTableRuleConfiguration(
                    this.getLogicTableName(), actualDataNodesStr
            );
            // 设置表分片策略
            configuration.setTableShardingStrategy(new HintShardingStrategyConfiguration(
                    ShardingAlgorithmName.ATTENDANCE_RECORD_HINT.getAlgorithmName()
            ));
            // 设置分布式序列生成策略
            configuration.setKeyGenerateStrategy(new KeyGenerateStrategyConfiguration(
                    ShardingSphereConstant.KEY_COLUMN, ShardingSphereConstant.KEY_GENERATOR_NAME
            ));
            return configuration;
        }
    };

    // region actual data nodes cache

    /**
     * 分片表名称缓存
     */
    public static final Map<String, Set<String>> ACTUAL_DATA_NODES_CACHE = new ConcurrentHashMap<>(16);

    static {
        Arrays.stream(ShardingTableName.values()).forEach(shardingTableName -> ACTUAL_DATA_NODES_CACHE.put(
                shardingTableName.getLogicTableName(), Collections.emptySet())
        );
    }

    // endregion

    /**
     * 逻辑表名称
     */
    private final String logicTableName;
}
