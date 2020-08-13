package com.example.finalproject;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class Activity3 extends AppCompatActivity {
    private TextView Acceleration, result,Timer;
    private MyReceiver receiver;
    private long timeStart, timeEnd;
    private Button Next,Back;
    private SeekBar seekRed,seekBlue; //https://stackoverflow.com/questions/14910226/how-to-make-slide-to-unlock-button-in-android
  //  @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_3);
        receiver = new MyReceiver();
        IntentFilter intentFilter = new IntentFilter("com.example.broadcast.Acceleration");
        registerReceiver(receiver,intentFilter);

        registerUIElements();
        startService(new Intent(this,AccelerationService.class));
        timeStart=System.currentTimeMillis();





    }

    private void registerUIElements() {
    result=(TextView) findViewById(R.id.Result);
    Timer=(TextView) findViewById(R.id.Time);
    Acceleration=(TextView) findViewById(R.id.Acceleration);
    Back=(Button) findViewById(R.id.BtnBack);
    Back.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent=new Intent(getBaseContext(), MainActivity.class);
            startActivity(intent);
        }
    });
    seekBlue=(SeekBar) findViewById(R.id.myseekBlue);
    seekBlue.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            int alpha = (progress * (255 / 100));
            seekBar.getThumb().setAlpha(255 - alpha);



        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            //https://www.coderzheaven.com/2017/09/10/slide-to-unlock-demo-in-android/
            if (seekBar.getProgress() < 95) {
                seekBar.setThumb(ContextCompat.getDrawable(Activity3.this, R.drawable.ic_launcher));
                seekBar.setProgress(0);
                result.setText("");
            } else {
                seekBar.setProgress(100);
                seekBar.getThumb().setAlpha(0);
                result.setText("Blue");
                timeEnd=System.currentTimeMillis()-timeStart;
                Timer.setText( "needed Time(ms): " +   String.valueOf(timeEnd));
            }
        }
    });
        seekRed=(SeekBar) findViewById(R.id.myseekRed);
        seekRed.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int alpha = (progress * (255 / 100));
                seekBar.getThumb().setAlpha(255 - alpha);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (seekBar.getProgress() < 95) {
                    seekBar.setThumb(ContextCompat.getDrawable(Activity3.this, R.drawable.ic_launcher));
                    seekBar.setProgress(0);
                    result.setText("");
                } else {
                    seekBar.setProgress(100);
                    seekBar.getThumb().setAlpha(0);
                    result.setText("Red");
                    timeEnd=System.currentTimeMillis()-timeStart;
                    Timer.setText( "needed Time(ms): " + String.valueOf(timeEnd));
                }
            }
        });
}




    @Override
    protected void onDestroy() {
        try {
            unregisterReceiver(receiver);

        }catch (Exception e){e.printStackTrace();}
        super.onDestroy();


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
                Acceleration.setText("Acceleration: X= "+x+"  Y= "+y+"  Z= "+z);

            }


        }
    }
}
