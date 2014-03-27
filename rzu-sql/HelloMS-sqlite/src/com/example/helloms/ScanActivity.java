package com.example.helloms;

import com.moodstocks.android.MoodstocksError;
import com.moodstocks.android.Result;
import com.moodstocks.android.ScannerSession;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;

public class ScanActivity extends Activity implements ScannerSession.Listener {

	private int ScanOptions = Result.Type.IMAGE
			| Result.Type.EAN8
			| Result.Type.EAN13
			| Result.Type.QRCODE
			| Result.Type.DATAMATRIX;

	private ScannerSession session;
	private TextView resultTextView;
	TabHost th;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_scan);

		th= (TabHost)findViewById(R.id.tabhost);
		th.setup();
		TabSpec specs = th.newTabSpec("tag1");
		specs.setContent(R.id.search);
		specs.setIndicator("Search");
		th.addTab(specs);
		specs = th.newTabSpec("tag2");
		specs.setContent(R.id.scan);
		specs.setIndicator("Scan");
		th.addTab(specs);
		specs = th.newTabSpec("tag3");
		specs.setContent(R.id.history);
		specs.setIndicator("History");
		th.addTab(specs);
		
		th.setCurrentTab(1);
		th.setOnTabChangedListener(new OnTabChangeListener(){
			public void onTabChanged(String tabId) {

				int i = th.getCurrentTab();
				 Log.i("@@@@@@@@ ANN CLICK TAB NUMBER", "------" + i);

				    if (i == 0) {
				    	
				    	Intent searchActivity = new Intent(getBaseContext(), MainActivity.class);
				    	searchActivity.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				    	startActivity(searchActivity);
				    	finish();

				    }
				    else if (i ==2) {
				        ;
				    }

				  }
			
		});
		// get the camera preview surface & result text view
		SurfaceView preview = (SurfaceView) findViewById(R.id.preview);
		
		resultTextView = (TextView) findViewById(R.id.scan_result);
	    resultTextView.setText("Scan result: N/A");

		// Create a scanner session
		try {
			session = new ScannerSession(this, this, preview);
		} catch (MoodstocksError e) {
			e.log();
		}

		// set session options
		session.setOptions(ScanOptions);
	}

	@Override
	protected void onResume() {
		super.onResume();
		session.resume();
	}

	@Override
	protected void onPause() {
		super.onPause();

		// pause the scanner session
		session.pause();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		// close the scanner session
		session.close();
	}

	@Override
	public void onApiSearchStart() {
		// TODO Auto-generated method stub
	}

	@Override
	public void onApiSearchComplete(Result result) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onApiSearchFailed(MoodstocksError e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onScanComplete(Result result) {
		if (result != null) {
			resultTextView.setText(String.format("Scan result: %s", result.getValue()));
		}
	}

	@Override
	public void onScanFailed(MoodstocksError error) {
		resultTextView.setText(String.format("Scan failed: %d", error.getErrorCode()));
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if (keyCode == KeyEvent.KEYCODE_BACK ) {
	    
	    	Intent searchActivity = new Intent(getBaseContext(), MainActivity.class);
	    	searchActivity.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
	    	startActivity(new Intent(searchActivity));
	    	finish();
	    }
	    return super.onKeyDown(keyCode, event);
	}
}
