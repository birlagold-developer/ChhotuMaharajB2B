package com.chhotumaharajbusiness.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.chhotumaharajbusiness.R;
import com.chhotumaharajbusiness.adapter.TopicWiseAdapter;
import com.chhotumaharajbusiness.constant.Constant;
import com.chhotumaharajbusiness.constant.MaintainRequestQueue;
import com.chhotumaharajbusiness.constant.SharedPrefrenceObj;
import com.chhotumaharajbusiness.model.TopicWiseModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class TopicWiseActivity extends AppCompatActivity implements TopicWiseAdapter.ClickListener{

    ProgressDialog progressDialog;
    RecyclerView recyclerView;
    ArrayList<TopicWiseModel> topicWiseModels;
    TopicWiseAdapter topicWiseAdapter;
    TopicWiseModel topicWiseModel;
    TextView textView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic_wise);
        getSupportActionBar().hide();
        progressDialog = new ProgressDialog(TopicWiseActivity.this);
        recyclerView   = findViewById(R.id.topic_wise_recylerview);
        textView = findViewById(R.id.topic_wise_txt);

        String topic = getIntent().getStringExtra("topic");


        if (topic.equalsIgnoreCase("topic")){
            textView.setText("Topics");
            topicWiseList();
        }
        else {
            textView.setText("Questionarires");
            questionWiseList();
        }

        topicWiseAdapter = new TopicWiseAdapter();
        topicWiseAdapter.setOnItemClickListener(this);


    }

    private void topicWiseList() {

        String url = Constant.URL + "domtypeList";
        url = url.replace(" ", "%20");
        url = url.replace("\n", "%0A");
        Log.e("User Name", "" + url);
        progressDialog.show();
        progressDialog.setMessage("Getting data...");
        StringRequest req = new StringRequest(com.android.volley.Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Response", response);
                try {
                    topicWiseModels = new ArrayList<>();
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        int id = jsonObject.getInt("id");
                        String name = jsonObject.getString("name");
                        String video = jsonObject.getString("video");
                        String ppt = jsonObject.getString("ppt");

                        topicWiseModels.add(new TopicWiseModel(i+1,id,name,ppt,video));

                    }
                    topicWiseAdapter = new TopicWiseAdapter(TopicWiseActivity.this, topicWiseModels);
                    recyclerView.setAdapter(topicWiseAdapter);
                    LinearLayoutManager mLayoutManager = new LinearLayoutManager(TopicWiseActivity.this);
                    recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
                    recyclerView.setLayoutManager(mLayoutManager);

                    progressDialog.dismiss();
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
                params.put("authorization","Bearer "+ SharedPrefrenceObj.getSharedValue(TopicWiseActivity.this,"auth_token"));
                params.put("content-type","application/x-www-form-urlencoded");
                Log.d("params..",params.toString());
                return params;
            }
            //
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("dom_type", "cafe");
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
        req.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MaintainRequestQueue.getInstance(this).addToRequestQueue(req, "tag");
    }

    private void questionWiseList() {

        String url = Constant.URL + "Faqtypelist";
        url = url.replace(" ", "%20");
        url = url.replace("\n", "%0A");
        Log.e("User Name", "" + url);
        progressDialog.show();
        progressDialog.setMessage("Getting data...");
        StringRequest req = new StringRequest(com.android.volley.Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Response", response);
                try {
                    topicWiseModels = new ArrayList<>();
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        int id = jsonObject.getInt("id");
                        String name = jsonObject.getString("name");
                        String video = jsonObject.getString("video");
                        String ppt = jsonObject.getString("ppt");

                        topicWiseModels.add(new TopicWiseModel(i+1,id,name,ppt,video));

                    }
                    topicWiseAdapter = new TopicWiseAdapter(TopicWiseActivity.this, topicWiseModels);
                    recyclerView.setAdapter(topicWiseAdapter);
                    LinearLayoutManager mLayoutManager = new LinearLayoutManager(TopicWiseActivity.this);
                    recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
                    recyclerView.setLayoutManager(mLayoutManager);

                    progressDialog.dismiss();
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
                params.put("authorization","Bearer "+ SharedPrefrenceObj.getSharedValue(TopicWiseActivity.this,"auth_token"));
                params.put("content-type","application/x-www-form-urlencoded");
                Log.d("params..",params.toString());
                return params;
            }
            //
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("dom_type", "cafe");
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
        req.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MaintainRequestQueue.getInstance(this).addToRequestQueue(req, "tag");
    }

    @Override
    public void onItemClick(View v, int position,String flag) {
        topicWiseModel = topicWiseAdapter.getWordAtPosition(position);

        if(flag.equalsIgnoreCase("1")){
            Intent intent = new Intent(TopicWiseActivity.this,TopicWiseDetailActivity.class);
            intent.putExtra("topic_name",topicWiseModel.getName());
            intent.putExtra("topic_video",topicWiseModel.getVideo().trim());
            intent.putExtra("video_id",topicWiseModel.getId());
            startActivity(intent);
        }
        else {
            updatePDF(topicWiseModel.getId(),topicWiseModel.getPpt());
        }

    }

    private void updatePDF(final int videoId,final String ppt) {

        String url = Constant.URL + "update_pdfvideo";
        url = url.replace(" ", "%20");
        url = url.replace("\n", "%0A");
        Log.e("User Name", "" + url);
        progressDialog.show();
        progressDialog.setMessage("Submitting query.....");
        StringRequest req = new StringRequest(com.android.volley.Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Response", response);
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(Constant.PPT_PATH+ppt.trim()));
                    startActivity(intent);
                    progressDialog.dismiss();
                } catch (Exception e) {
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
                params.put("authorization","Bearer "+ SharedPrefrenceObj.getSharedValue(TopicWiseActivity.this,"auth_token"));
                params.put("content-type","application/x-www-form-urlencoded");
                Log.d("params..",params.toString());
                return params;
            }
            //video_id,user_id,sub_query,query_type
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("video_id", String.valueOf(videoId));
                params.put("user_id", String.valueOf(SharedPrefrenceObj.getIntegerval(TopicWiseActivity.this,"id")));
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
        req.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MaintainRequestQueue.getInstance(this).addToRequestQueue(req, "tag");
    }

}
