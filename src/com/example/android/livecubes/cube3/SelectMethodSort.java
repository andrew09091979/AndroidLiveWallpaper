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
	SelectMethodSort(cl iArrToSort[])
	{
		mI = 0;
		mCurrMinPos = mI;
		iArr = iArrToSort;
		mJ = mI+1;
		mR = iArr.length;
		mTurn = 0;
	};
	void Init(cl iArrToSort[])
	{
		mI = 0;
		mCurrMinPos = mI;
		iArr = iArrToSort;
		mJ = mI+1;
		mR = iArr.length;
		mTurn = 0;
	};
	cl [] GetCurrState()
	{
		return iArr;
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
			Log.d("ret true", Integer.toString(mI));
			mI++;
			return true;			
		}
	return false;
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
		MakeTurn();
        mHandler.removeCallbacks(Turn);
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
			cubeEngine.drawTouchPoint(true);
			Log.d("returning true", Integer.toString(mI));	
			return true;
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
					cubeEngine.drawTouchPoint(true);
					--j;
				}
				iArr[j] = v;
				++mI;
				return true;
			}
		}
		cubeEngine.drawTouchPoint(false);
        mHandler.removeCallbacks(Turn);		
		return false;
	};	
}