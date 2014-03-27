package com.example.helloms;

import org.apache.http.impl.client.DefaultTargetAuthenticationHandler;

import android.os.Bundle;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;

public class DBOperation extends SQLiteOpenHelper{

	String TAG="message";
	private static final int version=1;
	private SQLiteDatabase db;
	//构造函数重载
	public DBOperation(Context context, String name) {
		this(context, name,version);
		// TODO Auto-generated constructor stub
	}
	//构造函数重载
	public DBOperation(Context context, String name,int version) {
		super(context, name,null, version);
		// TODO Auto-generated constructor stub
	}
	//SQLiteOpenHelper中必须包含的构造函数
	public DBOperation(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	/*数据库的创建，只在第一次创建时才调用此函数*/
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		Log.e(TAG,"数据库创建");
		this.db =db;
		db.execSQL("create table user(_id int,name vchar(10));");
		Log.e(TAG,"数据库创建成功");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
	}
	/*数据库插入*/
	
	public void insert(String table,ContentValues values){
		if(db==null)
			db=getWritableDatabase();
		db.insert(table, null, values);
		Log.e(TAG,"数据插入成功");
	}
	
	/*数据库更新*/
	public void updata(String table,ContentValues values,String whereClause,String[] whereArgs){
		if(db==null)
			db=getWritableDatabase();
		db.update(table, values, whereClause, whereArgs);
		Log.e(TAG,"数据库更新成功");
		close();
	}
	/*
	 * 数据库删除
	 * Parameters
	 *****table  the table to delete from 
	 *****whereClause  the optional WHERE clause to apply when deleting. Passing null will delete all rows. 
	 *****whereArgs  You may include ?s in the where clause, which will be replaced by the values from whereArgs. The values will be bound as Strings. 
	 * */
	public void deleteById(String table,int id){
		if(db==null)
			db=getWritableDatabase();
		Log.e(TAG,"数据库删除函数");
		db.delete(table, "_id=?", new String[]{String.valueOf(id)});
		Log.e(TAG,"数据库删除成功");
		close();
		
	}
	/*查询全部记录
	 * Parameters
		table  The table name to compile the query against. 
		columns  A list of which columns to return. Passing null will return all columns, which is discouraged to prevent reading data from storage that isn't going to be used. 
		selection  A filter declaring which rows to return, formatted as an SQL WHERE clause (excluding the WHERE itself). Passing null will return all rows for the given table. 
		selectionArgs  You may include ?s in selection, which will be replaced by the values from selectionArgs, in order that they appear in the selection. The values will be bound as Strings. 
		groupBy  A filter declaring how to group rows, formatted as an SQL GROUP BY clause (excluding the GROUP BY itself). Passing null will cause the rows to not be grouped. 
		having  A filter declare which row groups to include in the cursor, if row grouping is being used, formatted as an SQL HAVING clause (excluding the HAVING itself). Passing null will cause all row groups to be included, and is required when row grouping is not being used. 
		orderBy  How to order the rows, formatted as an SQL ORDER BY clause (excluding the ORDER BY itself). Passing null will use the default sort order, which may be unordered. 
	*/
	
	public Cursor query(String table){
		if(db==null)
			db=getReadableDatabase();
		Log.d("message","全部查询函数");
		return db.query(table, null, null, null, null, null, null);
		
	}
	/*模糊查询*/
	public Cursor query(String table,String nameLike){
		if(db==null)
			db=getReadableDatabase();
		return db.query(table, null, "name like ?", new String[]{"%"+nameLike+"%"}, null, null, null);
	}
	public void close() {
		// TODO Auto-generated method stub
		if (db != null)
			db.close();
	}
	
	
	
	
	
}


