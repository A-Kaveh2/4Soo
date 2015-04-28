package ir.rasen.charsoo;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import ir.rasen.charsoo.helper.LoginInfo;
import ir.rasen.charsoo.helper.Params;
import ir.rasen.charsoo.interfaces.IWebserviceResponse;


public class ActivityMain2 extends Activity implements View.OnClickListener, IWebserviceResponse {


    ImageView imageViewHome, imageViewSearch, imageViewUser;
    LinearLayout llFooterHome, llFooterSearch, llFooterUser;
    RelativeLayout rlHome, rlSearch, rlUser;
    FragmentManager fm;
    FragmentTransaction ft;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        fm = getFragmentManager();
        ft = fm.beginTransaction();
        try {
            ft.show(fm.findFragmentById(R.id.frag_home));
        }
        catch (Exception e){
            String s = e.getMessage();
        }


        imageViewHome = (ImageView) findViewById(R.id.imageView_home);
        imageViewSearch = (ImageView) findViewById(R.id.imageView_search);
        imageViewUser = (ImageView) findViewById(R.id.imageView_user);

        llFooterHome = (LinearLayout) findViewById(R.id.ll_footer_home);
        llFooterSearch = (LinearLayout) findViewById(R.id.ll_footer_search);
        llFooterUser = (LinearLayout) findViewById(R.id.ll_footer_user);

        rlHome = (RelativeLayout) findViewById(R.id.rl_home);
        rlSearch = (RelativeLayout) findViewById(R.id.rl_search);
        rlUser = (RelativeLayout) findViewById(R.id.rl_user);

        rlHome.setOnClickListener(this);
        rlSearch.setOnClickListener(this);
        rlUser.setOnClickListener(this);


    }


    private void setSelection(RelativeLayout rlContainer) {
        switch (rlContainer.getId()) {
            case R.id.rl_home:
                rlHome.setBackgroundColor(Color.BLACK);
                rlUser.setBackgroundColor(Color.DKGRAY);
                rlSearch.setBackgroundColor(Color.DKGRAY);

                llFooterHome.setVisibility(View.VISIBLE);
                llFooterUser.setVisibility(View.GONE);
                llFooterSearch.setVisibility(View.GONE);

                ft = fm.beginTransaction();
                ft.replace(R.id.fragmentContainer, new FragmentHome());
                ft.commit();

                break;
            case R.id.rl_search:
                rlHome.setBackgroundColor(Color.DKGRAY);
                rlUser.setBackgroundColor(Color.DKGRAY);
                rlSearch.setBackgroundColor(Color.BLACK);

                llFooterHome.setVisibility(View.GONE);
                llFooterUser.setVisibility(View.GONE);
                llFooterSearch.setVisibility(View.VISIBLE);

                ft = fm.beginTransaction();
                ft.replace(R.id.fragmentContainer, new FragmentSearch());
                ft.commit();

                break;
            case R.id.rl_user:
                rlHome.setBackgroundColor(Color.DKGRAY);
                rlUser.setBackgroundColor(Color.BLACK);
                rlSearch.setBackgroundColor(Color.DKGRAY);

                llFooterHome.setVisibility(View.GONE);
                llFooterUser.setVisibility(View.VISIBLE);
                llFooterSearch.setVisibility(View.GONE);

                ft = fm.beginTransaction();

                Bundle bundle = new Bundle();
                bundle.putInt(Params.VISITED_USER_ID, LoginInfo.getUserId(ActivityMain2.this));
                FragmentUser fragmentUser = new FragmentUser();
                fragmentUser.setArguments(bundle);

                ft.replace(R.id.fragmentContainer,fragmentUser);
                ft.commit();
                break;
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_home:
                setSelection(rlHome);
                break;
            case R.id.rl_user:
                setSelection(rlUser);
                break;
            case R.id.rl_search:
                setSelection(rlSearch);
                break;
        }
    }


    @Override
    public void getResult(Object result) {

    }

    @Override
    public void getError(Integer errorCode) {

    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }
}
