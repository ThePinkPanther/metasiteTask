package metasiteTask.services;

import metasiteTask.exceptions.SynchronousProcedureAlreadyRunningException;
import metasiteTask.utils.StreamWordReaderThread;
import metasiteTask.utils.StreamWordWriterThread;
import metasiteTask.utils.WordCounterFilter;

import java.io.*;
import java.util.Collection;
import java.util.LinkedList;
import java.util.function.Consumer;

/**
 * @author ben
 * @version 1.0.1
 */
public class WordFilterService {

    private final FileService fileReader;
    private final FileService fileWriter;
    Collection<WordCounterFilter> filters;
    private volatile boolean isRunning = false;

    public WordFilterService(FileService fileReader, FileService fileWriter, Collection<WordCounterFilter> filters) {
        this.fileReader = fileReader;
        this.fileWriter = fileWriter;
        this.filters = filters;
    }

    /**
     * Sends read words to word filters
     */
    private Consumer<String> wordConsumer = s -> {
        String word = WordCounterFilter.clean(s);
        for (WordCounterFilter filter : filters) {
            if (filter.consume(word)) break;
        }
    };

    /**
     * Checks if word filter procedure is running
     * @return boolean
     */
    public boolean isRunning() {
        return isRunning;
    }

    /**
     * Starts the procedure, that reads words from uploaded files, filters them and writes to new
     * files
     *
     * @throws SynchronousProcedureAlreadyRunningException
     */
    public void start() throws SynchronousProcedureAlreadyRunningException {
        if (isRunning)
            throw new SynchronousProcedureAlreadyRunningException();
        synchronized (this) {
            try {
                isRunning = true;
                readFiles();
                writeFiles();
                clear();
            } finally {
                isRunning = false;
            }
        }
    }

    private void clear() {
        for (WordCounterFilter filter :filters) {
            filter.clear();
        }
    }

    /**
     * A procedure that reads all files on separate threads and waits for them to finish
     */
    private void readFiles() {
        File[] files = fileReader.listFiles();
        Collection<Thread> threads = new LinkedList<>();
        // Read each file on separate thread
        for (File file : files) {
            try {
                Thread newThread = new StreamWordReaderThread(
                        new FileInputStream(file),
                        wordConsumer
                );
                threads.add(newThread);
                newThread.start();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        // Wait for all threads to finish
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * A procedure that writes filtered words to files on separate threads and waits for them to
     * finish
     */
    private void writeFiles() {
        final Collection<Thread> writerThreads = new LinkedList<>();
        // Write filtered words to files on separate threads
        for (WordCounterFilter filter : filters) {
            File file = null;
            try {
                file = fileWriter.getFileHandle(WordCounterFilter.clean(filter.getPattern()));
                Thread thread = new StreamWordWriterThread(
                        new FileOutputStream(file),
                        filter.getWords());
                writerThreads.add(thread);
                thread.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // Wait for all threads to finish
        for (Thread thread : writerThreads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
