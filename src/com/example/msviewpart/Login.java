package com.example.msviewpart;

import java.io.IOException;
import java.util.List;



import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;

import android.view.View;
import android.widget.EditText;


public class Login extends Activity {
	private EditText mUser; // ÕÊºÅ±à¼­¿ò
	private EditText mPassword; // ÃÜÂë±à¼­¿ò
	
	List<Person> persons;
	Person currentPerson;
	Bundle bundle;
	

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        
        
        bundle = new Bundle();
        
        try {
			persons = DomPerson.parserXmlByDom(getApplicationContext().getAssets().open("persons.xml"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        mUser = (EditText)findViewById(R.id.login_user_edit);
        mPassword = (EditText)findViewById(R.id.login_passwd_edit);
        
    }

    public void login_mainweixin(View v) {
    	
    	for (int i = 0; i< persons.size(); i++) {
			if (mUser.getText().toString().equals(persons.get(i).getName()) && mPassword.getText().toString().equals(persons.get(i).getPassword())) {
				currentPerson = persons.get(i);
				break;
			}
		}
    	
    	if(currentPerson != null && currentPerson.getName().equals(mUser.getText().toString()) && currentPerson.getPassword().equals(mPassword.getText().toString()))   //ÅÐ¶Ï ÕÊºÅºÍÃÜÂë
        {
    		Intent intent = new Intent(getApplicationContext(), ListActivity.class);
			bundle.putString("username", currentPerson.getName());
			bundle.putString("bookneed", currentPerson.getBookneed());
			intent.putExtra("bundle", bundle);
			startActivity(intent);
        }
        else if("".equals(mUser.getText().toString()) || "".equals(mPassword.getText().toString()))   //ÅÐ¶Ï ÕÊºÅºÍÃÜÂë
        {
        	new AlertDialog.Builder(Login.this)
			.setIcon(getResources().getDrawable(R.drawable.login_error_icon))
			.setTitle("Login failed.")
			.setMessage("Username or password are incorrect.\nPlease try again!")
			.create().show();
         }
        else{
           
        	new AlertDialog.Builder(Login.this)
			.setIcon(getResources().getDrawable(R.drawable.login_error_icon))
			.setTitle("Login failed.")
			.setMessage("Username or password are incorrect.\nPlease try again!")
			.create().show();
        }
    	
    	//µÇÂ¼°´Å¥
    	/*
      	Intent intent = new Intent();
		intent.setClass(Login.this,Whatsnew.class);
		startActivity(intent);
		Toast.makeText(getApplicationContext(), "µÇÂ¼³É¹¦", Toast.LENGTH_SHORT).show();
		this.finish();*/
      }  
    public void login_back(View v) {     //±êÌâÀ¸ ·µ»Ø°´Å¥
      	this.finish();
      }  
    public void login_pw(View v) {     //Íü¼ÇÃÜÂë°´Å¥
    	Uri uri = Uri.parse("http://3g.qq.com"); 
    	Intent intent = new Intent(Intent.ACTION_VIEW, uri); 
    	startActivity(intent);
    	//Intent intent = new Intent();
    	//intent.setClass(Login.this,Whatsnew.class);
        //startActivity(intent);
      }  
    

}
