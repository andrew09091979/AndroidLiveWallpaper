/*
 * Copyright (C) 2009 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.livecubes.cube1;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.os.SystemClock;
import android.service.wallpaper.WallpaperService;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

/*
 * This animated wallpaper draws a rotating wireframe cube.
 */
public class CubeWallpaper1 extends WallpaperService {

    private final Handler mHandler = new Handler();

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public Engine onCreateEngine() {
        return new CubeEngine();
    }

    class CubeEngine extends Engine {

        private final Paint mPaint = new Paint();
        private float mOffset;
        private float mTouchX = -1;
        private float mTouchY = -1;
        private long mStartTime;
        private float mCenterX;
        private float mCenterY;
    	private float fPointsX[];
    	private float fPointsY[];
    	private boolean mActionMove = true;
    	private int currPnt;
    	private int cnt = 0;
        private final Runnable mDrawCube = new Runnable() {
            public void run() {
                drawFrame();
            }
        };
        private boolean mVisible;

        CubeEngine() {
            // Create a Paint to draw the lines for our cube
            final Paint paint = mPaint;
        	fPointsX = new float[16];
        	fPointsY = new float[16];
        	currPnt = 0;
            paint.setColor(0xff0000ff);
            paint.setAntiAlias(true);
            paint.setStrokeWidth(2);
            paint.setStrokeCap(Paint.Cap.ROUND);
            paint.setStyle(Paint.Style.STROKE);

            mStartTime = SystemClock.elapsedRealtime();
        }

        @Override
        public void onCreate(SurfaceHolder surfaceHolder) {
            super.onCreate(surfaceHolder);

            // By default we don't get touch events, so enable them.
            setTouchEventsEnabled(true);
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            mHandler.removeCallbacks(mDrawCube);
        }

        @Override
        public void onVisibilityChanged(boolean visible) {
            mVisible = visible;
            if (visible) {
                drawFrame();
            } else {
                mHandler.removeCallbacks(mDrawCube);
            }
        }

        @Override
        public void onSurfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            super.onSurfaceChanged(holder, format, width, height);
            // store the center of the surface, so we can draw the cube in the right spot
            mCenterX = width/2.0f;
            mCenterY = height/2.0f;
            drawFrame();
        }

        @Override
        public void onSurfaceCreated(SurfaceHolder holder) {
            super.onSurfaceCreated(holder);
        }

        @Override
        public void onSurfaceDestroyed(SurfaceHolder holder) {
            super.onSurfaceDestroyed(holder);
            mVisible = false;
            mHandler.removeCallbacks(mDrawCube);
        }

        @Override
        public void onOffsetsChanged(float xOffset, float yOffset,
                float xStep, float yStep, int xPixels, int yPixels) {
            mOffset = xOffset;
            drawFrame();
        }

        /*
         * Store the position of the touch event so we can use it for drawing later
         */
        @Override
        public void onTouchEvent(MotionEvent event) 
        {

            if (event.getAction() == MotionEvent.ACTION_MOVE) 
            {
            	cnt = 3;//почему-то экран после окончания жеста закрашивается только минимум со 2-го раза
                fPointsX[currPnt] = event.getX();
                fPointsY[currPnt] = event.getY();
                mActionMove = true;
                if (currPnt>14)
                {
                	currPnt = 0;
                }
                else
                {
                	currPnt++;
                }
            } 
            else 
            {
            	for (int i=0; i<16; i++)
            	{
                    fPointsX[i] = -1;
                    fPointsY[i] = -1;
            	}
        		mActionMove = false;
            }
            super.onTouchEvent(event);
        }

        /*
         * Draw one frame of the animation. This method gets called repeatedly
         * by posting a delayed Runnable. You can do any drawing you want in
         * here. This example draws a wireframe cube.
         */
        void drawFrame() 
        {
            final SurfaceHolder holder = getSurfaceHolder();

            Canvas c = null;
            try {
                c = holder.lockCanvas();
                if (c != null) {
                    // draw something
                    //drawCube(c);
                	//drawPyramide(c);
                    drawTouchPoint(c);
                }
            } finally {
                if (c != null) holder.unlockCanvasAndPost(c);
            }

            // Reschedule the next redraw
            mHandler.removeCallbacks(mDrawCube);
            if (mVisible) {
                mHandler.postDelayed(mDrawCube, 1000 / 25);
            }
        }

