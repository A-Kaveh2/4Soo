package ir.rasen.charsoo.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import ir.rasen.charsoo.R;
import ir.rasen.charsoo.controller.helper.GetInstalledApps;
import ir.rasen.charsoo.controller.helper.Params;
import ir.rasen.charsoo.controller.object.ContactEntry;
import ir.rasen.charsoo.controller.object.PackageInfoCustom;
import ir.rasen.charsoo.view.adapter.AdapterInviteFriendsByApp;
import ir.rasen.charsoo.view.adapter.AdapterInviteNoneCharsooContact;
import ir.rasen.charsoo.view.interface_m.IFragInviteSelectionListener;
import ir.rasen.charsoo.view.interface_m.IGetInstalledAppsListener;
import ir.rasen.charsoo.view.interface_m.IInviteFriendByAppListener;
import ir.rasen.charsoo.view.widgets.buttons.ButtonFont;
import ir.rasen.charsoo.view.widgets.imageviews.RoundedSquareImageView;

/**
 * Created by hossein-pc on 6/9/2015.
 */
public class FragmentUserRegisterOfferFriendInvite extends Fragment implements IInviteFriendByAppListener,IFragInviteSelectionListener {

    public static final String TAG="OfferFriendInviteFriend";
    public static Context context;
//    adapter



//        TextViewFont persianLicenseTextView,englishLicenseTextView;
    ArrayList<PackageInfoCustom> applicationList;
    Hashtable<String,PackageInfoCustom> applicationsHashtable;
    ListView listViewApplication,listViewEmailContacts;
    AdapterInviteFriendsByApp listViewAdapter;
    LinearLayout selectedContactsContainer;
    ArrayList<ContactEntry> noneCharsooContactList;
    AdapterInviteNoneCharsooContact noneCharsooEmailContactsAdapter;
    ButtonFont sendEmailButton;
    Hashtable<Integer,ContactEntry> selectedContactsToInvite;
    Hashtable<Integer,Integer> positionMapForSelectedContacts;
    HorizontalScrollView selectedContactsScrollView;
    int selectedItemHeight,selectedItemMargin;
    LinearLayout.LayoutParams params;


    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_user_register_offer_friends_invite,
                container, false);
        context=getActivity();
        listViewApplication =(ListView) view.findViewById(R.id.applicationsListView);
        listViewEmailContacts=(ListView) view.findViewById(R.id.allContactsListView);
        sendEmailButton=(ButtonFont) view.findViewById(R.id.btn_SendEmail);
        selectedContactsScrollView=(HorizontalScrollView) view.findViewById(R.id.selectedContactsScrollView);
        selectedContactsContainer=(LinearLayout) view.findViewById(R.id.ll_SelectedContactsContainer);
        sendEmailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendEmail();
            }
        });
//        ((ImageView) view.findViewById(R.id.imageView_close)).setImageBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.ic_close_black_48dp));
//        ((ImageView) view.findViewById(R.id.imageView_close)).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                (view.findViewById(R.id.ll_EmailListContainer)).setVisibility(View.GONE);
//            }
//        });
        /*if (applicationList ==null) {
            listViewAdapter = new AdapterInviteFriendsByApp(getActivity(), new ArrayList<PackageInfoCustom>(),,FragmentUserRegisterOfferFriendInvite.this);
            new GetInstalledApps(getActivity(),FragmentUserRegisterOfferFriendInvite.this).execute();
        }
        else {
            if (listViewAdapter==null)
                listViewAdapter = new AdapterInviteFriendsByApp(getActivity(), applicationList,FragmentUserRegisterOfferFriendInvite.this);
            linearLayoutProgressBar.setVisibility(View.GONE);
        }*/

        if (selectedContactsToInvite==null)
            selectedContactsToInvite=new Hashtable<>();
        if (positionMapForSelectedContacts==null)
            positionMapForSelectedContacts=new Hashtable<>();

        if ((applicationList!=null)&&(noneCharsooContactList!=null))
        {
            listViewAdapter=new AdapterInviteFriendsByApp(getActivity(),applicationList,noneCharsooContactList,FragmentUserRegisterOfferFriendInvite.this);
            listViewApplication.setAdapter(listViewAdapter);
        }
