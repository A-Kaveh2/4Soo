package ir.rasen.charsoo.controller.helper;


import org.json.JSONObject;

public class Permission {

    public boolean followedBusiness = true;
    public boolean friends= true;
    public boolean reviews = true;

    public Permission(boolean followedBusiness,boolean friends,boolean reviews){
        this.followedBusiness = followedBusiness;
        this.friends = friends;
        this.reviews = reviews;
    }
    public Permission(){
    }
    public void getFromJsonObject(JSONObject jsonObject) throws Exception {
        followedBusiness = jsonObject.getBoolean(Params.FOLLOWED_BUSINESS);
        friends = jsonObject.getBoolean(Params.FRIENDS);
        reviews = jsonObject.getBoolean(Params.REVIEWS);
    }
}
