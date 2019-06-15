package cobong.jeongwoojin.cobongmemo.cobongmemo.common.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

    static public String curDate() {
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
        String getTime = sdf.format(date);

        return getTime;
    }

}
