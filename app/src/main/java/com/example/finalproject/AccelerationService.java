package com.example.finalproject;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.util.Log;

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
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];
         Intent broadcastIntent =new Intent();

        broadcastIntent.setAction("com.example.broadcast.Acceleration");

        broadcastIntent.putExtra("x",x);
        broadcastIntent.putExtra("y",y);
        broadcastIntent.putExtra("z",z);
        this.sendBroadcast(broadcastIntent);


    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
