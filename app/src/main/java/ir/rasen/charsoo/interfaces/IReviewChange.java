package ir.rasen.charsoo.interfaces;

import ir.rasen.charsoo.classes.Comment;
import ir.rasen.charsoo.classes.Review;

/**
 * Created by android on 3/9/2015.
 */
public interface IReviewChange {
    public void notifyDeleteReview(int reviewId);
    public void notifyUpdateReview(Review review);
}
