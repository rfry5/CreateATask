package com.example.rachel.createatask;
//A searchable activity is the activity in the app that performs searches based on a query string and presents the
//search results

//Declared searchable activity in manifest

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnMenuTabClickListener;

import junit.framework.Test;

import java.util.ArrayList;
import java.util.Collections;

import static android.R.attr.country;
import static android.media.CamcorderProfile.get;


public class SearchableActivity extends AppCompatActivity {

//    private ListView mListView;
    private SearchView mSearchView;
    private TextView mTextView;
//    DatabaseHelp helper;
    EditText txtName, txtSku, txtLocation, txtDescription;
    SQLiteDatabase db;
    // Adapter Object
    SimpleCursorAdapter adapter;
    String selected_ID = "";
    private Bitmap mImageBitmap;
    private ImageView mImageView;
    private VideoView mVideoView;
    //For Bottom Bar navigation
    private BottomBar mBottomBar;
    ListView listView;
    DatabaseHelp helper = new DatabaseHelp(this);
    ArrayList<ItemInfo> list;
    Integer flag;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_library);

        final DatabaseHelp helper = new DatabaseHelp(this);

        txtName = (EditText) findViewById(R.id.item_name);
        txtSku = (EditText) findViewById(R.id.sku);
        txtLocation = (EditText) findViewById(R.id.location);
        txtDescription = (EditText) findViewById(R.id.description);
        mImageView = (ImageView) findViewById(R.id.imageView_display);
        mVideoView = (VideoView) findViewById(R.id.videoView_display);
        SwipeMenuListView mListView = (SwipeMenuListView) findViewById(R.id.main_list_view);
        mSearchView = (SearchView) findViewById(R.id.search_items);
        mSearchView.setIconifiedByDefault(false);
        mTextView = (TextView) findViewById(R.id.text_view_search);
//        handleIntent(getIntent());

        //Setting click listener for search bar
//        mSearchView.setOnQueryTextListener(this);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) findViewById(R.id.search_items);
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

        System.out.println("START OF SEARCHABLE ACTIVITY ");

        //Retrieving data for listview
        fetchData();
        System.out.println("AFTER FETCH, SEARCHABLE ACTIVITY ");

        //This is what happens when the item is clicked in the ListView//
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item, sku, location, description, image, video, post, c1, c2, c3, c4, c5, c6, spinnerType, spinnerSize;
                byte [] thumbnailblob;

                //Add print statement
                System.out.println("IN ON CLICK LISTENER ");
                //Start EditTask Intent, should include all of the data and an update feature
                ItemInfo row = (ItemInfo) parent.getItemAtPosition(position);
                post = String.valueOf(row.getID());
                item = row.getItemname();
                sku = row.getSku();
                location = row.getLocation();
                description=row.getDescription();
                image = row.getPicture();
                System.out.println("Image after update " + image);
                video = row.getVideo();
                System.out.println("Video after update " + video);
                c1 = row.getCom1();

                c2 = row.getCom2();
                c3 = row.getCom3();
                c4 = row.getCom4();
                c5 = row.getCom5();
                c6 = row.getCom6();

                spinnerType = row.getSpinType();
                spinnerSize = row.getSpinSize();

                System.out.println("INSIDE SEARCHABLE ACTIVITY LINE 128 " + c1);

                thumbnailblob = row.getThumbnail();

                //Passing item information into EditTask activity
                Intent itemDisplay = new Intent(getApplicationContext(), EditTask.class);
                itemDisplay.putExtra("item", item);
                itemDisplay.putExtra("sku", sku);
                itemDisplay.putExtra("location", location);
                itemDisplay.putExtra("description", description);
                itemDisplay.putExtra("image", image);
                itemDisplay.putExtra("video", video);
                itemDisplay.putExtra("position", post);
                itemDisplay.putExtra("com1", c1);
                itemDisplay.putExtra("com2", c2);
                itemDisplay.putExtra("com3", c3);
                itemDisplay.putExtra("com4", c4);
                itemDisplay.putExtra("com5", c5);
                itemDisplay.putExtra("com6", c6);
                itemDisplay.putExtra("spinType", spinnerType);
                itemDisplay.putExtra("spinSize", spinnerSize);

                ///ADDED TODAY
                itemDisplay.putExtra("thumbnailblob", thumbnailblob); //Just added
                //////
                startActivity(itemDisplay);

            }
        });

