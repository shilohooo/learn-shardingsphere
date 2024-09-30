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
    // region CUD

    /**
     * 新增打卡记录
     *
     * @param attendanceRecord 打卡记录
     * @author shiloh
     * @date 2024/9/30 9:04
     */
    void insert(@Param("record") AttendanceRecord attendanceRecord);

    /**
     * 根据 ID、部门 ID、打卡时间删除打卡记录
     *
     * @param id          打卡记录 ID
     * @param deptId      部门 ID
     * @param clockInTime 打卡时间
     * @author shiloh
     * @date 2024/9/30 9:21
     */
    void delete(@Param("id") Long id, @Param("deptId") Long deptId, @Param("clockInTime") Date clockInTime);

    /**
     * 修改打卡记录
     *
     * @param attendanceRecord 打卡记录
     * @author shiloh
     * @date 2024/9/30 9:26
     */
    void update(@Param("record") AttendanceRecord attendanceRecord);

    // endregion

    // region R

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

    /**
     * 根据 ID、部门 ID、打卡时间查询打卡记录
     *
     * @param id          打卡记录 ID
     * @param deptId      部门 ID
     * @param clockInTime 打卡时间
     * @return 打卡记录
     * @author shiloh
     * @date 2024/9/30 9:22
     */
    AttendanceRecord selectOne(
            @Param("id") Long id, @Param("deptId") Long deptId, @Param("clockInTime") Date clockInTime
    );

    // endregion
}
