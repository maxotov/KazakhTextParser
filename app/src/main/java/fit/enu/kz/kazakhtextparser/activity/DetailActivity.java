package fit.enu.kz.kazakhtextparser.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import fit.enu.kz.kazakhtextparser.R;
import fit.enu.kz.kazakhtextparser.entity.Rule;
import fit.enu.kz.kazakhtextparser.helper.Constants;

public class DetailActivity extends BaseActivity {

    Toolbar toolbar;
    TextView textView;
    Firebase firebaseRef;
    String ruleIdStr;
    String ruleTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);
        setContentView(R.layout.activity_detail);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        textView = (TextView)findViewById(R.id.rule_content);
        Intent intent = getIntent();
        ruleIdStr = intent.getStringExtra("rule_id");
        ruleTitle = intent.getStringExtra("rule_title");
        Log.d("title", ruleTitle);
        toolbar.setTitle(ruleTitle);
        setSupportActionBar(toolbar);
        initToolbar(toolbar);
        makeNavigationBackButton();
        firebaseRef = new Firebase(Constants.FIREBASE_URL+"lang_rules/"+ruleIdStr);
        loadRule();
    }

    private void loadRule(){
        firebaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                // do some stuff once
                Rule rule = snapshot.getValue(Rule.class);
                textView.setText(Html.fromHtml(rule.getContent()));
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Toast.makeText(DetailActivity.this, firebaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
