package ir.rasen.charsoo.view.activity;

/**
 * Created by android on 3/28/2015.
 */


import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import java.io.File;
import java.util.Hashtable;

import ir.rasen.charsoo.R;
import ir.rasen.charsoo.controller.helper.Image_M;
import ir.rasen.charsoo.controller.helper.Params;
import ir.rasen.charsoo.controller.helper.ResultStatus;
import ir.rasen.charsoo.controller.helper.ServerAnswer;
import ir.rasen.charsoo.controller.object.Business;
import ir.rasen.charsoo.controller.object.MyApplication;
import ir.rasen.charsoo.model.business.GetBusinessProfileInfo;
import ir.rasen.charsoo.model.business.RegisterBusiness;
import ir.rasen.charsoo.model.business.UpdateBusinessProfileInfo;
import ir.rasen.charsoo.view.dialog.DialogDeleteBusinessConfirmation;
import ir.rasen.charsoo.view.dialog.DialogMessage;
import ir.rasen.charsoo.view.dialog.PopupSelectCameraGallery;
import ir.rasen.charsoo.view.fragment.FragmentBusinessEditBaseInfo;
import ir.rasen.charsoo.view.fragment.FragmentBusinessEditContactInfo;
import ir.rasen.charsoo.view.fragment.FragmentBusinessEditLocationInfo;
import ir.rasen.charsoo.view.fragment.FragmentBusinessRegisterPageOne;
import ir.rasen.charsoo.view.fragment.FragmentBusinessRegisterPageThree;
import ir.rasen.charsoo.view.fragment.FragmentBusinessRegisterPageTwo;
import ir.rasen.charsoo.view.interface_m.IChangeBusiness;
import ir.rasen.charsoo.view.interface_m.IGetCallForTakePicture;
import ir.rasen.charsoo.view.interface_m.IWebserviceResponse;
import ir.rasen.charsoo.view.widget_customized.TextViewFontActionBarTitle;
import ir.rasen.charsoo.view.widget_customized.charsoo_activity.CharsooActivity;

public class ActivityBusinessRegister extends CharsooActivity implements IWebserviceResponse, IGetCallForTakePicture, IChangeBusiness {

    public static final String FRAG_ONE="FragOne";
    public static final String FRAG_TWO="FragTwo";
    public static final String FRAG_THREE="FragThree";


    FragmentBusinessRegisterPageOne fragOne;
    FragmentBusinessRegisterPageTwo fragTwo;
    FragmentBusinessRegisterPageThree fragThree;
    ProgressDialog progressDialog;
    String filePath, businessPictureString;
    String currentFragment;

    Business unregisteredBusiness;
    FragmentManager fm;
    FragmentTransaction ft;
    MenuItem menuItemNext;

