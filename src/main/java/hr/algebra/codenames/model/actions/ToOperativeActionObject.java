package hr.algebra.codenames.model.actions;

import java.io.Serializable;

public class ToOperativeActionObject implements Serializable {
    //String clue, Integer givenWordCount
    private final String clue;
    private final Integer givenWordCount;
    public ToOperativeActionObject(String clue, Integer givenWordCount){
        this.clue = clue;
        this.givenWordCount = givenWordCount;
    }

    public String getClue() {
        return clue;
    }

    public Integer getGivenWordCount() {
        return givenWordCount;
    }
}
