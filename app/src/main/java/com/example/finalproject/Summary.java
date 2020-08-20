package com.example.finalproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.gson.Gson;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Summary extends AppCompatActivity {
    private TextView textViewBtnTime,textViewActivityTime,textViewXMean,textViewYMean,textViewZMean,textViewActivityTimeVehicle, AvgBtnDst ;
    private Button Back;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.summary);
        setBtn();
        CollectData(); //draw Acceleration data on Canvas
    }

    private void setBtn() {
            textViewBtnTime = (TextView) findViewById(R.id.AvgBtnTIme);
            textViewActivityTime = (TextView) findViewById(R.id.textViewActivityTime);
            textViewActivityTimeVehicle = (TextView) findViewById(R.id.textViewActivityTimeVehicle);
            textViewXMean=(TextView)findViewById(R.id.textViewXMean);
            textViewYMean=(TextView) findViewById(R.id.textViewYMean);
            textViewZMean=(TextView)findViewById(R.id.textViewZMean);
            AvgBtnDst=(TextView)findViewById(R.id.textViewAvgBtnDistance);
            Back=(Button) findViewById(R.id.BtnBack);
            Back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(getBaseContext(), MainActivity.class);
                    startActivity(intent);
                }
            });
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
            ArrayList<Float> AcclistX2=insuranceDataVehicle.getxAcceleration();
            ArrayList<Float> AcclistY = insuranceDataVertaps.getyAcceleration();
            ArrayList<Float> AcclistY2=insuranceDataVehicle.getyAcceleration();
            ArrayList<Float> AcclistZ = insuranceDataVertaps.getzAcceleration();
            ArrayList<Float> AcclistZ2=insuranceDataVehicle.getzAcceleration();
            for(int i=0;i<AcclistX2.size();i++)
            {
                AcclistX.add(AcclistX2.get(i));
                AcclistY.add(AcclistY2.get(i));
                AcclistZ.add(AcclistZ2.get(i));
            }

            int[] AcclistXInt= new int[AcclistX.size()];
            int[] AcclistYInt= new int[AcclistY.size()];
            int[] AcclistZInt= new int[AcclistZ.size()];
            float meanX=0,meanY=0,meanZ=0;
            float maxX=0,SDx=0,SDz=0;
            for(int i=0;i<AcclistX.size();i++)
            {
                meanX=meanX+AcclistX.get(i);
                meanY=meanY+AcclistY.get(i);
                meanZ=meanZ+AcclistZ.get(i);
                if(maxX<AcclistX.get(i))
                    maxX=AcclistX.get(i);

                AcclistXInt[i]=((Integer) Math.round(AcclistX.get(i)*10)); //*10 for better visibility of differences (due to cast to int (necessary for selecting pixel in graph))


                AcclistYInt[i]=((Integer) Math.round(AcclistY.get(i)*10));

                AcclistZInt[i]=((Integer) Math.round(AcclistZ.get(i)*10));

            }

            double meanXd=meanX/AcclistX.size();
            double meanzd=meanZ/AcclistZ.size();
            textViewXMean.setText("Mean Acceleration X-Axis:"+String.valueOf( meanX/AcclistX.size())+"     Max: "+String.valueOf(maxX)+"   SD of X-Acc: "+computeSD(meanXd, AcclistX));
            textViewYMean.setText("Mean Acceleration Y-Axis:"+String.valueOf( meanY/AcclistY.size()));
            textViewZMean.setText("Mean Acceleration Z-Axis:"+String.valueOf( meanZ/AcclistZ.size())+"   SD of Z-Acc: "+computeSD(meanzd, AcclistZ));
            GraphView graphView = (GraphView)findViewById(R.id.histogram_view);
            graphView.setGraphArray(1,AcclistXInt);
            graphView.setGraphArray(2,AcclistYInt);
            graphView.setGraphArray(3,AcclistZInt);

            ArrayList<Long> time2Button = insuranceDataVertaps.getButtonTime(); //calculate average Time between 1st and 2nd button hit
            time2Button.addAll(insuranceDataVehicle.getButtonTime()); //add used in vehicle class
            long ButtonTime=0;
            for(int i=0;i<time2Button.size();i++)
            {ButtonTime=ButtonTime+time2Button.get(i);}
            textViewBtnTime.setText(textViewBtnTime.getText()+" "+String.valueOf((ButtonTime/time2Button.size())));
            textViewActivityTime.setText(textViewActivityTime.getText()+"  "+String.valueOf(insuranceDataVertaps.getActivityTime()/1000));
            textViewActivityTimeVehicle.setText(textViewActivityTimeVehicle.getText()+"  "+String.valueOf(insuranceDataVehicle.getActivityTime()/1000));

            ArrayList<Double> BtnVehic=insuranceDataVehicle.getBtnPrecision();
            ArrayList<Double> BtnVeritaps=insuranceDataVertaps.getBtnPrecision();


            double dist=0;
            int count=0;
            for(int i=0;i<BtnVehic.size();i++)
             {
                 if(BtnVehic.get(i) !=0)
                dist=dist+BtnVehic.get(i);

                else count=count+1;


            }
            for(int i=0;i<BtnVeritaps.size();i++)
            {
                if(BtnVeritaps.get(i) !=0)
                dist=dist+BtnVeritaps.get(i);
            else count=count+1;


            }

            AvgBtnDst.setText(AvgBtnDst.getText()+String.valueOf(dist/(BtnVehic.size()+BtnVeritaps.size()-count)));


        }
    }

    //TODO: SD
    private double computeSD(double mean, ArrayList <Float> acclistInt) {
        double temp=0;
        int size=acclistInt.size();
        //https://stackoverflow.com/questions/37930631/standard-deviation-of-an-arraylist
        for (int i = 0; i < size; i++)
        {
         float temp1=acclistInt.get(i);
        double temp2=temp1;
         double pow=temp2-mean;
        temp= temp+Math.pow(pow,2);


        }
       double variance= temp/size;


        // Step 5:
        return Math.sqrt(variance);
    }
}