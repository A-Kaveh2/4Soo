package ir.rasen.charsoo.view.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

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

    View view;
    private ArrayList<ContactEntry> charsooContactList;

    public void getContactsHaveCharsoo(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_user_register_offer_friends_add,
                container, false);

//        hasApplicationX=new Hashtable<>();
//        new GetInstalledApps(getActivity()).execute();
        if (charsooContactList==null);
//            af=new AdapterInviteFriendsBySMS(getActivity(),new ArrayList<ContactEntry>());
        else{
//            af=new AdapterInviteFriendsBySMS(getActivity(),charsooContactList);
            if (charsooContactList.isEmpty())
                ((LinearLayout) view.findViewById(R.id.ll_hasNoFriend)).setVisibility(View.VISIBLE);
        }
        sc=(ListView) view.findViewById(R.id.listView2);
//        sc.setAdapter(af);

        return view;

    }

    @Override
    public void getResult(Object result) {

    }

    @Override
    public void getError(Integer errorCode, String callerStringID) {

    }


    public void getCharsooContacts(ArrayList<ContactEntry> charsooContacts){
        if (charsooContacts.isEmpty())
            ((LinearLayout) view.findViewById(R.id.ll_hasNoFriend)).setVisibility(View.VISIBLE);
        else{
            af.loadMore(charsooContacts);
        }
        charsooContactList=charsooContacts;
    }



}