//        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                System.out.println("In search submit ");
//
//                ArrayList<ItemInfo> search = new ArrayList<ItemInfo>();
//                Cursor c = helper.searchItems(query);
//                c.moveToFirst();
//
//                if (c.getCount() == 0) {
//                    Toast temp = Toast.makeText(SearchableActivity.this, "No Results Found", Toast.LENGTH_SHORT);
//                    temp.show();
//                    temp.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
//                    temp.show();
//                } else if (c.getCount() > 0) {
//                    do {
//                        ItemInfo i = new ItemInfo();
//                        i.setItemname(c.getString(1));
//                        i.setSku(c.getString(2));
//                        i.setLocation(c.getString(3));
//                        i.setDescription(c.getString(4));
//                        //Setting URI path to a string
//                        i.setPicture(c.getString(5));
//                        i.setVideo(c.getString(6));
//                        i.setThumbnail(c.getBlob(7));
//                        i.setID(c.getInt(0));
//                        search.add(i);
//                    } while (c.moveToNext());
//
//                    System.out.println("Cursor in Search intent");
//                    Log.v("Cursor Object", DatabaseUtils.dumpCursorToString(c));
//                    adapter.notifyDataSetChanged();
//                }
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                System.out.println("In search text change listener ");
//                //Adds box with search text on the screen
////                if (TextUtils.isEmpty(newText)) {
////                    listView.clearTextFilter();
////                } else {
////                    listView.setFilterText(newText.toString());
////                }
//                adapter.getFilter().filter(newText);
//                return false;
//            }
//        });

        //Setting search listeners
//        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                System.out.println("In search submit ");
//                itemSearch(query);
//                System.out.println("return to searchable activity  ");
////                fetchData();
////                adapter.notifyDataSetChanged();
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                return true;
//            }
//
//            public void itemSearch(String query){
//                //Perform search
//                helper.searchItems(query);
//                //Now I want to display the item
//            }
//        });

        //Bottom Bar navigation
        mBottomBar = BottomBar.attach(this, savedInstanceState);
        mBottomBar.useFixedMode(); //Shows all titles with more than 3 buttons
        mBottomBar.setItems(R.menu.bottombar_menu);
        //Need to setDefaultTabPosition to the activity (tab 2 is search library, etc.) or it won't state
        // in that default state
        mBottomBar.setDefaultTabPosition(2);
        mBottomBar.setOnMenuTabClickListener(new OnMenuTabClickListener() {
            @Override
            public void onMenuTabSelected(@IdRes int menuItemId) {
                if (menuItemId == R.id.bottomBarItemHome) {
//                    // The user selected item number one.
                    startActivity(new Intent(SearchableActivity.this, MainActivity.class));
                } else if (menuItemId == R.id.bottomBarItemThree) {
                    startActivity(new Intent(SearchableActivity.this, Dashboard.class));
                } else if (menuItemId == R.id.bottomBarItemOne) {
                    startActivity(new Intent(SearchableActivity.this, CreateTask.class));
                }
            }

            @Override
            public void onMenuTabReSelected(@IdRes int menuItemId) {
                if (menuItemId == R.id.bottomBarItemTwo) {
                    // The user reselected item number one, scroll your content to top.
                }
            }
        });

    }

    //Bottom bar navigation
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Necessary to restore the BottomBar's state, otherwise we would
        // lose the current tab on orientation change.
        mBottomBar.onSaveInstanceState(outState);
    }

