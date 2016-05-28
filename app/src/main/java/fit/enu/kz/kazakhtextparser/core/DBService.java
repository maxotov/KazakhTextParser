package fit.enu.kz.kazakhtextparser.core;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import fit.enu.kz.kazakhtextparser.entity.Auxiliaries;
import fit.enu.kz.kazakhtextparser.entity.Mainsuffix;
import fit.enu.kz.kazakhtextparser.entity.Newbase;
import fit.enu.kz.kazakhtextparser.entity.Subsuffix;
import fit.enu.kz.kazakhtextparser.entity.Suffix;

/**
 * Created by Aibol on 07.02.2015.
 */
public class DBService {
    private static final String url_newbase = "http://itdamu.kz/morf_backend/get_newbase.php";
    private static final String url_mainsuffix = "http://itdamu.kz/morf_backend/get_mainsuffix.php";
    private static final String url_suffix = "http://itdamu.kz/morf_backend/get_suffix.php";
    private static final String url_suffix_one = "http://itdamu.kz/morf_backend/get_suffix_one.php";
    private static final String url_subsuffix = "http://itdamu.kz/morf_backend/get_subsuffix.php";
    private static final String url_aux = "http://itdamu.kz/morf_backend/get_auxiliaries.php";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_ID = "id";
    private static final String TAG_ROOT = "root";
    private static final String TAG_RULE = "rule";
    private static final String TAG_NEWBASE = "newbases";

