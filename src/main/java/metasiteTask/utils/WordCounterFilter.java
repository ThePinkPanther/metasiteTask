package metasiteTask.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ben
 * @version 1.0
 */
public class WordCounterFilter implements QueueConsumer<String> {

    private final String pattern;
    private final Map<String, Integer> words = new HashMap<>();

    public String getPattern() {
        return pattern;
    }

    public Map<String, Integer> getWords() {
        return words;
    }

    public WordCounterFilter(String pattern) {
        this.pattern = pattern;
    }

    public void clear() {
        words.clear();
    }

    @Override
    public boolean consume(String word) {
        if (word.matches(pattern)) {
            add(word);
            return true;
        }
        return false;
    }

    private void add(String word) {
        if (words.containsKey(word)) {
            Integer counter = words.get(word);
            words.put(word, counter + 1);
        } else {
            words.put(word, 1);
        }
    }

    public static String clean(String string) {
        return string.replaceAll("[\\.\\ ,!@#$%^&*()\\-_+=|\\\\/\"?\\t\\n\\[\\]{}0-9]","")
                .toLowerCase();
    }

}
