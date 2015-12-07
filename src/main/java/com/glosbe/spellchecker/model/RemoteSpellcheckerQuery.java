package com.glosbe.spellchecker.model;

import java.util.List;

public class RemoteSpellcheckerQuery {
    private List<String> words;
    private String languageCode;

    public RemoteSpellcheckerQuery(String languageCode, List<String> words) {
        super();
        this.words = words;
        this.languageCode = languageCode;
    }

    public RemoteSpellcheckerQuery() {
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public List<String> getWords() {
        return words;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    public void setWords(List<String> words) {
        this.words = words;
    }

}
