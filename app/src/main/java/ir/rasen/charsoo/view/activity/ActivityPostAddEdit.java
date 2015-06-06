package ir.rasen.charsoo.view.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import java.io.File;
import java.text.DecimalFormat;

import ir.rasen.charsoo.R;
import ir.rasen.charsoo.controller.helper.Image_M;
import ir.rasen.charsoo.controller.helper.LoginInfo;
import ir.rasen.charsoo.controller.helper.Params;
import ir.rasen.charsoo.controller.helper.ServerAnswer;
import ir.rasen.charsoo.controller.helper.TextProcessor;
import ir.rasen.charsoo.controller.helper.Validation;
import ir.rasen.charsoo.controller.helper.WebservicesHandler;
import ir.rasen.charsoo.controller.image_loader.SimpleLoader;
import ir.rasen.charsoo.controller.object.MyApplication;
import ir.rasen.charsoo.controller.object.Post;
import ir.rasen.charsoo.model.post.AddPost;
import ir.rasen.charsoo.model.post.GetPost;
import ir.rasen.charsoo.model.post.UpdatePost;
import ir.rasen.charsoo.view.dialog.DialogMessage;
import ir.rasen.charsoo.view.dialog.PopupSelectCameraGallery;
import ir.rasen.charsoo.view.interface_m.IWebserviceResponse;
import ir.rasen.charsoo.view.widget_customized.EditTextFont;
import ir.rasen.charsoo.view.widget_customized.EditTextFontPasteDisabled;
import ir.rasen.charsoo.view.widget_customized.charsoo_activity.CharsooActivity;


public class ActivityPostAddEdit extends CharsooActivity implements View.OnClickListener, IWebserviceResponse {


