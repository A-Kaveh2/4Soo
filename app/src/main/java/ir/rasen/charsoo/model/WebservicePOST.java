package ir.rasen.charsoo.model;

import android.content.Context;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.util.ArrayList;

import ir.rasen.charsoo.controller.helper.NetworkHandler;
import ir.rasen.charsoo.controller.helper.ServerAnswer;
import ir.rasen.charsoo.controller.helper.WebservicesHandler;


/**
 * Created by android on 12/1/2014.
 */
public class WebservicePOST {
    HttpClient httpclient;
    HttpPost httpPost;
    JSONObject jsonParams;

    public WebservicePOST(String url) {
        httpclient = new DefaultHttpClient();
        httpPost = new HttpPost(url);
        jsonParams = new JSONObject();
    }

    public WebservicePOST(String url, ArrayList<String> paramsList) {
        httpclient = new DefaultHttpClient();
        if (paramsList != null) {
            for (String param : paramsList) {
                url += "/" + param;
            }
        }
        url = url.replaceAll ( " ", "%20" );
        httpPost = new HttpPost(url);
        jsonParams = new JSONObject();
    }

    public void addParam(String paramName, String paramValue) throws Exception {
        if (paramValue != null)
            jsonParams.put(paramName, paramValue);
        else
            jsonParams.put(paramName, "");
    }

    private HttpResponse run(HttpPost httpPost) throws Exception {
        HttpResponse httpResponse = null;
        //StringEntity params = new StringEntity(jsonParams.toString());
        StringEntity params = new StringEntity(jsonParams.toString(), "UTF-8");

        params.setChunked(true);
        httpPost.setEntity(params);
        //httpPost.setEntity(new ByteArrayEntity(params.toString().getBytes("UTF8")));
        httpPost.setHeader("Content-Type", "application/json");
        httpPost.setHeader("Accept", "application/json");

        try {
            httpResponse = httpclient.execute(httpPost);
        } catch (Exception e) {
            String s = e.getMessage();
        }

        return httpResponse;
    }

    public ServerAnswer execute(Context context) throws Exception {
        if (!NetworkHandler.isNetworkAvailable(context))
            return WebservicesHandler.getNetworkConnectionError();

        HttpResponse httpResponse = run(httpPost);
        return ServerAnswer.get(httpResponse);
    }

    public ServerAnswer executeList(Context context) throws Exception {
        if (!NetworkHandler.isNetworkAvailable(context))
            return WebservicesHandler.getNetworkConnectionError();

        HttpResponse httpResponse = run(httpPost);
        return ServerAnswer.getList(httpResponse);
    }


}
