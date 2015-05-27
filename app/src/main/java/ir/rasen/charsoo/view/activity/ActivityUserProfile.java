package ir.rasen.charsoo.view.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import java.io.File;
import java.util.ArrayList;

import ir.rasen.charsoo.R;
import ir.rasen.charsoo.controller.helper.Image_M;
import ir.rasen.charsoo.controller.helper.LoginInfo;
import ir.rasen.charsoo.controller.helper.Params;
import ir.rasen.charsoo.controller.helper.PersianDate;
import ir.rasen.charsoo.controller.helper.ResultStatus;
import ir.rasen.charsoo.controller.helper.ServerAnswer;
import ir.rasen.charsoo.controller.helper.Sex;
import ir.rasen.charsoo.controller.helper.Validation;
import ir.rasen.charsoo.controller.object.User;
import ir.rasen.charsoo.model.DownloadCoverImage;
import ir.rasen.charsoo.model.user.GetUserProfileInfo;
import ir.rasen.charsoo.model.user.UpdateUserProfile;
import ir.rasen.charsoo.view.dialog.DialogChangePassword;
import ir.rasen.charsoo.view.dialog.DialogClearSearchHistoryConfirmation;
import ir.rasen.charsoo.view.dialog.DialogMessage;
import ir.rasen.charsoo.view.dialog.PopupSelectCameraGallery;
import ir.rasen.charsoo.view.interface_m.IChangePassword;
import ir.rasen.charsoo.view.interface_m.IWebserviceResponse;
import ir.rasen.charsoo.view.widget_customized.EditTextFont;
import ir.rasen.charsoo.view.widget_customized.charsoo_activity.CharsooActivity;


public class ActivityUserProfile extends CharsooActivity implements View.OnClickListener, IWebserviceResponse, IChangePassword {


