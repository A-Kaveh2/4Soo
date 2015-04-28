package ir.rasen.charsoo.classes;

import android.content.Context;

import java.util.ArrayList;

import ir.rasen.charsoo.helper.LoginInfo;

/**
 * Created by android on 12/20/2014.
 */
public class Review {
    public int id;
    public int businessID;
    public String businessUserName;
    public int userID;
    public String userName;
    public String businessPicutre;
    public int businessPicutreId;
    public String userPicutre;
    public int userPicutreId;
    public String text;
    public float rate;

    public  Review(String userIdentifier,int userPicutreId,int rate,String text){
        this.userName = userIdentifier;
        this.userPicutreId = userPicutreId;
        this.rate = rate;
        this.text = text;
    }
    public Review(){

    }

    public static boolean submitBefore(Context context,ArrayList<Review> reviews){
        for (Review review : reviews)
            if(review.userID == LoginInfo.getUserId(context))
                return true;
        return false;


    }
}
