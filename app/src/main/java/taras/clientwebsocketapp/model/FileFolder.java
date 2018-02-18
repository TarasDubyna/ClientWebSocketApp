package taras.clientwebsocketapp.model;

import java.io.File;

/**
 * Created by Taras on 18.02.2018.
 */

public class FileFolder {

    private String type;
    private String absolutePath;
    private Folder folder;
    private File file;

    public FileFolder(Folder folder) {
        this.folder = folder;
        this.file = null;
        this.absolutePath = this.folder.folderAbsolutePath;
        this.type = "folder";
    }

    public FileFolder(File file) {
        this.file = file;
        this.folder = null;
        this.absolutePath = this.file.fileAbsolutePath;
        this.type = "file";
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAbsolutePath() {
        return absolutePath;
    }
    public void setAbsolutePath(String absolutePath) {
        this.absolutePath = absolutePath;
    }

    public Folder getFolder() {
        return folder;
    }
    public void setFolder(Folder folder) {
        this.folder = folder;
    }

    public File getFile() {
        return file;
    }
    public void setFile(File file) {
        this.file = file;
    }

    public static class Folder{
        private String folderPath;
        private String folderName;
        private String folderAbsolutePath;

        public Folder(String folderPath, String folderName, String folderAbsolutePath) {
            this.folderPath = folderPath;
            this.folderName = folderName;
            this.folderAbsolutePath = folderAbsolutePath;
        }

        public String getFolderAbsolutePath() {
            return folderAbsolutePath;
        }

        public void setFolderAbsolutePath(String folderAbsolutePath) {
            this.folderAbsolutePath = folderAbsolutePath;
        }

        public String getFolderPath() {
            return folderPath;
        }

        public void setFolderPath(String folderPath) {
            this.folderPath = folderPath;
        }

        public String getFolderName() {
            return folderName;
        }

        public void setFolderName(String folderName) {
            this.folderName = folderName;
        }
    }
    public static class File{
        private String filePath;
        private String fileName;
        private String fileAbsolutePath;

        public File(String filePath, String fileName, String fileAbsolutePath) {
            this.filePath = filePath;
            this.fileName = fileName;
            this.fileAbsolutePath = fileAbsolutePath;
        }

        public String getFileAbsolutePath() {
            return fileAbsolutePath;
        }
        public void setFileAbsolutePath(String fileAbsolutePath) {
            this.fileAbsolutePath = fileAbsolutePath;
        }
        public String getFilePath() {
            return filePath;
        }
        public void setFilePath(String filePath) {
            this.filePath = filePath;
        }
        public String getFileName() {
            return fileName;
        }
        public void setFileName(String fileName) {
            this.fileName = fileName;
        }
    }
}
