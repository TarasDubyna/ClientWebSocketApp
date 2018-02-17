package taras.clientwebsocketapp.utils;

import android.os.Handler;
import android.os.Looper;

import com.squareup.otto.Bus;

/**
 * Created by Taras on 17.02.2018.
 */

public class BusUtil {
    private static final Handler mainThread = new Handler(Looper.getMainLooper());

    public static void postOnMain(final Bus bus, final Object event) {

        mainThread.post(new Runnable() {
            @Override
            public void run() {
                bus.post(event);
            }
        });
    }
}
