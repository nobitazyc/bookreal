package com.example.msviewpart;

import com.moodstocks.android.MoodstocksError;
import com.moodstocks.android.Scanner;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.content.Intent;

public class Welcome extends Activity implements Scanner.SyncListener {

	// Moodstocks API key/secret pair
		private static final String API_KEY = "aetsuxd9meraxzsdelzx";
		private static final String API_SECRET = "0dD3V9UHm4R8N6AP";

		private boolean compatible = false;
		private Scanner scanner;
		
		public void welcome_login(View v) {  
	      	Intent intent = new Intent();
			intent.setClass(Welcome.this,Login.class);
			startActivity(intent);
			//this.finish();
	      }  
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);
		
		compatible = Scanner.isCompatible();
		if (compatible) {
			try {
				this.scanner = Scanner.get();
				scanner.open(this, API_KEY, API_SECRET);
				scanner.sync(this);
			} catch (MoodstocksError e) {
				e.log();
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (compatible) {
			try {
				scanner.close();
			} catch (MoodstocksError e) {
				e.log();
			}
		}
	}
	
	@Override
	public void onSyncStart() {
		Log.d("Moodstocks SDK", "Sync will start.");
	}

	@Override
	public void onSyncComplete() {
		try {
			Log.d("Moodstocks SDK", String.format("Sync succeeded (%d image(s))", scanner.count()));
		} catch (MoodstocksError e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onSyncFailed(MoodstocksError e) {
		Log.d("Moodstocks SDK", "Sync error: " + e.getErrorCode());
	}

	@Override
	public void onSyncProgress(int total, int current) {
		int percent = (int) ((float) current / (float) total * 100);
		Log.d("Moodstocks SDK", String.format("Sync progressing: %d%%", percent));
	}
	
	public void onScanButtonClicked(View view) {
		compatible = Scanner.isCompatible();
		if (compatible) {
			try {
				this.scanner = Scanner.get();
				scanner.open(this, API_KEY, API_SECRET);
				scanner.sync(this);
			} catch (MoodstocksError e) {
				e.log();
			}
		}
        startActivity(new Intent(this, ScanActivity.class));
    }
	

}
