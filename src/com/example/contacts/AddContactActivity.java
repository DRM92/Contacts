package com.example.contacts;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

public class AddContactActivity extends Activity {

	/* Declaring XML objects */
	private ImageButton buttonAdd;
	private EditText name;
	private EditText number;
	private EditText address;
	private EditText email;
	private EditText age;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_contact);
		
		/* Pairing vairables with XML objects */
		name = (EditText) findViewById(R.id.name);
		number = (EditText) findViewById(R.id.number);
		address = (EditText) findViewById(R.id.address);
		email = (EditText) findViewById(R.id.email);
		age = (EditText) findViewById(R.id.dob);
		buttonAdd = (ImageButton) findViewById(R.id.btn_add);
		
		/* Calls method to be called once button is clicked */
		addContact();
		cancelAdd();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_contact, menu);
		return true;
	}
	
	private void addContact() {
        buttonAdd.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            	/* Database object to access methods */
            	DatabaseHandler db = new DatabaseHandler(AddContactActivity.this);
            	
            	/* Pairing vairables with input from edit boxes */
            	String sName = name.getText().toString();	
            	String sNumber = number.getText().toString();
            	String sAddress = address.getText().toString();
            	String sEmail = email.getText().toString();
            	String sAge = age.getText().toString();
            	
            	/* Adds contact details input into database */
            	db.addContactData(new Contact(0,sName, sNumber, sAddress, sEmail, sAge));
            	
            	/* Open the home (contact) activity to show user it has been added */
            	Intent intent = new Intent(AddContactActivity.this, ContactsActivity.class);
	        	startActivity(intent);
	        	finish();            	
            }
        });
	}
	
	/* Returns to home (contact) activity */
	public void cancelAdd(){
		ImageButton cancel = (ImageButton) findViewById(R.id.btn_cancel);
		cancel.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
	        	Intent intent = new Intent(AddContactActivity.this, ContactsActivity.class);
	        	startActivity(intent);
	        	finish();
			}
		});
	}

}