//        hasApplicationX=new Hashtable<>();
//        new GetInstalledApps(getActivity()).execute();
        selectedItemHeight =getSizeInPixelFromDp(38);
        selectedItemMargin=getSizeInPixelFromDp(16);
        params=new LinearLayout.LayoutParams(selectedItemHeight,selectedItemHeight);
        params.setMargins(0, 0, selectedItemMargin,0);

        return view;

    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        if (!selectedContactsToInvite.isEmpty())
        {
            selectedContactsScrollView.setVisibility(View.VISIBLE);
            positionMapForSelectedContacts=new Hashtable<>();
            ArrayList<Integer> keys=new ArrayList<>(selectedContactsToInvite.keySet());
            for (Integer i = 0; i < keys.size() ; i++) {
                positionMapForSelectedContacts.put(keys.get(i),i);
                RoundedSquareImageView r=new RoundedSquareImageView(getActivity());

                r.setLayoutParams(params);
                if (selectedContactsToInvite.get(keys.get(i)).contactPhoto!=null){
                    r.setImageBitmap(selectedContactsToInvite.get(keys.get(i)).contactPhoto);
                }
                selectedContactsContainer.addView(r);
            }

        }
    }

    @Override
    public void onItemClicked(String itemTag) {
        Intent myIntent;
        switch (itemTag)
        {
            case Params.EMAIL_APP:
                (view.findViewById(R.id.ll_EmailListContainer)).setVisibility(View.VISIBLE);
                if (noneCharsooEmailContactsAdapter==null) {
                    noneCharsooEmailContactsAdapter = new AdapterInviteNoneCharsooContact(getActivity(), noneCharsooContactList, FragmentUserRegisterOfferFriendInvite.this);
                    listViewEmailContacts.setAdapter(noneCharsooEmailContactsAdapter);
                }
                //                startActivity(createEmailIntent("","subject","Body"));
//                String[] s=new String[]{"mhfathi.charsoo@gmail.com","cemhfathi@gmail.com"};
////                composeEmail(s,"this is subject","this is body");
//                myIntent = new Intent(Intent.ACTION_SEND);
//                myIntent.setType("message/rfc822");
//                myIntent.putExtra(Intent.EXTRA_EMAIL, s);
//                myIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
//                myIntent.putExtra(Intent.EXTRA_TEXT, "I'm email body.");
//                startActivity(Intent.createChooser(myIntent, "Send Email"));
                break;
            case Params.SHARE_APP:
                myIntent = new Intent(Intent.ACTION_SEND);
                myIntent.setType("text/html");
                myIntent.putExtra(Intent.EXTRA_TEXT, "Test Message");//
                getActivity().startActivity(Intent.createChooser(myIntent, "Share with"));
                break;
            default:
                myIntent = new Intent(Intent.ACTION_SEND);
                myIntent.setType("text/plain");
                String s2= applicationsHashtable.get(itemTag).pname;
                myIntent.setPackage(s2);
                myIntent.putExtra(Intent.EXTRA_TEXT, "Test Message");//
                getActivity().startActivity(Intent.createChooser(myIntent, "Share with"));
                break;
        }
    }

    public static Intent createEmailIntent(final String[] toEmail,
                                           final String subject,
                                           final String message)
    {
        String emailsString="";
        if (toEmail.length>0)
            emailsString=toEmail[0];

        for (int i=1;i<toEmail.length;i++)
        {
            emailsString+=",";
            emailsString+=toEmail[i];
        }
        Intent sendTo = new Intent(Intent.ACTION_SENDTO);
        String uriText = "mailto:" + Uri.encode(emailsString) +
                "?subject=" + Uri.encode(subject) +
                "&body=" + Uri.encode(message);
        Uri uri = Uri.parse(uriText);
        sendTo.setData(uri);

        List<ResolveInfo> resolveInfos =context.getPackageManager().queryIntentActivities(sendTo, 0);

        // Emulators may not like this check...
        if (!resolveInfos.isEmpty())
        {
            return Intent.createChooser(sendTo,"Send Email");
        }

        // Nothing resolves send to, so fallback to send...
        Intent send = new Intent(Intent.ACTION_SEND);

        send.setType("message/rfc822");
        send.putExtra(Intent.EXTRA_EMAIL,toEmail);
        send.putExtra(Intent.EXTRA_SUBJECT, subject);
        send.putExtra(Intent.EXTRA_TEXT, message);

        return Intent.createChooser(send, "Send Email");
    }


