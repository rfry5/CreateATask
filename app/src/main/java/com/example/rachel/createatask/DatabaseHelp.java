package com.example.rachel.createatask;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.sql.Blob;
import java.util.ArrayList;

import static android.R.attr.id;
import static android.R.attr.key;
import static android.R.attr.name;
import static android.R.attr.password;
import static android.R.id.primary;
import static android.app.DownloadManager.COLUMN_ID;


public class DatabaseHelp extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 4;
    private static final String DATABASE_NAME = "dataManager.db";

    //Table names
    private static final String TABLE_NAME = "login";
    static final String TABLE_ITEM_DATA = "items";

    //General Column Names
    private static final String COLUMN_ID = "id";

    //TABLE_NAME columns
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_PASSWORD = "password";

    //TABLE_ITEM_DATA columns
    static final String COLUMN_ITEMNAME = "itemname";
    static final String COLUMN_SKU = "sku";
    static final String COLUMN_DESCRIPTION = "description";
    static final String COLUMN_LOCATION = "location";
    static final String COLUMN_IMAGE = "image";
    static final String COLUMN_VIDEO = "video";


    SQLiteDatabase db;

    //Create Table for username and password
    private static final String CREATE_TABLE_NAME = "create table login (id integer primary key not null , "
        + "username text not null, password text not null);";

//    private static final String CREATE_TABLE_NAME = "CREATE TABLE " +  TABLE_NAME + "("+ COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
//            COLUMN_USERNAME + " TEXT," + COLUMN_PASSWORD + " TEXT," + ");";

    //Create ITEM_DATA table
//    private static final String CREATE_TABLE_ITEM_DATA = "create table items (id integer primary key not null , "
//            + "itemname text not null, sku text not null, description not null, location not null);";

    private static final String CREATE_TABLE_ITEM_DATA = "CREATE TABLE " +  TABLE_ITEM_DATA + "("+ COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            COLUMN_ITEMNAME + " TEXT," + COLUMN_SKU + " TEXT," + COLUMN_DESCRIPTION + " TEXT," +
            COLUMN_LOCATION + " TEXT," + COLUMN_IMAGE + " TEXT," + COLUMN_VIDEO + " TEXT" + ");";




    public DatabaseHelp(Context context)
    {
        super(context , DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Creating databases
        db.execSQL(CREATE_TABLE_NAME);
        db.execSQL(CREATE_TABLE_ITEM_DATA);
        this.db = db;

    }

    public String getItemDBName(){
        return TABLE_ITEM_DATA;
    }

    public ArrayList<String> getColNames(){
        ArrayList<String> cols = new ArrayList<String>();
        cols.add(COLUMN_ITEMNAME);
        cols.add(COLUMN_SKU);
        cols.add(COLUMN_LOCATION);
        cols.add(COLUMN_DESCRIPTION);

        return cols;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEM_DATA);

        this.onCreate(db);
    }



// Verify username and password
    public String searchPass(String username) {

        db = this.getReadableDatabase();
        String query = "select username, password from " +TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);
        String uname, pass;
        pass = "not found";

        if(cursor.moveToFirst())
        {
            do{
                uname = cursor.getString(0);

                if(uname.equals(username))
                {
                    pass = cursor.getString(1);
                    break;
                }
            }
            while(cursor.moveToNext());
        }

        return pass;
    }

    //Storing values in database
    public void insertContact(User c) {

        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        //How many id present in database
        String query = "select * from login";
        Cursor cursor = db.rawQuery(query, null);
        int count = cursor.getCount();


        values.put(COLUMN_ID, count);
        values.put(COLUMN_USERNAME, c.getUsername());
        values.put(COLUMN_PASSWORD, c.getPassword());

        //Inserts into database
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    //Storing Item Information into Database
    public void insertItem(ItemInfo c) {

        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        String query = "select * from items";
        Cursor cursor = db.rawQuery(query, null);
        int count = cursor.getCount();

//        values.put(COLUMN_ID, count);
        values.put(COLUMN_ITEMNAME, c.getItemname());
        values.put(COLUMN_SKU, c.getSku());
        values.put(COLUMN_LOCATION, c.getLocation());
        values.put(COLUMN_DESCRIPTION, c.getDescription());
        values.put(COLUMN_IMAGE, c.getPicture());
        values.put(COLUMN_VIDEO, c.getVideo());

        //Inserts into database
        db.insert(TABLE_ITEM_DATA, null, values);
        db.close();


    }


    //Uncomment to clear username records (also uncomment in Register.class)
//    public void deleteAll()
//    {
//        SQLiteDatabase db = this.getWritableDatabase();
//        db.delete(TABLE_NAME,null,null);
//        db.delete(TABLE_ITEM_DATA,null,null);
//        db.close();
//    }



    //To read database in table (from GitHub)//
    public ArrayList<Cursor> getData(String Query){
        //get writable database
        SQLiteDatabase sqlDB = this.getWritableDatabase();
        String[] columns = new String[] { "mesage" };
        //an array list of cursor to save two cursors one has results from the query
        //other cursor stores error message if any errors are triggered
        ArrayList<Cursor> alc = new ArrayList<Cursor>(2);
        MatrixCursor Cursor2= new MatrixCursor(columns);
        alc.add(null);
        alc.add(null);


        try{
            String maxQuery = Query ;
            //execute the query results will be save in Cursor c
            Cursor c = sqlDB.rawQuery(maxQuery, null);


            //add value to cursor2
            Cursor2.addRow(new Object[] { "Success" });

            alc.set(1,Cursor2);
            if (null != c && c.getCount() > 0) {


                alc.set(0,c);
                c.moveToFirst();

                return alc ;
            }
            return alc;
        } catch(SQLException sqlEx){
            Log.d("printing exception", sqlEx.getMessage());
            //if any exceptions are triggered save the error message to cursor an return the arraylist
            Cursor2.addRow(new Object[] { ""+sqlEx.getMessage() });
            alc.set(1,Cursor2);
            return alc;
        } catch(Exception ex){

            Log.d("printing exception", ex.getMessage());

            //if any exceptions are triggered save the error message to cursor an return the arraylist
            Cursor2.addRow(new Object[] { ""+ex.getMessage() });
            alc.set(1,Cursor2);
            return alc;
        }


    }


}
