package com.example.finalproject;

import java.util.ArrayList;

public class InsuranceData {
    private ArrayList<Long> list; //time for time differences between first and 2nd button hits
    private ArrayList<Float> xAcceleration,yAcceleration,zAcceleration;
    private long activityTime=0;
public InsuranceData(){
    list=new ArrayList<>();
    xAcceleration=new ArrayList<>();
    yAcceleration=new ArrayList<>();
    zAcceleration=new ArrayList<>();
    }

    public void addTime(long time){
    list.add(time);
    }

    public void addAccelerationData(float x,float y, float z){
        this.xAcceleration.add(x);
        this.yAcceleration.add(y);
        this.zAcceleration.add(z);
    }

    public void setActivityTime(long activityTime){
    this.activityTime=activityTime;
    }

    public ArrayList<Float> getxAcceleration() {
        return xAcceleration;
    }
    public ArrayList<Float> getyAcceleration(){
    return  yAcceleration;
    }
    public ArrayList<Float> getzAcceleration(){
        return  zAcceleration;
    }



    public ArrayList<Long> getActivityTime() {
        return list;
    }
}