//    @Override
//    protected void onNewIntent(Intent intent) {
//        System.out.println("INSIDE on new intent");
//        setIntent(intent);
//        handleIntent(intent);
//    }
//
//    private void handleIntent(Intent intent) {
//        System.out.println("INSIDE handle intent");
//        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
//            String query = intent.getStringExtra(SearchManager.QUERY);
//
//            ArrayList<ItemInfo> search = new ArrayList<ItemInfo>();
//            Cursor c = helper.searchItems(query);
//            c.moveToFirst();
//
//            //If no search results are found
//            if (c.getCount() == 0) {
//                Toast temp = Toast.makeText(SearchableActivity.this, "No Results Found", Toast.LENGTH_SHORT);
//                temp.show();
//                temp.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
//                temp.show();
//            } else if (c.getCount() > 0) {
//                do {
//                    ItemInfo i = new ItemInfo();
//                    i.setItemname(c.getString(1));
//                    i.setSku(c.getString(2));
//                    i.setLocation(c.getString(3));
//                    i.setDescription(c.getString(4));
//                    //Setting URI path to a string
//                    i.setPicture(c.getString(5));
//                    i.setVideo(c.getString(6));
//                    i.setThumbnail(c.getBlob(7));
//                    i.setID(c.getInt(0));
//                    search.add(i);
//                } while (c.moveToNext());
//
//                System.out.println("Cursor in Search intent");
//                Log.v("Cursor Object", DatabaseUtils.dumpCursorToString(c));
//
////                InfoAdapter adapter = new InfoAdapter(this, search);
//                InfoAdapter adapter = new InfoAdapter(this, search);
//                listView = (ListView) findViewById(R.id.main_list_view);
//                adapter.addAll(search);
//                adapter.notifyDataSetChanged();
//                listView.setAdapter(adapter);
//            }
//
//        }
//    }

    ///////////// Fetch data from database and display into listview ///////////
    private void fetchData() {

        System.out.println("INSIDE FETCH DATA");
        System.out.println("Count of array inside fetch data " + helper.getAll().size());


//        Collections.reverse(arrayOfInfo); Trying to reverse listview to have most recent first
        //Setting empty arraylist for item information
        ArrayList<ItemInfo> arrayOfInfo = new ArrayList<ItemInfo>();
        ArrayList<ItemInfo> allinfo = helper.getAll();
        System.out.println("Count of array allinfo " + allinfo.size());


        // Setting the adapter to the arraylist
        final InfoAdapter adapter = new InfoAdapter(this, arrayOfInfo);
//        adapter.notifyDataSetChanged();



        // Attach the adapter to a ListView
        listView = (ListView) findViewById(R.id.main_list_view);
        //Setting the adapter to all info, I want searched info also
        adapter.addAll(allinfo);
        System.out.println("Count of adapter " + adapter.getCount());
        listView.setAdapter(adapter);

        ////////Enabling swipe to delete (from library online, Swipe Menu List View library github)////////
        final SwipeMenuListView mListView = (SwipeMenuListView) findViewById(R.id.main_list_view);
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // Create different menus depending on the view type
                switch (menu.getViewType()) {
                    case 0:
                        createMenu1(menu);
                        break;
                }
            }
            private void createMenu1(SwipeMenu menu) {
                //CREATING FLAG ICON
                SwipeMenuItem item2 = new SwipeMenuItem(
                        getApplicationContext());
                item2.setBackground(new ColorDrawable(Color.rgb(0xFF,
                        0x99, 0x00)));
                item2.setWidth(200);
                item2.setIcon(R.drawable.ic_assistant_photo_black_24dp);
                item2.setTitle("FLAG");
                item2.setTitleSize(16);
                item2.setTitleColor(Color.WHITE);
                menu.addMenuItem(item2);

                //CREATING DELETE ICON
                SwipeMenuItem item1 = new SwipeMenuItem(
                        getApplicationContext());
                item1.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                item1.setWidth(200);
                item1.setIcon(R.drawable.ic_delete_black_40dp);
                item1.setTitle("DELETE");
                item1.setTitleSize(16);
                item1.setTitleColor(Color.WHITE);
                menu.addMenuItem(item1);
            }
        };
        // set creator
        mListView.setMenuCreator(creator);

        //Delete click after swiping in ListView
        mListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                ArrayList<ItemInfo> allinfo = helper.getAll();
                System.out.println("Array list " + helper.getAll().get(position).getID());
                Integer actualID = allinfo.get(position).getID();

                System.out.println("Position from searchable activity " + position);

                switch (index) {

                    case 0:
                        //FLAG for urgency (sent to dashboard)
                        //Adding "1" into urgency column

                        Integer urg = 1;

                        ItemInfo c = new ItemInfo();
                        c.setID(allinfo.get(position).getID());
                        c.setItemname(allinfo.get(position).getItemname());
                        c.setSku(allinfo.get(position).getSku());
                        c.setLocation(allinfo.get(position).getLocation());
                        c.setDescription(allinfo.get(position).getDescription());
                        c.setCom1(allinfo.get(position).getCom1());
                        c.setCom2(allinfo.get(position).getCom2());
                        c.setCom3(allinfo.get(position).getCom3());
                        c.setCom4(allinfo.get(position).getCom4());
                        c.setCom5(allinfo.get(position).getCom5());
                        c.setCom6(allinfo.get(position).getCom6());
                        c.setSpinType(allinfo.get(position).getSpinType());
                        c.setSpinSize(allinfo.get(position).getSpinSize());
                        c.setItemFlag(urg);

//                        String testing = allinfo.get(position).getItemname();
//                        System.out.println("IN FLAG ITEM SWIPE " + testing);

                        helper.flagItem(c);

                        break;

                    case 1:
                        // DELETE from database
                        helper.deleteItem(actualID);
                        adapter.notifyDataSetChanged();
                        fetchData();
                        break;
                }
                return false;
            }
        });

        mListView.setOnSwipeListener(new SwipeMenuListView.OnSwipeListener() {
            @Override
            public void onSwipeStart(int position) {
                // swipe start
            }
            @Override
            public void onSwipeEnd(int position) {
                // swipe end
            }
        });

