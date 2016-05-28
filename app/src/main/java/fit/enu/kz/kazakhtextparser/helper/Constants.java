package fit.enu.kz.kazakhtextparser.helper;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Aibol on 08.02.2015.
 */
public class Constants {

    public static final Character[] dauyssyzdar = {'б','в','г','ғ','д','ж','з','к','қ','п','с','т','ф','х','ц','ч','ш','щ','һ','й','р','л','м','н','ң'};
    public static final ArrayList<Character> DAUYSSYZ = new ArrayList<Character>();
    public static final Character[] dauyztylar = {'а', 'о', 'ұ', 'ы', 'э','ә', 'і', 'ү', 'ө', 'е', 'и'};
    public static final ArrayList<Character> DAUYSTY = new ArrayList<Character>();

    public ArrayList<Character> getDauyssyz(){
        DAUYSSYZ.addAll(Arrays.asList(dauyssyzdar));
        return DAUYSSYZ;
    }

    public ArrayList<Character> getDauysty(){
        DAUYSTY.addAll(Arrays.asList(dauyztylar));
        return DAUYSTY;
    }

}
