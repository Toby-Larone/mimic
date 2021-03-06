package uk.co.markg.mimic.markov;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import com.esotericsoftware.kryo.io.Output;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Trigram implements Markov {

  private static final Logger logger = LogManager.getLogger(Trigram.class);
  private static final String FILE_END = ".trigram";
  private Map<String, WeightedCollection<String>> wordMap;
  private Set<String> startWords;
  private Set<String> endWords;
  private long lastMessageId;

  public Trigram() {
    wordMap = new HashMap<>();
    startWords = new HashSet<>();
    endWords = new HashSet<>();
  }

  @Override
  public void setLastMessageId(long lastMessageId) {
    this.lastMessageId = lastMessageId;
  }

  @Override
  public String generate(String start) {
    String word = getStartWord(start);
    List<String> sentence = new ArrayList<>();
    sentence.add(word);
    boolean endWordHit = false;
    var currentPair = wordMap.get(word);
    logger.debug(word);
    String[] pair = currentPair.getRandom().getElement().split(" ");
    if (pair[0].equals(END_WORD)) {
      endWordHit = true;
    } else {
      sentence.add(pair[0]);
    }
    while (!endWordHit) {
      if (sentence.size() > 300) {
        break;
      }
      String secondWord = pair[0];
      var secondWordCollection = wordMap.get(secondWord);
      String thirdWord = pair[1];

      if (thirdWord == secondWord && secondWord == word) {
        break;
      }

      if (secondWord == null || secondWord.equals(END_WORD)) {
        break;
      }

      if (thirdWord == null || thirdWord.equals(END_WORD)) {
        break;
      }
      sentence.add(thirdWord);

      var secondPair = findPair(secondWordCollection, thirdWord);
      if (secondPair.isPresent()) {
        pair = secondPair.get().getElement().split(" ");
      } else {
        break;
      }
    }
    String s = String.join(" ", sentence);
    logger.debug("Generated: {}", s);
    if (s.matches("(.*[^.!?`+>\\-=_+:@~;'#\\[\\]{}\\(\\)\\/\\|\\\\]$)")) {
      s = s + SENTENCE_ENDS.getRandom().getElement();
    }
    if (!start.isEmpty() && !s.startsWith(start)) {
      s = start + " " + s;
    }
    return s;
  }

  private String getStartWord(String start) {
    if (!start.isEmpty() && wordMap.containsKey(start)) {
      return start;
    }
    int startNo = ThreadLocalRandom.current().nextInt(startWords.size());
    Iterator<String> itr = startWords.iterator();
    for (int i = 0; i < startNo; i++) {
      itr.next();
    }
    return itr.next();
  }

  private Optional<WeightedElement<String>> findPair(WeightedCollection<String> wc, String word) {
    var pairs = wc.getAll();
    for (WeightedElement<String> weightedElement : pairs) {
      if (weightedElement.getElement().split(" ")[0].equals(word)) {
        return Optional.of(weightedElement);
      }
    }
    return Optional.empty();
  }

  @Override
  public void parseInput(String input) {
    String[] tokens = input.split("\\s+\\v?");
    if (tokens.length < 3) {
      throw new IllegalArgumentException(
          "Input '" + input + "'is too short. Must be greater than 3 tokens.");
    }

    for (int i = 0; i < tokens.length; i++) {
      String word = tokens[i];
      if (word.isEmpty()) {
        continue;
      }

      if (i == 0) {
        startWords.add(word);
      } else if (i == tokens.length - 1 || isEndWord(word)) {
        endWords.add(word);
        insertOrUpdate(word, END_WORD);
        continue;
      }

      if (i == tokens.length - 1) {
        insertOrUpdate(word, END_WORD);
        break;
      }

      String second = tokens[i + 1];
      if (second.isEmpty()) {
        continue;
      }
      if (i == tokens.length - 2) {
        insertOrUpdate(word, second, END_WORD);
        continue;
      }
      String third = tokens[i + 2];
      insertOrUpdate(word, second, third);
    }
  }

  private void insertOrUpdate(String word, String followWord) {
    insertOrUpdate(word, followWord, followWord);
  }

  private void insertOrUpdate(String word, String second, String third) {
    WeightedCollection<String> followFrequency;
    if (wordMap.containsKey(word)) {
      followFrequency = wordMap.get(word);
    } else {
      followFrequency = new WeightedCollection<String>();
      wordMap.put(word, followFrequency);
    }
    String pair = String.join(" ", second, third);

    var element = followFrequency.get(pair);
    if (element.isPresent()) {
      followFrequency.update(element.get(), element.get().getWeight() + 1);
    } else {
      followFrequency.add(new WeightedElement<String>(pair, 1));
    }
  }

  @Override
  public void save(String file) throws IOException {
    var f = new File(file + FILE_END);
    logger.info("Saving file {}", f.getAbsolutePath());
    Output output = new Output(new FileOutputStream(new File(file + FILE_END)));
    kryo.writeObject(output, this);
    output.close();
  }

  @Override
  public String getFileEnd() {
    return FILE_END;
  }

  @Override
  public Long getLastMessageId() {
    return lastMessageId;
  }

}
