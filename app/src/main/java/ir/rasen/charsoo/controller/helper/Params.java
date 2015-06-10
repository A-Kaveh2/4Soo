package ir.rasen.charsoo.controller.helper;

public class Params {

    /**
     * Created by 'SINA KH'.
     */
    public static String USER_USERNAME_VALIDATION = "[0-9a-zA-Z._]*";
    public static int USER_USERNAME_MIN_LENGTH = 4;
    public static int USER_USERNAME_MAX_LENGTH = 15;
    public static String USER_NAME_VALIDATION = "[0-9a-zA-Z._ \\u0600-\\u06FF\\uFB8A\\u067E\\u0686\\u06AF]*";
    public static String CITY_VALIDATION = "[\\u0600-\\u06FF\\uFB8A\\u067E\\u0686\\u06AF]*";
    public static int USER_NAME_MIN_LENGTH = 3;
    public static int ABOUT_ME_MIN_LENGTH = 3;
    public static int USER_NAME_MAX_LENGTH = 25;
    public static int ABOUT_ME_MAX_LENGTH = 100;
    public static int USER_PASSWORD_MIN_LENGTH = 5;
    public static int BUSINESS_DESCRIPTION_MIN_LENGTH = 5;
    public static int COMMENT_TEXT_MIN_LENGTH = 3;
    public static int COMMENT_TEXT_MAX_LENGTH = 255;
    public static String TEXT_HASHTAG_VALIDATION = "[0-9a-zA-Z_\\u0600-\\u06FF\\uFB8A\\u067E\\u0686\\u06AF]*";
    public static int HASHTAG_MIN_LENGTH = 2;
    public static String NUMERIC_VALIDATION = "[0-9]*";
    public static String WELCOME_PAGE = "welcome_page";

    public static String BUSINESS = "business";
    public static String SEARCH_KEY_WORD = "searchKeyWord";

    public static int HOME = 0;
    public static int SEARCH = 1;
    public static int PROFILE = 2;
    public static String PROFILE_TYPE = "profile_type";
    public static String PROFILE_OWN = "profile_own";

    public static class ProfileType {
        public static int PROFILE_USER = 0;
        public static int PROFILE_BUSINESS = 1;
    }

    public static String HOME_TYPE = "home_type";
    public static class HomeType {
        public static int HOME_HOME = 0;
        public static int HOME_SEARCH = 1;
        public static int HOME_POST = 2;
    }
    public static String BUSINESS_OWNER = "business_owner";
    public static String USER_FRIEND_REQUESTS = "user_friend_requests";

    /*public static class ActionBarSensitivity {
        public static int TO_HIDE = 100;
        public static int TO_SHOW = 50;
    }*/

    public static String SET_LOCATION_TYPE = "set_location_type";
    public static String SEARCH_TYPE = "search_type";
    public static class SearchType {
        public static int BUSINESSES = 0;
        public static int PRODUCTS = 1;
    }
    public static int INTENT_LOCATION = 5;
    public static int INTENT_WORK_TIME = 6;
    public static int INTENT_ERROR = -1;
    public static int INTENT_OK = 1;

    public static String WORK_TIME_OPEN_HOUR= "work_time_open_hour";
    public static String WORK_TIME_OPEN_MINUTE= "work_time_open_minute";
    public static String WORK_TIME_CLOSE_HOUR= "work_time_close_hour";
    public static String WORK_TIME_CLOSE_MINUTE= "work_time_close_minute";

    public static String EDIT_MODE = "edit_mode";
    public static String EDIT_POST= "edit_post";
    public static String REGISTER_MODE = "register_mode";

    public static int LOCATION_REFRESH = 60000;
    public static int LOCATION_REFRESH_DISTANCE = 10;

    public static String CLOSED_BEFORE_RESPONSE = "closed before webservice response...";

