package metasiteTask.controllers;

import metasiteTask.Application;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import metasiteTask.services.FileService;

/**
 * @author ben
 * @version 1.0
 */

@RestController
@RequestMapping("/files/user")
public class UserFiles extends FileControllerBase {
    @Override
    protected FileService getFileService() {
        return Application.getUserFileService();
    }
}
