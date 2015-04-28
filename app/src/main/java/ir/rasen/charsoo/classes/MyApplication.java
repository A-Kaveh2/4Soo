package ir.rasen.charsoo.classes;

import android.app.Application;

import java.util.ArrayList;

import ir.rasen.charsoo.helper.Alarm_M;
import ir.rasen.charsoo.helper.BaseAdapterItem;
import ir.rasen.charsoo.helper.NetworkHandler;
import ir.rasen.charsoo.helper.Permission;
import ir.rasen.charsoo.helper.WebservicesHandler;
import ir.rasen.charsoo.helper.WorkTime;

/**
 * Created by android on 3/15/2015.
 */
public class MyApplication extends Application {

    private WebservicesHandler webservicesHandler ;
    private Permission permission;
    public Business business;
    public WorkTime workTime;
    public Post post;
    public ArrayList<BaseAdapterItem> searchUserResult;
    public ArrayList<User.UserBusinesses> userBusinesses;
    public boolean isHomeCreated,isSearchCreated,isUserCreated;
    public ArrayList<Post> homePosts;


    @Override
    public void onCreate()
    {
        super.onCreate();
        webservicesHandler = new WebservicesHandler();
        webservicesHandler.currentWebservice = WebservicesHandler.Webservices.NONE;

        permission = new Permission(false,false,false);
        business = new Business();
        workTime = new WorkTime();
        post = new Post();
        searchUserResult = new ArrayList<>();
        userBusinesses = new ArrayList<>();
        Alarm_M alarm_m = new Alarm_M();
        alarm_m.set(getApplicationContext());

        isHomeCreated = false;
        isSearchCreated = false;
        isUserCreated = false;
        homePosts =new ArrayList<>();

    }

    public WebservicesHandler.Webservices getCurrentWebservice() {
        return webservicesHandler.currentWebservice;
    }

    public void setCurrentWebservice(WebservicesHandler.Webservices currentWebservice) {
        this.webservicesHandler.currentWebservice = currentWebservice;
    }

    public Permission getPermission(){
        return  this.permission;
    }

    public void setPermission(Permission permission){
        this.permission = permission;
    }

}
