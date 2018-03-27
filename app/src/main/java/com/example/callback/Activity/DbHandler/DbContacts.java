package com.example.callback.Activity.DbHandler;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.callback.Activity.ContactModel.ContactModel;

import java.util.ArrayList;

/**
 * Created by Devroop Kar on 12-08-2017.
 */

public class DbContacts extends SQLiteOpenHelper {
    private static DbContacts sInstance;
    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "contactsManager";

    // Contacts table name
    private static final String TABLE_CONTACTS = "contacts";

    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_PH_PERSONAL = "personal_phone";
    private static final String KEY_PH_OFFICE = "office_phone";
    private static final String KEY_PH_HOME = "home_number";
    private static final String KEY_ADDRESS = "address";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_GENDER = "gender";
    private static final String KEY_PHOTO_PATH = "photo";

//    public DbContacts(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
//        super(context, name, factory, version);
//    }


    public static synchronized DbContacts getsInstance(Context c) {
        if(sInstance == null){
            sInstance = new DbContacts(c.getApplicationContext());
        }
        return sInstance;
    }

    public DbContacts(Context c){
        super(c, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_CONTACTS + " ("
                + KEY_ID + " INTEGER PRIMARY KEY, " + KEY_NAME + " TEXT, "
                + KEY_PH_PERSONAL + " TEXT, " + KEY_PH_OFFICE + " TEXT, " + KEY_PH_HOME + " TEXT, "
                + KEY_ADDRESS + " TEXT, " + KEY_EMAIL + " TEXT, " + KEY_GENDER + " TEXT, " + KEY_PHOTO_PATH + " TEXT "+")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);
        onCreate(db);
    }

    public void addContact(ContactModel model){
        SQLiteDatabase sdb = this.getWritableDatabase();

        ContentValues value = new ContentValues();
        value.put(KEY_NAME, model.getName());
        value.put(KEY_PH_PERSONAL, model.getPersonal_ph());
        value.put(KEY_PH_OFFICE, model.getOffice_ph());
        value.put(KEY_PH_HOME, model.getHome_ph());
        value.put(KEY_ADDRESS, model.getAddr());
        value.put(KEY_EMAIL, model.getEmail());
        value.put(KEY_GENDER, model.getGender());
        value.put(KEY_PHOTO_PATH, model.getPath());
        sdb.insert(TABLE_CONTACTS, null, value);
        sdb.close();
    }

    public Integer delete(Integer id){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_CONTACTS, "id = ?", new String[]{ Integer.toString(id) });
    }

    public Integer update(ContactModel model){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, model.getName());
        values.put(KEY_PH_PERSONAL, model.getPersonal_ph());
        values.put(KEY_PH_OFFICE, model.getOffice_ph());
        values.put(KEY_PH_HOME, model.getHome_ph());
        values.put(KEY_ADDRESS, model.getAddr());
        values.put(KEY_EMAIL, model.getEmail());
        values.put(KEY_GENDER, model.getGender());
        values.put(KEY_PHOTO_PATH, model.getPath());

        return db.update(TABLE_CONTACTS, values, "id = ?", new String[]{ Integer.toString(model.getId()) });
    }

    public ContactModel getContact(int id){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_CONTACTS, new String[] { KEY_ID,
                        KEY_NAME, KEY_PH_PERSONAL, KEY_PH_OFFICE, KEY_PH_HOME,
                KEY_ADDRESS, KEY_EMAIL, KEY_GENDER, KEY_PHOTO_PATH }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        ContactModel contactModel = new ContactModel();
        contactModel.setId(Integer.parseInt(cursor.getString(0)));
        contactModel.setName(cursor.getString(1));
        contactModel.setPersonal_ph(cursor.getString(2));
        contactModel.setOffice_ph(cursor.getString(3));
        contactModel.setHome_ph(cursor.getString(4));
        contactModel.setAddr(cursor.getString(5));
        contactModel.setEmail(cursor.getString(6));
        contactModel.setGender(cursor.getString(7));
        contactModel.setPath(cursor.getString(8));
        // return contactModel
        return contactModel;
    }

    public ArrayList<ContactModel> getAllContacts() {
        ArrayList<ContactModel> arr = new ArrayList<ContactModel>();
        String query = "SELECT  * FROM " + TABLE_CONTACTS + " ORDER BY " + KEY_NAME + " ASC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if(cursor.moveToFirst()){
            do{
                ContactModel model = new ContactModel();
                model.setId(Integer.parseInt(cursor.getString(0)));
                model.setName(cursor.getString(1));
                model.setPersonal_ph(cursor.getString(2));
                model.setOffice_ph(cursor.getString(3));
                model.setHome_ph(cursor.getString(4));
                model.setAddr(cursor.getString(5));
                model.setEmail(cursor.getString(6));
                model.setGender(cursor.getString(7));
                model.setPath(cursor.getString(8));

                arr.add(model);
            }while (cursor.moveToNext());
        }
        return arr;
    }
}