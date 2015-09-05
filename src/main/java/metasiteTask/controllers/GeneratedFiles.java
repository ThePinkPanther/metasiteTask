package metasiteTask.controllers;

import metasiteTask.Application;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;
import metasiteTask.services.FileService;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URISyntaxException;

/**
 * @author ben
 * @version 1.0
 */

@RestController
@RequestMapping("/files/generated")
public class GeneratedFiles extends FileControllerBase {

    @Override
    protected FileService getFileService() {
        return Application.getGeneratedFileService();
    }

    /**
     * Disabled for this controller
     * */
    @Override
    public ResponseEntity<String> upload(
            @RequestParam("name") String name,
            @RequestParam("file") MultipartFile file,
            HttpServletRequest request)
            throws URISyntaxException, IOException, NoSuchRequestHandlingMethodException {
        throw new NoSuchRequestHandlingMethodException(request);
    }

}
