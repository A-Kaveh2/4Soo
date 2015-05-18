package ir.rasen.charsoo.controller.helper;

import android.app.Activity;
import android.view.View;

import com.handmark.pulltorefresh.library.Footer;
import com.handmark.pulltorefresh.library.HFGridView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshGridViewWithHeaderAndFooter;

import ir.rasen.charsoo.R;
import ir.rasen.charsoo.view.interface_m.IPullToRefresh;

/**
 * Created by android on 5/6/2015.
 */
public class PullToRefreshGrid {

    //pull_to_refresh_lib
    private PullToRefreshGridViewWithHeaderAndFooter pullToRefreshGridViewHF;
    private Footer footer;
    private int resultSize;
    private HFGridView gridViewHF;

    public PullToRefreshGrid(final Activity activity,final PullToRefreshGridViewWithHeaderAndFooter pullToRefreshGridViewHF, final IPullToRefresh iPullToRefresh) {

        this.pullToRefreshGridViewHF = pullToRefreshGridViewHF;


        pullToRefreshGridViewHF.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<HFGridView>() {
            @Override
            public void onRefresh(PullToRefreshBase<HFGridView> refreshView) {
                iPullToRefresh.notifyRefresh();
            }
        });

        pullToRefreshGridViewHF.setOnLastItemVisibleListener(new PullToRefreshBase.OnLastItemVisibleListener() {

            @Override
            public void onLastItemVisible() {

                if (resultSize == 0)
                    return;
                if (!pullToRefreshGridViewHF.isRefreshing()
                        && resultSize > 0 && resultSize % activity.getResources().getInteger(R.integer.lazy_load_limitation) == 0) {
                    footer.setVisibility(View.VISIBLE);
                    iPullToRefresh.notifyLoadMore();
                }
            }
        });

        gridViewHF = pullToRefreshGridViewHF.getRefreshableView();
        activity.registerForContextMenu(gridViewHF);
        footer = new Footer(activity);
        gridViewHF.addFooterView(footer.getFooterView(), null, false);
    }

    public HFGridView getGridViewHeaderFooter() {
        return this.gridViewHF;
    }

    public void onRefreshComplete() {
        pullToRefreshGridViewHF.onRefreshComplete();
    }

    public void setResultSize(int resultSize) {
        this.resultSize = resultSize;
    }

    public void setFooterVisibility(int visibility) {
        footer.setVisibility(visibility);
    }

    public boolean isRefreshing(){
        return pullToRefreshGridViewHF.isRefreshing();
    }
}
