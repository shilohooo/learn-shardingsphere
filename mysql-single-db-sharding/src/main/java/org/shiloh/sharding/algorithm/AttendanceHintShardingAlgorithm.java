package org.shiloh.sharding.algorithm;

import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.sharding.api.sharding.hint.HintShardingAlgorithm;
import org.apache.shardingsphere.sharding.api.sharding.hint.HintShardingValue;
import org.shiloh.sharding.AttendanceShardingModel;
import org.shiloh.sharding.config.ShardingTableName;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 自定义打卡记录表分片算法
 *
 * @author shiloh
 * @date 2024/9/29 14:16
 */
@Slf4j
@SuppressWarnings("unused")
public class AttendanceHintShardingAlgorithm implements HintShardingAlgorithm<AttendanceShardingModel> {
    /**
     * 通过 Hint 设置分片表名称
     * <p>
     * 根据部门 ID 加打卡日期设置分片表名称：[t_attendance_record_deptId_yyyyMM]
     *
     * @param availableTargetNames 有效的分片表名称集合
     * @param hintShardingValue    分片值
     * @return 逻辑表名称对应的分片表名称列表
     * @author shiloh
     * @date 2024/9/29 14:19
     */
    @Override
    public Collection<String> doSharding(
            Collection<String> availableTargetNames,
            HintShardingValue<AttendanceShardingModel> hintShardingValue
    ) {
        // 创建实际要查询的分片表
        this.createShardingTables(hintShardingValue.getLogicTableName(), availableTargetNames);
        log.info(">=============== AttendanceHintShardingAlgorithm.doSharding ===============<");
        try {
            log.info("availableTargetNames: {}", availableTargetNames);
            log.info("hintShardingValue: {}", hintShardingValue);
            final Set<String> actualDataNodes = hintShardingValue.getValues()
                    .stream()
                    .map(shardingModel -> String.format(
                            "%s_%s", hintShardingValue.getLogicTableName(), shardingModel.getShardingValue()
                    ))
                    .collect(Collectors.toSet());
            log.info("actualDataNodes: {}", actualDataNodes);
            final Set<String> actualDataNodeCacheNames = ShardingTableName.ACTUAL_DATA_NODES_CACHE.getOrDefault(
                    hintShardingValue.getLogicTableName(),
                    Collections.emptySet()
            );
            // 返回之前判断是否需要创建实际的分片表
            createShardingTables(hintShardingValue.getLogicTableName(), actualDataNodeCacheNames);
            return actualDataNodes;
        } finally {
            log.info(">=============== AttendanceHintShardingAlgorithm.doSharding ===============<");
        }
    }

    /**
     * 创建分片表，已存在的则跳过
     *
     * @param logicTableName       逻辑表名称
     * @param availableTargetNames 有效的分片表名称集合
     * @author shiloh
     * @date 2024/10/12 17:15
     */
    private void createShardingTables(String logicTableName, Collection<String> availableTargetNames) {
        final Set<String> actualDataNodeCacheNames = ShardingTableName.ACTUAL_DATA_NODES_CACHE.get(logicTableName);
        for (final String availableTargetName : availableTargetNames) {
            // 缓存中包含分片表名称，表示表已存在
            if (actualDataNodeCacheNames.contains(availableTargetName)) {
                continue;
            }

            this.createShardingTables(logicTableName, availableTargetName);
            this.updateActualDataNodesCache(logicTableName);
        }
    }

    /**
     * 创建分片表
     * <p>
     * TODO 通过 MYSQL 的 create table new_table like old_table，创建一个与 old_table 具有相同列定义、约束和索引的新表 new_table，
     * 但是不会复制任何数据。
     *
     * @param logicTableName      逻辑表名称
     * @param availableTargetName 有效分片表名称
     * @author shiloh
     * @date 2024/10/12 17:15
     */
    private void createShardingTables(String logicTableName, String availableTargetName) {
    }

    /**
     * TODO 更新分片表名称缓存
     *
     * @param logicTableName 逻辑表名称
     * @author shiloh
     * @date 2024/10/12 17:23
     */
    private void updateActualDataNodesCache(String logicTableName) {
        // 重新在 information_schema 查一次相关的表名称
    }
}
