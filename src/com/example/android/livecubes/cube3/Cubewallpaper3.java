package com.example.android.livecubes.cube3;

import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.os.Handler;
import android.os.SystemClock;
import android.service.wallpaper.WallpaperService;
import android.service.wallpaper.WallpaperService.Engine;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import java.util.Random;

import com.example.android.livecubes.cube2.CubeWallpaper2;
import com.example.android.livecubes.cube3.SelectMethodSort;
public class Cubewallpaper3 extends WallpaperService {
    public static final String SHARED_PREFS_NAME="SortingWallpaperSettings";
    private final Handler mHandler = new Handler();
    private final int ArrLen = 14;
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

    class CubeEngine extends Engine 
    implements SharedPreferences.OnSharedPreferenceChangeListener    
    {
    	private final Paint mPaint = new Paint();
    	private Integer iArr[];
    	private boolean isDone;
    	int SortMethod;

    	SortMethod<Integer> sort;
        private SharedPreferences mPrefs;
        private boolean mVisible;

        CubeEngine() 
        {
            iArr = new Integer [ArrLen];
            
            isDone = true;
            final Paint paint = mPaint;
            paint.setColor(0xff6060ff);
            paint.setAntiAlias(true);
            paint.setStrokeWidth(2);
            paint.setStrokeCap(Paint.Cap.ROUND);
            paint.setStyle(Paint.Style.STROKE);
            paint.setTextSize(30);
            mPrefs = Cubewallpaper3.this.getSharedPreferences(SHARED_PREFS_NAME, 0);
            mPrefs.registerOnSharedPreferenceChangeListener(this);
            onSharedPreferenceChanged(mPrefs, null);            
            Start();
        }

        @Override
        public void onCreate(SurfaceHolder surfaceHolder) {
            super.onCreate(surfaceHolder);

            // By default we don't get touch events, so enable them.
            setTouchEventsEnabled(true);
        }

        @Override
        public void onDestroy() 
        {
            super.onDestroy();
            //mHandler.removeCallbacks(mDrawCube);
        }

        @Override
        public void onVisibilityChanged(boolean visible) 
        {
            mVisible = visible;
            if (visible) 
            {
                //drawFrame();
            } 
            else 
            {
                //mHandler.removeCallbacks(mDrawCube);
            }
        }

        @Override
        public void onSurfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            super.onSurfaceChanged(holder, format, width, height);
            // store the center of the surface, so we can draw the cube in the right spot
        }

        @Override
        public void onSurfaceCreated(SurfaceHolder holder) {
            super.onSurfaceCreated(holder);
        }

        @Override
        public void onSurfaceDestroyed(SurfaceHolder holder) {
            super.onSurfaceDestroyed(holder);
            mVisible = false;
           // mHandler.removeCallbacks(mDrawCube);
        }

        @Override
        public void onOffsetsChanged(float xOffset, float yOffset,
                float xStep, float yStep, int xPixels, int yPixels) {
            //drawFrame();
        }

        /*
         * Store the position of the touch event so we can use it for drawing later
         */
        @Override
        public void onTouchEvent(MotionEvent event) 
        {

            if (event.getAction() == MotionEvent.ACTION_MOVE) 
            {

            } 
            else 
            {

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
        }
        void Start()
        {
        	Random rnd = new Random();
        	for (int i=0;i<ArrLen;i++)
        		iArr[i] = Math.abs(rnd.nextInt()%10);
        	switch (SortMethod)
        	{
        	case 0:
        		sort = new InsertionMethodSort<Integer>(this, iArr);
        	break;
        	case 1:
        		sort = new SelectMethodSort<Integer>(this, iArr);
        	break;
        	}
        	
        }
        void drawTouchPoint(boolean isDone1)
        {
            final SurfaceHolder holder = getSurfaceHolder();        	
            Canvas c = null;
            try 
            {
                c = holder.lockCanvas();
                if (c != null) 
                {  

		        	c.drawColor(0xff000000);
		        	iArr = sort.GetCurrState();        	
		        	for (int i=0; i<ArrLen; i++)
		        	{
		        		
		                //mPaint.setShader(new RadialGradient((float)(i*20*2.5+35), 195, 20, 0xff8080ff, 0xff20206f,Shader.TileMode.MIRROR));
		        		mPaint.setShader(new RadialGradient(192, (float)(i*20*2.5+69), 20, 0xff8080ff, 0xff20206f,Shader.TileMode.MIRROR));		        		
		                mPaint.setStyle(Paint.Style.FILL);
		        		//c.drawCircle((float) (i*20*2.5+30), 200, 20, mPaint);
		                c.drawCircle(200, (float)(i*20*2.5+60), 20, mPaint);
		        		mPaint.setStyle(Paint.Style.STROKE);
		        		mPaint.setShader(null);
		        		mPaint.setColor(0xff000000);
		        		c.drawText(Integer.toString(iArr[i]), 192, (float)(i*20*2.5+69), mPaint);
		        	}
		        	mPaint.setColor(0xff8080ff);
	        		Path path = new Path();
	        		path.moveTo(150, 350);
	        		path.lineTo(150, 50);
		        	c.drawTextOnPath(sort.GetMethodName(),path,0,0,mPaint);
		        	if (isDone1)
		        	{
		        		path.reset();
		        		path.moveTo(150, 550);
		        		path.lineTo(150, 250);
		        		isDone = true;
		        		c.drawTextOnPath("Done!!!",path,0,0,mPaint);
		        		Start();
		        	}
                }
            } 
            finally 
            {
                if (c != null) holder.unlockCanvasAndPost(c);
            }        	
        }

		public void onSharedPreferenceChanged(SharedPreferences prefs, String key) 
		{
			String stSortMethod = prefs.getString("sort_method", "0");
			
			SortMethod = Integer.parseInt(stSortMethod);
			//SortMethod = prefs.getInt("sort_method",0);
			Log.d("onSharedPreferenceChanged SortMethod=", stSortMethod);
			//Log.d("onSharedPreferenceChanged SortMethod=", Integer.toString(SortMethod));
		}

    }
}
