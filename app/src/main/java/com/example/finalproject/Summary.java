package com.example.finalproject;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Summary extends AppCompatActivity {
    private TextView textViewVeritapsTime;
    private static SharedPreferences preferences;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.summary);
        setBtn();
        CollectData(); //draw Acceleration data on Canvas
    }

    private void setBtn() {
            textViewVeritapsTime = (TextView) findViewById(R.id.TextViewVeritapsTime);

    }


    private void CollectData() {
        {   final SharedPreferences mSharedPreference= PreferenceManager.getDefaultSharedPreferences(getBaseContext());

            Gson gson = new Gson();
            String json = mSharedPreference.getString("key", "");
            InsuranceData insuranceData = gson.fromJson(json, InsuranceData.class);

            Log.d("LOG1 AccX", String.valueOf(insuranceData.getActivityTime()));
            ArrayList<Float> AcclistX = insuranceData.getxAcceleration();
            ArrayList<Float> AcclistY = insuranceData.getyAcceleration();
            ArrayList<Float> AcclistZ = insuranceData.getzAcceleration();
            int[] AcclistXInt= new int[AcclistX.size()];
            int[] AcclistYInt= new int[AcclistY.size()];
            int[] AcclistZInt= new int[AcclistZ.size()];
            for(int i=0;i<AcclistX.size();i++)
            {
                AcclistXInt[i]=((Integer) Math.round(AcclistX.get(i)*10));
                AcclistYInt[i]=((Integer) Math.round(AcclistY.get(i)*10));
                AcclistZInt[i]=((Integer) Math.round(AcclistZ.get(i)*10));
            }
            GraphView graphView = (GraphView)findViewById(R.id.histogram_view);
            graphView.setGraphArray(1,AcclistXInt);
            graphView.setGraphArray(2,AcclistYInt);
            graphView.setGraphArray(3,AcclistZInt);

        }
    }
}