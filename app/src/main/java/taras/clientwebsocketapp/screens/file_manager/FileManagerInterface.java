package taras.clientwebsocketapp.screens.file_manager;

import java.io.File;

/**
 * Created by Taras on 26.02.2018.
 */

public interface FileManagerInterface {
    void longItemClick(int position);
    void shortItemClick(int position);
    void moveNextDirectory(String directory);
    void callFileInfo(File file);
}
