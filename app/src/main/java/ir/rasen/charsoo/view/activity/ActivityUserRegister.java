package ir.rasen.charsoo.view.activity;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.io.File;
import java.util.ArrayList;

import ir.rasen.charsoo.R;
import ir.rasen.charsoo.controller.helper.GetInstalledApps;
import ir.rasen.charsoo.controller.helper.Image_M;
import ir.rasen.charsoo.controller.helper.ServerAnswer;
import ir.rasen.charsoo.controller.object.ContactEntry;
import ir.rasen.charsoo.controller.object.PackageInfoCustom;
import ir.rasen.charsoo.controller.object.User;
import ir.rasen.charsoo.model.GetContactData;
import ir.rasen.charsoo.model.user.RegisterUser;
import ir.rasen.charsoo.view.dialog.DialogMessage;
import ir.rasen.charsoo.view.fragment.FragmentUserRegisterOfferFriend;
import ir.rasen.charsoo.view.fragment.FragmentUserRegisterPageOne;
import ir.rasen.charsoo.view.fragment.FragmentUserRegisterPageTwo;
import ir.rasen.charsoo.view.interface_m.IGetContactListener;
import ir.rasen.charsoo.view.interface_m.IGetInstalledAppsListener;
import ir.rasen.charsoo.view.interface_m.IWebserviceResponse;
import ir.rasen.charsoo.view.widgets.WaitDialog;
import ir.rasen.charsoo.view.widgets.charsoo_activity.CharsooActivity;

public class ActivityUserRegister extends CharsooActivity implements IWebserviceResponse,IGetContactListener,IGetInstalledAppsListener {


    public static final String FIRST_PAGE="FirstPage";
    public static final String SECOND_PAGE="SecondPage";
    public static final String THIRD_PAGE_OFFER_FRIEND="ThirdPage";
    FragmentTransaction ft;
    FragmentUserRegisterPageOne fragOne;
    FragmentUserRegisterPageTwo fragTwo;
    FragmentUserRegisterOfferFriend fragThree_OfferFriends;
    String userPictureString,userFullName,userStringId,userEmail,userPassword,userPhoneNumber;
    String filePath,currentFragment;
    MenuItem menuItemNext;
    Bitmap tempUserPicture;
    WaitDialog progressDialog;
    public int userIntId;
    public ArrayList<ContactEntry> noneCharsooEmailContactsList, noneCharsooPhoneNumberContactsList, charsooContactsList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register_new);
        setTitle(getString(R.string.str_RegisterInCharsoo));
//        noneCharsooEmailContactsList=new ArrayList<>();
//        noneCharsooPhoneNumberContactsList=new ArrayList<>();
//        charsooContactsList=new ArrayList<>()
        fragOne=new FragmentUserRegisterPageOne();
        fragTwo=new FragmentUserRegisterPageTwo();
        fragThree_OfferFriends =new FragmentUserRegisterOfferFriend();
        ft=getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragmentContainer,fragOne);
        currentFragment=FIRST_PAGE;
        ft.commit();
        progressDialog = new WaitDialog(this);
        progressDialog.setMessage(getResources().getString(R.string.please_wait));
        new GetContactData(this,ActivityUserRegister.this).execute();
        new GetInstalledApps(this,ActivityUserRegister.this).execute();

//            getContactList();
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

                ft=getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.fragmentContainer,fragOne);

                currentFragment=FIRST_PAGE;
                ft.commit();

                menuItemNext.setIcon(R.drawable.ic_arrow_next_white_24dp);

                break;
            case THIRD_PAGE_OFFER_FRIEND:
                if (fragThree_OfferFriends.isEmailListVisible())
                    fragThree_OfferFriends.hideEmailList();
                else {
                    Intent intent = new Intent(this, ActivityLogin.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    this.startActivity(intent);
                }
                break;
            default:
                finish();
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
//                    switchToSecondPage();
                    ft=getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.fragmentContainer, fragThree_OfferFriends);
                    currentFragment=THIRD_PAGE_OFFER_FRIEND;
                    ft.commit();
                    break;
                case SECOND_PAGE:
                    //register User
                    doRegisterUser();
                    break;
                case THIRD_PAGE_OFFER_FRIEND:
                    Intent intent = new Intent(ActivityUserRegister.this,ActivityMain.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    break;

            }
            return true;
        } else return super.onOptionsItemSelected(item);
    }

    @Override
    public void getResult(Object result) {
        progressDialog.dismiss();
        ft=getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragmentContainer, fragThree_OfferFriends);
        currentFragment=THIRD_PAGE_OFFER_FRIEND;
        ft.commit();
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
            ft=getSupportFragmentManager().beginTransaction();
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

    @Override
    public void getContacts(ArrayList<ContactEntry> charsooContacts, ArrayList<ContactEntry> noneCharsooPhoneContacts, ArrayList<ContactEntry> noneCharsooEmailContacts) {
        fragThree_OfferFriends.setContactLists(charsooContacts,noneCharsooPhoneContacts,noneCharsooEmailContacts);
    }

    @Override
    public void setAppResults(ArrayList<PackageInfoCustom> appList) {
        fragThree_OfferFriends.setApplicationList(appList);
    }


//    public ArrayList<ArrayList<ContactEntry>> getUserContacts(){
//
//    }

}
