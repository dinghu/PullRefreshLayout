package com.dinghu.support.ui.view.cptr.loadmore;

import android.view.View;
import android.view.View.OnClickListener;

import com.dinghu.support.ui.view.cptr.loadmore.ILoadMoreViewFactory.ILoadMoreView;

public interface LoadMoreHandler {

	/**
	 * @param contentView
	 * @param loadMoreView
	 * @param onClickLoadMoreListener
	 * @return ILoadMoreView
	 */
	public boolean handleSetAdapter(View contentView,
                                    ILoadMoreView loadMoreView, OnClickListener onClickLoadMoreListener);

	public void setOnScrollBottomListener(View contentView,
                                          OnScrollBottomListener onScrollBottomListener);

	void removeFooter();

	void addFooter();
}
