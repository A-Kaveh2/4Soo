package ir.rasen.charsoo.view.interface_m;

import ir.rasen.charsoo.classes.Comment;

/**
 * Created by android on 3/9/2015.
 */
public interface ICommentChange {
    public void notifyDeleteComment(int commentId);
    public void notifyUpdateComment(Comment comment);
}
