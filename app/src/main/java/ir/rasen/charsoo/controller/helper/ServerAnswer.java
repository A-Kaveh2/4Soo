package ir.rasen.charsoo.controller.helper;

import android.content.Context;

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import ir.rasen.charsoo.R;


public class ServerAnswer {
    private boolean success;
    private JSONObject result;
    private JSONArray resultList;
    private int errorCode;

    public static int NONE_DEFINED_ERROR = -3;
    public static int EXECUTION_ERROR = -2;
    public static int NONE_ERROR = -1;
    public static int NETWORK_CONNECTION_ERROR = -4;


    public static String getError(Context context, int errorCode,String callerStringID) {
        String errorPrefix=""; //mhfathi
        if (Params.isTestVersion) //mhfathi
        {
            errorPrefix ="Error Created By: "+callerStringID+" :";
        }

        switch (errorCode) {
            case -4:
                return errorPrefix+context.getResources().getString(R.string.err_network_connection);
            case -2:
                return errorPrefix+context.getResources().getString(R.string.err_execution);
            case -1:
                return errorPrefix+context.getResources().getString(R.string.err_none);
            case 0:
                return errorPrefix+context.getResources().getString(R.string.err_unknown);
            case 1:
                return errorPrefix+context.getResources().getString(R.string.err_database_connection);
            case 2:
                return errorPrefix+context.getResources().getString(R.string.err_user_dose_not_exist);
            case 3:
                return errorPrefix+context.getResources().getString(R.string.err_password_incorrect);
            case 4:
                return errorPrefix+context.getResources().getString(R.string.err_business_does_not_exist);
            case 5:
                return errorPrefix+context.getResources().getString(R.string.err_post_does_not_exist);
            case 6:
                return errorPrefix+context.getResources().getString(R.string.err_json_serialization);
            case 7:
                return errorPrefix+context.getResources().getString(R.string.err_further_development);
            case 8:
                return errorPrefix+context.getResources().getString(R.string.err_empty_post_request);
            case 9:
                return errorPrefix+context.getResources().getString(R.string.err_invalid_base64_string);
            case 10:
                return errorPrefix+context.getResources().getString(R.string.err_database_constraints_violation);
            case 11:
                return errorPrefix+context.getResources().getString(R.string.err_block_does_not_exist);
            case 12:
                return errorPrefix+context.getResources().getString(R.string.err_comment_does_not_exist);
            case 13:
                return errorPrefix+context.getResources().getString(R.string.err_follow_already_exist);
            case 14:
                return errorPrefix+context.getResources().getString(R.string.err_follow_does_not_exist);
            case 15:
                return errorPrefix+context.getResources().getString(R.string.err_friend_request_does_not_exist);
            case 16:
                return errorPrefix+context.getResources().getString(R.string.err_image_does_not_exist);
            case 17:
                return errorPrefix+context.getResources().getString(R.string.err_no_rate);
            case 18:
                return errorPrefix+context.getResources().getString(R.string.err_no_review);
            case 19:
                return errorPrefix+"repeated review";
            case 20:
                return errorPrefix+"repeated business identifier";
            case 21:
                return errorPrefix+"share doesn't exist";
            case 22:
                return errorPrefix+"user is blocked";

        }

        return "Undefined";
    }

    public boolean getSuccessStatus() {
        return success;
    }

    public void setSuccessStatus(boolean successStatus) {
        this.success = successStatus;
    }

    public JSONObject getResult() {
        return result;
    }

    public void setResult(JSONObject result) {
        this.result = result;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public JSONArray getResultList() {
        return resultList;
    }

    public void setResultList(JSONArray resultList) {
        this.resultList = resultList;
    }

    public static ServerAnswer getList(HttpResponse httpResponse) throws Exception {

        if (httpResponse == null) {
            ServerAnswer serverAnswer = new ServerAnswer();
            serverAnswer.setSuccessStatus(false);
            serverAnswer.setResult(null);
            serverAnswer.setErrorCode(EXECUTION_ERROR);

            return serverAnswer;
        }

        String responseBody = EntityUtils.toString(httpResponse.getEntity());
        JSONObject json = new JSONObject(responseBody);

        //get success status

        boolean sucessStatus = json.getBoolean(Params.SUCCESS);
        //get error code

        String error = json.getString(Params.ERROR);
        JSONObject errorJsonObject = null;
        if (error.length() > 4) {
            try {
                errorJsonObject = json.getJSONObject(Params.ERROR);
            } catch (Exception e) {

            }
        }

        int errorCode = NONE_DEFINED_ERROR;
        if (errorJsonObject != null) {
            try {
                if (!errorJsonObject.getString(Params.ERROR_CODE).equals("null"))
                    errorCode = Integer.valueOf(errorJsonObject.getString(Params.ERROR_CODE));
            } catch (Exception e) {

            }
        }

        //


        ServerAnswer serverAnswer = new ServerAnswer();
        serverAnswer.setSuccessStatus(sucessStatus);
        if (sucessStatus == true && json.getString(Params.RESULT).length() <= 2) {
            serverAnswer.setResultList(new JSONArray());
        } else {
            try {
                serverAnswer.setResultList(new JSONArray(json.getString(Params.RESULT)));
            } catch (Exception e) {

            }
        }
        serverAnswer.setErrorCode(errorCode);

        return serverAnswer;
    }

    public static ServerAnswer get(HttpResponse httpResponse) throws Exception {

        if (httpResponse == null) {
            ServerAnswer serverAnswer = new ServerAnswer();
            serverAnswer.setSuccessStatus(false);
            serverAnswer.setResult(null);
            serverAnswer.setErrorCode(EXECUTION_ERROR);

            return serverAnswer;
        }
        String responseBody = EntityUtils.toString(httpResponse.getEntity());
        JSONObject json = new JSONObject(responseBody);

        //get success status
        boolean sucessStatus = json.getBoolean(Params.SUCCESS);

        //get result
        String resultString = json.getString(Params.RESULT);
        JSONObject resultJsonObject = null;
        try {
            resultJsonObject = new JSONObject(resultString);
        } catch (Exception e) {

        }


        //get error code

        String error = json.getString(Params.ERROR);
        JSONObject errorJsonObject = null;
        if (error.length() > 4) {
            try {
                errorJsonObject = json.getJSONObject(Params.ERROR);
            } catch (Exception e) {

            }
        }

        int errorCode = NONE_DEFINED_ERROR;
        if (errorJsonObject != null) {
            try {
                errorCode = Integer.valueOf(errorJsonObject.getString(Params.ERROR_CODE));
            } catch (Exception e) {

            }
        }

        if (!(resultString.equals("True") || resultString.equals("False"))) {
            if (sucessStatus == true && resultJsonObject == null)
                sucessStatus = false;
        }


        ServerAnswer serverAnswer = new ServerAnswer();
        serverAnswer.setSuccessStatus(sucessStatus);
        serverAnswer.setResult(resultJsonObject);
        serverAnswer.setErrorCode(errorCode);

        return serverAnswer;
    }
}
