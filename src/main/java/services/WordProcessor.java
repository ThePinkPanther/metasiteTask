package services;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.function.Consumer;

/**
 * @author ben
 * @version 1.0
 */
public class WordProcessor implements Closeable, Flushable {

    private final Charset charset;
    private final ByteArrayOutputStream wordHolder = new ByteArrayOutputStream();
    private final Consumer<String> wordConsumer;

    public WordProcessor(Charset charset, Consumer<String> wordConsumer) {
        this.charset = charset;
        this.wordConsumer = wordConsumer;
    }

    public void feed(byte b) {
        synchronized (this) {
            // Split words with whitespaces
            if (isWhitespace(b)) {
                nextWord();
            } else {
                wordHolder.write(b);
            }
        }
    }

    private boolean isWhitespace(byte b) {
        return b == ' ' || b == '\n' || b == '\t';
    }

    private void nextWord() {
        if (wordHolder.size() != 0) {
            wordConsumer.accept(new String(wordHolder.toByteArray(), charset));
            wordHolder.reset();
        }
    }

    @Override
    public void close() throws IOException {
        flush();
        wordHolder.close();
    }

    @Override
    public void flush() throws IOException {
        nextWord();
    }
}