//        Setting search listeners
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                System.out.println("In search submit ");

                ArrayList<ItemInfo> search = new ArrayList<ItemInfo>();
                Cursor c = helper.searchItems(query);
                c.moveToFirst();

                if (c.getCount() == 0) {
                    Toast temp = Toast.makeText(SearchableActivity.this, "No Results Found", Toast.LENGTH_SHORT);
                    temp.show();
                    temp.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
                    temp.show();
                } else if (c.getCount() > 0) {
                    do {
                        ItemInfo i = new ItemInfo();
                        i.setItemname(c.getString(1));
                        i.setSku(c.getString(2));
                        i.setLocation(c.getString(3));
                        i.setDescription(c.getString(4));
                        //Setting URI path to a string
                        i.setPicture(c.getString(5));
                        i.setVideo(c.getString(6));
                        i.setThumbnail(c.getBlob(7));
                        i.setID(c.getInt(0));
                        search.add(i);
                    } while (c.moveToNext());

                    System.out.println("Cursor in Search intent");
                    Log.v("Cursor Object", DatabaseUtils.dumpCursorToString(c));
                    adapter.notifyDataSetChanged();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                System.out.println("In search text change listener ");
                //Adds box with search text on the screen
//                if (TextUtils.isEmpty(newText)) {
//                    listView.clearTextFilter();
//                } else {
//                    listView.setFilterText(newText.toString());
//                }
                adapter.getFilter().filter(newText);
                return false;
            }
        });

        /*
        //ViewBinder to resize image BELOW WORKS TO RESIZE IMAGE BITMAP, DIDN'T HELP WITH LAGGING, TRYING CACHE
        adapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
                if (view instanceof ImageView) {
                    ImageView image = (ImageView) view;
                    // just to see what is returned by this!
                    Log.d("orange", "images: " + cursor.getString(columnIndex));
                    Bitmap thumbImage = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(cursor.getString(columnIndex)), 150, 150);
                    image.setImageBitmap(thumbImage);
//                    Bitmap thumbImage = BitmapFactory.decodeFile(cursor.getString(columnIndex));
//                    image.setImageBitmap(Bitmap.createScaledBitmap(thumbImage, 10, 10, false));
                    return true;
                }
                return false;
            }
        });
        */
        }
    }

