package ir.rasen.charsoo.view.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import ir.rasen.charsoo.R;
import ir.rasen.charsoo.controller.helper.Image_M;
import ir.rasen.charsoo.controller.helper.Params;
import ir.rasen.charsoo.controller.helper.ServerAnswer;
import ir.rasen.charsoo.controller.image_loader.SimpleLoader;
import ir.rasen.charsoo.controller.object.Business;
import ir.rasen.charsoo.controller.object.MyApplication;
import ir.rasen.charsoo.model.business.GetBusinessContactInfo;
import ir.rasen.charsoo.view.dialog.DialogMessage;
import ir.rasen.charsoo.view.interface_m.IWebserviceResponse;
import ir.rasen.charsoo.view.widgets.TextViewFont;
import ir.rasen.charsoo.view.widgets.charsoo_activity.CharsooActivity;


public class ActivityBusinessContactInfo extends CharsooActivity implements IWebserviceResponse {


    ProgressDialog progressDialog;
    Business business;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_contact_info);
        setTitle(getResources().getString(R.string.call_info));

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getResources().getString(R.string.please_wait));

        progressDialog.show();
        MyApplication myApplication = (MyApplication) getApplication();
        new GetBusinessContactInfo(this, myApplication.business.id, this).execute();


        SimpleLoader simpleLoader = new SimpleLoader(this);
        simpleLoader.loadImage(myApplication.business.profilePictureId, Image_M.LARGE, Image_M.ImageType.BUSINESS, (ImageView) findViewById(R.id.imageView_cover));

        ((TextViewFont) findViewById(R.id.editText_name)).setText(myApplication.business.name);
        ((TextViewFont) findViewById(R.id.editText_identifier)).setText(myApplication.business.businessIdentifier);

        findViewById(R.id.rl_map).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityBusinessContactInfo.this, ActivityMapDisplay.class);
                intent.putExtra(Params.LATITUDE, Double.valueOf(business.location_m.getLatitude()));
                intent.putExtra(Params.LONGITUDE, Double.valueOf(business.location_m.getLongitude()));
                startActivity(intent);
            }
        });
        findViewById(R.id.imageView_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
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
        if (!(result instanceof Business))
            return;
        business = (Business) result;
        ((TextViewFont) findViewById(R.id.edt_description)).setText(business.description);
        ((TextViewFont) findViewById(R.id.edt_phone)).setText(business.phone);
        ((TextViewFont) findViewById(R.id.edt_mobile)).setText(business.mobile);
        ((TextViewFont) findViewById(R.id.edt_address)).setText(business.state+","+business.city+","+business.address);
        String open = business.workTime.getTimeWorkOpenWebservice();
        String close = business.workTime.getTimeWorkCloseWebservice();
        String workTime = getString(R.string.from) + " " + open + " " + getString(R.string.until) + " " + close;
        ((TextViewFont) findViewById(R.id.edt_time)).setText(workTime);
        ((TextViewFont) findViewById(R.id.edt_wesite)).setText(business.webSite);
        ((TextViewFont) findViewById(R.id.edt_email)).setText(business.email);

    }

    @Override
    public void getError(Integer errorCode,String callerStringID) {
        progressDialog.dismiss();
        new DialogMessage(ActivityBusinessContactInfo.this, ServerAnswer.getError(ActivityBusinessContactInfo.this, errorCode,callerStringID+">"+this.getLocalClassName())).show();
    }
}