    ProgressDialog progressDialog;
    ImageView imageViewCover;
    EditTextFont editTextAboutMe, editTextEmail, editTextDay, editTextMonth, editTextYear, editTextName, editTextIdentifier;
    Spinner spinnerSex;
    String filePath, userPictureString;
    User user;
    int coverPictureHieght;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        setTitle(getString(R.string.user_profile));

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getResources().getString(R.string.please_wait));

        (findViewById(R.id.btn_change_password)).setOnClickListener(this);
        (findViewById(R.id.btn_clear_search_history)).setOnClickListener(this);
        (findViewById(R.id.imageView_back)).setOnClickListener(this);
        (findViewById(R.id.imageView_camera)).setOnClickListener(this);
        (findViewById(R.id.btn_user_profile_submit)).setOnClickListener(this);

        imageViewCover = (ImageView) findViewById(R.id.imageView_cover);

        editTextAboutMe = (EditTextFont) findViewById(R.id.editText_about_me);
        editTextDay = (EditTextFont) findViewById(R.id.edt_day);
        editTextEmail = (EditTextFont) findViewById(R.id.edt_email);
        editTextMonth = (EditTextFont) findViewById(R.id.edt_month);
        editTextYear = (EditTextFont) findViewById(R.id.edt_year);

        editTextIdentifier = (EditTextFont) findViewById(R.id.editText_identifier);
        editTextName = (EditTextFont) findViewById(R.id.editText_name);

        spinnerSex = (Spinner) findViewById(R.id.spinner_sex);
        ArrayList<String> sexList = new ArrayList<>();
        sexList.add(getString(R.string.sex));
        sexList.add(getString(R.string.sex_male));
        sexList.add(getString(R.string.sex_female));

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                R.layout.layout_spinner_back, sexList);
        dataAdapter.setDropDownViewResource(R.layout.layout_spinner_back_drop_down);
        spinnerSex.setAdapter(dataAdapter);

        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (screenWidth / 3) * 2);
        imageViewCover.setLayoutParams(params);

        progressDialog.show();
        new GetUserProfileInfo(this, LoginInfo.getUserId(this), this).execute();


    }

    @Override
    public void getResult(Object result) {
        progressDialog.dismiss();
        if (result instanceof User) {
            //GetUserProfileInfo result
            user = (User) result;
            DownloadCoverImage downloadCoverImage = new DownloadCoverImage(this);
            downloadCoverImage.download(user.profilePictureId, imageViewCover, Image_M.ImageType.USER);

            //initial view
            editTextName.setText(user.name);
            editTextIdentifier.setText(user.userIdentifier);
            if (!user.aboutMe.equals("null"))
                editTextAboutMe.setText(user.aboutMe);
            if (user.sex == Sex.MALE)
                spinnerSex.setSelection(1);
            else if (user.sex == Sex.FEMALE)
                spinnerSex.setSelection(2);
            editTextEmail.setText(user.email);
            if (!user.birthDate.equals("") && !user.birthDate.equals("null")) {
                if (!PersianDate.getDayFromBirthDateString(user.birthDate).equals("null"))
                    editTextDay.setText(PersianDate.getDayFromBirthDateString(user.birthDate));
                if (!PersianDate.getMonthFromBirthDateString(user.birthDate).equals("null"))
                    editTextMonth.setText(PersianDate.getMonthFromBirthDateString(user.birthDate));
                if (!PersianDate.getYearFromBirthDateString(user.birthDate).equals("null"))
                    editTextYear.setText(PersianDate.getYearFromBirthDateString(user.birthDate));
            }
        } else if (result instanceof ResultStatus) {
            //UpdateUserProfileInfo result
            new DialogMessage(ActivityUserProfile.this, getString(R.string.dialog_success)).show();
            if (userPictureString != null) {
                Intent intent = new Intent(Params.UPDATE_USER_PROFILE_PCITURE);
                intent.putExtra(Params.USER_PICUTE,userPictureString);
                LocalBroadcastManager.getInstance(ActivityUserProfile.this).sendBroadcast(intent);
            }
            onBackPressed();


        }
    }

    @Override
    public void getError(Integer errorCode) {
        progressDialog.dismiss();
        new DialogMessage(ActivityUserProfile.this, ServerAnswer.getError(ActivityUserProfile.this, errorCode)).show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imageView_back:
                finish();
                break;
            case R.id.imageView_camera:
                new PopupSelectCameraGallery(ActivityUserProfile.this).show();
                break;
            case R.id.btn_change_password:
                new DialogChangePassword(ActivityUserProfile.this, ActivityUserProfile.this).show();
                break;
            case R.id.btn_clear_search_history:
                new DialogClearSearchHistoryConfirmation(ActivityUserProfile.this).show();
                break;
            case R.id.btn_user_profile_submit:
                if (getUserInfo()) {
                    progressDialog.show();
                    new UpdateUserProfile(ActivityUserProfile.this, user, ActivityUserProfile.this).execute();
                }
                break;
        }
    }

    private boolean getUserInfo() {
        if (!Validation.validateName(ActivityUserProfile.this, editTextName.getText().toString()).isValid()) {
            editTextName.setError(Validation.getErrorMessage());
            return false;
        }
        if (!Validation.validateAboutMe(ActivityUserProfile.this, editTextAboutMe.getText().toString()).isValid()) {
            editTextAboutMe.setError(Validation.getErrorMessage());
            return false;
        }
        if (!Validation.validateEmail(ActivityUserProfile.this, editTextEmail.getText().toString()).isValid()) {
            editTextEmail.setError(Validation.getErrorMessage());
            return false;
        }
        if (!Validation.validateDay(ActivityUserProfile.this, editTextDay.getText().toString()).isValid()) {
            editTextDay.setError(Validation.getErrorMessage());
            return false;
        }
        if (!Validation.validateMonth(ActivityUserProfile.this, editTextMonth.getText().toString()).isValid()) {
            editTextMonth.setError(Validation.getErrorMessage());
            return false;
        }
        if (!Validation.validateYear(ActivityUserProfile.this, editTextYear.getText().toString()).isValid()) {
            editTextYear.setError(Validation.getErrorMessage());
            return false;
        }
        if (spinnerSex.getSelectedItemPosition() == 0) {
            new DialogMessage(ActivityUserProfile.this, getString(R.string.choose_user_sex)).show();
            return false;
        }
        user.name = editTextName.getText().toString();
        user.aboutMe = editTextAboutMe.getText().toString();
        switch (spinnerSex.getSelectedItemPosition()) {
            case 1:
                user.sex = Sex.MALE;
                break;
            case 2:
                user.sex = Sex.FEMALE;
                break;
        }
        user.email = editTextEmail.getText().toString();


        user.birthDate = PersianDate.getBirthDateString(editTextDay.getText().toString(), editTextMonth.getText().toString(), editTextYear.getText().toString());
        if (userPictureString != null)
            user.profilePicture = userPictureString;
        else
            user.profilePicture = "";

        return true;

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
            //crop middle part of picture
            int width = getResources().getDisplayMetrics().widthPixels;
            //crop middle part of picture
            int bitmapWidth = myBitmap.getWidth();
            if (width > bitmapWidth)
                width = bitmapWidth;
            try {
                Bitmap croppedBitmap = Bitmap.createBitmap(myBitmap, 0, width / 6, width, (width / 3) * 2);
                imageViewCover.setImageBitmap(croppedBitmap);
                userPictureString = Image_M.getBase64String(filePath);
            } catch (Exception e) {
                String s = e.getMessage();
            }
        }
    }

    @Override
    public void notifyNewPassword(String newPassword) {
        user.password = newPassword;
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
