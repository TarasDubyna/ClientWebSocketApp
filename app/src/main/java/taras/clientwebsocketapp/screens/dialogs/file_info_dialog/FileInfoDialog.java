package taras.clientwebsocketapp.screens.dialogs.file_info_dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import taras.clientwebsocketapp.R;
import taras.clientwebsocketapp.managers.FavoriteFilesManager;
import taras.clientwebsocketapp.utils.FileUtils;

/**
 * Created by Taras on 27.02.2018.
 */

public class FileInfoDialog extends android.support.v4.app.DialogFragment {
    private static final String LOG_TAG = "myLogs";

    @BindView(R.id.tvName)
    TextView tvName;
    @BindView(R.id.tvDirectory)
    TextView tvDirectory;
    @BindView(R.id.tvDate)
    TextView tvDate;
    @BindView(R.id.tvSize)
    TextView tvSize;

    @BindView(R.id.llFileInfo)
    LinearLayout llFileInfo;
    @BindView(R.id.tvFileInfo)
    TextView tvFileInfo;

    //clickable
    @BindView(R.id.tvRename)
    TextView tvRename;
    @BindView(R.id.tvDelete)
    TextView tvDelete;

    @BindView(R.id.llRename)
    LinearLayout llRename;
    @BindView(R.id.etRename)
    EditText etRename;
    @BindView(R.id.btnRenameFile)
    Button btnRenameFile;

    @BindView(R.id.llDelete)
    LinearLayout llDelete;
    @BindView(R.id.btnDeleteFile)
    Button btnDeleteFile;

    @BindView(R.id.tvAddToFavorite)
    TextView tvFavorite;

    private Context mContext;
    private File file;
    private FileInfoDialogInterface fileInfoDialogInterface;

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity();
        View dialogView = inflater.inflate(R.layout.dialog_info_fragment,container, false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        //getDialog().getWindow().setLayout(600, ViewGroup.LayoutParams.WRAP_CONTENT);

        ButterKnife.bind(this, dialogView);
        setInfo();

        return dialogView;
    }

    public void setParamsInfo(File file, FileInfoDialogInterface fileInfoDialogInterface){
        this.file = file;
        this.fileInfoDialogInterface = fileInfoDialogInterface;
    }

    private void setInfo(){
        tvName.setText(file.getName());
        tvDirectory.setText(file.getAbsolutePath());
        tvDate.setText(FileUtils.getCreationTime(file));
        tvSize.setText(FileUtils.getSize(getContext(), file));
        etRename.setText(file.getName());

        if (FavoriteFilesManager.getInstance().isFavorite(file)){
            tvFavorite.setText(R.string.favorites_remove);
        } else {
            tvFavorite.setText(R.string.favorites_add);
        }
    }


    @OnClick({R.id.tvFileInfo, R.id.tvRename, R.id.tvDelete})
    void clickRenameText(View view){
        int id = view.getId();

        if (id == R.id.tvFileInfo){
            if (llFileInfo.getVisibility() == View.VISIBLE){
                llFileInfo.setVisibility(View.GONE);
            } else {
                llFileInfo.setVisibility(View.VISIBLE);
                llRename.setVisibility(View.GONE);
                llDelete.setVisibility(View.GONE);
            }
        } else if (id == R.id.tvRename){
            if (llRename.getVisibility() == View.VISIBLE){
                llRename.setVisibility(View.GONE);
            } else {
                llRename.setVisibility(View.VISIBLE);
                llFileInfo.setVisibility(View.GONE);
                llDelete.setVisibility(View.GONE);
            }
        } else if (id == R.id.tvDelete){
            if (llDelete.getVisibility() == View.VISIBLE){
                llDelete.setVisibility(View.GONE);
            } else {
                llDelete.setVisibility(View.VISIBLE);
                llFileInfo.setVisibility(View.GONE);
                llRename.setVisibility(View.GONE);
            }
        }

        if (llFileInfo.getVisibility() == View.GONE
                && llDelete.getVisibility() == View.GONE
                && llRename.getVisibility() == View.GONE) {
            llFileInfo.setVisibility(View.VISIBLE);
        }
    }


    //delete file
    @OnClick(R.id.btnDeleteFile)
    void deleteFile(View view){
        boolean isFavorite = false;
        if (FavoriteFilesManager.getInstance().isFavorite(file)){
            isFavorite = true;
        }
        if(file.delete()){
            if (isFavorite){
                FavoriteFilesManager.getInstance().removeFromFavorites(file);
            }
            Log.d(LOG_TAG, "FileInfoDialog, file deleted");
            Toast.makeText(getContext(), getString(R.string.file_deleted), Toast.LENGTH_SHORT).show();
            fileInfoDialogInterface.updateFileManagerRecyclerAll();
        }else{
            Log.d(LOG_TAG, "FileInfoDialog, file not deleted");
            Toast.makeText(getContext(), getString(R.string.file_not_deleted), Toast.LENGTH_SHORT).show();
        }
        dismiss();
    }

    //rename file
    @OnClick(R.id.btnRenameFile)
    void renameFile(View view){
        boolean isFavorite = false;
        if (FavoriteFilesManager.getInstance().isFavorite(file)){
            isFavorite = true;
        }

        String filepath = file.getParent();
        File from = new File(filepath, file.getName());
        File to = new File(filepath, etRename.getText().toString().trim());

        if(from.renameTo(to)){
            if (isFavorite){
                FavoriteFilesManager.getInstance().removeFromFavorites(from);
                FavoriteFilesManager.getInstance().addToFavorite(to);
            }
            Log.d(LOG_TAG, "FileInfoDialog, file rename successful");
            Toast.makeText(getContext(), getString(R.string.rename_successful), Toast.LENGTH_SHORT).show();
            fileInfoDialogInterface.updateFileManagerRecyclerAll();
        }else{
            Log.d(LOG_TAG, "FileInfoDialog, file rename not successful");
            Toast.makeText(getContext(), getString(R.string.rename_not_successful), Toast.LENGTH_SHORT).show();
        }
        dismiss();
    }

    //favorite add/delete
    @OnClick(R.id.tvAddToFavorite)
    void favoriteFile(View view){
        if (tvFavorite.getText().toString().equals(getString(R.string.favorites_add))){
            FavoriteFilesManager.getInstance().addToFavorite(file);
            tvFavorite.setText(getString(R.string.favorites_remove));
        } else {
            FavoriteFilesManager.getInstance().removeFromFavorites(file);
            tvFavorite.setText(getString(R.string.favorites_add));
        }
        dismiss();
        fileInfoDialogInterface.updateAfterFavorite();
    }
}