    public static List<Newbase> getNewbase(String searchWord) {
        List<Newbase> bases = new ArrayList<Newbase>();
        JSONParser jsonParser = new JSONParser();
        try {
            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("root", searchWord));

            // getting product details by making HTTP request
            // Note that product details url will use GET request
            JSONObject json = jsonParser.makeHttpRequest(
                    url_newbase, "GET", params);

            // check your log for json response
            Log.d("Single Product Details", json.toString());

            // json success tag
            int success = json.getInt(TAG_SUCCESS);
            if (success == 1) {
                JSONArray productObj = json.getJSONArray(TAG_NEWBASE); // JSON Array

                for(int i=0; i<productObj.length(); i++){
                    JSONObject product = productObj.getJSONObject(i);
                    Newbase newbase = new Newbase();
                    newbase.setId(product.getLong(TAG_ID));
                    newbase.setRoot(product.getString(TAG_ROOT));
                    newbase.setRule(product.getString(TAG_RULE));
                    bases.add(newbase);
                }

            }else{
                // product with pid not found
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return bases;
    }

    public static Mainsuffix getMainSuffix(String shortv){
        Mainsuffix mainsuffix = null;
        JSONParser jsonParser = new JSONParser();
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("shortv", shortv));
        JSONObject json = jsonParser.makeHttpRequest(
                url_mainsuffix, "GET", params);
        Log.d("Single Product Details", json.toString());

        try{
            int success = json.getInt(TAG_SUCCESS);
            if (success == 1) {
                JSONArray productObj = json.getJSONArray("mainsuffix"); // JSON Array

                JSONObject product = productObj.getJSONObject(0);

                mainsuffix = new Mainsuffix();
                mainsuffix.setId(product.getLong(TAG_ID));
                mainsuffix.setShortv(product.getString("shortv"));
                mainsuffix.setLongv(product.getString("longv"));

            }else{
                // product with pid not found
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return mainsuffix;
    }

    public static Suffix getSuffix(String shortv, long mid){
        Suffix suffix = null;
        JSONParser jsonParser = new JSONParser();
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("shortv", shortv));
        params.add(new BasicNameValuePair("mid", String.valueOf(mid)));
        JSONObject json = jsonParser.makeHttpRequest(
                url_suffix, "GET", params);
        Log.d("Single Product Details", json.toString());

        try{
            int success = json.getInt(TAG_SUCCESS);
            if (success == 1) {
                JSONArray productObj = json.getJSONArray("suffix"); // JSON Array

                JSONObject product = productObj.getJSONObject(0);

                suffix = new Suffix();
                suffix.setId(product.getLong(TAG_ID));
                suffix.setShortv(product.getString("shortv"));
                suffix.setLongv(product.getString("longv"));
                suffix.setMid(product.getLong("mid"));

            }else{
                // product with pid not found
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return suffix;
    }

    public static Suffix getSuffixOne(String shortv){
        Suffix suffix = null;
        JSONParser jsonParser = new JSONParser();
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("shortv", shortv));
        JSONObject json = jsonParser.makeHttpRequest(
                url_suffix_one, "GET", params);
        Log.d("Single Product Details", json.toString());

        try{
            int success = json.getInt(TAG_SUCCESS);
            if (success == 1) {
                JSONArray productObj = json.getJSONArray("suffix"); // JSON Array

                JSONObject product = productObj.getJSONObject(0);

                suffix = new Suffix();
                suffix.setId(product.getLong(TAG_ID));
                suffix.setShortv(product.getString("shortv"));
                suffix.setLongv(product.getString("longv"));
                suffix.setMid(product.getLong("mid"));

            }else{
                // product with pid not found
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return suffix;
    }

    public static Subsuffix getSubSuffix(String shortv){
        Subsuffix subsuffix = null;
        JSONParser jsonParser = new JSONParser();
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("shortv", shortv));
        JSONObject json = jsonParser.makeHttpRequest(
                url_subsuffix, "GET", params);
        Log.d("Single Product Details", json.toString());

        try{
            int success = json.getInt(TAG_SUCCESS);
            if (success == 1) {
                JSONArray productObj = json.getJSONArray("subsuffix"); // JSON Array

                JSONObject product = productObj.getJSONObject(0);

                subsuffix = new Subsuffix();
                subsuffix.setId(product.getLong(TAG_ID));
                subsuffix.setShortv(product.getString("shortv"));
                subsuffix.setLongv(product.getString("longv"));
                subsuffix.setSid(product.getLong("sid"));

            }else{
                // product with pid not found
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return subsuffix;
    }

    public static Auxiliaries getAuxiliaries(String shortv){
        Auxiliaries auxiliaries = null;
        JSONParser jsonParser = new JSONParser();
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("shortv", shortv));
        JSONObject json = jsonParser.makeHttpRequest(
                url_aux, "GET", params);
        Log.d("Single Product Details", json.toString());

        try{
            int success = json.getInt(TAG_SUCCESS);
            if (success == 1) {
                JSONArray productObj = json.getJSONArray("auxiliaries"); // JSON Array

                JSONObject product = productObj.getJSONObject(0);

               auxiliaries = new Auxiliaries();
                auxiliaries.setId(product.getLong(TAG_ID));
                auxiliaries.setShortv(product.getString("shortv"));
                auxiliaries.setLongv(product.getString("longv"));

            }else{
                // product with pid not found
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return auxiliaries;
    }

    public static String getAnalysis(String rule) {
        String retVal = "";
        String[] ruleParts = getRuleParts(rule);
        Mainsuffix mainSuffix = null;
        Suffix suffix = null;
        Subsuffix subsuffix = null;
        Auxiliaries auxiliaries = null;
        for (int i = 0; i < ruleParts.length; i++) {
            // Сөз табы
            if (i == 0) {
                mainSuffix = getMainSuffix(ruleParts[0]);
                if (mainSuffix != null)
                    retVal += mainSuffix.getLongv() + ", ";
            }
            // Сөз табы түрлері және т.б.
            if (i == 1) {
                if (mainSuffix != null) {
                    suffix = getSuffix(ruleParts[1],mainSuffix.getId());
                    if (suffix != null)
                        retVal += suffix.getLongv() + ", ";
                    else {
                        suffix = getSuffixOne(ruleParts[1]);
                        if (suffix != null)
                            retVal += suffix.getLongv() + ", ";
                    }
                }
            }
            // Т.б.
            if (i >= 2) {
                suffix = getSuffixOne(ruleParts[i]);
                if (suffix != null)
                    retVal += suffix.getLongv() + ", ";
                else {
                    subsuffix = getSubSuffix(ruleParts[i]);
                    if (subsuffix != null)
                        retVal += subsuffix.getLongv() + ", ";
                }
            }
            // Отыр, тұр, жүр, жатыр көмекші етістіктері
            if (i >= 1) {
                auxiliaries = getAuxiliaries(ruleParts[i]);
                if (auxiliaries != null)
                    retVal += auxiliaries.getLongv() + ", ";
            }
        }
        if (!retVal.equals(""))
            retVal = retVal.substring(0, retVal.length()-2);
        return retVal;
    }

    private static String[] getRuleParts(String rule) {
        String[] ruleParts = null;
        if (!rule.isEmpty()) {
            ruleParts = new String[rule.length()/2];
            for  (int i = 0; i < rule.length()/2; i++) {
                ruleParts[i] = rule.substring(i*2, i*2 + 2);
            }
        }
        return ruleParts;
    }

}
