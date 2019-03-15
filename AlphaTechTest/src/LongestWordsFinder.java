import java.io.File;
import java.util.*;

public class LongestWordsFinder {

    private static final String EMPTY_STRING = "";
    private static final String WORD_SEPERATOR_REGEX = "\\s+|\\.|\\,| |\\?|;|:|\\!|\\'|\"|\\(|\\)";
    private SortedSet<String> uniqueWordsSet;
    private Map<String, Long> wordToNumberOfOcurrencesMap;

    private String fileName;

    public LongestWordsFinder(String fileName) {
        this.fileName = fileName;
        uniqueWordsSet = new TreeSet<>(new WordSizeCompator());
        wordToNumberOfOcurrencesMap = new HashMap<>();
    }

    public void findAndDisplayTheLongestWords(int numberOfWords) throws Exception {
        processWordsInFile();
        displayLongestWords(numberOfWords);
    }

    private void processWordsInFile() throws Exception {
        ClassLoader classLoader = this.getClass().getClassLoader();
        File file = new File(classLoader.getResource(fileName).getFile());
        Scanner scanner = new Scanner(file);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (EMPTY_STRING.equals(line)) {
                continue;
            }

            String[] words = line.split(WORD_SEPERATOR_REGEX);
            processFoundWords(words);
        }
        scanner.close();

        uniqueWordsSet.addAll(wordToNumberOfOcurrencesMap.keySet());
    }

    private void displayLongestWords(int numberOfWords) {
        Iterator<String> iterator = uniqueWordsSet.iterator();
        int i = 0;

        String introText = "The " + numberOfWords + " longest words in file " + fileName + " are the following...";
        System.out.println(introText);

        while (iterator.hasNext() && i < numberOfWords) {
            String word = iterator.next();
            long numberOfOccurrences = wordToNumberOfOcurrencesMap.get(word);
            String wordInfo = word + " has occured " + numberOfOccurrences + " times.";
            System.out.println(wordInfo);
            i++;
        }
    }

    private void processFoundWords(String[] words) {
        if (words == null || words.length == 0) {
            return;
        }

        for (String word : words) {
            if(word == null || EMPTY_STRING.equals(word)){
                continue;
            }

            if (wordToNumberOfOcurrencesMap.containsKey(word)) {
                long numberOfOccurences = wordToNumberOfOcurrencesMap.get(word);
                numberOfOccurences++;
                wordToNumberOfOcurrencesMap.put(word, numberOfOccurences);
            } else {
                wordToNumberOfOcurrencesMap.put(word, 1l);
            }
        }
    }

    public static void main(String[] args) {
        String filename = "LordOfTheRings.txt";
        LongestWordsFinder longestWordsFinder = new LongestWordsFinder(filename);

        try {
            longestWordsFinder.findAndDisplayTheLongestWords(10);
        } catch (Exception e) {
            System.err.println("Unable to get longest words from file: " + e);
        }
    }
}

class WordSizeCompator implements Comparator<String> {
    public int compare(String firstWord, String secondWord) {
        if(firstWord.length() > secondWord.length()){
            return -1;
        }

        if(firstWord.length() == secondWord.length()){
            return 0;
        }

        return 1;
    }
}