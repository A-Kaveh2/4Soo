package ir.rasen.charsoo.view.widget_customized.listviews;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.AbsListView;
import android.widget.ListView;

import ir.rasen.charsoo.view.widget_customized.charsoo_activity.CharsooActivity;

/**
 * Created by Sina on 6/11/15.
 */
public class SmartListView extends ListView implements AbsListView.OnScrollListener {

    private int lastVisibleItem = 0;

    public SmartListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public SmartListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SmartListView(Context context) {
        super(context);
    }

    @Override
    public void setOnScrollListener(OnScrollListener l) {
        super.setOnScrollListener(l);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (view.getId() == getId()) {
            final int currentFirstVisibleItem = getFirstVisiblePosition();

            if (currentFirstVisibleItem > firstVisibleItem) {
                    ((CharsooActivity) getContext()).hideToolbar();
            } else if (currentFirstVisibleItem < lastVisibleItem) {
                // getSherlockActivity().getSupportActionBar().show();
                ((CharsooActivity) getContext()).showToolbar();
            }

            lastVisibleItem = currentFirstVisibleItem;
        }
    }
}
