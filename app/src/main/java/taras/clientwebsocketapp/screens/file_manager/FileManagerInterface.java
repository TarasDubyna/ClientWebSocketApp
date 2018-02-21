package taras.clientwebsocketapp.screens.file_manager;

/**
 * Created by Taras on 18.02.2018.
 */

public interface FileManagerInterface {

    void getFolderWithFiles(String absolutePath);
    void getFolderEmpty(String absolutePath);
    void goToPreviousFolder(int position);
}
