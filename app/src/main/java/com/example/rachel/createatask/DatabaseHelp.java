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

    private static final int DATABASE_VERSION = 23;
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
    static final String COLUMN_COM_1 = "com1";
    static final String COLUMN_COM_2 = "com2";
    static final String COLUMN_COM_3 = "com3";
    static final String COLUMN_COM_4 = "com4";
    static final String COLUMN_COM_5 = "com5";
    static final String COLUMN_COM_6 = "com6";
    static final String COLUMN_SIZE_SPINNER = "size";
    static final String COLUMN_TYPE_SPINNER = "type";
    static final String COLUMN_FLAG = "urgency";

    static final String[] COLUMNS = {COLUMN_ID,COLUMN_ITEMNAME,COLUMN_SKU,COLUMN_LOCATION,COLUMN_DESCRIPTION,COLUMN_IMAGE,COLUMN_VIDEO,COLUMN_IMAGE_BLOB,COLUMN_COM_1};
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

//    private static final String CREATE_TABLE_ITEM_DATA = "CREATE TABLE " +  TABLE_ITEM_DATA + "("+ COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
//            COLUMN_ITEMNAME + " TEXT," + COLUMN_SKU + " TEXT," + COLUMN_LOCATION + " TEXT," +
//            COLUMN_DESCRIPTION + " TEXT," + COLUMN_IMAGE + " TEXT," + COLUMN_VIDEO + " TEXT," + COLUMN_IMAGE_BLOB + " BLOB" + ");";

