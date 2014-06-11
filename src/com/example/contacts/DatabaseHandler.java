package com.example.contacts;
import java.util.ArrayList;
import java.util.List;
 
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
 
public class DatabaseHandler extends SQLiteOpenHelper {
 
	/* Declaring database objects and columns */
    private static final int DATABASE_VERSION = 46;
    private static final String DATABASE_NAME = "contactManager";
    public static final String TABLE_CONTACT = "contact";
    static final String KEY_P = "key";
    private static final String KEY_NAME = "name";
    private static final String KEY_ADDRESS = "address";
    private static final String KEY_NUMBER = "number";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_AGE = "age";
 
    /* Constructor */
    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
 
    /* Creates tables with specified columns and types */
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACT_TABLE = "CREATE TABLE " + TABLE_CONTACT + "("
                + KEY_P + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                + KEY_NUMBER + " TEXT," + KEY_EMAIL + " TEXT,"
                + KEY_ADDRESS + " TEXT," + KEY_AGE + " TEXT )";
        db.execSQL(CREATE_CONTACT_TABLE);
    }
 
    /* Method to upgrade database if changes i.e. columns/tables */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACT);
        onCreate(db);
    }
 
    /* Adds a single record into the database */
    void addContactData(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        
        /* Defines an object to contain the new values to insert */
        ContentValues values = new ContentValues();
        
        /*All columns apart from primary key are input into the database */
        values.put(KEY_NAME, contact.getName()); 
        values.put(KEY_NUMBER, contact.getNumber()); 
        values.put(KEY_ADDRESS, contact.getAddress()); 
        values.put(KEY_EMAIL, contact.getEmail()); 
        values.put(KEY_AGE, contact.getAge()); 
 
        /* Inserts the record into the database */
        db.insert(TABLE_CONTACT, null, values);
        db.close(); 
    }
 
    /* Retrieves an individual contact from the database */
    Contact getContact(int id) {
    	
    	/* calls onCreate() */
        SQLiteDatabase db = this.getReadableDatabase();
 
        /* Queries the database to retrieve all columns */
        Cursor cursor = db.query(TABLE_CONTACT, new String[] { KEY_P + 
                KEY_NAME, KEY_NUMBER, KEY_ADDRESS, KEY_EMAIL, KEY_AGE }, KEY_P + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
 
        /* Using contact structure to use the set methods to get each value */
        Contact contact = new Contact();
        contact.setPkey(Integer.parseInt(cursor.getString(0)));
        contact.setName(String.valueOf(cursor.getString(1)));
        contact.setNumber(String.valueOf(cursor.getString(2)));
        contact.setEmail(String.valueOf(cursor.getString(3)));
        contact.setAddress(String.valueOf(cursor.getString(4)));
        contact.setAge(String.valueOf(cursor.getString(5)));
        return contact;
    }
     
    /* Retrieves all the contacts from the database */
    public List<Contact> getAllContacts() {
        List<Contact> contactList = new ArrayList<Contact>();
 
        /* Calls onCreate() */
        SQLiteDatabase db = this.getWritableDatabase();
        
        /* Query which retrieves all contacts in database */
        Cursor cursor = db.rawQuery("SELECT  * FROM " + TABLE_CONTACT, null);
 
        /* Loops through each row, which adds to the list */
        if (cursor.moveToFirst()) {
            do {
            	
            	/* Uses the contact structure and calls the set methods */
                Contact contact = new Contact();
                contact.setPkey(Integer.parseInt(cursor.getString(0)));
                contact.setName(String.valueOf(cursor.getString(1)));
                contact.setNumber(String.valueOf(cursor.getString(2)));
                contact.setEmail(String.valueOf(cursor.getString(3)));
                contact.setAddress(String.valueOf(cursor.getString(4)));
                contact.setAge(String.valueOf(cursor.getString(5)));
                contactList.add(contact);
            } while (cursor.moveToNext());
        }
        return contactList;
    } 
}
