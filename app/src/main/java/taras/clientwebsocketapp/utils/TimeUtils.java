package taras.clientwebsocketapp.utils;

import java.util.Date;

/**
 * Created by Taras on 05.04.2018.
 */

public class TimeUtils {

    public static long getCurrentTime(){
        return System.currentTimeMillis();
    }

    public static Date convertTimeToDate(long time){
        return new Date(time);
    }

}
