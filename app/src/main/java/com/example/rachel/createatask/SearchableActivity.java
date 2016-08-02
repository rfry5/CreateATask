package com.example.rachel.createatask;
//A searchable activity is the activity in the app that performs searches based on a query string and presents the
//search results

//Declared searchable activity in manifest

import android.app.ListActivity;
import android.app.SearchManager;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.content.CursorLoader;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import static android.R.attr.data;
import static android.R.attr.handle;
import static android.R.attr.name;
import static android.R.string.no;
import static android.provider.CalendarContract.CalendarCache.URI;


public class SearchableActivity extends AppCompatActivity implements SearchView.OnQueryTextListener{

    private ListView mListView;
    private SearchView mSearchView;
    private TextView mTextView;
    DatabaseHelp helper;
    EditText txtName, txtSku, txtLocation, txtDescription;
    SQLiteDatabase db;
    // Adapter Object
    SimpleCursorAdapter adapter;
    String selected_ID = "";
    private Bitmap mImageBitmap;
    private ImageView mImageView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_library);

        helper = new DatabaseHelp(this);

        txtName = (EditText) findViewById(R.id.item_name_display);
        txtSku = (EditText) findViewById(R.id.sku_display);
        txtLocation = (EditText) findViewById(R.id.location_display);
        txtDescription  = (EditText) findViewById(R.id.description_display);
        mImageView = (ImageView) findViewById(R.id.item_img_icon);

        mListView = (ListView) findViewById(R.id.main_list_view);

        mSearchView = (SearchView) findViewById(R.id.search_items);
        mSearchView.setIconifiedByDefault(false);

        mTextView = (TextView) findViewById(R.id.text_view_search);

        //Setting click listener for search bar
        mSearchView.setOnQueryTextListener(this);

        System.out.println("VERY FIRTS");

        //This sets the listview for the page with the items, thumbnails and info
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item, sku, location, description, image, video;

//                Cursor row = (Cursor) parent.getItemAtPosition(position);
//                selected_ID = row.getString(0);
//                item = row.getString(1);
//                sku = row.getString(2);
//                location = row.getString(3);
//                description=row.getString(4);
//                image = row.getString(5);
////                video = row.getString(6);
//
//
//                txtName.setText(item);
//                txtSku.setText(sku);
//                txtLocation.setText(location);
//                txtDescription.setText(description);
//                mImageView.setImageBitmap(image);

            }
        });

        fetchData();

    }



    //Buttons for selecting, deleting, and editing item info.
//    @Override
//    public void onClick(View v) {
//
//        if (v == btnEdit){
//
//            //Take to interface to update file and review
//        }
//
//        if (v == btnDelete){
//
//            //Delete row from SQLite Database
//            db = helper.getWritableDatabase();
//            db.delete(DatabaseHelp.TABLE_ITEM_DATA, DatabaseHelp.COLUMN_ITEMNAME + "=?", new String[] {selected_ID});
//            db.close();
//
//            fetchData();
//
//        }
//
//    }


    // Fetch data from database and display into listview
    private void fetchData() {

        db = helper.getReadableDatabase();
        Cursor c = db.query(DatabaseHelp.TABLE_ITEM_DATA, null, null, null, null, null, null);
        adapter = new SimpleCursorAdapter(
                this, R.layout.item_listview, c,
                new String[] { DatabaseHelp.COLUMN_ITEMNAME, DatabaseHelp.COLUMN_SKU,
                        DatabaseHelp.COLUMN_LOCATION ,DatabaseHelp.COLUMN_DESCRIPTION, DatabaseHelp.COLUMN_IMAGE},

                new int[] { R.id.item_name_listview, R.id.sku_listview, R.id.location_listview,R.id.description_listview, R.id.item_img_icon});

        mListView.setAdapter(adapter);
    }

//    @Override
//    protected void onNewIntent(Intent intent) {
//        System.out.println("HERE 1");
//        // Because this activity has set launchMode="singleTop", the system calls this method
//        // to deliver the intent if this activity is currently the foreground activity when
//        // invoked again (when the user executes a search from this activity, we don't create
//        // a new instance of this activity, so the system delivers the search intent here)
//        handleIntent(intent);
//    }
//
//    private void handleIntent(Intent intent) {
//        System.out.println("WHAT UP HEREEEEE");
//        if (Intent.ACTION_VIEW.equals(intent.getAction())) {
//            // handles a click on a search suggestion; launches activity to show word
//            Intent itemIntent = new Intent(this, ItemDisplay.class);
//            itemIntent.setData(intent.getData());
//            startActivity(itemIntent);
//        } else if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
//            // handles a search query
//            String query = intent.getStringExtra(SearchManager.QUERY);
//            showResults(query);
//        }
//    }

    //WORKED WITH BEN
    private void showResults(String query) {
        System.out.println("Line 93" + query);

        //Accessing database
        helper = new DatabaseHelp(this);
        db = helper.getReadableDatabase();
        System.out.println("SHOW results");
        String itemName, sku, location, description;

        //Query database
        String query_item = "select * from "+helper.getItemDBName() + " where itemname like '%" + query + "%'";
        Cursor cur = db.rawQuery(query_item, null);
        //This worked with Ben
        cur.moveToFirst();
        System.out.println(cur.getString(0));
        //This worked with Ben

    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        showResults(query);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        showResults(newText);
        return false;
    }


    //WORKED WITH BEN
}
