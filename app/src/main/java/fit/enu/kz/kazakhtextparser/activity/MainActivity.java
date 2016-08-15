package fit.enu.kz.kazakhtextparser.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import fit.enu.kz.kazakhtextparser.R;
import fit.enu.kz.kazakhtextparser.adapter.RuleAdapter;
import fit.enu.kz.kazakhtextparser.entity.Rule;
import fit.enu.kz.kazakhtextparser.helper.Constants;


public class MainActivity extends BaseActivity {

    Toolbar toolbar;
    Firebase firebaseRef;
    private List<Rule> rows;
    private RecyclerView mRecyclerView;
    private RuleAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    protected Handler handler;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);
        firebaseRef = new Firebase(Constants.FIREBASE_URL+"lang_rules");
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.drawer_item_rule);
        setSupportActionBar(toolbar);
        initDrawer(toolbar);
        mRecyclerView = (RecyclerView) findViewById(R.id.listRule);
        progressBar = (ProgressBar)findViewById(R.id.progressbar_loading);
        progressBar.setVisibility(View.VISIBLE);
        rows = new ArrayList<>();
        handler = new Handler();
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        // use a linear layout manager
        mRecyclerView.setLayoutManager(mLayoutManager);
        loadRules();
    }


    private void loadRules(){
        firebaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                /*System.out.println(snapshot.getValue());
                System.out.println("There are " + snapshot.getChildrenCount() + " blog posts");*/
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    Rule post = postSnapshot.getValue(Rule.class);
                    post.setId(postSnapshot.getKey());
                    rows.add(post);
                }
                progressBar.setVisibility(View.GONE);
                mAdapter = new RuleAdapter(rows, MainActivity.this);
                mRecyclerView.setAdapter(mAdapter);
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this, firebaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }
}
