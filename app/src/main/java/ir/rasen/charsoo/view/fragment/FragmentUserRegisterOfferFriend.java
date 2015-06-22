package ir.rasen.charsoo.view.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Hashtable;

import ir.rasen.charsoo.R;
import ir.rasen.charsoo.controller.helper.Image_M;
import ir.rasen.charsoo.controller.helper.Validation;
import ir.rasen.charsoo.controller.object.ContactEntry;
import ir.rasen.charsoo.model.friend.TakeContactList;
import ir.rasen.charsoo.view.activity.ActivityUserRegister;
import ir.rasen.charsoo.view.interface_m.IWebserviceResponse;
import ir.rasen.charsoo.view.widgets.CustomViewPager;
import ir.rasen.charsoo.view.widgets.SlidingTabLayout;


/**
 * Created by hossein-pc on 6/9/2015.
 */
public class FragmentUserRegisterOfferFriend extends Fragment {

    public static final String TAG="OfferFriend";

    FragmentUserRegisterOfferFriendAdd fragAdd;
    FragmentUserRegisterOfferFriendInvite fragInvite;
    FragmentUserRegisterOfferFriendSMS fragSMS;
//    static final String LOG_TAG = "FragmentContainer";
    public ArrayList<ContactEntry> noneCharsooEmailContactsList, noneCharsooPhoneNumberContactsList, charsooContactsList;
    ProgressDialog progressDialog;

    ColorGenerator generator ;
    TextDrawable drawable2;
    int drawableRadius;
    /**
     * A custom {@link android.support.v4.view.ViewPager} title strip which looks much like Tabs present in Android v4.0 and
     * above, but is designed to give continuous feedback to the user when scrolling.
     */
    private SlidingTabLayout mSlidingTabLayout;
    Context context;

    /**
     * A {@link android.support.v4.view.ViewPager} which will be used in conjunction with the {@link SlidingTabLayout} above.
     */
    private CustomViewPager mViewPager;

    private View fragAddView,fragInviteView,fragSMSView;
    /**
     * Inflates the {@link android.view.View} which will be displayed by this {@link android.support.v4.app.Fragment}, from the app's
     * resources.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        context=getActivity();
        new GetContactData(context).execute();
        fragAdd=new FragmentUserRegisterOfferFriendAdd();
        fragSMS=new FragmentUserRegisterOfferFriendSMS();
        fragInvite=new FragmentUserRegisterOfferFriendInvite();

        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(context.getResources().getString(R.string.please_wait));
        View view = inflater.inflate(R.layout.fragment_user_register_offer_friends,
                container, false);
//        resultFragView= getActivity().getLayoutInflater().inflate(R.layout.fragment_result,
//                container, false);
//        resultFragView.post(new Runnable() {
//            @Override
//            public void run() {
//                FragmentResultPicture.getInstance(getActivity(),resultFragView);
//            }
//        });
//        addtextFragView = getActivity().getLayoutInflater().inflate(R.layout.fragment_addtext,
//                container, false);
//        FragmentAddtext.getInstance(getActivity(), addtextFragView);
//        designFragView=getActivity().getLayoutInflater().inflate(R.layout.fragment_design,
//                container, false);
//        FragmentDesignPicture.getInstance(getActivity(),designFragView);
        drawableRadius=getSizeInPixelFromDp(12);
        generator= ColorGenerator.MATERIAL;
        return view;

    }

    // BEGIN_INCLUDE (fragment_onviewcreated)
    /**
     * This is called after the {@link #onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)} has finished.
     * Here we can pick out the {@link android.view.View}s we need to configure from the content view.
     *
     * We set the {@link android.support.v4.view.ViewPager}'s adapter to be an instance of {@link }. The
     * {@link SlidingTabLayout} is then given the {@link android.support.v4.view.ViewPager} so that it can populate itself.
     *
     * @param view View created in {@link #onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)}
     */
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // BEGIN_INCLUDE (setup_viewpager)
        // Get the ViewPager and set it's PagerAdapter so that it can display items
        progressDialog.show();
        mViewPager = (CustomViewPager) view.findViewById(R.id.viewpager);
        mViewPager.setAdapter(new ViewPagerAdapter(getFragmentManager()));
        mViewPager.setScrollDurationFactor(0.3);
        // END_INCLUDE (setup_viewpager)


