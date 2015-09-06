package metasiteTask.services;

import metasiteTask.utils.WordCounterFilter;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.TestCase.fail;

public class WordFilterServiceTest {

    private static final String PATTERN = "dear";
    private static final Integer WORD_COUNT = 5;
    private static final String MOCK_PATH = "mock/";
    private static final String TEST_PATH = "tmp/test/";

    private FileService mockFiles = new FileService(MOCK_PATH);

    private File testDir = new File(TEST_PATH);
    private FileService testFiles = new FileService(TEST_PATH);

    WordFilterService filterService =
            new WordFilterService(
                    mockFiles,
                    testFiles,
                    Arrays.asList(
                            new WordCounterFilter(PATTERN)
                    ));

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
    public void testStart() throws Exception {
        filterService.start();
        final File f;
        try {
            f = testFiles.getFile(WordCounterFilter.clean(PATTERN));
        } catch (FileNotFoundException e) {
            fail("Failed to generate file");
            return;
        }

        List<String> lines = Files.readAllLines(f.toPath(), Charset.defaultCharset());
        assertEquals(1,lines.size());
        String line = lines.get(0);
        String[] tmp = line.split(", ");
        assertEquals(2,tmp.length);
        String word = tmp[0];
        assertEquals(true,word.matches(PATTERN));
        Integer count =  Integer.valueOf(tmp[1]);
        assertEquals(WORD_COUNT, count);

    }

}