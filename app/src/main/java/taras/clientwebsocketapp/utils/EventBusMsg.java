package taras.clientwebsocketapp.utils;

/**
 * Created by Taras on 05.04.2018.
 */

public class EventBusMsg<T> {

    public static final int TO_SERVICE = 0;
    public static final int TO_APP = 1;

    public static final int PACKAGE_SCANNER = 0;
    public static final int PACKAGE_PERMISSION = 1;
    public static final int PACKAGE_SERVER_STATE = 3;
    public static final int CHECK_IS_SERVER_WORK = 4;

    public static final int SERVER_START = 5;
    public static final int SERVER_STOP = 6;

    public static final int SCANNING_NETWORK_END = 100;

    private int codeDirection;
    private int codeType;
    private T model;
    private String message;

    public EventBusMsg(int codeDirection, int codeType, T model) {
        this.codeDirection = codeDirection;
        this.codeType = codeType;
        this.model = model;
    }

    public EventBusMsg(int codeDirection, String message) {
        this.codeDirection = codeDirection;
        this.message = message;
    }

    public int getCodeDirection() {
        return codeDirection;
    }

    public int getCodeType() {
        return codeType;
    }

    public T getModel() {
        return model;
    }

    public String getMessage() {
        return message;
    }
}
