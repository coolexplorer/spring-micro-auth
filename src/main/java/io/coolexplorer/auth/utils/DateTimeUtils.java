package io.coolexplorer.auth.utils;

import lombok.extern.slf4j.Slf4j;

import java.util.Date;

@Slf4j
public class DateTimeUtils {
    public static Long getSecondsBetweenDates(Date src, Date dest) {
        if (src != null && dest != null) {
            long diff = dest.getTime() - src.getTime();

            LOGGER.debug("src : {}, dest : {}, diff : {}", src, dest, diff);

            return diff / 1000;
        }

        return -1L;
    }
}
