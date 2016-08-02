package com.example.rachel.createatask;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class ItemDisplay extends AppCompatActivity{

    SQLiteDatabase db;
    DatabaseHelp mDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_item_info);

        mDB = new DatabaseHelp(this);

        Uri uri = getIntent().getData();
        Cursor cursor = managedQuery(uri, null, null, null, null);

        if (cursor == null) {
            finish();
        } else {
            cursor.moveToFirst();

            TextView name = (TextView) findViewById(R.id.item_name_display);
            TextView sku = (TextView) findViewById(R.id.sku_display);
            TextView location = (TextView) findViewById(R.id.location_display);
            TextView description = (TextView) findViewById(R.id.description_display);

            ArrayList<String> colnames = mDB.getColNames();

            int nIndex = cursor.getColumnIndexOrThrow(colnames.get(0));
            int sIndex = cursor.getColumnIndexOrThrow(colnames.get(1));
            int lIndex = cursor.getColumnIndexOrThrow(colnames.get(2));
            int dIndex = cursor.getColumnIndexOrThrow(colnames.get(3));


            name.setText(cursor.getString(nIndex));
            sku.setText(cursor.getString(sIndex));
            location.setText(cursor.getString(lIndex));
            description.setText(cursor.getString(dIndex));


//        // Construct the data source
//        images = new ArrayList();
//        // Create the adapter to convert the array to views
//        itemListView = new ItemListView(this, images);
//        // Attach the adapter to a ListView
//        listView = (ListView) findViewById(R.id.main_list_view);
//        listView.setAdapter(itemListView);
        }
    }

}
