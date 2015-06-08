package ir.rasen.charsoo.controller.helper;

import java.util.ArrayList;

import ir.rasen.charsoo.controller.object.Post;

/**
 * Created by android on 12/16/2014.
 */

public class SearchItemPost {
    public int postId;
    public int postPictureId;
    public String postPicture;

    public SearchItemPost( int postId, int postPictureId,String postPicture){
        this.postId =  postId;
        this.postPictureId = postPictureId;
        this.postPicture = postPicture;
    }

    public static ArrayList<SearchItemPost> getItems(ArrayList<Post> posts){
        ArrayList<SearchItemPost> result = new ArrayList<>();
        for (Post post:posts){
            result.add(new SearchItemPost(post.id,post.pictureId,post.picture));
        }

        return result;
    }

}
