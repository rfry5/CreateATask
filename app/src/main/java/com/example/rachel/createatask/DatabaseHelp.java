package com.example.rachel.createatask;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.MatrixCursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;
import android.widget.Toast;

import java.sql.Blob;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.R.attr.id;
import static android.R.attr.key;
import static android.R.attr.name;
import static android.R.attr.password;
import static android.R.attr.value;
import static android.R.id.primary;
import static android.app.DownloadManager.COLUMN_ID;
import static android.os.Build.ID;
import static com.example.rachel.createatask.R.id.location;
import static com.example.rachel.createatask.R.id.sku;
import static com.example.rachel.createatask.R.string.username;


public class DatabaseHelp extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 14;
    private static final String DATABASE_NAME = "dataManager.db";

    //Table names
    private static final String TABLE_NAME = "login";
    static final String TABLE_ITEM_DATA = "items";

    //General Column Names
    static final String COLUMN_ID = "_id";

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
    static final String COLUMN_IMAGE_BLOB ="blob";

    static final String[] COLUMNS = {COLUMN_ID,COLUMN_ITEMNAME,COLUMN_SKU,COLUMN_LOCATION,COLUMN_DESCRIPTION,COLUMN_IMAGE,COLUMN_VIDEO,COLUMN_IMAGE_BLOB};
    static final String[] COLUMN_DESC = {COLUMN_ID,COLUMN_ITEMNAME,COLUMN_SKU,COLUMN_LOCATION,COLUMN_DESCRIPTION};


    SQLiteDatabase db;

    //Create Table for username and password
    private static final String CREATE_TABLE_NAME = "create table login (_id integer primary key not null , "
        + "username text not null, password text not null);";

//    private static final String CREATE_TABLE_NAME = "CREATE TABLE " +  TABLE_NAME + "("+ COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
//            COLUMN_USERNAME + " TEXT," + COLUMN_PASSWORD + " TEXT," + ");";

    //Create ITEM_DATA table
//    private static final String CREATE_TABLE_ITEM_DATA = "create table items (id integer primary key not null , "
//            + "itemname text not null, sku text not null, description not null, location not null);";

