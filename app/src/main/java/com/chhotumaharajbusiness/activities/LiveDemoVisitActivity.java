package com.chhotumaharajbusiness.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
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
import com.chhotumaharajbusiness.utility.AvenuesParams;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class LiveDemoVisitActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    Spinner live_demo_type,demo_location,demo_slot;
    EditText date;
    Button submit;
    ArrayList<String> demoTypeArray,locationArray,locationArray1,slotArray;
    private int mYear, mMonth, mDay;
    String visitLocation,visitDate,visitTime;
    ProgressDialog progressDialog;
    LinearLayout amount_layout;
    String domVisit;
    String amount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_demo_visit);

        getSupportActionBar().hide();
        progressDialog = new ProgressDialog(LiveDemoVisitActivity.this);

        live_demo_type = findViewById(R.id.live_demo_type);
        demo_location  = findViewById(R.id.live_demo_location);
        demo_slot      = findViewById(R.id.live_demo_slot);
        date           = findViewById(R.id.booking_date);
        submit         = findViewById(R.id.appointment_submit);
        amount_layout  = findViewById(R.id.amount_layout);

        live_demo_type.setOnItemSelectedListener(this);
        demo_location.setOnItemSelectedListener(this);
        demo_slot.setOnItemSelectedListener(this);
        submit.setOnClickListener(this);

        demoTypeArray = new ArrayList<>();
        demoTypeArray.add("Select Live Demo Type");
        demoTypeArray.add("Exclusive Demo");
        demoTypeArray.add("Non Exclusive Demo");

        locationArray = new ArrayList<>();
        locationArray.add("Select Location");
        locationArray.add("Ahmadabad- Experience Center");
        locationArray.add("Ahmadabad- Thaltej");
        locationArray.add("Mumbai Head Office");

        locationArray1 = new ArrayList<>();
        locationArray1.add("Select Location");
        locationArray1.add("Ahmadabad- Thaltej");

        slotArray = new ArrayList<>();
        slotArray.add("Select Time Slot");
        slotArray.add("10:00 AM to 11:00 AM");
        slotArray.add("11:00 AM to 12:00 PM");
        slotArray.add("12:00 PM to 1:00 PM");
        slotArray.add("1:00 PM to 2:00 PM");
        slotArray.add("2:00 PM to 3:00 PM");
        slotArray.add("3:00 PM to 4:00 PM");
        slotArray.add("4:00 PM to 5:00 PM");
        slotArray.add("5:00 PM to 6:00 PM");
        slotArray.add("6:00 PM to 7:00 PM");
        slotArray.add("7:00 PM to 8:00 PM");

        date.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                c.add(Calendar.DAY_OF_MONTH,2);
                DatePickerDialog datePickerDialog = new DatePickerDialog(LiveDemoVisitActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                String monthString = String.valueOf((monthOfYear + 1));
                                if (monthString.length() == 1) {
                                    monthString = "0" + monthString;
                                }
                                String dayMonth = String.valueOf(dayOfMonth);
                                if (dayMonth.length() == 1) {
                                    dayMonth = "0" + dayMonth;
                                }
                                date.setText(year + "-" + monthString + "-" + dayMonth);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
                DatePicker datePicker = datePickerDialog.getDatePicker();
                datePicker.setMinDate(c.getTimeInMillis());
            }
        });

        ArrayAdapter<String> demotype = new ArrayAdapter<String>(LiveDemoVisitActivity.this, R.layout.spinner_text_layout, demoTypeArray);
        live_demo_type.setAdapter(demotype);
        demotype.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        live_demo_type.setAdapter(demotype);


        ArrayAdapter<String> location1 = new ArrayAdapter<String>(LiveDemoVisitActivity.this, R.layout.spinner_text_layout, locationArray1);
        demo_location.setAdapter(location1);
        location1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        demo_location.setAdapter(location1);

        ArrayAdapter<String> slot = new ArrayAdapter<String>(LiveDemoVisitActivity.this, R.layout.spinner_text_layout, slotArray);
        demo_slot.setAdapter(slot);
        slot.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        demo_slot.setAdapter(slot);

    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        switch (adapterView.getId()){

            case R.id.live_demo_type:
                if(adapterView.getSelectedItem().toString().equalsIgnoreCase("Exclusive Demo")){
                    ArrayAdapter<String> location = new ArrayAdapter<String>(LiveDemoVisitActivity.this, R.layout.spinner_text_layout, locationArray);
                    demo_location.setAdapter(location);
                    location.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    demo_location.setAdapter(location);
                    amount_layout.setVisibility(View.VISIBLE);
                    domVisit = "1";
                    amount = "10000";
                }
                else {
                    ArrayAdapter<String> location = new ArrayAdapter<String>(LiveDemoVisitActivity.this, R.layout.spinner_text_layout, locationArray1);
                    demo_location.setAdapter(location);
                    location.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    demo_location.setAdapter(location);
                    amount_layout.setVisibility(View.GONE);
                    domVisit = "2";
                    amount = "";
                }
                break;

            case R.id.live_demo_location:
                visitLocation = adapterView.getSelectedItem().toString();

                break;

            case R.id.live_demo_slot:
                visitTime = adapterView.getSelectedItem().toString();
                break;
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private void liveVisitSubmit(final String visitDate) {

        String url = Constant.URL + "save_Appointment";
        url = url.replace(" ", "%20");
        url = url.replace("\n", "%0A");
        Log.e("User Name", "" + url);

        progressDialog.show();
        progressDialog.setMessage("Booking appointment.....");

        StringRequest req = new StringRequest(com.android.volley.Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Response", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.optBoolean("success")) {
                        JSONObject data = jsonObject.getJSONObject("data");
                        int orderId = data.getInt("order_id");

                        String vAccessCode = "AVHI88GI06CL46IHLC";
                        String vMerchantId = "187245";
                        String vCurrency = "INR";
                        String vAmount = "10000";
                        if(!vAccessCode.equals("") && !vMerchantId.equals("") && !vCurrency.equals("") && !vAmount.equals("")) {
                            Intent intent = new Intent(LiveDemoVisitActivity.this, WebViewActivity.class);
                            intent.putExtra(AvenuesParams.ACCESS_CODE, vAccessCode);
                            intent.putExtra(AvenuesParams.MERCHANT_ID, vMerchantId);
                            intent.putExtra(AvenuesParams.ORDER_ID, String.valueOf(orderId));
                            intent.putExtra(AvenuesParams.CURRENCY, vCurrency);
                            intent.putExtra(AvenuesParams.AMOUNT, vAmount);
                            intent.putExtra(AvenuesParams.BILLING_NAME, SharedPrefrenceObj.getSharedValue(LiveDemoVisitActivity.this, "name"));
                            intent.putExtra(AvenuesParams.BILLING_EMAIL, SharedPrefrenceObj.getSharedValue(LiveDemoVisitActivity.this, "email"));
                            intent.putExtra(AvenuesParams.BILLING_TEL, SharedPrefrenceObj.getSharedValue(LiveDemoVisitActivity.this, "mobile"));

                            intent.putExtra(AvenuesParams.REDIRECT_URL, "http://chhotumaharajb2b.com/api/payment_Response");
                            intent.putExtra(AvenuesParams.CANCEL_URL, "http://chhotumaharajb2b.com/api/payment_Response");
                            intent.putExtra(AvenuesParams.RSA_KEY_URL, "https://test.ccavenue.com/transaction/jsp/GetRSA.jsp");

                            startActivity(intent);
                        }

                    }
                 /*   Toast.makeText(LiveDemoVisitActivity.this,"Appointment booked successfully",Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(LiveDemoVisitActivity.this, InterestedActivity.class);
                    intent.putExtra("concept","appointment");
                    startActivity(intent);
                    finish();*/
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
                params.put("authorization","Bearer "+ SharedPrefrenceObj.getSharedValue(LiveDemoVisitActivity.this,"auth_token"));
                params.put("content-type","application/x-www-form-urlencoded");
                Log.d("params..",params.toString());
                return params;
            }
            //appointment_date,user_id,sub_query,query_type,timeslot,location
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("appointment_date", visitDate);
                params.put("user_id", String.valueOf(SharedPrefrenceObj.getIntegerval(LiveDemoVisitActivity.this,"id")));
                params.put("dom_visit",domVisit);
                params.put("amount",amount);
                params.put("timeslot", visitTime);
                if(visitLocation.equalsIgnoreCase("Mumbai Head Office")){
                    params.put("location","1");
                }
                else if(visitLocation.equalsIgnoreCase("Ahmadabad- Experience Center")){
                    params.put("location","2");
                }
                else {
                    params.put("location","3");
                }

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

        switch (view.getId()){

            case R.id.appointment_submit:
                visitDate = date.getText().toString();
                if(live_demo_type.getSelectedItem().toString().equalsIgnoreCase("Select Live Demo Type")){
                    Toast.makeText(LiveDemoVisitActivity.this,"Select live demo type",Toast.LENGTH_SHORT).show();

                }
                else if(demo_location.getSelectedItem().toString().equalsIgnoreCase("Select Location")){
                    Toast.makeText(LiveDemoVisitActivity.this,"Select location",Toast.LENGTH_SHORT).show();
                }
                else if(date.getText().toString().equalsIgnoreCase("")){
                    Toast.makeText(LiveDemoVisitActivity.this,"Select date",Toast.LENGTH_SHORT).show();
                }
                else if(demo_slot.getSelectedItem().toString().equalsIgnoreCase("Select Time Slot")){
                    Toast.makeText(LiveDemoVisitActivity.this,"Select time slot",Toast.LENGTH_SHORT).show();

                }else {
                    liveVisitSubmit(visitDate);
                }

                break;

                default:
                    break;
        }
    }
}