package ir.rasen.charsoo.view.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.util.ArrayList;

import ir.rasen.charsoo.R;
import ir.rasen.charsoo.controller.object.ContactEntry;
import ir.rasen.charsoo.view.adapter.AdapterInviteFriendsBySMS;
import ir.rasen.charsoo.view.interface_m.IWebserviceResponse;

/**
 * Created by hossein-pc on 6/9/2015.
 */
public class FragmentUserRegisterOfferFriendAdd extends Fragment implements IWebserviceResponse{

    public static final String TAG="OfferFriendAddFriend";
    ListView sc;
    AdapterInviteFriendsBySMS af;

    boolean hasCharsooContacts=false;
    LinearLayout hasNoFriend;
    LinearLayout linearLayoutProgressBar;
    private ArrayList<ContactEntry> charsooContactList;

    public void getContactsHaveCharsoo(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_register_offer_friends_add,
                container, false);
        hasNoFriend=(LinearLayout) view.findViewById(R.id.ll_hasNoFriend);
        linearLayoutProgressBar=(LinearLayout) view.findViewById(R.id.ll_ProgressBar);
        if (charsooContactList==null){

        }
        else
        {
            if (charsooContactList.isEmpty())
                ((LinearLayout) view.findViewById(R.id.ll_hasNoFriend)).setVisibility(View.VISIBLE);
            linearLayoutProgressBar.setVisibility(View.GONE);
        }
//        hasApplicationX=new Hashtable<>();
//        new GetInstalledApps(getActivity()).execute();

//        sc.setAdapter(af);

        return view;

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
//        if (charsooContactList==null);
////            af=new AdapterInviteFriendsBySMS(getActivity(),new ArrayList<ContactEntry>());
//        else{
////            af=new AdapterInviteFriendsBySMS(getActivity(),charsooContactList);
//            if (charsooContactList.isEmpty())
//                ((LinearLayout) view.findViewById(R.id.ll_hasNoFriend)).setVisibility(View.VISIBLE);
//            else
//                af.resetItems(charsooContactList);
//        }
        sc=(ListView) view.findViewById(R.id.listView2);
    }


    @Override
    public void getResult(Object result) {

    }

    @Override
    public void getError(Integer errorCode, String callerStringID) {

    }


    public void setCharsooContacts(ArrayList<ContactEntry> charsooContacts){
        charsooContactList=new ArrayList<>(charsooContacts);
        if (charsooContactList.isEmpty()){
            if (hasNoFriend!=null)
                hasNoFriend.setVisibility(View.VISIBLE);
        }
        if (linearLayoutProgressBar!=null)
            linearLayoutProgressBar.setVisibility(View.GONE);
        if (af != null){
//            if (af.getCount()<=0)
                af.resetItems(charsooContactList);
        }

    }



}
