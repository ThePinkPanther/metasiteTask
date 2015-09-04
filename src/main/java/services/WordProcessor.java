package services;

import utils.Consumer;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.nio.charset.Charset;

/**
 * @author ben
 * @version 1.0
 */
public class WordProcessor implements Closeable {

    private final Charset charset;
    private final ByteArrayOutputStream wordHolder = new ByteArrayOutputStream();
    private final Consumer<String> wordConsumer;

    public WordProcessor(Charset charset, Consumer<String> wordConsumer) {
        this.charset = charset;
        this.wordConsumer = wordConsumer;
    }

    public void feed(byte b) {
        // Split words with whitespaces
        if (isWhitespace(b) && wordHolder.size() != 0) {
            wordConsumer.consume(new String(wordHolder.toByteArray(), charset));
        } else {
            wordHolder.write(b);
        }
    }

    private boolean isWhitespace(byte b) {
        return b == ' ' || b == '\n' || b == '\t';
    }

    @Override
    public void close() throws IOException {
        wordHolder.close();
    }
}
