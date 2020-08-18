package com.example.finalproject;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import com.google.gson.Gson;

public class eBike extends AppCompatActivity {
    private Spinner dropDownBrand,dropDownModel,dropDownCondition;
    private Button BtnOk, BtnCancel;
    private static SharedPreferences preferences;
    private  InsuranceData insuranceDataeCar;
    private  keyboard  mykeyboard;
    private EditText editYear,editPrice;
    private MyReceiver receiver;
    private long timeStart,timeStartFirstLetterOP,timeStartFirstLetter;
    private int textCounter=0, textCounterOP=0, textsize=0, textsizeOP=0;
    private int counterDeletions=0;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ebike);
        insuranceDataeCar=new InsuranceData();
        timeStart=System.currentTimeMillis();
        receiver = new MyReceiver();
        IntentFilter intentFilter = new IntentFilter("com.example.broadcast.Acceleration");
        registerReceiver(receiver,intentFilter);
        startService(new Intent(this,AccelerationService.class));
        setBtn();
        //hide  android softkeyboard
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        setTextBox(); //set onTextChanged Method for measuring time between inputs
}

    @Override //thanks to https://medium.com/@suragch/how-touch-events-are-delivered-in-android-eee3b607b038
    //// intercept touch event before handled by keyboard & compute distance to button(from touch event)
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if(ev.getAction()==MotionEvent.ACTION_DOWN) {
            if (mykeyboard.getClostestDistance(ev.getX(), ev.getY()) != 0) // only add touch events if they are within 100 distance units
            {   insuranceDataeCar.addBtnPrecision(mykeyboard.getClostestDistance(ev.getX(), ev.getY()));
            Log.d(String.valueOf(mykeyboard.getClostestDistance(ev.getX(), ev.getY())),"addBike");
        }}
        return super.dispatchTouchEvent(ev);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void setTextBox() {

        mykeyboard=(keyboard) findViewById(R.id.keyboard1);
        editPrice=(EditText) findViewById(R.id.TextViewPrice);
        editYear=(EditText) findViewById(R.id.TextViewYear);
        editYear.setShowSoftInputOnFocus(false);
        editPrice.setShowSoftInputOnFocus(false);

        final InputConnection ic=editPrice.onCreateInputConnection(new EditorInfo());
        final InputConnection ic2=editYear.onCreateInputConnection(new EditorInfo());
        editPrice.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if( hasFocus ){ showCustomKeyboard(v);
                    mykeyboard.setInputConnection(ic);} else hideCustomKeyboard();
            }
        });
        editYear.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if( hasFocus ){ showCustomKeyboard(v);
                    mykeyboard.setInputConnection(ic2);} else hideCustomKeyboard();
            }
        });
        editPrice.addTextChangedListener(new TextWatcher() { //set up on textChanged Listener for Purchase Year edit box
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Log.d("textCHanged  ", String.valueOf(PurchaseYear.getText().chars().count()));
                if( editPrice.getText().chars().count()==1&&textCounter==0)
                {   timeStartFirstLetter=System.currentTimeMillis();                                    //measure time between input of 1st and 2nd letter (using keyboard)
                    textCounter=1;
                }
                if( editPrice.getText().chars().count()==2&&textCounter==1)
                {  insuranceDataeCar.addTime(System.currentTimeMillis()-timeStartFirstLetter);
                    textCounter=2;

                }
                if(textsize> editPrice.getText().chars().sum())
                {counterDeletions=counterDeletions+1;}
            }

            @Override
            public void afterTextChanged(Editable s) {
                textsize=s.length();
            }
        });
        editYear.addTextChangedListener(new TextWatcher() { //set up on textChanged Listener for OriginalPrice edit box
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Log.d("textCHanged  ", String.valueOf(PurchaseYear.getText().chars().count()));
                if( editYear.getText().chars().count()==1&&textCounterOP==0)
                {   timeStartFirstLetterOP=System.currentTimeMillis();                                    //measure time between input of 1st and 2nd letter (using keyboard)
                    textCounterOP=1;
                }
                if( editYear.getText().chars().count()==2&&textCounterOP==1)
                {   insuranceDataeCar.addTime(System.currentTimeMillis()-timeStartFirstLetterOP);
                    textCounterOP=2;

                }
                if(textsizeOP> editYear.getText().chars().sum())
                {counterDeletions=counterDeletions+1;}
            }

            @Override
            public void afterTextChanged(Editable s) {
                textsizeOP=s.length();
            }
        });
    }
    private void hideCustomKeyboard() {
        mykeyboard.setVisibility(View.GONE);
        mykeyboard.setEnabled(false);
    }

    private void showCustomKeyboard(View v) {

        mykeyboard.setVisibility(View.VISIBLE);
        mykeyboard.setEnabled(true);
        if( v!=null ) ((InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    private void setBtn() {
        dropDownBrand=(Spinner) findViewById(R.id.dropdownBrand); //set up drop down menu for the car brand
        String [] list=new String[] {"Select Brand","Bike1","Bike2","Bike3"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(eBike.this, android.R.layout.simple_spinner_dropdown_item, list);
        dropDownBrand.setAdapter(adapter);
        dropDownBrand.setAdapter(adapter);

        dropDownModel=(Spinner) findViewById(R.id.dropdownModel); //set up Drop down Menu based on result of drop down menu of the brand
        dropDownBrand.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() { //https://stackoverflow.com/questions/11322642/i-have-an-error-setonitemclicklistener-cannot-be-used-with-a-spinner-what-is-w
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String[] list2 = new String[]{"Select Model", "Model1", "Model2", "Model3","Model4"};
                if(dropDownBrand.getSelectedItem()==null)
                {     dropDownBrand.setAdapter(SetAdapter(list2));}else {
                    if(dropDownBrand.getSelectedItem().toString()=="BMW")
                    {    list2[4] = "Model4";
                        list2[1] = "Model1";
                        list2[2] = "Model2";
                        list2[3] = "Model3";}
                    if(dropDownBrand.getSelectedItem().toString()=="Tesla"){
                        list2[4] = "Model4";
                        list2[1] = "Model1";
                        list2[2] = "Model2";
                        list2[3] = "Model3";
                    }
                    if(dropDownBrand.getSelectedItem().toString()=="Vw"){
                        list2[4] = "Model4";
                        list2[1] = "Model1";
                        list2[2] = "Model2";
                        list2[3] = "Model3";
                    }

                    dropDownModel.setAdapter(SetAdapter(list2));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                String[] list2 = new String[]{"Select Model", "Model1", "Model2", "Model3","Model4"};
                dropDownModel.setAdapter(SetAdapter(list2));
            }
        });




        dropDownCondition=(Spinner) findViewById(R.id.dropdownCondition);
        String [] list3=new String[] {"Select Condition","5%","20%","35%"};
        ArrayAdapter<String> adapter3 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, list3);
        dropDownCondition.setAdapter(adapter3);

        BtnOk=(Button) findViewById(R.id.BtnOk);
        BtnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Save();
                Intent intent=new Intent(getBaseContext(), Summary.class);
                startActivity(intent);


            }
        });

        BtnCancel=(Button) findViewById(R.id.BtnCancel);
        BtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getBaseContext(), Veritaps.class);
                startActivity(intent);
            }
        });

    }

    private void Save(){ //store gatheredData in sharedPreferences "vehicle" //https://stackoverflow.com/questions/7145606/how-do-you-save-store-objects-in-sharedpreferences-on-android

        insuranceDataeCar.setActivityTime(System.currentTimeMillis()-timeStart);
        //safe object of InsuranceData with Gson in sharedPreferences
        Gson gson = new Gson();
        String json = gson.toJson(insuranceDataeCar);

        preferences = PreferenceManager.getDefaultSharedPreferences(eBike.this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("vehicle",json);
        editor.apply();
        unregisterReceiver(receiver);
    }
    private ArrayAdapter SetAdapter(String [] list2) {
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, list2);
        return adapter1;
    }

    private class MyReceiver extends BroadcastReceiver {
        //https://www.journaldev.com/10356/android-broadcastreceiver-example-tutorial
        public MyReceiver(){}

        @Override
        public void onReceive(Context context, Intent intent) {
            if("com.example.broadcast.Acceleration".equals(intent.getAction())) {

                float x = intent.getFloatExtra("x", 0);
                float y =intent.getFloatExtra("y",0);
                float z=intent.getFloatExtra("z",0);
                insuranceDataeCar.addAccelerationData(x,y,z);

            }


        }
    }
}