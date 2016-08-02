package com.example.rachel.createatask;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.IdRes;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.VideoView;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnMenuTabClickListener;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.R.attr.checked;
import static android.R.attr.id;
import static android.R.attr.password;
import static android.R.attr.type;
import static android.os.Build.VERSION_CODES.M;
import static android.os.Environment.DIRECTORY_PICTURES;
import static android.os.Environment.getExternalStoragePublicDirectory;
import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;
import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO;

public class CreateTask extends AppCompatActivity implements View.OnClickListener {

    private static final int ACTION_TAKE_PHOTO_S = 2;
    private static final int ACTION_TAKE_VIDEO = 3;
    protected static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 0;

    private static final String BITMAP_STORAGE_KEY = "viewbitmap";
    private static final String IMAGEVIEW_VISIBILITY_STORAGE_KEY = "imageviewvisibility";
    private Bitmap mImageBitmap;
    //
    private static final String VIDEO_STORAGE_KEY = "viewvideo";
    private static final String VIDEOVIEW_VISIBILITY_STORAGE_KEY = "videoviewvisibility";
    private VideoView mVideoView;
    private Uri mVideoUri;

    private String mCurrentPhotoPath;
    static final int REQUEST_TAKE_PHOTO = 1;
    private ImageView mImageView;

    private static final String JPEG_FILE_PREFIX = "IMG_";
    private static final String JPEG_FILE_SUFFIX = ".jpg";

    // app that works
    private Uri mCapturedImageURI;
    private Uri mCapturedVideoURI;
    private static final int RESULT_LOAD_IMAGE = 1;
    private static final int REQUEST_IMAGE_CAPTURE = 2;
    private static final int RESULT_LOAD_VIDEO = 1;
    private static final int REQUEST_VIDEO_CAPTURE = 3;
//    ArrayList<MyImage> images;
//    ArrayList<MyVideos> videos;
    // 1 = take video, 2 = picture
    private static int VID_OR_PIC = 1;
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;


    //For database
    DatabaseHelp helper = new DatabaseHelp(this);

    //For Bottom Bar navigation
    private BottomBar mBottomBar;


    //For save button, play button over video and item information input
    Button bSaveButton;
    ImageButton mPlayButton;
    EditText etItemName, etSku, etLocation, etDescription;



    /*
    // taking the video
    private void dispatchTakeVideoIntent() {
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        startActivityForResult(takeVideoIntent, ACTION_TAKE_VIDEO);
    }
    */


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
        setContentView(R.layout.new_create);

//        images = new ArrayList();
//        videos = new ArrayList();

        etItemName = (EditText) findViewById(R.id.item_name);
        etSku = (EditText) findViewById(R.id.sku);
        etDescription = (EditText) findViewById(R.id.description);
        etLocation = (EditText) findViewById(R.id.location);


        //For save button
        bSaveButton = (Button) findViewById(R.id.save_button);
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
                    startActivity(new Intent(CreateTask.this, MainActivity.class));
                } else if (menuItemId == R.id.bottomBarItemTwo) {
                    startActivity(new Intent(CreateTask.this, SearchableActivity.class));
                } else if (menuItemId == R.id.bottomBarItemThree) {
                    startActivity(new Intent(CreateTask.this, Dashboard.class));
                }
            }

            @Override
            public void onMenuTabReSelected(@IdRes int menuItemId) {
                if (menuItemId == R.id.bottomBarItemTwo) {
                    // The user reselected item number one, scroll your content to top.
                }
            }
        });

        mImageView = (ImageView) findViewById(R.id.imageView1);
        mVideoView = (VideoView) findViewById(R.id.videoView1);
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


    /* For DB
        video location = mCapturedVideoURI.getpath()
        picture = mCapturedPictureURI.getpath()

            -- lookup how to store picture/video to phone DB

        iteminfo newitem = new Iteminfo();
        newitem.Video = mcapturedvideouri.getpath();
        newitem.Pic = picture;
        newitme.sku ...
        newitem.name = ....

        helper.insert(newitem);

    */



    private static Uri getOutputMediaFileUri(int type){
        return Uri.fromFile(getOutputMediaFile(type));
    }

    //Removed after help with Ben
//    private static Uri getOutputMediaFileUri(){
//        return Uri.fromFile(getOutputMediaFile());
//    }

    /** Create a File for saving an image or video */
    //Changed () to int type
    private static File getOutputMediaFile(int type){

        //"" to send it to Pictures folder in gallery
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "");

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }

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

