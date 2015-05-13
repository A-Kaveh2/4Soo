package ir.rasen.charsoo.view.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import ir.rasen.charsoo.R;
import ir.rasen.charsoo.controller.object.MyApplication;
import ir.rasen.charsoo.controller.object.User;
import ir.rasen.charsoo.controller.helper.LoginInfo;
import ir.rasen.charsoo.controller.helper.Params;
import ir.rasen.charsoo.view.interface_m.IChangeTabs;
import ir.rasen.charsoo.view.activity.ActivityBusiness;
import ir.rasen.charsoo.view.activity.ActivityBusinessRegisterEdit;

public class FragmentUserBusinesses extends Fragment {

    ArrayList<User.UserBusinesses> userBusinesses;
    ArrayList<String> userBusinessesStr;
    ArrayAdapter adapter;
    ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_user_businesses,
                container, false);
        listView = (ListView) view.findViewById(R.id.listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), ActivityBusiness.class);
                intent.putExtra(Params.BUSINESS_ID, userBusinesses.get(i).businessId);
                startActivityForResult(intent, Params.ACTION_DELETE_BUSIENSS);
            }
        });
        (view.findViewById(R.id.btn_creat_new_business)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ActivityBusinessRegisterEdit.class);
                startActivityForResult(intent, Params.ACTION_REGISTER_BUSINESS);
            }
        });

        recursivelyCallHandler();

        return view;
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
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //We don't want to run all webservices together
                //first HomeFragment, second SearchFragment and last UserFragment
                if (((MyApplication) getActivity().getApplication()).isUserCreated) {
                    userBusinesses = new ArrayList<>();
                    userBusinesses.addAll(((MyApplication)getActivity().getApplication()).userBusinesses);

                    if (userBusinesses.size() == 0)
                        return;

                    userBusinessesStr = new ArrayList<>();
                    for (User.UserBusinesses business : userBusinesses) {
                        userBusinessesStr.add(business.businessIdentifier);
                    }

                    adapter = new ArrayAdapter<String>(getActivity(),
                            R.layout.layout_user_businesses_item, android.R.id.text1, userBusinessesStr);
                    listView.setAdapter(adapter);
                } else
                    recursivelyCallHandler();
            }
        }, 500);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        MyApplication myApplication = (MyApplication) getActivity().getApplication();
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Params.ACTION_REGISTER_BUSINESS) {
                myApplication.userBusinesses.add(0,new User.UserBusinesses(myApplication.business.id, myApplication.business.businessIdentifier));

                //if user just add a business
                if (myApplication.userBusinesses.size() == 1) {
                    userBusinessesStr = new ArrayList<>();
                    userBusinesses = new ArrayList<>();
                }

                userBusinesses.add(0, new User.UserBusinesses(myApplication.business.id, myApplication.business.businessIdentifier));
                userBusinessesStr.add(0, myApplication.business.businessIdentifier);

                //if user just add a business
                if (myApplication.userBusinesses.size() == 1) {
                    adapter = new ArrayAdapter<String>(getActivity(),
                            R.layout.layout_user_businesses_item, android.R.id.text1, userBusinessesStr);
                    listView.setAdapter(adapter);

                    if (getActivity() instanceof IChangeTabs)
                        ((IChangeTabs) getActivity()).notifyMakeFourTabsWithInitialize();
                    LoginInfo.submitBusiness(getActivity());
                } else
                    adapter.notifyDataSetChanged();

            } else if (requestCode == Params.ACTION_DELETE_BUSIENSS) {
                int businessId = data.getExtras().getInt(Params.BUSINESS_ID);
                for (int i = 0; i < myApplication.userBusinesses.size(); i++)
                    if (myApplication.userBusinesses.get(i).businessId == businessId) {
                        myApplication.userBusinesses.remove(i);
                        userBusinesses.remove(i);
                        userBusinessesStr.remove(i);
                        break;
                    }

                //if user delete all the his own businesses
                if (myApplication.userBusinesses.size() == 0) {
                    if (getActivity() instanceof IChangeTabs)
                        ((IChangeTabs) getActivity()).notifyMakeThreeTab();
                    LoginInfo.removeBusiness(getActivity());
                } else
                    adapter.notifyDataSetChanged();
            }

        }
    }
}
