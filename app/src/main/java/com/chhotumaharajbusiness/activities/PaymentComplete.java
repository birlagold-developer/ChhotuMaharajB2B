package com.chhotumaharajbusiness.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Animatable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.chhotumaharajbusiness.R;

public class PaymentComplete extends AppCompatActivity {

    private ImageView checkView;
    private ImageView crossView;
    View view;
    TextView bookingid;
    ImageView circle;
    Button paymentbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_complete);

        checkView = findViewById(R.id.check);
        crossView = findViewById(R.id.cross);
        bookingid = findViewById(R.id.booking_id);
        circle = findViewById(R.id.btn_capture_photo);
        paymentbtn = findViewById(R.id.payment_finish);


        view = new View(this);

        Window window = PaymentComplete.this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(PaymentComplete.this, R.color.colorWhite));

        int Id = getIntent().getIntExtra("ID", 0);
        String status = getIntent().getStringExtra("status");

        if (status.equalsIgnoreCase("Success")) {
            bookingid.setText("Thank You for booking your Booking Id is " + Id);
            Typeface face = Typeface.createFromAsset(getAssets(), "cooperblackstd.otf");
            bookingid.setTypeface(face);
            checkAnimation(view);
            circle.setImageResource(R.drawable.border_circle);
        } else {
            crossAnimation(view);
            circle.setImageResource(R.drawable.border_circle1);
            bookingid.setText("Sorry booking is not completed due to " + status);
            Typeface face = Typeface.createFromAsset(getAssets(), "cooperblackstd.otf");
            bookingid.setTypeface(face);
        }


        paymentbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), NavigationActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("query_page", "0");
                startActivity(intent);
                finish();
            }
        });


    }

    public void checkAnimation(View v) {
        crossView.setVisibility(View.INVISIBLE);
        checkView.setVisibility(View.VISIBLE);
        ((Animatable) checkView.getDrawable()).start();

    }

    public void crossAnimation(View v) {
        crossView.setVisibility(View.VISIBLE);
        checkView.setVisibility(View.INVISIBLE);
        ((Animatable) crossView.getDrawable()).start();

    }

    @Override
    public void onBackPressed() {
    }
}
