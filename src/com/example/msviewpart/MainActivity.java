package com.example.msviewpart;

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
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity; 
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Point;

import com.example.FlyOutContainer.FlyOutContainer;
import com.example.msviewpart.ScanActivity.GoogleApiRequest;
import com.moodstocks.android.MoodstocksError;
import com.moodstocks.android.Scanner;

public class MainActivity extends Activity implements Scanner.SyncListener{

	// Moodstocks API key/secret pair  
	Context Main;
	View masterView;
	TextView fake;
	TextView fake2;
	String getISBN;
	int getBookId;
	private static final String API_KEY = "aetsuxd9meraxzsdelzx";
	private static final String API_SECRET = "0dD3V9UHm4R8N6AP";

	private boolean compatible = false;
	private Scanner scanner;
	TabHost th;
	FlyOutContainer root;
	View menu;
	View searchBar;
	SearchView search;
	View searchBtn;
	private GestureDetector gd;
	View.OnTouchListener gestureListener;
	private ListView bookInfo;
	MyAdapter adapter;
	private String[] infoTitles;
	private int[] images = {R.drawable.scan,R.drawable.map,R.drawable.title,R.drawable.author,R.drawable.publisher,R.drawable.publishdate,R.drawable.language,R.drawable.introduction,R.drawable.search};
	private ListView HistoryList;
	MyHistoryAdapter historyAdapter;
	private float[] rates={2,3,4,3,2,4,1};
	private String[] historyBookTitles={"aasdfa","sadfasdf","asdfasdf","dfasdf","dfasdf","dsfasdfa","sdfasdf"};
	private int[] images2 = {R.drawable.title,R.drawable.title,R.drawable.title,R.drawable.title,R.drawable.title,R.drawable.title,R.drawable.title};
	private int[] maps = {R.drawable.map1,R.drawable.map2,R.drawable.map3,R.drawable.map4,R.drawable.map5,R.drawable.map5,R.drawable.map6,R.drawable.map5,R.drawable.map2,R.drawable.map4};

	Animation fadeOut;
	Animation fadeIn;
	LinearLayout searchGroup;
	LinearLayout titleGroup;
	LinearLayout authorGroup;
	LinearLayout publisherGroup;
	LinearLayout publishDateGroup;
	LinearLayout languageGroup;
	LinearLayout introductionGroup;
	LinearLayout logoutGroup;
	LinearLayout mapGroup;
	TextView titleContent;
	TextView authorContent;
	TextView publisherContent;
	TextView publishDateContent;
	TextView languageContent;
	TextView introductionContent;
	ImageView searchGroupBg;
	ImageView titleGroupBg;
	ImageView mapGroupBg;
	int layoutscount = 0;
	int lastposition = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mapGroupBg = (ImageView)findViewById(R.id.mapbg);
		
		Bundle extras = getIntent().getExtras();
		  if (extras != null) {
		   Bundle bundle = extras.getBundle("bundle");
		   getISBN = bundle.getString("ISBN");
		   getBookId = bundle.getInt("BookId");
		   if(getISBN != null && !getISBN.isEmpty())
		   {
			   new GoogleApiRequest().execute(getISBN);
			   mapGroupBg.setBackgroundResource(maps[getBookId]);
		   }
		}  
		Main = this;
		fake = (TextView)findViewById(R.id.fakeArea);
		fake2 = (TextView)findViewById(R.id.fakeArea2);
		this.root = (FlyOutContainer)findViewById(R.id.FlyOutRoot);
		
		
		//set up the searchBar so that it will fit the screen size
		searchGroup = (LinearLayout)findViewById(R.id.searchGroup);
		this.searchBar = (View)findViewById(R.id.searchbar);
		this.search = (SearchView)findViewById(R.id.searchIndex);
		this.searchBtn = (View)findViewById(R.id.searchButton);
		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		int width = size.x;
		searchGroup.getLayoutParams().width = width-185;
		search.getLayoutParams().width = width-185;
		searchBtn.getLayoutParams().width = width-185;
		root.setMainView(findViewById(R.id.Main));
		searchGroupBg = (ImageView)findViewById(R.id.searchbg);
		
