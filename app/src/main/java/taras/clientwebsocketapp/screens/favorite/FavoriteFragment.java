package taras.clientwebsocketapp.screens.favorite;

import android.support.v4.app.Fragment;

/**
 * Created by Taras on 04.03.2018.
 */

public class FavoriteFragment extends Fragment {
    /*
    private static final String LOG_TAG = "myLogs";

    @BindView(R.id.rvFiles)
    RecyclerView rvFiles;
    @BindView(R.id.rvDirectories)
    RecyclerView rvDirectories;
    @BindView(R.id.tvEmptyFolder)
    TextView tvEmptyFolder;

    DirectoryAdapter directoryAdapter;
    FileManagerAdapter fileManagerAdapter;

    private View rootView;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(LOG_TAG, "FileManagerFragment, onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(LOG_TAG, "FileManagerFragment, onCreateView");
        rootView = inflater.inflate(R.layout.fragment_file_manager, container, false);
        ButterKnife.bind(this, rootView);

        initDirectoryRecyclers();
        initFileManagerRecyclers();


        return rootView;
    }
    @Override
    public void onResume() {
        super.onResume();
        if (!SelectedFileManager.getSelectedFileManager().isSelectedFilesListEmpty()){
            fileManagerAdapter.setFooterVisible(true);
        }
        SelectedFileManager.getSelectedFileManager().setSelectedFileView(((MainActivity)getActivity()).getSelectedFileView(), () -> {
            fileManagerAdapter.setFooterVisible(false);});
        Log.d(LOG_TAG, "FileManagerFragment, onResume");
        ((MainActivity) getActivity()).setToolbarTitle(getString(R.string.favorite));
    }

    private void initFileManagerRecyclers(){
        fileManagerAdapter = new FileManagerAdapter(getContext(), this, FavoriteFilesManager.getInstance().getAllStringFavorites());
        rvFiles.setHasFixedSize(true);
        rvFiles.setLayoutManager(new GridLayoutManager(getContext(), 1));
        rvFiles.setAdapter(fileManagerAdapter);
    }

    private void initDirectoryRecyclers(){
        directoryAdapter = new DirectoryAdapter(DirectoryAdapter.FAVORITE, getContext(), this);
        rvDirectories.setHasFixedSize(true);
        rvDirectories.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        rvDirectories.setAdapter(directoryAdapter);
    }

    @Override
    public void returnToPosition(int position) {
        directoryAdapter.removeListToPosition(position);
        if (position == 0){
            fileManagerAdapter.setFavoritesDirectory(FavoriteFilesManager.getInstance().getAllStringFavorites());
        } else {
            fileManagerAdapter.setCurrentDirectory(directoryAdapter.getDirectoryOfListByPosition(position));
        }
        rvFiles.setVisibility(View.VISIBLE);
        tvEmptyFolder.setVisibility(View.GONE);
    }

    @Override
    public void longItemClick(int position) {

    }

    @Override
    public void shortItemClick(int position) {

    }

    @Override
    public void moveNextDirectory(String directory) {
        File file = new File(directory);
        directoryAdapter.addDirectory(directory);
        fileManagerAdapter.setCurrentDirectory(directory);
        if (file.listFiles().length > 0){
            rvFiles.setVisibility(View.VISIBLE);
            tvEmptyFolder.setVisibility(View.GONE);
        } else {
            rvFiles.setVisibility(View.GONE);
            tvEmptyFolder.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void callFileInfo(File file) {
        FileInfoDialog fileInfoDialog = new FileInfoDialog();
        fileInfoDialog.setParamsInfo(file, this);
        fileInfoDialog.show(getFragmentManager(), "sdf");
    }

    @Override
    public void updateFileManagerRecyclerAll() {
        fileManagerAdapter.setNewFileList(FavoriteFilesManager.getInstance().getAllFilesFavorites());
        fileManagerAdapter.notifyDataSetChanged();
    }

    @Override
    public void updateAfterFavorite() {
        fileManagerAdapter.setNewFileList(FavoriteFilesManager.getInstance().getAllFilesFavorites());
        fileManagerAdapter.notifyDataSetChanged();
    }
    */
}
