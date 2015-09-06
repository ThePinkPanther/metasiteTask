package metasiteTask.utils;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

/**
 * mm, reusability is weak with this one
 *
 * @author ben
 * @version 1.0
 */
public class StreamWordWriterThread extends Thread {

    private final OutputStream outputStream;
    private final Map<String, Integer> wordsToWrite;

    public StreamWordWriterThread(
            final OutputStream outputStream,
            final Map<String, Integer> wordsToWrite) {
        this.outputStream = outputStream;
        this.wordsToWrite = wordsToWrite;
    }

    @Override
    public void run() {
        try {
            final StringBuilder sb = new StringBuilder();
            final Iterator<Entry<String, Integer>> wordIterator = wordsToWrite.entrySet().iterator();
            Entry<String, Integer> entry;
            while (wordIterator.hasNext()) {
                entry = wordIterator.next();
                sb.append(entry.getKey());
                sb.append(", ");
                sb.append(entry.getValue());
                sb.append("\n");
            }
            outputStream.write(sb.toString().getBytes());
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            synchronized (this) {
                notifyAll();
            }
        }
    }

}
