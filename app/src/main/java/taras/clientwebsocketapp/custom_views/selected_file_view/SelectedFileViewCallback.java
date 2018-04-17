package taras.clientwebsocketapp.custom_views.selected_file_view;

public interface SelectedFileViewCallback {
    void removeAllFromSelectedFiles();
    void removeAllFromSelectedDevices();
    void showDevices();
    void removeAllSelectedFiles();
    void removeAllSelectedDevices();
}
