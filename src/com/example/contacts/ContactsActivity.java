package com.example.contacts;

import android.os.Bundle;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import android.app.ListActivity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ContactsActivity extends ListActivity {
	
	/* Declaring database objects */
	private ArrayList<String> contact = new ArrayList<String>();
	private String tableName = DatabaseHandler.TABLE_CONTACT;
	private String key = DatabaseHandler.KEY_P;
	private SQLiteDatabase newDB;
	ArrayAdapter<String> adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contacts);
		
		/* Calls the methods which display the database in a list view */
        openDatabase();        
        displayResultList();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.contacts, menu);
		return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items
	    switch (item.getItemId()) {
	    
	    /* Opens the add contact activity from the action bar */
	        case R.id.action_add:
	        	Intent intent = new Intent(ContactsActivity.this, AddContactActivity.class);
	        	startActivity(intent);
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	@Override
	public void onBackPressed() {
		/*Stops list view updating on back button pressed after deletion */
	}
	
	@Override  
	protected void onListItemClick(ListView l, View v,  final int position, long id) {  
    	super.onListItemClick(l, v, position, id);
    	
	    /* Alert Dialog box giving user 3 options (View, Edit, Delete) */
	    new AlertDialog.Builder(this)
	    .setTitle("Contacts")
	    .setMessage("What would you like to do with this contact?")
	    
	    /* Opens the view activity and adds the position in the list with the intent */
	    .setPositiveButton("View", new DialogInterface.OnClickListener() {	    	
	        public void onClick(DialogInterface dialog, int which) { 
	        	Intent intent = new Intent(ContactsActivity.this, ViewContact.class);
	        	intent.putExtra("position", position);
	        	startActivity(intent);
	        }
	     })
	     
	     /* Opens the edit activity and adds the position in the list with the intent */
	     .setNeutralButton("Edit", new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int which) { 
	        	Intent intent = new Intent(ContactsActivity.this, EditContact.class);
	        	intent.putExtra("position", position);
	        	startActivity(intent);
	        }
	     })
	     
	     /* Deletes the contact from the database and list view, then updates the list view */
	    .setNegativeButton("Delete", new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int which) { 
	        	deleteContact(position);
	        	Intent intent = new Intent(ContactsActivity.this, ContactsActivity.class);
	        	startActivity(intent);
	        }
	     })
	     .show();
	}
	
	/* Method to count total number of contacts in the database */
	public int getIdCount(){
		DatabaseHandler dbHelper = new DatabaseHandler(this.getApplicationContext());
		String countId = "SELECT  * FROM " + tableName;
	    SQLiteDatabase db = dbHelper.getReadableDatabase();
	    Cursor cursor = db.rawQuery(countId, null);
	    int count = cursor.getCount();
	    cursor.close();
		return count;
	}
	
	/* Method to delete the selected contact from the database */
	public boolean deleteContact(int position) {
		
		/* Deletes the last contact in the list based on the ID */
		if(position==getIdCount()){			
        	Toast.makeText(getApplicationContext(), "Contact has been deleted.",
     			   Toast.LENGTH_LONG).show();
			return newDB.delete(tableName, key + "=" + position, null) > 0;
		
		} else {
			
			return false;
		}
	}
	
	public void displayResultList() {	
		
		/* Title of the listview */
        TextView titleView = new TextView(this);
        getListView().addHeaderView(titleView);
        
        /* Setting the adapter - Using custom adapter to change appearance of text */
        adapter = new ArrayAdapter<String>(ContactsActivity.this, R.layout.custom_text, contact);
        getListView().setAdapter(adapter);         
	}

	public void openDatabase() {		
		try {
			/* Declaring database objects */
            DatabaseHandler dbHelper = new DatabaseHandler(this.getApplicationContext());
            newDB = dbHelper.getWritableDatabase();
            
            /* Querying the database for all the names from the table */
            Cursor c = newDB.rawQuery("SELECT key, name FROM " +
                    tableName, null);
 
            if (c != null ) {
                if  (c.moveToFirst()) {
                    do {
                    	/* Setting the name column to a vairable */
                        String name = c.getString(c.getColumnIndex("name"));
                        
                        /* Adds a contact to the structure */
                        contact.add(name);
                    }while (c.moveToNext());
                } 
            }           
        } catch (SQLiteException se ) {
            Log.e(getClass().getSimpleName(), "Could not create or Open the database");
        } finally {

        } 
    }
}
	
