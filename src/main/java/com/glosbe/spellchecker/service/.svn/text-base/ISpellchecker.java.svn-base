package com.glosbe.spellchecker.service;

import java.util.List;

import com.glosbe.spellchecker.model.RemoteSpellcheckerQuery;
import com.glosbe.spellchecker.model.SpellcheckResult;

public interface ISpellchecker {

    List<String> suggest(String word, String language);

    boolean misspelled(String word, String language);

    boolean supported(String language);

    List<String> getLanguagesSupported();

    SpellcheckResult spellcheck(RemoteSpellcheckerQuery query);

}
