package ir.rasen.charsoo.controller.object;

import android.graphics.Bitmap;

import com.amulyakhare.textdrawable.TextDrawable;

/**
 * Created by hossein-pc on 6/21/2015.
 */
public class ContactEntry {

    public String fullName;
    public enum ContactType {PhoneNumber, Email, None }
    public ContactType type;
    public String contactData;
    public Bitmap contactPhoto;
    public TextDrawable contactPhotoDrawable;

    public boolean isInCharsoo;
    public int charsooIntId;
    public String charsooStringId;
    public String charsooFullName;
    public int charsooPictureIntId;
    public boolean isSeenBefore;

    public ContactType getContactType(int typeCode){
        if (typeCode==1)
            return ContactType.PhoneNumber;
        else if (typeCode==2)
            return ContactType.Email;
        else
            return ContactType.None;
    }

    public int getContactTypeCode(ContactType type){
        if (type==ContactType.PhoneNumber)
            return 1;
        else if (type==ContactType.Email)
            return 2;
        else
            return -1;
    }
}
