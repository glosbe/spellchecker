package com.glosbe.spellchecker.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.glosbe.spellchecker.model.RemoteSpellcheckerQuery;
import com.glosbe.spellchecker.model.SpellcheckResult;
import com.glosbe.spellchecker.service.ISpellchecker;

@RestController
public class ApiController {
    @Autowired
    private ISpellchecker spellchecker;

    @RequestMapping(value = "/spellcheck", method = RequestMethod.POST)
    public SpellcheckResult spellcheck(@RequestBody RemoteSpellcheckerQuery query) {
        return spellchecker.spellcheck(query);
    }

    @RequestMapping(value = "/is-misspelled", method = RequestMethod.GET)
    public Boolean isMisspelled(@RequestParam String language, @RequestParam String word) {
        return spellchecker.misspelled(word, language);
    }

    @RequestMapping(value = "/suggest", method = RequestMethod.GET)
    public List<String> suggest(@RequestParam String language, @RequestParam String word) {
        return spellchecker.suggest(word, language);
    }

    @RequestMapping("/ping")
    public String ping() {
        return "pong";
    }

    @RequestMapping(value = "/languages", method = RequestMethod.GET)
    public List<String> languages() {
        return spellchecker.getLanguagesSupported();
    }
}
