package ir.rasen.charsoo;

/**
 * Created by android on 3/28/2015.
 */


import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import java.io.File;

import ir.rasen.charsoo.classes.Business;
import ir.rasen.charsoo.classes.MyApplication;
import ir.rasen.charsoo.dialog.DialogMessage;
import ir.rasen.charsoo.dialog.PopupCameraGallery;
import ir.rasen.charsoo.helper.Image_M;
import ir.rasen.charsoo.helper.Params;
import ir.rasen.charsoo.helper.ResultStatus;
import ir.rasen.charsoo.helper.ServerAnswer;
import ir.rasen.charsoo.interfaces.IGetCallForTakePicture;
import ir.rasen.charsoo.interfaces.IWebserviceResponse;
import ir.rasen.charsoo.webservices.business.GetBusinessProfileInfo;
import ir.rasen.charsoo.webservices.business.RegisterBusiness;
import ir.rasen.charsoo.webservices.business.UpdateBusinessProfileInfo;

public class ActivityRegisterEditBusiness extends ActionBarActivity implements IWebserviceResponse, IGetCallForTakePicture {

    FragmentRegisterBusinessBaseInfo fragmentBaseInfo;
    FragmentRegisterBusinessContactInfo fragmentContactInfo;
    FragmentRegisterBusinessLocationInfo fragmentLocationInfo;
    ProgressDialog progressDialog;
    String filePath, businessPictureString;


    private enum Fragments {BASE_INFO, CONTACT_INFO, LOCATION_INFO}

