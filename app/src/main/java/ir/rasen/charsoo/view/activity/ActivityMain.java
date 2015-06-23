package ir.rasen.charsoo.view.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;

import java.util.ArrayList;

import ir.rasen.charsoo.R;
import ir.rasen.charsoo.controller.helper.Image_M;
import ir.rasen.charsoo.controller.image_loader.SimpleLoader;
import ir.rasen.charsoo.controller.object.MyApplication;
import ir.rasen.charsoo.controller.object.User;
import ir.rasen.charsoo.view.dialog.DialogExit;
import ir.rasen.charsoo.view.fragment.FragmentUserBusinesses;
import ir.rasen.charsoo.view.interface_m.IWebserviceResponse;
import ir.rasen.charsoo.view.widgets.TextViewFont;
import ir.rasen.charsoo.view.widgets.charsoo_activity.CharsooActivity;
import ir.rasen.charsoo.view.widgets.imageviews.ImageViewCircle;

public class ActivityMain extends CharsooActivity implements View.OnClickListener, IWebserviceResponse {//, IChangeTabs {

    private static final int FRAG_HOME=0, FRAG_SEARCH=1, FRAG_BUSINESS=2, FRAG_USER=3;
    private DrawerLayout drawerLayout;
    private View btnHome;

    boolean footerHome=true, footerUser, footerSearch, footerBusiness;

    FragmentManager fm;
    ProgressDialog progressDialog;
    int screenWidth;

    @Override
    public void onClick(View v) {

    }

    public enum FragmentTag {HOME, SEARCH, BUSINESSES, USER}

    ArrayList<FragmentTag> fragmentTagList = new ArrayList<FragmentTag>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressDialog = new ProgressDialog(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        btnHome = toolbar.findViewById(R.id.btn_home);

        fm = getSupportFragmentManager();

        fm.beginTransaction()
        .show(fm.findFragmentById(R.id.frag_home))
        .hide(fm.findFragmentById(R.id.frag_search))
        .hide(fm.findFragmentById(R.id.frag_user))
        .hide(fm.findFragmentById(R.id.frag_user_businesses))
        .commit();
        fragmentTagList.add(FragmentTag.HOME);

       /* ft.add(R.id.fragmentContainer, fragmentHome);
        ft.commit();*/

        screenWidth = getResources().getDisplayMetrics().widthPixels;

        initDrawerLayout();
        initNavigationView();

    }

    public void setFragment(FragmentTag fragmentTag) {
        switch (fragmentTag) {
            case HOME:
                setSelection(FRAG_HOME);
                break;
            case SEARCH:
                setSelection(FRAG_SEARCH);
                break;
        }

    }

