package com.chhotumaharajbusiness.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class ConceptBusinessActivity1 extends YouTubeBaseActivity implements View.OnClickListener, YouTubePlayer.OnInitializedListener {

    private static final int RECOVERY_DIALOG_REQUEST = 1;

    // YouTube player view
    private YouTubePlayerView youTubeView;
    private YouTubePlayer player;

    private int position = 0;
    ImageView imageViewBackward, imageViewPlayPause, imageViewForward;
    Button interested, not_interested;
    ProgressBar videoProgressBar;
    TextView user_name;
    ProgressDialog progressDialog;
    Handler progressBarHandler;
    String fileName = "K48Z32iaKKI";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_concept_business1);

        TextView tv = findViewById(R.id.chottu_txt);
        TextView tv1 = findViewById(R.id.maharaj_txt);
        Typeface face = Typeface.createFromAsset(getAssets(), "cooperblackstd.otf");

        tv.setTypeface(face);
        tv1.setTypeface(face);

        youTubeView = findViewById(R.id.youtube_view);

        imageViewBackward = findViewById(R.id.video_backward);
        imageViewPlayPause = findViewById(R.id.video_playpause);
        imageViewForward = findViewById(R.id.video_forward);
        videoProgressBar = findViewById(R.id.videoProgressBar);
        interested = findViewById(R.id.interested);
        not_interested = findViewById(R.id.not_interested);
        user_name = findViewById(R.id.user_login_name);

        imageViewBackward.setOnClickListener(this);
        imageViewPlayPause.setOnClickListener(this);
        imageViewForward.setOnClickListener(this);
        interested.setOnClickListener(this);
        not_interested.setOnClickListener(this);

        user_name.setText(SharedPrefrenceObj.getSharedValue(ConceptBusinessActivity1.this, "name"));

        Log.d("State..", SharedPrefrenceObj.getSharedValue(ConceptBusinessActivity1.this, "state"));
        Log.d("State..", SharedPrefrenceObj.getSharedValue(ConceptBusinessActivity1.this, "city"));

        progressDialog = new ProgressDialog(ConceptBusinessActivity1.this);

        if (SharedPrefrenceObj.getIntegerval(ConceptBusinessActivity1.this, "step") == 2) {
            Intent intent = new Intent(ConceptBusinessActivity1.this, PreAnalysisActivity.class);
            startActivity(intent);
            finish();
        }


        try {
            getVideoName();
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }

        progressBarHandler = new Handler();
        progressBarHandler.post(progressRunnable);

        youTubeView.initialize(Constant.YOUTUBE_KEY, ConceptBusinessActivity1.this);

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
            progressBarHandler.postDelayed(this, 1000);
        }
    };

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult errorReason) {
        if (errorReason.isUserRecoverableError()) {
            errorReason.getErrorDialog(this, RECOVERY_DIALOG_REQUEST).show();
        } else {
            String errorMessage = String.format(
                    getString(R.string.error_player), errorReason.toString());
            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                        YouTubePlayer player, boolean wasRestored) {
        if (!wasRestored) {

            // loadVideo() will auto play videoCheckList
            // Use cueVideo() method, if you don't want to play it automatically
            //TODO

            player.loadVideo(fileName, position);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RECOVERY_DIALOG_REQUEST) {
            // Retry initialization if user performed a recovery action
            getYouTubePlayerProvider().initialize(Constant.YOUTUBE_KEY, this);
        }
    }

    private YouTubePlayer.Provider getYouTubePlayerProvider() {
        return (YouTubePlayerView) findViewById(R.id.youtube_view);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (player != null && player.isPlaying()) {
            player.pause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            if (player != null) {
                player.play();
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        // Store current position.
        savedInstanceState.putInt("CurrentPosition", player.getCurrentTimeMillis());

    }


    // After rotating the phone. This method is called.
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // Get saved position.
        position = savedInstanceState.getInt("CurrentPosition");

    }

    @Override
    public void onBackPressed() {
        Toast.makeText(ConceptBusinessActivity1.this, "Please give your view", Toast.LENGTH_LONG).show();
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
        long seconds = 0;
        switch (view.getId()) {
            case R.id.video_playpause:
                if (player != null) {
                    if (player.isPlaying()) {
                        player.pause();
                        imageViewPlayPause.setImageResource(R.drawable.play);
                    } else {
                        player.play();
                        imageViewPlayPause.setImageResource(R.drawable.pause);
                    }
                }
                break;
            case R.id.video_backward:
                player.seekRelativeMillis(-5000);
                break;
            case R.id.video_forward:
                player.seekRelativeMillis(5000);
                break;
            case R.id.interested:
                seconds = TimeUnit.MILLISECONDS.toSeconds(player.getCurrentTimeMillis());
                updateVideoInterested(String.valueOf(seconds));
                break;
            case R.id.not_interested:
                seconds = TimeUnit.MILLISECONDS.toSeconds(player.getCurrentTimeMillis());
                updateVideoNotInterested(String.valueOf(seconds));
                break;
        }

    }

    private void getVideoName() {

        String url = Constant.URL + "getConceptBusinessVideo";
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
                        String video = dataJsonObject.getString("video");

                        //TODO

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
                params.put("authorization", "Bearer " + SharedPrefrenceObj.getSharedValue(ConceptBusinessActivity1.this, "auth_token"));
                params.put("content-type", "application/x-www-form-urlencoded");
                Log.d("params..", params.toString());
                return params;
            }

            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("userid", String.valueOf(SharedPrefrenceObj.getIntegerval(ConceptBusinessActivity1.this, "id")));
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
        MaintainRequestQueue.getInstance(ConceptBusinessActivity1.this).addToRequestQueue(req, "tag");
    }

    private void updateVideoInterested(final String video_time) {

        String url = Constant.URL + "updatevideop1p2";
        url = url.replace(" ", "%20");
        url = url.replace("\n", "%0A");
        Log.e("User Name", "" + url);
        progressDialog.show();
        progressDialog.setMessage("Saving data ....");
        StringRequest req = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Response", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.optBoolean("success")) {

                        SharedPrefrenceObj.setIntegerval(ConceptBusinessActivity1.this, "step", 2);
                        Intent intent = new Intent(ConceptBusinessActivity1.this, PreAnalysisActivity.class);
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
            public Priority getPriority() {
                return Priority.IMMEDIATE;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("authorization", "Bearer " + SharedPrefrenceObj.getSharedValue(ConceptBusinessActivity1.this, "auth_token"));
                params.put("content-type", "application/x-www-form-urlencoded");
                Log.d("params..", params.toString());
                return params;
            }

            //
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("video_interested", "1");
                params.put("watch_video", video_time);
                params.put("id", String.valueOf(SharedPrefrenceObj.getIntegerval(ConceptBusinessActivity1.this, "id")));
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

    private void updateVideoNotInterested(final String video_time) {

        String url = Constant.URL + "updatevideop1p2";
        url = url.replace(" ", "%20");
        url = url.replace("\n", "%0A");
        Log.e("User Name", "" + url);
        progressDialog.show();
        progressDialog.setMessage("Saving data ....");
        StringRequest req = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Response", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.optBoolean("success")) {

                        SharedPrefrenceObj.setIntegerval(ConceptBusinessActivity1.this, "step", 1);
                        Intent intent = new Intent(ConceptBusinessActivity1.this, InterestedActivity.class);
                        intent.putExtra("concept", "not_interested");
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
            public Priority getPriority() {
                return Priority.IMMEDIATE;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("authorization", "Bearer " + SharedPrefrenceObj.getSharedValue(ConceptBusinessActivity1.this, "auth_token"));
                params.put("content-type", "application/x-www-form-urlencoded");
                Log.d("params..", params.toString());
                return params;
            }

            //
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("video_interested", "0");
                params.put("watch_video", video_time);
                params.put("id", String.valueOf(SharedPrefrenceObj.getIntegerval(ConceptBusinessActivity1.this, "id")));
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
}
