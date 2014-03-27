package com.example.msviewpart;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class ListActivity extends Activity {
	
	Bundle bundle;
	
	ArrayList<HashMap<String, String>> productsList;
	List<Book> books;
	
	ListView lv;
	ProgressDialog pd; 
	
	private static final String TAG_IMAGE_NAME = "imageName";
    private static final String TAG_BOOK_NAME = "bookName";
    private static final String TAG_BOOK_AUTHOR = "bookAuthor";
    private static final String TAG_BOOK_PRICE = "bookPrice";

	private static final int IO_BUFFER_SIZE = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list);
		
		pd = new ProgressDialog(this);
		pd.setMessage("Downloading Image");
		
		Bundle extras = getIntent().getExtras();
		  if (extras != null) {
		   bundle = extras.getBundle("bundle");
		}  
		  
		String [] bundleBooks = bundle.get("bookneed").toString().split(",");
		
		try {
			books = DomBook.parserXmlByDom(getApplicationContext().getAssets().open("books.xml"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		productsList = new ArrayList<HashMap<String,String>>();
		lv = (ListView) findViewById(R.id.bookList);;
		
		for (int i=0; i < bundleBooks.length; i++ ) {
			
			for (int j = 0; j < books.size(); j ++) {
				
				if (bundleBooks[i].equals(String.valueOf(books.get(j).getId()))) {
					
//					String isbn = books.get(j).getIsbn();
					
					HashMap<String, String> map = new HashMap<String, String>();
					
					int bookId = books.get(j).getId();
					switch (bookId) {
						case 1 :
							map.put(TAG_IMAGE_NAME, String.valueOf(R.drawable.b1));
							break;
						case 2 :
							map.put(TAG_IMAGE_NAME, String.valueOf(R.drawable.b2));
							break;
						case 3 :
							map.put(TAG_IMAGE_NAME, String.valueOf(R.drawable.b3));
							break;
						case 4 :
							map.put(TAG_IMAGE_NAME, String.valueOf(R.drawable.b4));
							break;
						case 5 :
							map.put(TAG_IMAGE_NAME, String.valueOf(R.drawable.b5));
							break;
						case 6 :
							map.put(TAG_IMAGE_NAME, String.valueOf(R.drawable.b6));
							break;
						case 7 :
							map.put(TAG_IMAGE_NAME, String.valueOf(R.drawable.b7));
							break;
						case 8 :
							map.put(TAG_IMAGE_NAME, String.valueOf(R.drawable.b8));
							break;
						case 9 :
							map.put(TAG_IMAGE_NAME, String.valueOf(R.drawable.b9));
							break;
					}
					
					map.put(TAG_BOOK_NAME, books.get(j).getName());
					map.put(TAG_BOOK_AUTHOR, "by "+ books.get(j).getAuthor());
					map.put(TAG_BOOK_PRICE, "$" + String.valueOf(books.get(j).getPrice()));
				
					productsList.add(map);
					
				}
				
			}
			
			
		}
		
		
		 ListAdapter adapter = new SimpleAdapter(ListActivity.this, productsList, R.layout.list_row, new String[] { TAG_IMAGE_NAME, TAG_BOOK_NAME, TAG_BOOK_AUTHOR, TAG_BOOK_PRICE}, new int[] { R.id.imageName, R.id.bookName, R.id.bookAuthor, R.id.bookPrice });
         lv.setAdapter(adapter);
         
         lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				
				Bundle bundle = new Bundle();
				bundle.putString("ISBN", books.get(arg2).getIsbn());
				bundle.putInt("BookId",  books.get(arg2).getId());
				Intent i = new Intent(getApplicationContext(), MainActivity.class);
				i.putExtra("bundle", bundle);
				startActivity(i);
				
			}
        	 
        	 
        	 
		});
         
       
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.list, menu);
		return true;
	}
	
}
