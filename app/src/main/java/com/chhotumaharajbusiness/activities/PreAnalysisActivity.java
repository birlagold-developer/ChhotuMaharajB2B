package com.chhotumaharajbusiness.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.chhotumaharajbusiness.MainActivity;
import com.chhotumaharajbusiness.R;
import com.chhotumaharajbusiness.constant.SharedPrefrenceObj;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.util.ArrayList;

public class PreAnalysisActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    ArrayList<String> qualfArray,professionArray,landArray,typeArray,sizeArray;
    Button pre_analysis_next;
    EditText name,age,state,city;
    String qualification,professionVal,landVal,landType,landSize;
    SearchableSpinner pre_analysis_qlf,pre_analysis_profession,pre_analysis_land,pre_analysis_type,pre_analysis_size;
    View view1,view2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_analysis);

        getSupportActionBar().hide();

        TextView tv =  findViewById(R.id.chottu_txt);
        TextView tv1 = findViewById(R.id.maharaj_txt);
        Typeface face = Typeface.createFromAsset(getAssets(), "cooperblackstd.otf");

        tv.setTypeface(face);
        tv1.setTypeface(face);

        name = findViewById(R.id.pre_analysis_name);
        age = findViewById(R.id.pre_analysis_age);
        state = findViewById(R.id.pre_analysis_state);
        city = findViewById(R.id.pre_analysis_city);
        pre_analysis_qlf = findViewById(R.id.pre_analysis_qualification);
        pre_analysis_profession = findViewById(R.id.pre_analysis_profession);
        pre_analysis_land = findViewById(R.id.pre_analysis_land);
        pre_analysis_type = findViewById(R.id.pre_analysis_landtype);
        pre_analysis_size = findViewById(R.id.pre_analysis_landsize);
        pre_analysis_next = findViewById(R.id.pre_analysis_next);
        view1 = findViewById(R.id.view1);
        view2 = findViewById(R.id.view2);

        pre_analysis_next.setOnClickListener(this);

        name.setEnabled(false);
        state.setEnabled(false);
        city.setEnabled(false);

        if(SharedPrefrenceObj.getIntegerval(PreAnalysisActivity.this,"step")==3){
            Intent intent = new Intent(PreAnalysisActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        try{
            if(SharedPrefrenceObj.getSharedValue(PreAnalysisActivity.this,"name")!=null){
                name.setText(SharedPrefrenceObj.getSharedValue(PreAnalysisActivity.this,"name"));
            }
            else {
                name.setText("");
            }
            if(SharedPrefrenceObj.getSharedValue(PreAnalysisActivity.this,"state")!=null){
                state.setText(SharedPrefrenceObj.getSharedValue(PreAnalysisActivity.this,"state"));
            }
            else {
                state.setText("");
            }
            if(SharedPrefrenceObj.getSharedValue(PreAnalysisActivity.this,"city")!=null){
                city.setText(SharedPrefrenceObj.getSharedValue(PreAnalysisActivity.this,"city"));
            }
            else {
                city.setText("");
            }

        }catch (Exception e){

        }

        pre_analysis_qlf.setTitle("");
        pre_analysis_profession.setTitle("");
        pre_analysis_land.setTitle("");
        pre_analysis_type.setTitle("");
        pre_analysis_size.setTitle("");



        qualfArray = new ArrayList<>();
        qualfArray.add("Select Qualification");
        qualfArray.add("Under Graduate");
        qualfArray.add("Graduate");
        qualfArray.add("Post Graduate");

        professionArray = new ArrayList<>();
        professionArray.add("Select Profession");
        professionArray.add("Business");
        professionArray.add("Pvt. Service");
        professionArray.add("Govt. Service");
        professionArray.add("Student");

        landArray = new ArrayList<>();
        landArray.add("Select Land");
        landArray.add("Owned");
        landArray.add("To Be Taken On Lease/Rent");

        typeArray = new ArrayList<>();
        typeArray.add("Select Land Type");
        typeArray.add("Commercial");
        typeArray.add("Non Agriculture");
        typeArray.add("Agriculture");

        sizeArray = new ArrayList<>();
        sizeArray.add("Select Land Size");
        sizeArray.add("2000 to 3000 Sq. Ft.");
        sizeArray.add("3000 to 5000 Sq. Ft.");
        sizeArray.add("5000 to 10000 Sq. Ft.");
        sizeArray.add("Above 10000 Sq. Ft.");




        ArrayAdapter<String> categoryAdap = new ArrayAdapter<String>(PreAnalysisActivity.this, R.layout.spinner_text_layout, qualfArray);
        pre_analysis_qlf.setAdapter(categoryAdap);
        categoryAdap.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pre_analysis_qlf.setAdapter(categoryAdap);

        ArrayAdapter<String> profession = new ArrayAdapter<String>(PreAnalysisActivity.this, R.layout.spinner_text_layout, professionArray);
        pre_analysis_profession.setAdapter(profession);
        profession.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pre_analysis_profession.setAdapter(profession);

        final ArrayAdapter<String> land = new ArrayAdapter<String>(PreAnalysisActivity.this, R.layout.spinner_text_layout, landArray);
        pre_analysis_land.setAdapter(land);
        land.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pre_analysis_land.setAdapter(land);

        final ArrayAdapter<String> landtype = new ArrayAdapter<String>(PreAnalysisActivity.this, R.layout.spinner_text_layout, typeArray);
        pre_analysis_type.setAdapter(landtype);
        landtype.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pre_analysis_type.setAdapter(landtype);

        ArrayAdapter<String> landsize = new ArrayAdapter<String>(PreAnalysisActivity.this, R.layout.spinner_text_layout, sizeArray);
        pre_analysis_size.setAdapter(landsize);
        landsize.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pre_analysis_size.setAdapter(landsize);

        pre_analysis_qlf.setOnItemSelectedListener(this);
        pre_analysis_profession.setOnItemSelectedListener(this);
        pre_analysis_land.setOnItemSelectedListener(this);
        pre_analysis_type.setOnItemSelectedListener(this);
        pre_analysis_size.setOnItemSelectedListener(this);



    }

    @Override
    public void onClick(View view) {

        String Name = name.getText().toString();
        String Age = age.getText().toString();
        String State = state.getText().toString();
        String City = city.getText().toString();

        if(name.getText().toString().equalsIgnoreCase("")){
            name.setError("Invalid name");
          //  Toast.makeText(PreAnalysisActivity.this,"Invalid name",Toast.LENGTH_SHORT).show();
        }
        else if(age.getText().toString().equalsIgnoreCase("")){
            age.setError("Invalid age");
        }
        else if(state.getText().toString().equalsIgnoreCase("")){
            state.setError("Invalid state");
           // Toast.makeText(PreAnalysisActivity.this,"Invalid state",Toast.LENGTH_SHORT).show();
        }
        else if(city.getText().toString().equalsIgnoreCase("")){
            city.setError("Invalid city");
           // Toast.makeText(PreAnalysisActivity.this,"Invalid city",Toast.LENGTH_SHORT).show();
        }

        else if(pre_analysis_qlf.getSelectedItem().toString().equalsIgnoreCase("Select Qualification")){
            TextView errorText = (TextView)pre_analysis_qlf.getSelectedView();
            errorText.setError("");
            errorText.setTextColor(Color.RED);
            errorText.setText("Select Qualification");
         //   Toast.makeText(PreAnalysisActivity.this,"Select Qualification",Toast.LENGTH_SHORT).show();

        }
        else if(pre_analysis_profession.getSelectedItem().toString().equalsIgnoreCase("Select Profession")){
            TextView errorText = (TextView)pre_analysis_profession.getSelectedView();
            errorText.setError("");
            errorText.setTextColor(Color.RED);//just to highlight that this is an error
            errorText.setText("Select Profession");
         //   Toast.makeText(PreAnalysisActivity.this,"Select Profession",Toast.LENGTH_SHORT).show();

        }
        else if(pre_analysis_land.getSelectedItem().toString().equalsIgnoreCase("Select Land")){
            TextView errorText = (TextView)pre_analysis_land.getSelectedView();
            errorText.setError("");
            errorText.setTextColor(Color.RED);//just to highlight that this is an error
            errorText.setText("Select Land");
         //   Toast.makeText(PreAnalysisActivity.this,"Select Land",Toast.LENGTH_SHORT).show();

        }
        else if(pre_analysis_land.getSelectedItem().toString().equalsIgnoreCase("Owned")){
            if(pre_analysis_type.getSelectedItem().toString().equalsIgnoreCase("Select Land Type")){
                TextView errorText = (TextView)pre_analysis_type.getSelectedView();
                errorText.setError("");
                errorText.setTextColor(Color.RED);//just to highlight that this is an error
                errorText.setText("Select Land Type");
                // Toast.makeText(PreAnalysisActivity.this,"Select Land Type",Toast.LENGTH_SHORT).show();

            }
            else if(pre_analysis_size.getSelectedItem().toString().equalsIgnoreCase("Select Land Size")){
                TextView errorText = (TextView)pre_analysis_size.getSelectedView();
                errorText.setError("");
                errorText.setTextColor(Color.RED);//just to highlight that this is an error
                errorText.setText("Select Land Size");
                //   Toast.makeText(PreAnalysisActivity.this,"Select Land Size",Toast.LENGTH_SHORT).show();
            }
            else {
                Intent intent = new Intent(PreAnalysisActivity.this,PreAnalysisActivity2.class);
                intent.putExtra("name",Name);
                intent.putExtra("age", Age);
                intent.putExtra("state",State);
                intent.putExtra("city",City);
                intent.putExtra("qualification",qualification);
                intent.putExtra("profession",professionVal);
                intent.putExtra("land",landVal);
                intent.putExtra("land_type",landType);
                intent.putExtra("land_size",landSize);
                startActivity(intent);
            }
        }

        else {
            Intent intent = new Intent(PreAnalysisActivity.this,PreAnalysisActivity2.class);
            intent.putExtra("name",Name);
            intent.putExtra("age", Age);
            intent.putExtra("state",State);
            intent.putExtra("city",City);
            intent.putExtra("qualification",qualification);
            intent.putExtra("profession",professionVal);
            intent.putExtra("land",landVal);
            intent.putExtra("land_type",landType);
            intent.putExtra("land_size",landSize);
            startActivity(intent);
        }


    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getId()){
            case R.id.pre_analysis_qualification:
                qualification = String.valueOf(adapterView.getSelectedItem());
                break;
            case R.id.pre_analysis_profession:
                professionVal = String.valueOf(adapterView.getSelectedItem());
                break;
            case R.id.pre_analysis_land:
                if(pre_analysis_land.getSelectedItem().toString().equalsIgnoreCase("Owned")){
                    pre_analysis_type.setVisibility(View.VISIBLE);
                    pre_analysis_size.setVisibility(View.VISIBLE);
                    view1.setVisibility(View.VISIBLE);
                    view2.setVisibility(View.VISIBLE);
                }
                else {
                    pre_analysis_type.setVisibility(View.GONE);
                    pre_analysis_size.setVisibility(View.GONE);
                    view1.setVisibility(View.GONE);
                    view2.setVisibility(View.GONE);
                }
                landVal = String.valueOf(adapterView.getSelectedItem());
                break;
            case R.id.pre_analysis_landtype:

                landType = String.valueOf(adapterView.getSelectedItem());
                break;
            case R.id.pre_analysis_landsize:

                landSize = String.valueOf(adapterView.getSelectedItem());
                break;
                default:
                    break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
