package com.example.finalproject;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.gson.Gson;
import java.util.ArrayList;

public class Summary extends AppCompatActivity {
    private TextView textViewBtnTime,textViewActivityTime,textViewXMean,textViewYMean,textViewZMean,textViewActivityTimeVehicle ;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.summary);
        setBtn();
        CollectData(); //draw Acceleration data on Canvas
    }

    private void setBtn() {
            textViewBtnTime = (TextView) findViewById(R.id.textViewAvgTime2Btn);
            textViewActivityTime = (TextView) findViewById(R.id.textViewActivityTime);
            textViewActivityTimeVehicle = (TextView) findViewById(R.id.textViewActivityTimeVehicle);
            textViewXMean=(TextView)findViewById(R.id.textViewXMean);
            textViewYMean=(TextView) findViewById(R.id.textViewYMean);
            textViewZMean=(TextView)findViewById(R.id.textViewZMean);
    }


    private void CollectData() {
        {   final SharedPreferences mSharedPreference= PreferenceManager.getDefaultSharedPreferences(getBaseContext());

            Gson gson = new Gson();
            String veritaps = mSharedPreference.getString("veritaps", "");
            String vehicle=mSharedPreference.getString("vehicle","");
            InsuranceData insuranceDataVertaps = gson.fromJson(veritaps, InsuranceData.class);
            InsuranceData insuranceDataVehicle = gson.fromJson(vehicle, InsuranceData.class);

          //combine Lists from Vehicle+Veritaps
            ArrayList<Float> AcclistX = insuranceDataVertaps.getxAcceleration();
            AcclistX.addAll(insuranceDataVehicle.getxAcceleration());
            ArrayList<Float> AcclistY = insuranceDataVertaps.getyAcceleration();
            AcclistY.addAll(insuranceDataVehicle.getyAcceleration());
            ArrayList<Float> AcclistZ = insuranceDataVertaps.getzAcceleration();
            AcclistZ.addAll(insuranceDataVehicle.getzAcceleration());

            int[] AcclistXInt= new int[AcclistX.size()];
            int[] AcclistYInt= new int[AcclistY.size()];
            int[] AcclistZInt= new int[AcclistZ.size()];
            float meanX=0,meanY=0,meanZ=0;
            float maxX=0,SDx=0,SDz=0;
            for(int i=0;i<AcclistX.size();i++)
            {
                AcclistXInt[i]=((Integer) Math.round(AcclistX.get(i)*10)); //*10 for better visibility of differences (due to cast to int (necessary for selecting pixel in graph))
                meanX=meanX+AcclistX.get(i);
                if(maxX<AcclistX.get(i))
                    maxX=AcclistX.get(i);
                AcclistYInt[i]=((Integer) Math.round(AcclistY.get(i)*10));
                meanY=meanY+AcclistY.get(i);
                AcclistZInt[i]=((Integer) Math.round(AcclistZ.get(i)*10));
                meanZ=meanZ+AcclistZ.get(i);
            }


            textViewXMean.setText("Mean Acceleration X-Axis:"+String.valueOf( meanX/AcclistX.size())+"     Max: "+String.valueOf(maxX)+"   SD of X-Acc: "+computeSD(meanX, AcclistXInt));
            textViewYMean.setText("Mean Acceleration Y-Axis:"+String.valueOf( meanY/AcclistY.size()));
            textViewZMean.setText("Mean Acceleration Z-Axis:"+String.valueOf( meanZ/AcclistZ.size())+"   SD of Z-Acc: "+computeSD(meanZ, AcclistZInt));
            GraphView graphView = (GraphView)findViewById(R.id.histogram_view);
            graphView.setGraphArray(1,AcclistXInt);
            graphView.setGraphArray(2,AcclistYInt);
            graphView.setGraphArray(3,AcclistZInt);

            ArrayList<Long> time2Button = insuranceDataVertaps.getButtonTime(); //calculate average Time between 1st and 2nd button hit
            time2Button.addAll(insuranceDataVehicle.getButtonTime()); //add used in vehicle class
            long ButtonTime=0;
            for(int i=0;i<time2Button.size();i++)
            {ButtonTime=ButtonTime+time2Button.get(i);}
            textViewBtnTime.setText(String.valueOf((ButtonTime/time2Button.size())));
            textViewActivityTime.setText(textViewActivityTime.getText()+"  "+String.valueOf(insuranceDataVertaps.getActivityTime()/1000));
            textViewActivityTimeVehicle.setText(textViewActivityTimeVehicle.getText()+"  "+String.valueOf(insuranceDataVehicle.getActivityTime()/1000));

        }
    }

    //TODO: SD
    private double computeSD(float mean, int[] acclistInt) {
        double temp=0;
        //https://stackoverflow.com/questions/37930631/standard-deviation-of-an-arraylist
        for (int i = 0; i < acclistInt.length; i++)
        {
            int val = acclistInt [i];

            // Step 2:
            double squrDiffToMean = Math.pow(val - mean, 2);

            // Step 3:
            temp += squrDiffToMean;
        }
        double meanOfDiffs = (double) temp / (double) (acclistInt.length);

        // Step 5:
        return Math.sqrt(meanOfDiffs);
    }
}