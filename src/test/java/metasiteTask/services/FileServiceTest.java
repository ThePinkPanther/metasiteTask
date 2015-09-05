package metasiteTask.services;

import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.util.FileCopyUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static junit.framework.TestCase.assertEquals;


public class FileServiceTest {

    private static final String MOCK_PATH = "mock/";
    private static final String TEST_PATH = "tmp/test/";
    private File mockDir = new File(MOCK_PATH);
    private File[] mockFiles = {
            new File(MOCK_PATH + "testFile1"),
            new File(MOCK_PATH + "testFile2")};

    private File testDir = new File(TEST_PATH);
    private FileService fs = new FileService(TEST_PATH);

    @Before
    public void setUp() throws Exception {
        if (testDir.exists() && testDir.isDirectory())
            return;
        testDir.mkdirs();
    }

    @After
    public void tearDown() throws Exception {
        FileUtils.cleanDirectory(testDir);
    }

    @Test
    public void testRemoveAll() throws Exception {
        copyFiles();
        fs.removeAll();

        assertEquals(
                "Directory is not empty",
                true,
                testDir.list().length == 0);
    }

    private void copyFiles() throws IOException {
        for (File f : mockFiles) {
            FileCopyUtils.copy(f, new File(TEST_PATH + f.getName()));
        }
    }

    @Test
    public void testRemove() throws Exception {
        copyFiles();
        fs.remove("testFile1");

        assertEquals(
                "File was not removed",
                false,
                new File(TEST_PATH + "testFile1").exists()
        );
    }

    @Test
    public void testSave() throws Exception {
        fs.save(Files.readAllBytes(Paths.get(mockFiles[0].getPath())), "testFile1");
        assertEquals(
                "File was not saved",
                true,
                new File(TEST_PATH + "testFile1").exists()
        );
    }

    @Test
    public void testGetFile() throws Exception {
        copyFiles();
        File result = fs.getFile("testFile1");
        assertEquals(
                "Files does not match",
                true,
                org.apache.commons.io.FileUtils.contentEquals(result,mockFiles[0]));

    }
}