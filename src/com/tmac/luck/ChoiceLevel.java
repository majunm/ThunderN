package com.tmac.luck;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.tmac.R;

/**
 * 
 * 功能概述:查找聊天记录<br/>
 * 创建日期：2015年7月5日下午12:29:21<br/>
 * 创建作者：majun<br/>
 * 联系作者:<a href="mailto:747673016@qq.com">@author majun</a><br/>
 * ======================================================<br/>
 * ======================================================<br/>
 */
@SuppressLint("InflateParams")
public class ChoiceLevel extends Dialog implements
		android.view.View.OnClickListener {
	// private final String TAG = ChoiceLevel.class.getSimpleName();
	private Context context;

	public ChoiceLevel(Context context, int theme) {
		super(context, R.style.OverlayDialogStyleTheme);
		this.context = context;
	}
	public ChoiceLevel(Context context) {
		super(context, R.style.OverlayDialogStyleTheme);
		this.context = context;
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init();
		// getWindow().setSoftInputMode(
		// WindowManager.LayoutParams.SOFT_INPUT_MASK_ADJUST);
	}

	public ListView chatLogLstv;
	public LinearLayout searchNoResult;
	private ListView popListView;

	/** 初始化工作 */
	private void init() {
		setContentView(R.layout.pop_layout);
		initView();
		initData();
		/** 延时 开启 软键盘 ,否则弹起软键盘失败 */
		openSoftBoard();
	}

	private void openSoftBoard() {
	}

	private void initData() {
	}

	private void initView() {
		popListView = (ListView)findViewById(R.id.mini_pop_listview);
		
		fillData();
	}

	private List<String> list;
	private PopAdapter adapter;

	/**
	 */
	private void fillData() {
		list = new ArrayList<String>();
		// 40, 42, 45, 47, 50, 60
		list.add("40颗");
		list.add("42颗");
		list.add("45颗");
		list.add("47颗");
		list.add("50颗");
		list.add("60颗");
		list.add("随机雷");
		initAdapter();
		popListView.setAdapter(adapter);
		popListView.setFocusableInTouchMode(true);
		popListView.setFocusable(true);
	}

	public void setOnItemClickListener(OnItemClickListener listener) {
		popListView.setOnItemClickListener(listener);
	}

	public void addItems(List<String> list) {
		this.list.clear();
		if (list != null && list.size() != 0) {
			for (String string : list) {
				this.list.add(string);
			}
		}
		initAdapter();
		popListView.setAdapter(adapter);
		popListView.setFocusableInTouchMode(true);
		popListView.setFocusable(true);
	}

	public void addItem(String string) {
		list.add(string);
	}

	private void initAdapter() {
		adapter = new PopAdapter(list, context);
	}

	@Override
	public void onClick(View view) {
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}

}