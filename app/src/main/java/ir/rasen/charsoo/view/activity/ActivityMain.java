package ir.rasen.charsoo.view.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import java.util.ArrayList;

import ir.rasen.charsoo.R;
import ir.rasen.charsoo.controller.helper.LoginInfo;
import ir.rasen.charsoo.controller.helper.Params;
import ir.rasen.charsoo.controller.object.MyApplication;
import ir.rasen.charsoo.view.dialog.DialogExit;
import ir.rasen.charsoo.view.fragment.FragmentUserBusinesses;
import ir.rasen.charsoo.view.interface_m.IChangeTabs;
import ir.rasen.charsoo.view.interface_m.IWebserviceResponse;
import ir.rasen.charsoo.view.widget_customized.buttons.NoShadowFloatButton;
import ir.rasen.charsoo.view.widget_customized.charsoo_activity.CharsooActivity;


public class ActivityMain extends CharsooActivity implements View.OnClickListener, IWebserviceResponse, IChangeTabs {

    PopupWindow popupWindow;

    boolean footerHome=true, footerUser, footerSearch, footerBusiness, popupWindowDS=false;

    NoShadowFloatButton btnHome, btnSearch, btnUser, btnBusiness;
    FragmentManager fm;
    FragmentTransaction ft;
    ProgressDialog progressDialog;
    int screenWidth;


    public enum FragmentTag {HOME, SEARCH, BUSINESSES, USER}

    ArrayList<FragmentTag> fragmentTagList = new ArrayList<FragmentTag>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressDialog = new ProgressDialog(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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

        btnHome = (NoShadowFloatButton) findViewById(R.id.btn_home);
        btnSearch = (NoShadowFloatButton) findViewById(R.id.btn_search);
        btnUser = (NoShadowFloatButton) findViewById(R.id.btn_user);
        btnBusiness = (NoShadowFloatButton) findViewById(R.id.btn_businesses2);

        btnHome.setOnClickListener(this);
        btnSearch.setOnClickListener(this);
        btnUser.setOnClickListener(this);
        btnBusiness.setOnClickListener(this);

        screenWidth = getResources().getDisplayMetrics().widthPixels;

        if (LoginInfo.hasBusiness(this)) {
            //if user has any business, display four tab
            makeItFour();
        } else {
            //display three tab
            makeItThree();
        }


    }

