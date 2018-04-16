package taras.clientwebsocketapp.screens.file_manager;

import java.io.File;

public interface FileManagerAdapterInterface {
    void callFileInfo(File file);
    void moveNextDirectory(String newFileDirectory);
}
