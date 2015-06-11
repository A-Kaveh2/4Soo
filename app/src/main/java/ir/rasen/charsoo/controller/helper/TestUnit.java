package ir.rasen.charsoo.controller.helper;

import android.content.Context;
import android.content.res.Resources;

import java.io.InputStream;
import java.util.ArrayList;

import ir.rasen.charsoo.controller.object.Business;
import ir.rasen.charsoo.controller.object.Category;
import ir.rasen.charsoo.controller.object.Comment;
import ir.rasen.charsoo.controller.object.CommentNotification;
import ir.rasen.charsoo.controller.object.Post;
import ir.rasen.charsoo.controller.object.Review;
import ir.rasen.charsoo.controller.object.SubCategory;
import ir.rasen.charsoo.controller.object.User;

/**
 * Created by android on 3/7/2015.
 */
public class TestUnit {

    public static boolean isTestingCommentActivity = true;

    public static ArrayList<BaseAdapterItem> getBaseAdapterItems(Resources resources) throws Exception {
        ArrayList<BaseAdapterItem> sampleData = new ArrayList<>();
        sampleData.add(new BaseAdapterItem(resources, 1, 2022, "1مریم وحدتی"));
        sampleData.add(new BaseAdapterItem(resources, 2, 2022, "2مریم وحدتی"));
        sampleData.add(new BaseAdapterItem(resources, 3, 2022, "3مریم وحدتی"));
        sampleData.add(new BaseAdapterItem(resources, 4, 2022, "4مریم وحدتی"));
        sampleData.add(new BaseAdapterItem(resources, 5, 2022, "5مریم وحدتی"));
        sampleData.add(new BaseAdapterItem(resources, 6, 2022, "6مریم وحدتی"));
        sampleData.add(new BaseAdapterItem(resources, 7, 2022, "7مریم وحدتی"));
        sampleData.add(new BaseAdapterItem(resources, 8, 2022, "8مریم وحدتی"));
        sampleData.add(new BaseAdapterItem(resources, 9, 2022, "9مریم وحدتی"));
        sampleData.add(new BaseAdapterItem(resources, 10, 2022, "9مریم وحدتی"));
        sampleData.add(new BaseAdapterItem(resources, 1, 2022, "1مریم وحدتی"));
        sampleData.add(new BaseAdapterItem(resources, 2, 2022, "2مریم وحدتی"));
        sampleData.add(new BaseAdapterItem(resources, 3, 2022, "3مریم وحدتی"));
        sampleData.add(new BaseAdapterItem(resources, 4, 2022, "4مریم وحدتی"));
        sampleData.add(new BaseAdapterItem(resources, 5, 2022, "5مریم وحدتی"));
        sampleData.add(new BaseAdapterItem(resources, 6, 2022, "6مریم وحدتی"));
        sampleData.add(new BaseAdapterItem(resources, 7, 2022, "7مریم وحدتی"));
        sampleData.add(new BaseAdapterItem(resources, 8, 2022, "8مریم وحدتی"));
        sampleData.add(new BaseAdapterItem(resources, 9, 2022, "9مریم وحدتی"));
        sampleData.add(new BaseAdapterItem(resources, 10, 2022, "9مریم وحدتی"));


        return sampleData;
    }

    public static ArrayList<Comment> getCommentAdapterItems() {
        ArrayList<Comment> comments = new ArrayList<>();

        Comment comment1 = new Comment();
        comment1.id = 1;
        comment1.businessID = 1;
        comment1.postID = 1;
        comment1.userID = 3;
        comment1.username = "علی";
        comment1.userProfilePictureID = 2022;
        comment1.text = "صاحب کامنت";
        comments.add(comment1);

        Comment comment2 = new Comment();
        comment2.id = 2;
        comment2.businessID = 1;
        comment2.postID = 1;
        comment2.userID = 2;
        comment2.username = "علی";
        comment2.userProfilePictureID = 2022;
        comment2.text = "خیلی خوب" + "\n" + "توووووووووووووووپ" + "خیلی خوب" + "\n" + "توووووووووووووووپ" + "خیلی خوب" + "\n" + "توووووووووووووووپ";
        comments.add(comment2);

        Comment comment3 = new Comment();
        comment3.id = 3;
        comment3.businessID = 1;
        comment3.postID = 1;
        comment3.userID = 2;
        comment3.username = "علی";
        comment3.userProfilePictureID = 2022;
        comment3.text = "خیلی خوب" + "\n" + "توووووووووووووووپ";
        comments.add(comment3);

        Comment comment4 = new Comment();
        comment4.id = 4;
        comment4.businessID = 1;
        comment4.postID = 1;
        comment4.userID = 2;
        comment4.username = "علی";
        comment4.userProfilePictureID = 2022;
        comment4.text = "خیلی خوب" + "\n" + "توووووووووووووووپ" + "خیلی خوب" + "\n" + "توووووووووووووووپ" + "خیلی خوب" + "\n" + "توووووووووووووووپ" + "خیلی خوب" + "\n" + "توووووووووووووووپ" + "خیلی خوب" + "\n" + "توووووووووووووووپ" + "خیلی خوب" + "\n" + "توووووووووووووووپ";
        comments.add(comment4);

        for (int i = 0;i<16;i++){
            comments.add(comment4);
        }
        return comments;
    }