    boolean isCheckingIdAvailability;
    int businessId = 0;
    String businessIdentifier;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_register_new);

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.layout_action_bar_home, null);

        unregisteredBusiness=new Business();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getResources().getString(R.string.please_wait));


        fragOne=new FragmentBusinessRegisterPageOne();
        fragTwo=new FragmentBusinessRegisterPageTwo();
        fragThree=new FragmentBusinessRegisterPageThree();

        fm = getFragmentManager();
        ft = fm.beginTransaction();
        currentFragment=FRAG_ONE;
        ft.replace(R.id.fragmentContainer, fragOne);
        ft.commit();


        ((TextViewFontActionBarTitle) v.findViewById(R.id.textView_title)).setText(getString(R.string.register_business));


    }

    @Override
    public void onBackPressed() {
        switch (currentFragment) {
            case FRAG_ONE:
                finish();
                break;
            case FRAG_TWO:
                ft = fm.beginTransaction();
                currentFragment=FRAG_ONE;
                ft.replace(R.id.fragmentContainer, fragOne);
                ft.commit();

                break;
            case FRAG_THREE:
                ft = fm.beginTransaction();
                currentFragment=FRAG_TWO;
                ft.replace(R.id.fragmentContainer, fragTwo);
                ft.commit();
                fragTwo.setSpinners(this);
                menuItemNext.setIcon(R.drawable.ic_arrow_next_white_24dp);
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
            switch (currentFragment) {
                case FRAG_ONE:
                    // go fragment two
                    switchToSecondPage();
                    break;
                case FRAG_TWO:
                    // GO FRAGMNET TWO
                    switchToThirdPage();
                    break;
                case FRAG_THREE:
                    // DO REGISTER BUSINESS
                    // BEFORE REGISTER  CHECK FOR STRING ID AVAILABILITY
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
        }
        else if (result instanceof Boolean){
            if ((Boolean)result)
            {
                progressDialog.dismiss();
                isCheckingIdAvailability=false;
                if (currentFragment.equals(FRAG_ONE)){
                    ft= fm.beginTransaction();
                    currentFragment=FRAG_TWO;
                    ft.replace(R.id.fragmentContainer,fragTwo);
                    ft.commit();
                }
            }
            else{
                progressDialog.dismiss();
                isCheckingIdAvailability=false;
                if (!currentFragment.equals(FRAG_ONE)){
                    menuItemNext.setIcon(R.drawable.ic_arrow_next_white_24dp);
                }
                ft = fm.beginTransaction();
                currentFragment=FRAG_ONE;
                ft.replace(R.id.fragmentContainer, fragOne);
                ft.commit();
                fragOne.setErrorStringIdIsNotAvailable();

                // TODO: a well behavior on id not availability is like below
                /*if (currentFragment.equals(FRAG_ONE)){
                    fragOne.setErrorStringIdIsNotAvailable();
                }
                else{
                    // open pupup to get new String identifier
                }*/
            }
        }else if (result instanceof ResultStatus) {
            Intent i = getIntent();
            i.putExtra(Params.PROFILE_PICTURE, ((MyApplication) getApplication()).business.profilePicture);
            i.putExtra(Params.TYPE, Business.ChangeType.EDIT.name());
            setResult(RESULT_OK, i);
            finish();
        }
    }

    @Override
    public void getError(Integer errorCode,String callerStringID) {
        progressDialog.dismiss();
        new DialogMessage(ActivityBusinessRegister.this, ServerAnswer.getError(ActivityBusinessRegister.this, errorCode,callerStringID+">"+this.getLocalClassName())).show();
    }

    @Override
    public void notifyCallForTakePicture() {
        new PopupSelectCameraGallery(ActivityBusinessRegister.this).show();
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
                fragOne.setPicture(myBitmap);
                unregisteredBusiness.profilePicture = Image_M.getBase64String(filePath);
            } catch (Exception e) {
                String s = e.getMessage();
            }
        }
    }

    @Override
    public void notifyDeleteBusiness(int businessId) {
       /* Intent intent = new Intent(Params.DELETE_BUSINESS);
        intent.putExtra(Params.BUSINESS_ID_STRING, businessId);
        LocalBroadcastManager.getInstance(ActivityBusinessRegisterEdit.this).sendBroadcast(intent);*/

        Intent i = getIntent();
        i.putExtra(Params.BUSINESS_ID_STRING, businessId);
        i.putExtra(Params.TYPE,Business.ChangeType.DELETE.name());
        setResult(RESULT_OK, i);
        finish();
    }


    public void switchToSecondPage(){
        isCheckingIdAvailability=true;
        Hashtable<String,String> tempInputData=fragOne.getInputData();
        if (tempInputData!=null) {
            progressDialog.show();
            unregisteredBusiness.businessIdentifier=tempInputData.get(Params.STRING_IDENTIFIER);
            unregisteredBusiness.name=tempInputData.get(Params.FULL_NAME);
        }
    }

    public void switchToThirdPage(){
        if (fragTwo.isVerified()){

            unregisteredBusiness.category=fragTwo.selectedCategory.name;
            unregisteredBusiness.categoryID=fragTwo.selectedCategory.id;
            unregisteredBusiness.subcategory=fragTwo.selectedSubcategory.name;
            unregisteredBusiness.subCategoryID=fragTwo.selectedSubcategory.id;
            unregisteredBusiness.description=fragTwo.getInputDescription();
            unregisteredBusiness.hashtagList=fragTwo.getHashtagList();
            ft = fm.beginTransaction();
            currentFragment=FRAG_THREE;
            ft.replace(R.id.fragmentContainer, fragThree);
            ft.commit();
            menuItemNext.setIcon(R.drawable.ic_check_white_24dp);
        }

    }

}