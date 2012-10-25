package com.example.android.livecubes.cube3;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.os.SystemClock;
import android.service.wallpaper.WallpaperService;
import android.service.wallpaper.WallpaperService.Engine;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import java.util.Random;
import com.example.android.livecubes.cube3.SelectMethodSort;
public class Cubewallpaper3 extends WallpaperService {

    private final Handler mHandler = new Handler();
    private final int ArrLen = 9;
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
    {
    	private final Paint mPaint = new Paint();
    	private Integer iArr[];
    	private boolean isDone;
    	//InsertionMethodSort<Integer> sort;
    	SelectMethodSort<Integer> sort;
/*    	
        private final Runnable mDrawCube = new Runnable() {
            public void run() {
                drawFrame();
            }
        };
*/        
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
        	Log.d("drawFrame","isDone");
        	//sort = new InsertionMethodSort<Integer>(this, iArr);
        	sort = new SelectMethodSort<Integer>(this, iArr);
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
		        	Log.d("drawTouchPoint","drawing");
		        	c.drawColor(0xff000000);
		        	iArr = sort.GetCurrState();        	
		        	for (int i=0; i<ArrLen; i++)
		        	{
		        		c.drawCircle((float) (i*20*2.5+30), 200, 20, mPaint);
		        		c.drawText(Integer.toString(iArr[i]), (float)(i*20*2.5+23), 210, mPaint);
		        	}
		        	if (isDone1)
		        	{
		        		isDone = true;
		        		c.drawText("Done!!!",200,160,mPaint);
		        		Start();
		        	}
                }
            } 
            finally 
            {
                if (c != null) holder.unlockCanvasAndPost(c);
            }        	
        }

    }
}
