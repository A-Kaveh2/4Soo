package ir.rasen.charsoo.model;

import android.content.Context;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.util.ArrayList;

import ir.rasen.charsoo.controller.helper.NetworkHandler;
import ir.rasen.charsoo.controller.helper.ServerAnswer;
import ir.rasen.charsoo.controller.helper.WebservicesHandler;


/**
 * Created by android on 12/1/2014.
 */
public class WebserviceGET {
    HttpClient httpclient;
    HttpGet httpGet;


    public WebserviceGET(String url, ArrayList<String> paramsList) {
        httpclient = new DefaultHttpClient();
        if (paramsList != null) {
            for (String param : paramsList) {
                url += "/" + param;
            }
        }
        url = url.replaceAll ( " ", "%20" );
       /* try {
            url = URLEncoder.encode(url, "UTF-8");
        }
        catch (Exception e){
            String s = e.getMessage();
        }*/
        httpGet = new HttpGet(url);

    }


    public ServerAnswer execute(Context context) throws Exception {
        if (!NetworkHandler.isNetworkAvailable(context))
            return WebservicesHandler.getNetworkConnectionError();

        HttpResponse httpResponse = null;
        try {
            httpResponse = httpclient.execute(httpGet);
        } catch (Exception e) {
            String s = e.getMessage();
        }
        return ServerAnswer.get(httpResponse);
    }

    public ServerAnswer executeList(Context context) throws Exception {
        if (!NetworkHandler.isNetworkAvailable(context))
            return WebservicesHandler.getNetworkConnectionError();

        HttpResponse httpResponse = null;
        try {
            Toast.makeText(context,"executelist",Toast.LENGTH_LONG);
            httpResponse = httpclient.execute(httpGet);

        } catch (Exception e) {
            Toast.makeText(context,e.getMessage(),Toast.LENGTH_LONG).show();
        }
        return ServerAnswer.getList(httpResponse);
    }


}
