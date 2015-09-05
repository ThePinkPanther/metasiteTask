package metasiteTask.controllers;

import metasiteTask.dto.FileInfo;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;
import org.springframework.web.util.UriUtils;
import metasiteTask.services.FileService;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.LinkedList;

import static org.springframework.web.bind.annotation.RequestMethod.*;

/**
 * @author ben
 * @version 1.0
 */
public abstract class FileControllerBase {

    protected abstract FileService getFileService();

    /**
     * Lists files
     *
     * @param request
     * @return
     */
    @RequestMapping(method = GET)
    @ResponseBody
    public Collection<FileInfo> getAll(HttpServletRequest request) throws UnsupportedEncodingException {

        Collection<FileInfo> result = new LinkedList<>();

        File[] filesList = getFileService().listFiles();
        for (File f : filesList) {
            if (f.isFile()) {
                String name = f.getName();
                result.add(
                        new FileInfo(
                                name,
                                request.getRequestURL().toString() +
                                        "/" + UriUtils.encodePath(name, "UTF-8")));
            }
        }

        return result;
    }

    @RequestMapping(value = "/{name}", method = DELETE)
    public ResponseEntity<Void> remove(@PathVariable("name") String name) {
        try {
            getFileService().remove(name);
            return new ResponseEntity<Void>(HttpStatus.OK);
        } catch (FileNotFoundException e) {
            return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(method = DELETE)
    public ResponseEntity<Void> deleteAll()
            throws IOException {
        getFileService().removeAll();
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    /**
     * Returns a file
     *
     * @param name a file name to return
     * @return a file
     */
    @RequestMapping(value = "/{name}", method = GET)
    public ResponseEntity<FileSystemResource> get(@PathVariable("name") String name) {
        try {
            return new ResponseEntity<>(
                    new FileSystemResource(getFileService().getFile(name)),
                    HttpStatus.OK);
        } catch (FileNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Uploads a file
     *
     * @param name    file name
     * @param file    multipart file data
     * @param request request injection
     * @return
     */
    @RequestMapping(method = POST)
    public ResponseEntity<String> upload(
            @RequestParam("name") String name,
            @RequestParam("file") MultipartFile file,
            HttpServletRequest request) throws URISyntaxException, IOException, NoSuchRequestHandlingMethodException {
        byte[] data = file.getBytes();

        getFileService().save(data, name);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(new URI(
                request.getRequestURL().toString() + "/" + UriUtils.encodePath(name, "UTF-8")));
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

}
