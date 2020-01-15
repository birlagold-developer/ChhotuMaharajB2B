package com.chhotumaharajbusiness.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Html;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.chhotumaharajbusiness.R;
import com.chhotumaharajbusiness.constant.SharedPrefrenceObj;
import com.google.android.material.textfield.TextInputLayout;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.util.ArrayList;
import java.util.Calendar;

public class PreAnalysisNewActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private EditText editTextQ1, editTextQ2, editTextQ3, editTextQ4, editTextQ5, editTextQ8, editTextQ9, editTextQ10, editTextQ11, editTextQ18, editTextQ23, editTextQ24;
    private TextInputLayout textInputLayoutQ5;

    private Spinner spinnerQ6, spinnerQ7, spinnerQ12, spinnerQ13, spinnerQ14, spinnerQ15, spinnerQ16, spinnerQ17, spinnerQ19, spinnerQ20, spinnerQ21, spinnerQ22;

    private Button pre_analysis_submit;

    private String[] dropDownLandStatus = new String[]{"Land status?", "Owned", "To Be Taken On Lease/Rent"};
    private String[] dropDownLandSize = new String[]{"Size of the land?", "2000 to 3000 Sq. Ft.", "3000 to 5000 Sq. Ft.", "5000 to 10000 Sq. Ft.", "Above 10000 Sq. Ft."};
    private String[] dropDownLandType = new String[]{"Type Of the land?", "Commercial", "Non Agriculture", "Agriculture"};
    private String[] dropDownLandLocation = new String[]{"About your land location?", ""};
    private String[] dropDownIndustryExperience = new String[]{"Do you have any experience of cinema or restaurant industry?", ""};
    private String[] dropDownDecisionMaker = new String[]{"Decision maker?", "Self", "Family Head", "Partner/s"};
    private String[] dropDownInvestment = new String[]{"Are you aware how much would be the investment?", "15 to 20 L", "21 to 35 L", "36 to 50 L", "51 to 75 L"};
    private String[] dropDownInvestmentType = new String[]{"Finance /Loan / Self Funding?", "Self / Family", "Partial Loan", "Fully Dependent On Loan"};
    private String[] dropDownOperaation = new String[]{"Who will look after daily day to day operation?", "Self", "Family Head", "Partner/s"};
    private String[] dropDownPropertyVisited = new String[]{"Have you visited our property?", ""};

    private DatePickerDialog datePickerDialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_analysis_new);

        getSupportActionBar().hide();

        TextView tv = findViewById(R.id.chottu_txt);
        TextView tv1 = findViewById(R.id.maharaj_txt);
        Typeface face = Typeface.createFromAsset(getAssets(), "cooperblackstd.otf");

        tv.setTypeface(face);
        tv1.setTypeface(face);

        editTextQ1 = findViewById(R.id.editTextQ1);
        editTextQ2 = findViewById(R.id.editTextQ2);
        editTextQ3 = findViewById(R.id.editTextQ3);
        editTextQ4 = findViewById(R.id.editTextQ4);
        editTextQ5 = findViewById(R.id.editTextQ5);
        editTextQ8 = findViewById(R.id.editTextQ8);
        editTextQ9 = findViewById(R.id.editTextQ9);
        editTextQ10 = findViewById(R.id.editTextQ10);
        editTextQ11 = findViewById(R.id.editTextQ11);
        editTextQ18 = findViewById(R.id.editTextQ18);
        editTextQ23 = findViewById(R.id.editTextQ23);
        editTextQ24 = findViewById(R.id.editTextQ24);

        textInputLayoutQ5 = findViewById(R.id.textInputLayoutQ5);

        spinnerQ6 = findViewById(R.id.spinnerQ6);
        spinnerQ7 = findViewById(R.id.spinnerQ7);
        spinnerQ12 = findViewById(R.id.spinnerQ12);
        spinnerQ13 = findViewById(R.id.spinnerQ13);
        spinnerQ14 = findViewById(R.id.spinnerQ14);
        spinnerQ15 = findViewById(R.id.spinnerQ15);
        spinnerQ16 = findViewById(R.id.spinnerQ16);
        spinnerQ17 = findViewById(R.id.spinnerQ17);
        spinnerQ19 = findViewById(R.id.spinnerQ19);
        spinnerQ20 = findViewById(R.id.spinnerQ20);
        spinnerQ21 = findViewById(R.id.spinnerQ21);
        spinnerQ22 = findViewById(R.id.spinnerQ22);

        pre_analysis_submit = findViewById(R.id.pre_analysis_submit);

        ArrayAdapter<String> adpQ6 = new ArrayAdapter<String>(PreAnalysisNewActivity.this, android.R.layout.simple_spinner_item, dropDownLandStatus);
        adpQ6.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerQ6.setAdapter(adpQ6);

        ArrayAdapter<String> adpQ7 = new ArrayAdapter<String>(PreAnalysisNewActivity.this, android.R.layout.simple_spinner_item, dropDownLandSize);
        adpQ7.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerQ7.setAdapter(adpQ7);

        ArrayAdapter<String> adpQ12 = new ArrayAdapter<String>(PreAnalysisNewActivity.this, android.R.layout.simple_spinner_item, dropDownLandType);
        adpQ12.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerQ12.setAdapter(adpQ12);

        ArrayAdapter<String> adpQ13 = new ArrayAdapter<String>(PreAnalysisNewActivity.this, android.R.layout.simple_spinner_item, dropDownLandLocation);
        adpQ13.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerQ13.setAdapter(adpQ13);

        ArrayAdapter<String> adpQ14 = new ArrayAdapter<String>(PreAnalysisNewActivity.this, android.R.layout.simple_spinner_item, dropDownIndustryExperience);
        adpQ14.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerQ14.setAdapter(adpQ14);

