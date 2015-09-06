package metasiteTask.services;

import org.apache.tomcat.util.http.fileupload.FileUtils;

import java.io.*;

/**
 * @author ben
 * @version 1.0
 */
public class FileService {

    public final String directory;

    public FileService(String directory) {
        this.directory = directory;
        mkdir(directory);
    }

    private void mkdir(String name) {
        File directory = new File(name);
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }

    public File[] listFiles() {
        File fileDir = new File(directory);
        return fileDir.listFiles();
    }

    public String getDirectory() {
        return directory;
    }

    public void removeAll()
            throws IOException {
        FileUtils.cleanDirectory(new File(directory));
    }

    public void remove(final String name)
            throws FileNotFoundException {
        File file = new File(directory + name);
        if (file.exists() && file.isFile() && file.delete()) {
            return;
        }
        throw new FileNotFoundException();
    }

    /**
     * Returns file if exists, if not creates a new file instead
     * @param name file name
     * @return file handle
     * @throws IOException
     */
    public File getFileHandle(final String name)
            throws IOException {
        File file = new File(directory+name);
        if(file.isDirectory())
            throw new IOException("Couldn't get a handle for file: "+name+". It's a directory");
        if (!file.exists() ) {
            file.createNewFile();
        }
        return file;
    }

    /**
     * Saves given byte array to a file
     * @param data
     * @param name
     * @throws IOException
     */
    public void save(final byte[] data, final String name)
            throws IOException {
        File file = new File(directory + name);
        if (!file.exists()) {
            file.createNewFile();
        }
        try (BufferedOutputStream stream = new BufferedOutputStream(
                new FileOutputStream(directory + name, false))) {
            stream.write(data);
            stream.close();
        }
    }

    public File getFile(final String name)
            throws FileNotFoundException {
        File file = new File(directory + name);
        if (file.exists() && file.isFile()) {
            return file;
        }
        throw new FileNotFoundException();
    }

}
