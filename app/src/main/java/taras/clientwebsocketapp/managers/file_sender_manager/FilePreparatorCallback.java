package taras.clientwebsocketapp.managers.file_sender_manager;

import java.util.List;

import taras.clientwebsocketapp.model.FileSendPackage;

public interface FilePreparatorCallback {
    void getFileForSendList(List<FileSendPackage> fileSendPackageList);
}
