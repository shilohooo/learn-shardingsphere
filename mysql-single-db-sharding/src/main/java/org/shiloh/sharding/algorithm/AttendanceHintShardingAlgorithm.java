package org.shiloh.sharding.algorithm;

import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.sharding.api.sharding.hint.HintShardingAlgorithm;
import org.apache.shardingsphere.sharding.api.sharding.hint.HintShardingValue;
import org.shiloh.sharding.AttendanceShardingModel;

import java.util.Collection;
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
        log.info(">=============== AttendanceHintShardingAlgorithm.doSharding ===============<");
        try {
            log.info("availableTargetNames: {}", availableTargetNames);
            log.info("hintShardingValue.logicTableName: {}", hintShardingValue.getLogicTableName());
            log.info("hintShardingValue.columnName: {}", hintShardingValue.getColumnName());
            log.info("hintShardingValue.values: {}", hintShardingValue.getValues());
            final Set<String> actualDataNodes = hintShardingValue.getValues().stream()
                    .map(shardingModel -> String.format(
                            "%s_%s", hintShardingValue.getLogicTableName(), shardingModel.getShardingValue()
                    ))
                    .collect(Collectors.toSet());
            log.info("actualDataNodes: {}", actualDataNodes);
            return actualDataNodes;
        } finally {
            log.info(">=============== AttendanceHintShardingAlgorithm.doSharding ===============<");
        }
    }
}