    EditTextFont editTextTitle, editTextDescription, editTextCode, editTextHashtags;
    EditTextFontPasteDisabled editTextPrice;
    ProgressDialog progressDialog;
    ImageView imageViewPostPicture;
    String filePath, postPictureString;
    int businessId;
    int postId = 0;
    int counter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_add);


        businessId = getIntent().getExtras().getInt(Params.BUSINESS_ID_STRING);
        try {
            //if there is postId it means the user is editing the post
            postId = getIntent().getExtras().getInt(Params.POST_ID_INT);
        } catch (Exception e) {

        }
        postPictureString = "";

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getResources().getString(R.string.please_wait));

        imageViewPostPicture = (ImageView) findViewById(R.id.imageView_user_picture);
        imageViewPostPicture.setOnClickListener(this);
        (findViewById(R.id.btn_submit)).setOnClickListener(this);

        editTextTitle = (EditTextFont) findViewById(R.id.edt_title);
        editTextDescription = (EditTextFont) findViewById(R.id.edt_description);
        editTextHashtags = (EditTextFont) findViewById(R.id.edt_hashtags);
        editTextPrice = (EditTextFontPasteDisabled) findViewById(R.id.edt_price);
        editTextCode = (EditTextFont) findViewById(R.id.edt_code);

        if (postId != 0) {
            ((MyApplication) getApplication()).setCurrentWebservice(WebservicesHandler.Webservices.GET_POST);
            progressDialog.show();
            new GetPost(ActivityPostAddEdit.this, LoginInfo.getUserId(ActivityPostAddEdit.this), businessId, postId, Post.GetPostType.BUSINESS, ActivityPostAddEdit.this).execute();
            imageViewPostPicture.setEnabled(false);
            setTitle(getResources().getString(R.string.edit_product));
        } else
            setTitle(getString(R.string.new_product));

        editTextPrice.addTextChangedListener(new TextWatcher() {
            String oldText;

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                oldText = charSequence.toString();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                if (charSequence.toString().equals(oldText))
                    return;
                /*String price = editTextPrice.getText().toString();
                int commaCount = price.length() - price.replace(",", "").length();
                if ((price.length() - counter) % 3 == 0) {
                    editTextPrice.setText(price + ",");
                    counter++;
                }*/

                String price = editTextPrice.getText().toString().replace(",", "");
                if (price.equals(""))
                    return;
                double amount = Double.parseDouble(price);
                DecimalFormat formatter = new DecimalFormat("#,###");
                price = formatter.format(amount);
                editTextPrice.setText(price);
                editTextPrice.setSelection(price.length());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        //process hashtags
        editTextHashtags.addTextChangedListener(new TextWatcher() {
            String oldText;

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                oldText = charSequence.toString();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                if (charSequence.toString().equals(oldText))
                    return;
                TextProcessor.processEdtHashtags(editTextHashtags.getText().toString(), editTextHashtags, ActivityPostAddEdit.this);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == ActivityCamera.CAPTURE_PHOTO) {
                filePath = data.getStringExtra(ActivityCamera.FILE_PATH);
                displayCroppedImage(filePath);
            } else if (requestCode == ActivityGallery.CAPTURE_GALLERY) {
                filePath = data.getStringExtra(ActivityGallery.FILE_PATH);
                displayCroppedImage(filePath);
            }
        }

    }

    private void displayCroppedImage(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            Bitmap myBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
            try {
                imageViewPostPicture.setImageBitmap(myBitmap);
                postPictureString = Image_M.getBase64String(filePath);
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
            case R.id.btn_submit:
                submit();
                break;
            case R.id.imageView_user_picture:
                new PopupSelectCameraGallery(ActivityPostAddEdit.this).show();
                break;
        }
    }

    private void submit() {
        if (!Validation.validateTitle(ActivityPostAddEdit.this, editTextTitle.getText().toString()).isValid()) {
            editTextTitle.setError(Validation.getErrorMessage());
            return;
        }
        if (postId == 0 && postPictureString.equals("")) {
            new DialogMessage(ActivityPostAddEdit.this, getString(R.string.choose_post_picture)).show();
            return;
        }

        Post post = new Post();
        post.businessID = businessId;
        post.title = editTextTitle.getText().toString();
        post.picture = postPictureString;
        post.description = editTextDescription.getText().toString();
        post.price = editTextPrice.getText().toString();
        post.code = editTextCode.getText().toString();
        post.hashtagList = TextProcessor.getHashtags(editTextHashtags.getText().toString());

        progressDialog.show();
        if (postId != 0) {
            //the user is updating the post
            post.id = postId;
            ((MyApplication) getApplication()).setCurrentWebservice(WebservicesHandler.Webservices.UPDATE_POST);
            new UpdatePost(ActivityPostAddEdit.this, post, ActivityPostAddEdit.this).execute();
        } else {
            //the user is adding new post
            ((MyApplication) getApplication()).setCurrentWebservice(WebservicesHandler.Webservices.ADD_POST);
            new AddPost(ActivityPostAddEdit.this, post, ActivityPostAddEdit.this).execute();
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
        if (result instanceof Post) {
            WebservicesHandler.Webservices currentWebservice = ((MyApplication) getApplication()).getCurrentWebservice();
            if (currentWebservice == WebservicesHandler.Webservices.GET_POST) {
                Post post = (Post) result;
                editTextTitle.setText(post.title);
                //download and display the post picture
                SimpleLoader simpleLoader = new SimpleLoader(ActivityPostAddEdit.this);
                simpleLoader.loadImage(post.pictureId, Image_M.MEDIUM, Image_M.ImageType.POST, imageViewPostPicture);

                editTextDescription.setText(post.description);
                if (post.hashtagList.size() != 0) {
                    String hashtags = "";
                    for (String hashtag : post.hashtagList) {
                        if (!hashtag.equals(""))
                            hashtags += "#" + hashtag;
                    }
                    editTextHashtags.setText(hashtags + " ");
                }

                editTextPrice.setText(post.price);
                editTextCode.setText(post.code);
            } else if (currentWebservice == WebservicesHandler.Webservices.ADD_POST || currentWebservice == WebservicesHandler.Webservices.UPDATE_POST) {
                //it is AddPost or UpdatePost. The postAdapter which calls this activity needs to be updated
                ((MyApplication) getApplication()).post = new Post();
                ((MyApplication) getApplication()).post = (Post) result;
                Intent i = getIntent();
                setResult(RESULT_OK, i);
                finish();
            }
        }

    }

    @Override
    public void getError(Integer errorCode, String callerStringID) {
        progressDialog.dismiss();
        new DialogMessage(ActivityPostAddEdit.this, ServerAnswer.getError(ActivityPostAddEdit.this, errorCode, callerStringID + ">" + this.getLocalClassName())).show();
    }
}
