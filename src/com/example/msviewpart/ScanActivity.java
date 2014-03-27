package com.example.msviewpart;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.moodstocks.android.MoodstocksError;
import com.moodstocks.android.Result;
import com.moodstocks.android.ScannerSession;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
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
	private ImageView cover;
	private RatingBar rating;
	private String search ="";
	private LinearLayout linearLayout1;
	private String imageUrl = "http://bks1.books.google.ca/books?id=QKqktgAACAAJ&printsec=frontcover&img=1&zoom=5&source=gbs_api";
	private URL onLineURL;
	TabHost th;
	private String lastResult = "";
	private boolean imageShow = false;
	private boolean textShow = false;
	private int showDelay = 4000;
	
	private Handler handler = new Handler();
	private Runnable runnable = new Runnable() {
		   @Override
		   public void run() {
		      /* do what you need to do */
			   if (imageShow == true){
		    		 Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.sun_push_left_out);
		    	     cover.setAnimation(anim);
		    	     anim.start();
		    	     imageShow = false;
		    	     cover.setVisibility(View.INVISIBLE);
		    	 }
		    	 if (textShow == true){
		    		 Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.push_bottom_out);
		    		 resultTextView.setAnimation(anim);
		    	     anim.start();
		    	     textShow = false;
		    	     linearLayout1.setVisibility(View.INVISIBLE);
		    	     lastResult = "";
		    	 }
		      /* and here comes the "trick" */
		      //handler.postDelayed(this, 100);
		   }
		};
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_scan);

		
		// get the camera preview surface & result text view
		SurfaceView preview = (SurfaceView) findViewById(R.id.preview);
		rating = (RatingBar) findViewById(R.id.ratingBar);
		resultTextView = (TextView) findViewById(R.id.scan_result);
	    resultTextView.setText("Scan result: N/A");
	    linearLayout1 = (LinearLayout) findViewById(R.id.linearLayout1);
	    linearLayout1.setOnClickListener(new View.OnClickListener() {
	        @Override
	        public void onClick(View v) {
	        	String isbn;
				try {
					isbn = lastResult.substring(1);
					Bundle bundle = new Bundle();
					bundle.putString("ISBN", isbn);
					Intent i = new Intent(getApplicationContext(), MainActivity.class);
					i.putExtra("bundle", bundle);
					startActivity(i);
				    finish();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        	
	        }
	    });
	    
	    cover = (ImageView) findViewById(R.id.imageView1);
	    cover.setOnClickListener(new View.OnClickListener() {
	        @Override
	        public void onClick(View v) {
	        	String isbn;
				try {
					isbn = lastResult.substring(1);
					Bundle bundle = new Bundle();
					bundle.putString("ISBN", isbn);
					Intent i = new Intent(getApplicationContext(), MainActivity.class);
					i.putExtra("bundle", bundle);
					startActivity(i);
				    finish();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        }
	    });
	    

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
		resultTextView.setText("Scan failed");
	}

	@Override
	public void onScanComplete(Result result) {
		if (result != null && lastResult.equals(result.getValue())==false) {
			lastResult = result.getValue();
			if (lastResult.length()==13 || lastResult.length()==9){
				MediaPlayer mPlayer1 = MediaPlayer.create(getBaseContext(), R.raw.barcode); // in 2nd param u have to pass your desire ringtone
				mPlayer1.start();
				Bundle bundle = new Bundle();
				bundle.putString("ISBN", lastResult);
				Intent i = new Intent(getApplicationContext(), MainActivity.class);
				i.putExtra("bundle", bundle);
				startActivity(i);
			    finish();
			}else{
				new GoogleApiRequest().execute(lastResult.substring(1));
				handler.removeCallbacks(runnable);
				handler.postDelayed(runnable, showDelay);
			}
			
		}
	}

	@Override
	public void onScanFailed(MoodstocksError error) {
		resultTextView.setText(String.format("Scan failed: %d", error.getErrorCode()));
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if (keyCode == KeyEvent.KEYCODE_BACK ) {
	    
	    	Intent searchActivity = new Intent(getBaseContext(), Welcome.class);
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
	    	 search="not found";
	    	 boolean noImage = false;
	    	 //Bitmap coverBitmap = getBitmapFromURL(imageUrl);
	    	 //new ImageDownloadTask().execute("http://bks1.books.google.ca/books?id=QKqktgAACAAJ&printsec=frontcover&img=1&zoom=5&source=gbs_api");
	    	 
	    	 
	    		 try {
	    			 JSONArray itemArray = responseJson.getJSONArray("items");
	    			 JSONObject itemObject = itemArray.getJSONObject(0);
	    			 JSONObject volumeInfoObject = itemObject.getJSONObject("volumeInfo");
					 search = volumeInfoObject.getString("title");
					  //Intent resultIntent = new Intent();
					  //resultIntent.putExtra("ISBN", lastResult);
					  //setResult(Activity.RESULT_OK,resultIntent);
				      //finish();
					  try {
						JSONObject imageLinksObject = volumeInfoObject.getJSONObject("imageLinks");
						String url = imageLinksObject.getString("thumbnail");
						imageUrl = url;
					 	  try {
					    		 onLineURL = new URL(imageUrl);
					    		   new MyNetworkTask(cover).execute(onLineURL);
					    		   
					    		  } catch (MalformedURLException e) {
					    		   e.printStackTrace();
					    		  }
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						cover.setVisibility(View.INVISIBLE);
						imageShow = false;
						noImage = true;
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	 	   
		   if (noImage) {
			   resultTextView.setText(search);
			Animation anim = AnimationUtils.loadAnimation(
					getApplicationContext(), R.anim.push_bottom_in);
			linearLayout1.setAnimation(anim);
			anim.start();
			linearLayout1.setVisibility(View.VISIBLE);
			textShow = true;
			MediaPlayer mPlayer2 = MediaPlayer.create(getBaseContext(), R.raw.image); // in 2nd param u have to pass your desire ringtone
		       mPlayer2.start();
		}
	     }
	 }
	}
	
	private class MyNetworkTask extends AsyncTask<URL, Void, Bitmap>{
	     
	     ImageView tIV;
	     
	     public MyNetworkTask(ImageView iv){
	      tIV = iv;
	     }

	  @Override
	  protected Bitmap doInBackground(URL... urls) {

	   Bitmap networkBitmap = null;
	   
	   URL networkUrl = urls[0]; //Load the first element
	   try {
		   URLConnection connection = networkUrl.openConnection();
		   connection.setUseCaches(true);
		   Object response = connection.getContent();
		   if (response instanceof Bitmap) {
		      return (Bitmap)response;
		   }else{
	      networkBitmap = BitmapFactory.decodeStream(
	      networkUrl.openConnection().getInputStream());
		   }
	   } catch (IOException e) {
	    e.printStackTrace();
	   }

	   return networkBitmap;
	  }

	  @Override
	  protected void onPostExecute(Bitmap result) {
		  
		  resultTextView.setText(search);
		   Animation animText = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.push_bottom_in);
		   linearLayout1.setAnimation(animText);
	       animText.start();
	       linearLayout1.setVisibility(View.VISIBLE);
	       textShow = true;
	   tIV.setImageBitmap(result);
	   
	   Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in);
       tIV.setAnimation(anim);
       anim.start();
       tIV.setVisibility(View.VISIBLE);
       imageShow = true;
       MediaPlayer mPlayer2 = MediaPlayer.create(getBaseContext(), R.raw.image); // in 2nd param u have to pass your desire ringtone
       mPlayer2.start();
	  }

	    }

}
