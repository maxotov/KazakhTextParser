package fit.enu.kz.kazakhtextparser.entity;

/**
 * Created by pc_ww on 15.08.2016.
 */
public class Rule {
    private String id;
    private String content;
    private String title;
    private int view;

    public Rule(){

    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getView() {
        return view;
    }

    public void setView(int view) {
        this.view = view;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
