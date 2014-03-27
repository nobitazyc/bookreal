package com.example.msviewpart;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MyAdapter extends ArrayAdapter<String> {

	Context context;
	int[] images;
	String[] titleArray;
	public MyAdapter(Context c,String[] titles, int[] ims)
	{		
		super(c,R.layout.single_row,R.id.infoTitle,titles);
		this.images = ims;
		this.titleArray = titles;
		this.context = c;
	}


	public View getView(int position, View convertView, ViewGroup parent)
	{
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View row = inflater.inflate(R.layout.single_row, parent,false);
		ImageView myImage = (ImageView)row.findViewById(R.id.logoutbg);
		TextView myTitle = (TextView)row.findViewById(R.id.infoTitle);
		myImage.setImageResource(images[position]);
		myTitle.setText(this.titleArray[position]);
		return row;
	}
}
