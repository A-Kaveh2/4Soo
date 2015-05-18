package ir.rasen.charsoo.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import ir.rasen.charsoo.R;
import ir.rasen.charsoo.controller.helper.ActionBar_M;
import ir.rasen.charsoo.controller.helper.Params;
import ir.rasen.charsoo.controller.helper.ServerAnswer;
import ir.rasen.charsoo.controller.object.Business;
import ir.rasen.charsoo.controller.object.MyApplication;
import ir.rasen.charsoo.controller.object.User;
import ir.rasen.charsoo.view.adapter.AdapterUserBusinesses;
import ir.rasen.charsoo.view.dialog.DialogMessage;
import ir.rasen.charsoo.view.interface_m.IWebserviceResponse;


public class ActivityUserBusinesses extends ActionBarActivity implements IWebserviceResponse {

    ArrayList<Business> userBusinesses;
    AdapterUserBusinesses adapterUserBusinesses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_businesses);
        ActionBar_M.setActionBar(getSupportActionBar(), this, getResources().getString(R.string.businesses));
        userBusinesses = new ArrayList<>();
        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(ActivityUserBusinesses.this, ActivityBusiness.class);
                intent.putExtra(Params.BUSINESS_ID, userBusinesses.get(i).id);
                startActivity(intent);
            }
        });
        (findViewById(R.id.btn_creat_new_business)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityUserBusinesses.this, ActivityBusinessRegisterEdit.class);
                startActivityForResult(intent, Params.ACTION_REGISTER_BUSINESS);
            }
        });

        adapterUserBusinesses = new AdapterUserBusinesses(this, userBusinesses);
        listView.setAdapter(adapterUserBusinesses);

        //TODO:: SHOULD BE EXECUTED
        //new GetUserBusinesses(this,this).execute();

        //TODO:: TEMPORARY::
        ArrayList<User.UserBusinesses> userBusinesses = ((MyApplication) getApplication()).userBusinesses;
        for (User.UserBusinesses businessItem : userBusinesses) {
            Business business = new Business();
            business.id = businessItem.businessId;
            business.name = businessItem.businessIdentifier;
        }
        adapterUserBusinesses.notifyDataSetChanged();

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

    @Override
    public void getResult(Object result) {
        findViewById(R.id.progressBar).setVisibility(View.GONE);
        if (result instanceof ArrayList) {
            ArrayList<Business> temp = new ArrayList<>();
            temp.addAll(userBusinesses);
            temp.addAll((ArrayList<Business>) result);
            userBusinesses.clear();
            userBusinesses.addAll(temp);
            adapterUserBusinesses.notifyDataSetChanged();
        }
    }

    @Override
    public void getError(Integer errorCode) {
        new DialogMessage(this, ServerAnswer.getError(this, errorCode)).show();
    }

}
