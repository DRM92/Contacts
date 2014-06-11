package com.example.contacts;

import android.os.Bundle;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class EditContact extends Activity {

	 private String tableName = DatabaseHandler.TABLE_CONTACT;
	 private SQLiteDatabase newDB;
	 private EditText colName;
	 private EditText colNumber;
	 private EditText colAddress;
	 private EditText colEmail;
	 private EditText colAge;
	 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_contact);
		colName = (EditText) findViewById(R.id.name);
		colNumber = (EditText) findViewById(R.id.number);
		colEmail = (EditText) findViewById(R.id.email);
		colAddress = (EditText) findViewById(R.id.address);
		colAge = (EditText) findViewById(R.id.age);		
		
		viewEdit();
		editContact();
		openDatabase();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.edit_contact, menu);
		return true;
	}
	
	public void editContact(){
		ImageButton edit = (ImageButton) findViewById(R.id.btn_save);
		edit.setOnClickListener(new Button.OnClickListener() {
		    public void onClick(View v) {
		    	
		    	Intent posIntent = getIntent();
				int position = posIntent.getIntExtra("position", 0);
		    	
				/* Defines an object to contain the new values to insert */
		    	ContentValues editContact = new ContentValues();
		    	String sName = colName.getText().toString();	
            	String sNumber = colEmail.getText().toString();
            	String sAddress = colNumber.getText().toString();
            	String sEmail = colAddress.getText().toString();
            	String sAge = colAge.getText().toString();
            	
            	/* Sets the values of each column and inserts the value */
            	editContact.put("name", sName);
            	editContact.put("number", sNumber);
            	editContact.put("address", sAddress);
            	editContact.put("email", sEmail);
            	editContact.put("age", sAge);
            	
            	/* updates all the columns above, where the primary key equals the position */
            	newDB.update(tableName, editContact, "key=" + position, null);
            	
            	/* Confirms to the user that the contact has been updated */
            	Toast.makeText(getApplicationContext(), "Contact has been updated.",
            			   Toast.LENGTH_LONG).show();
            	
            	/* Changes activity to the view contact, so they can see there changes */
	        	Intent intent = new Intent(EditContact.this, ViewContact.class);
	        	intent.putExtra("position", position); //so it displays correct contact
	        	startActivity(intent);
	        	finish();
		    }
		});
	}
	
	/* Returns to the view page if user doesnt want to edit contact */
	public void viewEdit(){
		ImageButton cancel = (ImageButton) findViewById(R.id.btn_cancel);
		cancel.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				Intent posIntent = getIntent();
				int position = posIntent.getIntExtra("position", 0);
	        	Intent intent = new Intent(EditContact.this, ViewContact.class);
	        	intent.putExtra("position", position); //so it displays correct contact
	        	startActivity(intent);
	        	finish();
			}
		});
	}
	
	/* Gets specific record from database and displays all columns */
	public void openDatabase() {		
		try {
			/* Retrieving posistion selected from contact activity */
			Intent posIntent = getIntent();
			int position = posIntent.getIntExtra("position", 0);
			
			/*Declaring database objects */
            DatabaseHandler dbHelper = new DatabaseHandler(this.getApplicationContext());
            newDB = dbHelper.getWritableDatabase();
            
            /* Query which finds the record based on their primary key matching the posistion in the list */
            Cursor c = newDB.rawQuery("SELECT * FROM " + tableName + " WHERE key = " + position, null);

            if (c != null ) {
            	/* Traverses to find record */
                if  (c.moveToFirst()) {
                    do {
                    	
                    	/* Setting vairables to the values from database */
                        String name = c.getString(c.getColumnIndex("name"));                        
                        String number = c.getString(c.getColumnIndex("number"));  
                        String address = c.getString(c.getColumnIndex("address"));
                        String age = c.getString(c.getColumnIndex("age"));                         
                        String email = c.getString(c.getColumnIndex("email"));                        

                        /* Setting the database values to xml edit boxes */
                		colName.setText(name);
                		colNumber.setText(address);
                		colEmail.setText(number);
                		colAddress.setText(email);
                		colAge.setText(age);                		
                     
                    }while (c.moveToNext());
                } 
            }           
        } catch (SQLiteException se ) {
            Log.e(getClass().getSimpleName(), "Could not create or Open the database");
        } finally {

        } 
    }
}
