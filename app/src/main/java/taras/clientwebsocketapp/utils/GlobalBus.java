package taras.clientwebsocketapp.utils;

import com.squareup.otto.Bus;

/**
 * Created by Taras on 17.02.2018.
 */

public class GlobalBus {
    private static Bus sBus;
    public static Bus getBus() {
        if (sBus == null)
            sBus = new Bus();
        return sBus;
    }
}

