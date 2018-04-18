package taras.clientwebsocketapp.model;

import java.io.File;

public interface FileManagerHolderClickCallback {
    void longClick(int position);
    void shortClick(int position);
    void moreInfoClick(File file);
}
