package org.shiloh.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 分片表名称枚举
 *
 * @author shiloh
 * @date 2024/9/29 15:37
 */
@Getter
@RequiredArgsConstructor
public enum ShardingTableName {
    ATTENDANCE_RECORD("t_attendance_record");

    /**
     * 逻辑表名称
     */
    private final String logicTableName;
}