//    private static final String CREATE_TABLE_ITEM_DATA = "CREATE TABLE " +  TABLE_ITEM_DATA + "("+ COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
//            COLUMN_ITEMNAME + " TEXT," + COLUMN_SKU + " TEXT," + COLUMN_LOCATION + " TEXT," +
//            COLUMN_DESCRIPTION + " TEXT," + COLUMN_IMAGE + " TEXT," + COLUMN_VIDEO + " TEXT," + COLUMN_IMAGE_BLOB + " BLOB," + COLUMN_COM_1 + " TEXT" + ");";

    private static final String CREATE_TABLE_ITEM_DATA = "CREATE TABLE " +  TABLE_ITEM_DATA + "("+ COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            COLUMN_ITEMNAME + " TEXT," + COLUMN_SKU + " TEXT," + COLUMN_LOCATION + " TEXT," +
            COLUMN_DESCRIPTION + " TEXT," + COLUMN_IMAGE + " TEXT," + COLUMN_VIDEO + " TEXT," + COLUMN_IMAGE_BLOB + " BLOB," + COLUMN_COM_1 + " TEXT," +
            COLUMN_COM_2 + " TEXT," + COLUMN_COM_3 + " TEXT," + COLUMN_COM_4 + " TEXT," + COLUMN_COM_5 + " TEXT," + COLUMN_COM_6 + " TEXT," +
            COLUMN_SIZE_SPINNER + " INTEGER," + COLUMN_TYPE_SPINNER + " INTEGER," + COLUMN_FLAG + " INTEGER" + ");";




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

    ///////Saving Item Information into Database//////
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

        //9/21 Trying to save command line 1
        values.put(COLUMN_COM_1, c.getCom1());
        values.put(COLUMN_COM_2, c.getCom2());
        values.put(COLUMN_COM_3, c.getCom3());
        values.put(COLUMN_COM_4, c.getCom4());
        values.put(COLUMN_COM_5, c.getCom5());
        values.put(COLUMN_COM_6, c.getCom6());

        //Spinner values
        values.put(COLUMN_TYPE_SPINNER, c.getSpinType());
        values.put(COLUMN_SIZE_SPINNER, c.getSpinSize());

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
        values.put(COLUMN_COM_1, c.getCom1());
        values.put(COLUMN_COM_2, c.getCom2());
        values.put(COLUMN_COM_3, c.getCom3());
        values.put(COLUMN_COM_4, c.getCom4());
        values.put(COLUMN_COM_5, c.getCom5());
        values.put(COLUMN_COM_6, c.getCom6());
        values.put(COLUMN_TYPE_SPINNER, c.getSpinType());
        values.put(COLUMN_SIZE_SPINNER, c.getSpinSize());

        /////ADDED TODAY
        values.put(COLUMN_IMAGE_BLOB, c.getThumbnail()); //just added
        //////
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
        return db.update(TABLE_ITEM_DATA, values, "_id = ?", new String[] {String.valueOf(c.getID())});
    }


    //////////////////////////////////////
    //// Deleting row from database///////
    public void deleteItem(int id) {
        db = this.getWritableDatabase();
        String query = "select _id from " +TABLE_ITEM_DATA;
        Cursor cursor = db.rawQuery(query, null);
//        Log.v("Cursor Object", DatabaseUtils.dumpCursorToString(cursor));
        cursor.moveToFirst();
        cursor.getInt(0);

//        Log.v("Cursor Object", DatabaseUtils.dumpCursorToString(cursor));
//        System.out.println("in delete fxn at getInt0 " + cursor.getInt(0));
//        System.out.println("This is the ID were sending " + id);
//        System.out.println("This is the printout of getInt0 " + cursor.getInt(0));

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

    //////////////////////////////////////////
    //Returns all item info from the database
    public ArrayList<ItemInfo> getAll(){

        boolean debug = false;
        if (debug == true)
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
            c.setCom1(cur.getString(8));
            c.setCom2(cur.getString(9));
            c.setCom3(cur.getString(10));
            c.setCom4(cur.getString(11));
            c.setCom5(cur.getString(12));
            c.setCom6(cur.getString(13));
            c.setSpinSize(cur.getString(14));
            c.setSpinType(cur.getString(15));

            info.add(c);
        } while (cur.moveToNext());
        //Just added
        cur.close();
        System.out.println("END OF DATABASEHELP");
        return info;
    }

    /////////FLAG database//////////
    public int flagItem(ItemInfo c) {
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
        values.put(COLUMN_FLAG, c.getItemFlag());
        System.out.println("GETTER " + c.getItemFlag());
        System.out.println("FLAG ITEM");

        Log.v("Cursor Object", DatabaseUtils.dumpCursorToString(cursor));

        if(cursor.moveToFirst())
        {
            do{
                colID = cursor.getString(0);
                System.out.println("column id " + cursor.getString(0));

                if(colID.equals(String.valueOf(cursor.getInt(0))))
                {
//                    db.replace(TABLE_ITEM_DATA, "urgency", values);
                    db.update(TABLE_ITEM_DATA, values, "_id = ?", new String[] {String.valueOf(c.getID())});
                    System.out.println(values);

                    break;
                }
            }
            while(cursor.moveToNext());
        }
        return db.update(TABLE_ITEM_DATA, values, "_id = ?", new String[] {String.valueOf(c.getID())});
    }
    ///////FLAG////////


    //////Returns urgent items for dashboard///////
    public ArrayList<ItemInfo> getUrgent(){

        boolean debug = false;
        if (debug == true)
            System.out.println("START OF GETURGENT IN DATABASE HELP");

        Integer urg;

        ArrayList<ItemInfo> info = new ArrayList<ItemInfo>();
        db = this.getReadableDatabase();
        String query = "select * from " +TABLE_ITEM_DATA;
        //Selects all values from urgent column
        //If column value equals 1, set info, else nothing
//        String query = "select * from items";
        Cursor cur = db.rawQuery(query, null);

        cur.moveToFirst();
        Log.v("Cursor Object", DatabaseUtils.dumpCursorToString(cur));

        cur.getInt(16);

        if(cur.moveToFirst())
            do{
                urg = cur.getInt(16);
                System.out.println("urgency value " + cur.getInt(16));

                if(urg.equals(1))
                {
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
                    c.setCom1(cur.getString(8));
                    c.setCom2(cur.getString(9));
                    c.setCom3(cur.getString(10));
                    c.setCom4(cur.getString(11));
                    c.setCom5(cur.getString(12));
                    c.setCom6(cur.getString(13));
                    c.setSpinSize(cur.getString(14));
                    c.setSpinType(cur.getString(15));

                    info.add(c);
                }
            } while(cur.moveToNext());
            cur.close();
            System.out.println("END OF DATABASEHELP get URGENT");
            return info;
    }

    /////Resolve and remove urgency from dashboard/////
    //similar to update, updating urgency column to null
    // int id is messed up... position is wrong!
    public int resolveItems(int id) {
        String colID;
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        System.out.println("INSIDE RESOLVE ITEMS");
        System.out.println(id);

        //Retrieving all of the column ids
        String query = "select _id from " +TABLE_ITEM_DATA;
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        //This should be the ID of the position the item is in
        cursor.getInt(0);
        System.out.println("getID " + cursor.getInt(0));

        //Getting values/item inputs
        values.put(COLUMN_FLAG, 0);

        Log.v("Cursor Object", DatabaseUtils.dumpCursorToString(cursor));

        if(cursor.moveToFirst())
        {
            do{
                colID = cursor.getString(0);
                System.out.println("This is the column ID at cursor 0 " + colID);
                System.out.println("Print getInt0  " + cursor.getInt(0));

                if(colID.equals(String.valueOf(cursor.getInt(0))))
                {
                    db.update(TABLE_ITEM_DATA, values, "_id = ?", new String[] {String.valueOf(id)});
                    System.out.println("In here");
                    break;
                }
            }
            while(cursor.moveToNext());
        }
        return db.update(TABLE_ITEM_DATA, values, "_id = ?", new String[] {String.valueOf(id)});
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
