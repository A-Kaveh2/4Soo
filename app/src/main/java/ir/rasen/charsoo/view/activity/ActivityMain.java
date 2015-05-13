package ir.rasen.charsoo.view.activity;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import ir.rasen.charsoo.view.fragment.FragmentUserBusinesses;
import ir.rasen.charsoo.R;
import ir.rasen.charsoo.controller.object.MyApplication;
import ir.rasen.charsoo.controller.helper.LoginInfo;
import ir.rasen.charsoo.view.interface_m.IGoToRegisterBusinessActivity;
import ir.rasen.charsoo.view.interface_m.IChangeTabs;
import ir.rasen.charsoo.view.interface_m.IWebserviceResponse;


public class ActivityMain extends Activity implements View.OnClickListener, IWebserviceResponse, IChangeTabs, IGoToRegisterBusinessActivity {


    ImageView imageViewHome, imageViewSearch, imageViewUser, imageViewBusinesses;
    LinearLayout llFooterHome, llFooterSearch, llFooterUser, llFooterBusinesses;
    RelativeLayout rlHome, rlSearch, rlUser, rlBusinesses;
    FragmentManager fm;
    FragmentTransaction ft;
    ProgressDialog progressDialog;
    int screenWidth;

    private enum FragmentTag {HOME, SEARCH, BUSINESSES, USER}

    ArrayList<FragmentTag> fragmentTagList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressDialog = new ProgressDialog(this);


        fm = getFragmentManager();
        ft = fm.beginTransaction();

        ft.show(fm.findFragmentById(R.id.frag_home));
        ft.hide(fm.findFragmentById(R.id.frag_search));
        ft.hide(fm.findFragmentById(R.id.frag_user));
        ft.hide(fm.findFragmentById(R.id.frag_user_businesses));
        ft.commit();
        fragmentTagList.add(FragmentTag.HOME);

       /* ft.add(R.id.fragmentContainer, fragmentHome);
        ft.commit();*/

        imageViewHome = (ImageView) findViewById(R.id.imageView_home);
        imageViewSearch = (ImageView) findViewById(R.id.imageView_search);
        imageViewUser = (ImageView) findViewById(R.id.imageView_user);
        imageViewBusinesses = (ImageView) findViewById(R.id.imageView_businesses);

        llFooterHome = (LinearLayout) findViewById(R.id.ll_footer_home);
        llFooterSearch = (LinearLayout) findViewById(R.id.ll_footer_search);
        llFooterUser = (LinearLayout) findViewById(R.id.ll_footer_user);
        llFooterBusinesses = (LinearLayout) findViewById(R.id.ll_footer_businesses);


        rlHome = (RelativeLayout) findViewById(R.id.rl_home);
        rlSearch = (RelativeLayout) findViewById(R.id.rl_search);
        rlUser = (RelativeLayout) findViewById(R.id.rl_user);
        rlBusinesses = (RelativeLayout) findViewById(R.id.rl_businesses2);

        rlHome.setOnClickListener(this);
        rlSearch.setOnClickListener(this);
        rlUser.setOnClickListener(this);
        rlBusinesses.setOnClickListener(this);

        screenWidth = getResources().getDisplayMetrics().widthPixels;

