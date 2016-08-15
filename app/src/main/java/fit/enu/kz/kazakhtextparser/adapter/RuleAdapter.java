package fit.enu.kz.kazakhtextparser.adapter;
import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.MutableData;
import com.firebase.client.Transaction;
import com.firebase.client.ValueEventListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fit.enu.kz.kazakhtextparser.R;
import fit.enu.kz.kazakhtextparser.activity.DetailActivity;
import fit.enu.kz.kazakhtextparser.entity.Rule;
import fit.enu.kz.kazakhtextparser.helper.Constants;

/**
 * Created by pc_ww on 15.08.2016.
 */
public class RuleAdapter extends RecyclerView.Adapter {
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;

    private List<Rule> rowList;
    private Activity activity;
    private Firebase baseRef;

    public RuleAdapter(List<Rule> rows, Activity act) {
        rowList = rows;
        activity = act;
    }

    @Override
    public int getItemViewType(int position) {
        return rowList.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {
        RecyclerView.ViewHolder vh;
        if (viewType == VIEW_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.row_rule, parent, false);

            vh = new FirmViewHolder(v);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.progress_item, parent, false);

            vh = new ProgressViewHolder(v);
        }
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof FirmViewHolder) {

            Rule drug = rowList.get(position);
            ((FirmViewHolder) holder).name.setText(drug.getTitle());
            ((FirmViewHolder) holder).did.setText(drug.getId());
            ((FirmViewHolder) holder).ruleView.setText(drug.getView()+"");
        } else {
            ((ProgressViewHolder) holder).progressBar.setIndeterminate(true);
        }
    }

    @Override
    public int getItemCount() {
        return rowList.size();
    }

    public class FirmViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public TextView did;
        public TextView ruleView;

        public FirmViewHolder(View v) {
            super(v);
            name = (TextView) v.findViewById(R.id.rule_title);
            did = (TextView) v.findViewById(R.id.rule_id);
            ruleView = (TextView) v.findViewById(R.id.rule_view);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String ruleId = did.getText().toString();
                    //String viewCount = ruleView.getText().toString();
                    Log.d("Rule id = ", ruleId);
                    /**baseRef = new Firebase(Constants.FIREBASE_URL+"lang_rules/"+ruleId+"/view");
                    baseRef.runTransaction(new Transaction.Handler() {
                        @Override
                        public Transaction.Result doTransaction(MutableData currentData) {
                            if(currentData.getValue() == null) {
                                currentData.setValue(1);
                            } else {
                                currentData.setValue((Long) currentData.getValue() + 1);
                            }
                            return Transaction.success(currentData); //we can also abort by calling Transaction.abort()
                        }
                        @Override
                        public void onComplete(FirebaseError firebaseError, boolean committed, DataSnapshot currentData) {
                            //This method will be called once with the results of the transaction.
                        }
                    });*/
                    //Map<String, Object> viewRule = new HashMap<String, Object>();
                    //viewRule.put("view", Integer.parseInt(ruleView.getText().toString())+1);
                    //int viewCount = Integer.parseInt(ruleView.getText().toString())+1;
                    //baseRef.updateChildren(viewRule);
                    /*baseRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Log.d(ruleId, dataSnapshot.getValue().toString());
                            int viewCount = Integer.parseInt(dataSnapshot.getValue().toString())+1;
                            baseRef.setValue(viewCount);
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {
                            Log.d("firebase error", firebaseError.getMessage());
                        }
                    });*/
                    Intent intent = new Intent(activity, DetailActivity.class);
                    intent.putExtra("rule_id", ruleId);
                    intent.putExtra("rule_title", name.getText().toString());
                    activity.startActivity(intent);
                }
            });
        }
    }

    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public ProgressViewHolder(View v) {
            super(v);
            progressBar = (ProgressBar) v.findViewById(R.id.progressBar1);
        }
    }
}
