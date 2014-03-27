package com.example.helloms;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
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
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;
import android.app.Activity; 
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Point;

import com.example.FlyOutContainer.FlyOutContainer;
import com.example.helloms.ScanActivity.GoogleApiRequest;
import com.moodstocks.android.MoodstocksError;
import com.moodstocks.android.Scanner;

public class MainActivity extends Activity implements Scanner.SyncListener {

	// Moodstocks API key/secret pair  
	Context Main;
	private static final String API_KEY = "xe98xkhdfiiw94zgsfxn";
	private static final String API_SECRET = "4HDq3eBygA5N78nI";

	private boolean compatible = false;
	private Scanner scanner;
	TabHost th;
	FlyOutContainer root;
	View searchBar;
	SearchView search;
	View searchBtn;
	private ListView bookInfo;
	MyAdapter adapter;
	private String[] infoTitles;
	private String[] infoDescriptions={"","","","","","",""};
	private int[] images = {R.drawable.title,R.drawable.author,R.drawable.publisher,R.drawable.publishdate,R.drawable.language,R.drawable.introduction,R.drawable.cover};
	private ListView HistoryList;
	MyHistoryAdapter historyAdapter;
	private float[] rates={2,3,4,3,2,4,1};
	private String[] historyBookTitles={"aasdfa","sadfasdf","asdfasdf","dfasdf","dfasdf","dsfasdfa","sdfasdf"};
	private int[] images2 = {R.drawable.title,R.drawable.title,R.drawable.title,R.drawable.title,R.drawable.title,R.drawable.title,R.drawable.title};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Main = this;
		this.root = (FlyOutContainer)findViewById(R.id.FlyOutRoot);
		
		
		//set up the searchBar so that it will fit the screen size
		this.searchBar = (View)findViewById(R.id.searchbar);
		this.search = (SearchView)findViewById(R.id.searchIndex);
		this.searchBtn = (View)findViewById(R.id.searchButton);
		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		int width = size.x;
		search.getLayoutParams().width = width;
		searchBtn.getLayoutParams().width = width;
		root.setMainView(findViewById(R.id.Main));
		
		//set up the list of the bookInfo
		bookInfo = (ListView)findViewById(R.id.BookInfo);
		Resources res = getResources();
		infoTitles = res.getStringArray(R.array.bookinfo);
		adapter = new MyAdapter(this, infoTitles, images, infoDescriptions);
		bookInfo.setAdapter(adapter);
		
		
		
		
		
		
		
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
				    	startActivityForResult(scanActivity,0);

				    }
				    else if (i ==2) {
				    	HistoryList = (ListView)findViewById(R.id.HistoryList);
						historyAdapter = new MyHistoryAdapter(Main, historyBookTitles, images2, rates);
						HistoryList.setAdapter(historyAdapter);
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
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch(requestCode){
		case 0:{
			if(resultCode == Activity.RESULT_OK)
			{
				String value = data.getStringExtra("ISBN");
				new GoogleApiRequest().execute(value);
				this.root.toggleMenu();
			}
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
	
	public void search(View v)
	{
		
		String value;
		value =this.search.getQuery().toString();
		if(!value.isEmpty())
		{
			new GoogleApiRequest().execute(value);
		}
		
		this.root.toggleMenu();
		
		
	
	}

	//Received ISBN from Barcode Scanner. Send to GoogleBooks to obtain book information.
		class GoogleApiRequest extends AsyncTask<String, Object, JSONObject>{

		 @Override
		 protected void onPreExecute() {
		     // Check network connection.

		 }
		 @Override
		 protected JSONObject doInBackground(String... keywords) {
		     // Stop if cancelled

		     String apiUrlString = "https://www.googleapis.com/books/v1/volumes?q=" + keywords[0];
		     try{
		         HttpURLConnection connection = null;
		         // Build Connection.
		         try{
		             URL url = new URL(apiUrlString);
		             connection = (HttpURLConnection) url.openConnection();
		             connection.setRequestMethod("GET");
		             connection.setReadTimeout(5000); // 5 seconds
		             connection.setConnectTimeout(5000); // 5 seconds
		         } catch (MalformedURLException e) {
		             // Impossible: The only two URLs used in the app are taken from string resources.
		             e.printStackTrace();
		         }
		         int responseCode = connection.getResponseCode();
		         if(responseCode != 200){
		             Log.w(getClass().getName(), "GoogleBooksAPI request failed. Response Code: " + responseCode);
		             connection.disconnect();
		             return null;
		         }

		         // Read data from response.
		         StringBuilder builder = new StringBuilder();
		         BufferedReader responseReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		         String line = responseReader.readLine();
		         while (line != null){
		             builder.append(line);
		             line = responseReader.readLine();
		         }
		         String responseString = builder.toString();
		         Log.d(getClass().getName(), responseString);
		         JSONObject responseJson = new JSONObject(responseString);
		         // Close connection and return response code.
		         connection.disconnect();
		         return responseJson;
		     } catch (SocketTimeoutException e) {
		         Log.w(getClass().getName(), "Connection timed out. Returning null");
		         return null;
		     } catch(IOException e){
		         Log.d(getClass().getName(), "IOException when connecting to Google Books API.");
		         e.printStackTrace();
		         return null;
		     } catch (JSONException e) {
		         Log.d(getClass().getName(), "JSONException when connecting to Google Books API.");
		         e.printStackTrace();
		         return null;
		     }
		 }
		 @Override
		 protected void onPostExecute(JSONObject responseJson) {
		     if(isCancelled()){
		         // Request was cancelled due to no network connection.
		     } else if(responseJson == null){
		     }
		     else{
		         // All went well. Do something with your new JSONObject.
		    	 String title="not found";
		    	 String author="not found";
		    	 String publisher="not found";
		    	 String publishDate="not found";
		    	 String description="not found";
		    	 String language="not found";
		    		 try {
		    			 JSONArray itemArray = responseJson.getJSONArray("items");
		    			 JSONObject itemObject = itemArray.getJSONObject(0);
		    			 JSONObject volumeInfoObject = itemObject.getJSONObject("volumeInfo");
						 title = volumeInfoObject.getString("title");
						 try
						 {
							 publishDate = volumeInfoObject.getString("publishedDate");
						 }
						 catch(JSONException e)
						 {
							 publishDate="not found";
						 }
						 try
						 {
							 description = volumeInfoObject.getString("description");
						 }
						 catch(JSONException e)
						 {
							 description="not found";
						 }
						 try
						 {
							 language = volumeInfoObject.getString("language");
						 }
						 catch(JSONException e)
						 {
							 language="not found";
						 }
						 try
						 {
							 JSONArray authors= volumeInfoObject.getJSONArray("authors");
							 author = "";
							 for(int i = 0; i< authors.length();i++ )
							 {
								 String item = authors.getString(i);
								 author += item;
							 }
						 }
						 catch(JSONException e)
						 {
							 author="not found";
						 }
						 try
						 {
							 publisher = volumeInfoObject.getString("publisher");
						 }
						 catch(JSONException e)
						 {
							 publisher="not found";
						 }
						 
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		    		 infoDescriptions[0]=title;
		    		 infoDescriptions[1]=author;
		    		 infoDescriptions[2]=publisher;
		    		 infoDescriptions[3]=publishDate;
		    		 infoDescriptions[4]=language;
		    		 infoDescriptions[5]=description;
		    		 adapter = new MyAdapter(Main, infoTitles, images, infoDescriptions);
		    		 bookInfo.setAdapter(adapter);
		    		 adapter.notifyDataSetChanged();
		     }
		 }
		}
	
}
