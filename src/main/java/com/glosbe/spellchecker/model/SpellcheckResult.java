package com.glosbe.spellchecker.model;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class SpellcheckResult {
    public static final SpellcheckResult EMPTY = new SpellcheckResult(Collections.<String, List<String>> emptyMap());

    /**
     * "ok" or error indicator
     */
    private String result;
    /**
     * map containing data: misspeled-word->list-of-suggestions ; list can be
     * null if there are no suggestions, but the word is misspelled; can be null
     * if there is an error (indicated by "result")
     */
    private Map<String, List<String>> suggestions;

    public SpellcheckResult(Map<String, List<String>> suggestions, String result) {
        super();
        this.result = result;
        this.suggestions = suggestions;
    }

    public SpellcheckResult(Map<String, List<String>> suggestions) {
        super();
        this.result = "ok";
        this.suggestions = suggestions;
    }

    public SpellcheckResult() {
        // TODO Auto-generated constructor stub
    }

    public String getResult() {
        return result;
    }

    public Map<String, List<String>> getSuggestions() {
        return suggestions;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public void setSuggestions(Map<String, List<String>> suggestions) {
        this.suggestions = suggestions;
    }

}
