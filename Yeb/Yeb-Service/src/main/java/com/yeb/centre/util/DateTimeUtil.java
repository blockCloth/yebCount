package com.yeb.centre.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author dai
 * @create 2022-02-2022/2/5  23-14-26
 */
@Slf4j
public class DateTimeUtil {

    /**
     * 将String[]转换为localdate数组
     * @param datetimes
     * @return
     */
    public static LocalDate[] parseForDate(String[] datetimes){
        //创建localDate[]数组
        LocalDate[] localDates = new LocalDate[2];
        //判断数组是否为空
        for (int i = 0; i < datetimes.length; i++) {
            if (!StringUtils.isEmpty(datetimes[i])){
                String source = datetimes[i];
                log.info(source);
                //设置时间
                localDates[i] = LocalDate.parse(source,DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            }
        }
        if (localDates[0] != null && localDates[1] != null){
            return localDates;
        }
        return null;
    }
}
