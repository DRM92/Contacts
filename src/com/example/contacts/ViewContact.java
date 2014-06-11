package com.example.contacts;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import android.os.Bundle;
import android.app.Activity;
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

public class ViewContact extends Activity {
	
	 /* Declaring database objects */
	 private String tableName = DatabaseHandler.TABLE_CONTACT;
	 private SQLiteDatabase newDB;
	 
	 /* Declaring XML text */
	 private TextView colName;
	 private TextView colNumber;
	 private TextView colAddress;
	 private TextView colEmail;
	 private TextView colAge;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_contact);
		
		/* Pairing vairables with xml text */
		colName = (TextView) findViewById(R.id.cName);
		colNumber = (TextView) findViewById(R.id.cNumber);
		colEmail = (TextView) findViewById(R.id.cEmail);
		colAddress = (TextView) findViewById(R.id.cAddress);
		colAge = (TextView) findViewById(R.id.cAge);
		
		/* Accesses the database and displays single record */
		openDatabase();
		
		/* Methods which wait for button clicked */
		cancelEdit();
		editContact();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.view_contact, menu);
		return true;
	}
	
	/* Changes activity to edit activity and adds the position along with the intent */
	public void editContact(){
		ImageButton edit = (ImageButton) findViewById(R.id.btn_edit);
		edit.setOnClickListener(new Button.OnClickListener() {
		    public void onClick(View v) {
				Intent posIntent = getIntent();
				int position = posIntent.getIntExtra("position", 0);
	        	Intent intent = new Intent(ViewContact.this, EditContact.class);
	        	intent.putExtra("position", position);
	        	startActivity(intent);
	        	finish();
		    }
		});
	}
	
	/* Returns to home (contact) activity */
	public void cancelEdit(){
		ImageButton cancel = (ImageButton) findViewById(R.id.btn_cancel);
		cancel.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
	        	Intent intent = new Intent(ViewContact.this, ContactsActivity.class);	        	
	        	startActivity(intent);
	        	finish();
			}
		});
	}
	
	/* Dynamically calculates age based on DOB using gregorian caldendar */
	public int calcAge(String age){
		
		if(age=="" || age.length()!= 10){
			/* Handling if user enters no date of birth or wrong format */
			return 0;
		} else {
		
		/* Splitting date of birth by /, then setting each to appropiate vairable */
		String splitDOB[] = age.split("/");
		int day = Integer.parseInt(splitDOB[0]);
		int month = Integer.parseInt(splitDOB[1]);
		int year = Integer.parseInt(splitDOB[2]);
		
		/* Using Georgian Calender to retrieve the correct format and todays date */
	    Calendar input = new GregorianCalendar(year, month, day);
	    Calendar today = new GregorianCalendar();
	    
	    /* First calculates the difference in years not taking into account the month */
	    int yearsOld = today.get(Calendar.YEAR) - input.get(Calendar.YEAR);
	    
	    /* Calculates if the month than todays month, if they are the same, compares the day */
	    if ((input.get(Calendar.MONTH) > today.get(Calendar.MONTH))
	    	|| (input.get(Calendar.MONTH) == today.get(Calendar.MONTH) 
	        && input.get(Calendar.DAY_OF_MONTH) > today.get(Calendar.DAY_OF_MONTH))) {
	    	
	    	/* Decrements the age as the month they were born is before January */
	        yearsOld--;
	    }
		return yearsOld;
		}
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
                        
                        /* Setting the database values to xml text */
                		colName.setText(name);
                		colNumber.setText(address);
                		colEmail.setText(number);
                		colAddress.setText(email);
                		colAge.setText(calcAge(age) + " years old" + "\n" + "Birthday is " + age);                		
                     
                    }while (c.moveToNext());
                } 
            }           
        } catch (SQLiteException se ) {
            Log.e(getClass().getSimpleName(), "Could not create or Open the database");
        } finally {

        } 
    }
}
