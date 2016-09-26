package com.example.rachel.createatask;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnMenuTabClickListener;

import java.util.ArrayList;

import io.karim.MaterialTabs;


public class Dashboard extends AppCompatActivity{

    //For Bottom Bar navigation
    private BottomBar mBottomBar;
    DatabaseHelp helper = new DatabaseHelp(this);
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_dashboard);

        mListView = (ListView) findViewById(R.id.dash_list_view);

        //Need to set adapter to get items in database

        ArrayList<ItemInfo> arrayOfInfo = new ArrayList<ItemInfo>();
        ArrayList<ItemInfo> allinfo = helper.getUrgent();

        // Setting the adapter to the arraylist
        final InfoAdapter adapter = new InfoAdapter(this, arrayOfInfo);

        // Attach the adapter to a ListView
        mListView = (ListView) findViewById(R.id.dash_list_view);
        //Setting the adapter to all info, I want searched info also
        adapter.addAll(allinfo);
        System.out.println("Count of adapter " + adapter.getCount());
        mListView.setAdapter(adapter);


        ////////Enabling swipe to delete (from library online, Swipe Menu List View library github)////////
        final SwipeMenuListView mListView = (SwipeMenuListView) findViewById(R.id.dash_list_view);
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
                SwipeMenuItem item1 = new SwipeMenuItem(
                        getApplicationContext());
                item1.setBackground(new ColorDrawable(Color.rgb(0x5E,
                        0xC6, 0x39)));
                item1.setWidth(200);
                item1.setIcon(R.drawable.ic_check_black_40dp);
                item1.setTitle("Resolve");
                item1.setTitleSize(16);
                item1.setTitleColor(Color.WHITE);
                menu.addMenuItem(item1);

                SwipeMenuItem item2 = new SwipeMenuItem(
                        getApplicationContext());
                item2.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                item2.setWidth(200);
//                item2.setIcon(R.drawable.ic_check_black_40dp);
                item2.setTitle("OUT OF STOCK");
                item2.setTitleSize(16);
                item2.setTitleColor(Color.WHITE);
                menu.addMenuItem(item2);

            }
        };
        // set creator
        mListView.setMenuCreator(creator);

        //Delete click after swiping DELETE FROM LISTVIEW NEED TO FIX
        mListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                ArrayList<ItemInfo> allinfo = helper.getUrgent();
                System.out.println("Array list " + helper.getUrgent().get(position).getID());
                Integer actualID = allinfo.get(position).getID();

                System.out.println("Position from searchable activity " + position);
                switch (index) {
                    case 0:
                        //Resolve
                        System.out.println("INSIDE RESOLVE CASE DASHBOARD LINE 123");

                        helper.resolveItems(actualID);
                        adapter.clear();
                        ArrayList <ItemInfo> newInfo = helper.getUrgent();
//                        adapter.clear();
                        adapter.addAll(newInfo);
                        System.out.println("notify changes " + actualID);

                        break;

                    case 1:
                        // delete
                        helper.deleteItem(actualID);
////                        System.out.println(Arrays.toString(item));
//                        allinfo.remove(position);
                        System.out.println("After remove position");
                        adapter.notifyDataSetChanged();
                        System.out.println("After data change");
//                        fetchData();
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




        //Bottom Bar navigation
        mBottomBar = BottomBar.attach(this, savedInstanceState);
        mBottomBar.useFixedMode(); //Shows all titles with more than 3 buttons
        mBottomBar.setItems(R.menu.bottombar_menu);
        mBottomBar.setDefaultTabPosition(3);
        mBottomBar.setOnMenuTabClickListener(new OnMenuTabClickListener() {
            @Override
            public void onMenuTabSelected(@IdRes int menuItemId) {
                if (menuItemId == R.id.bottomBarItemHome) {
                    // The user selected item number one.
                    startActivity(new Intent(Dashboard.this, MainActivity.class));
                } else if (menuItemId == R.id.bottomBarItemTwo) {
                    startActivity(new Intent(Dashboard.this, SearchableActivity.class));
                } else if (menuItemId == R.id.bottomBarItemOne) {
                    startActivity(new Intent(Dashboard.this, CreateTask.class));
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
}
