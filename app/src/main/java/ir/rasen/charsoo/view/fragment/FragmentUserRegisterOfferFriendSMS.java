package ir.rasen.charsoo.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Hashtable;

import ir.rasen.charsoo.R;
import ir.rasen.charsoo.controller.object.ContactEntry;
import ir.rasen.charsoo.view.adapter.AdapterInviteNoneCharsooContact;
import ir.rasen.charsoo.view.interface_m.IFragInviteSelectionListener;
import ir.rasen.charsoo.view.widgets.buttons.ButtonFont;
import ir.rasen.charsoo.view.widgets.imageviews.RoundedSquareImageView;

/**
 * Created by hossein-pc on 6/9/2015.
 */
public class FragmentUserRegisterOfferFriendSMS extends Fragment implements IFragInviteSelectionListener {

    public static final String TAG="OfferFriendInviteFriend";

    public String freeSMSButtonText, chargeSMSButtonText;
    public String smsText;


    int remainingSMSCount=5;
    AdapterInviteNoneCharsooContact adapterInviteFriends;
    ListView listViewAllContacts;
    Hashtable<Integer,ContactEntry> selectedContactsToInvite;
    Hashtable<Integer,Integer> positionMapForSelectedContacts;
    //    adapter
    ArrayList<ContactEntry> noneCharsooContactsList;
    LinearLayout selectedContactsLayout;
    ButtonFont sendSMS;
    LinearLayout linearLayoutSendButtonContainer;
    HorizontalScrollView selectedScrollView;
    int selectedItemHeight,selectedItemMargin;
    LinearLayout.LayoutParams params;


//        TextViewFont persianLicenseTextView,englishLicenseTextView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_register_offer_friends_invite_sms,
                container, false);
        initialStrings(getActivity());
        linearLayoutSendButtonContainer=(LinearLayout) view.findViewById(R.id.ll_SendButtonContainer);
        if (selectedContactsToInvite==null)
            selectedContactsToInvite=new Hashtable<>();

        if (positionMapForSelectedContacts==null)
            positionMapForSelectedContacts=new Hashtable<>();

        if (noneCharsooContactsList==null) {
            if (adapterInviteFriends == null)
                adapterInviteFriends = new AdapterInviteNoneCharsooContact(getActivity(), new ArrayList<ContactEntry>(), FragmentUserRegisterOfferFriendSMS.this);
        }
        else{
            if (adapterInviteFriends==null)
                adapterInviteFriends = new AdapterInviteNoneCharsooContact(getActivity(), noneCharsooContactsList, FragmentUserRegisterOfferFriendSMS.this);
        }

        selectedScrollView=(HorizontalScrollView) view.findViewById(R.id.selectedContacts);
        selectedContactsLayout=(LinearLayout) view.findViewById(R.id.ll_SelectedContactsContainer);

        sendSMS=(ButtonFont) view.findViewById(R.id.btn_sendSMS);
        sendSMS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doSendSMS();
            }
        });


//        hasApplicationX=new Hashtable<>();
//        new GetInstalledApps(getActivity()).execute();
        if (remainingSMSCount==0){
            // TODO: try getting remaining sms count from server
        }

        selectedItemHeight =getSizeInPixelFromDp(38);
        selectedItemMargin=getSizeInPixelFromDp(16);
        params=new LinearLayout.LayoutParams(selectedItemHeight,selectedItemHeight);
        params.setMargins(0, 0, selectedItemMargin,0);


        return view;

    }



    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
