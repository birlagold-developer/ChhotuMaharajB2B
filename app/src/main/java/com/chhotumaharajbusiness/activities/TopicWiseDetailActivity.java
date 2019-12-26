
package com.chhotumaharajbusiness.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class TopicWiseDetailActivity extends AppCompatActivity implements View.OnClickListener {

    TextView topic_name;
    VideoView videoView;
    Button understand,query;
    Button chat,call;
    String topicName,topicVideo;
    private int position = 0;
    private MediaController mediaController;
    int videoId;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic_wise_detail);

        getSupportActionBar().hide();

        topic_name = findViewById(R.id.topic_detail_name);
        videoView  = findViewById(R.id.topic_videoView);
        understand  = findViewById(R.id.understand);
        query  = findViewById(R.id.query);
        chat = findViewById(R.id.chat_me);
        call = findViewById(R.id.call_me);



        topicName  =  getIntent().getStringExtra("topic_name");
        topicVideo =  getIntent().getStringExtra("topic_video").trim();
        videoId    =  getIntent().getIntExtra("video_id",0);

        progressDialog = new ProgressDialog(this);


        chat.setOnClickListener(this);
        call.setOnClickListener(this);
        query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long seconds = TimeUnit.MILLISECONDS.toSeconds(videoView.getCurrentPosition());
                Intent intent = new Intent(TopicWiseDetailActivity.this,QueryActivity.class);
                intent.putExtra("video_id",videoId);
                intent.putExtra("video_time",String.valueOf(seconds));
                startActivity(intent);
            }
        });

        understand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long seconds = TimeUnit.MILLISECONDS.toSeconds(videoView.getCurrentPosition());
                updateQuery(videoId, String.valueOf(seconds));
            }
        });

        topic_name.setText(topicName);
        if (mediaController == null) {
            mediaController = new MediaController(TopicWiseDetailActivity.this);

            // Set the videoView that acts as the anchor for the MediaController.
            mediaController.setAnchorView(videoView);

            // Set MediaController for VideoView
            videoView.setMediaController(mediaController);

        }


        try {
                videoView.setVideoPath(Constant.VIDEO_PATH+topicVideo);
         } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }

        videoView.requestFocus();


        // When the video file ready for playback.
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

            public void onPrepared(MediaPlayer mediaPlayer) {

                videoView.seekTo(position);
                Log.d("psition..............", String.valueOf(position));
                if (position == 0) {
                    videoView.start();
                }
                // When video Screen change size.
                mediaPlayer.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
                    @Override
                    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {

                        mediaController.setAnchorView(videoView);
                    }
                });
            }
        });

    }

    @Override
    public void onBackPressed() {
        Toast.makeText(TopicWiseDetailActivity.this,"Please give your view",Toast.LENGTH_LONG).show();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
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

    private void updateQuery(final int videoId,final String video_time) {

        String url = Constant.URL + "update_VideoHistory";
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
                    Toast.makeText(TopicWiseDetailActivity.this,"Thank you for your response..",Toast.LENGTH_LONG).show();
                    finish();
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
                params.put("authorization","Bearer "+ SharedPrefrenceObj.getSharedValue(TopicWiseDetailActivity.this,"auth_token"));
                params.put("content-type","application/x-www-form-urlencoded");
                Log.d("params..",params.toString());
                return params;
            }
            //video_id,user_id,sub_query,query_type
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("video_id", String.valueOf(videoId));
                params.put("user_id", String.valueOf(SharedPrefrenceObj.getIntegerval(TopicWiseDetailActivity.this,"id")));
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
        MaintainRequestQueue.getInstance(this).addToRequestQueue(req, "tag");
    }


    private void updateQuery(final int videoId) {

        String url = Constant.URL + "update_VideoHistory";
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
                    Toast.makeText(TopicWiseDetailActivity.this,"Thank you for your response..",Toast.LENGTH_LONG).show();
                    finish();
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
                params.put("authorization","Bearer "+ SharedPrefrenceObj.getSharedValue(TopicWiseDetailActivity.this,"auth_token"));
                params.put("content-type","application/x-www-form-urlencoded");
                Log.d("params..",params.toString());
                return params;
            }
            //video_id,user_id,sub_query,query_type
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("video_id", String.valueOf(videoId));
                params.put("user_id", String.valueOf(SharedPrefrenceObj.getIntegerval(TopicWiseDetailActivity.this,"id")));
                params.put("chat_now", "1");

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
    public void onClick(View view) {

        if(view.getId()==R.id.chat_me){
            updateQuery(videoId);
        }
        if(view.getId()==R.id.call_me){
            updateQuery(videoId);
        }
    }
}
