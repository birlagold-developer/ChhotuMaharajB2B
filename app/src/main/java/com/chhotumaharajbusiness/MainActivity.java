package com.chhotumaharajbusiness;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.chhotumaharajbusiness.activities.LiveDemoVisitActivity;
import com.chhotumaharajbusiness.activities.TopicWiseActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    LinearLayout linearLayout,linearLayoutl;
    Button view_more,topic_wise,question_wise,book_appointment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();
        linearLayout = findViewById(R.id.layout);
        linearLayoutl = findViewById(R.id.layout1);

        view_more = findViewById(R.id.view_more);
        topic_wise = findViewById(R.id.topic_wise);
        question_wise = findViewById(R.id.question_wise);
        book_appointment = findViewById(R.id.book_appointment);

        view_more.setOnClickListener(this);
        topic_wise.setOnClickListener(this);
        question_wise.setOnClickListener(this);
        book_appointment.setOnClickListener(this);




    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.view_more:
                linearLayout.setVisibility(View.GONE);
                linearLayoutl.setVisibility(View.VISIBLE);
                break;

            case R.id.topic_wise:
                Intent intent = new Intent(MainActivity.this, TopicWiseActivity.class);
                intent.putExtra("topic","topic");
                startActivity(intent);
                break;

            case R.id.question_wise:
                Intent intent1 = new Intent(MainActivity.this, TopicWiseActivity.class);
                intent1.putExtra("topic","question");
                startActivity(intent1);
                break;

            case R.id.book_appointment:
                Intent intent2 = new Intent(MainActivity.this, LiveDemoVisitActivity.class);
                intent2.putExtra("topic","question");
                startActivity(intent2);
                break;
        }
    }
}
