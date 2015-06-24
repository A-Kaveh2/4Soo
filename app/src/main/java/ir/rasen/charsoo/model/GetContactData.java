package ir.rasen.charsoo.model;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.util.TypedValue;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Hashtable;

import ir.rasen.charsoo.controller.helper.Image_M;
import ir.rasen.charsoo.controller.helper.Params;
import ir.rasen.charsoo.controller.helper.Validation;
import ir.rasen.charsoo.controller.object.ContactEntry;
import ir.rasen.charsoo.model.friend.TakeContactList;
import ir.rasen.charsoo.view.interface_m.IGetContactListener;
import ir.rasen.charsoo.view.interface_m.IWebserviceResponse;

/**
 * Created by hossein-pc on 6/23/2015.
 */
public class GetContactData extends AsyncTask<Void,Void,Void> implements IWebserviceResponse {
    Hashtable<String,ArrayList<ContactEntry>> noneCharsooEmailContacts;
    Hashtable<String,ArrayList<ContactEntry>> noneCharsooPhoneContacts;
    Hashtable<String,ContactEntry> charsooContacts;
    Context context;
    IGetContactListener delegate;
    public ArrayList<ContactEntry> noneCharsooEmailContactsList, noneCharsooPhoneNumberContactsList, charsooContactsList;
    ColorGenerator generator ;
    TextDrawable drawable2;
    int drawableWidthHeight;


    public GetContactData(Context c,IGetContactListener delegate){
        this.delegate=delegate;
        charsooContacts=new Hashtable<>();
        context=c;
        generator= ColorGenerator.MATERIAL;
        drawableWidthHeight=getSizeInPixelFromDp(Params.PROFILE_ROUNDED_THUMBNAIL_DIP);
    }

    @Override
    protected Void doInBackground(Void... params) {
        noneCharsooEmailContacts=getEmailContacts();
        noneCharsooPhoneContacts=getPhoneNumberContacts();

        // TODO: mechanisme zamanbari ast, barresi shavad
        new TakeContactList(context,1/*((ActivityUserRegister)context).userIntId*/,noneCharsooPhoneContacts,
                noneCharsooEmailContacts, GetContactData.this).execute();

        return null;
    }



