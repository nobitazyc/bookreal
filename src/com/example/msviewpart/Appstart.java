package com.example.msviewpart;



import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.view.Menu;
import android.view.WindowManager;
import android.widget.VideoView;

public class Appstart extends Activity{

	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);	
		setContentView(R.layout.appstart);
		
		
		new Handler().postDelayed(new Runnable(){
			@Override
			public void run(){
				Intent intent = new Intent (Appstart.this,Welcome.class);			
				startActivity(intent);			
				Appstart.this.finish();
			}
		}, 4000);
		
		getWindow().setFormat(PixelFormat.UNKNOWN);
        
        //Displays a video file.   
        VideoView mVideoView = new VideoView(this);
        
         
        String uriPath = "android.resource://"+ getPackageName() +"/"+R.raw.r_2;
        Uri uri = Uri.parse(uriPath);
        mVideoView.setVideoURI(uri);
        mVideoView.requestFocus();
        setContentView(mVideoView);
        mVideoView.start();
		
   }
}