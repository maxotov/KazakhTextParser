package fit.enu.kz.kazakhtextparser.tabs;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import fit.enu.kz.kazakhtextparser.R;
import fit.enu.kz.kazakhtextparser.core.DBService;
import fit.enu.kz.kazakhtextparser.entity.Newbase;
import fit.enu.kz.kazakhtextparser.entity.Text;
import fit.enu.kz.kazakhtextparser.helper.ConnectionDetector;
import fit.enu.kz.kazakhtextparser.helper.IconButton;

public class Morf extends Fragment {

    private FloatingActionButton buttonParse;
    private IconButton resultShareBtn;
    private EditText text;
    private LinearLayout records;
    private LinearLayout admobLayout;
    String root;
    private ProgressDialog pDialog;
    String outputMessage = "";
    ConnectionDetector cd;
    Boolean isInternetPresent = false;
    AdView adView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.morf_frag, container, false);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        buttonParse = (FloatingActionButton) view.findViewById(R.id.btnParse);
        admobLayout = (LinearLayout)view.findViewById(R.id.morfAdmobLayout);
        adView = new AdView(getActivity());
        adView.setAdUnitId(getResources().getString(R.string.banner_ad_unit_id));
        adView.setAdSize(AdSize.BANNER);
        text = (EditText) view.findViewById(R.id.content);
        resultShareBtn = new IconButton(getActivity());
        resultShareBtn.setText("нәтижемен бөлісу");
        resultShareBtn.setBackgroundColor(getResources().getColor(R.color.seawave));
        resultShareBtn.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.share), null, null, null);
        resultShareBtn.setTextColor(getResources().getColor(R.color.white));
        resultShareBtn.setHeight(40);
        resultShareBtn.setIconPadding(15);
        resultShareBtn.setPadding(0, 0, 0, 0);
        records = (LinearLayout) view.findViewById(R.id.linearLayoutRecords);
        cd = new ConnectionDetector(getActivity().getApplicationContext());
        buttonParse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                isInternetPresent = cd.isConnectingToInternet();
                // check for Internet status
                if (isInternetPresent) {
                    new ParseTask().execute();
                } else {
                    showAlertDialog(getActivity(), "Интернет қатынас жоқ",
                            "Интернетпен байланыс орнатыңыз.", false);
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
                            share.putExtra(Intent.EXTRA_SUBJECT, "Морфологиялық талдау нәтижесі");
                            share.putExtra(Intent.EXTRA_TEXT, contentString);

                            startActivity(Intent.createChooser(share, "Нәтижемен бөлісу"));
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
       isInternetPresent = cd.isConnectingToInternet();
        if(isInternetPresent){
            admobLayout.removeAllViews();
            admobLayout.addView(adView);
            adView.loadAd(new AdRequest.Builder().build());
            adView.setAdListener(new AdListener() {
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

    public void showAlertDialog(Context context, String title, String message, Boolean status) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setIcon((status) ? R.drawable.success : R.drawable.fail);
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        alertDialog.show();
    }

    private class ParseTask extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Күте тұрыңыз..");
            pDialog.setIndeterminate(true);
            pDialog.setCancelable(false);
            pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pDialog.show();
        }

        protected String doInBackground(String... unused) {
            root = text.getText().toString();
            System.out.println(root);
            if (!root.equals("")) {
                Text txt = new Text(root);
                ArrayList<String> words = txt.getWords();
                if (words!=null) {
                    if (words.size() > 0) {
                        List<Newbase> bases = null;
                        for (int k=0; k<words.size(); k++) {
                            bases = DBService.getNewbase(words.get(k));
                            if (bases != null && bases.size() > 0) {
                                String root = "";
                                for (Newbase newbase : bases) {
                                    int delimInd = newbase.getRule().indexOf("#");
                                    if (delimInd != -1) {
                                        root += newbase.getRule().substring(0, delimInd) + " не ";
                                    }
                                }
                                if (!root.equals(""))
                                    root = root.substring(0, root.length() - 4);
                                if (root.equals("")) root = bases.get(0).getRoot();
                                outputMessage += words.get(k) + " - түбірі: " + root + "\n";
                                int i = 1;
                                String rule = "";
                                for (Newbase newbase : bases) {
                                    int delimInd = newbase.getRule().indexOf("#");
                                    if (delimInd != -1) {
                                        rule = newbase.getRule().substring(delimInd + 1);
                                    }
                                    outputMessage += i + ". Талдауы: " + DBService.getAnalysis(rule) + "\n";
                                    i++;
                                }
                            } else {
                                outputMessage += words.get(k) + " - базада жоқ" + "\n";
                            }
                            if(k+1<words.size()){
                                outputMessage += "\n";
                            }
                        }
                    }
                }
            }
            return null;
        }

        protected void onPostExecute(String unused) {
            pDialog.dismiss();
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(text.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            records.removeAllViews();
            if(!outputMessage.equals("")) {
                TextView textViewLocationItem = new TextView(getActivity());
                textViewLocationItem.setPadding(0, 10, 0, 0);
                textViewLocationItem.setText(outputMessage);
                textViewLocationItem.setTextSize(20);
                textViewLocationItem.setTextColor(getResources().getColor(R.color.black));
                records.addView(textViewLocationItem);
                records.addView(resultShareBtn);
            }
            outputMessage = "";
        }
    }
}
