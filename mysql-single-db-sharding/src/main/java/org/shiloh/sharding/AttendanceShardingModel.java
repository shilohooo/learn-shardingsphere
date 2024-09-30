package org.shiloh.sharding;

import lombok.*;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.shiloh.common.constant.DatePatternConstant;

import javax.annotation.Nonnull;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 * 打卡记录分片参数 Model
 *
 * @author shiloh
 * @date 2024/9/29 14:10
 */
@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceShardingModel implements Serializable, Comparable<Date> {
    private static final long serialVersionUID = -3712868104271670922L;

    /**
     * 部门 ID
     */
    private Long deptId;

    /**
     * 打卡日期
     */
    private Date clockInDate;

    /**
     * 获取分片字符串
     *
     * @return 分片字符串
     * @author shiloh
     * @date 2024/9/29 15:43
     */
    public String getShardingValue() {
        final String month = DateFormatUtils.format(this.clockInDate, DatePatternConstant.YEAR_MONTH_WITHOUT_DASH);
        return String.format("%d_%s", this.deptId, month);
    }

    /**
     * Compares this object with the specified object for order.  Returns a
     * negative integer, zero, or a positive integer as this object is less
     * than, equal to, or greater than the specified object.
     *
     * <p>The implementor must ensure {@link Integer#signum
     * signum}{@code (x.compareTo(y)) == -signum(y.compareTo(x))} for
     * all {@code x} and {@code y}.  (This implies that {@code
     * x.compareTo(y)} must throw an exception if and only if {@code
     * y.compareTo(x)} throws an exception.)
     *
     * <p>The implementor must also ensure that the relation is transitive:
     * {@code (x.compareTo(y) > 0 && y.compareTo(z) > 0)} implies
     * {@code x.compareTo(z) > 0}.
     *
     * <p>Finally, the implementor must ensure that {@code
     * x.compareTo(y)==0} implies that {@code signum(x.compareTo(z))
     * == signum(y.compareTo(z))}, for all {@code z}.
     *
     * @param o the object to be compared.
     * @return a negative integer, zero, or a positive integer as this object
     * is less than, equal to, or greater than the specified object.
     * @throws NullPointerException if the specified object is null
     * @throws ClassCastException   if the specified object's type prevents it
     *                              from being compared to this object.
     * @apiNote It is strongly recommended, but <i>not</i> strictly required that
     * {@code (x.compareTo(y)==0) == (x.equals(y))}.  Generally speaking, any
     * class that implements the {@code Comparable} interface and violates
     * this condition should clearly indicate this fact.  The recommended
     * language is "Note: this class has a natural ordering that is
     * inconsistent with equals."
     */
    @Override
    public int compareTo(@Nonnull Date o) {
        return this.clockInDate.compareTo(o);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final AttendanceShardingModel that = (AttendanceShardingModel) o;
        return Objects.equals(deptId, that.deptId) && Objects.equals(clockInDate, that.clockInDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.deptId, this.clockInDate);
    }
}