    public static String NOTIFICATION = "notification";
    public static String NEW_FIREND= "newFriend";
    /**
     * Created by android on 12/15/2014.
     */
    public static String EMAIL = "Email";
    public static String PASSWORD = "Password";
    public static String PASSWORD_NEW = "PasswordNew";
    public static String USER_ID_INT = "UserId";
    public static String FRIEND_USER_ID_INT = "FriendUser_Id";
    public static String USER_UNDER_LINE_ID = "User_Id";
    public static String USERS_BUSINESS = "userBusiness";
    public static String USERS_SEEN_NOTIFICATIONS_PREFERENCE_NAME ="userSeenNotifications";
    public static String NOTIFICATION_ID = "NotificationId";
    public static String USER_IDENTIFIER = "UserIdentifier";
    public static String USER_ID_STRING = "UserName";
    public static String FRIEND_USER_ID_STRING = "FriendUserId";
    public static String NAME = "Name";
    public static String ABOUT_ME = "AboutMe";
    public static String SEX = "Sex";
    public static String BIRTH_DATE = "BirthDate";
    public static String PROFILE_PICTURE = "ProfilePicture";
    public static String COVER_PICTURE = "CoverPicture";
    public static String COVER_PICTURE_ID = "CoverPictureId";
    public static String FRIEND_REQUEST_NUMBER = "FriendRequestNumber";
    public static String REVIEWS_NUMBER = "ReviewsNumber";
    public static String FOLLOWED_BUSINESSES_NUMBER = "FollowedBusinessNumber";
    public static String FRIENDS_NUMBER = "FriendsNumber";
    public static String PERMISSION = "Permission";
    public static String FOLLOWED_BUSINESS = "FollowedBusinesses";
    public static String FRIENDS = "Friends";
    public static String REVIEWS = "Reviews";
    public static String FRIENDSHIP_RELATION_STATUS = "FriendshipRelationStatus";
    public static String FRIENDSHIP_RELATION_STATUS_CODE = "FriendshipRelationStatusCode";
    public static String POST_PICTURE_STRING = "Picture";
    public static String PICTURE_ID = "PictureId";
    public static String SEARCH_PICTURE_ID = "SearchPictureId";
    public static String POST_TITLE_STRING = "Title";
    public static String PERMISSION_FOLLOWED_BUSINESSES = "PermissionFollowedBusinesses";
    public static String PERMISSION_FRIENDS = "PermissionFriends";
    public static String PERMISSION_REVIEWS = "PermissionReviews";
    public static String FROM_INDEX = "FromIndex";
    public static String UNTIL_INDEX = "UntilIndex";
    public static String ID = "Id";
    public static String CREATION_DATAE_DATE = "CreationDate";
    public static String PICTURE_SMALL= "PictureSmall";
    public static String DESCRIPTION_STRING = "Description";
    public static String POST_DESCRIPTION_STRING = "Description";

