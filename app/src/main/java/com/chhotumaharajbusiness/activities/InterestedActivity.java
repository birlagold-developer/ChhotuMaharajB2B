package com.chhotumaharajbusiness.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.chhotumaharajbusiness.R;

public class InterestedActivity extends AppCompatActivity {

    TextView interested_or_not, interset_txt, appointment_txt;
    String interset;
    Button continueBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interested);

        getSupportActionBar().hide();

        interset = getIntent().getStringExtra("concept");

        TextView tv = findViewById(R.id.chottu_txt);
        TextView tv1 = findViewById(R.id.maharaj_txt);
        Typeface face = Typeface.createFromAsset(getAssets(), "cooperblackstd.otf");

        tv.setTypeface(face);
        tv1.setTypeface(face);

        interested_or_not = findViewById(R.id.interested_or_not);
        interset_txt = findViewById(R.id.interested_txt);
        continueBtn = findViewById(R.id.interested_continue);
        appointment_txt = findViewById(R.id.appontment_done_txt);

        if (interset.equalsIgnoreCase("not_interested")) {
            interested_or_not.setText("Not Interested");
            interset_txt.setText("For interacting with us.");
        } else if (interset.equalsIgnoreCase("appointment")) {
            interested_or_not.setText("");
            interset_txt.setText("For Showing Your Interest");
            appointment_txt.setText("We Will Connect & confirm Your Appointment Shortly.");
            continueBtn.setVisibility(View.VISIBLE);
        } else {
            interested_or_not.setText("");
            interset_txt.setText("For Showing Your Interest");
            continueBtn.setVisibility(View.VISIBLE);
        }

        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InterestedActivity.this, NavigationActivity.class);
                intent.putExtra("query_page", "0");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });


    }

}
