package ir.rasen.charsoo.controller.helper;

import android.content.res.Resources;

/**
 * Created by android on 3/31/2015.
 */
public class SearchItemBusiness extends  BaseAdapterItem {
    public double distance;

    public SearchItemBusiness(Resources resources,int id, int imageId, String title,double distance)throws Exception{
        super(resources,id,imageId,title);
        this.distance = distance;
    }
}