    public static String POST_PRICE_STRING = "Price";
    public static String POST_CODE_STRING = "Code";
    public static String Post_COMMENTS_STRING = "Comments";
    public static String HASHTAG_LIST= "HashTagList";
    public static String POST_ID_INT = "PostId";
    public static String POST_TYPE= "PostType";
    public static String POST_OWNER_BUSINESS_ID= "PostOwnerBusinessId";
    public static String POST_PICTURE_ID_INT = "PostPictureId";
    public static String TEXT= "Text";
    public static String APPLICATOR_USER_ID= "ApplicatorId";
    public static String REQUESTED_USER_ID= "RequestedId";
    public static String ANSWER= "Answer";
    public static String BUSINESS_ID_STRING = "BusinessId";
    public static String BUSINESS_ID_INT = "Business_Id";
    public static String BUSINESS_IDENTIFIER= "BusinessIdentifier";
    public static String BUSINESS_USERNAME_STRING = "BusinessUserName";
    public static String BUSINESS_NAME="BusinessName";//mhfathi
    public static String REVIEW_ID= "ReviewId";
    public static String SEARCH_TEXT= "SearchText";
    public static String LOCATION_LATITUDE= "LocationLatitude";
    public static String LATITUDE= "Latitude";
    public static String LOCATION_LONGITUDE= "LocationLongitude";
    public static String LONGITUDE= "Longitude";
    public static String NEAR_BY= "NearBy";
    public static String CATEGORY= "Category";
    public static String SUBCATEGORY= "SubCategory";
    public static String WORK_DAYS= "WorkDays";
    public static String PHONE= "Phone";
    public static String ADDRESS= "Address";
    public static String MOBILE= "Mobile";
    public static String FOLLOWERS_NUMBER= "FollowersNumber";
    public static String IS_FOLLOWING= "IsFollowing";
    public static String WORK_TIME_OPEN= "WorkTimeOpen";
    public static String WORK_TIME_CLOSE= "WorkTimeClose";
    public static String COMMENT_ID= "CommentId";
    public static String COMMENT= "Comment";
    public static String RATE= "Rate";
    public static String REVIEW= "Review";
    public static String STATE= "State";
    public static String CITY= "City";
    public static String IS_EMAIL_COMFIRMED= "IsEmailConfirmed";
    public static String POST_IS_LIKED = "IsLiked";
    public static String POST_IS_REPORTED = "IsReported";
    public static String POST_IS_SHARED = "IsShared";
    public static String LIKE_NUMBER= "LikeNumber";
    public static String SHARE_NUMBER= "ShareNumber";
    public static String COMMENT_NUMBER= "CommentNumber";
    public static String SUCCESS = "SuccessStatus";
    public static String RESULT = "Result";
    public static String ERROR = "Error";
    public static String POST_PICUTE = "PostPicture";
    public static String USER_PICUTE = "UserPicture";
    public static String USER_NAME_STRING = "UserName";
    public static String USER_PROFILE_PICTURE_ID= "UserProfilePictureId";
    public static String USER_PROFILE_PICTURE= "UserProfilePicture";
    public static String BUSINESS_PICUTE = "BusinessPicture";
    public static String BUSINESS_PICUTE_ID = "BusinessPictureId";
    public static String BUSINESS_PROFILE_PICUTE_ID_INT = "BusinessProfilePictureId";
    public static String INTERVAL_TIME = "IntervalTime";
    public static String ERROR_CODE = "ErrorCode";
    public static String ACCESS_TOKEN = "AccessToken";
    public static String IMAGE = "Image";
    public static String CATEGORY_ID = "CategoryId";
    public static String SUB_CATEGORY_ID = "SubCategoryId";
    public static String BUSINESSES = "Businesses";
    public static String PROFILE_PICTURE_ID = "ProfilePictureId";
    public static String DISTANCE = "Distance";
    public static String DISCOUNT = "Discount";
    public static String TYPE = "PostTypeId";
    public static String FILE_PATH = "FilePath";
    public static String WEBSITE = "Website";
    public static String CELLPHONE= "Cellphone";
    public static String IS_EDITTING = "isEditting";
    public static String VISITED_USER_ID = "visitedUserId";
    public static String VISITOR_USER_ID = "visitorUserId";
    public static String HAS_REQUEST = "hasRequest";
    public static String IS_OWNER = "isOwner";
    public static String UPATE_TIME_LINE = "updateTimeLine";
    public static String UPDATE_TIME_LINE_POST_LAST_THREE_COMMENTS = "updateTimeLinePostLastThreeComments";
    public static String CANCEL_USER_SHARE_POST = "cancelUserSharePost";
    public static String UPDATE_TIME_LINE_TYPE = "updateTimeLineType";
    public static String REMOVE_REQUEST_ANNOUNCEMENT = "removeRequestAnnouncement";
    public static String UPDATE_USER_PROFILE_PCITURE = "updateUserProfilePicture";
    public static final String UPATE_TIME_LINE_TYPE_SHARE = "updateTimeLineTypeShare";
    public static final String UPATE_TIME_LINE_TYPE_CANCEL_SHARE = "updateTimeLineTypeCancelShare";
    public static final String DELETE_POST_FROM_ACTIVITY = "deletePostFromActivity";
    public static final String DELETE_BUSINESS= "deleteBusiness";
    public static final String HAS_REMAINIG_FRIEND_REQUESTS_STRING="hasRemainingFriendRequests";
    public static final String NETWORK_IS_CONNECTED = "networkIsConnected";
    public static final String NETWORK_IS_DISCONNECTED ="networkIsDisconnected";
    public static final String NETWORK_STATE ="networkState";
    public static final String DO_ON_NETWORK_STATE_CHANGE="doOnNetworkStateChange";

    //Actions
    public static int ACTION_ADD_NEW_BUSIENSS_SUCCESS = 1001;
    public static int ACTION_USER_EDIT_PROFILE = 1002;
    public static int ACTION_DELETE_BUSIENSS = 1003;
    public static int ACTION_WORK_TIME = 1004;
    public static int ACTION_REGISTER_BUSINESS = 1005;
    public static int ACTION_ADD_POST = 1006;
    public static int ACTION_UPDATE_USER_PROFILE = 1007;
    public static int ACTION_CHOOSE_LOCATION = 1008;
    public static int ACTION_EDIT_BUSINESS = 1009;
    public static int ACTION_ACTIVITY_POST = 1010;
    public static int ACTION_EDIT_POST = 1011;
    public static int ACTION_NEW_FRIENDS = 1012;



    public static enum UpdateTimeLineType{SHARE,CANCEL_SHARE,LIKE,CANCEL_LIKE,REPORT,ADD_COMMENT,DELETE_COMMENT,UPDATE_COMMENT};


    /**
     * Created by MHFathi
     */
    public static final boolean isTestVersion=true;

}
