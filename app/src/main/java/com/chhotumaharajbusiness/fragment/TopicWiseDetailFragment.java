package com.chhotumaharajbusiness.fragment;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.chhotumaharajbusiness.R;
import com.chhotumaharajbusiness.activities.QueryActivity;
import com.chhotumaharajbusiness.constant.Constant;
import com.chhotumaharajbusiness.constant.MaintainRequestQueue;
import com.chhotumaharajbusiness.constant.SharedPrefrenceObj;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class TopicWiseDetailFragment extends Fragment implements View.OnClickListener {
    public static View fragment;

    TextView topic_name, language;
    VideoView videoView;
    ProgressBar progressBar;
    Button understand, query;
    Button chat, call;
    String topicName, prefered_language, topicVideo, ppt;
    private int position = 0;
    private MediaController mediaController;
    int videoId, parentVideoId;
    String[] videoLanguage = null;
    ProgressDialog progressDialog;
    public static final int MAKE_CALL_PERMISSION_REQUEST_CODE = 1;
    String topic;
    LinearLayout question_layout, topic_layout;
    Button not_understood, question_understood;
    Button viewpdf;
    ImageView zoom;
    long sec;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragment = inflater.inflate(R.layout.activity_topic_wise_detail, container, false);

        topic_name = fragment.findViewById(R.id.topic_detail_name);
        language = fragment.findViewById(R.id.language);
        videoView = fragment.findViewById(R.id.topic_videoView);
        progressBar = fragment.findViewById(R.id.progressBar);
        understand = fragment.findViewById(R.id.understand);
        query = fragment.findViewById(R.id.query);
        chat = fragment.findViewById(R.id.chat_me);
        call = fragment.findViewById(R.id.call_me);
        question_layout = fragment.findViewById(R.id.question_layout);
        topic_layout = fragment.findViewById(R.id.topic_layout);
        question_understood = fragment.findViewById(R.id.question_understand);
        not_understood = fragment.findViewById(R.id.not_understood);
        viewpdf = fragment.findViewById(R.id.detail_pdf);
        //   zoom   = fragment.findViewById(R.id.fragment_zoom);


    /*    sec        = getArguments().getLong("sec");

        if(sec!=0){
            Long l= new Long(sec);
            position =l.intValue();
        }*/

        topicName = getArguments().getString("topic_name");
        prefered_language = SharedPrefrenceObj.getSharedValue(getActivity(), "language");
        topicVideo = getArguments().getString("topic_video").trim();
        videoId = getArguments().getInt("video_id", 0);
        parentVideoId = videoId;
        topic = getArguments().getString("topic");
        ppt = getArguments().getString("pdf");

        progressDialog = new ProgressDialog(getActivity());

        if (topic.equalsIgnoreCase("topic")) {
            topic_layout.setVisibility(View.VISIBLE);
            question_layout.setVisibility(View.GONE);

            getVideoForTopic(prefered_language);

        } else {
            topic_layout.setVisibility(View.GONE);
            question_layout.setVisibility(View.VISIBLE);

            getVideoForQuestion(prefered_language);

        }

        language.setOnClickListener(this);
        understand.setOnClickListener(this);
        not_understood.setOnClickListener(this);
        question_understood.setOnClickListener(this);
        query.setOnClickListener(this);
        chat.setOnClickListener(this);
        call.setOnClickListener(this);

        topic_name.setText(topicName);

        if (mediaController == null) {
            mediaController = new MediaController(getActivity());

            // Set the videoView that acts as the anchor for the MediaController.
            mediaController.setAnchorView(videoView);

            // Set MediaController for VideoView
            videoView.setMediaController(mediaController);

        }

        // When the video file ready for playback.
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

            public void onPrepared(MediaPlayer mediaPlayer) {


                progressBar.setVisibility(View.GONE);
                videoView.seekTo(position);
                videoView.start();
                Log.e("psition..............", String.valueOf(position));
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

        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Log.e("setOnCompletionListener","onCompletion");
            }
        });


        if (ppt.equalsIgnoreCase("null")) {
            viewpdf.setText("PDF file Not Available");
        } else {
            viewpdf.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    videoView.pause();
                    updatePDF(videoId, ppt);
                }
            });
        }

        /*zoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long miliseconds = TimeUnit.MILLISECONDS.toMillis(videoView.getCurrentPosition());
                Intent intent = new Intent(getActivity(),DemoActivity.class);
                intent.putExtra("sec",miliseconds);
                intent.putExtra("topic_name",topicName);
                intent.putExtra("topic_video",topicVideo);
                intent.putExtra("video_id",videoId);
                intent.putExtra("topic",topic);
                intent.putExtra("pdf",ppt);
                startActivity(intent);

            }
        });
*/

        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            videoView.resume();
        } catch (Exception e) {
            System.out.println(e);
        }

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();

        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        Toast.makeText(getActivity(), "Your response is required", Toast.LENGTH_SHORT).show();
                        return true;
                    }
                }
                return false;
            }
        });
    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.language) {
            try {

                ArrayList<String> languageList = new ArrayList<String>();

                for (String language : videoLanguage) {
                    languageList.add(language);
                }

                final ArrayAdapter<String> languageAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, languageList);

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                builder.setTitle("Select Language");

                builder.setAdapter(languageAdapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String selected_language = languageAdapter.getItem(which);
                        language.setText(selected_language);
                        dialog.dismiss();

                        videoView.stopPlayback();
                        progressBar.setVisibility(View.VISIBLE);
                        if (topic.equalsIgnoreCase("topic")) {
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
            videoView.pause();
            long seconds = TimeUnit.MILLISECONDS.toSeconds(videoView.getCurrentPosition());
            updateUnderstood(String.valueOf(seconds));
        }

        if (view.getId() == R.id.not_understood) {
            videoView.pause();
            long seconds = TimeUnit.MILLISECONDS.toSeconds(videoView.getCurrentPosition());
            updateNotUnderstood(String.valueOf(seconds));
        }

        if (view.getId() == R.id.question_understand) {
            videoView.pause();
            long seconds = TimeUnit.MILLISECONDS.toSeconds(videoView.getCurrentPosition());
            updateUnderstood(String.valueOf(seconds));
        }

        if (view.getId() == R.id.query) {
            videoView.pause();
            long seconds = TimeUnit.MILLISECONDS.toSeconds(videoView.getCurrentPosition());
            Intent intent = new Intent(getActivity(), QueryActivity.class);
            intent.putExtra("video_id", videoId);
            intent.putExtra("parentvideo_id", parentVideoId);
            intent.putExtra("video_time", String.valueOf(seconds));
            startActivity(intent);
        }

        if (view.getId() == R.id.chat_me) {
            videoView.pause();
            updateChatMe();
        }

        if (view.getId() == R.id.call_me) {
            videoView.pause();
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
        StringRequest req = new StringRequest(com.android.volley.Request.Method.POST, url, new Response.Listener<String>() {
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

                        for (int i = 0; i < videoLanguageArray.length(); i++) {
                            JSONObject object = videoLanguageArray.getJSONObject(i);
                            videoLanguage[i] = object.getString("lang_name");
                        }

                        topicVideo = video.getString("video").replace(" ", "%20");
                        videoId = video.getInt("id");
                        String video_lang = video.getString("language_name");

                        language.setText(video_lang);
                        //videoView.setVideoPath(Constant.VIDEO_PATH + topicVideo);
                        videoView.setVideoURI(Uri.parse(Constant.VIDEO_PATH + topicVideo));
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
                params.put("authorization", "Bearer " + SharedPrefrenceObj.getSharedValue(getActivity(), "auth_token"));
                params.put("content-type", "application/x-www-form-urlencoded");
                Log.d("params..", params.toString());
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
        MaintainRequestQueue.getInstance(getActivity()).addToRequestQueue(req, "tag");
    }

    private void getVideoForQuestion(final String selectedLanguage) {

        String url = Constant.URL + "videoDetailsForQuestionary";
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
                        JSONObject video = dataJsonObject.getJSONObject("list");
                        JSONArray videoLanguageArray = dataJsonObject.getJSONArray("video_languages");

                        videoLanguage = new String[videoLanguageArray.length()];

                        for (int i = 0; i < videoLanguageArray.length(); i++) {
                            JSONObject object = videoLanguageArray.getJSONObject(i);
                            videoLanguage[i] = object.getString("lang_name");
                        }

                        topicVideo = video.getString("video").replace(" ", "%20");
                        videoId = video.getInt("id");
                        String video_lang = video.getString("language_name");

                        language.setText(video_lang);
                        videoView.setVideoPath(Constant.VIDEO_PATH + topicVideo);
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
                params.put("authorization", "Bearer " + SharedPrefrenceObj.getSharedValue(getActivity(), "auth_token"));
                params.put("content-type", "application/x-www-form-urlencoded");
                Log.d("params..", params.toString());
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
        MaintainRequestQueue.getInstance(getActivity()).addToRequestQueue(req, "tag");
    }

    private void updateUnderstood(final String video_time) {

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
                    Toast.makeText(getActivity(), "Thank you for your response..", Toast.LENGTH_LONG).show();
                    TopicWiseFragment topicWiseFragment = new TopicWiseFragment();
                    Bundle args = new Bundle();
                    args.putString("topic", topic);
                    topicWiseFragment.setArguments(args);
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.replace(R.id.frame_container, topicWiseFragment);
                    transaction.commit();
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
                params.put("authorization", "Bearer " + SharedPrefrenceObj.getSharedValue(getActivity(), "auth_token"));
                params.put("content-type", "application/x-www-form-urlencoded");
                Log.d("params..", params.toString());
                return params;
            }

            //video_id,user_id,sub_query,query_type
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("video_id", String.valueOf(videoId));
                params.put("user_id", String.valueOf(SharedPrefrenceObj.getIntegerval(getActivity(), "id")));
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
        MaintainRequestQueue.getInstance(getActivity()).addToRequestQueue(req, "tag");
    }

    private void updateNotUnderstood(final String video_time) {

        String url = Constant.URL + "update_VideoHistory";
        url = url.replace(" ", "%20");
        url = url.replace("\n", "%0A");
        Log.e("User Name", "" + url);
        progressDialog.show();
        progressDialog.setMessage("updating data...");
        StringRequest req = new StringRequest(com.android.volley.Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Response", response);
                try {
                    Toast.makeText(getActivity(), "Thank you for your response..", Toast.LENGTH_LONG).show();
                    TopicWiseFragment topicWiseFragment = new TopicWiseFragment();
                    Bundle args = new Bundle();
                    args.putString("topic", topic);
                    topicWiseFragment.setArguments(args);
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.replace(R.id.frame_container, topicWiseFragment);
                    transaction.commit();
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
                params.put("authorization", "Bearer " + SharedPrefrenceObj.getSharedValue(getActivity(), "auth_token"));
                params.put("content-type", "application/x-www-form-urlencoded");
                Log.d("params..", params.toString());
                return params;
            }

            //video_id,user_id,sub_query,query_type
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("video_id", String.valueOf(videoId));
                params.put("user_id", String.valueOf(SharedPrefrenceObj.getIntegerval(getActivity(), "id")));
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
        MaintainRequestQueue.getInstance(getActivity()).addToRequestQueue(req, "tag");
    }

    private void updateChatMe() {

        String url = Constant.URL + "chatMe";
        url = url.replace(" ", "%20");
        url = url.replace("\n", "%0A");
        Log.e("User Name", "" + url);
        progressDialog.show();
        progressDialog.setMessage("updating data...");
        StringRequest req = new StringRequest(com.android.volley.Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Response", response);
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.optBoolean("success")) {

                        PackageManager packageManager = getActivity().getPackageManager();
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
            public Request.Priority getPriority() {
                return Request.Priority.IMMEDIATE;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("authorization", "Bearer " + SharedPrefrenceObj.getSharedValue(getActivity(), "auth_token"));
                params.put("content-type", "application/x-www-form-urlencoded");
                Log.d("params..", params.toString());
                return params;
            }

            //video_id,user_id,sub_query,query_type
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("video_id", String.valueOf(videoId));
                params.put("user_id", String.valueOf(SharedPrefrenceObj.getIntegerval(getActivity(), "id")));
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
        MaintainRequestQueue.getInstance(getActivity()).addToRequestQueue(req, "tag");
    }

    private void updateCallMe() {

        String url = Constant.URL + "callMe";
        url = url.replace(" ", "%20");
        url = url.replace("\n", "%0A");
        Log.e("User Name", "" + url);
        progressDialog.show();
        progressDialog.setMessage("updating data...");
        StringRequest req = new StringRequest(com.android.volley.Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Response", response);
                try {
                    progressDialog.dismiss();
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.optBoolean("success")) {
                        new AlertDialog.Builder(getActivity())
                                .setTitle("")
                                .setMessage("Our concern person will call you shortly")
                                .setCancelable(false)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        TopicWiseFragment topicWiseFragment = new TopicWiseFragment();
                                        Bundle args = new Bundle();
                                        args.putString("topic", topic);
                                        topicWiseFragment.setArguments(args);
                                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                        FragmentTransaction transaction = fragmentManager.beginTransaction();
                                        transaction.replace(R.id.frame_container, topicWiseFragment);
                                        transaction.commit();
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
            public Request.Priority getPriority() {
                return Request.Priority.IMMEDIATE;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("authorization", "Bearer " + SharedPrefrenceObj.getSharedValue(getActivity(), "auth_token"));
                params.put("content-type", "application/x-www-form-urlencoded");
                Log.d("params..", params.toString());
                return params;
            }

            //video_id,user_id,sub_query,query_type
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("video_id", String.valueOf(videoId));
                params.put("user_id", String.valueOf(SharedPrefrenceObj.getIntegerval(getActivity(), "id")));
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
        MaintainRequestQueue.getInstance(getActivity()).addToRequestQueue(req, "tag");
    }

    private void updatePDF(final int videoId, final String ppt) {

        String url = Constant.URL + "update_pdfvideo";
        url = url.replace(" ", "%20");
        url = url.replace("\n", "%0A");
        Log.e("User Name", "" + url);
        progressDialog.show();
        progressDialog.setMessage("Opening PDF.....");
        StringRequest req = new StringRequest(com.android.volley.Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Response", response);
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(Constant.PPT_PATH + ppt.trim()));
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
                params.put("authorization", "Bearer " + SharedPrefrenceObj.getSharedValue(getActivity(), "auth_token"));
                params.put("content-type", "application/x-www-form-urlencoded");
                Log.d("params..", params.toString());
                return params;
            }

            //video_id,user_id,sub_query,query_type
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("video_id", String.valueOf(videoId));
                params.put("user_id", String.valueOf(SharedPrefrenceObj.getIntegerval(getActivity(), "id")));
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
        MaintainRequestQueue.getInstance(getActivity()).addToRequestQueue(req, "tag");
    }
}
