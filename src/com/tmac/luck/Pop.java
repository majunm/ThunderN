package com.tmac.luck;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.tmac.R;

/**
 * 
 * 功能概述:TODO<br/>
 * 类功能详细描述:TODO<br/>
 * 创建日期：2015-4-14下午5:13:39<br/>
 * 创建作者：majun<br/>
 * 联系作者:<a href="mailto:747673016@qq.com">@author majun</a><br/>
 */
public class Pop {
	private Activity context;
	private LayoutInflater inflater;
	private View parentLayout;
	public ListView popListView;
	private PopupWindow pop;
	int screenWidth;
	int popWidth;
	private int width;

	public Pop(Activity context) {
		this.context = context;
		init();
	}

	public Pop(Activity context, int width) {
		this.context = context;
		this.width = width;
		init();
	}

	private boolean useNewItem;

	/**
	 * @param context
	 *            Activity
	 * @param useNewItem
	 *            竖直居中变量控制
	 */
	public Pop(Activity context, boolean useNewItem) {
		this.context = context;
		this.useNewItem = true;
		init();
	}

	@SuppressWarnings("deprecation")
	private void init() {
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		parentLayout = inflater.inflate(R.layout.pop_layout, null);
		popListView = (ListView) parentLayout
				.findViewById(R.id.mini_pop_listview);
		// screenWidth = Tools.get
		// popWidth = Tools.
		DisplayMetrics mDisplayMetrics = new DisplayMetrics();
		((WindowManager) context.getSystemService(Context.WINDOW_SERVICE))
				.getDefaultDisplay().getMetrics(mDisplayMetrics);
		popWidth = (int) (mDisplayMetrics.density * 300);
		// context.getResources().getDimensionPixelSize(R.dimen.mini_pop_width);
		if (width == 0) {
			// pop = new PopupWindow(parentLayout, LayoutParams.MATCH_PARENT,
			pop = new PopupWindow(parentLayout, LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT);
		} else {
			pop = new PopupWindow(parentLayout, width,
					LayoutParams.WRAP_CONTENT);
		}
		// 设置背景才会有动画效果
		pop.setBackgroundDrawable(new BitmapDrawable());
		// pop.setAnimationStyle(R.style.popupAnimation);// 设置动画
		// WindowManager windowManager = context.getWindowManager();
		// Display display = windowManager.getDefaultDisplay();
		// 设置动画效果
		// WindowManager.LayoutParams params = context.getWindow()
		// .getAttributes();
		// params.alpha = 0.7f;

		// context.getWindow().setAttributes(params);
		fillData();
	}

	private List<String> list;
	private PopAdapter adapter;

	/**
	 */
	private void fillData() {
		list = new ArrayList<String>();
		initAdapter();
		popListView.setAdapter(adapter);
		popListView.setFocusableInTouchMode(true);
		popListView.setFocusable(true);
	}

	public void setOnItemClickListener(OnItemClickListener listener) {
		popListView.setOnItemClickListener(listener);
	}

	/**
	 * @description :TODO
	 */
	public void addItems(List<String> list) {
		this.list.clear();
		if (list != null && list.size() != 0) {
			for (String string : list) {
				this.list.add(string);
			}
		}
	}

	public void addItem(String string) {
		list.add(string);
	}

	/**
	 * @data :2014年7月5日下午5:44:47
	 * @param:
	 * @return :void
	 * @description :TODO
	 */
	private void initAdapter() {
		if (useNewItem) {
			adapter = new PopAdapter(list, context, true);
		} else {
			adapter = new PopAdapter(list, context);
		}
	}

	public void disMiss() {
		if (pop.isShowing()) {
			pop.dismiss();
		}
	}

	public void shoAsDropDown(View target) {
		// pop.showAsDropDown(target, (screenWidth - popWidth) / 2,
		// context.getResources().getDimensionPixelSize(R.dimen.mini_pop_offset_y));
		// pop.showAsDropDown(target, 0,
		// context.getResources().getDimensionPixelSize(R.dimen.mini_pop_offset_y));
		pop.showAsDropDown(target);
		pop.setOutsideTouchable(true);
		pop.setFocusable(true);
		pop.update();
	}

	public void showAtLocation(View target) {
		pop.showAtLocation(target, Gravity.CENTER, 0, 0);
		pop.setOutsideTouchable(true);
		pop.setFocusable(true);
		pop.update();
	}
}
