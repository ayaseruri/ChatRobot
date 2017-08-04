package uq.edu.au.chatroom.other;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by wufeiyang on 2017/7/5.
 */

public class Utils {
    public static String parseTime(long time) {
        Date date = new Date(time);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        return sdf.format(date);
    }
}
