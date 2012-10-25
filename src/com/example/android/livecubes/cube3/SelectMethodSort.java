package com.example.android.livecubes.cube3;

import com.example.android.livecubes.cube3.Cubewallpaper3.CubeEngine;

import android.util.Log;
import android.os.Handler;
import android.service.wallpaper.WallpaperService.Engine;

public class SelectMethodSort<cl> {
	private int mTurn;
	private int mI;
	private int mJ;
	private int mR;
	private cl iArr[];
	private int mCurrMinPos;
	private CubeEngine cubeEngine;
	private final Handler mHandler = new Handler();
    private final Runnable Turn = new Runnable() 
    {
        public void run() 
        {
            DoTurn();
        }
    };
    SelectMethodSort(CubeEngine cubeEngn, cl iArrToSort[])
	{
		cubeEngine = cubeEngn;
		iArr = iArrToSort;
		init__();
		DoTurn();
	};	    
	SelectMethodSort(cl iArrToSort[])
	{
		iArr = iArrToSort;
		init__();
	};

	void Init(cl iArrToSort[])
	{
		iArr = iArrToSort;
		init__();
	};
	private void init__()
	{
		mI = 0;
		mCurrMinPos = mI;
		mJ = mI+1;
		mR = iArr.length;
		mTurn = 0;		
	}		
    private void Wait()
    {
        long endTime = System.currentTimeMillis() + 2000;
        while (System.currentTimeMillis() < endTime)
        {}
    }	
	cl [] GetCurrState()
	{
		return iArr;
	};
	private void DoTurn()
	{
		boolean res=MakeTurn();
        mHandler.removeCallbacks(Turn);
        if (!res)
        	mHandler.postDelayed(Turn, 2000);		
	};	
	boolean MakeTurn()
	{
		int turnToBreak = 0;
		int j = mJ;
/*		
		for(mJ=mI+1; mJ<mR; mJ++)
		{
			if(iArr[mJ].hashCode()<iArr[mCurrMinPos].hashCode())
				mCurrMinPos = mJ;
		}
		cl Temp = iArr[mI];
		iArr[mI] = iArr[mCurrMinPos];
		iArr[mCurrMinPos] = Temp;

		if (mI<mR-1)
		{
			mI++;
			mCurrMinPos = mI;
		}
		else
			return false;
		
		return true;
*/		
		
		if(mI<mR-1)
		{
			mCurrMinPos = mI;
			for(j=mI+1; j<mR; j++)
			{
				if(iArr[j].hashCode()<iArr[mCurrMinPos].hashCode())
					mCurrMinPos = j;
			}
			cl Temp = iArr[mI];
			iArr[mI] = iArr[mCurrMinPos];
			iArr[mCurrMinPos] = Temp;
			Log.d("ret false", Integer.toString(mI));
			cubeEngine.drawTouchPoint(false);
			mI++;
			return false;			
		}
	cubeEngine.drawTouchPoint(true);		
	return true;
	};
	int GetCurrentPos()
	{
		return mCurrMinPos;
	};
};
class InsertionMethodSort<cl>
{
	private cl iArr[];
	private int mCurrMinPos;
	private boolean bFirstStage;
	private int mI,mR;
	private CubeEngine cubeEngine;
	private final Handler mHandler = new Handler();
    private final Runnable Turn = new Runnable() 
    {
        public void run() 
        {
            DoTurn();
        }
    };
    private void Wait()
    {
        long endTime = System.currentTimeMillis() + 2000;
        while (System.currentTimeMillis() < endTime)
        {}
    }
	InsertionMethodSort(CubeEngine cubeEngn, cl iArrToSort[])
	{
		cubeEngine = cubeEngn;
		iArr = iArrToSort;
		init__();
		DoTurn();
	};	
	InsertionMethodSort(cl iArrToSort[])
	{
		iArr = iArrToSort;
		init__();
	};

	void Init(cl iArrToSort[])
	{
		iArr = iArrToSort;
		init__();
	};
	private void init__()
	{
		bFirstStage = true;
		mR = iArr.length-1;
		mI = mR;	
	}
	cl [] GetCurrState()
	{
		Log.d("GetCurrState", "Getting current state");
		return iArr;
	};
	private void DoTurn()
	{
		boolean res=MakeTurn();
        mHandler.removeCallbacks(Turn);
        if (!res)
        	mHandler.postDelayed(Turn, 2000);		
	};
	boolean MakeTurn()
	{
		if (bFirstStage)
		{
			if (mI>0)
			{
				Log.d("bFirstStage true", Integer.toString(mI));
				if(iArr[mI].hashCode()<iArr[mI-1].hashCode())
				{
					cl Temp = iArr[mI];
					iArr[mI] = iArr[mI-1];
					iArr[mI-1] = Temp;
					Log.d("exchanged", Integer.toString(mI));					
				}
				--mI;
			}
			else
			{
				mI = 2;
				bFirstStage = false;
			}
			cubeEngine.drawTouchPoint(false);
			Log.d("returning false", Integer.toString(mI));	
			return false;
		}
		else
		{
			if (mI<=mR)
			{
				int j = mI;
				cl v = iArr[mI];
				while(v.hashCode()<iArr[j-1].hashCode())
				{
					iArr[j] = iArr[j-1];
					cubeEngine.drawTouchPoint(false);
					Wait();
					--j;
				}
				iArr[j] = v;
				cubeEngine.drawTouchPoint(false);
				++mI;	
				return false;
			}
		}
		cubeEngine.drawTouchPoint(true);		
		return true;
	};	
}