    private Hashtable<String,ArrayList<ContactEntry>> getPhoneNumberContacts(){
        Hashtable<String,ArrayList<ContactEntry>> result=new Hashtable<>();
        Cursor phones = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        phones.moveToFirst();

        do
        {
            ContactEntry contactEntry=new ContactEntry();
            contactEntry.type= ContactEntry.ContactType.PhoneNumber;
            contactEntry.fullName =phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            contactEntry.contactData = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            String image_uri=phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI));
            try {
                Bitmap bitmap = MediaStore.Images.Media
                        .getBitmap(context.getContentResolver(),
                                Uri.parse(image_uri));
                contactEntry.contactPhoto = bitmap.copy(Bitmap.Config.ARGB_8888,true);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                String s=contactEntry.fullName.substring(0,1).toUpperCase();
                if (Character.isDigit(s.charAt(0))){
                    s=null;
                    s="N";
                }
                try {
                    if (contactEntry.fullName.contains(" ")){
                        s+=contactEntry.fullName.substring(contactEntry.fullName.indexOf(" ")+1,contactEntry.fullName.indexOf(" ")+2).toUpperCase();
                    }
                }catch (Exception ee){}

                contactEntry.contactPhoto= Image_M.drawableToBitmap(TextDrawable.builder()
                        .beginConfig()
                        .width(drawableWidthHeight)  // width in px
                        .height(drawableWidthHeight) // height in px
                        .endConfig().buildRound(s, generator.getColor(s)));
            }
            if (Validation.checkNumberIfIsMobile(contactEntry.contactData).isValid()){
                if (result.containsKey(contactEntry.fullName)) {
                    boolean addIt = true;
                    for (int i = 0; i <result.get(contactEntry.fullName).size() ; i++) {
                        if (contactEntry.contactData.equals(result.get(contactEntry.fullName).get(i).contactData))
                            addIt=false;
                    }
                    if (addIt)
                        result.get(contactEntry.fullName).add(contactEntry);
                }
                else
                    result.put(contactEntry.fullName,new ArrayList<>(Arrays.asList(contactEntry)));
            }
        }while (phones.moveToNext());
        phones.close();
        return result;
    }

    private Hashtable<String,ArrayList<ContactEntry>> getEmailContacts(){
        Hashtable<String,ArrayList<ContactEntry>> result=new Hashtable<>();
        Cursor phones = context.getContentResolver().query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, null,null,null, null);
        phones.moveToFirst();
        do
        {
            ContactEntry contactEntry=new ContactEntry();
            contactEntry.type= ContactEntry.ContactType.Email;
            contactEntry.fullName=phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            contactEntry.contactData= phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS));
            String image_uri=phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI));
            try {
                Bitmap bitmap = MediaStore.Images.Media
                        .getBitmap(context.getContentResolver(),
                                Uri.parse(image_uri));
                contactEntry.contactPhoto = bitmap.copy(Bitmap.Config.ARGB_8888,true);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                String s=contactEntry.fullName.substring(0,1).toUpperCase();
                if (Character.isDigit(s.charAt(0))){
                    s=null;
                    s="N";
                }
                try {
                    if (contactEntry.fullName.contains(" ")){
                        s+=contactEntry.fullName.substring(contactEntry.fullName.indexOf(" ")+1,contactEntry.fullName.indexOf(" ")+2).toUpperCase();
                    }
                }catch (Exception ee){}

                contactEntry.contactPhoto= Image_M.drawableToBitmap(TextDrawable.builder()
                        .beginConfig()
                        .width(drawableWidthHeight)  // width in px
                        .height(drawableWidthHeight) // height in px
                        .endConfig().buildRound(s, generator.getColor(s)));
            }

            if (Validation.validateEmail(context, contactEntry.contactData).isValid())
            {
                if (result.containsKey(contactEntry.fullName)) {
                    boolean addIt = true;
                    for (int i = 0; i <result.get(contactEntry.fullName).size() ; i++) {
                        if (contactEntry.contactData.equals(result.get(contactEntry.fullName).get(i).contactData))
                            addIt=false;
                    }
                    if (addIt)
                        result.get(contactEntry.fullName).add(contactEntry);
                }
                else
                    result.put(contactEntry.fullName,new ArrayList<>(Arrays.asList(contactEntry)));
            }
        }while (phones.moveToNext());
        phones.close();
        return result;
    }

    @Override
    public void getResult(Object result) {
        if (result instanceof ArrayList)
        {
            ArrayList<ContactEntry> tempResult=(ArrayList<ContactEntry>) result;
            ContactEntry contactEntry;
            for (int i = 0; i < tempResult.size(); i++) {
                contactEntry=tempResult.get(i);
                if (contactEntry.type== ContactEntry.ContactType.Email){
                    noneCharsooEmailContacts.remove(contactEntry.contactData);
                }
                else if (contactEntry.type== ContactEntry.ContactType.PhoneNumber){
                    noneCharsooPhoneContacts.remove(contactEntry.contactData);
                }
            }

            noneCharsooEmailContactsList=new ArrayList<>();
            noneCharsooPhoneNumberContactsList=new ArrayList<>();
            charsooContactsList=new ArrayList<>();

            ArrayList<String> fullNameList=new ArrayList<>(noneCharsooEmailContacts.keySet());
            Collections.sort(fullNameList);
            for (int i = 0; i < fullNameList.size() ; i++) {
                for (int j = 0; j < noneCharsooEmailContacts.get(fullNameList.get(i)).size(); j++) {
                    noneCharsooEmailContactsList.add(noneCharsooEmailContacts.get(fullNameList.get(i)).get(j));
                }
            }
            fullNameList=new ArrayList<>(noneCharsooPhoneContacts.keySet());
            Collections.sort(fullNameList);
            for (int i = 0; i < fullNameList.size() ; i++) {
                for (int j = 0; j < noneCharsooPhoneContacts.get(fullNameList.get(i)).size(); j++) {
                    noneCharsooPhoneNumberContactsList.add(noneCharsooPhoneContacts.get(fullNameList.get(i)).get(j));
                }
            }


            charsooContactsList=tempResult;

            delegate.getContacts(charsooContactsList,noneCharsooPhoneNumberContactsList,noneCharsooEmailContactsList);

        }
    }

    @Override
    public void getError(Integer errorCode, String callerStringID) {

    }

    public int getSizeInPixelFromDp(int dpToConvert){
        Resources r = context.getResources();
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpToConvert, r.getDisplayMetrics());
        return (int) px;
    }
}