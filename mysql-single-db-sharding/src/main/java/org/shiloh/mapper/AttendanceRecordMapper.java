package org.shiloh.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.shiloh.entity.AttendanceRecord;

import java.util.Date;
import java.util.List;

/**
 * 打卡记录 DAO
 *
 * @author shiloh
 * @date 2024/9/29 14:36
 */
@Mapper
public interface AttendanceRecordMapper {
    /**
     * 根据部门 ID 和打卡时间范围查询打卡记录列表
     *
     * @param deptId           部门 ID
     * @param clockInStartTime 打卡开始时间
     * @param clockInEndTime   打卡结束时间
     * @return 打卡记录列表
     * @author shiloh
     * @date 2024/9/29 14:38
     */
    List<AttendanceRecord> selectAll(
            @Param("deptId") Long deptId,
            @Param("clockInTimeStart") Date clockInStartTime,
            @Param("clockInTimeEnd") Date clockInEndTime
    );
}
