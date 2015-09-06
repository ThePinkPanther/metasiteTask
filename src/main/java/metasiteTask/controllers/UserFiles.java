package metasiteTask.controllers;

import metasiteTask.Application;
import metasiteTask.exceptions.SynchronousProcedureAlreadyRunningException;
import metasiteTask.services.FileService;
import metasiteTask.services.WordFilterService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * @author ben
 * @version 1.1
 */

@RestController
@RequestMapping("/files/user")
public class UserFiles extends FileControllerBase {
    @Override
    protected FileService getFileService() {
        return Application.getUserFileService();
    }

    @RequestMapping(value = "/generate", method = GET)
    public ResponseEntity<Void> generateFiles() {
        WordFilterService service = Application.getFilterService();
        try {
            service.start();
            return new ResponseEntity<Void>(HttpStatus.OK);
        } catch (SynchronousProcedureAlreadyRunningException e) {
            return new ResponseEntity<Void>(HttpStatus.LOCKED);
        }
    }

}
