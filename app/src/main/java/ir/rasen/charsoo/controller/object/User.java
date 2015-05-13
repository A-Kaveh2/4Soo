package ir.rasen.charsoo.controller.object;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import ir.rasen.charsoo.view.activity.ActivityUserOther;
import ir.rasen.charsoo.controller.helper.FriendshipRelation;
import ir.rasen.charsoo.controller.helper.Params;
import ir.rasen.charsoo.controller.helper.Permission;
import ir.rasen.charsoo.controller.helper.Sex;


/**
 * Created by A.Kaveh on 11/29/2014.
 */
public class User {

    public int id;
    public String userIdentifier;
    public String name;
    public String email;
    public boolean isEmailConfirmed;
    public String password;
    public String aboutMe;
    public Sex sex;
    public String birthDate;
    public String profilePicture;
    public int profilePictureId;
    public int coverPictureId;
    public String coverPicture;
    public Permission permissions;
    public FriendshipRelation.Status friendshipRelationStatus;
    public int friendRequestNumber;
    public int reviewsNumber;
    public int followedBusinessesNumber;
    public int friendsNumber;
    public ArrayList<UserBusinesses> businesses;




    public static class UserBusinesses{
        public int businessId;
        public String businessIdentifier;
        public UserBusinesses(int businessId,String businessIdentifier){
            this.businessId = businessId;
            this.businessIdentifier = businessIdentifier;
        }
        public UserBusinesses(){

        }
    }

    public static ArrayList<UserBusinesses> getUserBusinesses(JSONArray jsonArray)throws Exception {
        ArrayList<UserBusinesses> businesses = new ArrayList<>();

        for (int j = 0; j < jsonArray.length(); j++) {
            JSONObject jsonObject = jsonArray.getJSONObject(j);
            UserBusinesses userBusinesses = new UserBusinesses();
            userBusinesses.businessId = Integer.valueOf(jsonObject.getString(Params.BUSINESS_ID));
            userBusinesses.businessIdentifier = jsonObject.getString(Params.BUSINESS_USER_NAME);
            businesses.add(userBusinesses);
        }

        return businesses;
    }

    public static void goUserHomeInfoPage(Context context,int visitedUserId){
        Intent intent = new Intent(context, ActivityUserOther.class);
        intent.putExtra(Params.VISITED_USER_ID,visitedUserId);
        context.startActivity(intent);
    }

    public static void clearSearchHistory(Context context){
        Toast.makeText(context,"Clear Search History",Toast.LENGTH_LONG).show();
    }
}
