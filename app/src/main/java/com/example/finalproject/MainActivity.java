package com.example.finalproject;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private Button StartBtn,BtnVeritaps;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setBTN();

    }

    private void setBTN() {
    StartBtn=(Button) findViewById(R.id.btnStart);
    StartBtn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent=new Intent(getBaseContext(), Activity2.class);
            startActivity(intent);
        }
    });
    BtnVeritaps=(Button) findViewById(R.id.btnVeritaps);
    BtnVeritaps.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent=new Intent(getBaseContext(), Veritaps.class);
            startActivity(intent);
        }
    });
    }

    //code snippet from https://developer.android.com/training/gestures/movement
    // velocity measures speed of touch input after a certain event




}