//    private static final String CREATE_TABLE_ITEM_DATA = "CREATE TABLE " +  TABLE_ITEM_DATA + "("+ COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
//            COLUMN_ITEMNAME + " TEXT," + COLUMN_SKU + " TEXT," + COLUMN_DESCRIPTION + " TEXT," +
//            COLUMN_LOCATION + " TEXT," + COLUMN_IMAGE + " TEXT," + COLUMN_VIDEO + " TEXT" + ");";

    private static final String CREATE_TABLE_ITEM_DATA = "CREATE TABLE " +  TABLE_ITEM_DATA + "("+ COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            COLUMN_ITEMNAME + " TEXT," + COLUMN_SKU + " TEXT," + COLUMN_LOCATION + " TEXT," +
            COLUMN_DESCRIPTION + " TEXT," + COLUMN_IMAGE + " TEXT," + COLUMN_VIDEO + " TEXT," + COLUMN_IMAGE_BLOB + " BLOB" + ");";




    public DatabaseHelp(Context context)
    {
        super(context , DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        //Creating databases
        db.execSQL(CREATE_TABLE_NAME);
        db.execSQL(CREATE_TABLE_ITEM_DATA);
        db.execSQL("CREATE VIRTUAL TABLE fts_table USING fts4 (content='items', itemname, sku, location)");
        this.db = db;

    }

    public String getItemDBName(){
        return TABLE_ITEM_DATA;
    }

    public String getColumnId(){
        return COLUMN_ID;
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
        db.execSQL("DROP TABLE IF EXISTS fts_table");

        this.onCreate(db);
    }


    ////////Verify username and password/////////
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

    /////////Storing username and passwords in database////////
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

    ///////Storing Item Information into Database//////
    public void insertItem(ItemInfo c) {

        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

//        String query = "select * from items";
//        Cursor cursor = db.rawQuery(query, null);
//        int count = cursor.getCount();
//        values.put(COLUMN_ID, count);
        values.put(COLUMN_ITEMNAME, c.getItemname());
        values.put(COLUMN_SKU, c.getSku());
        values.put(COLUMN_LOCATION, c.getLocation());
        values.put(COLUMN_DESCRIPTION, c.getDescription());
        values.put(COLUMN_IMAGE, c.getPicture());
        values.put(COLUMN_VIDEO, c.getVideo());
        values.put(COLUMN_IMAGE_BLOB, c.getThumbnail());

        //Inserts into database
        db.insert(TABLE_ITEM_DATA, null, values);
        db.execSQL("INSERT INTO fts_table (docid, itemname, sku, location) SELECT _id, itemname, sku, location FROM items");
        db.execSQL("VACUUM");
        db.close();
    }

    //////////Searching database///////////////
    public Cursor searchItems(String query) {

        System.out.println("In search items in DatabaseHelp " );
        String sql = "SELECT * FROM items WHERE _id IN " +
                "(SELECT docid FROM fts_table WHERE fts_table MATCH ?)";
        //Must have query+* to check for partial words!!
        //This only queries the first column...
        String[] selectionArgs = { query + "*" };
        System.out.println(Arrays.toString(selectionArgs));
        Cursor cursor = db.rawQuery(sql, selectionArgs);
        //rawQuery always returns a cursor! So I have to check if it's empty, not null
        if (cursor.getCount() == 0) {
            System.out.println("cursor == null, There are no search results ");

        } else if (!cursor.moveToFirst()) {
            System.out.println("Cursor move to first");
            cursor.close();
        }
        Log.v("Cursor Object", DatabaseUtils.dumpCursorToString(cursor));
        return cursor;
    }

    /////////Update database//////////
    public int updateItems(ItemInfo c) {
        String colID;
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        //Retrieving all of the column ids
        String query = "select _id from " +TABLE_ITEM_DATA;
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        //This should be the ID of the position the item is in
        cursor.getInt(0);
        System.out.println("getID " + cursor.getInt(0));

        //Getting values/item inputs
        values.put(COLUMN_ITEMNAME, c.getItemname());
        System.out.println("GETTER " + c.getItemname());
        values.put(COLUMN_SKU, c.getSku());
        values.put(COLUMN_LOCATION, c.getLocation());
        values.put(COLUMN_DESCRIPTION, c.getDescription());
        values.put(COLUMN_IMAGE, c.getPicture());
        values.put(COLUMN_VIDEO, c.getVideo());
        System.out.println("UPDATING");

        Log.v("Cursor Object", DatabaseUtils.dumpCursorToString(cursor));

        if(cursor.moveToFirst())
        {
            do{
                colID = cursor.getString(0);
                System.out.println("This is the column ID at cursor 0 " + colID);
                System.out.println("Print getInt0  " + cursor.getInt(0));

                if(colID.equals(String.valueOf(cursor.getInt(0))))
                {
                    db.update(TABLE_ITEM_DATA, values, "_id = ?", new String[] {String.valueOf(c.getID())});
                    System.out.println("In here");
                    break;
                }
            }
            while(cursor.moveToNext());
        }
//        return db.update(TABLE_ITEM_DATA, values, selection, selectionArgs);
        // updating row
//        return db.update(TABLE_ITEM_DATA, values, "_id =?",
//                new String[] { "_id" });
//        db.close();
        return db.update(TABLE_ITEM_DATA, values, "_id = ?", new String[] {String.valueOf(c.getID())});
    }


    //////////////////////////////////////
    //// Deleting row from database///////
    public void deleteItem(int id) {
        db = this.getWritableDatabase();
        String query = "select _id from " +TABLE_ITEM_DATA;
        Cursor cursor = db.rawQuery(query, null);
                Log.v("Cursor Object", DatabaseUtils.dumpCursorToString(cursor));
        cursor.moveToFirst();
        cursor.getInt(0);
//        Log.v("Cursor Object", DatabaseUtils.dumpCursorToString(cursor));
        System.out.println("in delete fxn at getInt0 " + cursor.getInt(0));
        System.out.println("This is the ID were sending " + id);

        System.out.println("This is the printout of getInt0 " + cursor.getInt(0));
        db.delete(TABLE_ITEM_DATA, "_id = ?", new String[] { String.valueOf(id) });
        db.execSQL("VACUUM");
        db.close();
    }

    public Integer getNumberOfItems(){
        db = this.getWritableDatabase();
        String query = "select _id from " +TABLE_ITEM_DATA;
        Cursor cursor = db.rawQuery(query, null);
        Integer numItems = cursor.getCount();
        System.out.println(numItems);

        return numItems;
    }


    //Uncomment to clear username records (also uncomment in Register.class)
//    public void deleteAll()
//    {
//        SQLiteDatabase db = this.getWritableDatabase();
//        db.delete(TABLE_NAME,null,null);
//        db.delete(TABLE_ITEM_DATA,null,null);
//        db.close();
//    }

//    //Returns the id of the TABLE of the row that matches name,sku and location
//    public int find(ArrayList<String> opts){
//        int id = -1;
//        int count = 0;
//        //opts holds the name, sku, location
//        db = this.getReadableDatabase();
//        String query = "select * from items";
//        Cursor cur = db.rawQuery(query, null);
//        cur.moveToFirst();
//
//        do{
//            String name = cur.getString(1);
//            String sku = cur.getString(2);
//            String location = cur.getString(3);
//            System.out.println("PRINTING LINE VALUES: " + name);
//
//            if(name.equals(opts.get(0)) && sku.equals(opts.get(1)) && location.equals(opts.get(2))){
//                System.out.println("found match for " + name);
//                id = cur.getInt(0);
//            }
//            count++;
//
//        }while (cur.moveToNext());
//
//        if (id == -1){
//            System.out.println("THERE WAS NO MATCH: Databasehelp.java Line 294");
//        }
//            return count;
//    }

    //////////////////////////////////////////
    //Returns all item info from the database
    public ArrayList<ItemInfo> getAll(){

        System.out.println("START OF GETALL IN DATABASE HELP");

        ArrayList<ItemInfo> info = new ArrayList<ItemInfo>();
        db = this.getReadableDatabase();
        String query = "select * from items";
        Cursor cur = db.rawQuery(query, null);

        cur.moveToFirst();
//        Log.v("Cursor Object", DatabaseUtils.dumpCursorToString(cur));

        do {
            ItemInfo c = new ItemInfo();
            c.setItemname(cur.getString(1));
            System.out.println(cur.getString(1));
            c.setSku(cur.getString(2));
            c.setLocation(cur.getString(3));
            c.setDescription(cur.getString(4));
            System.out.println(cur.getString(4));
            //Setting URI path to a string
            c.setPicture(cur.getString(5));
            c.setVideo(cur.getString(6));
            c.setThumbnail(cur.getBlob(7));
            //Added with Ben
            c.setID(cur.getInt(0));
//            System.out.println("Inside getAll from database help for ArrayList");
//            System.out.println("Inside getAll from database help for ArrayList " + c.getID());
            info.add(c);
        } while (cur.moveToNext());
        //Just added
        cur.close();
        System.out.println("END OF DATABASEHELP");
        return info;
    }

    ///////////////////////////////////////////
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
