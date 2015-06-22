package ir.rasen.charsoo.view.fragment;

import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;

import java.util.ArrayList;
import java.util.Hashtable;

import ir.rasen.charsoo.R;
import ir.rasen.charsoo.controller.object.ContactEntry;
import ir.rasen.charsoo.view.adapter.AdapterInviteFriendsBySMS;
import ir.rasen.charsoo.view.interface_m.IFragInviteSelectionListener;
import ir.rasen.charsoo.view.widgets.buttons.ButtonFont;
import ir.rasen.charsoo.view.widgets.imageviews.RoundedSquareImageView;

/**
 * Created by hossein-pc on 6/9/2015.
 */
public class FragmentUserRegisterOfferFriendSMS extends Fragment implements IFragInviteSelectionListener {

    public static final String TAG="OfferFriendInviteFriend";

    int remainingSMSCount;
    AdapterInviteFriendsBySMS adapterInviteFriends;
    ListView listViewAllContacts;
    Hashtable<Integer,ContactEntry> selectedContactsToInvite;
    Hashtable<Integer,Integer> positionMapForSelectedContacts;
//    adapter
    ArrayList<ContactEntry> noneCharsooContactsList;
    LinearLayout selectedContactsLayout;
    ButtonFont sendSMS;


    HorizontalScrollView selectedScrollView;
    int selectedItemHeight,selectedItemMargin;
    LinearLayout.LayoutParams params;

//        TextViewFont persianLicenseTextView,englishLicenseTextView;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_register_offer_friends_invite_sms,
                container, false);
        if (selectedContactsToInvite==null)
            selectedContactsToInvite=new Hashtable<>();
        if (positionMapForSelectedContacts==null)
            positionMapForSelectedContacts=new Hashtable<>();

        if (noneCharsooContactsList==null) {
            if (adapterInviteFriends == null)
                adapterInviteFriends = new AdapterInviteFriendsBySMS(getActivity(), new ArrayList<ContactEntry>(), FragmentUserRegisterOfferFriendSMS.this);
        }
        else if (adapterInviteFriends==null)
                adapterInviteFriends=new AdapterInviteFriendsBySMS(getActivity(),noneCharsooContactsList,FragmentUserRegisterOfferFriendSMS.this);
        listViewAllContacts=(ListView) view.findViewById(R.id.allContacts);
        listViewAllContacts.setAdapter(adapterInviteFriends);
        selectedScrollView=(HorizontalScrollView) view.findViewById(R.id.selectedContacts);
        selectedContactsLayout=(LinearLayout) view.findViewById(R.id.ll_SelectedContactsContainer);

        sendSMS=(ButtonFont) view.findViewById(R.id.btn_sendSMS);


//        hasApplicationX=new Hashtable<>();
//        new GetInstalledApps(getActivity()).execute();
        remainingSMSCount=50;
        recursivelyCallHandler();

        selectedItemHeight =getSizeInPixelFromDp(38);
        selectedItemMargin=getSizeInPixelFromDp(16);
        params=new LinearLayout.LayoutParams(selectedItemHeight,selectedItemHeight);
        params.setMargins(0, 0, selectedItemMargin,0);
        return view;

    }

    Handler handler = new Handler();

    public void recursivelyCallHandler() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //We don't want to run all webservices together
                //first HomeFragment, second SearchFragment and last UserFragment

                if (!selectedContactsToInvite.isEmpty())
                {
                    int i=3;
                }
                recursivelyCallHandler();
            }
        }, 500);
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
    }


    public void getNoneCharsooContacts(ArrayList<ContactEntry> noneCharsooContacts){
        adapterInviteFriends.loadMore(noneCharsooContacts);
        noneCharsooContactsList=noneCharsooContacts;
    }

    @Override
    public void onItemCheckBoxClicked(int position) {
        if (selectedContactsToInvite.containsKey(position)){
            selectedContactsToInvite.remove(position);
            int removePosition=positionMapForSelectedContacts.get(position);
            selectedContactsLayout.removeViewAt(removePosition);
            for (int i:positionMapForSelectedContacts.keySet())
            {
                int tempInt=positionMapForSelectedContacts.get(i);
                if (tempInt>removePosition){
                    positionMapForSelectedContacts.put(i,tempInt-1);
                }
            }
            positionMapForSelectedContacts.remove(position);
        }
        else{
            selectedContactsToInvite.put(position,noneCharsooContactsList.get(position));
            positionMapForSelectedContacts.put(position,selectedContactsLayout.getChildCount());
            RoundedSquareImageView r=new RoundedSquareImageView(getActivity());

            r.setLayoutParams(params);
            if (selectedContactsToInvite.get(position).contactPhoto!=null){
                r.setImageBitmap(selectedContactsToInvite.get(position).contactPhoto);
            }
            else
            {
                r.setImageDrawable(selectedContactsToInvite.get(position).contactPhotoDrawable);
            }
            selectedContactsLayout.addView(r);
        }

        if (!selectedContactsToInvite.isEmpty()){
            selectedScrollView.setVisibility(View.VISIBLE);
            sendSMS.setVisibility(View.VISIBLE);

        }
        else
        {
            selectedScrollView.setVisibility(View.GONE);
            sendSMS.setVisibility(View.GONE);
        }
    }
//    private class GetInstalledApps extends AsyncTask<Void,Void,Void> {
//
//        ArrayList<PInfo> res;
//        Context context;
//
//        public GetInstalledApps(Context c){
//            this.context=c;
//        }
//
//        @Override
//        protected Void doInBackground(Void... params) {
//            res = new ArrayList<PInfo>();
//            List<PackageInfo> packs = context.getPackageManager().getInstalledPackages(0);
//            for(int i=0;i<packs.size();i++) {
//                PackageInfo p = packs.get(i);
//                /*if ((!getSysPackages) && (p.versionName == null)) {
//                    continue ;
//                }*/
//                PInfo newInfo = new PInfo();
//                newInfo.appname = p.applicationInfo.loadLabel(context.getPackageManager()).toString();
//                newInfo.pname = p.packageName;
//                newInfo.versionName = p.versionName;
//                newInfo.versionCode = p.versionCode;
//                newInfo.icon = p.applicationInfo.loadIcon(context.getPackageManager());
//                if ((newInfo.appname.equalsIgnoreCase(Params.WHATSAPP))||(newInfo.appname.equalsIgnoreCase(Params.LINE))||
//                        (newInfo.appname.equalsIgnoreCase(Params.VIBER))||(newInfo.appname.equalsIgnoreCase(Params.TELEGRAM))) {
//                    hasApplicationX.put(newInfo.appname,true);
//                    res.add(newInfo);
//                }
//            }
//
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Void result){
////            if (res!=null) {
////                for (PInfo p : res){
////                    p.prettyPrint();
////                }
////            }
//
//            for (String s: hasApplicationX.keySet()){
//
//            }
//        }
//    }
//
//    class PInfo {
//        private String appname = "";
//        private String pname = "";
//        private String versionName = "";
//        private int versionCode = 0;
//        private Drawable icon;
//        private void prettyPrint() {
//            Log.v(appname + "\t" + pname + "\t" + versionName + "\t" + versionCode, "");
//        }
//    }

    public int getSizeInPixelFromDp(int dpToConvert){
        Resources r = getResources();
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpToConvert, r.getDisplayMetrics());
        return (int) px;
    }
}
