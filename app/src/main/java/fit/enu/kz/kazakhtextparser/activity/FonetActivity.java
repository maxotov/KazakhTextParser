package fit.enu.kz.kazakhtextparser.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import fit.enu.kz.kazakhtextparser.R;
import fit.enu.kz.kazakhtextparser.entity.Letters;
import fit.enu.kz.kazakhtextparser.entity.Word;
import fit.enu.kz.kazakhtextparser.helper.ConnectionDetector;
import fit.enu.kz.kazakhtextparser.helper.Constants;
import fit.enu.kz.kazakhtextparser.helper.Hyphenator;
import fit.enu.kz.kazakhtextparser.helper.IconButton;

public class FonetActivity extends BaseActivity {

    private Toolbar toolbar;
    private AdView fonetAdView;
    private FloatingActionButton fonetButton;
    private IconButton resultShareBtn;
    private EditText fonetContent;
    private LinearLayout records;
    private LinearLayout admobLayout;
    ConnectionDetector cd;
    Boolean isInternetPresent = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fonet);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.fonet_greeting);
        setSupportActionBar(toolbar);
        initToolbar(toolbar);
        makeNavigationBackButton();
        admobLayout = (LinearLayout)findViewById(R.id.fonetAdmobLayout);
        fonetAdView = new AdView(this);
        fonetAdView.setAdUnitId(getResources().getString(R.string.banner_ad_unit_id));
        fonetAdView.setAdSize(AdSize.BANNER);
        fonetButton = (FloatingActionButton) findViewById(R.id.fonetButton);
        fonetContent = (EditText) findViewById(R.id.fonetContent);
        records = (LinearLayout) findViewById(R.id.linearRecords);
        resultShareBtn = new IconButton(this);
        resultShareBtn.setText("нәтижемен бөлісу");
        resultShareBtn.setBackgroundColor(getResources().getColor(R.color.seawave));
        resultShareBtn.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.share), null, null, null);
        resultShareBtn.setTextColor(getResources().getColor(R.color.white));
        resultShareBtn.setHeight(40);
        resultShareBtn.setIconPadding(15);
        resultShareBtn.setPadding(0, 0, 0, 0);
        fonetButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(fonetContent.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                records.removeAllViews();
                List<Word> words = getWords(fonetContent.getText().toString());
                String OutputData = "";
                for (int i=0; i<words.size(); i++) {
                    OutputData += "Сөз:  " + words.get(i).getName() + "\n";
                    OutputData += words.get(i).getByun_sani() + " буынды\n";
                    OutputData += words.get(i).getType_byun() + "\n";
                    for (int j = 0; j < words.get(i).getLetters().size(); j++) {
                        OutputData += words.get(i).getLetters().get(j) + " - "
                                + words.get(i).getLetterDesc().get(j) + "\n";
                    }
                    OutputData += words.get(i).getCountLetterDesc() + "\n";
                    if(i+1 < words.size()){
                        OutputData += "\n";
                    }
                }
                if(!OutputData.equals("")) {
                    TextView textViewLocationItem = new TextView(FonetActivity.this);
                    textViewLocationItem.setPadding(0, 10, 0, 0);
                    textViewLocationItem.setText(OutputData);
                    textViewLocationItem.setTextSize(20);
                    textViewLocationItem.setTextColor(getResources().getColor(R.color.black));
                    records.addView(textViewLocationItem);
                    records.addView(resultShareBtn);
                }
            }
        });

        resultShareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                TextView content = (TextView)records.getChildAt(0);
                String contentString = content.getText().toString();
                Intent share = new Intent(android.content.Intent.ACTION_SEND);
                share.setType("text/plain");
                share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                // Add data to the intent, the receiving app will decide
                // what to do with it.
                share.putExtra(Intent.EXTRA_SUBJECT, "Фонетикалық талдау нәтижесі");
                share.putExtra(Intent.EXTRA_TEXT, contentString);
                startActivity(Intent.createChooser(share, "Нәтижемен бөлісу"));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        cd = new ConnectionDetector(getApplicationContext());
        isInternetPresent = cd.isConnectingToInternet();
        if(isInternetPresent){
            admobLayout.removeAllViews();
            admobLayout.addView(fonetAdView);
            fonetAdView.loadAd(new AdRequest.Builder().build());
            fonetAdView.setAdListener(new AdListener() {
                @Override
                public void onAdLoaded() {
                    Log.d("admobing", "Ad Received");
                }
                @Override
                public void onAdFailedToLoad(int errorcode) {
                    Log.d("admobing", "Ad Not Loaded Received");
                    admobLayout.removeAllViews();
                }
            });
        }
    }

    private List<Word> getWords(String text) {
        StringTokenizer tokenizer = new StringTokenizer(text, " (){}[]<>#*!?.,:;-\'\"/");
        List<Word> wr = new ArrayList<Word>();
        Letters let = new Letters();
        Constants c = new Constants();
        Hyphenator h = new Hyphenator();
        while (tokenizer.hasMoreTokens()) {
            Word w = new Word();
            String soz = tokenizer.nextToken();
            w.setName(soz);
            w.setType_byun(h.hyphenateWord(soz.toLowerCase()));
            w.setCountLetterDesc("Сөзде " + soz.length() + " әріп, " + soz.length() + " дыбыс бар");
            for (int i = 0; i < soz.length(); i++) {
                int arib_sani = soz.length();
                if (soz.charAt(i) == 'ю') {
                    soz = soz.replaceAll("ю", "йу");
                    int dybys_sani = soz.length();
                    w.setCountLetterDesc("Сөзде " + arib_sani + " әріп, " + dybys_sani + " дыбыс бар");
                } else if (soz.charAt(i) == 'я') {
                    soz = soz.replaceAll("я", "йа");
                    int dybys_sani = soz.length();
                    w.setCountLetterDesc("Сөзде " + arib_sani + " әріп, " + dybys_sani + " дыбыс бар");
                } else if (soz.charAt(i) == 'ё') {
                    soz = soz.replaceAll("ё", "йо");
                    int dybys_sani = soz.length();
                    w.setCountLetterDesc("Сөзде " + arib_sani + " әріп, " + dybys_sani + " дыбыс бар");
                } else if ((soz.charAt(i) == 'ь')||(soz.charAt(i) == 'ъ')){
                    int dybys_sani = soz.length()-1;
                    w.setCountLetterDesc("Сөзде " + arib_sani + " әріп, " + dybys_sani + " дыбыс бар");
                }

            }
            int count = 0;
            if(soz.length()==1){
                w.addLetters(soz.charAt(0));
                if(soz.charAt(0)=='у'){
                    w.addLetterDesc("дауысты, жуан, қысаң, еріндік");
                    count++;
                } else if(c.getDauysty().contains(soz.charAt(0))){
                    w.addLetterDesc(let.parseLetters(soz.toLowerCase().charAt(0)));
                    count++;
                }else {
                    w.addLetterDesc(let.parseLetters(soz.toLowerCase().charAt(0)));
                }
            } else {
                for (int i = 0; i < soz.length(); i++) {
                    if (c.getDauysty().contains(soz.charAt(i))) {
                        count++;
                    }
                    w.addLetters(soz.charAt(i));
                    if (i != soz.length() - 1) {
                        if ((soz.charAt(i) == 'у') && c.getDauyssyz().contains(soz.charAt(i + 1))) {
                            w.addLetterDesc("дауысты, жуан, қысаң, еріндік");
                            count++;
                        } else {
                            w.addLetterDesc(let.parseLetters(soz.toLowerCase().charAt(i)));

                        }
                    } else if (c.getDauyssyz().contains(soz.charAt(soz.length() - 2)) && (soz.charAt(soz.length() - 1) == 'у')) {
                        w.addLetterDesc("дауысты, жуан, қысаң, еріндік");
                        count++;
                    } else {
                        w.addLetterDesc(let.parseLetters(soz.toLowerCase().charAt(i)));
                    }
                }
            }
            w.setByun_sani(count);
            wr.add(w);
        }
        return wr;
    }
}
