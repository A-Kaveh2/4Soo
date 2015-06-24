package ir.rasen.charsoo.model.friend;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;

import ir.rasen.charsoo.controller.helper.Hashtag;
import ir.rasen.charsoo.controller.helper.LoginInfo;
import ir.rasen.charsoo.controller.helper.Params;
import ir.rasen.charsoo.controller.helper.ServerAnswer;
import ir.rasen.charsoo.controller.helper.URLs;
import ir.rasen.charsoo.controller.object.Business;
import ir.rasen.charsoo.controller.object.ContactEntry;
import ir.rasen.charsoo.controller.object.Post;
import ir.rasen.charsoo.controller.object.User;
import ir.rasen.charsoo.model.WebservicePOST;
import ir.rasen.charsoo.view.interface_m.IWebserviceResponse;


/**
 * Created by android on 12/16/2014.
 */
public class TakeContactList extends AsyncTask<Void, Void, ArrayList<ContactEntry>> {
    private static final String TAG = "TakeContactList";

    private IWebserviceResponse delegate = null;
    private int userIntId;
    private ServerAnswer serverAnswer;
    private Context context;
    private Hashtable<String,ArrayList<ContactEntry>> contactsWithNumber,contactsWithEmail;


    public TakeContactList(Context context, int userIntId,Hashtable<String,ArrayList<ContactEntry>> phoneNumberContacts,Hashtable<String,ArrayList<ContactEntry>> emailContacts, IWebserviceResponse delegate) {
        this.userIntId=userIntId;
        contactsWithNumber=phoneNumberContacts;
        contactsWithEmail=emailContacts;
        this.delegate = delegate;
        this.context = context;
    }

    @Override
    protected ArrayList<ContactEntry> doInBackground(Void... voids) {
        ArrayList<User> seenUserList,unseenUserList,result;
        WebservicePOST webservicePOST = new WebservicePOST(URLs.TAKE_CONTACT_LIST,
                new ArrayList<>(Arrays.asList(String.valueOf(userIntId))));



        try {
            ArrayList<String> phoneNumberList=new ArrayList<>(),EmailList=new ArrayList<>();

            String emails="",phoneNumbers="";
            for (String str:contactsWithNumber.keySet())
            {
                for (int i=0;i<contactsWithNumber.get(str).size();i++){
                    phoneNumbers+=contactsWithNumber.get(str).get(i).contactData;
                    phoneNumbers+=",";
                }

            }
            for (String str:contactsWithEmail.keySet())
            {
                for (int i = 0; i < contactsWithEmail.get(str).size() ; i++) {
                    emails+=contactsWithEmail.get(str).get(i).contactData;
                    emails+=",";
                }

            }
            emails=emails.substring(0,emails.length()-1);
            phoneNumbers=phoneNumbers.substring(0,phoneNumbers.length()-1);

           /* JSONArray jArray=new JSONArray();
            for(String str:phoneNumbers){
                JSONObject jObject=new JSONObject();
                jObject.put(Params.DATA_TYPE,Params.PHONE_NUMBER);
                jObject.put(Params.DATA_VALUE,str);
                jArray.put(jObject);
            }

            for(String str:emailAddresses){
                JSONObject jObject=new JSONObject();
                jObject.put(Params.DATA_TYPE,Params.EMAIL_FOR_TAKECONTACTLIST);
                jObject.put(Params.DATA_VALUE,str);
                jArray.put(jObject);
            }*/

            webservicePOST.addParam(Params.EMAILS,emails);
            webservicePOST.addParam(Params.PHONE_NUMBERS,phoneNumbers);


//            serverAnswer = webservicePOST.execute(context);
            // TODO: yek Arayeye Sort shode tahvil bedim.
            return null;
//            if (serverAnswer.getSuccessStatus()) {
//                JSONArray jsonArray = serverAnswer.getResultList();
//                seenUserList=new ArrayList<>();
//                unseenUserList=new ArrayList<>();
//                for (int i = 0; i < jsonArray.length(); i++) {
//                    JSONObject jsonObject = jsonArray.getJSONObject(i);
//                    if (jsonObject.getBoolean(Params.IS_USER_SEEN_BOOL)){
//                        if (jsonObject.getString(Params.MATCHED_FIELD_TYPE).equals(Params.EMAIL_FOR_TAKECONTACTLIST)){
//                            User user=new User();
////                            user.id
//                        }
//                        else{
//
//                        }
//                    }
//                    else
//                    {
//
//                    }
//
//                }
//
//            } else
//                return 0;
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            serverAnswer = null;
        }
        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<ContactEntry> result) {

        delegate.getResult(new ArrayList<ContactEntry>());

//        if (serverAnswer == null) {
//            delegate.getError(ServerAnswer.EXECUTION_ERROR,TAG);
//            return;
//        }
//        if (serverAnswer.getSuccessStatus())
//            delegate.getResult(result);
//        else
//            delegate.getError(serverAnswer.getErrorCode(),TAG);
    }
}
