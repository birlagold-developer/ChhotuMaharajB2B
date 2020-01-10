package com.chhotumaharajbusiness.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.appcompat.app.AlertDialog;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.chhotumaharajbusiness.R;
import com.chhotumaharajbusiness.constant.Constant;
import com.chhotumaharajbusiness.constant.MaintainRequestQueue;
import com.chhotumaharajbusiness.constant.SharedPrefrenceObj;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class TopicWiseDetailFragment1 extends YouTubeBaseActivity implements View.OnClickListener, YouTubePlayer.OnInitializedListener {

    private static final int RECOVERY_DIALOG_REQUEST = 1;

    // YouTube player view
    private YouTubePlayerView youTubeView;
    private YouTubePlayer player;

    private int position = 0;
    TextView topic_name, language;
    Button understand,query;
    Button chat,call;
    String topicName,prefered_language,topicVideo,ppt;
    ImageView imageViewBackward, imageViewPlayPause, imageViewForward;
    ProgressBar videoProgressBar;
    Handler progressBarHandler;
    int videoId, parentVideoId;
    String[] videoLanguage = null;
    ProgressDialog progressDialog;
    String topic;
    LinearLayout question_layout,topic_layout;
    Button not_understood,question_understood;
    Button viewpdf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic_wise_detail1);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setActionBar(toolbar);

