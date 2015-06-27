package ir.rasen.charsoo.view.fragment;


import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import ir.rasen.charsoo.R;
import ir.rasen.charsoo.controller.helper.GetInstalledApps;
import ir.rasen.charsoo.controller.object.ContactEntry;
import ir.rasen.charsoo.controller.object.PackageInfoCustom;
import ir.rasen.charsoo.model.GetContactData;
import ir.rasen.charsoo.view.interface_m.IGetContactListener;
import ir.rasen.charsoo.view.interface_m.IGetInstalledAppsListener;
import ir.rasen.charsoo.view.widgets.CustomViewPager;
import ir.rasen.charsoo.view.widgets.SlidingTabLayout;
import ir.rasen.charsoo.view.widgets.WaitDialog;


/**
 * Created by hossein-pc on 6/9/2015.
 */
public class FragmentUserRegisterOfferFriend extends Fragment implements IGetContactListener,IGetInstalledAppsListener {

    public static final String TAG="OfferFriend";

    public String tabOneTitle,tabTwoTitle,tabThreeTitle;


    FragmentUserRegisterOfferFriendAdd fragAdd;
    FragmentUserRegisterOfferFriendInvite fragInvite;
    FragmentUserRegisterOfferFriendSMS fragSMS;
    //    static final String LOG_TAG = "FragmentContainer";
    public ArrayList<ContactEntry> noneCharsooEmailContactsList, noneCharsooPhoneNumberContactsList, charsooContactsList;
    ArrayList<PackageInfoCustom> applicationList;
    WaitDialog progressDialog;

    boolean haveContactList=false;
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

    View view;
    private View fragAddView,fragInviteView,fragSMSView;
    /**
     * Inflates the {@link android.view.View} which will be displayed by this {@link android.support.v4.app.Fragment}, from the app's
     * resources.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        context=getActivity();
        progressDialog = new WaitDialog(context);
        progressDialog.setMessage(context.getResources().getString(R.string.please_wait));
        fragAdd=new FragmentUserRegisterOfferFriendAdd();
        fragSMS=new FragmentUserRegisterOfferFriendSMS();
        fragInvite=new FragmentUserRegisterOfferFriendInvite();

        initialStrings();



        view = inflater.inflate(R.layout.fragment_user_register_offer_friends,
                container, false);
//        progressDialog.show();

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
        if (applicationList!=null) {
            fragInvite.setApplicationList(applicationList);
        }else
            new GetInstalledApps(getActivity(),FragmentUserRegisterOfferFriend.this);

        if (charsooContactsList==null) {
            new GetContactData(context,FragmentUserRegisterOfferFriend.this).execute();
        }
        else
            doOnContactListReady();

        if ((applicationList!=null) && (charsooContactsList!=null))
            (view.findViewById(R.id.ll_ProgressBar)).setVisibility(View.GONE);

    }

    private void initialStrings(){
        tabOneTitle=context.getString(R.string.txt_FindFriends);
        tabTwoTitle=context.getString(R.string.txt_InviteFriends);
        tabThreeTitle=context.getString(R.string.txt_sendSMS);
    }

    @Override
    public void getContacts(ArrayList<ContactEntry> charsooContacts, ArrayList<ContactEntry> noneCharsooPhoneContacts, ArrayList<ContactEntry> noneCharsooEmailContacts) {
        //        if (!haveContactList){
//            charsooContactsList=charsooContacts;
//            noneCharsooEmailContactsList=noneCharsooEmailContacts;
//            noneCharsooPhoneNumberContactsList=noneCharsooPhoneContacts;
//            haveContactList=true;
//        }
//        doOnContactListReady();
        setContactLists(charsooContacts,noneCharsooPhoneContacts,noneCharsooEmailContacts);

    }

    public void doOnContactListReady(){
        if (view!=null){
            if (charsooContactsList.isEmpty()){
                mViewPager.setCurrentItem(1,true);
            }

            fragAdd.setCharsooContacts(charsooContactsList);
            fragSMS.setNoneCharsooContacts(noneCharsooPhoneNumberContactsList);
            fragInvite.setNoneCharsooContacts(noneCharsooEmailContactsList);
            if (applicationList!=null)
                (view.findViewById(R.id.ll_ProgressBar)).setVisibility(View.GONE);
        }
//        progressDialog.dismiss();
    }

    @Override
    public void setAppResults(ArrayList<PackageInfoCustom> appList)  {
        setApplicationList(appList);
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
                    return tabOneTitle;
                case 1:
                    return tabTwoTitle;
                case 2:
                    return tabThreeTitle;
                default:
                    return "";
            }
        }

    }




    public int getSizeInPixelFromDp(int dpToConvert){
        Resources r = getResources();
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpToConvert, r.getDisplayMetrics());
        return (int) px;
    }

    public void setContactLists(ArrayList<ContactEntry> charsooContacts, ArrayList<ContactEntry> noneCharsooPhoneContacts, ArrayList<ContactEntry> noneCharsooEmailContacts){
//        if (!haveContactList){
//            charsooContactsList=charsooContacts;
//            noneCharsooEmailContactsList=noneCharsooEmailContacts;
//            noneCharsooPhoneNumberContactsList=noneCharsooPhoneContacts;
//            haveContactList=true;
//        }
//        doOnContactListReady();
        if (charsooContactsList==null){
            charsooContactsList=charsooContacts;
            noneCharsooEmailContactsList=noneCharsooEmailContacts;
            noneCharsooPhoneNumberContactsList=noneCharsooPhoneContacts;
            haveContactList=true;
            doOnContactListReady();

        }
    }

    public void setApplicationList(ArrayList<PackageInfoCustom> appList){
        applicationList=appList;
        if (fragInvite!=null){
            fragInvite.setApplicationList(applicationList);
        }
    }

    public boolean isEmailListVisible(){
        if (fragInvite!=null)
            return fragInvite.isEmailListVisible();
        else
            return false;
    }

    public void hideEmailList(){
        if (fragInvite!=null){
            fragInvite.hideEmailList();
        }
    }
}







