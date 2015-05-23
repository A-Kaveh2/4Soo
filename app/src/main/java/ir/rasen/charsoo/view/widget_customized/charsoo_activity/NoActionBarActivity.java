package ir.rasen.charsoo.view.widget_customized.charsoo_activity;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

/**
 * Created by Sina on 5/21/15.
 */
public class NoActionBarActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getSupportActionBar()!=null)
            getSupportActionBar().hide();
    }
}
