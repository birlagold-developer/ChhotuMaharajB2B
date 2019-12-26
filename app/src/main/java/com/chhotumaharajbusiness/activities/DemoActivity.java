package com.chhotumaharajbusiness.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

import com.chhotumaharajbusiness.R;
import com.chhotumaharajbusiness.constant.Constant;
import com.chhotumaharajbusiness.fragment.TopicWiseDetailFragment;

import java.util.concurrent.TimeUnit;

public class DemoActivity extends AppCompatActivity {

    VideoView videoView;
    private int position = 0;
    private MediaController mediaController;
    long seconds;
    ImageView zoom_out;
    String topicVideo;
    String topic,topicName,ppt;
    int videoId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);

        getSupportActionBar().hide();
        videoView =  findViewById(R.id.videoView_rotate);
        zoom_out  = findViewById(R.id.zoom_out);




        seconds = getIntent().getLongExtra("sec",0);
        topicVideo = getIntent().getStringExtra("topic_video");
        topic = getIntent().getStringExtra("topic");
        topicName = getIntent().getStringExtra("topic_name");
        videoId = getIntent().getIntExtra("video_id",0);
        ppt = getIntent().getStringExtra("ppt");


        if (mediaController == null) {
            mediaController = new MediaController(DemoActivity.this);

            // Set the videoView that acts as the anchor for the MediaController.
            mediaController.setAnchorView(videoView);


            // Set MediaController for VideoView
            videoView.setMediaController(mediaController);

        }


        try {
            videoView.setVideoPath(Constant.VIDEO_PATH+topicVideo);

         /*   if(SharedPrefrenceObj.getSharedValue(ConceptBusinessActivity.this,"language").equalsIgnoreCase("English")){
                videoView.setVideoPath("http://chhotumaharajb2b.com/public/video/English%20Version.mp4");
            }
            else {
                videoView.setVideoPath("http://chhotumaharajb2b.com/public/video/chhotu-maharaj-project-video.mp4");

            }*/

        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        Long l= new Long(seconds);
        position =l.intValue();

        videoView.requestFocus();

       // videoView.start();

        // When the video file ready for playback.
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

            public void onPrepared(MediaPlayer mediaPlayer) {

                Long l= new Long(seconds);
                position =l.intValue();


                Log.d("position..............", String.valueOf(position));
                if (position == 0) {
                    videoView.start();

                }

                videoView.seekTo(position);
                videoView.start();
                // When video Screen change size.
                mediaPlayer.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
                    @Override
                    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {


                        // Re-Set the videoView that acts as the anchor for the MediaController
                      //  mediaController.setAnchorView(videoView);
                        Log.d("comiiii","com");
                    }
                });
            }
        });

        zoom_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long miliseconds = TimeUnit.MILLISECONDS.toMillis(videoView.getCurrentPosition());
                TopicWiseDetailFragment topicWiseFragment1 = new TopicWiseDetailFragment();
                Bundle args1 = new Bundle();
                args1.putString("topic_name",topicName);
                args1.putString("topic_video",topicVideo);
                args1.putInt("video_id",videoId);
                args1.putString("topic",topic);
                args1.putString("pdf",ppt);
                args1.putLong("sec", miliseconds);
                topicWiseFragment1.setArguments(args1);
                FragmentManager fragmentManager1 = getSupportFragmentManager();
                FragmentTransaction transaction1 = fragmentManager1.beginTransaction();
                transaction1.replace(R.id.frame_container, topicWiseFragment1);
                transaction1.commit();
            }
        });

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideSystemUI();
        }
    }

    @Override
    public void onBackPressed() {

    }

    private void hideSystemUI() {
        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        // Set the content to appear under the system bars so that the
                        // content doesn't resize when the system bars hide and show.
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Hide the nav bar and status bar
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }
}
