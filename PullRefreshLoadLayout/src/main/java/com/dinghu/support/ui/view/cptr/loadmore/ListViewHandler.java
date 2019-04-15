package com.dinghu.support.ui.view.cptr.loadmore;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.HeaderViewListAdapter;
import android.widget.ListView;

import com.dinghu.support.ui.view.cptr.loadmore.ILoadMoreViewFactory.FootViewAdder;
import com.dinghu.support.ui.view.cptr.loadmore.ILoadMoreViewFactory.ILoadMoreView;

public class ListViewHandler implements LoadMoreHandler {

    private AbsListView mListView;
    private View mFooter;

    @Override
    public boolean handleSetAdapter(View contentView,
                                    ILoadMoreView loadMoreView, OnClickListener onClickLoadMoreListener) {
        final AbsListView listView = (AbsListView) contentView;
        mListView = listView;
        boolean hasInit = false;
        if (loadMoreView != null) {
            final Context context = listView.getContext()
                    .getApplicationContext();
            loadMoreView.init(new FootViewAdder() {

                @Override
                public View addFootView(int layoutId) {
                    View view = LayoutInflater.from(context).inflate(layoutId,
                            listView, false);
                    mFooter = view;
                    return addFootView(view);
                }

                @Override
                public View addFootView(View view) {
                    if (listView instanceof ListView) {
                        ((ListView) listView).addFooterView(view);
                    }

                    return view;
                }
            }, onClickLoadMoreListener);
            hasInit = true;
        }
        return hasInit;
    }

    @Override
    public void setOnScrollBottomListener(View contentView,
                                          OnScrollBottomListener onScrollBottomListener) {

        AbsListView listView = (AbsListView) contentView;
        listView.setOnScrollListener(new ListViewOnScrollListener(
                onScrollBottomListener));
        listView.setOnItemSelectedListener(new ListViewOnItemSelectedListener(
                onScrollBottomListener));
    }

    @Override
    public void removeFooter() {
        if (mListView instanceof ListView) {
            if (((ListView) mListView).getFooterViewsCount() > 0 && null != mFooter) {
                if (mListView.getAdapter() instanceof HeaderViewListAdapter) {
                    ((ListView) mListView).removeFooterView(mFooter);
                }
            }
        }
    }

    @Override
    public void addFooter() {
        if (mListView instanceof ListView) {
            if (((ListView) mListView).getFooterViewsCount() <= 0 && null != mFooter) {
                ((ListView) mListView).addFooterView(mFooter);
            }
        }
    }

    /**
     * 自动加载更多数据
     */
    private class ListViewOnItemSelectedListener implements
            OnItemSelectedListener {
        private OnScrollBottomListener onScrollBottomListener;

        public ListViewOnItemSelectedListener(
                OnScrollBottomListener onScrollBottomListener) {
            super();
            this.onScrollBottomListener = onScrollBottomListener;
        }

        @Override
        public void onItemSelected(AdapterView<?> listView, View view,
                                   int position, long id) {
            if (listView.getLastVisiblePosition() + 1 == listView.getCount()) {
                // 如果滚动到最后一行
                if (onScrollBottomListener != null) {
                    onScrollBottomListener.onScorllBootom();
                }
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

    ;

    /**
     * 滚动到底部自动加载更多
     */
    private static class ListViewOnScrollListener implements OnScrollListener {
        private OnScrollBottomListener onScrollBottomListener;
        private int lvIndext = 0;

        public ListViewOnScrollListener(
                OnScrollBottomListener onScrollBottomListener) {
            super();
            this.onScrollBottomListener = onScrollBottomListener;
        }

        @Override
        public void onScrollStateChanged(AbsListView listView, int scrollState) {
            if (scrollState == OnScrollListener.SCROLL_STATE_IDLE
                    && listView.getLastVisiblePosition() + 1 == listView
                    .getCount()) {
                // 如果滚动到最后行
                if (onScrollBottomListener != null) {
                    onScrollBottomListener.onScorllBootom();
                }
            }
//            switch (scrollState) {
//                case OnScrollListener.SCROLL_STATE_IDLE:
//                    if (listView.getFirstVisiblePosition() == 0) {
//                        // 如果滚动到最后行
//                        if (onScrollBottomListener != null) {
//                            onScrollBottomListener.onScorllTop();
//                        }
//                    }
//
//                    //记录滚动停止后 记录当前item的位置
//                    int scrolled = listView.getLastVisiblePosition();
//                    //滚动后下标大于滚动前 向下滚动了
//                    if (scrolled > lvIndext) {
//                        //scroll = false;
//                        if (onScrollBottomListener != null) {
//                            onScrollBottomListener.onScorllDown();
//                        }
//                    }
//                    //向上滚动了
//                    else {
//                        if (onScrollBottomListener != null) {
//                            onScrollBottomListener.onScorllUp();
//                        }
//                    }
//                    break;
//                case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
//                    //view.getLastVisiblePosition()得到当前屏幕可见的第一个item在整个listview中的下标
//                    lvIndext = listView.getLastVisiblePosition();
//                    break;
//            }
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem,
                             int visibleItemCount, int totalItemCount) {

        }
    }
}