    private void exit() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
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
                        fm.beginTransaction()
                        .show(fm.findFragmentById(R.id.frag_user_businesses))
                        .hide(fm.findFragmentById(R.id.frag_search))
                        .hide(fm.findFragmentById(R.id.frag_home))
                        .hide(fm.findFragmentById(R.id.frag_user))
                        .commit();
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
                    if (footerUser) {
                        fm.beginTransaction()
                        .show(fm.findFragmentById(R.id.frag_user))
                        .hide(fm.findFragmentById(R.id.frag_search))
                        .hide(fm.findFragmentById(R.id.frag_home))
                        .hide(fm.findFragmentById(R.id.frag_user_businesses))
                        .commit();
                    }
            }
        }, 500);
    }

    private void setSelection(int relativeLayoutId) {
        drawerLayout.closeDrawer(Gravity.RIGHT);
        switch (relativeLayoutId) {
            case FRAG_HOME:
                if (footerHome)
                    return;
                //getSupportActionBar().show();

                addFragment(FragmentTag.HOME);

                footerHome=true;
                footerBusiness=false;
                footerSearch=false;
                footerUser=false;


                fm.beginTransaction()
                //ft.replace(R.id.fragmentContainer, fragmentHome);
                .show(fm.findFragmentById(R.id.frag_home))
                .hide(fm.findFragmentById(R.id.frag_search))
                .hide(fm.findFragmentById(R.id.frag_user))
                .hide(fm.findFragmentById(R.id.frag_user_businesses))
                .commit();

                ((ImageView) drawerLayout.findViewById(R.id.drawer_home_img)).setImageResource(R.mipmap.ic_home_active);
                ((ImageView) drawerLayout.findViewById(R.id.drawer_businesses_img)).setImageResource(R.mipmap.ic_business);
                findViewById(R.id.drawer_home).setBackgroundColor(getResources().getColor(R.color.material_gray_light));
                findViewById(R.id.drawer_businesses).setBackgroundResource(R.drawable.bg_menu);

                btnHome.setVisibility(View.GONE);

                break;
            case FRAG_SEARCH:
                if (footerSearch)
                    return;

                //getSupportActionBar().hide();
                addFragment(FragmentTag.SEARCH);

                footerHome = false;
                footerBusiness=false;
                footerSearch=true;
                footerUser=false;

                fm.beginTransaction()
                .show(fm.findFragmentById(R.id.frag_search))
                .hide(fm.findFragmentById(R.id.frag_home))
                .hide(fm.findFragmentById(R.id.frag_user))
                .hide(fm.findFragmentById(R.id.frag_user_businesses))
                .commit();
                //ft.replace(R.id.fragmentContainer, fragmentSearch);
                /*if (((MyApplication) getApplication()).isSearchCreated) {
                    ft.show(fm.findFragmentById(R.id.frag_search));
                    ft.hide(fm.findFragmentById(R.id.frag_home));
                    ft.hide(fm.findFragmentById(R.id.frag_user));
                    ft.hide(fm.findFragmentById(R.id.frag_user_businesses));
                    ft.commit();
                } else
                    recursivelyCallHandlerSearchFragment();*/

                ((ImageView) drawerLayout.findViewById(R.id.drawer_home_img)).setImageResource(R.mipmap.ic_home);
                ((ImageView) drawerLayout.findViewById(R.id.drawer_businesses_img)).setImageResource(R.mipmap.ic_business);
                findViewById(R.id.drawer_home).setBackgroundResource(R.drawable.bg_menu);
                findViewById(R.id.drawer_businesses).setBackgroundResource(R.drawable.bg_menu);

                btnHome.setVisibility(View.VISIBLE);
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
            case FRAG_USER:
                if (footerUser)
                    return;

                //getSupportActionBar().hide();
                addFragment(FragmentTag.USER);

                footerHome = false;
                footerBusiness=false;
                footerSearch=false;
                footerUser=true;

                fm.beginTransaction()
                .show(fm.findFragmentById(R.id.frag_user))
                .hide(fm.findFragmentById(R.id.frag_search))
                .hide(fm.findFragmentById(R.id.frag_home))
                .hide(fm.findFragmentById(R.id.frag_user_businesses))
                .commit();
                //ft.replace(R.id.fragmentContainer, fragmentUser);

                if (((MyApplication) getApplication()).isUserCreated)
                    recursivelyCallHandlerUserFragment();

                ((ImageView) drawerLayout.findViewById(R.id.drawer_home_img)).setImageResource(R.mipmap.ic_home);
                ((ImageView) drawerLayout.findViewById(R.id.drawer_businesses_img)).setImageResource(R.mipmap.ic_business);
                findViewById(R.id.drawer_home).setBackgroundResource(R.drawable.bg_menu);
                findViewById(R.id.drawer_businesses).setBackgroundResource(R.drawable.bg_menu);

                btnHome.setVisibility(View.VISIBLE);
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
            case FRAG_BUSINESS:
                //getSupportActionBar().hide();
                if (footerBusiness)
                    return;
                addFragment(FragmentTag.BUSINESSES);
                initialUserBusinessesTab();

                ((ImageView) drawerLayout.findViewById(R.id.drawer_home_img)).setImageResource(R.mipmap.ic_home);
                ((ImageView) drawerLayout.findViewById(R.id.drawer_businesses_img)).setImageResource(R.mipmap.ic_business_active);
                findViewById(R.id.drawer_home).setBackgroundResource(R.drawable.bg_menu);
                findViewById(R.id.drawer_businesses).setBackgroundColor(getResources().getColor(R.color.material_gray_light));

                btnHome.setVisibility(View.VISIBLE);
                break;
        }
    }

    public void initialUserBusinessesTab() {

        footerHome = false;
        footerBusiness=true;
        footerSearch=false;
        footerUser=false;

        fm.beginTransaction()
        .hide(fm.findFragmentById(R.id.frag_search))
        .hide(fm.findFragmentById(R.id.frag_home))
        .hide(fm.findFragmentById(R.id.frag_user))
        .show(fm.findFragmentById(R.id.frag_user_businesses))
        .commit();
        //ft.replace(R.id.fragmentContainer, fragmentUser);

        //if user.businesses in intialized
        if (((MyApplication) getApplication()).isUserCreated)
            recursivelyCallHandlerUserBusinessesFragment();

        btnHome.setVisibility(View.VISIBLE);
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
    public void getResult(Object result) {

    }

    @Override
    public void getError(Integer errorCode,String callerStringID) {

    }

    @Override
    public void onBackPressed() {
        if(btnHome.getVisibility()==View.VISIBLE)
            toHome(null);
        else
            finish();
//        checkBack();
    }

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

    private void initialPopupOptionsUser(View view, final PopupWindow popupWindow) {
/*        (view.findViewById(R.id.imageView_drawer_edit)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, ActivityProfileUser.class);
                activity.startActivity(intent);
                popupWindow.dismiss();
            }
        });

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
        });*/
    }
    public void switchPopupWindow(View view) {
        drawerLayout.openDrawer(Gravity.RIGHT);
    }

    private void initDrawerLayout() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
    }
    private void initNavigationView() {
        //Initializing NavigationView

        findViewById(R.id.drawer_home).setBackgroundColor(getResources().getColor(R.color.material_gray_light));
        (findViewById(R.id.drawer_profile)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setSelection(FRAG_USER);
            }
        });
        (findViewById(R.id.drawer_home)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setSelection(FRAG_HOME);
            }
        });
        (findViewById(R.id.drawer_businesses)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setSelection(FRAG_BUSINESS);
            }
        });
        (findViewById(R.id.drawer_feedback)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getResources().getString(R.string.url_contact_us)));
                startActivity(browserIntent);
                drawerLayout.closeDrawer(Gravity.RIGHT);
            }
        });
        (findViewById(R.id.drawer_settings)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityMain.this, ActivityUserSetting.class);
                startActivity(intent);
                drawerLayout.closeDrawer(Gravity.RIGHT);
            }
        });
        (findViewById(R.id.drawer_help)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getResources().getString(R.string.url_guide)));
                startActivity(browserIntent);
                drawerLayout.closeDrawer(Gravity.RIGHT);
            }
        });
        (findViewById(R.id.drawer_exit)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DialogExit(ActivityMain.this).show();
                drawerLayout.closeDrawer(Gravity.RIGHT);
            }
        });
        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
/*        setNavigationItemSelectedListener(new OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {


                //Checking if the item is in checked state or not, if not make it in checked state
                if(menuItem.isChecked()) menuItem.setChecked(false);
                else menuItem.setChecked(true);

                //Closing drawer on item click
                drawerLayout.closeDrawers();

                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()){
                    case R.id.home:
                    default:
                        Toast.makeText(getApplicationContext(),"Somethings Wrong",Toast.LENGTH_SHORT).show();
                        return true;

                }
            }
        });*/
    }

    public void setUserDrawer(User user) {
        SimpleLoader simpleLoader = new SimpleLoader(this);
        simpleLoader.loadImage(user.profilePictureId, Image_M.SMALL, Image_M.ImageType.USER
                , (ImageViewCircle) findViewById(R.id.drawer_user_pic));
        ((TextViewFont) findViewById(R.id.drawer_user_name)).setText(user.name);
        ((TextViewFont) findViewById(R.id.drawer_user_id)).setText(user.userIdentifier);
    }
    public void toHome(View view) {
        setSelection(FRAG_HOME);
    }
    public void toSearch(View view) {
        setSelection(FRAG_SEARCH);
    }

}
