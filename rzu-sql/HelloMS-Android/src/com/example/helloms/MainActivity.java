package com.example.helloms;

import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;
import android.app.Activity; 
import android.content.Intent;
import android.graphics.Point;

import com.example.FlyOutContainer.FlyOutContainer;
import com.moodstocks.android.MoodstocksError;
import com.moodstocks.android.Scanner;

public class MainActivity extends Activity implements Scanner.SyncListener {

	// Moodstocks API key/secret pair  
	private static final String API_KEY = "xe98xkhdfiiw94zgsfxn";
	private static final String API_SECRET = "4HDq3eBygA5N78nI";

	private boolean compatible = false;
	private Scanner scanner;
	TabHost th;
	FlyOutContainer root;
	View searchBar;
	View search;
	View searchBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		this.root = (FlyOutContainer)findViewById(R.id.FlyOutRoot);
		
		this.searchBar = (View)findViewById(R.id.searchbar);
		this.search = (View)findViewById(R.id.searchIndex);
		this.searchBtn = (View)findViewById(R.id.searchButton);
		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		int width = size.x;
		search.getLayoutParams().width = width;
		searchBtn.getLayoutParams().width = width;
		
		root.setMainView(findViewById(R.id.Main));
		
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
		//setup the tabs
		th = (TabHost)findViewById(R.id.tabhost);
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
		th.setCurrentTab(0);
		th.setOnTabChangedListener(new OnTabChangeListener(){
			public void onTabChanged(String tabId) {

				int i = th.getCurrentTab();
				 Log.i("@@@@@@@@ ANN CLICK TAB NUMBER", "------" + i);

				    if (i == 1) {
				    	
				    	Intent scanActivity = new Intent(getBaseContext(), ScanActivity.class);
				    	startActivity(scanActivity);

				    }
				    else if (i ==2) {
				        ;
				    }

				  }
			
		});
		
		
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
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
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
		startActivity(new Intent(this, ScanActivity.class));
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		th.setCurrentTab(0);
		super.onResume();
	}

	public void toggleMenu(View v)
	{
		//v = (LinearLayout)findViewById(R.id.resultContainer);
		//if (v.getVisibility()== View.GONE)
		//{
		//	Animation slideIn = AnimationUtils.loadAnimation(getBaseContext(),R.anim.slide_in );
		//	v.startAnimation(slideIn);
		//	v.setVisibility(View.VISIBLE);
		//}
		//else
		//{
		//	Animation slideOut = AnimationUtils.loadAnimation(getBaseContext(),R.anim.slide_out );
		//	v.startAnimation(slideOut);
		//	v.setVisibility(View.GONE);
		//}
		this.root.toggleMenu();
		
		
	}

	
	
}
