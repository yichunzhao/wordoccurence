/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uniquewordintext;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import java.util.stream.Stream;

/**
 *
 * @author YNZ
 */
public class UniqueWordInText {

    final private static String pattern = "[;:!?.,\\s]+";

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {

        Instant start = Instant.now();

        Map<String, Long> top10 = wordOccurrence();

        Instant end = Instant.now();

        System.out.println("Top 10 word: " + top10.toString());
        System.out.println("Time cost  :" + Duration.between(start, end).toMillis());
    }

    public static Map<String, Long> wordOccurrence() throws IOException {

        String str = new String(Files.readAllBytes(Paths.get("tempest.txt")), StandardCharsets.UTF_8);

        List<String> wordsList = Stream.of(str.toLowerCase().split(pattern))
                .collect(toList());

        //all unique words
        Set<String> wordsSet = wordsList.stream().distinct()
                .collect(toSet());

        //counting each word
        Map<String, Long> wordNumber = new HashMap<>();
        wordsSet.forEach(word -> wordNumber.put(word, wordsList.stream()
                .filter(w -> w.equals(word)).count()));

        //all word occurrences
        Map<String, Long> result = wordNumber.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (o, n) -> o, LinkedHashMap::new));  //o: existing key value; n: new value. 

        //top 10 
        Map<String, Long> top10 = result.entrySet().stream().limit(10)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (o, n) -> o, LinkedHashMap::new));

        return top10;

    }

}