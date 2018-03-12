package taras.clientwebsocketapp.utils;

import com.squareup.otto.Bus;

/**
 * Created by Taras on 17.02.2018.
 */

public class GlobalBus {

    public static final int TO_SERVICE = 0;
    public static final int TO_APP = 1;


    private static Bus sBus;
    public static Bus getBus() {
        if (sBus == null)
            sBus = new Bus();
        return sBus;
    }
}