    ;
    private Fragments fragmentCurrent;
    FragmentManager fm;
    FragmentTransaction ft;
    LinearLayout llIndicatorBase, llIndicatorContact, llIndicatorLocation;
    MenuItem menuItemNext;
    int businessId = 0;
    String businessIdentifier;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_business);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.DeepSkyBlue)));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ((MyApplication) getApplication()).business = new Business();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getResources().getString(R.string.please_wait));

        llIndicatorBase = (LinearLayout) findViewById(R.id.ll_indicator_base);
        llIndicatorContact = (LinearLayout) findViewById(R.id.ll_indicator_contact);
        llIndicatorLocation = (LinearLayout) findViewById(R.id.ll_indicator_location);

        fragmentBaseInfo = new FragmentRegisterBusinessBaseInfo();
        fragmentContactInfo = new FragmentRegisterBusinessContactInfo();
        fragmentLocationInfo = new FragmentRegisterBusinessLocationInfo();
        fragmentCurrent = Fragments.BASE_INFO;


        try {
            businessId = getIntent().getExtras().getInt(Params.BUSINESS_ID);
            businessIdentifier = getIntent().getExtras().getString(Params.BUSINESS_IDENTIFIER);
        } catch (Exception e) {

        }

        Bundle bundle = new Bundle();
        if (businessId != 0) {
            new GetBusinessProfileInfo(ActivityRegisterEditBusiness.this, businessId, ActivityRegisterEditBusiness.this).execute();
            bundle.putBoolean(Params.IS_EDITTING, true);
            progressDialog.show();
        } else
            bundle.putBoolean(Params.IS_EDITTING, false);

        fragmentBaseInfo.setArguments(bundle);
        fragmentContactInfo.setArguments(bundle);
        fragmentLocationInfo.setArguments(bundle);

        fm = getFragmentManager();
        ft = fm.beginTransaction();
        ft.add(R.id.fragmentContainer, fragmentBaseInfo);

        //if the user is editing the business profile, he should wait to get GetBusinessProfileInfo's result
        //then goes to the fragment
        if (businessId == 0)
            ft.commit();

    }

    @Override
    public void onBackPressed() {
        switch (fragmentCurrent) {
            case BASE_INFO:
                fragmentCurrent = Fragments.BASE_INFO;
                finish();
                break;
            case CONTACT_INFO:
                fragmentCurrent = Fragments.BASE_INFO;
                ft = fm.beginTransaction();
                ft.replace(R.id.fragmentContainer, fragmentBaseInfo);
                ft.commit();
                llIndicatorContact.setVisibility(View.GONE);
                llIndicatorBase.setVisibility(View.VISIBLE);
                break;
            case LOCATION_INFO:
                fragmentCurrent = Fragments.CONTACT_INFO;
                ft = fm.beginTransaction();
                ft.replace(R.id.fragmentContainer, fragmentContactInfo);
                ft.commit();
                menuItemNext.setIcon(R.drawable.ic_arrow_next_white_24dp);
                ;
                llIndicatorLocation.setVisibility(View.GONE);
                llIndicatorContact.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_next_button, menu);
        this.menuItemNext = menu.findItem(R.id.action_next);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        } else if (item.getItemId() == R.id.action_next) {
            switch (fragmentCurrent) {
                case BASE_INFO:
                    if (fragmentBaseInfo.isVerified()) {
                        ft = fm.beginTransaction();
                        ft.replace(R.id.fragmentContainer, fragmentContactInfo);
                        ft.commit();
                        fragmentCurrent = Fragments.CONTACT_INFO;
                        llIndicatorBase.setVisibility(View.GONE);
                        llIndicatorContact.setVisibility(View.VISIBLE);
                    }
                    break;
                case CONTACT_INFO:
                    if (fragmentContactInfo.isVerified()) {
                        ft = fm.beginTransaction();
                        ft.replace(R.id.fragmentContainer, fragmentLocationInfo);
                        ft.commit();
                        fragmentCurrent = Fragments.LOCATION_INFO;
                        menuItemNext.setIcon(R.drawable.ic_check_white_24dp);
                        //menuItemNext.setVisible(false);
                        llIndicatorContact.setVisibility(View.GONE);
                        llIndicatorLocation.setVisibility(View.VISIBLE);
                    }
                    break;
                case LOCATION_INFO:
                    if (fragmentLocationInfo.isVerified()) {
                        progressDialog.show();
                        if (businessPictureString != null)
                            ((MyApplication) getApplication()).business.profilePicture = businessPictureString;

                        //if user doesn't choose the location


                        if (businessId != 0)
                            //it is editing
                            new UpdateBusinessProfileInfo(ActivityRegisterEditBusiness.this, ((MyApplication) getApplication()).business, ActivityRegisterEditBusiness.this).execute();
                        else
                            new RegisterBusiness(ActivityRegisterEditBusiness.this, ((MyApplication) getApplication()).business, ActivityRegisterEditBusiness.this).execute();

                    }
                    break;
            }
            return true;
        } else return super.onOptionsItemSelected(item);
    }

    @Override
    public void getResult(Object result) {
        progressDialog.dismiss();
        if (result instanceof Integer) {
            //RegisterBusiness' result
            //result is the registered business' id
            ((MyApplication) getApplication()).business.id = (Integer) result;
            Intent i = getIntent();
            setResult(RESULT_OK, i);
            finish();


        } else if (result instanceof Business) {
            //GetBusinessProfileInfo's result
            ((MyApplication) getApplication()).business = (Business) result;
            ((MyApplication) getApplication()).business.businessIdentifier = businessIdentifier;
            ft.commit();
        } else if (result instanceof ResultStatus) {
            Intent i = getIntent();
            i.putExtra(Params.PROFILE_PICTURE,((MyApplication) getApplication()).business.profilePicture);
            setResult(RESULT_OK, i);
            finish();
        }
    }

    @Override
    public void getError(Integer errorCode) {
        progressDialog.dismiss();
        new DialogMessage(ActivityRegisterEditBusiness.this, ServerAnswer.getError(ActivityRegisterEditBusiness.this, errorCode)).show();
    }

    @Override
    public void notifyCallForTakePicture() {
        new PopupCameraGallery(ActivityRegisterEditBusiness.this).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == ActivityCamera.CAPTURE_PHOTO) {
                filePath = data.getStringExtra(ActivityCamera.FILE_PATH);
                displayCropedImage(filePath);
            } else if (requestCode == ActivityGallery.CAPTURE_GALLERY) {
                filePath = data.getStringExtra(ActivityGallery.FILE_PATH);
                displayCropedImage(filePath);
            }

        }

    }

    private void displayCropedImage(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            Bitmap myBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
            try {
                fragmentBaseInfo.setPicture(myBitmap);
                businessPictureString = Image_M.getBase64String(filePath);
            } catch (Exception e) {
                String s = e.getMessage();
            }
        }
    }

}