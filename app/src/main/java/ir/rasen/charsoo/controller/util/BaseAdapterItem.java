package ir.rasen.charsoo.helper;

import android.content.res.Resources;

import ir.rasen.charsoo.R;

/**
 * Created by android on 3/7/2015.
 */
public class BaseAdapterItem {
    private int id;
    private int imageId;
    private String title;
    private Resources resources;


    public BaseAdapterItem(Resources resources) {
        this.resources = resources;
    }

    public BaseAdapterItem(Resources resources,int id, int imageId, String title) throws Exception {
        this.resources = resources;
        this.id = id;
        this.imageId = imageId;
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id)throws Exception {
        if(id<1)
            throw new Exception(ExceptionMessage.set(resources,
                    resources.getString(R.string.invalid_id_code),
                    resources.getString(R.string.invalid_id)));
        this.id = id;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) throws Exception {
        if (imageId < 1)
            throw new Exception(ExceptionMessage.set(resources,
                    resources.getString(R.string.invalid_image_id_code),
                    resources.getString(R.string.invalid_image_id)));
        else
            this.imageId = imageId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) throws Exception {
        if (title.length() > resources.getInteger(R.integer.restriction_identifier_length))
            throw new Exception((ExceptionMessage.set(resources,
                    resources.getString(R.string.identifier_is_too_long_code),
                    resources.getString(R.string.identifier_is_too_long))));
        else
            this.title = title;
    }
}