    public static ArrayList<CommentNotification> getCommentNotificationAdapterItems(Context context) {
        CommentNotification commentNotification = new CommentNotification();
        commentNotification.id = 1;
        commentNotification.postID = 1;
        commentNotification.userID = 2;
        commentNotification.userName = "ali_1";

        byte[] buffer = new byte[0];
        try {
            InputStream stream = context.getAssets().open("picture.txt");
            int size = stream.available();
            buffer = new byte[size];
            stream.read(buffer);
            stream.close();
        } catch (Exception e) {
            String s = e.getMessage();
        }
        String text = new String(buffer);

        commentNotification.postPicture = text;
        commentNotification.userPicture = text;
        commentNotification.text = "خیلی خوب" + "\n" + "توووووووووووووووپ" + "خیلی خوب" + "\n" + "توووووووووووووووپ" + "خیلی خوب" + "\n" + "توووووووووووووووپ" + "خیلی خوب" + "\n" + "توووووووووووووووپ" + "خیلی خوب" + "\n" + "توووووووووووووووپ" + "خیلی خوب" + "\n" + "توووووووووووووووپ";

        ArrayList<CommentNotification> commentNotifications = new ArrayList<>();
        for (int i = 0; i < 10; i++)
            commentNotifications.add(commentNotification);

        return commentNotifications;
    }

    public static ArrayList<Review> getUserReviewAdapterItems() {
        Review review = new Review();
        review.id = 1;
        review.businessID = 1;
        review.businessUserName = "business_1";
        review.businessPicutreId = 2022;
        review.text = "خیلی خوب و very good بود" + "\n" + "توووووووووووووووپ" + "خیلی خوب" + "\n" + "توووووووووووووووپ" + "خیلی خوب" + "\n" + "توووووووووووووووپ" + "خیلی خوب" + "\n" + "توووووووووووووووپ";
        review.rate = 3;
        review.userPicutreId = 2022;
        review.userName = "احمدی";

        Review review2 = new Review();
        review2.id = 2;
        review2.businessID = 2;
        review2.businessUserName = "business_2";
        review2.businessPicutreId = 2022;
        review2.text = "خیلی خوب" + "\n" + "توووووووووووووووپ" + "خیلی خوب" + "\n" + "توووووووووووووووپ" + "خیلی خوب" + "\n" + "توووووووووووووووپ" + "خیلی خوب" + "\n" + "توووووووووووووووپ" + "خیلی خوب" + "\n" + "توووووووووووووووپ" + "خیلی خوب" + "\n" + "توووووووووووووووپ";
        review2.rate = 2;
        review2.userPicutreId = 2022;
        review2.userName = "علی";

        ArrayList<Review> reviews = new ArrayList<>();
        reviews.add(review);
        for (int i = 0; i < 19; i++)
            reviews.add(review2);

        return reviews;
    }

