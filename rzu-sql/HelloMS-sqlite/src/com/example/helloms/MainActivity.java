package com.example.helloms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TabHost;
import android.widget.Toast;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;
import android.app.Activity; 
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.helloms.DBOperation;
import com.example.helloms.R;
import com.moodstocks.android.MoodstocksError;
import com.moodstocks.android.Scanner;


public class MainActivity extends Activity implements Scanner.SyncListener {

	// Moodstocks API key/secret pair
	private static final String API_KEY = "aetsuxd9meraxzsdelzx";
	private static final String API_SECRET = "0dD3V9UHm4R8N6AP";

	private boolean compatible = false;
	private Scanner scanner;
	TabHost th;
	
	String TAG = "message";
	DBOperation dbOperation = null;
	List<Map<String, Object>> list = null;
	Cursor cursor = null;
	SQLiteDatabase db = null;
	private static final String DB_name = "Student_db";
	private static final String Table_name = "user";
	SimpleAdapter simpleAdapter=null;
	Context context = null;
	EditText input=null;
	Button modBtn = null;
	Button insert_btn = null;
	Button Blur_Btn = null;
	Button All_Btn = null;
	Button cre_btn = null;
	ListView mListView=null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		context = this;
		
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
		input = (EditText) findViewById(R.id.searchBook);
	
		insert_btn = (Button) findViewById(R.id.insert_btn);
		
		Blur_Btn = (Button) findViewById(R.id.Blur_Btn);
		All_Btn = (Button) findViewById(R.id.All_Btn);
		mListView = (ListView) findViewById(R.id.mListView);
		cre_btn = (Button) findViewById(R.id.cre_btn);
		
		cre_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Log.d(TAG, "创建数据库");
				dbOperation = new DBOperation(context, DB_name);
				dbOperation.getReadableDatabase();
			}
		});
		
	
				insert_btn.setOnClickListener(new OnClickListener() {

					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						Log.d(TAG, "插入数据到数据库");
						dbOperation = new DBOperation(context, DB_name);
						String name = input.getText().toString();
						int id = (int) (1 + Math.random() * 999);
						Log.d(TAG, "name="+name+",id="+id);
						if (name != null /*&& "".equals(name.trim())*/) {
							// 增加一条数据到数据库
							ContentValues values = new ContentValues();
							values.put("_id", id);
							values.put("name", name);
							Log.d(TAG, "value" + id);
							dbOperation.insert(Table_name, values);
							Toast.makeText(context, "插入成功，ID=" + id, Toast.LENGTH_SHORT)
									.show();
							dbOperation.close();
							
						}
					}
				});
			
				// 全部查询
				All_Btn.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Log.d(TAG, "全部查询");
						dbOperation = new DBOperation(context, DB_name);
						Cursor cursor = dbOperation.query(Table_name);
						list = new ArrayList<Map<String, Object>>();
						for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
								.moveToNext()) {
							Map<String, Object> map = new HashMap<String, Object>();
							map.put("_id", cursor.getInt(cursor.getColumnIndex("_id")));
							map.put("name",
									cursor.getString(cursor.getColumnIndex("name")));
							list.add(map);
							Log.d(TAG, "全部查询_list");
						}
						SimpleAdapter simpleAdapter = new SimpleAdapter(context, list,
								R.layout.list_item, new String[] { "_id", "name" },
								new int[] { R.id.itemsId, R.id.itemsTxt });
						Log.d(TAG, simpleAdapter.toString());
						mListView.setAdapter(simpleAdapter);
					}
				});
				// 根据关键字查询
				Blur_Btn.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Log.d(TAG, "根据关键字查询");
						list.removeAll(list);
						dbOperation = new DBOperation(context, DB_name);
						Cursor cursor = dbOperation.query(Table_name, input.getText()
								.toString());
						for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
								.moveToNext()) {
							Map<String, Object> map = new HashMap<String, Object>();
							map.put("_id", cursor.getInt(cursor.getColumnIndex("_id")));
							map.put("name",
									cursor.getString(cursor.getColumnIndex("name")));
							list.add(map);
							Log.d(TAG, "根据关键字查询_list");
						}
						SimpleAdapter simpleAdapter = new SimpleAdapter(context, list,
								R.layout.list_item, new String[] { "_id", "name" },
								new int[] { R.id.itemsId, R.id.itemsTxt });
						mListView.setAdapter(simpleAdapter);
					}
				});
		
		
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
	
	
}
