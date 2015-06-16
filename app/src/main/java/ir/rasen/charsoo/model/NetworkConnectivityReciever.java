package ir.rasen.charsoo.model;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.content.LocalBroadcastManager;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Hashtable;

import ir.rasen.charsoo.controller.helper.Params;
import ir.rasen.charsoo.view.interface_m.NetworkStateChangeListener;

/**
 * Created by hossein-pc on 6/8/2015.
 */
public class NetworkConnectivityReciever extends BroadcastReceiver {

//    private static Activity activity;
    private static Hashtable<String ,NetworkStateChangeListener> listeners=new Hashtable<>();

//    public static void setActivity(Activity a){
//        activity=a;
//    }

    public static void setNetworkStateListener(String tag,NetworkStateChangeListener lsr){
        listeners.put(tag,lsr);

    }

    private void broadcastNetworkIsConnected(){
        ArrayList<NetworkStateChangeListener> tempArray=new ArrayList<>(listeners.values());
        for (NetworkStateChangeListener lsr: tempArray){
            lsr.doOnNetworkConnected();
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
//        Log.d(ConnectivityReceiver.class.getSimpleName(), "action: "
//                + intent.getAction());
        if ((haveNetworkConnection(context))/*&&(activity!=null)*/){
            broadcastNetworkIsConnected();
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