    public void setFragment(FragmentTag fragmentTag) {
        switch (fragmentTag) {
            case HOME:
                setSelection(btnHome.getId(), true);
                break;
            case BUSINESSES:
                setSelection(btnBusiness.getId(), true);
                break;
            case SEARCH:
                setSelection(btnSearch.getId(), true);
                break;
            case USER:
                setSelection(btnUser.getId(), true);
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
        //if the fragment is not added before
        if (fragmentPosition == -1)
            fragmentTagList.add(fragmentTag);
        else {
            //if the fragment is added before, remove it and add the fragment in the end of the list
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

    Handler handlerUserBusinessesFragment = new Handler();

    public void recursivelyCallHandlerUserBusinessesFragment() {
        handlerUserBusinessesFragment.postDelayed(new Runnable() {
            @Override
            public void run() {
                //We don't want to run all webservices together
                //first HomeFragment, second SearchFragment and last UserFragment
                if (((MyApplication) getApplication()).isUserCreated) {
                    if (footerBusiness) {
                        ft = fm.beginTransaction();
                        ft.show(fm.findFragmentById(R.id.frag_user_businesses));
                        ft.hide(fm.findFragmentById(R.id.frag_search));
                        ft.hide(fm.findFragmentById(R.id.frag_home));
                        ft.hide(fm.findFragmentById(R.id.frag_user));
                        ft.commit();
                    }
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
                    if (footerUser) {
                        ft = fm.beginTransaction();
                        ft.show(fm.findFragmentById(R.id.frag_user));
                        ft.hide(fm.findFragmentById(R.id.frag_search));
                        ft.hide(fm.findFragmentById(R.id.frag_home));
                        ft.hide(fm.findFragmentById(R.id.frag_user_businesses));
                        ft.commit();
                    }
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
                    if (footerSearch) {
                        ft = fm.beginTransaction();
                        ft.show(fm.findFragmentById(R.id.frag_search));
                        ft.hide(fm.findFragmentById(R.id.frag_home));
                        ft.hide(fm.findFragmentById(R.id.frag_user));
                        ft.hide(fm.findFragmentById(R.id.frag_user_businesses));
                        ft.commit();
                    }
                } else
                    recursivelyCallHandlerSearchFragment();
            }
        }, 500);
    }

    private void setSelection(int relativeLayoutId, boolean autoSetBackground) {
        switch (relativeLayoutId) {
            case R.id.btn_home:
                if (footerHome)
                    return;
                //getSupportActionBar().show();
                nothingChoseInHeader();

                addFragment(FragmentTag.HOME);
                btnHome.setDrawableIcon(R.drawable.ic_home_blue_36dp);
                if(autoSetBackground)
                    btnHome.setBackgroundColor(getResources().getColor(android.R.color.white));
                else
                    btnHome.setFillWithRipple(true);

                footerHome=true;
                footerBusiness=false;
                footerSearch=false;
                footerUser=false;


                ft = fm.beginTransaction();
                //ft.replace(R.id.fragmentContainer, fragmentHome);
                ft.show(fm.findFragmentById(R.id.frag_home));
                ft.hide(fm.findFragmentById(R.id.frag_search));
                ft.hide(fm.findFragmentById(R.id.frag_user));
                ft.hide(fm.findFragmentById(R.id.frag_user_businesses));
                ft.commit();

                break;
            case R.id.btn_search:
                if (footerSearch)
                    return;

                //getSupportActionBar().hide();
                addFragment(FragmentTag.SEARCH);

                nothingChoseInHeader();
                btnSearch.setDrawableIcon(R.drawable.ic_search_blue_36dp);
                btnSearch.setDrawableIcon(R.drawable.ic_home_blue_36dp);
                if(autoSetBackground)
                    btnSearch.setBackgroundColor(getResources().getColor(android.R.color.white));
                else
                    btnSearch.setFillWithRipple(true);

                footerHome = false;
                footerBusiness=false;
                footerSearch=true;
                footerUser=false;

                ft = fm.beginTransaction();
                ft.show(fm.findFragmentById(R.id.frag_search));
                ft.hide(fm.findFragmentById(R.id.frag_home));
                ft.hide(fm.findFragmentById(R.id.frag_user));
                ft.hide(fm.findFragmentById(R.id.frag_user_businesses));
                ft.commit();
                //ft.replace(R.id.fragmentContainer, fragmentSearch);
                /*if (((MyApplication) getApplication()).isSearchCreated) {
                    ft.show(fm.findFragmentById(R.id.frag_search));
                    ft.hide(fm.findFragmentById(R.id.frag_home));
                    ft.hide(fm.findFragmentById(R.id.frag_user));
                    ft.hide(fm.findFragmentById(R.id.frag_user_businesses));
                    ft.commit();
                } else
                    recursivelyCallHandlerSearchFragment();*/
                if (!((MyApplication) getApplication()).isSearchCreated)
                    recursivelyCallHandlerSearchFragment();
                break;
                /*if (footerSearch)
                    return;

                //getSupportActionBar().hide();
                addFragment(FragmentTag.SEARCH);

                nothingChoseInHeader();
                imageViewSearch.setImageResource(R.drawable.ic_search_blue_36dp);

                footerHome=false;
                footerBusiness=false;
                footerSearch=true;
                footerUser=false;


                //ft.replace(R.id.fragmentContainer, fragmentSearch);
                if (((MyApplication) getApplication()).isSearchCreated) {
                    ft = fm.beginTransaction();
                    ft.show(fm.findFragmentById(R.id.frag_search));
                    ft.hide(fm.findFragmentById(R.id.frag_home));
                    ft.hide(fm.findFragmentById(R.id.frag_user));
                    ft.hide(fm.findFragmentById(R.id.frag_user_businesses));
                    ft.commit();
                } else
                    recursivelyCallHandlerSearchFragment();
                break;*/
            case R.id.btn_user:
                if (footerUser)
                    return;

                //getSupportActionBar().hide();
                addFragment(FragmentTag.USER);

                nothingChoseInHeader();
                btnUser.setDrawableIcon(R.drawable.ic_person_blue_36dp);
                btnUser.setDrawableIcon(R.drawable.ic_home_blue_36dp);
                if(autoSetBackground)
                    btnUser.setBackgroundColor(getResources().getColor(android.R.color.white));
                else
                    btnUser.setFillWithRipple(true);

                footerHome = false;
                footerBusiness=false;
                footerSearch=false;
                footerUser=true;

                ft = fm.beginTransaction();
                ft.show(fm.findFragmentById(R.id.frag_user));
                ft.hide(fm.findFragmentById(R.id.frag_search));
                ft.hide(fm.findFragmentById(R.id.frag_home));
                ft.hide(fm.findFragmentById(R.id.frag_user_businesses));
                ft.commit();
                //ft.replace(R.id.fragmentContainer, fragmentUser);

                if (((MyApplication) getApplication()).isUserCreated)
                    recursivelyCallHandlerUserFragment();
                break;

                    //old code
                /*if (footerUser)
                    return;

                //getSupportActionBar().hide();
                addFragment(FragmentTag.USER);

                nothingChoseInHeader();
                imageViewUser.setImageResource(R.drawable.ic_person_blue_36dp);

                footerHome=false;
                footerBusiness=false;
                footerSearch=false;
                footerUser=true;

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
                break;*/
            case R.id.btn_businesses2:
                //getSupportActionBar().hide();
                if (footerBusiness)
                    return;
                addFragment(FragmentTag.BUSINESSES);
                initialUserBusinessesTab(autoSetBackground);
                break;
        }
    }


    private void makeItThree() {
        btnBusiness.setVisibility(View.GONE);
    }

    private void makeItFour() {
        btnBusiness.setVisibility(View.VISIBLE);
    }


    public void initialUserBusinessesTab(boolean autoSetBackground) {
        nothingChoseInHeader();

        btnBusiness.setDrawableIcon(R.drawable.ic_store_mall_directory_blue_36dp);
        btnBusiness.setDrawableIcon(R.drawable.ic_home_blue_36dp);
        if(autoSetBackground)
            btnBusiness.setBackgroundColor(getResources().getColor(android.R.color.white));
        else
            btnBusiness.setFillWithRipple(true);

        footerHome = false;
        footerBusiness=true;
        footerSearch=false;
        footerUser=false;

        ft = fm.beginTransaction();
        ft.hide(fm.findFragmentById(R.id.frag_search));
        ft.hide(fm.findFragmentById(R.id.frag_home));
        ft.hide(fm.findFragmentById(R.id.frag_user));
        ft.show(fm.findFragmentById(R.id.frag_user_businesses));
        ft.commit();
        //ft.replace(R.id.fragmentContainer, fragmentUser);

        //if user.businesses in intialized
        if (((MyApplication) getApplication()).isUserCreated)
            recursivelyCallHandlerUserBusinessesFragment();
        /*nothingChoseInHeader();

        imageViewBusinesses.setImageResource(R.drawable.ic_store_mall_directory_blue_36dp);

        footerHome=false;
        footerBusiness=true;
        footerSearch=false;
        footerUser=false;

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
            recursivelyCallHandlerUserBusinessesFragment();*/
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_home:
                setSelection(btnHome.getId(), false);
                break;
            case R.id.btn_user:
                setSelection(btnUser.getId(), false);
                break;
            case R.id.btn_search:
                setSelection(btnSearch.getId(), false);
                break;
            case R.id.btn_businesses2:
                setSelection(btnBusiness.getId(), false);
                break;
        }
    }


    @Override
    public void getResult(Object result) {

    }

    @Override
    public void getError(Integer errorCode,String callerStringID) {

    }

    @Override
    public void onBackPressed() {
        if(popupWindow!=null && popupWindow.isShowing()) {
            popupWindow.dismiss();
            return;
        }
        checkBack();
    }

    @Override
    public void notifyMakeThreeTab() {
        makeItThree();
        setSelection(btnUser.getId(), false);
    }

    @Override
    public void notifyMakeFourTabsWithInitialize() {
        makeItFour();
        initialUserBusinessesTab(true);
    }

    @Override
    public void notifyMakeFourTabs() {
        makeItFour();
        ft.show(fm.findFragmentById(R.id.frag_user_businesses));

    }

    public void notifyGo() {
        if (((MyApplication) getApplication()).userBusinesses.size() == 0) {
            //we have to go to the FragmentUserBusinesses first and then go to registerBusinessActvitiy because we need get register result back to the fragment.
            FragmentUserBusinesses fragmentUserBusinesses = (FragmentUserBusinesses)
                    fm.findFragmentById(R.id.frag_user_businesses);

            if (fragmentUserBusinesses != null)
                fragmentUserBusinesses.goToRegisterBusinessActivity();
        } else {
            initialUserBusinessesTab(true);
        }
    }

    private void nothingChoseInHeader() {
        btnHome.setDrawableIcon(R.drawable.ic_home_white_36dp);
        btnHome.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        btnHome.setFillWithRipple(true);
        btnUser.setDrawableIcon(R.drawable.ic_person_white_36dp);
        btnUser.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        btnUser.setFillWithRipple(true);
        btnSearch.setDrawableIcon(R.drawable.ic_search_white_36dp);
        btnSearch.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        btnSearch.setFillWithRipple(true);
        btnBusiness.setDrawableIcon(R.drawable.ic_store_mall_directory_white_36dp);
        btnBusiness.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        btnBusiness.setFillWithRipple(true);
    }


    private void initialPopupOptionsUser(View view, final PopupWindow popupWindow) {
/*        (view.findViewById(R.id.imageView_drawer_edit)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, ActivityProfileUser.class);
                activity.startActivity(intent);
                popupWindow.dismiss();
            }
        });
*/
        (view.findViewById(R.id.ll_drawer_businesses)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityMain.this, ActivityBusinessRegisterEdit.class);
                startActivityForResult(intent, Params.ACTION_REGISTER_BUSINESS);
                notifyGo();
                popupWindow.dismiss();
            }
        });
        (view.findViewById(R.id.ll_drawer_setting)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityMain.this, ActivityUserSetting.class);
                startActivity(intent);
                popupWindow.dismiss();
            }
        });
        (view.findViewById(R.id.ll_drawer_exit)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DialogExit(ActivityMain.this).show();
                popupWindow.dismiss();
            }
        });
        (view.findViewById(R.id.ll_drawer_contact_us)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getResources().getString(R.string.url_contact_us)));
                startActivity(browserIntent);
                popupWindow.dismiss();
            }
        });
        (view.findViewById(R.id.ll_drawer_guide)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getResources().getString(R.string.url_guide)));
                startActivity(browserIntent);
                popupWindow.dismiss();
            }
        });

        (view.findViewById(R.id.ll_drawer)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });
    }

    public void initPopupWindowUser() {
        popupWindow = new PopupWindow(ActivityMain.this);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.popup_options_user, null, false);
        popupWindow.setContentView(view);

        initialPopupOptionsUser(view, popupWindow);

        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                popupWindowDS=true;
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        popupWindowDS = false;
                    }
                }, 100);
            }
        });
        popupWindow.setWindowLayoutMode(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);

    }

    public void switchPopupWindow(View view) {
        // SHOW POPUP
        if(popupWindow==null)
            initPopupWindowUser();
        if(!popupWindow.isShowing() && !popupWindowDS)
            popupWindow.showAsDropDown(view);
    }

}
