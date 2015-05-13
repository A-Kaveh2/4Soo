package ir.rasen.charsoo.view.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import java.io.File;

import ir.rasen.charsoo.R;
import ir.rasen.charsoo.classes.User;
import ir.rasen.charsoo.dialog.DialogMessage;
import ir.rasen.charsoo.dialog.PopupSelectCameraGallery;
import ir.rasen.charsoo.helper.ActionBar_M;
import ir.rasen.charsoo.helper.Image_M;
import ir.rasen.charsoo.helper.ServerAnswer;
import ir.rasen.charsoo.helper.Validation;
import ir.rasen.charsoo.interfaces.IWebserviceResponse;
import ir.rasen.charsoo.ui.EditTextFont;
import ir.rasen.charsoo.ui.ImageViewCircle;
import ir.rasen.charsoo.webservices.user.RegisterUser;


public class ActivityUserRegister extends ActionBarActivity implements View.OnClickListener, IWebserviceResponse {


    EditTextFont editTextName, editTextEmail, editTextPassword, editTextRepeatPassword;
    ProgressDialog progressDialog;
    EditText editTextUserIdentifier;
    ImageViewCircle imageViewUserPicture;
    String filePath,userPictureString;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_registerr);
        ActionBar_M.setActionBar(getSupportActionBar(), this, getResources().getString(R.string.register));

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getResources().getString(R.string.please_wait));

        imageViewUserPicture = (ImageViewCircle) findViewById(R.id.imageView_user_picture);
        imageViewUserPicture.setOnClickListener(this);
        (findViewById(R.id.btn_register)).setOnClickListener(this);

        editTextUserIdentifier = (EditText) findViewById(R.id.editText_identifier);
        editTextName = (EditTextFont) findViewById(R.id.editText_name);
        editTextEmail = (EditTextFont) findViewById(R.id.editText_email);
        editTextPassword = (EditTextFont) findViewById(R.id.editText_password);
        editTextRepeatPassword = (EditTextFont) findViewById(R.id.editText_password_repeat);
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
                imageViewUserPicture.setImageBitmap(myBitmap);
                userPictureString = Image_M.getBase64String(filePath);
            } catch (Exception e) {
                String s = e.getMessage();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        /*MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_next_button, menu);*/
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_register:
                if (!Validation.validateIdentifier(ActivityUserRegister.this, editTextUserIdentifier.getText().toString()).isValid()) {
                    editTextUserIdentifier.setError(Validation.getErrorMessage());
                    return;
                }
                if (!Validation.validateName(ActivityUserRegister.this, editTextName.getText().toString()).isValid()) {
                    editTextName.setError(Validation.getErrorMessage());
                    return;
                }
                if (!Validation.validateEmail(ActivityUserRegister.this, editTextEmail.getText().toString()).isValid()) {
                    editTextEmail.setError(Validation.getErrorMessage());
                    return;
                }
                if (!Validation.validatePassword(ActivityUserRegister.this, editTextPassword.getText().toString()).isValid()) {
                    editTextPassword.setError(Validation.getErrorMessage());
                    return;
                }
                if (!Validation.validateRepeatPassword(ActivityUserRegister.this, editTextPassword.getText().toString(), editTextRepeatPassword.getText().toString()).isValid()) {
                    editTextRepeatPassword.setError(Validation.getErrorMessage());
                    return;
                }
                if(userPictureString == null){
                    new DialogMessage(ActivityUserRegister.this,getString(R.string.choose_user_picture)).show();
                    return;
                }
                User user = new User();
                user.userIdentifier = editTextUserIdentifier.getText().toString();
                user.name= editTextName.getText().toString();
                user.email = editTextEmail.getText().toString();
                user.password = editTextPassword.getText().toString();
                user.profilePicture = userPictureString;

                progressDialog.show();
                new RegisterUser(ActivityUserRegister.this,user,ActivityUserRegister.this).execute();
                break;
            case R.id.imageView_user_picture:
                 new PopupSelectCameraGallery(ActivityUserRegister.this).show();
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        } else
            return super.onOptionsItemSelected(item);
    }


    @Override
    public void getResult(Object result) {
        progressDialog.dismiss();
        Intent intent = new Intent(ActivityUserRegister.this,ActivityMain.class);
        startActivity(intent);
    }

    @Override
    public void getError(Integer errorCode) {
        progressDialog.dismiss();
        new DialogMessage(ActivityUserRegister.this, ServerAnswer.getError(ActivityUserRegister.this, errorCode)).show();
    }
}