		mapGroup = (LinearLayout)findViewById(R.id.mapGroup);
		mapGroup.getLayoutParams().width = width;
		
		
		titleGroup = (LinearLayout)findViewById(R.id.titleGroup);
		titleGroup.getLayoutParams().width = width;
		titleGroupBg = (ImageView)findViewById(R.id.titlebg);
		titleContent = (TextView)findViewById(R.id.titleContent);
		
		authorGroup = (LinearLayout)findViewById(R.id.authorGroup);
		authorGroup.getLayoutParams().width = width;
		ImageView authorGroupBg = (ImageView)findViewById(R.id.authorbg);
		authorContent = (TextView)findViewById(R.id.authorContent);
		
		publisherGroup = (LinearLayout)findViewById(R.id.publisherGroup);
		publisherGroup.getLayoutParams().width = width;
		ImageView publisherGroupBg = (ImageView)findViewById(R.id.publisherbg);
		publisherContent = (TextView)findViewById(R.id.publisherContent);
		
		publishDateGroup = (LinearLayout)findViewById(R.id.publishDateGroup);
		publishDateGroup.getLayoutParams().width = width;
		ImageView publishDateGroupBg = (ImageView)findViewById(R.id.publishdatebg);
		publishDateContent = (TextView)findViewById(R.id.publishDateContent);
		
		languageGroup = (LinearLayout)findViewById(R.id.languageGroup);
		languageGroup.getLayoutParams().width = width;
		ImageView languageGroupBg = (ImageView)findViewById(R.id.languagebg);
		languageContent = (TextView)findViewById(R.id.languageContent);
		
		
		introductionGroup = (LinearLayout)findViewById(R.id.introductionGroup);
		introductionGroup.getLayoutParams().width = width-150;
		ImageView introductionGroupBg = (ImageView)findViewById(R.id.introductionbg);
		introductionContent = (TextView)findViewById(R.id.introductionContent);
		
		
		final LinearLayout[] layouts = {mapGroup,titleGroup,authorGroup,publisherGroup,publishDateGroup,languageGroup,introductionGroup,searchGroup};
		final ImageView[] bgs = {mapGroupBg,titleGroupBg,authorGroupBg,publisherGroupBg,publishDateGroupBg,languageGroupBg,introductionGroupBg,searchGroupBg};
		
		//final Animation slideIn = AnimationUtils.loadAnimation(this, R.anim.slide_in);
		