        if (LoginInfo.hasBusiness(this)) {
            //display four tab
            makeItFour();
        } else {
            //display three tab
            makeItThree();
        }


    }

    private void setFragment(FragmentTag fragmentTag) {
        switch (fragmentTag) {
            case HOME:
                setSelection(rlHome.getId());
                break;
            case BUSINESSES:
                setSelection(rlBusinesses.getId());
                break;
            case SEARCH:
                setSelection(rlSearch.getId());
                break;
            case USER:
                setSelection(rlUser.getId());
                break;
        }
    }

    private void exit() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void checkBack() {
        switch (fragmentTagList.size()) {
            case 0:
                exit();
                break;
            case 1:
                if (fragmentTagList.get(0) == FragmentTag.HOME)
                    exit();
                fragmentTagList.remove(0);
                setFragment(FragmentTag.HOME);
                break;
            default:
                fragmentTagList.remove(fragmentTagList.size() - 1);
                setFragment(fragmentTagList.get(fragmentTagList.size()-1));
                break;
        }
    }

    private void addFragment(FragmentTag fragmentTag) {
        int fragmentPosition = checkFragment(fragmentTag);
        if (fragmentPosition == -1)
            fragmentTagList.add(fragmentTag);
        else {
            fragmentTagList.remove(fragmentPosition);
            fragmentTagList.add(fragmentTag);
        }
    }

    private int checkFragment(FragmentTag fragmentTag) {
        for (int i = 0; i < fragmentTagList.size(); i++) {
            if (fragmentTag == fragmentTagList.get(i))
                return i;
        }
        return -1;
    }

    private void setParams(int width) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, LinearLayout.LayoutParams.MATCH_PARENT);
        rlBusinesses.setLayoutParams(params);
        rlUser.setLayoutParams(params);
        rlHome.setLayoutParams(params);
        rlSearch.setLayoutParams(params);
    }

    Handler handlerUserBusinessesFragment = new Handler();

    public void recursivelyCallHandlerUserBusinessesFragment() {
        handlerUserBusinessesFragment.postDelayed(new Runnable() {
            @Override
            public void run() {
                //We don't want to run all webservices together
                //first HomeFragment, second SearchFragment and last UserFragment
                if (((MyApplication) getApplication()).isUserCreated) {
                    ft.show(fm.findFragmentById(R.id.frag_user_businesses));
                    ft.hide(fm.findFragmentById(R.id.frag_search));
                    ft.hide(fm.findFragmentById(R.id.frag_home));
                    ft.hide(fm.findFragmentById(R.id.frag_user));
                    ft.commit();
                } else
                    recursivelyCallHandlerUserBusinessesFragment();
            }
        }, 500);
    }

    Handler handlerUserFragment = new Handler();

    public void recursivelyCallHandlerUserFragment() {
        handlerUserFragment.postDelayed(new Runnable() {
            @Override
            public void run() {
                //We don't want to run all webservices together
                //first HomeFragment, second SearchFragment and last UserFragment
                if (((MyApplication) getApplication()).isSearchCreated) {
                    ft.show(fm.findFragmentById(R.id.frag_user));
                    ft.hide(fm.findFragmentById(R.id.frag_search));
                    ft.hide(fm.findFragmentById(R.id.frag_home));
                    ft.hide(fm.findFragmentById(R.id.frag_user_businesses));
                    ft.commit();
                } else
                    recursivelyCallHandlerUserFragment();
            }
        }, 500);
    }

    Handler handlerSearchFragment = new Handler();

    public void recursivelyCallHandlerSearchFragment() {
        handlerSearchFragment.postDelayed(new Runnable() {
            @Override
            public void run() {
                //We don't want to run all webservices together
                //first HomeFragment, second SearchFragment and last UserFragment
                if (((MyApplication) getApplication()).isSearchCreated) {
                    ft.show(fm.findFragmentById(R.id.frag_search));
                    ft.hide(fm.findFragmentById(R.id.frag_home));
                    ft.hide(fm.findFragmentById(R.id.frag_user));
                    ft.hide(fm.findFragmentById(R.id.frag_user_businesses));
                    ft.commit();
                } else
                    recursivelyCallHandlerSearchFragment();
            }
        }, 500);
    }

    private void setSelection(int relativeLayoutId) {
        switch (relativeLayoutId) {
            case R.id.rl_home:
                if (llFooterHome.getVisibility() == View.VISIBLE)
                    return;
                //getSupportActionBar().show();
                addFragment(FragmentTag.HOME);
                rlHome.setBackgroundColor(Color.BLACK);
                rlUser.setBackgroundColor(Color.DKGRAY);
                rlSearch.setBackgroundColor(Color.DKGRAY);
                rlBusinesses.setBackgroundColor(Color.DKGRAY);

                llFooterHome.setVisibility(View.VISIBLE);
                llFooterUser.setVisibility(View.GONE);
                llFooterSearch.setVisibility(View.GONE);
                llFooterBusinesses.setVisibility(View.GONE);


                ft = fm.beginTransaction();
                //ft.replace(R.id.fragmentContainer, fragmentHome);
                ft.show(fm.findFragmentById(R.id.frag_home));
                ft.hide(fm.findFragmentById(R.id.frag_search));
                ft.hide(fm.findFragmentById(R.id.frag_user));
                ft.hide(fm.findFragmentById(R.id.frag_user_businesses));
                ft.commit();

                break;
            case R.id.rl_search:
                if (llFooterSearch.getVisibility() == View.VISIBLE)
                    return;

                //getSupportActionBar().hide();
                addFragment(FragmentTag.SEARCH);
                rlHome.setBackgroundColor(Color.DKGRAY);
                rlUser.setBackgroundColor(Color.DKGRAY);
                rlSearch.setBackgroundColor(Color.BLACK);
                rlBusinesses.setBackgroundColor(Color.DKGRAY);

                llFooterHome.setVisibility(View.GONE);
                llFooterUser.setVisibility(View.GONE);
                llFooterSearch.setVisibility(View.VISIBLE);
                llFooterBusinesses.setVisibility(View.GONE);


                ft = fm.beginTransaction();
                //ft.replace(R.id.fragmentContainer, fragmentSearch);
                if (((MyApplication) getApplication()).isSearchCreated) {
                    ft.show(fm.findFragmentById(R.id.frag_search));
                    ft.hide(fm.findFragmentById(R.id.frag_home));
                    ft.hide(fm.findFragmentById(R.id.frag_user));
                    ft.hide(fm.findFragmentById(R.id.frag_user_businesses));
                    ft.commit();
                } else
                    recursivelyCallHandlerSearchFragment();
                break;
            case R.id.rl_user:
                if (llFooterUser.getVisibility() == View.VISIBLE)
                    return;

                //getSupportActionBar().hide();
                addFragment(FragmentTag.USER);
                rlHome.setBackgroundColor(Color.DKGRAY);
                rlUser.setBackgroundColor(Color.BLACK);
                rlSearch.setBackgroundColor(Color.DKGRAY);
                rlBusinesses.setBackgroundColor(Color.DKGRAY);

                llFooterHome.setVisibility(View.GONE);
                llFooterUser.setVisibility(View.VISIBLE);
                llFooterSearch.setVisibility(View.GONE);
                llFooterBusinesses.setVisibility(View.GONE);


                ft = fm.beginTransaction();
                //ft.replace(R.id.fragmentContainer, fragmentUser);

                if (((MyApplication) getApplication()).isUserCreated) {
                    ft.show(fm.findFragmentById(R.id.frag_user));
                    ft.hide(fm.findFragmentById(R.id.frag_search));
                    ft.hide(fm.findFragmentById(R.id.frag_home));
                    ft.hide(fm.findFragmentById(R.id.frag_user_businesses));
                    ft.commit();
                } else
                    recursivelyCallHandlerUserFragment();
                break;

            case R.id.rl_businesses2:
                //getSupportActionBar().hide();
                addFragment(FragmentTag.BUSINESSES);
                initialUserBusinessesTab();
                break;
        }
    }


    private void makeItThree() {
        rlBusinesses.setVisibility(View.GONE);
        setParams(screenWidth / 3);

    }

    private void makeItFour() {
        rlBusinesses.setVisibility(View.VISIBLE);
        setParams(screenWidth / 4);
    }


    public void initialUserBusinessesTab() {
        rlHome.setBackgroundColor(Color.DKGRAY);
        rlUser.setBackgroundColor(Color.DKGRAY);
        rlSearch.setBackgroundColor(Color.DKGRAY);
        rlBusinesses.setBackgroundColor(Color.BLACK);

        llFooterHome.setVisibility(View.GONE);
        llFooterUser.setVisibility(View.GONE);
        llFooterSearch.setVisibility(View.GONE);
        llFooterBusinesses.setVisibility(View.VISIBLE);

        ft = fm.beginTransaction();
        //ft.replace(R.id.fragmentContainer, fragmentUser);

        //if user.businesses in intialized
        if (((MyApplication) getApplication()).isUserCreated) {
            //ft.show(fm.findFragmentById(R.id.frag_user));
            ft.hide(fm.findFragmentById(R.id.frag_search));
            ft.hide(fm.findFragmentById(R.id.frag_home));
            ft.hide(fm.findFragmentById(R.id.frag_user));
            ft.show(fm.findFragmentById(R.id.frag_user_businesses));
            ft.commit();
        } else
            recursivelyCallHandlerUserBusinessesFragment();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_home:
                setSelection(rlHome.getId());
                break;
            case R.id.rl_user:
                setSelection(rlUser.getId());
                break;
            case R.id.rl_search:
                setSelection(rlSearch.getId());
                break;
            case R.id.rl_businesses2:
                setSelection(rlBusinesses.getId());
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
        checkBack();
    }

    @Override
    public void notifyMakeThreeTab() {
        makeItThree();
        setSelection(rlUser.getId());
    }

    @Override
    public void notifyMakeFourTabsWithInitialize() {
        makeItFour();
        initialUserBusinessesTab();
    }

    @Override
    public void notifyMakeFourTabs() {
        makeItFour();
        ft.show(fm.findFragmentById(R.id.frag_user_businesses));

    }

    @Override
    public void notifyGo() {
        if (((MyApplication) getApplication()).userBusinesses.size() == 0) {
            //we have to go to the FragmentUserBusinesses first and then go to registerBusinessActvitiy because we need get register result back to the fragment.
            FragmentUserBusinesses fragmentUserBusinesses = (FragmentUserBusinesses)
                    fm.findFragmentById(R.id.frag_user_businesses);

            if (fragmentUserBusinesses != null)
                fragmentUserBusinesses.goToRegisterBusinessActivity();
        } else {
            initialUserBusinessesTab();
        }
    }


}
