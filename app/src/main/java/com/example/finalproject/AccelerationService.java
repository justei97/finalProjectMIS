package com.example.finalproject;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;

import androidx.annotation.Nullable;

public class AccelerationService extends Service implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor Acceleration;

    public void onCreate() {
    sensorManager=(SensorManager) getSystemService(SENSOR_SERVICE);
    Acceleration=sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    sensorManager.registerListener(this,Acceleration,SensorManager.SENSOR_DELAY_NORMAL);

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        int x = (int) event.values[0];
        int y = (int) event.values[1];
         Intent broadcastIntent =new Intent();

        broadcastIntent.setAction("com.example.broadcast.Acceleration");
        broadcastIntent.putExtra("x",x);
        broadcastIntent.putExtra("y",y);

     /* (context.sendBroadcast)*/  this.sendBroadcast(broadcastIntent);


    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
