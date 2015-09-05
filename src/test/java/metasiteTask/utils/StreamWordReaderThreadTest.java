package metasiteTask.utils;

import metasiteTask.utils.StreamWordReaderThread;
import org.junit.BeforeClass;
import org.junit.Test;

import org.apache.commons.lang3.StringUtils;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

import static junit.framework.TestCase.assertEquals;

/**
 * @author ben
 * @version 1.0
 */
public class StreamWordReaderThreadTest {

    private static final List<String> resultWords = new LinkedList<>();
    private static final Consumer<String> consumer = resultWords::add;
    private static final String testString = "In girum imus nocte et consumimur igni";
    private static final List<String> initialWords = Arrays.asList(testString.split(" "));

    @BeforeClass
    public static void setUp() {
        try {
            InputStream stream = new ByteArrayInputStream(testString.getBytes());
            StreamWordReaderThread streamWordReaderThread = new StreamWordReaderThread(stream, consumer);
            streamWordReaderThread.start();
            synchronized (streamWordReaderThread) {
                streamWordReaderThread.wait();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test() {
        assertEquals(
                "Result does not match the initial data: " + StringUtils.join(resultWords, " "),
                initialWords.size(),
                resultWords.size());
        Iterator<String> initialIterator = initialWords.iterator();
        Iterator<String> resultIterator = resultWords.iterator();
        while (resultIterator.hasNext() && initialIterator.hasNext()) {
            assertEquals(
                    "Result does not match the initial data: " + StringUtils.join(resultWords, " "),
                    resultIterator.next(),
                    initialIterator.next());
        }
    }

}