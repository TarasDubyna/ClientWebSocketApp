package taras.clientwebsocketapp.managers;

import android.content.Context;

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

    private Realm mRealm;

    private List<FavoriteFile> filesDirectoriesList;

    private static FavoriteFilesManager favoriteFilesManager;

    public static FavoriteFilesManager getInstance(Context context){
        if (favoriteFilesManager == null){
            favoriteFilesManager = new FavoriteFilesManager(context);
        }
        return favoriteFilesManager;
    }

    public FavoriteFilesManager(Context context) {
        Realm.init(context);
        mRealm = Realm.getDefaultInstance();
        initFavoriteList();
    }


    private void initFavoriteList(){
        mRealm.beginTransaction();
        this.filesDirectoriesList = mRealm.copyFromRealm(mRealm.where(FavoriteFile.class).findAll());
        mRealm.commitTransaction();
    }

    public void addToFavorite(File file){
        FavoriteFile favoriteFile = new FavoriteFile();
        favoriteFile.setDirectory(file.getPath());
        this.filesDirectoriesList.add(favoriteFile);
        addToRealm(file.getPath());
    }
    public void addToFavorite(String fileDirectory){
        FavoriteFile favoriteFile = new FavoriteFile();
        favoriteFile.setDirectory(fileDirectory);
        this.filesDirectoriesList.add(favoriteFile);
        addToRealm(fileDirectory);
    }
    public void removeFromFavorites(File file){
        FavoriteFile favoriteFile = new FavoriteFile();
        favoriteFile.setDirectory(file.getPath());
        this.filesDirectoriesList.remove(favoriteFile);
        deleteFromRealm(file.getPath());
    }
    public void removeFromFavorites(String fileDirectory){
        FavoriteFile favoriteFile = new FavoriteFile();
        favoriteFile.setDirectory(fileDirectory);
        this.filesDirectoriesList.remove(favoriteFile);
        deleteFromRealm(fileDirectory);
    }
    public boolean isFavorite(File file){
        if (filesDirectoriesList.contains(file.getPath())){
            return true;
        } else {
            return false;
        }
    }


    //work with Realm
    private void addToRealm(String directory){
        mRealm.beginTransaction();
        FavoriteFile favoriteFile = new FavoriteFile();
        favoriteFile.setDirectory(directory);
        favoriteFile = mRealm.createObject(FavoriteFile.class);

        mRealm.commitTransaction();
    }
    public void deleteFromRealm(String directory){
        mRealm.beginTransaction();
        RealmResults<FavoriteFile> directories = mRealm.where(FavoriteFile.class).equalTo("directory",directory).findAll();
        if(!directories.isEmpty()) {
            for(int i = directories.size() - 1; i >= 0; i--) {
                directories.get(i).deleteFromRealm();
            }
        }
        mRealm.commitTransaction();
    }

}