//        // Create a media file name
//        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
//        File mediaFile = new File(mediaStorageDir.getPath() + File.separator +
//                    "VID_"+ timeStamp + ".mp4");

        return mediaFile;
    }

    private void activeTakeVideo(){
        VID_OR_PIC = 1;
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if(takeVideoIntent.resolveActivity(getPackageManager()) != null){
//            System.out.println("I am here in the video thing!!");
//            String fileName = "temp.jpg";
            mCapturedVideoURI = getOutputMediaFileUri(MEDIA_TYPE_VIDEO);
//            mCapturedVideoURI = getOutputMediaFileUri();
            takeVideoIntent.putExtra(MediaStore.EXTRA_OUTPUT, mCapturedVideoURI);
            takeVideoIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
            startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);        }
    }

    /**
     * take a photo
     */
    private void activeTakePhoto() {
        VID_OR_PIC = 2;
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {

//            mCapturedImageURI = getOutputMediaFileUri();
//            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mCapturedImageURI);
//            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);

            String fileName = "temp1.jpg";
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.TITLE, fileName);
            mCapturedImageURI =  getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
//            mCapturedImageURI = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mCapturedImageURI);
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }


    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        System.out.println(requestCode);
        super.onActivityResult(requestCode, resultCode, data);
//        mPlayButton = (ImageButton) findViewById(R.id.play_button);

        if (VID_OR_PIC == 2 && requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
//            String[] projection = {MediaStore.Images.Media.DATA};
//            System.out.println("INHERE");
//            Cursor cursor = managedQuery(mCapturedImageURI, projection, null, null, null);
//            int column_index_data = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//            cursor.moveToFirst();
//            String picturePath = cursor.getString(column_index_data);
            String picturePath = mCapturedImageURI.getPath();
            MyImage image = new MyImage();
            image.setTitle("Test");
            image.setDescription("test take a photo and add it to list view");
            image.setDatetime(System.currentTimeMillis());
            image.setPath(picturePath);
//            images.add(image);
            mImageBitmap = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(image.getPath()),200,250);
            mImageView.setImageBitmap(mImageBitmap);
            mImageView.setVisibility(View.VISIBLE);
        }
        // this is for video
        if(VID_OR_PIC == 1 && requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK){
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

           /* mVideoView.getLayoutParams().height = (int) getResources().getDimension(R.dimen.imageview_height);
            mVideoView.getLayoutParams().width = (int) getResources().getDimension(R.dimen.imageview_width);
            System.out.println(mVideoView.getHeight());
            System.out.println(mVideoView.getWidth());
            mVideoView.setMinimumWidth((int) getResources().getDimension(R.dimen.imageview_width));
            mVideoView.setMinimumHeight((int) getResources().getDimension(R.dimen.imageview_height));
*/
            mVideoView.setVisibility(View.VISIBLE);
        }
    }


    /*
    // camera handle -- look into this
    private void handleSmallCameraPhoto(Intent intent) {
        Log.d("PIPE", "Line 224 -- handling the intent");
        Bundle extras = intent.getExtras();
        mImageBitmap = (Bitmap) extras.get("data");
        mImageView.setImageBitmap(mImageBitmap);
        mVideoUri = null;
        mImageView.setVisibility(View.VISIBLE);
        mVideoView.setVisibility(View.INVISIBLE);
    }
    */


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


/*
    // take the photo or video -- listener
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("PIPE", "LINE 278 - switch to take vid or pic");
        switch (requestCode) {

            case ACTION_TAKE_PHOTO_S: {
                if (resultCode == RESULT_OK) {
                    handleSmallCameraPhoto(data);
//                    savePic(data);
                }
                break;
            } // ACTION_TAKE_PHOTO_S

            case ACTION_TAKE_VIDEO: {
                if (resultCode == RESULT_OK) {
                    handleCameraVideo(data);
                }
                break;
            } // ACTION_TAKE_VIDEO
        } // switch
    }
*/

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


    //Saving item information to database
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.save_button:

                EditText itemname = (EditText) findViewById(R.id.item_name);
                EditText sku = (EditText) findViewById(R.id.sku);
                EditText location = (EditText) findViewById(R.id.location);
                EditText description = (EditText) findViewById(R.id.description);

                String itemnamestring = itemname.getText().toString();
                String skustring = sku.getText().toString();
                String locationstring = location.getText().toString();
                String descriptionstring = description.getText().toString();


                //Passing through database
                ItemInfo c = new ItemInfo();
                c.setItemname(itemnamestring);
                c.setSku(skustring);
                c.setLocation(locationstring);
                c.setDescription(descriptionstring);
                //Setting URI path to a string
                c.setPicture(mCapturedImageURI.getPath());
                c.setVideo(mCapturedVideoURI.getPath());



                helper.insertItem(c);

                Toast temp = Toast.makeText(CreateTask.this, "Save Successful", Toast.LENGTH_SHORT);
                temp.show();
                temp.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
                temp.show();

                startActivity(new Intent(this, MainActivity.class));

                break;

        }


    }
}