    public static ArrayList<Post> getPostAdapterListItems() {
        ArrayList<Post> posts = new ArrayList<Post>();

        //announcements
        Post post7 = new Post();
        post7.businessProfilePictureId = 2022;
        //post7.creationDate = 1;
        post7.businessUserName = "کسب و کار 7";
        post7.businessID = 1;
        post7.userId = 3;
        post7.userName = "ali_1";
        post7.type = Post.Type.Follow;

        Post post8 = new Post();
        post8.businessProfilePictureId = 2022;
        //post8.creationDate = 1;
        post8.businessUserName = "کسب و کار 7";
        post8.businessID = 1;
        post8.userId = 4;
        post8.userName = "ali_2";
        post8.type = Post.Type.Review;

        posts.add(post7);
        posts.add(post8);

        for (int i = 0; i < 18; i++) {
            Post post = new Post();
            post.businessProfilePictureId = 2022;
            post.businessUserName = "business1";
            post.businessID = 1;
            post.type = Post.Type.Complete;
            post.pictureId = 2022;
            //post.creationDate = -10;
            post.likeNumber = 325485;
            post.commentNumber = 6529851;
            post.shareNumber = 6325985;
            post.description = "توضیحات توضیحات توضحایت توضیحات توضیحات توضحایت توضیحات توضیحات توضحایت توضیحات توضیحات توضحایت توضیحات توضیحات توضحایت توضیحات توضیحات توضحایت توضیحات توضیحات توضحایت توضیحات توضیحات توضحایت توضیحات توضیحات توضحایت توضیحات توضیحات توضحایت ";
            ArrayList<Comment> lastThreeComments = new ArrayList<>();
            lastThreeComments.add(new Comment(1, "علی احمدی", "بسیار بسیار عالی بسیار بسیار عالی بسیار بسیار عالی بسیار بسیار عالی بسیار بسیار عالی بسیار بسیار عالی بسیار بسیار عالی"));
            lastThreeComments.add(new Comment(2, "کریم محمودی", "سیار عالی بسیار بسیار عالی بسیار بسیار عالی بسیار بسیار عالی"));
            lastThreeComments.add(new Comment(3, "نستری رحیمی", "بسیار بسیار عالی بسیار بسیار عالی بسیار بسیار      عالی بسیار بسیار عالی بسیار بسیار عالی بسیار بسیار عالی بسیار بسیار عالی"));
            post.lastThreeComments = lastThreeComments;
            switch (i) {
                case 0: {
                    post.isLiked = true;
                    post.isShared = true;
                    post.isReported = true;
                    break;
                }

                case 1: {
                    post.isLiked = true;
                    post.isShared = true;
                    post.isReported = false;
                    break;
                }

                case 2: {
                    post.isLiked = true;
                    post.isShared = false;
                    post.isReported = true;
                    break;
                }

                case 3: {
                    post.isLiked = true;
                    post.isShared = false;
                    post.isReported = false;
                    break;
                }

                case 4: {
                    post.isLiked = false;
                    post.isShared = true;
                    post.isReported = true;
                    break;
                }

                case 5: {
                    post.isLiked = false;
                    post.isShared = true;
                    post.isReported = false;
                    break;
                }
                case 6: {
                    post.isLiked = false;
                    post.isShared = false;
                    post.isReported = true;
                    break;
                }
                case 7: {
                    post.isLiked = false;
                    post.isShared = false;
                    post.isReported = false;
                    break;
                }
            }
            posts.add(post);
        }


        return posts;
    }

    public static ArrayList<Post> getPostAdapterSharedListItems() {
        ArrayList<Post> posts = new ArrayList<Post>();
        for (int i = 0; i < 8; i++) {
            Post post = new Post();
            post.id = i + 1;
            post.businessProfilePictureId = 2022;
            post.businessUserName = "business1";
            post.businessID = 1;
            post.type = Post.Type.Complete;
            post.pictureId = 2022;
            //post.creationDate = -10;
            post.likeNumber = 325485;
            post.commentNumber = 6529851;
            post.shareNumber = 6325985;
            post.description = "توضیحات توضیحات توضحایت توضیحات توضیحات توضحایت توضیحات توضیحات توضحایت توضیحات توضیحات توضحایت توضیحات توضیحات توضحایت توضیحات توضیحات توضحایت توضیحات توضیحات توضحایت توضیحات توضیحات توضحایت توضیحات توضیحات توضحایت توضیحات توضیحات توضحایت ";
            ArrayList<Comment> lastThreeComments = new ArrayList<>();
            lastThreeComments.add(new Comment(1, "علی احمدی", "بسیار بسیار عالی بسیار بسیار عالی بسیار بسیار عالی بسیار بسیار عالی بسیار بسیار عالی بسیار بسیار عالی بسیار بسیار عالی"));
            lastThreeComments.add(new Comment(2, "کریم محمودی", "سیار عالی بسیار بسیار عالی بسیار بسیار عالی بسیار بسیار عالی"));
            lastThreeComments.add(new Comment(3, "نستری رحیمی", "بسیار بسیار عالی بسیار بسیار عالی بسیار بسیار      عالی بسیار بسیار عالی بسیار بسیار عالی بسیار بسیار عالی بسیار بسیار عالی"));
            post.lastThreeComments = lastThreeComments;
            switch (i) {
                case 0: {
                    post.isLiked = true;
                    post.isShared = true;
                    post.isReported = true;
                    break;
                }

                case 1: {
                    post.isLiked = true;
                    post.isShared = true;
                    post.isReported = false;
                    break;
                }

                case 2: {
                    post.isLiked = true;
                    post.isShared = false;
                    post.isReported = true;
                    break;
                }

                case 3: {
                    post.isLiked = true;
                    post.isShared = false;
                    post.isReported = false;
                    break;
                }

                case 4: {
                    post.isLiked = false;
                    post.isShared = true;
                    post.isReported = true;
                    break;
                }

                case 5: {
                    post.isLiked = false;
                    post.isShared = true;
                    post.isReported = false;
                    break;
                }
                case 6: {
                    post.isLiked = false;
                    post.isShared = false;
                    post.isReported = true;
                    break;
                }
                case 7: {
                    post.isLiked = false;
                    post.isShared = false;
                    post.isReported = false;
                    break;
                }
            }
            posts.add(post);
        }


        return posts;
    }