//        getActionBar().setTitle("");

        topic_name = findViewById(R.id.topic_detail_name);
        language = findViewById(R.id.language);

        youTubeView = findViewById(R.id.youtube_view);
        imageViewBackward = findViewById(R.id.video_backward);
        imageViewPlayPause = findViewById(R.id.video_playpause);
        imageViewForward = findViewById(R.id.video_forward);
        videoProgressBar = findViewById(R.id.videoProgressBar);

        understand  = findViewById(R.id.understand);
        query  = findViewById(R.id.query);
        chat = findViewById(R.id.chat_me);
        call = findViewById(R.id.call_me);
        question_layout = findViewById(R.id.question_layout);
        topic_layout = findViewById(R.id.topic_layout);
        question_understood = findViewById(R.id.question_understand);
        not_understood = findViewById(R.id.not_understood);
        viewpdf = findViewById(R.id.detail_pdf);

        topicName           =  getIntent().getStringExtra("topic_name");
        prefered_language   =  SharedPrefrenceObj.getSharedValue(TopicWiseDetailFragment1.this,"language");
        topicVideo          =  getIntent().getStringExtra("topic_video").trim();
        videoId             =  getIntent().getIntExtra("video_id",0);
        parentVideoId       =   videoId;
        topic               =  getIntent().getStringExtra("topic");
        ppt                 =  getIntent().getStringExtra("pdf");

        progressDialog = new ProgressDialog(TopicWiseDetailFragment1.this);

        if(topic.equalsIgnoreCase("topic")){
            topic_layout.setVisibility(View.VISIBLE);
            question_layout.setVisibility(View.GONE);

            getVideoForTopic(prefered_language);

        } else {
            topic_layout.setVisibility(View.GONE);
            question_layout.setVisibility(View.VISIBLE);

            getVideoForQuestion(prefered_language);

        }

        imageViewBackward.setOnClickListener(this);
        imageViewPlayPause.setOnClickListener(this);
        imageViewForward.setOnClickListener(this);

        language.setOnClickListener(this);
        understand.setOnClickListener(this);
        not_understood.setOnClickListener(this);
        question_understood.setOnClickListener(this);
        query.setOnClickListener(this);
        chat.setOnClickListener(this);
        call.setOnClickListener(this);

        topic_name.setText(topicName);


        if (ppt.equalsIgnoreCase("null")) {
            viewpdf.setText("PDF file Not Available");
        } else {
            viewpdf.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    player.pause();
                    updatePDF(videoId, ppt);
                }
            });
        }

        progressBarHandler = new Handler();
        progressBarHandler.post(progressRunnable);

     }

     Runnable progressRunnable = new Runnable() {
         @Override
         public void run() {
             if (player != null && player.isPlaying()) {

                 System.out.println("CurrentTimeMillis : " + player.getCurrentTimeMillis());
                 System.out.println("DurationMillis : " + player.getDurationMillis());
                 int progress = (player.getCurrentTimeMillis() * 100) / player.getDurationMillis();
                 System.out.println("progress : " + progress);
                 videoProgressBar.setProgress(progress);
             }
             progressBarHandler.postDelayed(this,1000);
         }
     };

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult errorReason) {
        if (errorReason.isUserRecoverableError()) {
            errorReason.getErrorDialog(TopicWiseDetailFragment1.this, RECOVERY_DIALOG_REQUEST).show();
        } else {
            String errorMessage = String.format(getString(R.string.error_player), errorReason.toString());
            Toast.makeText(TopicWiseDetailFragment1.this, errorMessage, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                        YouTubePlayer player, boolean wasRestored) {
        if (!wasRestored) {

            // loadVideo() will auto play videoCheckList
            // Use cueVideo() method, if you don't want to play it automatically
            //TODO

            player.loadVideo(topicVideo, position);
            this.player = player;

            // Hiding player controls
            player.setPlayerStyle(YouTubePlayer.PlayerStyle.CHROMELESS);

            // set YouTube Player State Change Listener
            player.setPlayerStateChangeListener(new YouTubePlayer.PlayerStateChangeListener() {
                @Override
                public void onLoading() {

                }

                @Override
                public void onLoaded(String s) {

                }

                @Override
                public void onAdStarted() {

                }

                @Override
                public void onVideoStarted() {

                }

                @Override
                public void onVideoEnded() {

                }

                @Override
                public void onError(YouTubePlayer.ErrorReason errorReason) {

                }
            });

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RECOVERY_DIALOG_REQUEST) {
            getYouTubePlayerProvider().initialize(Constant.YOUTUBE_KEY, this);
        }
    }

    private YouTubePlayer.Provider getYouTubePlayerProvider() {
        return (YouTubePlayerView) findViewById(R.id.youtube_view);
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            player.pause();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            player.play();
        } catch (Exception e) {
            System.out.println(e);
        }
    }


    @Override
    public void onBackPressed() {
        Toast.makeText(TopicWiseDetailFragment1.this, "Your response is required", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            progressBarHandler.removeCallbacks(progressRunnable);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.video_playpause) {
            if (player.isPlaying()) {
                player.pause();
                imageViewPlayPause.setImageResource(R.drawable.play);
            } else {
                player.play();
                imageViewPlayPause.setImageResource(R.drawable.pause);
            }
        } else if (view.getId() == R.id.video_backward) {
            player.seekRelativeMillis(-5000);
        } else if (view.getId() == R.id.video_forward) {
            player.seekRelativeMillis(5000);
        } else if (view.getId() == R.id.language) {
            try {

                ArrayList<String> languageList = new ArrayList<String>();

                for (String language : videoLanguage) {
                    languageList.add(language);
                }

                final ArrayAdapter<String> languageAdapter = new ArrayAdapter<String>(TopicWiseDetailFragment1.this, android.R.layout.simple_list_item_1, languageList);

                AlertDialog.Builder builder = new AlertDialog.Builder(TopicWiseDetailFragment1.this);

                builder.setTitle("Select Language");

                builder.setAdapter(languageAdapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String selected_language = languageAdapter.getItem(which);
                        language.setText(selected_language);
                        dialog.dismiss();

                        player.pause();

                        if(topic.equalsIgnoreCase("topic")){
                            getVideoForTopic(selected_language);
                        } else {
                            getVideoForQuestion(selected_language);
                        }
                    }
                });

                AlertDialog alertDialog = builder.create();
                ListView listView = alertDialog.getListView();
                listView.setDivider(new ColorDrawable(Color.GRAY));
                listView.setDividerHeight(2);
                alertDialog.show();

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        if (view.getId() == R.id.understand) {
            player.pause();
            long seconds = TimeUnit.MILLISECONDS.toSeconds(player.getCurrentTimeMillis());
            updateUnderstood(String.valueOf(seconds));
        }

        if (view.getId() == R.id.not_understood) {
            player.pause();
            long seconds = TimeUnit.MILLISECONDS.toSeconds(player.getCurrentTimeMillis());
            updateNotUnderstood(String.valueOf(seconds));
        }

        if (view.getId() == R.id.question_understand) {
            player.pause();
            long seconds = TimeUnit.MILLISECONDS.toSeconds(player.getCurrentTimeMillis());
            updateUnderstood(String.valueOf(seconds));
        }

        if (view.getId() == R.id.query) {
            player.pause();
            long seconds = TimeUnit.MILLISECONDS.toSeconds(player.getCurrentTimeMillis());
            Intent intent = new Intent(TopicWiseDetailFragment1.this, QueryActivity.class);
            intent.putExtra("video_id",videoId);
            intent.putExtra("parentvideo_id",parentVideoId);
            intent.putExtra("video_time",String.valueOf(seconds));
            startActivity(intent);
        }

        if(view.getId()==R.id.chat_me){
            player.pause();
            updateChatMe();
        }

        if(view.getId()==R.id.call_me){
            player.pause();
            updateCallMe();

        }
    }

    private void getVideoForTopic(final String selectedLanguage) {

        String url = Constant.URL + "videoDetailsForLanguage";
        url = url.replace(" ", "%20");
        url = url.replace("\n", "%0A");
        Log.e("User Name", "" + url);
        progressDialog.show();
        progressDialog.setMessage("Loading data.....");
        StringRequest req = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Response", response);
                try {
                    progressDialog.dismiss();
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.optBoolean("success")) {
                        JSONObject dataJsonObject = jsonObject.getJSONObject("data");
                        JSONObject video = dataJsonObject.getJSONObject("list");
                        JSONArray videoLanguageArray = dataJsonObject.getJSONArray("video_languages");

                        videoLanguage = new String[videoLanguageArray.length()];

                        for (int i=0;i< videoLanguageArray.length();i++) {
                            JSONObject object = videoLanguageArray.getJSONObject(i);
                            videoLanguage[i] = object.getString("lang_name");
                        }

                        topicVideo          =  video.getString("youtube_id");
                        videoId             =  video.getInt("id");
                        String video_lang   =  video.getString("language_name");

                        youTubeView.initialize(Constant.YOUTUBE_KEY, TopicWiseDetailFragment1.this);

                        language.setText(video_lang);

                    }

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
            public Priority getPriority() {
                return Priority.IMMEDIATE;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("authorization","Bearer "+ SharedPrefrenceObj.getSharedValue(TopicWiseDetailFragment1.this,"auth_token"));
                params.put("content-type","application/x-www-form-urlencoded");
                Log.d("params..",params.toString());
                return params;
            }

            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("video_id", String.valueOf(parentVideoId));
                params.put("preferred_language", selectedLanguage);
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
        MaintainRequestQueue.getInstance(TopicWiseDetailFragment1.this).addToRequestQueue(req, "tag");
    }

    private void getVideoForQuestion(final String selectedLanguage) {

        String url = Constant.URL + "videoDetailsForQuestionary";
        url = url.replace(" ", "%20");
        url = url.replace("\n", "%0A");
        Log.e("User Name", "" + url);
        progressDialog.show();
        progressDialog.setMessage("Loading data.....");
        StringRequest req = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Response", response);
                try {
                    progressDialog.dismiss();
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.optBoolean("success")) {
                        JSONObject dataJsonObject = jsonObject.getJSONObject("data");
                        JSONObject video = dataJsonObject.getJSONObject("list");
                        JSONArray videoLanguageArray = dataJsonObject.getJSONArray("video_languages");

                        videoLanguage = new String[videoLanguageArray.length()];

                        for (int i=0;i< videoLanguageArray.length();i++) {
                            JSONObject object = videoLanguageArray.getJSONObject(i);
                            videoLanguage[i] = object.getString("lang_name");
                        }

                        topicVideo          =  video.getString("youtube_id");
                        videoId             =  video.getInt("id");
                        String video_lang   =  video.getString("language_name");

                        language.setText(video_lang);

                        youTubeView.initialize(Constant.YOUTUBE_KEY, TopicWiseDetailFragment1.this);


                    }

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
            public Priority getPriority() {
                return Priority.IMMEDIATE;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("authorization","Bearer "+ SharedPrefrenceObj.getSharedValue(TopicWiseDetailFragment1.this,"auth_token"));
                params.put("content-type","application/x-www-form-urlencoded");
                Log.d("params..",params.toString());
                return params;
            }

            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("video_id", String.valueOf(parentVideoId));
                params.put("preferred_language", selectedLanguage);
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
        MaintainRequestQueue.getInstance(TopicWiseDetailFragment1.this).addToRequestQueue(req, "tag");
    }

    private void updateUnderstood(final String video_time) {

        String url = Constant.URL + "update_VideoHistory";
        url = url.replace(" ", "%20");
        url = url.replace("\n", "%0A");
        Log.e("User Name", "" + url);
        progressDialog.show();
        progressDialog.setMessage("Submitting query.....");
        StringRequest req = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Response", response);
                try {
                    Toast.makeText(TopicWiseDetailFragment1.this,"Thank you for your response..",Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                    finish();
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
            public Priority getPriority() {
                return Priority.IMMEDIATE;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("authorization","Bearer "+ SharedPrefrenceObj.getSharedValue(TopicWiseDetailFragment1.this,"auth_token"));
                params.put("content-type","application/x-www-form-urlencoded");
                Log.d("params..",params.toString());
                return params;
            }
            //video_id,user_id,sub_query,query_type
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("video_id", String.valueOf(videoId));
                params.put("user_id", String.valueOf(SharedPrefrenceObj.getIntegerval(TopicWiseDetailFragment1.this,"id")));
                params.put("sub_query", "");
                params.put("watch_video", video_time);
                params.put("query_type", "understood");
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
        MaintainRequestQueue.getInstance(TopicWiseDetailFragment1.this).addToRequestQueue(req, "tag");
    }

    private void updateNotUnderstood(final String video_time) {

        String url = Constant.URL + "update_VideoHistory";
        url = url.replace(" ", "%20");
        url = url.replace("\n", "%0A");
        Log.e("User Name", "" + url);
        progressDialog.show();
        progressDialog.setMessage("updating data...");
        StringRequest req = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Response", response);
                try {
                    Toast.makeText(TopicWiseDetailFragment1.this,"Thank you for your response..",Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                    finish();
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
            public Priority getPriority() {
                return Priority.IMMEDIATE;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("authorization","Bearer "+ SharedPrefrenceObj.getSharedValue(TopicWiseDetailFragment1.this,"auth_token"));
                params.put("content-type","application/x-www-form-urlencoded");
                Log.d("params..",params.toString());
                return params;
            }
            //video_id,user_id,sub_query,query_type
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("video_id", String.valueOf(videoId));
                params.put("user_id", String.valueOf(SharedPrefrenceObj.getIntegerval(TopicWiseDetailFragment1.this,"id")));
                params.put("sub_query", "");
                params.put("watch_video", video_time);
                params.put("query_type", "not_understood");
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
        MaintainRequestQueue.getInstance(TopicWiseDetailFragment1.this).addToRequestQueue(req, "tag");
    }

    private void updateChatMe() {

        String url = Constant.URL + "chatMe";
        url = url.replace(" ", "%20");
        url = url.replace("\n", "%0A");
        Log.e("User Name", "" + url);
        progressDialog.show();
        progressDialog.setMessage("updating data...");
        StringRequest req = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Response", response);
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.optBoolean("success")) {

                        PackageManager packageManager = getApplicationContext().getPackageManager();
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        String phone = "917718802588";
                        String message = "I'm interested in your Chhotu Maharaj Cine Cafe Franchise";
                        try {
                            String url = "https://api.whatsapp.com/send?phone=" + phone + "&text=" + URLEncoder.encode(message, "UTF-8");
                            i.setPackage("com.whatsapp");
                            i.setData(Uri.parse(url));
                            if (i.resolveActivity(packageManager) != null) {
                                startActivity(i);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
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
            public Priority getPriority() {
                return Priority.IMMEDIATE;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("authorization","Bearer "+ SharedPrefrenceObj.getSharedValue(TopicWiseDetailFragment1.this,"auth_token"));
                params.put("content-type","application/x-www-form-urlencoded");
                Log.d("params..",params.toString());
                return params;
            }
            //video_id,user_id,sub_query,query_type
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("video_id", String.valueOf(videoId));
                params.put("user_id", String.valueOf(SharedPrefrenceObj.getIntegerval(TopicWiseDetailFragment1.this,"id")));
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
        MaintainRequestQueue.getInstance(TopicWiseDetailFragment1.this).addToRequestQueue(req, "tag");
    }

    private void updateCallMe() {

        String url = Constant.URL + "callMe";
        url = url.replace(" ", "%20");
        url = url.replace("\n", "%0A");
        Log.e("User Name", "" + url);
        progressDialog.show();
        progressDialog.setMessage("updating data...");
        StringRequest req = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Response", response);
                try {
                    progressDialog.dismiss();
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.optBoolean("success")) {
                        new AlertDialog.Builder(TopicWiseDetailFragment1.this)
                                .setTitle("")
                                .setMessage("Our concern person will call you shortly")
                                .setCancelable(false)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        finish();
                                    }

                                })
                                .show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
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
            public Priority getPriority() {
                return Priority.IMMEDIATE;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("authorization","Bearer "+ SharedPrefrenceObj.getSharedValue(TopicWiseDetailFragment1.this,"auth_token"));
                params.put("content-type","application/x-www-form-urlencoded");
                Log.d("params..",params.toString());
                return params;
            }
            //video_id,user_id,sub_query,query_type
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("video_id", String.valueOf(videoId));
                params.put("user_id", String.valueOf(SharedPrefrenceObj.getIntegerval(TopicWiseDetailFragment1.this,"id")));
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
        MaintainRequestQueue.getInstance(TopicWiseDetailFragment1.this).addToRequestQueue(req, "tag");
    }

    private void updatePDF(final int videoId,final String ppt) {

        String url = Constant.URL + "update_pdfvideo";
        url = url.replace(" ", "%20");
        url = url.replace("\n", "%0A");
        Log.e("User Name", "" + url);
        progressDialog.show();
        progressDialog.setMessage("Opening PDF.....");
        StringRequest req = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
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
            public Priority getPriority() {
                return Priority.IMMEDIATE;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("authorization","Bearer "+ SharedPrefrenceObj.getSharedValue(TopicWiseDetailFragment1.this,"auth_token"));
                params.put("content-type","application/x-www-form-urlencoded");
                Log.d("params..",params.toString());
                return params;
            }
            //video_id,user_id,sub_query,query_type
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("video_id", String.valueOf(videoId));
                params.put("user_id", String.valueOf(SharedPrefrenceObj.getIntegerval(TopicWiseDetailFragment1.this,"id")));
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
        MaintainRequestQueue.getInstance(TopicWiseDetailFragment1.this).addToRequestQueue(req, "tag");
    }
}
