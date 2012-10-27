package com.example.android.livecubes.cube3;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;

import com.example.android.livecubes.R;

public class SortSettings extends PreferenceActivity
implements SharedPreferences.OnSharedPreferenceChangeListener 
{
	@Override
	protected void onCreate(Bundle icicle) {
	    super.onCreate(icicle);
	    getPreferenceManager().setSharedPreferencesName(
	            Cubewallpaper3.SHARED_PREFS_NAME);
	    addPreferencesFromResource(R.xml.sort_settings);
	    getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(
	            this);
	}
	
	@Override
	protected void onResume() {
	    super.onResume();
	}
	
	@Override
	protected void onDestroy() {
	    getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(
	            this);
	    super.onDestroy();
	}
	
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
	        String key) {
	}

}
