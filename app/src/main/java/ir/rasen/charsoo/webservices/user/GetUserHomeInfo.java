package ir.rasen.charsoo.webservices.user;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.internal.id;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
;import ir.rasen.charsoo.classes.User;
import ir.rasen.charsoo.helper.FriendshipRelation;
import ir.rasen.charsoo.helper.Params;
import ir.rasen.charsoo.helper.Permission;
import ir.rasen.charsoo.helper.ServerAnswer;
import ir.rasen.charsoo.helper.TestUnit;
import ir.rasen.charsoo.helper.URLs;
import ir.rasen.charsoo.interfaces.IWebserviceResponse;
import ir.rasen.charsoo.webservices.WebserviceGET;


/**
 * Created by android on 12/16/2014.
 */
public class GetUserHomeInfo extends AsyncTask<Void, Void, User> {
    private static final String TAG = "GetUserHomeInfo";
    private IWebserviceResponse delegate = null;
    private int visitedUserID;
    private int visitorUserID;
    private ServerAnswer serverAnswer;
    private Context context;

    public GetUserHomeInfo(Context context,int visitedUserID, int visitorUserID, IWebserviceResponse delegate) {
        this.visitedUserID = visitedUserID;
        this.visitorUserID = visitorUserID;
        this.delegate = delegate;
        this.context = context;
    }

    @Override
    protected User doInBackground(Void... voids) {
        User user = new User();
       /* if(TestUnit.isTestingUserActivity) {
            serverAnswer = new ServerAnswer();
            serverAnswer.setSuccessStatus(true);
            return TestUnit.getUser();
        }
*/
        WebserviceGET webserviceGET = new WebserviceGET(URLs.GET_HOME_INFO, new ArrayList<>(
                Arrays.asList(String.valueOf(visitedUserID), String.valueOf(visitorUserID))));

        try {
            serverAnswer = webserviceGET.execute(context);

            if (serverAnswer.getSuccessStatus()) {
                JSONObject jsonObject = serverAnswer.getResult();
                user.id = visitedUserID;
                user.userIdentifier = jsonObject.getString(Params.USER_ID);
                user.name = jsonObject.getString(Params.NAME);
                user.aboutMe = jsonObject.getString(Params.ABOUT_ME);
                user.profilePictureId = jsonObject.getInt(Params.PROFILE_PICTURE_ID);
                user.coverPictureId = jsonObject.getInt(Params.COVER_PICTURE_ID);
                user.friendRequestNumber = jsonObject.getInt(Params.FRIEND_REQUEST_NUMBER);
                user.reviewsNumber = jsonObject.getInt(Params.REVIEWS_NUMBER);
                user.followedBusinessesNumber = jsonObject.getInt(Params.FOLLOWED_BUSINESSES_NUMBER);
                user.friendsNumber = jsonObject.getInt(Params.FRIENDS_NUMBER);

                Permission permission = new Permission();
                String per = jsonObject.getString(Params.PERMISSION);
                JSONObject jsonObjectPermission;
                if (!per.equals("null") && !per.equals("NULL")) {
                    jsonObjectPermission = new JSONObject(per);
                    permission.getFromJsonObject(jsonObjectPermission);
                }
                user.permissions = permission;
                user.friendshipRelationStatus = FriendshipRelation.getFromCode(jsonObject.getInt(Params.FRIENDSHIP_RELATION_STATUS_CODE));
                //user.friendshipRelationStatus = FriendshipRelation.Status.REQUEST_SENT;

                String busi = jsonObject.getString(Params.BUSINESSES);
                JSONArray busiArray;
                if (!busi.equals("null") && !busi.equals("NULL")) {
                    busiArray = new JSONArray(busi);
                    user.businesses = User.getUserBusinesses(busiArray);
                }
                return user;
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            serverAnswer = null;
        }
        return null;
    }

    @Override
    protected void onPostExecute(User result) {

        //if webservice.execute() throws exception
        if (serverAnswer == null) {
            delegate.getError(ServerAnswer.EXECUTION_ERROR);
            return;
        }
        if (serverAnswer.getSuccessStatus())
            delegate.getResult(result);
        else
            delegate.getError(serverAnswer.getErrorCode());
    }
}
