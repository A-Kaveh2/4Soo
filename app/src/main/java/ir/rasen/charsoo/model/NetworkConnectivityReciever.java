package ir.rasen.charsoo.model;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.content.LocalBroadcastManager;

import ir.rasen.charsoo.controller.helper.Params;

/**
 * Created by hossein-pc on 6/8/2015.
 */
public class NetworkConnectivityReciever extends BroadcastReceiver {

    private static Activity activity;

    public static void setActivity(Activity a){
        activity=a;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
//        Log.d(ConnectivityReceiver.class.getSimpleName(), "action: "
//                + intent.getAction());
        if ((haveNetworkConnection(context))&&(activity!=null)){
            Intent intentNetworkConnectionChanged = new Intent(Params.DO_ON_NETWORK_STATE_CHANGE);
            intentNetworkConnectionChanged.putExtra(Params.NETWORK_STATE, Params.NETWORK_IS_CONNECTED);
            LocalBroadcastManager.getInstance(activity).sendBroadcast(intentNetworkConnectionChanged);
        }
            //    listener.networkIsNowConnected();
        //IS_NETWORK_AVAILABLE this variable in your activities to check networkavailability.

    }


    private boolean haveNetworkConnection(Context context) {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager)   context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }
}
