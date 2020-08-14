package com.example.finalproject;

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
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

public class Veritaps extends AppCompatActivity {
    private Spinner dropDown;
    private Button BtnNext,BtnCancel;
    private long timeStart,timeStartFirstLetterOP,timeStartFirstLetter;
    private int textCounter=0, textCounterOP=0, textsize=0, textsizeOP=0;


    private int counterDeletions=0;
    private static SharedPreferences preferences;
    private  InsuranceData insuranceDataVeritaps;
    private MyReceiver receiver;
    private EditText PurchaseYear,OrigininalPrice;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.veritaps);
        insuranceDataVeritaps=new InsuranceData();
        receiver = new MyReceiver();
        IntentFilter intentFilter = new IntentFilter("com.example.broadcast.Acceleration");
        registerReceiver(receiver,intentFilter);


        startService(new Intent(this,AccelerationService.class));
        timeStart=System.currentTimeMillis();
        setBtn();



    }
    @Override
    protected void onDestroy() {
        try {
            unregisterReceiver(receiver);

        }catch (Exception e){e.printStackTrace();}
        super.onDestroy();


    }

    private void setBtn() {
        //https://stackoverflow.com/questions/13377361/how-to-create-a-drop-down-list
        dropDown=(Spinner) findViewById(R.id.dropdown);
        String [] list=new String[] {"Select Item","eCar","eBike"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, list);
        dropDown.setAdapter(adapter);
        BtnNext=(Button) findViewById(R.id.BtnOk);
        BtnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(dropDown.getSelectedItem().toString()=="eCar")
                { Save();
                Intent intent=new Intent(getBaseContext(), eCar.class);
                startActivity(intent);
            }
                if(dropDown.getSelectedItem().toString()=="eBike")
                {   Save();
                    Intent intent=new Intent(getBaseContext(), eBike.class);
                    startActivity(intent);
                }
            }
        });

        BtnCancel=(Button) findViewById(R.id.BtnCancel);
        BtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getBaseContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        PurchaseYear=(EditText) findViewById(R.id.EditTextPurchaseYear);
        PurchaseYear.addTextChangedListener(new TextWatcher() { //set up on textChanged Listener for Purchase Year edit box
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
               // Log.d("textCHanged  ", String.valueOf(PurchaseYear.getText().chars().count()));
                if(PurchaseYear.getText().chars().count()==1&&textCounter==0)
                {   timeStartFirstLetter=System.currentTimeMillis();                                    //measure time between input of 1st and 2nd letter (using keyboard)
                    textCounter=1;
                }
                if(PurchaseYear.getText().chars().count()==2&&textCounter==1)
                {  insuranceDataVeritaps.addTime(System.currentTimeMillis()-timeStartFirstLetter);
                    textCounter=2;
                    Log.d("log","saveTimeSuccessfull");
                }
                if(textsize>PurchaseYear.getText().chars().sum())
                {counterDeletions=counterDeletions+1;}
            }

            @Override
            public void afterTextChanged(Editable s) {
            textsize=s.length();
            }
        });



        OrigininalPrice=(EditText) findViewById(R.id.EditTextPrice);
        OrigininalPrice.addTextChangedListener(new TextWatcher() { //set up on textChanged Listener for OriginalPrice edit box
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Log.d("textCHanged  ", String.valueOf(PurchaseYear.getText().chars().count()));
                if(OrigininalPrice.getText().chars().count()==1&&textCounterOP==0)
                {   timeStartFirstLetterOP=System.currentTimeMillis();                                    //measure time between input of 1st and 2nd letter (using keyboard)
                    textCounterOP=1;
                }
                if(OrigininalPrice.getText().chars().count()==2&&textCounterOP==1)
                {  insuranceDataVeritaps.addTime(System.currentTimeMillis()-timeStartFirstLetterOP);
                    textCounterOP=2;
                    Log.d("log","saveTimeSuccessfull");
                }
                if(textsizeOP>OrigininalPrice.getText().chars().sum())
                {counterDeletions=counterDeletions+1;}
            }

            @Override
            public void afterTextChanged(Editable s) {
                textsizeOP=s.length();
            }
        });


    }

    private void Save(){ //store gatheredData in sharedPreferences  //https://stackoverflow.com/questions/7145606/how-do-you-save-store-objects-in-sharedpreferences-on-android

        insuranceDataVeritaps.setActivityTime(System.currentTimeMillis()-timeStart);
        //safe object of InsuranceData with Gson in sharedPreferences
        Gson gson = new Gson();
        String json = gson.toJson(insuranceDataVeritaps);

        preferences = PreferenceManager.getDefaultSharedPreferences(Veritaps.this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("key",json);
        editor.apply();
        unregisterReceiver(receiver);
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
                insuranceDataVeritaps.addAccelerationData(x,y,z);

            }


        }
    }
}
