package hr.algebra.codenames.model.actions;

import java.io.Serializable;
import java.util.List;

public class ToSpymasterActionObject implements Serializable {
    private final List<String> guessedWords;

    public ToSpymasterActionObject(List<String> guessedWords){
        this.guessedWords = guessedWords;
    }

    public List<String> getGuessedWords() {
        return guessedWords;
    }
}
