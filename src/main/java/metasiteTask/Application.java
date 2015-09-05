package metasiteTask;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import metasiteTask.services.FileService;

/**
 * @author ben
 * @version 1.0
 */

@SpringBootApplication
public class Application {

    private static FileService userFileService;
    private static FileService generatedFileService;

    public static FileService getUserFileService() {
        return userFileService;
    }

    public static FileService getGeneratedFileService() {
        return generatedFileService;
    }

    public static void main(String[] args) {
        // parse arguments
        String uploadDir = "files/uploaded/";
        String generatedDir = "files/generated/";

        if (args.length >= 1)
            uploadDir = args[0];
        if (args.length >= 2)
            generatedDir = args[1];

        // Load metasiteTask.services
        userFileService = new FileService(uploadDir);
        generatedFileService = new FileService(generatedDir);

        // Run Spring
        SpringApplication.run(Application.class, args);
    }



}