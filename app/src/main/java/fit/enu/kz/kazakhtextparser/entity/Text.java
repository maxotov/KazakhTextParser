package fit.enu.kz.kazakhtextparser.entity;

import java.util.ArrayList;

/**
 * Created by Aibol on 08.02.2015.
 */
public class Text {
    private String txt;

    public Text(String txt) {
        this.txt = txt;
    }

    public String getTxt() {
        return txt;
    }

    public void setTxt(String txt) {
        this.txt = txt;
    }

    public ArrayList<String> getWords()
    {
        ArrayList<String> words = null;
        if (!txt.trim().equals("")) {
            words = new ArrayList<String>();
            for (String str1 : txt.split("\\s+")) {
                //str1 = str1.replaceAll("(.)\\p{Punct}", "");
                str1 = str1.replaceAll("\\p{Punct}|\\d", "");
                str1 = str1.replaceAll("«", "").replaceAll("»", "").replaceAll("-", "").replaceAll("–", "");
                str1 = str1.replaceAll("TITLE", "").replaceAll("DATE", "").
                        replaceAll("AUTHOR", "").replaceAll("META", "").
                        replaceAll("TEXT", "").replaceAll("ITLE", "");
                str1 = str1.replaceAll("\uFEFF","");//ZERO WIDTH NO-BREAK SPACE
                str1 = str1.trim();
                if (!str1.equals("") && str1 != "" && !str1.isEmpty()) words.add(str1);
            }
        }
        return words;
    }
}
