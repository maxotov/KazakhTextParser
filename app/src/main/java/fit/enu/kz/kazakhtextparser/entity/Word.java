package fit.enu.kz.kazakhtextparser.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Created by Aibol on 08.02.2015.
 */
public class Word {
    public Word(){
        this.letters = new ArrayList<Character>();
        this.letterDesc = new ArrayList<String>();

    }
    private String name;
    private List<Character> letters;
    private List<String> letterDesc;
    private String countLetterDesc;
    private int byun_sani;
    private Vector type_byun;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Character> getLetters() {
        return letters;
    }

    public void setLetters(List<Character> letters) {
        this.letters = letters;
    }

    public void addLetters(Character ch){
        this.letters.add(ch);
    }

    public List<String> getLetterDesc() {
        return letterDesc;
    }

    public void setLetterDesc(List<String> letterDesc) {
        this.letterDesc = letterDesc;
    }

    public void addLetterDesc(String st){
        this.letterDesc.add(st);
    }

    public String getCountLetterDesc() {
        return countLetterDesc;
    }

    public void setCountLetterDesc(String countLetterDesc) {
        this.countLetterDesc = countLetterDesc;
    }

    public int getByun_sani() {
        return byun_sani;
    }

    public void setByun_sani(int byun_sani) {
        this.byun_sani = byun_sani;
    }

    public Vector getType_byun() {
        return type_byun;
    }

    public void setType_byun(Vector type_byun) {
        this.type_byun = type_byun;
    }
}
