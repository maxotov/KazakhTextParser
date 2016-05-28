package fit.enu.kz.kazakhtextparser.helper;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Created by Aibol on 08.02.2015.
 */
public class Hyphenator {

    private String x = "ьъ";
    private String g = "аеиоуыэюяүұәіө";
    private String s = "бвгджзйклмнпрстфхцчушщңғқһ";
    private Vector rules = new Vector();

    public Hyphenator() {
        rules.addElement(new HyphenPair("xgg", 1));
        rules.addElement(new HyphenPair("xgs", 1));
        rules.addElement(new HyphenPair("xsg", 1));
        rules.addElement(new HyphenPair("xss", 1));
        rules.addElement(new HyphenPair("gssssg", 3));
        rules.addElement(new HyphenPair("gsssg", 3));
        rules.addElement(new HyphenPair("gsssg", 2));
        rules.addElement(new HyphenPair("sgsg", 2));
        rules.addElement(new HyphenPair("gssg", 2));
        rules.addElement(new HyphenPair("sggg", 2));
        rules.addElement(new HyphenPair("sggs", 2));
        rules.addElement(new HyphenPair("gsgs", 1));

    }

    public Vector hyphenateWord(String text) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (x.indexOf(c) != -1) {
                sb.append("x");
            } else if (g.indexOf(c) != -1) {
                sb.append("g");
            } else if (s.indexOf(c) != -1) {
                sb.append("s");
            } else {
                sb.append(c);
            }
        }
        String hyphenatedText = sb.toString();
        for (int i = 0; i < rules.size(); i++) {
            HyphenPair hp = (HyphenPair) rules.elementAt(i);
            int index = hyphenatedText.indexOf(hp.pattern);
            while (index != -1) {
                int actualIndex = index + hp.position;
                hyphenatedText = hyphenatedText.substring(0, actualIndex) + "-" + hyphenatedText.substring(actualIndex);
                text = text.substring(0, actualIndex) + "-" + text.substring(actualIndex);
                index = hyphenatedText.indexOf(hp.pattern);
            }
        }
        String[] parts = split(text, "-");
        Vector result = new Vector(parts.length);
        Constants c = new Constants();
        for (int i = 0; i < parts.length; i++) {
            String value = parts[i];
            if (value.contains("у")) {
                List<String> resp = handleSpecial(value);
                for (String s : resp) {
                    result.add(s);
                }
            } else {

                if (c.getDauysty().contains(value.charAt(0)) && (value.length() == 1)) {
                    result.addElement(value + " - ашық");
                } else if ((value.length() == 2) && c.getDauyssyz().contains(value.charAt(0)) && c.getDauysty().contains(value.charAt(1))) {
                    result.addElement(value + " - ашық");
                } else if ((value.length() == 3) && c.getDauysty().contains(value.charAt(0)) && c.getDauyssyz().contains(value.charAt(1)) && c.getDauysty().contains(value.charAt(2))) {
                    result.addElement(value.substring(0, 1) + " - ашық");
                    result.addElement(value.substring(1, 3) + " - ашық");
                } else if ((value.length() == 2) && c.getDauysty().contains(value.charAt(0)) && c.getDauyssyz().contains(value.charAt(1))) {
                    result.addElement(value + " - тұйық");
                } else if ((value.length() == 3) && c.getDauyssyz().contains(value.charAt(0)) && c.getDauysty().contains(value.charAt(1)) && c.getDauyssyz().contains(value.charAt(2))) {
                    result.addElement(value + " - бітеу");
                } else if ((value.length() == 3) && c.getDauysty().contains(value.charAt(0)) && c.getDauyssyz().contains(value.charAt(1)) && c.getDauyssyz().contains(value.charAt(2))) {
                    result.addElement(value + " - тұйық");
                } else if ((value.length() == 4) && c.getDauyssyz().contains(value.charAt(0)) && c.getDauysty().contains(value.charAt(1)) && c.getDauyssyz().contains(value.charAt(2)) && c.getDauyssyz().contains(value.charAt(3))) {
                    result.addElement(value + " - бітеу");
                } else {
                    result.addElement(value);
                }
            }
        }
        return result;
    }

    private String[] split(String original, String separator) {
        original = original.trim();
        Vector nodes = new Vector();
        int index = original.indexOf(separator);
        int i = 0;
        while (index >= 0) {
            nodes.addElement(original.substring(0, index));
            original = original.substring(index + separator.length());
            index = original.indexOf(separator);
            i++;
        }
        nodes.addElement(original);
        String[] result = new String[nodes.size()];
        if (nodes.size() > 0) {
            for (int loop = 0; loop < nodes.size(); loop++) {
                result[loop] = (String) nodes.elementAt(loop);
            }

        }
        return result;
    }

    private List<String> handleSpecial(String input) {
        int len = input.length();
        List<String> parts = new ArrayList<String>();
        Constants c = new Constants();
        if((len==1) && input.charAt(0)=='у'){
            parts.add(input + " - ашық");
        } else if ((len == 2) && (c.getDauyssyz().contains(input.charAt(0))) && (input.charAt(1) == 'у')) {
            parts.add(input + " - ашық"); //мысалы: су
        } else if ((len == 3) && (c.getDauyssyz().contains(input.charAt(0))) && (c.getDauysty().contains(input.charAt(1))) && (input.charAt(2) == 'у')) {
            parts.add(input + " - бітеу"); //тау
        } else if ((len == 3) && (input.charAt(0) == 'у') && (c.getDauysty().contains(input.charAt(1))) && (c.getDauyssyz().contains(input.charAt(2)))) {
            parts.add(input + " - бітеу"); //уақ
        } else {
            parts.add(input);
        }
        return parts;
    }

    class HyphenPair {

        public String pattern;
        public int position;

        public HyphenPair(String pattern, int position) {
            this.pattern = pattern;
            this.position = position;
        }
    }
}
