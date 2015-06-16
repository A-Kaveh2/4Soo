package ir.rasen.charsoo.view.activity;

import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;

import ir.rasen.charsoo.R;
import ir.rasen.charsoo.controller.helper.FriendshipRelation;
import ir.rasen.charsoo.controller.helper.LoginInfo;
import ir.rasen.charsoo.controller.helper.Params;
import ir.rasen.charsoo.controller.helper.PullToRefreshGrid;
import ir.rasen.charsoo.controller.helper.ServerAnswer;
import ir.rasen.charsoo.controller.object.Post;
import ir.rasen.charsoo.controller.object.User;
import ir.rasen.charsoo.model.post.GetSharedPosts;
import ir.rasen.charsoo.model.user.GetUserHomeInfo;
import ir.rasen.charsoo.view.dialog.DialogMessage;
import ir.rasen.charsoo.view.fragment.FragmentUser;
import ir.rasen.charsoo.view.fragment.FragmentUserOther;
import ir.rasen.charsoo.view.interface_m.IPullToRefresh;
import ir.rasen.charsoo.view.interface_m.IWebserviceResponse;
import ir.rasen.charsoo.view.widget_customized.GridViewUserOther;
import ir.rasen.charsoo.view.widget_customized.charsoo_activity.CharsooActivity;
import ir.rasen.charsoo.view.widget_customized.pull_to_refresh.PullToRefreshGridViewWithHeaderAndFooter;

public class ActivityUserOther extends CharsooActivity {

    int visitedUserIntId,visitorIntId;
    FragmentUserOther fragOther;
    FragmentUser fragUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_other_new);

        visitedUserIntId = getIntent().getExtras().getInt(Params.VISITED_USER_ID);
        visitorIntId=LoginInfo.getUserId(this);

        if (visitorIntId==visitedUserIntId){
            fragUser=new FragmentUser();
            FragmentTransaction ft= getFragmentManager().beginTransaction();
            ft.replace(R.id.fragmentContainer,fragUser);
            ft.commit();
        }
        else
        {
            fragOther=new FragmentUserOther();
            FragmentTransaction ft= getFragmentManager().beginTransaction();
            ft.replace(R.id.fragmentContainer,fragOther);
            ft.commit();
        }
    }

}