//    public void composeEmail(String[] addresses, String subject,String body) {
//        Intent intent = new Intent(Intent.ACTION_SEND);
//        intent.setData(Uri.parse("mailto:")); // only email appNames should handle this
//        intent.putExtra(Intent.EXTRA_EMAIL, addresses);
//        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
//        intent.putExtra(Intent.EXTRA_TEXT,body);
//        startActivity(Intent.createChooser(intent, "mail to"));
//    }

//    @Override
//    public void setAppResults(ArrayList<PackageInfoCustom> appList) {
//        setApplicationList(appList);
//    }

    private void doOnApplicationListReady(){
        if(listViewAdapter!=null)
            listViewAdapter.resetItems(applicationList);

        if (view !=null) {
            if ((applicationList != null) && (noneCharsooContactList != null)) {
                listViewAdapter = new AdapterInviteFriendsByApp(getActivity(), applicationList, noneCharsooContactList, FragmentUserRegisterOfferFriendInvite.this);
                listViewApplication.setAdapter(listViewAdapter);
            }
        }
    }

    public void setApplicationList(ArrayList<PackageInfoCustom> appList){
        if (applicationList==null){
            applicationList =appList;
            applicationsHashtable=new Hashtable<>();
            for (int i = 0; i < applicationList.size(); i++) {
                applicationsHashtable.put(applicationList.get(i).appname, applicationList.get(i));
            }
            doOnApplicationListReady();
        }
    }

    public void setNoneCharsooContacts(ArrayList<ContactEntry> noneCharsooContacts){
        noneCharsooContactList=new ArrayList<>(noneCharsooContacts);
        if (view !=null) {
            if ((applicationList != null) && (noneCharsooContactList != null)) {
                listViewAdapter = new AdapterInviteFriendsByApp(getActivity(), applicationList, noneCharsooContactList, FragmentUserRegisterOfferFriendInvite.this);
                listViewApplication.setAdapter(listViewAdapter);
            }
        }
    }

    @Override
    public void onItemCheckBoxClicked(int position) {
        if (selectedContactsToInvite.containsKey(position)){
            selectedContactsToInvite.remove(position);
            int removePosition=positionMapForSelectedContacts.get(position);
            selectedContactsContainer.removeViewAt(removePosition);
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
            selectedContactsToInvite.put(position,noneCharsooContactList.get(position));
            positionMapForSelectedContacts.put(position,selectedContactsContainer.getChildCount());
            RoundedSquareImageView r=new RoundedSquareImageView(getActivity());

            r.setLayoutParams(params);
            if (selectedContactsToInvite.get(position).contactPhoto!=null){
                r.setImageBitmap(selectedContactsToInvite.get(position).contactPhoto);
            }
            else
            {
//                r.setImageDrawable(selectedContactsToInvite.get(position).contactPhotoDrawable);

            }
            selectedContactsContainer.addView(r);
        }

        if (!selectedContactsToInvite.isEmpty()){
            selectedContactsScrollView.setVisibility(View.VISIBLE);

        }
        else
        {
            selectedContactsScrollView.setVisibility(View.GONE);

        }
    }


    private void sendEmail(){
        int size=selectedContactsToInvite.size();
        String[] emailsStringArray=new String[size];
        ArrayList<Integer> keys=new ArrayList<>(selectedContactsToInvite.keySet());
        for (int i=0 ; i<size;i++){
            emailsStringArray[i]=selectedContactsToInvite.get(keys.get(i)).contactData;
        }
        startActivity(createEmailIntent(emailsStringArray,"subject","Body"));
    }
    
    public int getSizeInPixelFromDp(int dpToConvert){
        Resources r = getResources();
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpToConvert, r.getDisplayMetrics());
        return (int) px;
    }


    public boolean isEmailListVisible(){
        if (view!=null){
                return (view.findViewById(R.id.ll_EmailListContainer)).getVisibility()==View.VISIBLE;
        }
        else
            return false;
    }

    public void hideEmailList(){
        (view.findViewById(R.id.ll_EmailListContainer)).setVisibility(View.GONE);
    }

}
