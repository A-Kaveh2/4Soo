package ir.rasen.charsoo.controller.object;

import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;

import ir.rasen.charsoo.view.activity.ActivityBusinessOther;
import ir.rasen.charsoo.controller.helper.Location_M;
import ir.rasen.charsoo.controller.helper.Params;
import ir.rasen.charsoo.controller.helper.WorkTime;


/**
 * Created by android on 12/1/2014.
 */
public class Business {
    public int id;
    public String businessIdentifier;
    public int userID;
    public String name;
    public String coverPicture;
    public int coverPictureId;
    public String profilePicture;
    public int profilePictureId;
    public String category;
    public String subcategory;
    public int categoryID;
    public int subCategoryID;
    public String description;
    public WorkTime workTime = new WorkTime();
    public String phone;
    public String state;
    public String city;
    public String address;
    public Location_M location_m = new Location_M();
    public String email;
    public String mobile;
    public String webSite;
    public ArrayList<String> hashtagList = new ArrayList<String>();
    public int reviewsNumber;
    public int followersNumber;
    public boolean isFollowing;
    public float rate;

    public static enum ChangeType{EDIT,DELETE};

    public static void goBusinessHomeInfoPage(Context context,int businessId) {
        Intent intent = new Intent(context, ActivityBusinessOther.class);
        intent.putExtra(Params.BUSINESS_ID,businessId);
        context.startActivity(intent);
    }



}