        // BEGIN_INCLUDE (setup_slidingtablayout)
        // Give the SlidingTabLayout the ViewPager, this must be done AFTER the ViewPager has had
        // it's PagerAdapter set.
        mSlidingTabLayout = (SlidingTabLayout) view.findViewById(R.id.sliding_tabs);
        mSlidingTabLayout.setViewPager(mViewPager);
        // END_INCLUDE (setup_slidingtablayout)
    }
    // END_INCLUDE (fragment_onviewcreated)





    /**
     * The {@link android.support.v4.view.PagerAdapter} used to display pages in this sample.
     * The individual pages are simple and just display two lines of text. The important section of
     * this class is the {@link #getPageTitle(int)} method which controls what is displayed in the
     * {@link SlidingTabLayout}.
     */


    private class ViewPagerAdapter extends FragmentStatePagerAdapter {

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);

        }

        public Fragment getItem(int num) {
            if (num==0)
                return fragAdd;
            else if (num==1)
                return fragInvite;
            else
                return fragSMS;
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position){
                case 0:
                    return context.getString(R.string.txt_FriendRequest);
                case 1:
                    return context.getString(R.string.txt_InviteFriends);
                case 2:
                    return context.getString(R.string.txt_SendSMS);
                default:
                    return "";
            }
        }

    }


    private class GetContactData extends AsyncTask<Void,Void,Void> implements IWebserviceResponse {
        Hashtable<String,ArrayList<ContactEntry>> noneCharsooEmailContacts;
        Hashtable<String,ArrayList<ContactEntry>> noneCharsooPhoneContacts;
        Hashtable<String,ContactEntry> charsooContacts;
        Context context;

        public GetContactData(Context c){
            charsooContacts=new Hashtable<>();
            context=c;
        }

        @Override
        protected Void doInBackground(Void... params) {
            noneCharsooEmailContacts=getEmailContacts();
            noneCharsooPhoneContacts=getPhoneNumberContacts();
            new TakeContactList(context,((ActivityUserRegister)context).userIntId,noneCharsooPhoneContacts,
                    noneCharsooEmailContacts, GetContactData.this).execute();

            return null;
        }



        private Hashtable<String,ArrayList<ContactEntry>> getPhoneNumberContacts(){
            Hashtable<String,ArrayList<ContactEntry>> result=new Hashtable<>();
            Cursor phones = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
            phones.moveToFirst();

            do
            {
                ContactEntry contactEntry=new ContactEntry();
                contactEntry.type= ContactEntry.ContactType.PhoneNumber;
                contactEntry.fullName =phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                contactEntry.contactData = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                String image_uri=phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI));
                if (image_uri != null) {

                    try {
                        Bitmap bitmap = MediaStore.Images.Media
                                .getBitmap(context.getContentResolver(),
                                        Uri.parse(image_uri));
                        contactEntry.contactPhoto = bitmap.copy(Bitmap.Config.ARGB_8888,true);
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        String s=contactEntry.fullName.substring(0,1).toUpperCase();
                        if (Character.isDigit(s.charAt(0))){
                            s=null;
                            s="N";
                        }
                        try {
                            if (contactEntry.fullName.contains(" ")){
                                s+=contactEntry.fullName.substring(contactEntry.fullName.indexOf(" ")+1,contactEntry.fullName.indexOf(" ")+2).toUpperCase();
                            }
                        }catch (Exception ee){}

                        contactEntry.contactPhotoDrawable= TextDrawable.builder()
                                .buildRound(s, generator.getColor(s));
                    }
                }
                if (Validation.checkNumberIfIsMobile(contactEntry.contactData).isValid()){
                    if (result.containsKey(contactEntry.fullName)) {
                        boolean addIt = true;
                        for (int i = 0; i <result.get(contactEntry.fullName).size() ; i++) {
                            if (contactEntry.contactData.equals(result.get(contactEntry.fullName).get(i).contactData))
                                addIt=false;
                        }
                        if (addIt)
                            result.get(contactEntry.fullName).add(contactEntry);
                    }
                    else
                        result.put(contactEntry.fullName,new ArrayList<>(Arrays.asList(contactEntry)));
                }
            }while (phones.moveToNext());
            phones.close();
            return result;
        }

        private Hashtable<String,ArrayList<ContactEntry>> getEmailContacts(){
            Hashtable<String,ArrayList<ContactEntry>> result=new Hashtable<>();
            Cursor phones = context.getContentResolver().query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, null,null,null, null);
            phones.moveToFirst();
            do
            {
                ContactEntry contactEntry=new ContactEntry();
                contactEntry.type= ContactEntry.ContactType.Email;
                contactEntry.fullName=phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                contactEntry.contactData= phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS));
                String image_uri=phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI));
                if (image_uri != null) {

                    try {
                        Bitmap bitmap = MediaStore.Images.Media
                                .getBitmap(context.getContentResolver(),
                                        Uri.parse(image_uri));
                        contactEntry.contactPhoto = bitmap.copy(Bitmap.Config.ARGB_8888,true);
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        String s=contactEntry.fullName.substring(0,1).toUpperCase();
                        if (Character.isDigit(s.charAt(0))){
                            s=null;
                            s="N";
                        }
                        try {
                            if (contactEntry.fullName.contains(" ")){
                                s+=contactEntry.fullName.substring(contactEntry.fullName.indexOf(" ")+1,contactEntry.fullName.indexOf(" ")+2).toUpperCase();
                            }
                        }catch (Exception ee){}

                        contactEntry.contactPhotoDrawable= TextDrawable.builder()
                                .buildRound(s, generator.getColor(s));
                    }

                }
                if (Validation.validateEmail(context, contactEntry.contactData).isValid())
                {
                    if (result.containsKey(contactEntry.fullName)) {
                        boolean addIt = true;
                        for (int i = 0; i <result.get(contactEntry.fullName).size() ; i++) {
                            if (contactEntry.contactData.equals(result.get(contactEntry.fullName).get(i).contactData))
                                addIt=false;
                        }
                        if (addIt)
                            result.get(contactEntry.fullName).add(contactEntry);
                    }
                    else
                        result.put(contactEntry.fullName,new ArrayList<>(Arrays.asList(contactEntry)));
                }
            }while (phones.moveToNext());
            phones.close();
            return result;
        }

        @Override
        public void getResult(Object result) {
            if (result instanceof ArrayList)
            {
                ArrayList<ContactEntry> tempResult=(ArrayList<ContactEntry>) result;
                ContactEntry contactEntry;
                for (int i = 0; i < tempResult.size(); i++) {
                    contactEntry=tempResult.get(i);
                    if (contactEntry.type== ContactEntry.ContactType.Email){
                        noneCharsooEmailContacts.remove(contactEntry.contactData);
                    }
                    else if (contactEntry.type== ContactEntry.ContactType.PhoneNumber){
                        noneCharsooPhoneContacts.remove(contactEntry.contactData);
                    }
                }

                noneCharsooEmailContactsList=new ArrayList<>();
                noneCharsooPhoneNumberContactsList=new ArrayList<>();
                charsooContactsList=new ArrayList<>();

                ArrayList<String> fullNameList=new ArrayList<>(noneCharsooEmailContacts.keySet());
                Collections.sort(fullNameList);
                for (int i = 0; i < fullNameList.size() ; i++) {
                    for (int j = 0; j < noneCharsooEmailContacts.get(fullNameList.get(i)).size(); j++) {
                        noneCharsooEmailContactsList.add(noneCharsooEmailContacts.get(fullNameList.get(i)).get(j));
                    }
                }
                fullNameList=new ArrayList<>(noneCharsooPhoneContacts.keySet());
                Collections.sort(fullNameList);
                for (int i = 0; i < fullNameList.size() ; i++) {
                    for (int j = 0; j < noneCharsooPhoneContacts.get(fullNameList.get(i)).size(); j++) {
                        noneCharsooPhoneNumberContactsList.add(noneCharsooPhoneContacts.get(fullNameList.get(i)).get(j));
                    }
                }

                int i = noneCharsooEmailContactsList.size();
                i=noneCharsooPhoneNumberContactsList.size();
                charsooContactsList=tempResult;


                if (charsooContactsList.isEmpty()){
                    mViewPager.setCurrentItem(1,true);
                }
                fragAdd.getCharsooContacts(charsooContactsList);
                fragSMS.getNoneCharsooContacts(noneCharsooPhoneNumberContactsList);
                progressDialog.dismiss();
            }
        }

        @Override
        public void getError(Integer errorCode, String callerStringID) {

        }
    }

    public int getSizeInPixelFromDp(int dpToConvert){
        Resources r = getResources();
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpToConvert, r.getDisplayMetrics());
        return (int) px;
    }
}







