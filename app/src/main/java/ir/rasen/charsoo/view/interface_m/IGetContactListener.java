package ir.rasen.charsoo.view.interface_m;

import java.util.ArrayList;

import ir.rasen.charsoo.controller.object.CommentNotification;
import ir.rasen.charsoo.controller.object.ContactEntry;

/**
 * Created by hossein-pc on 6/23/2015.
 */
public interface IGetContactListener {
    public void getContacts(ArrayList<ContactEntry> charsooContacts,ArrayList<ContactEntry> noneCharsooPhoneContacts,ArrayList<ContactEntry> noneCharsooEmailContacts);
}
