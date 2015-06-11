package ir.rasen.charsoo.view.activity;


import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.io.File;

import ir.rasen.charsoo.R;
import ir.rasen.charsoo.controller.helper.Image_M;
import ir.rasen.charsoo.controller.helper.ServerAnswer;
import ir.rasen.charsoo.controller.object.User;
import ir.rasen.charsoo.model.user.RegisterUser;
import ir.rasen.charsoo.view.dialog.DialogMessage;
import ir.rasen.charsoo.view.fragment.FragmentUserRegisterPageOne;
import ir.rasen.charsoo.view.fragment.FragmentUserRegisterPageTwo;
import ir.rasen.charsoo.view.interface_m.IWebserviceResponse;
import ir.rasen.charsoo.view.widget_customized.charsoo_activity.CharsooActivity;

public class ActivityUserRegister extends CharsooActivity  implements IWebserviceResponse{


    public static final String FIRST_PAGE="FirstPage";
    public static final String SECOND_PAGE="SecondPage";
    FragmentTransaction ft;
    FragmentUserRegisterPageOne fragOne;
    FragmentUserRegisterPageTwo fragTwo;
    String userPictureString,userFullName,userStringId,userEmail,userPassword,userPhoneNumber;
    String filePath,currentFragment;
    MenuItem menuItemNext;
    Bitmap tempUserPicture;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register_new);
        setTitle(getString(R.string.str_RegisterInCharsoo));
        fragOne=new FragmentUserRegisterPageOne();
        fragTwo=new FragmentUserRegisterPageTwo();
        ft=getFragmentManager().beginTransaction();
        ft.replace(R.id.fragmentContainer,fragOne);
        currentFragment=FIRST_PAGE;
        ft.commit();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getResources().getString(R.string.please_wait));


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
            if (myBitmap!=null) {
                tempUserPicture = myBitmap;
                fragOne.setProfileImage(tempUserPicture);
                userPictureString = Image_M.getBase64String(filePath);
                fragOne.setUserImageString(userPictureString);
            }
        }
    }

    @Override
    public void onBackPressed() {
        switch (currentFragment) {
            case FIRST_PAGE:
                // back to login page
                Intent i= new Intent(this, ActivityLogin.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                this.startActivity(i);
                break;
            case SECOND_PAGE:
                //back to prev fragment

                ft=getFragmentManager().beginTransaction();
                ft.replace(R.id.fragmentContainer,fragOne);

                currentFragment=FIRST_PAGE;
                ft.commit();

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
                case FIRST_PAGE:
                    switchToSecondPage();
                    break;
                case SECOND_PAGE:
                    //register User
                    doRegisterUser();
                    break;
            }
            return true;
        } else return super.onOptionsItemSelected(item);
    }

    @Override
    public void getResult(Object result) {
        progressDialog.dismiss();
        Intent intent = new Intent(ActivityUserRegister.this,ActivityMain.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    public void getError(Integer errorCode,String callerStringID) {
        progressDialog.dismiss();
        new DialogMessage(ActivityUserRegister.this, ServerAnswer.getError(ActivityUserRegister.this, errorCode, callerStringID + ">" + this.getLocalClassName())).show();
    }

    public void onDoneButtonPressed(String callerStringId)
    {
        if (callerStringId.equals(FIRST_PAGE))
        {
            switchToSecondPage();
        }
        else{
            doRegisterUser();
        }

    }


    private void switchToSecondPage(){
        String[] tempStringOne=fragOne.getInputData();
        if (tempStringOne!=null) {
            userFullName=tempStringOne[0];
            userStringId=tempStringOne[1];
            ft=getFragmentManager().beginTransaction();
            ft.replace(R.id.fragmentContainer,fragTwo);
            currentFragment=SECOND_PAGE;
            ft.commit();
            menuItemNext.setIcon(R.drawable.ic_check_white_24dp);
        }
    }

    private void doRegisterUser(){
        String[] tempStringTwo=fragTwo.getInputData();
        if (tempStringTwo!=null){
            userEmail=tempStringTwo[0];
            userPassword=tempStringTwo[1];
            userPhoneNumber=tempStringTwo[2];
            //now register business
            User user = new User();
            user.userIdentifier = userStringId;
            user.name= userFullName;
            user.email = userEmail;
            user.password = userPassword;
            user.profilePicture = userPictureString;

            progressDialog.show();
            new RegisterUser(ActivityUserRegister.this,user,ActivityUserRegister.this).execute();
        }
    }
}