    public static ArrayList<SearchItemPost> getPostAdapterGridItems() {
        ArrayList<SearchItemPost> posts = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            posts.add(new SearchItemPost(0,2022,""));
        }
        return posts;
    }

    public static ArrayList<BusinessListItem> getBusinessListItems() {
        ArrayList<BusinessListItem> b = new ArrayList<>();
        b.add(new BusinessListItem(2, "business_2"));
        b.add(new BusinessListItem(3, "business_3"));
        b.add(new BusinessListItem(4, "business_4"));
        b.add(new BusinessListItem(5, "business_5"));
        b.add(new BusinessListItem(6, "business_6"));
        b.add(new BusinessListItem(7, "business_7"));
        b.add(new BusinessListItem(8, "business_8"));
        b.add(new BusinessListItem(9, "business_9"));
        b.add(new BusinessListItem(10, "business_10"));
        b.add(new BusinessListItem(11, "business_11"));
        b.add(new BusinessListItem(12, "business_12"));

        return b;
    }

    public static User getUser() {
        User user = new User();
        user.id = 4;
        user.userIdentifier = "ali_1";
        user.name = "علی احمدی";
        user.profilePictureId = 2022;
        user.friendshipRelationStatus = FriendshipRelation.Status.NOT_FRIEND;
        user.permissions = new Permission(true, true, true);

        return user;
    }

    public static Business getBusiness() {
        Business business = new Business();
        business.id = 2;
        business.profilePictureId = 2022;
        business.businessIdentifier = "business_1";
        business.name = "کسب و کار من";
        return business;
    }

    public static ArrayList<Category> getCategories() {
        ArrayList<Category> categories = new ArrayList<>();
        categories.add(new Category(0,"دسته بندی"));
        categories.add(new Category(1, "دسته بندی یک"));
        categories.add(new Category(2, "دسته بندی دو"));
        categories.add(new Category(3, "دسته بندی سه"));

        return categories;
    }

    public static ArrayList<SubCategory> getSubcategories(int categoryId) {
        ArrayList<SubCategory> subcategory1 = new ArrayList<>();
        ArrayList<SubCategory> subcategory2 = new ArrayList<>();
        ArrayList<SubCategory> subcategory3 = new ArrayList<>();
        ArrayList<SubCategory> result = new ArrayList<>();


        subcategory1.add(new SubCategory(1, "زیر دسته بندی یک - یک"));
        subcategory1.add(new SubCategory(2, "زیر دسته بندی یک - دو"));

        subcategory2.add(new SubCategory(3, "زیر دسته بندی دو - یک"));
        subcategory2.add(new SubCategory(4, "زیر دسته بندی دو - دو"));

        subcategory3.add(new SubCategory(5, "زیر دسته بندی سه - یک"));
        subcategory3.add(new SubCategory(6, "زیر دسته بندی سه - دو"));

        switch (categoryId) {
            case 1:
                result = subcategory1;
                break;
            case 2:
                result = subcategory2;
                break;
            case 3:
                result= subcategory3;
                break;
        }

        result.add(0,new SubCategory(0,"زیر دسته بندی"));
        return result;
    }

}
