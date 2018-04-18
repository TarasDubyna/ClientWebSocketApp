package taras.clientwebsocketapp.custom_views.selected_file_view;

public interface SelectedFileViewCallback {
    void removeAllFromSelectedFiles();
    //void showDevices();
    void removeAllSelectedFiles();
    void removeAllSelectedDevices();


    void clickShare();
    void clickCancel();
}
