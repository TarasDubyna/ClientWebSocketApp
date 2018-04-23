package taras.clientwebsocketapp.utils;

/**
 * Created by Taras on 08.02.2018.
 */

public class Constants {
    public static final long REALM_DB_VERSION = 3L;

    public static final int SERVER_PORT = 20000;

    public static final String PACKAGE_TYPE_SCANNING = "package_type_scanning";
    public static final String PACKAGE_TYPE_PERMISSION_FIRST_STAGE = "package_type_permission_first_stage";
    public static final String PACKAGE_TYPE_PERMISSION_SECOND_STAGE = "package_type_permission_second_stage";
    public static final String PACKAGE_TYPE_SERVER_STATE = "package_type_server_state";

    //EventBus
    public static final String START_SCANNING = "start_scanning";
    public static final String SEND_FILE = "send_file";


    //FileManager type
    public static final String FILE_MANAGER_TYPE = "FILE_MANAGER_TYPE";
    public static final int CONTENT_USUAL = 0;
    public static final int CONTENT_FAVORITE = 1;

    public static final String START_SCANNING_FOR_FILE = "start_scanning_for_file";

}
