package fit.enu.kz.kazakhtextparser.activity;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.accountswitcher.AccountHeader;
import com.mikepenz.materialdrawer.accountswitcher.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;

import java.util.ArrayList;
import java.util.List;

import fit.enu.kz.kazakhtextparser.R;

/**
 * Created by pc_ww on 12.08.2016.
 */
public class BaseActivity extends AppCompatActivity {
    public static final int SCREEN_MAIN = 0;
    public static final int SCREEN_FON_ANALYZE = 1;
    public static final int SCREEN_ABOUT = 2;
    public static final int SCREEN_RATE = 3;
    public static final int SCREEN_SHARE = 4;
    protected Drawer drawer;
    protected AccountHeader accountHeader;
    private Toolbar toolbar;

    protected Drawer initDrawer(Toolbar toolbar) {
        this.toolbar = toolbar;
        DrawerBuilder drawerBuilder = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withSliderBackgroundColorRes(android.R.color.white)
                .addDrawerItems(initDrawerItems())
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(AdapterView<?> adapterView, View view, int i, long l, IDrawerItem iDrawerItem) {
                        int identifier = iDrawerItem.getIdentifier();
                        Intent  intent;
                        switch (identifier) {
                            case SCREEN_MAIN:
                                intent = new Intent(BaseActivity.this, MainActivity.class);
                                startActivity(intent);
                                return true;
                            case SCREEN_FON_ANALYZE:
                                intent = new Intent(BaseActivity.this, FonetActivity.class);
                                startActivity(intent);
                                return true;
                            case SCREEN_ABOUT:
                                drawer.closeDrawer();
                                new AlertDialog.Builder(BaseActivity.this)
                                        .setTitle(R.string.drawer_item_about)
                                        .setMessage(R.string.about_text)
                                        .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                dialogInterface.dismiss();
                                            }
                                        })
                                        .show();
                                return true;
                            case SCREEN_RATE:
                                onClickRateThisApp();
                                return true;
                            case SCREEN_SHARE:
                                drawer.closeDrawer();
                                shareWithApp();
                                return true;
                            default:
                                return false;
                        }
                    }
                });
        accountHeader = createAccountHeader();
        drawerBuilder.withAccountHeader(accountHeader);
        drawer = drawerBuilder.build();
        return drawer;
    }

    protected void initToolbar(Toolbar toolbar){
        this.toolbar = toolbar;
    }

    private AccountHeader createAccountHeader() {
        IProfile profile = new ProfileDrawerItem()
                .withEmail("Kaz Grammar қосымшасы")
                .withIcon(getResources().getDrawable(R.drawable.ic_launcher));

        return new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(getResources().getDrawable(R.drawable.header2))
                .addProfiles(profile)
                .withProfileImagesVisible(true)
                .withProfileImagesClickable(false)
                .withSelectionListEnabled(false)
                .build();
    }

    private IDrawerItem[] initDrawerItems() {
        List<IDrawerItem> items = new ArrayList<>();
        items.add(new PrimaryDrawerItem()
                .withName(R.string.drawer_item_rule)
                .withIcon(R.drawable.item_news)
                .withSelectedColor(android.R.color.transparent)
                .withIdentifier(SCREEN_MAIN));
        items.add(new PrimaryDrawerItem()
                .withName(R.string.fonet_greeting)
                .withIcon(R.drawable.item_cases_lists)
                .withSelectedColor(android.R.color.transparent)
                .withIdentifier(SCREEN_FON_ANALYZE));
        items.add(new SectionDrawerItem().withName(R.string.drawer_section_item));
        items.add(new PrimaryDrawerItem()
                .withName(R.string.drawer_item_about)
                .withIcon(R.drawable.item_settings)
                .withSelectedColor(android.R.color.transparent)
                .withIdentifier(SCREEN_ABOUT));
        items.add(new PrimaryDrawerItem()
                .withName(R.string.drawer_item_rate)
                .withIcon(R.drawable.ic_comment_text)
                .withSelectedColor(android.R.color.transparent)
                .withIdentifier(SCREEN_RATE));
        items.add(new PrimaryDrawerItem()
                .withName(R.string.drawer_item_share)
                .withIcon(R.drawable.ic_share_variant)
                .withSelectedColor(android.R.color.transparent)
                .withIdentifier(SCREEN_SHARE));
        return items.toArray(new IDrawerItem[items.size()]);
    }

    protected void makeNavigationBackButton() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar == null) {
            return;
        }
        actionBar.setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent upIntent = NavUtils.getParentActivityIntent(BaseActivity.this);
                if (NavUtils.shouldUpRecreateTask(BaseActivity.this, upIntent)) {
                    TaskStackBuilder.create(BaseActivity.this)
                            .addNextIntentWithParentStack(upIntent)
                            .startActivities();
                } else {
                    NavUtils.navigateUpTo(BaseActivity.this, upIntent);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (drawer != null && drawer.isDrawerOpen()) {
            drawer.closeDrawer();
            return;
        }
        super.onBackPressed();
    }

    protected Drawer resetDrawer() {
        if (drawer == null) {
            return null;
        }
        drawer.removeAllItems();
        drawer.addItems(initDrawerItems());
        return drawer;
    }

    private boolean isActivityStarted(Intent aIntent) {
        try {
            startActivity(aIntent);
            return true;
        } catch (ActivityNotFoundException e) {
            return false;
        }
    }

    public void onClickRateThisApp() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("market://details?id=fit.enu.kz.kazakhtextparser"));
        if (!isActivityStarted(intent)) {
            intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=fit.enu.kz.kazakhtextparser"));
            if (!isActivityStarted(intent)) {
                Toast.makeText(this, "Could not open Android market, please check if the market app installed or not. Try again later",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void shareWithApp(){
        Intent share = new Intent(android.content.Intent.ACTION_SEND);
        share.setType("text/plain");
        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        share.putExtra(Intent.EXTRA_SUBJECT, R.string.app_name);
        String content = "Я пользуюсь мобильным приложением Kaz Grammar для изучения казахского языка - ";
        content += "https://play.google.com/store/apps/details?id=fit.enu.kz.kazakhtextparser";
        share.putExtra(Intent.EXTRA_TEXT, content);
        startActivity(Intent.createChooser(share, "Қосымшамен бөлісу"));
    }
}
