package services;

import utils.Consumer;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

/**
 * @author ben
 * @version 1.0
 */
public class StreamWordReaderThread extends Thread {

    private final static String THREAD_NAME = "File Reader Thread %d";
    private static final int DEFAULT_BUFFER_SIZE = 1024;
    private static final Charset DEFAULT_CHARSET = Charset.defaultCharset();
    private volatile static long threadPrefix = 1;
    private final InputStream inputStream;
    private Consumer<String> wordConsumer;

    public StreamWordReaderThread(InputStream stream) {
        this(stream, DEFAULT_BUFFER_SIZE);
    }

    public StreamWordReaderThread(InputStream stream, int bufferSize) {
        setName(generateThreadName());
        inputStream = stream;
    }

    /**
     * Creates verbose and unique thread name
     *
     * @return thread name
     */
    private synchronized String generateThreadName() {
        return String.format(THREAD_NAME, ++threadPrefix);
    }

    public Consumer<String> getWordConsumer() {
        return wordConsumer;
    }

    public void setWordConsumer(Consumer<String> wordConsumer) {
        this.wordConsumer = wordConsumer;
    }

    @Override
    public void run() {
        try (WordProcessor processor = new WordProcessor(DEFAULT_CHARSET, wordConsumer)) {
            // Buffer to read the stream
            byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];

            // Number of bytes read from the stream
            int bytesRead;

            // Read the stream into buffer
            while ((bytesRead = inputStream.read(buffer)) > 0) {

                // Separate lines
                for (int i = 0; i < bytesRead; i++) {
                    processor.feed(buffer[i]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
