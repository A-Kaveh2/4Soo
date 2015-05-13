package ir.rasen.charsoo.view.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import ir.rasen.charsoo.R;
import ir.rasen.charsoo.controller.helper.ActionBar_M;
import ir.rasen.charsoo.controller.helper.Image_M;
import ir.rasen.charsoo.controller.helper.Params;
import ir.rasen.charsoo.model.DownloadImages;


public class ActivityProfilePicture extends ActionBarActivity  {




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_picture);

        int profilePictureId = getIntent().getExtras().getInt(Params.PROFILE_PICTURE_ID);
        String userIdentifier = getIntent().getExtras().getString(Params.USER_IDENTIFIER);

        ActionBar_M.setActionBar(getSupportActionBar(), this,userIdentifier);

        DownloadImages downloadImages = new DownloadImages(this);
        downloadImages.download(profilePictureId,Image_M.LARGE, Image_M.ImageType.USER,(ImageView)findViewById(R.id.imageView_profile_picture),false);


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
