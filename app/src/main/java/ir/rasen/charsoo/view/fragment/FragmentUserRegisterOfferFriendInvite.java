package ir.rasen.charsoo.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import ir.rasen.charsoo.R;
import ir.rasen.charsoo.controller.helper.Image_M;
import ir.rasen.charsoo.controller.helper.Params;
import ir.rasen.charsoo.controller.object.PackageInfoCustom;
import ir.rasen.charsoo.view.adapter.AdapterInviteFriendsByApp;
import ir.rasen.charsoo.view.interface_m.IInviteFriendByAppListener;
import ir.rasen.charsoo.view.widgets.TextViewFont;
import ir.rasen.charsoo.view.widgets.imageviews.RoundedSquareImageView;

/**
 * Created by hossein-pc on 6/9/2015.
 */
public class FragmentUserRegisterOfferFriendInvite extends Fragment implements IInviteFriendByAppListener {

    public static final String TAG="OfferFriendInviteFriend";
    public static Context context;
//    adapter

//        TextViewFont persianLicenseTextView,englishLicenseTextView;

    public ArrayList<PackageInfoCustom> applications;
    ListView applicationList;
    AdapterInviteFriendsByApp listViewAdapter;
    Hashtable<String,PackageInfoCustom> apps;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_register_offer_friends_invite,
                container, false);
        context=getActivity();
        applicationList=(ListView) view.findViewById(R.id.applicationsListView);
        if (apps==null)
            apps=new Hashtable<>();
        if (applications==null) {
            applications = new ArrayList<>();
            listViewAdapter = new AdapterInviteFriendsByApp(getActivity(), new ArrayList<PackageInfoCustom>(),FragmentUserRegisterOfferFriendInvite.this);
            PackageInfoCustom emailP=new PackageInfoCustom();
            emailP.appname=Params.EMAIL_INVITE;
            emailP.icon=BitmapFactory.decodeResource(getResources(),R.drawable.ic_email_blue);
            applications.add(emailP);
        }
        else {
            if (listViewAdapter==null)
                listViewAdapter = new AdapterInviteFriendsByApp(getActivity(), applications,FragmentUserRegisterOfferFriendInvite.this);
        }
        applicationList.setAdapter(listViewAdapter);
//        hasApplicationX=new Hashtable<>();
//        new GetInstalledApps(getActivity()).execute();
        new GetInstalledApps(getActivity()).execute();
        return view;

    }

    @Override
    public void onItemClicked(String itemTag) {
        Intent myIntent;
        switch (itemTag)
        {
            case Params.EMAIL_INVITE:
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
            case Params.SHARE:
                myIntent = new Intent(Intent.ACTION_SEND);
                myIntent.setType("text/html");
                myIntent.putExtra(Intent.EXTRA_TEXT, "Test Message");//
                getActivity().startActivity(Intent.createChooser(myIntent, "Share with"));
                break;
            default:
                myIntent = new Intent(Intent.ACTION_SEND);
                myIntent.setType("text/plain");
                String s2=apps.get(itemTag).pname;
                myIntent.setPackage(s2);
                myIntent.putExtra(Intent.EXTRA_TEXT, "Test Message");//
                getActivity().startActivity(Intent.createChooser(myIntent, "Share with"));
                break;
        }



//        switch (itemTag){
//            case Params.TELEGRAM:
//                Intent myIntent = new Intent(Intent.ACTION_SEND);
//                myIntent.setType("text/plain");
//                String s=apps.get(itemTag).pname;
//                myIntent.setPackage(s);
//                myIntent.putExtra(Intent.EXTRA_TEXT, "Test Message");//
//                getActivity().startActivity(Intent.createChooser(myIntent, "Share with"));
//                break;
//        }
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


    public void composeEmail(String[] addresses, String subject,String body) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_EMAIL, addresses);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT,body);
        startActivity(Intent.createChooser(intent,"mail to"));
    }


    private class GetInstalledApps extends AsyncTask<Void,Void,Void> {

        Context context;

        public GetInstalledApps(Context c){
            this.context=c;
        }

        @Override
        protected Void doInBackground(Void... params) {
            List<PackageInfo> packs = context.getPackageManager().getInstalledPackages(0);
            for(int i=0;i<packs.size();i++) {
                PackageInfo p = packs.get(i);
                /*if ((!getSysPackages) && (p.versionName == null)) {
                    continue ;
                }*/
                PackageInfoCustom newInfo = new PackageInfoCustom();
                newInfo.appname = p.applicationInfo.loadLabel(context.getPackageManager()).toString();
                newInfo.pname = p.packageName;
                newInfo.versionName = p.versionName;
                newInfo.versionCode = p.versionCode;
                newInfo.icon = Image_M.drawableToBitmap(p.applicationInfo.loadIcon(context.getPackageManager()));

                if (newInfo.appname.equalsIgnoreCase(Params.WHATSAPP))
                {
                    newInfo.appname=Params.WHATSAPP;
                    applications.add(newInfo);
                    apps.put(newInfo.appname,newInfo);
                }
                else if (newInfo.appname.equalsIgnoreCase(Params.LINE))
                {
                    newInfo.appname=Params.LINE;
                    applications.add(newInfo);
                    apps.put(newInfo.appname, newInfo);
                }
                else if (newInfo.appname.equalsIgnoreCase(Params.VIBER))
                {
                    newInfo.appname=Params.VIBER;
                    applications.add(newInfo);
                    apps.put(newInfo.appname, newInfo);
                }
                else if (newInfo.appname.equalsIgnoreCase(Params.TELEGRAM))
                {
                    newInfo.appname=Params.TELEGRAM;
                    applications.add(newInfo);
                    apps.put(newInfo.appname, newInfo);
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result){
//            if (res!=null) {
//                for (PInfo p : res){
//                    p.prettyPrint();
//                }
//            }
            PackageInfoCustom shareP=new PackageInfoCustom();
            shareP.appname= Params.SHARE;
            shareP.icon= BitmapFactory.decodeResource(getResources(),R.drawable.ic_share_black_48dp);
            applications.add(shareP);

            listViewAdapter.loadMore(applications);

//            for (String s: applications.keySet()){
//                View view=LayoutInflater.from(context).inflate(R.layout.item_application_to_invite,null);
//                Holder holder=new Holder();
//                holder.imageViewAppIcone=(RoundedSquareImageView) view.findViewById(R.id.imageView_ApplicationIcone);
//                holder.textViewAppName=(TextViewFont) view.findViewById(R.id.textViewFont_ApplicationName);
//                holder.textViewAppName.setText(s);
//
//                holder.imageViewAppIcone.setImageBitmap(drawableToBitmap(applications.get(s).icon));
//                holder.TAG=s;
//            }
        }
    }











}