        /*
         * Draw a wireframe cube by drawing 12 3 dimensional lines between
         * adjacent corners of the cube
         */
        void drawCube(Canvas c) {
            c.save();
            c.translate(mCenterX, mCenterY);
            c.drawColor(0xff000000);
           
            drawLine(c, -400, -400, -400,  400, -400, -400);
            drawLine(c,  400, -400, -400,  400,  400, -400);
            drawLine(c,  400,  400, -400, -400,  400, -400);
            drawLine(c, -400,  400, -400, -400, -400, -400);

            drawLine(c, -400, -400,  400,  400, -400,  400);
            drawLine(c,  400, -400,  400,  400,  400,  400);
            drawLine(c,  400,  400,  400, -400,  400,  400);
            drawLine(c, -400,  400,  400, -400, -400,  400);

            drawLine(c, -400, -400,  400, -400, -400, -400);
            drawLine(c,  400, -400,  400,  400, -400, -400);
            drawLine(c,  400,  400,  400,  400,  400, -400);
            drawLine(c, -400,  400,  400, -400,  400, -400);
            
            c.restore();
        }
        void drawPyramide(Canvas c) {
            c.save();
            c.translate(mCenterX, mCenterY);
            c.drawColor(0xff000000);
           
            drawLine(c, -400, -400, -400,  400, -400, -400);
            drawLine(c,  400, -400, -400,  0,  400, -400);
            drawLine(c,  0,  400, -400, -400,  -400, -400);

            drawLine(c, -400,  -400, -400, 0, 0, 400);
            drawLine(c, 400, -400,  -400,  0, 0,  400);
            drawLine(c,  0, 400,  -400,  0,  0,  400);
            
            c.restore();
        }

        /*
         * Draw a 3 dimensional line on to the screen
         */
        void drawLine(Canvas c, int x1, int y1, int z1, int x2, int y2, int z2) {
            long now = SystemClock.elapsedRealtime();
            float xrot = ((float)(now - mStartTime)) / 1000;
            float yrot = (0.5f - mOffset) * 2.0f;
            float zrot = 0;

            // 3D transformations

            // rotation around X-axis
            float newy1 = (float)(Math.sin(xrot) * z1 + Math.cos(xrot) * y1);
            float newy2 = (float)(Math.sin(xrot) * z2 + Math.cos(xrot) * y2);
            float newz1 = (float)(Math.cos(xrot) * z1 - Math.sin(xrot) * y1);
            float newz2 = (float)(Math.cos(xrot) * z2 - Math.sin(xrot) * y2);

            // rotation around Y-axis
            float newx1 = (float)(Math.sin(yrot) * newz1 + Math.cos(yrot) * x1);
            float newx2 = (float)(Math.sin(yrot) * newz2 + Math.cos(yrot) * x2);
            newz1 = (float)(Math.cos(yrot) * newz1 - Math.sin(yrot) * x1);
            newz2 = (float)(Math.cos(yrot) * newz2 - Math.sin(yrot) * x2);

            // 3D-to-2D projection
            float startX = newx1 / (4 - newz1 / 400);
            float startY = newy1 / (4 - newz1 / 400);
            float stopX =  newx2 / (4 - newz2 / 400);
            float stopY =  newy2 / (4 - newz2 / 400);

            c.drawLine(startX, startY, stopX, stopY, mPaint);
        }

        /*
         * Draw a circle around the current touch point, if any.
         */
        void drawTouchPoint(Canvas c) 
        {   
        	if (cnt > 0)
        	{
        		Log.d("drawTouchPoint","clearing screen");
            	c.drawColor(0xff000000);
            	cnt--;
	            if (mActionMove) 
	            {            	
	            	for (int i=0; i<16; i++)
	            	{
	            		if (fPointsX[i]>=0 && fPointsY[i]>=0)
	            		{
	            			c.drawCircle(fPointsX[i], fPointsY[i], 30, mPaint);
	            		}
	            	}
	            }
        	}
        }

    }
}