		fadeOut = AnimationUtils.loadAnimation(this,R.anim.fade_out);
		fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
		
		
		//set up the list of the bookInfo
		bookInfo = (ListView)findViewById(R.id.BookInfo);
		Resources res = getResources();
		infoTitles = res.getStringArray(R.array.bookinfo);
		adapter = new MyAdapter(this, infoTitles, images);
		bookInfo.setAdapter(adapter);
		bookInfo.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				
				if(arg2>0)
				{
					if(layoutscount == 0)
					{
						mapGroup.setVisibility(View.GONE);
						mapGroupBg.setVisibility(View.GONE);
					}
					else
					{
					
						layouts[lastposition-1].setVisibility(View.GONE);
						bgs[lastposition-1].setVisibility(View.GONE);
					}
					layouts[arg2-1].startAnimation(fadeIn);
					layouts[arg2-1].setVisibility(View.VISIBLE);
					bgs[arg2-1].startAnimation(fadeIn);
					bgs[arg2-1].setVisibility(View.VISIBLE);
					lastposition = arg2;
					layoutscount++;
					//root.setMenuState(FlyOutContainer.MenuState.CLOSED);
					//root.openMenu();
				}
				else
				{
					Intent scanActivity = new Intent(getBaseContext(), ScanActivity.class);
			    	startActivityForResult(scanActivity,0);
				}
			}
			
		});
		
		
		
		this.menu = (View)findViewById(R.id.resultContainer);
		gd = new GestureDetector(this,new OnGestureListener(){

			@Override
			public boolean onDown(MotionEvent e) {
				 
				// TODO Auto-generated method stub
				return true;
			}

			@Override
			public boolean onFling(MotionEvent e1, MotionEvent e2,
					float velocityX, float velocityY) {
					 boolean result = false;
			            try {
			                float diffX = e2.getX() - e1.getX();
			                
		                    if (Math.abs(diffX) > 50) {
		                        if (diffX > 0) {
		                          // root.openMenu();
		                    
		                        } else {
		                         // root.closeMenu();
		                        }
		                    }
			                
			            } catch (Exception exception) {
			                exception.printStackTrace();
			            }
			       return result;
			}

			@Override
			public void onLongPress(MotionEvent e) {
				//root.toggleMenu();
				// TODO Auto-generated method stub
				
			}

			@Override
			public boolean onScroll(MotionEvent e1, MotionEvent e2,
					float distanceX, float distanceY) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public void onShowPress(MotionEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public boolean onSingleTapUp(MotionEvent e) {
				// TODO Auto-generated method stub
				return false;
			}
			
		});
		titleGroupBg.setOnTouchListener(new OnTouchListener(){

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				
				if(gd.onTouchEvent(event))
				{
					return true;
				}
				return false;
			}
			
		});
		authorGroupBg.setOnTouchListener(new OnTouchListener(){

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				
				if(gd.onTouchEvent(event))
				{
					return true;
				}
				return false;
			}
			
		});
		publisherGroupBg.setOnTouchListener(new OnTouchListener(){

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				
				if(gd.onTouchEvent(event))
				{
					return true;
				}
				return false;
			}
			
		});
		publishDateGroupBg.setOnTouchListener(new OnTouchListener(){

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				
				if(gd.onTouchEvent(event))
				{
					return true;
				}
				return false;
			}
			
		});
		languageGroupBg.setOnTouchListener(new OnTouchListener(){

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				
				if(gd.onTouchEvent(event))
				{
					return true;
				}
				return false;
			}
			
		});
		introductionGroupBg.setOnTouchListener(new OnTouchListener(){

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				
				if(gd.onTouchEvent(event))
				{
					return true;
				}
				return false;
			}
			
		});
		fake.setOnTouchListener(new OnTouchListener(){

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				
				if(gd.onTouchEvent(event))
				{
					return true;
				}
				return false;
			}
			
		});
		fake2.setOnTouchListener(new OnTouchListener(){

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				
				if(gd.onTouchEvent(event))
				{
					return true;
				}
				return false;
			}
			
		});
		
		
		
		
		
		
		
		
		
		
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
		super.onResume();
	}

	
	public void search(View v)
	{
		
		String value;
		value =this.search.getQuery().toString();
		if(!value.isEmpty())
		{
			new GoogleApiRequest().execute(value);
			searchGroup.setVisibility(View.GONE);
			searchGroupBg.setVisibility(View.GONE);
			titleGroup.startAnimation(fadeIn);
			titleGroup.setVisibility(View.VISIBLE);
			titleGroupBg.startAnimation(fadeIn);
			titleGroupBg.setVisibility(View.VISIBLE);
			lastposition = 2;
			mapGroupBg.setBackgroundResource(R.drawable.mapnull);
		}
		
		InputMethodManager imm = (InputMethodManager)getSystemService(
			      Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(search.getWindowToken(), 0);
	
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
		    		titleContent.setText(title);
		    		authorContent.setText(author);
		    		publisherContent.setText(publisher);
		    		publishDateContent.setText(publishDate);
		    		introductionContent.setText(description);
		    		languageContent.setText(language);
		     }
		 }
		}

		

		

	
}
