package ir.rasen.charsoo.controller.helper;

/**
 * Created by android on 3/16/2015.
 */
public class WebservicesHandler {
    public  enum Webservices {NONE,LOGIN,FORGET_PASSWORD,GET_USER_HOME_INFO,GET_USER_SHARED_POSTS,GET_BUSINESS_CATEGORY,GET_BUSINESS_SUB_CATEGORY,ADD_POST,GET_POST,UPDATE_POST};

    public  Webservices currentWebservice = Webservices.NONE;

    public static ServerAnswer getNetworkConnectionError(){
        ServerAnswer serverAnswer = new ServerAnswer();
        serverAnswer.setSuccessStatus(false);
        serverAnswer.setResult(null);
        serverAnswer.setErrorCode(ServerAnswer.NETWORK_CONNECTION_ERROR);
        return serverAnswer;
    }
}
