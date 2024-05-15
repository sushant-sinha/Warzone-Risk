package com.APP.Project.UserCoreLogic.Utility;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * This class provides utility for <code>Date</code> and <code>Time</code>.
 *
 * @author Bhoomiben Bhatt
 * @version 1.0
 */
public class DataTimeUtil {
    /**
     * Retrives the current date and time of Virtual Machine.
     *
     * @return A string representation of the current VM date and time.
     */
    public static String getTime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SS");
        return format.format(new Date());
    }
}
