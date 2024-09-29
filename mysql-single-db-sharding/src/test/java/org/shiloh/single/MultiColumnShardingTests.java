package org.shiloh.single;

import org.apache.commons.lang3.time.DateUtils;
import org.apache.shardingsphere.infra.hint.HintManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.shiloh.constant.DatePatternConstant;
import org.shiloh.constant.ShardingTableName;
import org.shiloh.entity.AttendanceRecord;
import org.shiloh.mapper.AttendanceRecordMapper;
import org.shiloh.sharding.AttendanceShardingModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.ParseException;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

/**
 * 单表多列分片单元测试
 *
 * @author shiloh
 * @date 2024/9/29 14:41
 */
@SpringBootTest
class MultiColumnShardingTests {
    @Autowired
    private AttendanceRecordMapper attendanceRecordMapper;

    /**
     * 单表多字段分片查询测试（不跨月）
     *
     * @author shiloh
     * @date 2024/9/29 14:42
     */
    @Test
    void testSelect() throws ParseException {
        try (final HintManager hintManager = HintManager.getInstance()) {
            final Date clockInStartTime = DateUtils.parseDate(
                    "2024-09-01 00:00:00", DatePatternConstant.NORM_DATETIME_PATTERN
            );
            final Date clockInEndTime = DateUtils.parseDate(
                    "2024-09-30 23:59:59", DatePatternConstant.NORM_DATETIME_PATTERN
            );
            final AttendanceShardingModel shardingModel = new AttendanceShardingModel(1L, clockInStartTime);
            hintManager.addTableShardingValue(ShardingTableName.ATTENDANCE_RECORD.getLogicTableName(), shardingModel);
            final List<AttendanceRecord> attendanceRecords = this.attendanceRecordMapper.selectAll(
                    1L, clockInStartTime, clockInEndTime
            );
            Assertions.assertThat(attendanceRecords).isNotEmpty();
        }
    }

    /**
     * 单表多字段分片查询测试（跨月）
     * <p>
     * 跨多少月份，就要添加多少个 TableShardingValue，这样才能知道要查多少个表
     * <p>
     * 当涉及到跨多个表查询时，会使用 union all 合并查询结果集
     *
     * @author shiloh
     * @date 2024/9/29 15:52
     */
    @Test
    void testSelectAcrossMonth() throws ParseException {
        try (final HintManager hintManager = HintManager.getInstance()) {
            final Date clockInStartTime = DateUtils.parseDate(
                    "2024-08-01 00:00:00", DatePatternConstant.NORM_DATETIME_PATTERN
            );
            hintManager.addTableShardingValue(
                    ShardingTableName.ATTENDANCE_RECORD.getLogicTableName(),
                    new AttendanceShardingModel(1L, clockInStartTime)
            );
            final Date clockInEndTime = DateUtils.parseDate(
                    "2024-09-30 23:59:59", DatePatternConstant.NORM_DATETIME_PATTERN
            );
            hintManager.addTableShardingValue(
                    ShardingTableName.ATTENDANCE_RECORD.getLogicTableName(),
                    new AttendanceShardingModel(1L, clockInEndTime)
            );

            final Period period = Period.between(
                    clockInStartTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
                    clockInEndTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
            );
            // 使用年份 + 12 将统计单位统一为月份，避免跨年计算时，月份不准确的情况
            Assertions.assertThat(period.getYears() * 12 + period.getMonths()).isEqualTo(1);

            final List<AttendanceRecord> attendanceRecords = this.attendanceRecordMapper.selectAll(
                    1L, clockInStartTime, clockInEndTime
            );
            Assertions.assertThat(attendanceRecords).isNotEmpty();
        }
    }
}
