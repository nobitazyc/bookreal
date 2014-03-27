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
	//���캯������
	public DBOperation(Context context, String name) {
		this(context, name,version);
		// TODO Auto-generated constructor stub
	}
	//���캯������
	public DBOperation(Context context, String name,int version) {
		super(context, name,null, version);
		// TODO Auto-generated constructor stub
	}
	//SQLiteOpenHelper�б�������Ĺ��캯��
	public DBOperation(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	/*���ݿ�Ĵ�����ֻ�ڵ�һ�δ���ʱ�ŵ��ô˺���*/
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		Log.e(TAG,"���ݿⴴ��");
		this.db =db;
		db.execSQL("create table user(_id int,name vchar(10));");
		Log.e(TAG,"���ݿⴴ���ɹ�");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
	}
	/*���ݿ����*/
	
	public void insert(String table,ContentValues values){
		if(db==null)
			db=getWritableDatabase();
		db.insert(table, null, values);
		Log.e(TAG,"���ݲ���ɹ�");
	}
	
	/*���ݿ����*/
	public void updata(String table,ContentValues values,String whereClause,String[] whereArgs){
		if(db==null)
			db=getWritableDatabase();
		db.update(table, values, whereClause, whereArgs);
		Log.e(TAG,"���ݿ���³ɹ�");
		close();
	}
	/*
	 * ���ݿ�ɾ��
	 * Parameters
	 *****table  the table to delete from 
	 *****whereClause  the optional WHERE clause to apply when deleting. Passing null will delete all rows. 
	 *****whereArgs  You may include ?s in the where clause, which will be replaced by the values from whereArgs. The values will be bound as Strings. 
	 * */
	public void deleteById(String table,int id){
		if(db==null)
			db=getWritableDatabase();
		Log.e(TAG,"���ݿ�ɾ������");
		db.delete(table, "_id=?", new String[]{String.valueOf(id)});
		Log.e(TAG,"���ݿ�ɾ���ɹ�");
		close();
		
	}
	/*��ѯȫ����¼
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
		Log.d("message","ȫ����ѯ����");
		return db.query(table, null, null, null, null, null, null);
		
	}
	/*ģ����ѯ*/
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


