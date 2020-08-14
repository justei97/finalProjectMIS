package com.example.finalproject;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

//https://stackoverflow.com/questions/12545936/how-to-draw-graph-in-android
public class GraphView extends View {

    int X_graphArray[] = null;
    int Y_graphArray[] = null;
    int Z_graphArray[] = null;
    int m_maxY = 0;

    Paint X_paint,Y_Paint,Z_Paint;


    public GraphView(Context context) {
        super(context);
        init();
    }

    public GraphView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GraphView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        X_paint = new Paint();
        X_paint.setColor(Color.BLUE);
        X_paint.setStrokeWidth(5);
        Y_Paint = new Paint();
        Y_Paint.setColor(Color.RED);
        Y_Paint.setStrokeWidth(5);
        Z_Paint = new Paint();
        Z_Paint.setColor(Color.GREEN);
        Z_Paint.setStrokeWidth(5);
    }

    public void setGraphArray(int i,int Xi_graphArray[], int Xi_maxY)
    {   if(i==1)
        X_graphArray = Xi_graphArray;
         if(i==2)
        Y_graphArray=Xi_graphArray;
         if(i==3)
        Z_graphArray=Xi_graphArray;

        if(m_maxY<Xi_maxY)
         m_maxY = Xi_maxY;
    }

    public void setGraphArray(int ix,int Xi_graphArray[])
    {
        int maxY = 0;

        for(int i = 0; i < Xi_graphArray.length; ++i)
        {
            if(Xi_graphArray[i] > maxY)
            {
                maxY = Xi_graphArray[i];
            }
        }
        setGraphArray(ix,Xi_graphArray, maxY);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if(X_graphArray == null)
        {
            return;
        }

        int maxX = X_graphArray.length;

        float factorX = getWidth() / (float)maxX;
        float factorY = getHeight() / (float)m_maxY;

        for(int i = 1; i < X_graphArray.length; ++i) {
            int x0 = i - 1;
            int y0 = X_graphArray[i-1];
            int x1 = i;
            int y1 = X_graphArray[i];

            int sx = (int)(x0 * factorX);
            int sy = getHeight() - (int)(y0* factorY);
            int ex = (int)(x1*factorX);
            int ey = getHeight() - (int)(y1* factorY);
            canvas.drawLine(sx, sy, ex, ey, X_paint);
        }

        for(int i = 1; i < Y_graphArray.length; ++i) {
            int x0 = i - 1;
            int y0 =Y_graphArray[i-1];
            int x1 = i;
            int y1 =Y_graphArray[i];

            int sx = (int)(x0 * factorX);
            int sy = getHeight() - (int)(y0* factorY);
            int ex = (int)(x1*factorX);
            int ey = getHeight() - (int)(y1* factorY);
            canvas.drawLine(sx, sy, ex, ey, Y_Paint);
        }
        for(int i = 1; i < Z_graphArray.length; ++i) {
            int x0 = i - 1;
            int y0 =Z_graphArray[i-1];
            int x1 = i;
            int y1 =Z_graphArray[i];

            int sx = (int)(x0 * factorX);
            int sy = getHeight() - (int)(y0* factorY);
            int ex = (int)(x1*factorX);
            int ey = getHeight() - (int)(y1* factorY);
            canvas.drawLine(sx, sy, ex, ey, Z_Paint);
        }
    }
}