package ir.rasen.charsoo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import java.io.File;
import java.text.DecimalFormat;

import ir.rasen.charsoo.classes.MyApplication;
import ir.rasen.charsoo.classes.Post;
import ir.rasen.charsoo.dialog.DialogMessage;
import ir.rasen.charsoo.dialog.PopupCameraGallery;
import ir.rasen.charsoo.helper.ActionBar_M;
import ir.rasen.charsoo.helper.Image_M;
import ir.rasen.charsoo.helper.LoginInfo;
import ir.rasen.charsoo.helper.Params;
import ir.rasen.charsoo.helper.ServerAnswer;
import ir.rasen.charsoo.helper.TextProcessor;
import ir.rasen.charsoo.helper.Validation;
import ir.rasen.charsoo.helper.WebservicesHandler;
import ir.rasen.charsoo.interfaces.IWebserviceResponse;
import ir.rasen.charsoo.ui.EditTextFont;
import ir.rasen.charsoo.webservices.DownloadImages;
import ir.rasen.charsoo.webservices.post.AddPost;
import ir.rasen.charsoo.webservices.post.GetPost;
import ir.rasen.charsoo.webservices.post.UpdatePost;


public class ActivityProfilePicture extends ActionBarActivity  {




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_picture);

        int profilePictureId = getIntent().getExtras().getInt(Params.PROFILE_PICTURE_ID);
        String userIdentifier = getIntent().getExtras().getString(Params.USER_IDENTIFIER);

        ActionBar_M.setActionBar(getSupportActionBar(), this,userIdentifier);

        DownloadImages downloadImages = new DownloadImages(this);
        downloadImages.download(profilePictureId,Image_M.LARGE,(ImageView)findViewById(R.id.imageView_profile_picture),false);


    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        /*MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_next_button, menu);*/
        return true;
    }





    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        } else
            return super.onOptionsItemSelected(item);
    }



}
