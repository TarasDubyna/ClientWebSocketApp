package taras.clientwebsocketapp.screens.file_manager;

public interface DirectoryInterface {
    void moveToDirectory(String directory);
    void goToZeroPosition(String directory);
    void changeTypeMemory(String directory, int memoryType);
}
