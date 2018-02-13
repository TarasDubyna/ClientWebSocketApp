package taras.clientwebsocketapp.network;

/**
 * Created by Taras on 08.02.2018.
 */

public class NetworkConnection {
    private static ConnectionRepository sConnectionRepository;

    public static ConnectionRepository getConnectionRepository(){
        if (sConnectionRepository == null){
            sConnectionRepository = new NetworkDataRepository();
        }
        return sConnectionRepository;
    }
}
