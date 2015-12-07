package com.glosbe.spellchecker.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.CharacterCodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.languagetool.rules.spelling.hunspell.Hunspell;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.glosbe.spellchecker.model.RemoteSpellcheckerQuery;
import com.glosbe.spellchecker.model.SpellcheckResult;
import com.google.common.base.CharMatcher;

/**
 * @author pstawinski
 * 
 */
@Component("languageToolsSpellchecker")
@Lazy
public class SpellcheckerJnaLanguageToolsBased implements ISpellchecker {
    private static final org.apache.log4j.Logger log = org.apache.log4j.Logger
            .getLogger(SpellcheckerJnaLanguageToolsBased.class);

    private final Map<String, Hunspell.Dictionary> map;
    private final boolean DEBUG_TO_FILE = false;
    private final FileWriter fileWriter;

    @Autowired
    public SpellcheckerJnaLanguageToolsBased(@Value("${spellchecker.hunspell.dir}") String hunspellDir) {

        Map<String, Hunspell.Dictionary> localMap = new HashMap<String, Hunspell.Dictionary>();

        File dir = new File(hunspellDir);
        if (dir.isDirectory()) {
            for (String dictionaryName : dir.list()) {
                try {
                    log.debug("Registering dictionary: " + dictionaryName);
                    Hunspell.Dictionary dict = Hunspell.getInstance()
                            .getDictionary(hunspellDir + "/" + dictionaryName + "/" + dictionaryName);
                    localMap.put(dictionaryName, dict);
                    if (dictionaryName.indexOf("_") != -1) {
                        // to have not only de_DE but also de; it can be
                        // overriden by just "de"
                        String lang = dictionaryName.substring(0, dictionaryName.indexOf("_"));
                        if (!localMap.containsKey(lang))
                            localMap.put(lang, dict);
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (UnsatisfiedLinkError e) {
                    e.printStackTrace();
                } catch (UnsupportedOperationException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            log.warn("There is no directory: " + hunspellDir + ", I wouldn't load any dictionaries");
        }

        map = Collections.unmodifiableMap(localMap);

        if (DEBUG_TO_FILE) {

            DateFormat df = new SimpleDateFormat("yyyy-MM-dd-HH-mm");
            FileWriter fileWriterTmp = null;
            String filename = "/tmp/hunspell-debug-" + df.format(new Date());
            log.debug("Every hunspell suggest query will be saved to debug file: " + filename);
            try {
                fileWriterTmp = new FileWriter(new File(filename));
            } catch (IOException e) {
                e.printStackTrace();
            }
            fileWriter = fileWriterTmp;
        } else {
            fileWriter = null;
        }
    }

    @Override
    public boolean supported(String language) {
        return map.containsKey(language);
    }

    /**
     * Check if is misspelled
     * 
     * @param word
     * @param language
     * @return
     */
    @Override
    public synchronized boolean misspelled(String word, String language) {
        if (highSurrogateCharMather.matchesAnyOf(word)) {
            log.debug("Ommited " + word + " when tried to check it with hunspell");
            return false;
        }

        Hunspell.Dictionary dictionary = map.get(language);
        return dictionary != null ? dictionary.misspelled(word) : false;
    }

    private final CharMatcher highSurrogateCharMather = CharMatcher.inRange(Character.MIN_HIGH_SURROGATE,
            Character.MAX_HIGH_SURROGATE);

    /**
     * Suggest other spellings
     * 
     * @param word
     * @param language
     * @return
     */
    @Override
    public synchronized List<String> suggest(String word, String language) {
        if (highSurrogateCharMather.matchesAnyOf(word)) {
            log.debug("Ommited " + word + " when tried to check it with hunspell");
            return Collections.emptyList();
        }

        Hunspell.Dictionary dictionary = map.get(language);
        try {
            if (DEBUG_TO_FILE) {
                try {
                    fileWriter.write("" + System.currentTimeMillis() + "\t" + language + "\t" + word + "\n");
                    fileWriter.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return dictionary.suggest(word);
        } catch (CharacterCodingException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    @Override
    public List<String> getLanguagesSupported() {
        return new ArrayList<>(map.keySet());
    }

    @Override
    public SpellcheckResult spellcheck(RemoteSpellcheckerQuery query) {
        Map<String, List<String>> map = new LinkedHashMap<String, List<String>>(query.getWords().size());

        String languageCode = query.getLanguageCode();
        for (String word : query.getWords()) {
            if (misspelled(word, languageCode)) {
                map.put(word, suggest(word, languageCode));
            }
        }

        return new SpellcheckResult(map);
    }

}
