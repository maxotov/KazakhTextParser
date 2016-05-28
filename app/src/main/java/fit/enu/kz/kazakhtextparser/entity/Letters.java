package fit.enu.kz.kazakhtextparser.entity;

/**
 * Created by Aibol on 08.02.2015.
 */
public class Letters {
    public static Character[] dauysty_juan = { 'а', 'о', 'ұ', 'ы', 'э' };
    public static Character[] dauysty_jinishke = { 'ә', 'і', 'ү', 'ө', 'е', 'и', };
    public static Character[] dauysty_awik = {'а','ә','е','о','ө','э'};
    public static Character[] dauysty_kisan = {'ы','и','і','ұ','ү'};
    public static Character[] dauysty_erindik = {'о','ө','ұ','ү'};
    public static Character[] dauysty_ezulik = {'а','ә','е','э','ы','і','и'};
    // Дауыссыз дыбыстар
    public static Character[] dauyssyz_uyan = { 'б', 'в', 'г', 'ғ', 'д', 'ж', 'з', 'һ' };
    public static Character[] dauyssyz_undi = { 'й', 'л', 'м', 'н', 'ң', 'р', 'у' };
    public static Character[] dauyssyz_katan = { 'к', 'қ', 'п', 'с', 'т', 'ф', 'х', 'ч', 'ц', 'ш', 'щ' };


    public String parseLetters(Character ch){
        String letterDesc = "";
        for(int i=0; i<dauysty_juan.length; i++){
            if(ch.equals(dauysty_juan[i])) letterDesc += "дауысты, жуан";
        }
        for(int i=0; i<dauysty_jinishke.length; i++){
            if(ch.equals(dauysty_jinishke[i])) letterDesc += "дауысты, жіңішке";
        }
        for(int i=0; i<dauysty_awik.length; i++){
            if(ch.equals(dauysty_awik[i])) letterDesc += ", ашық";
        }
        for(int i=0; i<dauysty_kisan.length; i++){
            if(ch.equals(dauysty_kisan[i])) letterDesc += ", қысаң";
        }
        for(int i=0; i<dauysty_erindik.length; i++){
            if(ch.equals(dauysty_erindik[i])) letterDesc += ", еріндік";
        }
        for(int i=0; i<dauysty_ezulik.length; i++){
            if(ch.equals(dauysty_ezulik[i])) letterDesc += ", езулік";
        }
        for(int i=0; i<dauyssyz_katan.length; i++){
            if(ch.equals(dauyssyz_katan[i])) letterDesc += "дауыссыз, қатаң";
        }
        for(int i=0; i<dauyssyz_undi.length; i++){
            if(ch.equals(dauyssyz_undi[i])) letterDesc += "дауыссыз, үнді";
        }
        for(int i=0; i<dauyssyz_uyan.length; i++){
            if(ch.equals(dauyssyz_uyan[i])) letterDesc += "дауыссыз, ұяң";
        }
        return letterDesc;

    }
}
