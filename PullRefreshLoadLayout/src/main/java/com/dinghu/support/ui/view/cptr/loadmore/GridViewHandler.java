/*
Copyright 2015 chanven

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */
package com.dinghu.support.ui.view.cptr.loadmore;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ListAdapter;

import com.dinghu.support.ui.view.cptr.loadmore.ILoadMoreViewFactory.FootViewAdder;
import com.dinghu.support.ui.view.cptr.loadmore.ILoadMoreViewFactory.ILoadMoreView;

public class GridViewHandler implements LoadMoreHandler {

	private GridViewWithHeaderAndFooter mGridView;
	private View mFooter;

	@Override
	public boolean handleSetAdapter(View contentView,
                                    ILoadMoreView loadMoreView, OnClickListener onClickLoadMoreListener) {
		mGridView = (GridViewWithHeaderAndFooter) contentView;
		ListAdapter adapter = mGridView.getAdapter();
		boolean hasInit = false;
		if (loadMoreView != null) {
			final Context context = mGridView.getContext()
					.getApplicationContext();
			loadMoreView.init(new FootViewAdder() {

				@Override
				public View addFootView(int layoutId) {
					View view = LayoutInflater.from(context).inflate(layoutId,
							mGridView, false);
					mFooter = view;
					return addFootView(view);
				}

				@Override
				public View addFootView(View view) {
					mGridView.addFooterView(view);
					return view;
				}
			}, onClickLoadMoreListener);
			hasInit = true;
			if (null != adapter) {
				mGridView.setAdapter(adapter);
			}
		}
		return hasInit;
	}

	@Override
	public void addFooter() {
		if (mGridView.getFooterViewCount() <= 0 && null != mFooter) {
			mGridView.addFooterView(mFooter);
		}
	}

	@Override
	public void removeFooter() {
		if (mGridView.getFooterViewCount() > 0 && null != mFooter) {
			mGridView.removeFooterView(mFooter);
		}
	}

	@Override
	public void setOnScrollBottomListener(View contentView,
			OnScrollBottomListener onScrollBottomListener) {
		GridViewWithHeaderAndFooter gridView = (GridViewWithHeaderAndFooter) contentView;
		gridView.setOnScrollListener(new GridViewOnScrollListener(
				onScrollBottomListener));
		gridView.setOnItemSelectedListener(new GridViewOnItemSelectedListener(
				onScrollBottomListener));
	}

	/**
	 * 自动加载更多数据
	 */
	private class GridViewOnItemSelectedListener implements
            OnItemSelectedListener {
		private OnScrollBottomListener onScrollBottomListener;

		public GridViewOnItemSelectedListener(
				OnScrollBottomListener onScrollBottomListener) {
			super();
			this.onScrollBottomListener = onScrollBottomListener;
		}

		@Override
		public void onItemSelected(AdapterView<?> adapterView, View view,
                                   int position, long id) {
			if (adapterView.getLastVisiblePosition() + 1 == adapterView
					.getCount()) {
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
	 * 滚动到底部自动加载更多数�?
	 */
	private static class GridViewOnScrollListener implements OnScrollListener {
		private OnScrollBottomListener onScrollBottomListener;
		private int lvIndext = 0;
		public GridViewOnScrollListener(
				OnScrollBottomListener onScrollBottomListener) {
			super();
			this.onScrollBottomListener = onScrollBottomListener;
		}

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			if (scrollState == OnScrollListener.SCROLL_STATE_IDLE
					&& view.getLastVisiblePosition() + 1 == view.getCount()) {
				// 如果滚动到最后一行
				if (onScrollBottomListener != null) {
					onScrollBottomListener.onScorllBootom();
				}
			}

//			switch (scrollState) {
//				case OnScrollListener.SCROLL_STATE_IDLE:
//					if (view.getFirstVisiblePosition() == 0) {
//						// 如果滚动到最后行
//						if (onScrollBottomListener != null) {
//							onScrollBottomListener.onScorllTop();
//						}
//					}
//
//					//记录滚动停止后 记录当前item的位置
//					int scrolled = view.getLastVisiblePosition();
//					//滚动后下标大于滚动前 向下滚动了
//					if (scrolled > lvIndext) {
//						//scroll = false;
//						if (onScrollBottomListener != null) {
//							onScrollBottomListener.onScorllDown();
//						}
//					}
//					//向上滚动了
//					else {
//						if (onScrollBottomListener != null) {
//							onScrollBottomListener.onScorllUp();
//						}
//					}
//					break;
//				case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
//					//view.getLastVisiblePosition()得到当前屏幕可见的第一个item在整个listview中的下标
//					lvIndext = view.getLastVisiblePosition();
//					break;
//			}
		}

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem,
                             int visibleItemCount, int totalItemCount) {

		}
	}
}
