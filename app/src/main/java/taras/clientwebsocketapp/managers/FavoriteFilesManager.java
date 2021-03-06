package taras.clientwebsocketapp.managers;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import taras.clientwebsocketapp.model.realm.FavoriteFile;

/**
 * Created by Taras on 01.03.2018.
 */

public class FavoriteFilesManager {

    private static final String LOG_TAG = "myLogs";

    private List<FavoriteFile> filesDirectoriesList;
    private static FavoriteFilesManager favoriteFilesManager;

    public static FavoriteFilesManager getInstance(){
        if (favoriteFilesManager == null){
            favoriteFilesManager = new FavoriteFilesManager();
        }
        return favoriteFilesManager;
    }

    public FavoriteFilesManager() {
        this.filesDirectoriesList = new ArrayList<>();
        initFavoriteList();
    }


    public void addToFavorite(File file){
        FavoriteFile favoriteFile = new FavoriteFile();
        favoriteFile.setDirectory(file.getPath());
        this.filesDirectoriesList.add(favoriteFile);
        insertToRealm(file.getPath());
    }
    public void addToFavorite(String fileDirectory){
        FavoriteFile favoriteFile = new FavoriteFile();
        favoriteFile.setDirectory(fileDirectory);
        this.filesDirectoriesList.add(favoriteFile);
        insertToRealm(fileDirectory);
    }
    public void removeFromFavorites(File file){
        for (int i = 0; i < filesDirectoriesList.size(); i++){
            if (filesDirectoriesList.get(i).getDirectory().equals(file.getPath())){
                filesDirectoriesList.remove(i);
                break;
            }
        }
        deleteFromRealm(file.getPath());
    }
    public void removeFromFavorites(String fileDirectory){
        FavoriteFile favoriteFile = new FavoriteFile();
        favoriteFile.setDirectory(fileDirectory);
        this.filesDirectoriesList.remove(favoriteFile);
        deleteFromRealm(fileDirectory);
    }
    public boolean isFavorite(File file){
        FavoriteFile favoriteFile = new FavoriteFile();
        favoriteFile.setDirectory(file.getPath());
        for (FavoriteFile favoriteFileRealm: filesDirectoriesList){
            if (favoriteFile.getDirectory().equals(favoriteFileRealm.getDirectory())){
                return true;
            }
        }
        return false;
    }


    //work with Realm
    private void initFavoriteList(){
        Realm mRealm = Realm.getDefaultInstance();
        mRealm.executeTransaction(realm -> {
            RealmResults<FavoriteFile> result = realm.where(FavoriteFile.class).findAll();
            if (result != null){
                this.filesDirectoriesList.addAll(mRealm.copyFromRealm(result));
                Log.d(LOG_TAG, "FavoriteFile list from Realm: " + filesDirectoriesList.size());
            }
        });
    }
    private void insertToRealm(String directory){
        Realm.getDefaultInstance().executeTransaction(realm -> {
            FavoriteFile favoriteFile = new FavoriteFile();
            favoriteFile.setDirectory(directory);
            realm.insert(favoriteFile);
            Log.d(LOG_TAG, "Insert to realm FavoriteFile: " + directory);
        });
    }
    public void deleteFromRealm(String directory) {
        Realm.getDefaultInstance().executeTransaction(realm -> {
            RealmResults<FavoriteFile> result = realm.where(FavoriteFile.class).equalTo("directory", directory).findAll();
            result.deleteAllFromRealm();

        });
    }

    public ArrayList<String> getAllStringFavorites(){
        ArrayList<String> favoritesString = new ArrayList<>();
        for (FavoriteFile favoriteFile: filesDirectoriesList){
            favoritesString.add(favoriteFile.getDirectory());
        }
        return favoritesString;
    }

    public ArrayList<File> getAllFilesFavorites(){
        ArrayList<File> favoritesFiles = new ArrayList<>();
        for (FavoriteFile favoriteFile: filesDirectoriesList){
            favoritesFiles.add(new File(favoriteFile.getDirectory()));
        }
        return favoritesFiles;
    }

}