//        if (noneCharsooContactsList!=null)
//            adapterInviteFriends.loadMore(noneCharsooContactsList);
        listViewAllContacts=(ListView) view.findViewById(R.id.allContacts);
        listViewAllContacts.setAdapter(adapterInviteFriends);

        if (!selectedContactsToInvite.isEmpty())
        {
            selectedScrollView.setVisibility(View.VISIBLE);
            sendSMS.setVisibility(View.VISIBLE);
            linearLayoutSendButtonContainer.setVisibility(View.VISIBLE);
            positionMapForSelectedContacts=new Hashtable<>();
            ArrayList<Integer> keys=new ArrayList<>(selectedContactsToInvite.keySet());
            for (Integer i = 0; i < keys.size() ; i++) {
                positionMapForSelectedContacts.put(keys.get(i),i);
                RoundedSquareImageView r=new RoundedSquareImageView(getActivity());

                r.setLayoutParams(params);
                if (selectedContactsToInvite.get(keys.get(i)).contactPhoto!=null){
                    r.setImageBitmap(selectedContactsToInvite.get(keys.get(i)).contactPhoto);
                }
                selectedContactsLayout.addView(r);
            }

            if (selectedContactsToInvite.size()<=remainingSMSCount){
                sendSMS.setText(freeSMSButtonText + "(" + Integer.toString(remainingSMSCount - selectedContactsToInvite.size()) + ")");
            }
            else{
                sendSMS.setText(chargeSMSButtonText);
            }
        }
    }

    private void initialStrings(Context context){
        freeSMSButtonText=context.getString(R.string.txt_SendFreeSMS);
        chargeSMSButtonText=context.getString(R.string.txt_SendSMSPayamak);
        smsText=context.getString(R.string.txt_SMSBodyText);
    }

    private void doSendSMS(){
        if (selectedContactsToInvite.size()>0) {
            if (selectedContactsToInvite.size() <= remainingSMSCount) {
                // TODO: SEND SMS REQUEST TO SERVER
            } else {
                String numbers;
                ArrayList<Integer> keys=new ArrayList<>(selectedContactsToInvite.keySet());
                numbers=selectedContactsToInvite.get(keys.get(0)).contactData;
                for (int i=1;i<keys.size();i++){
                    numbers +=";";
                    numbers += selectedContactsToInvite.get(keys.get(i)).contactData;
                }
                Intent smsIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:"+numbers));
                smsIntent.putExtra("sms_body", "Body of Message");
                startActivity(smsIntent);
            }
        }
    }

    public void setNoneCharsooContacts(ArrayList<ContactEntry> noneCharsooContacts){
        noneCharsooContactsList=new ArrayList<>(noneCharsooContacts);
        if (adapterInviteFriends!=null){
//            if (adapterInviteFriends.getCount()<=0)
            adapterInviteFriends.resetItems(noneCharsooContactsList);
        }

    }


    private void addNewlySelectedContact(){

    }

    private void removeSelectedContact(int itemPosition){
        selectedContactsToInvite.remove(itemPosition);
        int removePosition=positionMapForSelectedContacts.get(itemPosition);
        selectedContactsLayout.removeViewAt(removePosition);
        for (int i:positionMapForSelectedContacts.keySet())
        {
            int tempInt=positionMapForSelectedContacts.get(i);
            if (tempInt>removePosition){
                positionMapForSelectedContacts.put(i,tempInt-1);
            }
        }
        positionMapForSelectedContacts.remove(itemPosition);

        if (selectedContactsToInvite.size()<=remainingSMSCount){
            sendSMS.setText(freeSMSButtonText + "(" + Integer.toString(remainingSMSCount - selectedContactsToInvite.size()) + ")");
        }
        else{
            sendSMS.setText(chargeSMSButtonText);
        }

        if (!selectedContactsToInvite.isEmpty()){
            selectedScrollView.setVisibility(View.VISIBLE);
            sendSMS.setVisibility(View.VISIBLE);
            linearLayoutSendButtonContainer.setVisibility(View.VISIBLE);
        }
        else
        {
            selectedScrollView.setVisibility(View.GONE);
            sendSMS.setVisibility(View.GONE);
            linearLayoutSendButtonContainer.setVisibility(View.GONE);
        }
    }
    @Override
    public void onItemCheckBoxClicked(final int position) {
        if (selectedContactsToInvite.containsKey(position)){
            removeSelectedContact(position);
        }
        else{
            selectedContactsToInvite.put(position,noneCharsooContactsList.get(position));
            positionMapForSelectedContacts.put(position,selectedContactsLayout.getChildCount());
            RoundedSquareImageView r=new RoundedSquareImageView(getActivity());

            r.setLayoutParams(params);
            r.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    adapterInviteFriends.setUncheckedViewAt(position);
                    removeSelectedContact(position);
                }
            });
            if (selectedContactsToInvite.get(position).contactPhoto!=null){
                r.setImageBitmap(selectedContactsToInvite.get(position).contactPhoto);
            }
            else
            {
//                r.setImageDrawable(selectedContactsToInvite.get(position).contactPhotoDrawable);

            }
            selectedContactsLayout.addView(r);
        }

        if (selectedContactsToInvite.size()<=remainingSMSCount){
            sendSMS.setText(freeSMSButtonText + "(" + Integer.toString(remainingSMSCount - selectedContactsToInvite.size()) + ")");
        }
        else{
            sendSMS.setText(chargeSMSButtonText);
        }

        if (!selectedContactsToInvite.isEmpty()){
            selectedScrollView.setVisibility(View.VISIBLE);
            sendSMS.setVisibility(View.VISIBLE);
            linearLayoutSendButtonContainer.setVisibility(View.VISIBLE);
        }
        else
        {
            selectedScrollView.setVisibility(View.GONE);
            sendSMS.setVisibility(View.GONE);
            linearLayoutSendButtonContainer.setVisibility(View.GONE);
        }
    }

    public int getSizeInPixelFromDp(int dpToConvert){
        Resources r = getResources();
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpToConvert, r.getDisplayMetrics());
        return (int) px;
    }


}
