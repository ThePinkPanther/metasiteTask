package metasiteTask;

import metasiteTask.services.FileService;
import metasiteTask.services.WordFilterService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author ben
 * @version 1.0
 */

@SpringBootApplication
public class Application {

    private static FileService userFileService;
    private static FileService generatedFileService;
    private static WordFilterService filterService;

    public static FileService getUserFileService() {
        return userFileService;
    }

    public static FileService getGeneratedFileService() {
        return generatedFileService;
    }

    public static WordFilterService getFilterService() {
        return filterService;
    }

    public static void main(String[] args) {
        // parse arguments
        String uploadDir = "files/uploaded/";
        String generatedDir = "files/generated/";

        if (args.length >= 1)
            uploadDir = args[0];
        if (args.length >= 2)
            generatedDir = args[1];

        // Load metasiteTask services
        userFileService = new FileService(uploadDir);
        generatedFileService = new FileService(generatedDir);
        filterService = new WordFilterService(userFileService, generatedFileService);

        // Run Spring
        SpringApplication.run(Application.class, args);
    }


}