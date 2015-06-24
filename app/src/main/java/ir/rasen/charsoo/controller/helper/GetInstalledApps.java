package ir.rasen.charsoo.controller.helper;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import ir.rasen.charsoo.R;
import ir.rasen.charsoo.controller.object.PackageInfoCustom;
import ir.rasen.charsoo.view.interface_m.IGetInstalledAppsListener;

/**
 * Created by hossein-pc on 6/23/2015.
 */
public class GetInstalledApps extends AsyncTask<Void,Void,Void> {

    Context context;
    IGetInstalledAppsListener delegate;
    ArrayList<PackageInfoCustom> applicationList;

    public GetInstalledApps(Context c,IGetInstalledAppsListener delegate){
        this.context=c;
        this.delegate=delegate;
        applicationList=new ArrayList<>();
    }

    @Override
    protected Void doInBackground(Void... params) {

        PackageInfoCustom customPackage=new PackageInfoCustom();
        customPackage.appname=Params.EMAIL_APP;
        customPackage.icon=BitmapFactory.decodeResource(context.getResources(),R.drawable.ic_email_blue);
        applicationList.add(customPackage);


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
                applicationList.add(newInfo);
            }
            else if (newInfo.appname.equalsIgnoreCase(Params.LINE))
            {
                newInfo.appname=Params.LINE;
                applicationList.add(newInfo);
            }
            else if (newInfo.appname.equalsIgnoreCase(Params.VIBER))
            {
                newInfo.appname=Params.VIBER;
                applicationList.add(newInfo);
            }
            else if (newInfo.appname.equalsIgnoreCase(Params.TELEGRAM))
            {
                newInfo.appname=Params.TELEGRAM;
                applicationList.add(newInfo);
            }

        }
        customPackage=new PackageInfoCustom();
        customPackage.appname= Params.SHARE_APP;
        customPackage.icon= BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_share_black_48dp);
        applicationList.add(customPackage);

        return null;
    }

    @Override
    protected void onPostExecute(Void result){
        delegate.setAppResults(applicationList);
//            if (res!=null) {
//                for (PInfo p : res){
//                    p.prettyPrint();
//                }
//            }

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