package ir.rasen.charsoo.controller.helper;

import android.app.Activity;
import android.view.View;
import android.widget.ListView;

import ir.rasen.charsoo.view.widget_customized.pull_to_refresh.Footer;
import ir.rasen.charsoo.view.widget_customized.pull_to_refresh.PullToRefreshBase;
import ir.rasen.charsoo.view.widget_customized.pull_to_refresh.PullToRefreshListView;


import ir.rasen.charsoo.R;
import ir.rasen.charsoo.view.interface_m.IPullToRefresh;

/**
 * Created by android on 5/6/2015.
 */
public class PullToRefreshList {

    //pull_to_refresh_lib
    private PullToRefreshListView pullToRefreshListView;
    private Footer footer;
    private int resultSize;
    private ListView listView;

    public PullToRefreshList(final Activity activity,PullToRefreshListView pullToRefreshList, final IPullToRefresh iPullToRefresh) {

        this.pullToRefreshListView = pullToRefreshList;

        pullToRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                iPullToRefresh.notifyRefresh();
            }
        });
        pullToRefreshListView.setOnLastItemVisibleListener(new PullToRefreshBase.OnLastItemVisibleListener() {

            @Override
            public void onLastItemVisible() {

                if (resultSize == 0)
                    return;
                if (!pullToRefreshListView.isRefreshing()
                        && resultSize > 0 && resultSize % activity.getResources().getInteger(R.integer.lazy_load_limitation) == 0) {
                    footer.setVisibility(View.VISIBLE);
                    iPullToRefresh.notifyLoadMore();
                }
            }
        });

        listView = pullToRefreshListView.getRefreshableView();
        activity.registerForContextMenu(listView);
        footer = new Footer(activity);
        listView.addFooterView(footer.getFooterView(), null, false);
    }

    public ListView getListView() {
        return this.listView;
    }

    public void onRefreshComplete(){
        pullToRefreshListView.onRefreshComplete();
    }
    public void setResultSize(int resultSize) {
        this.resultSize = resultSize;
    }

    public void setFooterVisibility(int visibility) {
        footer.setVisibility(visibility);
    }
}
