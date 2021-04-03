package com.chhotumaharajbusiness.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

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
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class ConceptBusinessActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int RECOVERY_DIALOG_REQUEST = 1;

    // YouTube player view
    private YouTubePlayerView youTubeView;

    private VideoView videoView;
    private ProgressBar progressBar;
    private int position = 0;
    private MediaController mediaController;
    private Button interested, not_interested;
    private String language, name;
    private TextView user_name;
    private ProgressDialog progressDialog;
    private static final int RECOVERY_REQUEST = 1;
    private YouTubePlayer player;
    private ImageView rotate;
    private long seconds;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_concept_business);

        getSupportActionBar().hide();

        TextView tv = findViewById(R.id.chottu_txt);
        TextView tv1 = findViewById(R.id.maharaj_txt);
        Typeface face = Typeface.createFromAsset(getAssets(), "cooperblackstd.otf");

        tv.setTypeface(face);
        tv1.setTypeface(face);

        videoView = findViewById(R.id.videoView);
        progressBar = findViewById(R.id.progressBar);
        interested = findViewById(R.id.interested);
        not_interested = findViewById(R.id.not_interested);
        user_name = findViewById(R.id.user_login_name);
        //   rotate  = findViewById(R.id.zoom);

        interested.setOnClickListener(this);
        not_interested.setOnClickListener(this);

        //   seconds = getIntent().getLongExtra("sec",0);

        /*if(seconds!=0){
            Long l= new Long(seconds);
            position = l.intValue();
        }*/

       /* rotate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long seconds = TimeUnit.MILLISECONDS.toSeconds(videoView.getCurrentPosition());
                long miliseconds = TimeUnit.MILLISECONDS.toMillis(videoView.getCurrentPosition());
                Intent intent = new Intent(ConceptBusinessActivity.this,DemoActivity.class);
                intent.putExtra("sec",miliseconds);
                startActivity(intent);
                finish();
            }
        });*/

        user_name.setText(SharedPrefrenceObj.getSharedValue(ConceptBusinessActivity.this, "name"));

        Log.d("State..", SharedPrefrenceObj.getSharedValue(ConceptBusinessActivity.this, "state"));
        Log.d("State..", SharedPrefrenceObj.getSharedValue(ConceptBusinessActivity.this, "city"));

        progressDialog = new ProgressDialog(ConceptBusinessActivity.this);

        if (SharedPrefrenceObj.getIntegerval(ConceptBusinessActivity.this, "step") == 2) {
            Intent intent = new Intent(ConceptBusinessActivity.this, PreAnalysisActivity.class);
            startActivity(intent);
            finish();
        }

       /* youTubeView =  findViewById(R.id.youtube_view);
        youTubeView.initialize(Config.DEVELOPER_KEY, this);*/

        // Set the media controller buttons
        if (mediaController == null) {
            mediaController = new MediaController(ConceptBusinessActivity.this);

            // Set the videoView that acts as the anchor for the MediaController.
            mediaController.setAnchorView(videoView);


            // Set MediaController for VideoView
            videoView.setMediaController(mediaController);

        }


        try {
            //  videoView.setVideoPath("http://chhotumaharajb2b.com/public/video/chhotu-maharaj-project-video.mp4");
//
////            String language = SharedPrefrenceObj.getSharedValue(ConceptBusinessActivity.this,"language");
////
////            if(language.equalsIgnoreCase("English")){
////                videoView.setVideoPath("http://chhotumaharajb2b.com/public/video/English%20Version.mp4");
////            }
////            else {
////                videoView.setVideoPath("http://chhotumaharajb2b.com/public/video/chhotu-maharaj-project-video.mp4");
////            }
////

            getVideoName();

        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }

        videoView.requestFocus();


        // When the video file ready for playback.
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

            public void onPrepared(MediaPlayer mediaPlayer) {

                progressBar.setVisibility(View.GONE);

/*
                videoView.seekTo(position);
                videoView.start();*/
                Log.d("psition..............", String.valueOf(position));
                if (position == 0) {
                    videoView.start();
                }

                // When video Screen change size.
                mediaPlayer.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
                    @Override
                    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {

                        // Re-Set the videoView that acts as the anchor for the MediaController
                        mediaController.setAnchorView(videoView);
                    }
                });
            }
        });


    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        // Store current position.
        savedInstanceState.putInt("CurrentPosition", videoView.getCurrentPosition());

        videoView.pause();
    }


    // After rotating the phone. This method is called.
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // Get saved position.
        position = savedInstanceState.getInt("CurrentPosition");
        videoView.seekTo(position);


    }

    @Override
    public void onBackPressed() {
        Toast.makeText(ConceptBusinessActivity.this, "Please give your view", Toast.LENGTH_LONG).show();
//          String timeeeee = String.valueOf(player.getCurrentTimeMillis());
//          Log.d("time........",timeeeee);
    }

    /*   @Override
       public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player, boolean wasRestored) {
           if (!wasRestored) {
               this.player = player;
               player.cueVideo("fhWaJi1Hsfo"); // Plays https://www.youtube.com/watch?v=fhWaJi1Hsfo
           }
       }
   */
  /*  @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult errorReason) {
        if (errorReason.isUserRecoverableError()) {
            errorReason.getErrorDialog(this, RECOVERY_REQUEST).show();
        } else {
        //    String error = String.format(getString(R.string.player_error), errorReason.toString());
       //     Toast.makeText(this, error, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RECOVERY_REQUEST) {
            // Retry initialization if user performed a recovery action
            getYouTubePlayerProvider().initialize(Config.DEVELOPER_KEY, this);
        }
    }

    protected YouTubePlayer.Provider getYouTubePlayerProvider() {
        return youTubeView;
    }
*/
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.interested) {
            long minutes = TimeUnit.MILLISECONDS.toMinutes(videoView.getCurrentPosition());
            long seconds = TimeUnit.MILLISECONDS.toSeconds(videoView.getCurrentPosition());
            updateVideo(String.valueOf(seconds));
        } else {
            long minutes = TimeUnit.MILLISECONDS.toMinutes(videoView.getCurrentPosition());
            long seconds = TimeUnit.MILLISECONDS.toSeconds(videoView.getCurrentPosition());
            updateVideo1(String.valueOf(seconds));
        }

    }

    private void getVideoName() {

        String url = Constant.URL + "getConceptBusinessVideo";
        url = url.replace(" ", "%20");
        url = url.replace("\n", "%0A");
        Log.e("User Name", "" + url);
        progressDialog.show();
        progressDialog.setMessage("Loading data.....");
        StringRequest req = new StringRequest(com.android.volley.Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Response", response);
                try {
                    progressDialog.dismiss();
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.optBoolean("success")) {
                        JSONObject dataJsonObject = jsonObject.getJSONObject("data");
                        String video = dataJsonObject.getString("video");

                        videoView.setVideoPath(Constant.VIDEO_PATH + video.replace(" ", "%20"));
                        videoView.requestFocus();

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
            public Request.Priority getPriority() {
                return Request.Priority.IMMEDIATE;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("authorization", "Bearer " + SharedPrefrenceObj.getSharedValue(ConceptBusinessActivity.this, "auth_token"));
                params.put("content-type", "application/x-www-form-urlencoded");
                Log.d("params..", params.toString());
                return params;
            }

            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("userid", String.valueOf(SharedPrefrenceObj.getIntegerval(ConceptBusinessActivity.this, "id")));
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
        MaintainRequestQueue.getInstance(ConceptBusinessActivity.this).addToRequestQueue(req, "tag");
    }

    private void updateVideo(final String video_time) {

        String url = Constant.URL + "updatevideop1p2";
        url = url.replace(" ", "%20");
        url = url.replace("\n", "%0A");
        Log.e("User Name", "" + url);
        progressDialog.show();
        progressDialog.setMessage("Saving data ....");
        StringRequest req = new StringRequest(com.android.volley.Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Response", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.optBoolean("success")) {

                        SharedPrefrenceObj.setIntegerval(ConceptBusinessActivity.this, "step", 2);
                        Intent intent = new Intent(ConceptBusinessActivity.this, PreAnalysisActivity.class);
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
                params.put("authorization", "Bearer " + SharedPrefrenceObj.getSharedValue(ConceptBusinessActivity.this, "auth_token"));
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
                params.put("id", String.valueOf(SharedPrefrenceObj.getIntegerval(ConceptBusinessActivity.this, "id")));
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

    private void updateVideo1(final String video_time) {

        String url = Constant.URL + "updatevideop1p2";
        url = url.replace(" ", "%20");
        url = url.replace("\n", "%0A");
        Log.e("User Name", "" + url);
        progressDialog.show();
        progressDialog.setMessage("Saving data ....");
        StringRequest req = new StringRequest(com.android.volley.Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Response", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.optBoolean("success")) {

                        SharedPrefrenceObj.setIntegerval(ConceptBusinessActivity.this, "step", 1);
                        Intent intent = new Intent(ConceptBusinessActivity.this, InterestedActivity.class);
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
            public Request.Priority getPriority() {
                return Request.Priority.IMMEDIATE;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("authorization", "Bearer " + SharedPrefrenceObj.getSharedValue(ConceptBusinessActivity.this, "auth_token"));
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
                params.put("id", String.valueOf(SharedPrefrenceObj.getIntegerval(ConceptBusinessActivity.this, "id")));
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
