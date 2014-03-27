package com.example.msviewpart;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

public class MyHistoryAdapter extends ArrayAdapter<String>  {
	Context context;
	int[] images;
	String[] titleArray;
	float[] rates;
	
	public MyHistoryAdapter(Context c,String[] titles, int[] ims, float[] rates)
	{		
		super(c,R.layout.single_row,R.id.infoTitle,titles);
		this.images = ims;
		this.titleArray = titles;
		this.rates = rates;
		this.context = c;
	}


	public View getView(int position, View convertView, ViewGroup parent)
	{
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View row = inflater.inflate(R.layout.history_row, parent,false);
		ImageView myImage = (ImageView)row.findViewById(R.id.imageView2);
		TextView myTitle = (TextView)row.findViewById(R.id.historyTitle);
		RatingBar Rating = (RatingBar)row.findViewById(R.id.historyRate);
		myImage.setImageResource(images[position]);
		myTitle.setText(this.titleArray[position]);
		Rating.setRating(this.rates[position]);
		return row;
	}
}
