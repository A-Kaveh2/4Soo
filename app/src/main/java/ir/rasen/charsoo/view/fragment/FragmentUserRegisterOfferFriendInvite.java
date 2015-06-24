package ir.rasen.charsoo.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import ir.rasen.charsoo.R;
import ir.rasen.charsoo.controller.helper.GetInstalledApps;
import ir.rasen.charsoo.controller.helper.Params;
import ir.rasen.charsoo.controller.object.PackageInfoCustom;
import ir.rasen.charsoo.view.adapter.AdapterInviteFriendsByApp;
import ir.rasen.charsoo.view.interface_m.IGetInstalledAppsListener;
import ir.rasen.charsoo.view.interface_m.IInviteFriendByAppListener;

/**
 * Created by hossein-pc on 6/9/2015.
 */
public class FragmentUserRegisterOfferFriendInvite extends Fragment implements IInviteFriendByAppListener,IGetInstalledAppsListener {

    public static final String TAG="OfferFriendInviteFriend";
    public static Context context;
//    adapter

//        TextViewFont persianLicenseTextView,englishLicenseTextView;
    ArrayList<PackageInfoCustom> applicationList;
    Hashtable<String,PackageInfoCustom> applicationsHashtable;
    ListView listViewApplication;
    AdapterInviteFriendsByApp listViewAdapter;
    LinearLayout linearLayoutProgressBar;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_user_register_offer_friends_invite,
                container, false);
        context=getActivity();
        listViewApplication =(ListView) view.findViewById(R.id.applicationsListView);
        linearLayoutProgressBar=(LinearLayout) view.findViewById(R.id.ll_ProgressBar);
        if (applicationList ==null) {
            listViewAdapter = new AdapterInviteFriendsByApp(getActivity(), new ArrayList<PackageInfoCustom>(),FragmentUserRegisterOfferFriendInvite.this);
            new GetInstalledApps(getActivity(),FragmentUserRegisterOfferFriendInvite.this).execute();
        }
        else {
            if (listViewAdapter==null)
                listViewAdapter = new AdapterInviteFriendsByApp(getActivity(), applicationList,FragmentUserRegisterOfferFriendInvite.this);
            linearLayoutProgressBar.setVisibility(View.GONE);
        }
        listViewApplication.setAdapter(listViewAdapter);
//        hasApplicationX=new Hashtable<>();
//        new GetInstalledApps(getActivity()).execute();

        return view;

    }

    @Override
    public void onItemClicked(String itemTag) {
        Intent myIntent;
        switch (itemTag)
        {
            case Params.EMAIL_APP:
                startActivity(createEmailIntent("","subject","Body"));
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

    public static Intent createEmailIntent(final String toEmail,
                                           final String subject,
                                           final String message)
    {
        Intent sendTo = new Intent(Intent.ACTION_SENDTO);
        String uriText = "mailto:" + Uri.encode(toEmail) +
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
        send.putExtra(Intent.EXTRA_EMAIL,
                new String[] { "" });
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

    @Override
    public void setAppResults(ArrayList<PackageInfoCustom> appList) {
        setApplicationList(appList);
    }

    private void doOnApplicationListReady(){
        if(listViewAdapter!=null)
            listViewAdapter.resetItems(applicationList);
        if (linearLayoutProgressBar!=null)
            linearLayoutProgressBar.setVisibility(View.GONE);
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


}
