package taras.clientwebsocketapp.utils;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.internal.Utils;

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

    public static void startTimer(int milliseconds, TimerTask timerTaskCallback){
        Timer timer = new Timer();
        timer.schedule(timerTaskCallback, milliseconds);

        /*
        timer.scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {
                final float value = Utils.randInt(-10, 35);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("Info", "Value: " + value);
                    }
                });
            }
        }, 0, milliseconds);
        */
    }

}
