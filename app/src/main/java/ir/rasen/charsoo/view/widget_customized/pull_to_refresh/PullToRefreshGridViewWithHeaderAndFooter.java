/*******************************************************************************
 * Copyright 2011, 2012 Chris Banes.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package ir.rasen.charsoo.view.widget_customized.pull_to_refresh;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.util.AttributeSet;
import android.view.View;

import ir.rasen.charsoo.R;
import ir.rasen.charsoo.view.widget_customized.pull_to_refresh.internal.EmptyViewMethodAccessor;

public class PullToRefreshGridViewWithHeaderAndFooter extends PullToRefreshAdapterViewBase<HFGridView> {

	public PullToRefreshGridViewWithHeaderAndFooter(Context context) {
		super(context);
	}

	public PullToRefreshGridViewWithHeaderAndFooter(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public PullToRefreshGridViewWithHeaderAndFooter(Context context, Mode mode) {
		super(context, mode);
	}

	public PullToRefreshGridViewWithHeaderAndFooter(Context context, Mode mode, AnimationStyle style) {
		super(context, mode, style);
	}

	@Override
	public final Orientation getPullToRefreshScrollDirection() {
		return Orientation.VERTICAL;
	}

	@Override
	protected final HFGridView createRefreshableView(Context context, AttributeSet attrs) {
		final HFGridView gv;
		if (VERSION.SDK_INT >= VERSION_CODES.GINGERBREAD) {
			gv = new InternalHFGridViewSDK9(context, attrs);
		} else {
			gv = new InternalHFGridView(context, attrs);
		}

		// Use Generated ID (from res/values/ids.xml)
		gv.setId(R.id.gridview);
		return gv;
	}

	class InternalHFGridView extends HFGridView implements EmptyViewMethodAccessor {

		public InternalHFGridView(Context context, AttributeSet attrs) {
			super(context, attrs);
		}

		@Override
		public void setEmptyView(View emptyView) {
			PullToRefreshGridViewWithHeaderAndFooter.this.setEmptyView(emptyView);
		}

		@Override
		public void setEmptyViewInternal(View emptyView) {
			super.setEmptyView(emptyView);
		}
	}

	@TargetApi(9)
	final class InternalHFGridViewSDK9 extends InternalHFGridView {

		public InternalHFGridViewSDK9(Context context, AttributeSet attrs) {
			super(context, attrs);
		}

		@Override
		protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX,
				int scrollRangeY, int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {

			final boolean returnValue = super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX,
					scrollRangeY, maxOverScrollX, maxOverScrollY, isTouchEvent);

			// Does all of the hard work...
			OverscrollHelper.overScrollBy(PullToRefreshGridViewWithHeaderAndFooter.this, deltaX, scrollX, deltaY, scrollY, isTouchEvent);

			return returnValue;
		}
	}
}
