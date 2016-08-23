package com.example.rachel.createatask;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.IdRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.BottomBarTab;
import com.roughike.bottombar.OnMenuTabClickListener;
import com.roughike.bottombar.OnTabSelectedListener;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static com.example.rachel.createatask.R.id.location;
import static com.example.rachel.createatask.R.id.sku;

public class EditTask extends AppCompatActivity implements View.OnClickListener{

    private Bitmap mImageBitmap;
    String selected_ID = "";

    private VideoView mVideoView;
    private Uri mVideoUri;
    private String mCurrentPhotoPath;
    private ImageView mImageView;
    private Uri mCapturedImageURI;
    private Uri mCapturedVideoURI;
    private static final int REQUEST_IMAGE_CAPTURE = 2;
    private static final int REQUEST_VIDEO_CAPTURE = 3;
    private static int VID_OR_PIC = 1;
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;
    SQLiteDatabase db;


    //For database
    DatabaseHelp helper = new DatabaseHelp(this);

    //For Bottom Bar navigation
    private BottomBar mBottomBar;

    //For save button, play button over video and item information input
    Button bSaveButton;
    ImageButton mPlayButton;
    EditText etItemName, etSku, etLocation, etDescription;


    //Setting button listener for take photo and take video
    Button.OnClickListener mTakePicSOnClickListener =
            new Button.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //dispatchTakePictureIntent(ACTION_TAKE_PHOTO_S);
                    activeTakePhoto();
                }
            };

    Button.OnClickListener mTakeVidOnClickListener =
            new Button.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //dispatchTakeVideoIntent();
                    activeTakeVideo();
                }
            };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_item_info);

        etItemName = (EditText) findViewById(R.id.item_name_display);
        etSku = (EditText) findViewById(R.id.sku_display);
        etDescription = (EditText) findViewById(R.id.description_display);
        etLocation = (EditText) findViewById(R.id.location_display);
        mImageView = (ImageView) findViewById(R.id.imageView_display);
        mVideoView = (VideoView) findViewById(R.id.videoView_display);

        //Getting info when Listview was clicked from library (SearchableActivity) activity
        Intent i = getIntent();
        String item = i.getStringExtra("item");
        String sku = i.getStringExtra("sku");
        String location = i.getStringExtra("location");
        String description = i.getStringExtra("description");
        String image = i.getStringExtra("image");
        String video = i.getStringExtra("video");
        String post = i.getStringExtra("position");
        System.out.println("Position " + post);
        etItemName.setText(item);
        etSku.setText(sku);
        etLocation.setText(location);
        etDescription.setText(description);
        //If the image or the video is null, this crashes
        mImageBitmap = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(image),200,250);
        mImageView.setImageBitmap(mImageBitmap);
        if (video == null){
            System.out.println("No video file exists");
        } else if (video != null) {
            mVideoView.setVideoPath(video);
        mVideoView.seekTo(100);
        mVideoView.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (mVideoView.isPlaying())
                {
                    return false;
                }
                else
                {
                    mVideoView.start();
//                    mPlayButton.setVisibility(View.GONE);
                    return false;
                }

            }
        });
        }

        //For update button
        bSaveButton = (Button) findViewById(R.id.update_button);
        bSaveButton.setOnClickListener(this);

        //Bottom Bar navigation
        mBottomBar = BottomBar.attach(this, savedInstanceState);
        mBottomBar.useFixedMode(); //Shows all titles with more than 3 buttons
        mBottomBar.setItems(R.menu.bottombar_menu);
        mBottomBar.setOnMenuTabClickListener(new OnMenuTabClickListener() {
            @Override
            public void onMenuTabSelected(@IdRes int menuItemId) {
                if (menuItemId == R.id.bottomBarItemHome) {
                    // The user selected item number one.
                    startActivity(new Intent(EditTask.this, MainActivity.class));
                } else if (menuItemId == R.id.bottomBarItemTwo) {
                    startActivity(new Intent(EditTask.this, SearchableActivity.class));
                } else if (menuItemId == R.id.bottomBarItemThree) {
                    startActivity(new Intent(EditTask.this, Dashboard.class));
                }
            }

            @Override
            public void onMenuTabReSelected(@IdRes int menuItemId) {
                if (menuItemId == R.id.bottomBarItemTwo) {
                    // The user reselected item number one, scroll your content to top.
                }
            }
        });


        mImageBitmap = null;
        mVideoUri = null;

        Button picSBtn = (Button) findViewById(R.id.take_small_photo_button);
        setBtnListenerOrDisable(
                picSBtn,
                mTakePicSOnClickListener,
                MediaStore.ACTION_IMAGE_CAPTURE
        );

        Button vidBtn = (Button) findViewById(R.id.take_video_button);
        setBtnListenerOrDisable(
                vidBtn,
                mTakeVidOnClickListener,
                MediaStore.ACTION_VIDEO_CAPTURE
        );
    }


    //Creating URI from file
    private static Uri getOutputMediaFileUri(int type){
        return Uri.fromFile(getOutputMediaFile(type));
    }


    /** Create a File for saving an image or video */
    //Changed () to int type
    private static File getOutputMediaFile(int type){

        //"" to send it to Pictures folder in gallery
        //File path is pointing to gallery
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "");

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }
        //Time stamp image and name the image (still blank, allocating file)
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE){
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_"+ timeStamp + ".jpg");
        } else if(type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "VID_"+ timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }

    private void activeTakeVideo(){
        VID_OR_PIC = 1;
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if(takeVideoIntent.resolveActivity(getPackageManager()) != null){
            mCapturedVideoURI = getOutputMediaFileUri(MEDIA_TYPE_VIDEO);
            takeVideoIntent.putExtra(MediaStore.EXTRA_OUTPUT, mCapturedVideoURI);
            takeVideoIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
            startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);        }
    }

    //take a photo
    private void activeTakePhoto() {
        VID_OR_PIC = 2;
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {

            //Creating space for photo
            mCapturedImageURI =  getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mCapturedImageURI);
            //Now fill URI with picture
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }


    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        System.out.println(requestCode);
        super.onActivityResult(requestCode, resultCode, data);
        mPlayButton = (ImageButton) findViewById(R.id.play_button);

        if (VID_OR_PIC == 2 && requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            String picturePath = mCapturedImageURI.getPath();
            System.out.println(picturePath);
            MyImage image = new MyImage();
            image.setTitle("Test");
            image.setDescription("test take a photo and add it to list view");
            image.setDatetime(System.currentTimeMillis());
            image.setPath(picturePath);
            mImageBitmap = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(image.getPath()),200,250);
            mImageView.setImageBitmap(mImageBitmap);
            mImageView.setVisibility(View.VISIBLE);
        }
        // this is for video
        if(VID_OR_PIC == 1 && requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK){
            Toast.makeText(this, "Video saved to:\n" + data.getData(), Toast.LENGTH_LONG).show();
            mVideoView.setVideoPath(mCapturedVideoURI.getPath());
            mVideoView.setOnTouchListener(new View.OnTouchListener()
            {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (mVideoView.isPlaying())
                    {
                        return false;
                    }
                    else
                    {
                        mVideoView.start();
                        return false;
                    }
                }
            });
            mVideoView.setVisibility(View.VISIBLE);
        }
    }


    // create the image file and name
    private File createImageFile() throws IOException {
        Log.d("PIPE", "Line 231 -- naming the picture");
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        Log.d("FILE", mCurrentPhotoPath);
        return image;
    }


    public static boolean isIntentAvailable(Context context, String action) {
        final PackageManager packageManager = context.getPackageManager();
        final Intent intent = new Intent(action);
        List<ResolveInfo> list =
                packageManager.queryIntentActivities(intent,
                        PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

    private void setBtnListenerOrDisable(
            Button btn,
            Button.OnClickListener onClickListener,
            String intentName
    ) {
        if (isIntentAvailable(this, intentName)) {
            btn.setOnClickListener(onClickListener);
        } else {
            btn.setText(
                    getText(R.string.cannot).toString() + " " + btn.getText());
            btn.setClickable(false);
        }
    }


    //Bottom bar navigation
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Necessary to restore the BottomBar's state, otherwise we would
        // lose the current tab on orientation change.
        mBottomBar.onSaveInstanceState(outState);
    }


    //Updating database

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.update_button:

                etItemName = (EditText) findViewById(R.id.item_name_display);
                etSku = (EditText) findViewById(R.id.sku_display);
                etDescription = (EditText) findViewById(R.id.description_display);
                etLocation = (EditText) findViewById(R.id.location_display);

                //Gathering new data from the R.id views
                String itemUpdate= etItemName.getText().toString();
                String skuUpdate = etSku.getText().toString();
                String locationUpdate = etLocation.getText().toString();
                String descriptionUpdate = etDescription.getText().toString();

                //Getting values passed from Listview click
                Intent i = getIntent();
                String post = i.getStringExtra("position");
                String image = i.getStringExtra("image");
                String video = i.getStringExtra("video");
                Integer position = Integer.valueOf(String.valueOf(post));
                System.out.println("In update with position " + position);


                //Have to take photo with update or it will crash!! Same with video
//                String picturePath = mCapturedImageURI.getPath();
//                mVideoView.setVideoPath(mCapturedVideoURI.getPath());

//                if (mCapturedImageURI.getPath() !=null) {
//                    mCapturedImageURI.getPath();
//                }
//
//                if (mCapturedVideoURI.getPath() !=null) {
//                    mVideoView.setVideoPath(mCapturedVideoURI.getPath());
//                }

                //Setting the values/item inputs
                ItemInfo c = new ItemInfo();
                c.setID(position);
                c.setItemname(itemUpdate);
                System.out.println("UPDATE ITEM NAME" + itemUpdate);
                c.setSku(skuUpdate);
                c.setLocation(locationUpdate);
                c.setDescription(descriptionUpdate);
                //Setting URI path to a string
//                if (mVideoView !=null) {
//                    c.setVideo(mVideoView.toString());
//                }

                if (mCapturedImageURI ==null) {
                    c.setPicture(image);
                    System.out.println("Image path when update is clicked" + image);
                }

                System.out.println(mCapturedImageURI);

//                } else {
//                    c.setPicture(mCapturedImageURI.getPath());
//                }

                if (mCapturedVideoURI ==null){
                    c.setVideo(video);
                    System.out.println("Video path when update is clicked" + video);
                }

                if (mCapturedVideoURI != null){
                    c.setVideo(video);
                }

                System.out.println("HERE" + mCapturedVideoURI);
//                } else {
//                    c.setVideo(mCapturedVideoURI.getPath());
//                }

//                c.setPicture(mCapturedImageURI.getPath());
//                c.setVideo(mVideoView.toString());

                //Calling udateItems function in DatabaseHelp
                helper.updateItems(c);


                Toast temp = Toast.makeText(EditTask.this, "Update Successful", Toast.LENGTH_SHORT);
                temp.show();
                temp.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
                temp.show();

                startActivity(new Intent(this, SearchableActivity.class));

                break;

        }


    }
}