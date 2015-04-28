package ir.rasen.charsoo;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import ir.rasen.charsoo.helper.Params;


public class ActivityEntrance extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entrance);


        final Intent intent = new Intent(ActivityEntrance.this, ActivityLogin.class);
        startActivity(intent);


        (findViewById(R.id.button_login)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityEntrance.this, ActivityLogin.class);
                startActivity(intent);
            }
        });
        (findViewById(R.id.button_adapter_base)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                intent.putExtra("type", "button_adapter_base");
                startActivity(intent);
            }
        });
        (findViewById(R.id.button_business_post_adapter_comment)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.putExtra("type", "button_business_post_adapter_comment");
                startActivity(intent);
            }
        });
        (findViewById(R.id.button_post_adapter_comment)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(ActivityEntrance.this, ActivityComments.class);
                intent1.putExtra(Params.POST_ID, 1);
                intent1.putExtra(Params.POST_OWNER_BUSINESS_ID, 1);
                startActivity(intent1);
            }
        });
        (findViewById(R.id.button_adapter_comment_notification)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(ActivityEntrance.this, ActivityCommentsNotifications.class);
                intent1.putExtra(Params.BUSINESS_ID, 1);
                startActivity(intent1);
            }
        });
        (findViewById(R.id.button_adapter_user_reviews)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(ActivityEntrance.this, ActivityUserReviews.class);
                intent1.putExtra(Params.VISITED_USER_ID, 3);
                startActivity(intent1);
            }
        });
        (findViewById(R.id.button_dialog)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.putExtra("type", "button_dialog");
                startActivity(intent);
            }
        });
        (findViewById(R.id.button_popup)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.putExtra("type", "button_popup");
                startActivity(intent);
            }
        });
        (findViewById(R.id.button_adapter_business_reviews)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(ActivityEntrance.this, ActivityBusinessReviews.class);
                intent1.putExtra(Params.BUSINESS_ID, 1);
                startActivity(intent1);
            }
        });
        (findViewById(R.id.button_friends)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(ActivityEntrance.this, ActivityFriends.class);
                intent1.putExtra(Params.VISITED_USER_ID, 3);
                startActivity(intent1);
            }
        });

        (findViewById(R.id.button_followers)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(ActivityEntrance.this, ActivityBusinessFollowers.class);
                intent1.putExtra(Params.BUSINESS_ID, 1);
                startActivity(intent1);
            }
        });
        (findViewById(R.id.button_following_businesses)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(ActivityEntrance.this, ActivityFollowingBusinesses.class);
                intent1.putExtra(Params.VISITED_USER_ID, 3);
                startActivity(intent1);
            }
        });
        (findViewById(R.id.button_user_search_result)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(ActivityEntrance.this, ActivitySearchUser.class);
                intent1.putExtra(Params.SEARCH_KEY_WORD, "key");
                startActivity(intent1);
            }
        });
        (findViewById(R.id.button_business_search_result)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(ActivityEntrance.this, ActivitySearchBusinessResult.class);
                intent1.putExtra(Params.SEARCH_KEY_WORD, "key");
                intent1.putExtra(Params.LATITUDE, "35.265321");
                intent1.putExtra(Params.LONGITUDE, "53.265987");
                intent1.putExtra(Params.SUB_CATEGORY_ID, 1);
                startActivity(intent1);
            }
        });
        (findViewById(R.id.button_friends_request)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(ActivityEntrance.this, ActivityFriendRequests.class);
                startActivity(intent1);
            }
        });
        (findViewById(R.id.button_time_line_posts)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.putExtra("type", "button_time_line_posts");
                startActivity(intent);
            }
        });
        (findViewById(R.id.button_grid_posts)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(ActivityEntrance.this, ActivitySearchPostResult.class);
                intent1.putExtra(Params.SEARCH_KEY_WORD, "key");
                startActivity(intent1);
            }
        });
        (findViewById(R.id.button_user_home)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityEntrance.this, ActivityTestUser.class);
                startActivity(intent);
            }
        });
        (findViewById(R.id.button_business_home)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityEntrance.this, ActivityBusiness.class);
                startActivity(intent);
            }
        });
        (findViewById(R.id.button_map_display)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityEntrance.this, ActivityMapDisplay.class);
                intent.putExtra(Params.LATITUDE, 35.7014396);
                intent.putExtra(Params.LONGITUDE, 51.3498186);
                startActivity(intent);
            }
        });
        (findViewById(R.id.button_map_choose)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityEntrance.this, ActivityMapChoose.class);
                startActivity(intent);
            }
        });
        (findViewById(R.id.button_register_business)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityEntrance.this, ActivityRegisterEditBusiness.class);
                startActivity(intent);
            }
        });
        (findViewById(R.id.button_work_time)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityEntrance.this, ActivityWorkTime.class);
                startActivity(intent);
            }
        });
        (findViewById(R.id.button_add_post)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityEntrance.this, ActivityAddPost.class);
                intent.putExtra(Params.BUSINESS_ID, 1);
                startActivity(intent);
            }
        });
        (findViewById(R.id.button_update_post)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityEntrance.this, ActivityAddPost.class);
                intent.putExtra(Params.BUSINESS_ID, 1);
                intent.putExtra(Params.POST_ID, 1);
                startActivity(intent);
            }
        });
        (findViewById(R.id.button_blocked_users)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityEntrance.this, ActivityBlockedUsers.class);
                intent.putExtra(Params.BUSINESS_ID, 1);
                startActivity(intent);
            }
        });
        (findViewById(R.id.button_contact_info)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityEntrance.this, ActivityContactInfo.class);
                startActivity(intent);
            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
