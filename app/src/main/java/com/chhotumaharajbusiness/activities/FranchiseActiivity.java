package com.chhotumaharajbusiness.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.chhotumaharajbusiness.R;
import com.chhotumaharajbusiness.constant.Constant;
import com.chhotumaharajbusiness.constant.MaintainRequestQueue;
import com.chhotumaharajbusiness.constant.SharedPrefrenceObj;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class FranchiseActiivity extends AppCompatActivity implements View.OnClickListener {

    EditText name, mobile, email, city;
    CheckBox check;
    String nameVal, mobileVal, emailVal, cityVal;
    Button submit;
    ProgressDialog progressDialog;
    //   Spinner language;
    ArrayList<String> languageArray;
    ArrayList<String> stateArray;
    String languageVal, stateVal;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    SearchableSpinner language, state;
    TextView terms_txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_franchise_actiivity);

        getSupportActionBar().hide();

        TextView tv = findViewById(R.id.chottu_txt);
        TextView tv1 = findViewById(R.id.maharaj_txt);
        Typeface face = Typeface.createFromAsset(getAssets(), "cooperblackstd.otf");

        tv.setTypeface(face);
        tv1.setTypeface(face);

        name = findViewById(R.id.franchise_name);
        email = findViewById(R.id.franchise_email);
        state = findViewById(R.id.franchise_state);
        city = findViewById(R.id.franchise_city);
        check = findViewById(R.id.reg_terms);
        language = findViewById(R.id.franchise_language);
        terms_txt = findViewById(R.id.terms_text);

        terms_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(FranchiseActiivity.this)
                        .setTitle("Terms & Condition")
                        .setMessage("I hereby request & Authorize KSS to contact me on my above registered phone number,Whatsapp & Email Id.\n" +
                                "\n" +
                                "I acknowledge & allow such official approach by KSS with TRAI rules & regulations rules."
                        )
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .show();
            }
        });

        languageVal = "";
        language.setTitle("");
        state.setTitle("");

        stateArray = new ArrayList<>();
        languageArray = new ArrayList<>();
        languageArray.add("Select Language");

        if (SharedPrefrenceObj.getIntegerval(FranchiseActiivity.this, "step") == 1) {
            Intent intent = new Intent(FranchiseActiivity.this, ConceptBusinessActivity1.class);
            startActivity(intent);
            finish();
        } else if (SharedPrefrenceObj.getIntegerval(FranchiseActiivity.this, "step") == 2) {
            Intent intent = new Intent(FranchiseActiivity.this, PreAnalysisActivity.class);
            startActivity(intent);
            finish();
        } else if (SharedPrefrenceObj.getIntegerval(FranchiseActiivity.this, "step") == 3) {
            Intent intent = new Intent(FranchiseActiivity.this, NavigationActivity.class);
            intent.putExtra("query_page", "0");
            startActivity(intent);
            finish();
        } else if (SharedPrefrenceObj.getIntegerval(FranchiseActiivity.this, "step") == 4) {
            Intent intent = new Intent(FranchiseActiivity.this, NavigationActivity.class);
            intent.putExtra("query_page", "0");
            startActivity(intent);
            finish();
        } else if (SharedPrefrenceObj.getIntegerval(FranchiseActiivity.this, "step") == 5) {
            Intent intent = new Intent(FranchiseActiivity.this, NavigationActivity.class);
            intent.putExtra("query_page", "0");
            startActivity(intent);
            finish();
        }


        submit = findViewById(R.id.franchise_submit);
        submit.setOnClickListener(this);

        progressDialog = new ProgressDialog(FranchiseActiivity.this);

        ArrayAdapter<String> lang = new ArrayAdapter<String>(FranchiseActiivity.this, R.layout.spinner_text_layout, languageArray);
        language.setAdapter(lang);
        lang.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        language.setAdapter(lang);

        language.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                languageVal = String.valueOf(adapterView.getSelectedItem());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        getLanguagerequest();

        getState();

        state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                stateVal = String.valueOf(adapterView.getSelectedItem());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(this, new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                String newToken = instanceIdResult.getToken();
                Log.e("newToken", newToken);
                SharedPrefrenceObj.setSharedValue(FranchiseActiivity.this, "token", newToken);

                saveFCMToken(SharedPrefrenceObj.getSharedValue(FranchiseActiivity.this, "mobile"), newToken);

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.franchise_submit:

                nameVal = name.getText().toString();
                emailVal = email.getText().toString().trim();
                cityVal = city.getText().toString();

                if (name.getText().toString().equalsIgnoreCase("")) {
                    name.setError("Invalid name");
                } else if (email.getText().toString().equalsIgnoreCase("")) {
                    email.setError("Invalid email");
                } else if (!isValidEmail(emailVal)) {
                    email.setError("Invalid email");
                } else if (state.getSelectedItem().toString().equalsIgnoreCase("Select State")) {
                    TextView errorText = (TextView) state.getSelectedView();
                    errorText.setError("");
                    errorText.setTextColor(Color.RED);//just to highlight that this is an error
                    errorText.setText("Select State");
                } else if (city.getText().toString().equalsIgnoreCase("")) {
                    city.setError("Invalid city");
                    //Toast.makeText(FranchiseActiivity.this,"Invalid city",Toast.LENGTH_SHORT).show();
                } else if (languageVal.equalsIgnoreCase("Select Language")) {
                    TextView errorText = (TextView) language.getSelectedView();
                    errorText.setError("");
                    errorText.setTextColor(Color.RED);//just to highlight that this is an error
                    errorText.setText("Select language");
                    // Toast.makeText(FranchiseActiivity.this,"Select language",Toast.LENGTH_SHORT).show();

                } else if (check.isChecked() == false) {
                    Toast.makeText(FranchiseActiivity.this, "Please accept terms and condition", Toast.LENGTH_SHORT).show();
                } else {
                    saveFranchiserequest(nameVal, emailVal, stateVal, cityVal, languageVal);
                }

                break;
        }
    }

    private void getLanguagerequest() {

        String url = Constant.URL + "language";
        url = url.replace(" ", "%20");
        url = url.replace("\n", "%0A");
        progressDialog.show();
        progressDialog.setMessage("loading data....");
        StringRequest req = new StringRequest(com.android.volley.Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Response", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.optBoolean("success")) {
                        //{"success":true,"error":"","data":{"languages":["Tamil","Hindi","English","Telegu"]}}
                        JSONArray languagesArray = jsonObject.optJSONObject("data").getJSONArray("languages");
                        for (int i = 0; i < languagesArray.length(); i++) {
                            languageArray.add(languagesArray.getString(i));
                        }

                        SharedPrefrenceObj.setSharedValue(FranchiseActiivity.this, "all_language", jsonObject.optJSONObject("data").toString());

                        progressDialog.dismiss();
                    } else {

                        progressDialog.dismiss();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", "Error: " + error.getMessage());
                progressDialog.dismiss();
            }

        }) {
            @Override
            public Request.Priority getPriority() {
                return Request.Priority.IMMEDIATE;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("authorization", "Bearer " + SharedPrefrenceObj.getSharedValue(FranchiseActiivity.this, "auth_token"));
                params.put("content-type", "application/x-www-form-urlencoded");
                Log.d("params..", params.toString());
                return params;
            }

            //
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                System.out.println("Param value..........." + params);
                return checkParams(params);
            }

            private Map<String, String> checkParams(Map<String, String> map) {
                Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry<String, String> pairs = (Map.Entry<String, String>) it.next();
                    if (pairs.getValue() == null) {
                        map.put(pairs.getKey(), "");
                    }
                }
                return map;
            }
        };
        // Adding request to request queue
        MaintainRequestQueue.getInstance(this).addToRequestQueue(req, "tag");
    }

    private void saveFranchiserequest(final String name, final String email, final String state,
                                      final String city, final String language) {

        String url = Constant.URL + "saveFranchiserequest";
        url = url.replace(" ", "%20");
        url = url.replace("\n", "%0A");
        Log.e("User Name", "" + url);
        progressDialog.show();
        progressDialog.setMessage("Saving data....");
        StringRequest req = new StringRequest(com.android.volley.Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Response", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.optBoolean("success")) {

                        SharedPrefrenceObj.setIntegerval(FranchiseActiivity.this, "step", 1);
                        SharedPrefrenceObj.setSharedValue(FranchiseActiivity.this, "language", language);
                        SharedPrefrenceObj.setSharedValue(FranchiseActiivity.this, "name", name);
                        SharedPrefrenceObj.setSharedValue(FranchiseActiivity.this, "email", email);
                        SharedPrefrenceObj.setSharedValue(FranchiseActiivity.this, "state", state);
                        SharedPrefrenceObj.setSharedValue(FranchiseActiivity.this, "city", city);

                        Intent intent = new Intent(FranchiseActiivity.this, ConceptBusinessActivity1.class);
                        intent.putExtra("sec", 0);
                        startActivity(intent);
                        finish();

                        progressDialog.dismiss();
                    } else {

                        progressDialog.dismiss();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", "Error: " + error.getMessage());
                progressDialog.dismiss();
            }

        }) {
            @Override
            public Request.Priority getPriority() {
                return Request.Priority.IMMEDIATE;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("authorization", "Bearer " + SharedPrefrenceObj.getSharedValue(FranchiseActiivity.this, "auth_token"));
                params.put("content-type", "application/x-www-form-urlencoded");
                Log.d("params..", params.toString());
                return params;
            }

            //
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("name", name);
                params.put("email", email);
                params.put("states", state);
                params.put("cities", city);
                params.put("prefered_language", language);
                System.out.println("Param value..........." + params);
                return checkParams(params);
            }

            private Map<String, String> checkParams(Map<String, String> map) {
                Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry<String, String> pairs = (Map.Entry<String, String>) it.next();
                    if (pairs.getValue() == null) {
                        map.put(pairs.getKey(), "");
                    }
                }
                return map;
            }
        };
        // Adding request to request queue
        MaintainRequestQueue.getInstance(this).addToRequestQueue(req, "tag");
    }

    private void saveFCMToken(final String mobile, final String fcmToken) {

        String url = Constant.URL + "update_fcmToken";
        url = url.replace(" ", "%20");
        url = url.replace("\n", "%0A");
        Log.e("User Name", "" + url);
        StringRequest req = new StringRequest(com.android.volley.Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Response", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.optBoolean("success")) {

                        System.out.println("fcmToken Updated");

                    } else {
                        System.out.println(jsonObject.optString("error"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", "Error: " + error.getMessage());
            }

        }) {
            @Override
            public Request.Priority getPriority() {
                return Request.Priority.IMMEDIATE;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("authorization", "Bearer " + SharedPrefrenceObj.getSharedValue(FranchiseActiivity.this, "auth_token"));
                params.put("content-type", "application/x-www-form-urlencoded");
                Log.d("params..", params.toString());
                return params;
            }

            //
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("mobile", mobile);
                params.put("FCMToken", fcmToken);
                System.out.println("Param value..........." + params);
                return checkParams(params);
            }

            private Map<String, String> checkParams(Map<String, String> map) {
                Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry<String, String> pairs = (Map.Entry<String, String>) it.next();
                    if (pairs.getValue() == null) {
                        map.put(pairs.getKey(), "");
                    }
                }
                return map;
            }
        };
        // Adding request to request queue
        MaintainRequestQueue.getInstance(this).addToRequestQueue(req, "tag");
    }

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    private void getState() {

        String url = Constant.URL + "state";
        url = url.replace(" ", "%20");
        url = url.replace("\n", "%0A");
        Log.e("User Name", "" + url);
        progressDialog.show();
        progressDialog.setMessage("Saving data....");
        StringRequest req = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Response", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.optBoolean("success")) {

                        JSONArray data = jsonObject.getJSONArray("data");
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject jsonObject1 = data.getJSONObject(i);
                            String stateName = jsonObject1.getString("state_name");

                            stateArray.add(stateName);

                        }
                        stateArray.add(0, "Select State");

                        ArrayAdapter<String> statee = new ArrayAdapter<String>(FranchiseActiivity.this, R.layout.spinner_text_layout, stateArray);
                        state.setAdapter(statee);
                        statee.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        state.setAdapter(statee);


                        progressDialog.dismiss();
                    } else {

                        progressDialog.dismiss();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", "Error: " + error.getMessage());
                progressDialog.dismiss();
            }

        }) {
            @Override
            public Request.Priority getPriority() {
                return Request.Priority.IMMEDIATE;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                //       params.put("authorization","Bearer "+SharedPrefrenceObj.getSharedValue(FranchiseActiivity.this,"auth_token"));
                params.put("content-type", "application/x-www-form-urlencoded");
                Log.d("params..", params.toString());
                return params;
            }

            //
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();


                return checkParams(params);
            }

            private Map<String, String> checkParams(Map<String, String> map) {
                Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry<String, String> pairs = (Map.Entry<String, String>) it.next();
                    if (pairs.getValue() == null) {
                        map.put(pairs.getKey(), "");
                    }
                }
                return map;
            }
        };
        // Adding request to request queue
        MaintainRequestQueue.getInstance(this).addToRequestQueue(req, "tag");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        progressDialog.dismiss();
    }
}
