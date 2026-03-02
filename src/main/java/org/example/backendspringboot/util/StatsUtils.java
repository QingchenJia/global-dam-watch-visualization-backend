package org.example.backendspringboot.util;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import org.apache.commons.lang3.ObjectUtils;
import org.example.backendspringboot.common.RangeData;
import org.example.backendspringboot.common.StatsData;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class StatsUtils {

    public static <T> List<StatsData> formatStats(List<T> tList, Function<? super T, ?> getField) {
        Map<Object, Integer> count = tList.stream()
                // 过滤掉具体数据值为空字符串或-99的情况
                .filter(t -> {
                    Object fieldValue = getField.apply(t);
                    return !(fieldValue instanceof Long && (Long) fieldValue == -99)
                            && !(fieldValue instanceof Double && (Double) fieldValue == -99)
                            && !("".equals(fieldValue));
                })
                .collect(Collectors.groupingBy(
                        getField,
                        Collectors.summingInt(e -> 1)
                ));

        return count.entrySet()
                .stream()
                .map(entry ->
                        new StatsData(ObjectUtils.toString(entry.getKey()), entry.getValue()))
                .toList();
    }

    public static <T> LambdaQueryWrapper<T> setRange(
            LambdaQueryWrapper<T> queryWrapper,
            SFunction<T, ?> getField,
            RangeData rangeData
    ) {
        if (rangeData != null) {
            return queryWrapper.eq(rangeData.getEq() != -1, getField, rangeData.getEq())
                    .gt(rangeData.getGt() != -1, getField, rangeData.getGt())
                    .ge(rangeData.getGe() != -1, getField, rangeData.getGe())
                    .lt(rangeData.getLt() != -1, getField, rangeData.getLt())
                    .le(rangeData.getLe() != -1, getField, rangeData.getLe());
        }
        return queryWrapper;
    }
}