//        ArrayAdapter<String> adpQ15 = new ArrayAdapter<String>(PreAnalysisNewActivity.this, android.R.layout.simple_spinner_item, );
//        adpQ15.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinnerQ15.setAdapter(adpQ15);
//
//        ArrayAdapter<String> adpQ16 = new ArrayAdapter<String>(PreAnalysisNewActivity.this, android.R.layout.simple_spinner_item, );
//        adpQ16.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinnerQ16.setAdapter(adpQ16);

        ArrayAdapter<String> adpQ17 = new ArrayAdapter<String>(PreAnalysisNewActivity.this, android.R.layout.simple_spinner_item, dropDownDecisionMaker);
        adpQ17.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerQ17.setAdapter(adpQ17);

        ArrayAdapter<String> adpQ19 = new ArrayAdapter<String>(PreAnalysisNewActivity.this, android.R.layout.simple_spinner_item, dropDownInvestment);
        adpQ19.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerQ19.setAdapter(adpQ19);

        ArrayAdapter<String> adpQ20 = new ArrayAdapter<String>(PreAnalysisNewActivity.this, android.R.layout.simple_spinner_item, dropDownInvestmentType);
        adpQ20.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerQ20.setAdapter(adpQ20);


        ArrayAdapter<String> adpQ21 = new ArrayAdapter<String>(PreAnalysisNewActivity.this, android.R.layout.simple_spinner_item, dropDownOperaation);
        adpQ21.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerQ21.setAdapter(adpQ21);

        ArrayAdapter<String> adpQ22 = new ArrayAdapter<String>(PreAnalysisNewActivity.this, android.R.layout.simple_spinner_item, dropDownPropertyVisited);
        adpQ22.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerQ22.setAdapter(adpQ22);

        editTextQ5.setOnClickListener(dateListener);

        spinnerQ6.setOnItemSelectedListener(this);
        spinnerQ7.setOnItemSelectedListener(this);
        spinnerQ12.setOnItemSelectedListener(this);
        spinnerQ13.setOnItemSelectedListener(this);
        spinnerQ14.setOnItemSelectedListener(this);
        spinnerQ15.setOnItemSelectedListener(this);
        spinnerQ16.setOnItemSelectedListener(this);
        spinnerQ17.setOnItemSelectedListener(this);
        spinnerQ19.setOnItemSelectedListener(this);
        spinnerQ20.setOnItemSelectedListener(this);
        spinnerQ21.setOnItemSelectedListener(this);
        spinnerQ22.setOnItemSelectedListener(this);

        pre_analysis_submit.setOnClickListener(this);

//        if (SharedPrefrenceObj.getIntegerval(PreAnalysisNewActivity.this, "step") == 3) {
//            Intent intent = new Intent(PreAnalysisNewActivity.this, NavigationActivity.class);
//            startActivity(intent);
//            finish();
//        }

    }

    private View.OnClickListener dateListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            Calendar c = Calendar.getInstance();
            int mYear = c.get(Calendar.YEAR);
            int mMonth = c.get(Calendar.MONTH);
            int mDay = c.get(Calendar.DAY_OF_MONTH);
            c.add(Calendar.DAY_OF_MONTH, 2);
            if (datePickerDialog == null) {
                datePickerDialog = new DatePickerDialog(PreAnalysisNewActivity.this,
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
                                editTextQ5.setText(year + "-" + monthString + "-" + dayMonth);
                            }
                        }, mYear, mMonth, mDay);
            }

            DatePicker datePicker = datePickerDialog.getDatePicker();
            datePicker.setMinDate(c.getTimeInMillis());

            if (!datePickerDialog.isShowing()) {
                datePickerDialog.show();
            }

        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.pre_analysis_submit:
                System.out.println("spinnerQ6 selected text : "+spinnerQ6.getSelectedItem().toString());
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        TextView textView = (TextView) view;

        if (textView != null) {
            textView.setSingleLine(false);
            textView.setLineSpacing(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3.0f,  getResources().getDisplayMetrics()), 1.0f);

            String firstValue = (String) adapterView.getItemAtPosition(0);
            if (adapterView.getSelectedItemPosition() == 0) {
                textView.setText(firstValue);
            } else {
                textView.setText(Html.fromHtml(firstValue + "<font color='#ED3237'><br/>" + adapterView.getSelectedItem().toString()+"</font>"));
            }
        }

        switch (adapterView.getId()) {

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
