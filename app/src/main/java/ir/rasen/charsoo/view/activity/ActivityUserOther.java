package ir.rasen.charsoo.view.activity;

import android.app.FragmentTransaction;
import android.os.Bundle;

import ir.rasen.charsoo.R;
import ir.rasen.charsoo.controller.helper.LoginInfo;
import ir.rasen.charsoo.controller.helper.Params;
import ir.rasen.charsoo.view.fragment.FragmentUser;
import ir.rasen.charsoo.view.fragment.FragmentUserOther;
import ir.rasen.charsoo.view.widgets.charsoo_activity.CharsooActivity;

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
