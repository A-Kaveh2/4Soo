package ir.rasen.charsoo.view.fragment;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import ir.rasen.charsoo.R;
import ir.rasen.charsoo.controller.helper.LoginInfo;
import ir.rasen.charsoo.controller.helper.Params;
import ir.rasen.charsoo.controller.helper.ServerAnswer;
import ir.rasen.charsoo.controller.object.Business;
import ir.rasen.charsoo.controller.object.MyApplication;
import ir.rasen.charsoo.controller.object.User;
import ir.rasen.charsoo.model.NetworkConnectivityReciever;
import ir.rasen.charsoo.view.activity.ActivityBusiness;
import ir.rasen.charsoo.view.activity.ActivityBusinessRegister;
import ir.rasen.charsoo.view.activity.ActivityBusinessRegisterEdit;
import ir.rasen.charsoo.view.activity.ActivityMain;
import ir.rasen.charsoo.view.adapter.AdapterUserBusinesses;
import ir.rasen.charsoo.view.dialog.DialogMessage;
import ir.rasen.charsoo.view.interface_m.IChangeTabs;
import ir.rasen.charsoo.view.interface_m.IWebserviceResponse;
import ir.rasen.charsoo.view.interface_m.NetworkStateChangeListener;
import ir.rasen.charsoo.view.widgets.buttons.FloatButton;

public class FragmentUserBusinesses extends Fragment implements IWebserviceResponse,NetworkStateChangeListener {
    public static final String TAG="FragmentUserBusinesses";

    ArrayList<Business> userBusinesses;
    AdapterUserBusinesses adapterUserBusinesses;
    ListView listView;
    View view;
    FloatButton addBtn;
    BroadcastReceiver deleteBusinessReciever;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_user_businesses,
                container, false);

        NetworkConnectivityReciever.setNetworkStateListener(TAG,FragmentUserBusinesses.this);
        addBtn = (FloatButton) (view.findViewById(R.id.btn_creat_new_business));
        listView = (ListView) view.findViewById(R.id.listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), ActivityBusiness.class);
                intent.putExtra(Params.BUSINESS_ID_STRING, userBusinesses.get(i).id);
                startActivityForResult(intent, Params.ACTION_DELETE_BUSIENSS);
            }
        });
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (view.getId() == listView.getId()) {
                    addBtn.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
                }
            }
        });
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ActivityBusinessRegister.class);
                startActivityForResult(intent, Params.ACTION_REGISTER_BUSINESS);
            }
        });

        userBusinesses = new ArrayList<>();
        adapterUserBusinesses = new AdapterUserBusinesses(getActivity(), userBusinesses);
        listView.setAdapter(adapterUserBusinesses);

        // TODO:: TEMPORARY
        recursivelyCallHandler();

        // TODO:: WEBSERVICE SHOULD BE WRITTEN ON WEB SIDE AND THEN THIS POST_CODE_STRING CAN BE UNCOMMENTED::
        //new GetUserBusinesses(getActivity(),FragmentUserBusinesses.this).execute();

        deleteBusinessReciever = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int deletedBusinessId = intent.getIntExtra(Params.BUSINESS_ID_STRING,0);
                //TODO:: UPDATE THE BUSINESSES LIST
                ((ActivityMain)getActivity()).setFragment(ActivityMain.FragmentTag.BUSINESSES);
            }
        };
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(deleteBusinessReciever, new IntentFilter(Params.DELETE_BUSINESS));

        return view;
    }

    public void deleteBusiness(int businessId){

    }
    Handler handler = new Handler();
    public void goToRegisterBusinessActivity() {
        Activity activity = getActivity();
        if (activity == null)
            return;
        try {
            Intent intent = new Intent(activity, ActivityBusinessRegisterEdit.class);
            startActivityForResult(intent, Params.ACTION_REGISTER_BUSINESS);
        } catch (Exception e) {
            String s = e.getMessage();
        }
    }

    public void recursivelyCallHandler() {
        //TODO:: TEMPORARY::
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //We don't want to run all webservices together
                //first HomeFragment, second SearchFragment and last UserFragment
                try { // agar pish az Ejad shodane userBusinesses az app kharej shavim force midahad
                    if (((MyApplication) getActivity().getApplication()).isUserCreated) {
                        view.findViewById(R.id.progressBar).setVisibility(View.GONE);
                        ArrayList<User.UserBusinesses> us = ((MyApplication) getActivity().getApplication()).userBusinesses;

                        if (us.size() == 0)
                            return;

                        for (User.UserBusinesses businessItem : us) {
                            Business business = new Business();
                            business.id = businessItem.businessId;
                            business.name = businessItem.businessIdentifier;
                            userBusinesses.add(business);
                        }
                        adapterUserBusinesses.notifyDataSetChanged();

                    } else {
                        ConnectivityManager cm =
                                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
                        NetworkInfo netInfo = cm.getActiveNetworkInfo();
                        if (netInfo != null && netInfo.isConnectedOrConnecting())
                            recursivelyCallHandler();
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }, 500);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        MyApplication myApplication = (MyApplication) getActivity().getApplication();
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Params.ACTION_REGISTER_BUSINESS) {

                myApplication.userBusinesses.add(0, new User.UserBusinesses(myApplication.business.id, myApplication.business.businessIdentifier));

                //changed by Sina
                //if user just add a business
                if (myApplication.userBusinesses.size() == 1) {
                    userBusinesses = new ArrayList<>();
                }

                User.UserBusinesses u = new User.UserBusinesses(myApplication.business.id, myApplication.business.businessIdentifier);
                Business business = new Business();
                business.id = u.businessId;
                business.name = u.businessIdentifier;
                // TODO:: should be added to class
//                business.description = u.description;
//                business.profilePictureId = u.businessPicId;
                userBusinesses.add(0, business);

                //if user just add a business
                if (myApplication.userBusinesses.size() == 1) {
                    adapterUserBusinesses.notifyDataSetChanged();

                    if (getActivity() instanceof IChangeTabs)
                        ((IChangeTabs) getActivity()).notifyMakeFourTabsWithInitialize();
                    LoginInfo.submitBusiness(getActivity());
                } else
                    adapterUserBusinesses.notifyDataSetChanged();

            } else if (requestCode == Params.ACTION_DELETE_BUSIENSS) {
                int businessId = data.getExtras().getInt(Params.BUSINESS_ID_STRING);
                for (int i = 0; i < myApplication.userBusinesses.size(); i++)
                    if (myApplication.userBusinesses.get(i).businessId == businessId) {
                        myApplication.userBusinesses.remove(i);
                        userBusinesses.remove(i);
                        break;
                    }

                //if user delete all the his own businesses
                if (myApplication.userBusinesses.size() == 0) {
                    if (getActivity() instanceof IChangeTabs)
                        ((IChangeTabs) getActivity()).notifyMakeThreeTab();
                    LoginInfo.removeBusiness(getActivity());
                } else
                    adapterUserBusinesses.notifyDataSetChanged();
            }

        }
    }

    @Override
    public void getResult(Object result) {
        view.findViewById(R.id.progressBar).setVisibility(View.GONE);
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
    public void getError(Integer errorCode,String callerStringID) {
        new DialogMessage(getActivity(), ServerAnswer.getError(getActivity(), errorCode,callerStringID+">"+TAG)).show();
    }


    @Override
    public void doOnNetworkConnected() {
        recursivelyCallHandler();
    }
}
