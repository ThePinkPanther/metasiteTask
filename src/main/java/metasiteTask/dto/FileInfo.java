package metasiteTask.dto;

/**
 * @author ben
 * @version 1.0
 */
public class FileInfo {

    private final String name;
    private final String url;

    public FileInfo(String name, String url) {

        this.name = name;
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public String getName() {
        return name;
    }

}
