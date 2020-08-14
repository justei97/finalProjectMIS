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

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Activity2 extends AppCompatActivity {
    private Button BluetBtn,RedBtn, BtnBack,BtnNext;
    private TextView editText,editTextBTNCHoice, Acceleration, answer;
    private MyReceiver receiver;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2);
        receiver = new MyReceiver();
        IntentFilter intentFilter = new IntentFilter("com.example.broadcast.Acceleration");
        registerReceiver(receiver,intentFilter);

        registerUIElements();
        startService(new Intent(this,AccelerationService.class));
    }

    private void registerUIElements() {
        Acceleration=(TextView) findViewById(R.id.Acceleration);
        BluetBtn=(Button) findViewById(R.id.BtnBlue);
        BluetBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                //  String text=String.valueOf(findBTN((int)event.getX(),(int)event.getY()));
                String text=String.valueOf((int)Math.round(Math.sqrt((Math.pow(BluetBtn.getWidth()/2-event.getX(),2)+Math.pow(BluetBtn.getHeight()-event.getY(),2)))));
                editText.setText(text);
                editTextBTNCHoice.setText("BlueBtn");
                answer.setText("Blue");
                return false;
            }
        });

        RedBtn=(Button) findViewById(R.id.BtnRed);
        RedBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                String text=String.valueOf((int)Math.round(Math.sqrt((Math.pow(RedBtn.getWidth()/2-event.getX(),2)+Math.pow(RedBtn.getHeight()-event.getY(),2)))));
                editText.setText(text);
                editTextBTNCHoice.setText("RedBtn");
                answer.setText("Red");
                return false;
            }
        });

        BtnBack=(Button) findViewById(R.id.BtnBack);
        BtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getBaseContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        BtnNext=(Button) findViewById(R.id.BtnForward);
        BtnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Activity2.this, Activity3.class);
                startActivity(intent);
            }
        });

        editText=(TextView) findViewById(R.id.EditTextTouchPrecision);
        editTextBTNCHoice=(TextView) findViewById(R.id.EditTextBTNCHOICE);
        answer=(TextView) findViewById(R.id.Result);
    }



    public int findBTN(int InputX, int InputY){ //returns true if distance between touch pointer and btnBlue(middle of BTN) is smaller then distance between touchpointer and btnRed; else: return false
        int BlueX=(int) BluetBtn.getX()+BluetBtn.getWidth()/2;
        int BlueY=(int) BluetBtn.getY()+BluetBtn.getHeight();
        int RedX=(int) RedBtn.getX()+RedBtn.getWidth()/2;
        int RedY=(int) RedBtn.getY()+RedBtn.getHeight();

        int distanceBlue=(int)Math.round(Math.sqrt((Math.pow(BlueX-InputX,2)+Math.pow(BlueY-InputY,2))));
        int distanceRed=(int)Math.round(Math.sqrt((Math.pow(RedX-InputX,2)+Math.pow(RedY-InputY,2))));
        if(distanceBlue<distanceRed)
        {   editTextBTNCHoice.setText("BlueBtn");
            return distanceBlue;}
        else{
            editTextBTNCHoice.setText("RedBtn");
            return distanceRed;}


    }



    //snippet from https://www.vogella.com/tutorials/AndroidTouch/article.html
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        // get pointer index from the event object
        int pointerIndex = event.getActionIndex();

                // get pointer ID
        int pointerId = event.getPointerId(pointerIndex);

        // get masked (not specific to a pointer) action
        int maskedAction = event.getActionMasked();

        int x=(int) event.getX();
        int y=(int) event.getY();
        Log.d("x: ", String.valueOf(event.getX()));
        Log.d("Y: ", String.valueOf(event.getY()));
        String text=String.valueOf(findBTN(x,y));
        editText.setText(text);

        switch (maskedAction) {

            case MotionEvent.ACTION_DOWN: // new touch started
                {
                // TODO use data


                }
            case MotionEvent.ACTION_POINTER_DOWN: //Pointer down (multi-touch)
                {

                break;
            }
            case MotionEvent.ACTION_MOVE: // a pointer was moved -->Finger is moving  (swipe)
                {
                // TODO use data
                break;
            }

            case MotionEvent.ACTION_UP: // Finger (swipe) went up
            {

            }
            case MotionEvent.ACTION_POINTER_UP: //Pointer up (multi-touch)

            case MotionEvent.ACTION_BUTTON_PRESS: //This action is not a touch event so it is delivered to View#onGenericMotionEvent(MotionEvent) rather than View#onTouchEvent(MotionEvent).
            { String text2=String.valueOf(findBTN(x,y));
                editText.setText(text2);}
            case MotionEvent.ACTION_CANCEL: {
               break;
            }
        }


        return true;
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
