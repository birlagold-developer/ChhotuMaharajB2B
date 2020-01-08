package com.chhotumaharajbusiness.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.chhotumaharajbusiness.R;
import com.chhotumaharajbusiness.constant.Constant;
import com.chhotumaharajbusiness.constant.MaintainRequestQueue;
import com.chhotumaharajbusiness.constant.SharedPrefrenceObj;
import com.chhotumaharajbusiness.utility.AvenuesParams;
import com.chhotumaharajbusiness.utility.LoadingDialog;
import com.chhotumaharajbusiness.utility.RSAUtility;
import com.chhotumaharajbusiness.utility.ServiceUtility;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class WebViewActivity extends AppCompatActivity {


    Intent mainIntent;
    String encVal;
    String vResponse;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        getSupportActionBar().hide();
        mainIntent = getIntent();
        //get rsa key method
        progressDialog = new ProgressDialog(WebViewActivity.this);
        progressDialog.setCancelable(false);

        get_RSA_key(mainIntent.getStringExtra(AvenuesParams.ACCESS_CODE), mainIntent.getStringExtra(AvenuesParams.ORDER_ID));


    }

    private class RenderView extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            LoadingDialog.showLoadingDialog(WebViewActivity.this, "Loading...");
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            if (!ServiceUtility.chkNull(vResponse).equals("") && ServiceUtility.chkNull(vResponse).toString().indexOf("ERROR") == -1) {
                StringBuffer vEncVal = new StringBuffer("");
                vEncVal.append(ServiceUtility.addToPostParams(AvenuesParams.AMOUNT, mainIntent.getStringExtra(AvenuesParams.AMOUNT)));
                vEncVal.append(ServiceUtility.addToPostParams(AvenuesParams.CURRENCY, mainIntent.getStringExtra(AvenuesParams.CURRENCY)));
                encVal = RSAUtility.encrypt(vEncVal.substring(0, vEncVal.length() - 1), vResponse);  //encrypt amount and currency
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            LoadingDialog.cancelLoading();

            @SuppressWarnings("unused")
            class MyJavaScriptInterface {
                @JavascriptInterface
                public void processHTML(String html) {
                    // process the html source code to get final status of transaction
                    String status = null;
                    if(html.indexOf("Failure") != -1) {
                        status = "Transaction Declined!";
                    }else if (html.indexOf("Success") != -1) {
                        status = "Transaction Successful!";

                    }else if (html.indexOf("Aborted") != -1) {
                        status = "Transaction Cancelled!";
                    } else {
                        status = "Status Not Known!";
                    }
                    Toast.makeText(getApplicationContext(), status, Toast.LENGTH_SHORT).show();
                  /*  Intent intent = new Intent(getApplicationContext(), StatusActivity.class);
                    intent.putExtra("transStatus", status);
                    startActivity(intent);*/
                }
            }
            final WebView webview = (WebView) findViewById(R.id.webview);
            webview.getSettings().setJavaScriptEnabled(true);
            webview.addJavascriptInterface(new MyJavaScriptInterface(), "HTMLOUT");
            webview.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(webview, url);
                    LoadingDialog.cancelLoading();
                    Log.d("URlll",url.toString());
                    if (url.indexOf(Constant.URL+"payment_Response") != -1) {

                        //  webview.loadUrl("javascript:window.HTMLOUT.processHTML('<head>'+document.getElementsByTagName('html')[0].innerHTML+'</head>');");
                        getOrderid(mainIntent.getStringExtra(AvenuesParams.ORDER_ID));
                    }
                }
                @Override
                public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                    Log.d("Errrorr.......", String.valueOf(errorCode));
                }
                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    super.onPageStarted(view, url, favicon);
                    LoadingDialog.showLoadingDialog(WebViewActivity.this, "Loading...");
                }
            });
            try {
                String postData = AvenuesParams.ACCESS_CODE + "=" + URLEncoder.encode(mainIntent.getStringExtra(AvenuesParams.ACCESS_CODE), "UTF-8") + "&" +
                        AvenuesParams.MERCHANT_ID           + "=" + URLEncoder.encode(mainIntent.getStringExtra(AvenuesParams.MERCHANT_ID), "UTF-8") + "&" +
                        AvenuesParams.ORDER_ID              + "=" + URLEncoder.encode(mainIntent.getStringExtra(AvenuesParams.ORDER_ID), "UTF-8") + "&" +
                        AvenuesParams.REDIRECT_URL          + "=" + URLEncoder.encode(mainIntent.getStringExtra(AvenuesParams.REDIRECT_URL), "UTF-8") + "&" +
                        AvenuesParams.CANCEL_URL            + "=" + URLEncoder.encode(mainIntent.getStringExtra(AvenuesParams.CANCEL_URL), "UTF-8") + "&" +
                        AvenuesParams.ENC_VAL               + "=" + URLEncoder.encode(encVal, "UTF-8")+ "&" +
                        AvenuesParams.AMOUNT                + "=" + URLEncoder.encode(mainIntent.getStringExtra(AvenuesParams.AMOUNT), "UTF-8")  + "&" +
                        AvenuesParams.BILLING_NAME          + "=" + URLEncoder.encode(mainIntent.getStringExtra(AvenuesParams.BILLING_NAME), "UTF-8") + "&" +
                        AvenuesParams.BILLING_EMAIL         + "=" + URLEncoder.encode(mainIntent.getStringExtra(AvenuesParams.BILLING_EMAIL), "UTF-8") + "&" +
                        AvenuesParams.BILLING_STATE         + "=" + URLEncoder.encode(mainIntent.getStringExtra(AvenuesParams.BILLING_STATE), "UTF-8") + "&" +
                        AvenuesParams.BILLING_CITY         + "=" + URLEncoder.encode(mainIntent.getStringExtra(AvenuesParams.BILLING_CITY), "UTF-8") + "&" +
                        AvenuesParams.BILLING_TEL           + "=" + URLEncoder.encode(mainIntent.getStringExtra(AvenuesParams.BILLING_TEL), "UTF-8");

                webview.postUrl(Constant.TRANS_URL, postData.getBytes());
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }

    public void get_RSA_key(final String ac, final String od) {
        LoadingDialog.showLoadingDialog(WebViewActivity.this, "Loading...");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, mainIntent.getStringExtra(AvenuesParams.RSA_KEY_URL),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Toast.makeText(WebViewActivity.this,response,Toast.LENGTH_LONG).show();
                        LoadingDialog.cancelLoading();
                        if (response != null && !response.equals("")) {
                            vResponse = response;     ///save retrived rsa key
                            if (vResponse.contains("!ERROR!")) {
                                show_alert(vResponse);
                            } else {
                                new RenderView().execute();   // Calling async task to get display content
                            }
                        }
                        else
                        {
                            show_alert("No response");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        LoadingDialog.cancelLoading();
                        //Toast.makeText(WebViewActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put(AvenuesParams.ACCESS_CODE, ac);
                params.put(AvenuesParams.ORDER_ID, od);
                return params;
            }

        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void show_alert(String msg) {
        AlertDialog alertDialog = new AlertDialog.Builder(
                WebViewActivity.this).create();
        alertDialog.setTitle("Error!!!");
        if (msg.contains("\n"))
            msg = msg.replaceAll("\\\n", "");
        alertDialog.setMessage(msg);
        alertDialog.setButton(Dialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        alertDialog.show();
    }

    private void getOrderid(final String order_id) {

        String url = Constant.URL+"payment-status?payment_id="+order_id;

        url = url.replace(" ", "%20");
        url = url.replace("\n", "%0A");
        Log.e("User Name",""+url);

        progressDialog.show();
        progressDialog.setMessage("Please wait...");

        StringRequest req = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Response", response);
                try {
                    JSONObject jsonObject= new JSONObject(response);
                    if(jsonObject.optBoolean("success")) {

                        JSONObject data =jsonObject.getJSONObject("data");
                        int id = data.getInt("id");
                        String status = data.getString("orderstatus");
                        if(status.equalsIgnoreCase("Success")){
                            Intent intent = new Intent(getApplicationContext(), PaymentComplete.class);
                            intent.putExtra("ID", id);
                            intent.putExtra("status",status);
                            startActivity(intent);
                            finish();
                        }
                        else if(status.equalsIgnoreCase("Aborted")){
                            Intent intent = new Intent(getApplicationContext(), PaymentComplete.class);
                            intent.putExtra("ID", id);
                            intent.putExtra("status",status);
                            startActivity(intent);
                            finish();
                        }
                        else if(status.equalsIgnoreCase("Failure")){
                            Intent intent = new Intent(getApplicationContext(), PaymentComplete.class);
                            intent.putExtra("ID", id);
                            intent.putExtra("status",status);
                            startActivity(intent);
                            finish();
                        }
                        else if(status.equalsIgnoreCase("SeatUnavailable")){
                            Intent intent = new Intent(getApplicationContext(), PaymentComplete.class);
                            intent.putExtra("ID", id);
                            intent.putExtra("status",status);
                            startActivity(intent);
                            finish();
                        }
                        else if(status.equalsIgnoreCase("Illegal")){
                            Intent intent = new Intent(getApplicationContext(), PaymentComplete.class);
                            intent.putExtra("ID", id);
                            intent.putExtra("status",status);
                            startActivity(intent);
                            finish();
                        }
                        else if(status.equalsIgnoreCase("invalidrequest")){
                            Intent intent = new Intent(getApplicationContext(), PaymentComplete.class);
                            intent.putExtra("ID", id);
                            intent.putExtra("status",status);
                            startActivity(intent);
                            finish();
                        }
                        progressDialog.dismiss();
                    }else{
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
                //    Log.e("TAG", "Error: " + error.getMessage());
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
                //  params.put("Content-Type","application/json");
                params.put("Accept","application/json");
                params.put("Authorization", "Bearer " + SharedPrefrenceObj.getSharedValue(WebViewActivity.this,"auth_token"));
                return params;
            }

            /*  @Override
              public Map<String, String> getParams() throws AuthFailureError {
                  Map<String, String> params = new HashMap<String, String>();
                  params.put("orderid",order_id);
                  System.out.println("Param value..........." + params);
                  return checkParams(params);
              }
          */
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

//    @Override
//    public void onBackPressed() {
//    }
}
