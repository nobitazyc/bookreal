package com.example.helloms;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.helloms.bookIO;
import com.example.helloms.R;
import com.moodstocks.android.MoodstocksError;
import com.moodstocks.android.Result;
import com.moodstocks.android.ScannerSession;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;
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
	private TextView resultTextView1;
	TabHost th;
	private String lastResult = "";
	//dsdsd
	String TAG = "message";
	bookIO bookIO = null;
	List<Map<String, Object>> list = null;
	Cursor cursor = null;
	bookIO db = null;
	private static final String DB_name = "book_db";
	private static final String Table_name = "book_info";
	SimpleAdapter simpleAdapter=null;
	Context context = null;
	EditText input=null;
	Button modBtn = null;
	Button insert_btn = null;
	Button cre_btn = null;
	Button Blur_Btn = null;
	Button All_Btn = null;
	ListView mListView=null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_scan);
		context = this;
		
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
		//
		//input = (EditText) findViewById(R.id.input_name);
		//modBtn = (Button) findViewById(R.id.upd_btn);
		insert_btn = (Button) findViewById(R.id.insert_btn);
		//cre_btn = (Button) findViewById(R.id.cre_btn);
		//Blur_Btn = (Button) findViewById(R.id.Blur_Btn);
		//All_Btn = (Button) findViewById(R.id.All_Btn);
		//mListView = (ListView) findViewById(R.id.mListView);
		
		
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
		resultTextView1 = (TextView) findViewById(R.id.scan_isbn);
	    resultTextView.setText("Scan result: N/A");
	    //cover = (ImageView) findViewById(R.id.imageView1);

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
		if (result != null && lastResult.equals(result.getValue())==false) {
			lastResult = result.getValue();
			new GoogleApiRequest().execute(lastResult);
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
	//Received ISBN from Barcode Scanner. Send to GoogleBooks to obtain book information.
	class GoogleApiRequest extends AsyncTask<String, Object, JSONObject>{

	 @Override
	 protected void onPreExecute() {
	     // Check network connection.

	 }
	 @Override
	 protected JSONObject doInBackground(String... isbns) {
	     // Stop if cancelled

	     String apiUrlString = "https://www.googleapis.com/books/v1/volumes?q=isbn:" + isbns[0];
	     resultTextView1.setText(isbns[0]);
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
	     } 
	     else if(responseJson == null)
	     {
	     }
	     else{
	         // All went well. Do something with your new JSONObject.
	    	 String search="not found";
	    		 try {
	    			 JSONArray itemArray = responseJson.getJSONArray("items");
	    			 JSONObject itemObject = itemArray.getJSONObject(0);
	    			 JSONObject volumeInfoObject = itemObject.getJSONObject("volumeInfo");
					  search = volumeInfoObject.getString("title");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	 	   resultTextView.setText(search);
	 	  insert_btn.setOnClickListener(new OnClickListener() {

	 			@Override
	 			public void onClick(View arg0) {
	 				// TODO Auto-generated method stub
	 				if(((String) resultTextView.getText())=="Scan result: N/A"){
	 					Log.d(TAG, "不能插");
	 					Toast.makeText(context, "can't add to history", Toast.LENGTH_SHORT)
							.show();
	 					
	 				}
	 				else{
	 				Log.d(TAG, "插入数据到数据库");
	 				bookIO = new bookIO(context, DB_name);
	 				String isbn = resultTextView1.getText().toString();
	 				String name = resultTextView.getText().toString();
	 				int id = (int) (1 + Math.random() * 999);
	 				Log.d(TAG, "name="+name+",id="+id);
	 				if (name != null /*&& "".equals(name.trim())*/) {
	 					// 增加一条数据到数据库
	 					ContentValues values = new ContentValues();
	 					values.put("_id", id);
	 					values.put("name", name);
	 					values.put("isbn", "3");
	 					Log.d(TAG, "value" + id);
	 					bookIO.insert(Table_name, values);
	 					Toast.makeText(context, "Added to history，book name=" + name +"           isbn number is: "+ isbn, Toast.LENGTH_SHORT)
	 							.show();
	 					bookIO.close();
	 				}
	 				}
	 			}
	 		});
	     }
	     
	 }
	 
	 
	 
	 
	}
	
}
