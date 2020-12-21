package rural.rhiss.Util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by acer on 6/7/2017.
 */

public class DateFormat {
    public static String convertDate(String date) {
        SimpleDateFormat spf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        try {
            Date newDate = spf.parse(date);
            spf = new SimpleDateFormat("dd-MMM-yyyy hh:mm");
            date = spf.format(newDate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }

    public static String currentDate() {
        long date = System.currentTimeMillis();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = sdf.format(date);
        return dateString;
    }

    public static String currentTime() {
        long time = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
        String dateString = sdf.format(time);
        return dateString;
    }

    public static String currentDateActivity() {
        long date = System.currentTimeMillis();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = sdf.format(date);
        return dateString;
    }

}
