package ir.rasen.charsoo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import ir.rasen.charsoo.classes.MyApplication;
import ir.rasen.charsoo.classes.User;
import ir.rasen.charsoo.helper.ActionBar_M;
import ir.rasen.charsoo.helper.Params;


public class ActivityUserBusinesses extends ActionBarActivity {

    ArrayList<User.UserBusinesses> userBusinesses;
    ArrayList<String> userBusinessesStr;
    ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_businesses);
        ActionBar_M.setActionBar(getSupportActionBar(), this, getResources().getString(R.string.businesses));
        ListView listView = (ListView) findViewById(R.id.listView);
        userBusinesses = ((MyApplication) getApplication()).userBusinesses;
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(ActivityUserBusinesses.this, ActivityBusiness.class);
                intent.putExtra(Params.BUSINESS_ID, userBusinesses.get(i).businessId);
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

        if (userBusinesses.size() == 0)
            return;

        userBusinessesStr = new ArrayList<>();
        for (User.UserBusinesses business : userBusinesses) {
            userBusinessesStr.add(business.businessIdentifier);
        }

        adapter = new ArrayAdapter<String>(ActivityUserBusinesses.this,
                R.layout.layout_user_businesses_item, android.R.id.text1, userBusinessesStr);
        listView.setAdapter(adapter);


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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == Params.ACTION_REGISTER_BUSINESS) {
                MyApplication myApplication = (MyApplication) getApplication();
                myApplication.userBusinesses.add(new User.UserBusinesses(myApplication.business.id, myApplication.business.businessIdentifier));
                userBusinesses.add(0, new User.UserBusinesses(myApplication.business.id, myApplication.business.businessIdentifier));
                userBusinessesStr.add(0, myApplication.business.businessIdentifier);
                adapter.notifyDataSetChanged();
            }
        }

    }


}
