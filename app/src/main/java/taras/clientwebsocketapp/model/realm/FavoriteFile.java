package taras.clientwebsocketapp.model.realm;

import io.realm.RealmObject;
import io.realm.annotations.Required;

/**
 * Created by Taras on 01.03.2018.
 */

public class FavoriteFile extends RealmObject {

    @Required
    private String directory;

    public FavoriteFile() {
    }

    public String getDirectory() {
        return directory;
    }

    public void setDirectory(final String directory) {
        this.directory = directory;
    }
}
