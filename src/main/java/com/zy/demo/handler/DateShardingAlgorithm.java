package com.zy.demo.handler;

import com.google.common.collect.Range;
import org.apache.shardingsphere.sharding.api.sharding.standard.PreciseShardingValue;
import org.apache.shardingsphere.sharding.api.sharding.standard.RangeShardingValue;
import org.apache.shardingsphere.sharding.api.sharding.standard.StandardShardingAlgorithm;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

/**
 * 日期分片算法
 *
 * @author zy
 */
public class DateShardingAlgorithm implements StandardShardingAlgorithm<LocalDateTime> {

    private DateTimeFormatter dateTimeFormatter;

    /**
     * 精确分片
     *
     * @param collection           分片数据源的所有物理表名
     * @param preciseShardingValue 精确分片值
     * @return 精确的物理表名
     */
    @Override
    public String doSharding(Collection<String> collection, PreciseShardingValue<LocalDateTime> preciseShardingValue) {
        LocalDateTime localDateTime = preciseShardingValue.getValue();
        String suffix = localDateTime.format(this.dateTimeFormatter);
        String targetTable = preciseShardingValue.getLogicTableName() + "_" + suffix;
        if (collection.contains(targetTable)) {
            return targetTable;
        }
        throw new IllegalArgumentException("no table found for:" + targetTable);
    }

    /**
     * 范围分片，尽量缩小查询的物理表范围。
     *
     * @param collection         分片数据源的所有物理表名
     * @param rangeShardingValue 范围分片值
     * @return 物理表名集合
     */
    @Override
    public Collection<String> doSharding(Collection<String> collection, RangeShardingValue<LocalDateTime> rangeShardingValue) {
        Range<LocalDateTime> range = rangeShardingValue.getValueRange();
        LocalDateTime lower = range.hasLowerBound() ? range.lowerEndpoint() : null;
        LocalDateTime upper = range.hasUpperBound() ? range.upperEndpoint() : null;
        if (lower == null && upper == null) {
            return collection;
        }
        //如果时间范围无下限，则开始时间取上限值年份减1年
        YearMonth start = lower != null ? YearMonth.from(lower) : YearMonth.from(upper).minusYears(1);
        //如果时间范围无上限，则结束时间取下限制年份加1年
        YearMonth end = upper != null ? YearMonth.from(upper) : YearMonth.from(lower).plusYears(1);
        List<String> tables = new ArrayList<>();
        YearMonth current = start;
        while (!current.isAfter(end)) {
            String tableName = rangeShardingValue.getLogicTableName() + "_" + current.format(this.dateTimeFormatter);
            if (collection.contains(tableName)) {
                tables.add(tableName);
            }
            current = current.plusMonths(1);
        }
        return tables;
    }

    /**
     * 分片算法名称，关联配置文件的type
     *
     * @return String
     */
    @Override
    public String getType() {
        return "SHARDING_TABLE_BY_DATE";
    }

    /**
     * 读取配置文件属性
     *
     * @param properties Properties
     */
    @Override
    public void init(Properties properties) {
        this.dateTimeFormatter = DateTimeFormatter.ofPattern(properties.getProperty("date-pattern", "yyyyMM"));
    }
}
