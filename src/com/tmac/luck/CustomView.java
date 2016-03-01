package com.tmac.luck;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tmac.R;

public class CustomView extends FrameLayout implements OnClickListener {

	private ViewPager vPager;
	private TextView register;
	private TextView login;
	private int width;
	private int height;
	private List<View> list;
	private SharedPreferences mShpf;

	public CustomView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public CustomView(Context context) {
		super(context);
		init(context);
	}

	public void init(Context context) {
		View.inflate(context, R.layout.activity_main, this);
		mShpf = PreferenceManager.getDefaultSharedPreferences(context);
		initView();
		initData();
		
	}

	public void initData() {
		View mOneView = View.inflate(getContext(), R.layout.iew, null);
		View mTwoView = View.inflate(getContext(), R.layout.iew, null);
		View mThrView = View.inflate(getContext(), R.layout.iew, null);
		ImageView img = (ImageView) mOneView.findViewById(R.id.img);
		ImageView img2 = (ImageView) mTwoView.findViewById(R.id.img);
		ImageView img3 = (ImageView) mThrView.findViewById(R.id.img);
		img.setBackgroundResource(R.drawable.readme);
		img2.setBackgroundResource(R.drawable.read_2);
		img3.setBackgroundResource(R.drawable.read_m3);
		list = new ArrayList<View>();
		list.add(mThrView);
		list.add(mOneView);
		list.add(mTwoView);
		vPager.setAdapter(new IPagerAdapter());
	}

	private void initView() {
		vPager = (ViewPager) findViewById(R.id.vPager);
		login = (TextView) findViewById(R.id.login);
		register = (TextView) findViewById(R.id.register);
		login.setOnClickListener(this);
		register.setOnClickListener(this);
		boolean isOpen = mShpf.getBoolean("isOpen", true);
		if (isOpen) {
			register.setText("教程已开");
		} else {
			register.setText("教程已关");
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.login:
			Toast.makeText(getContext(), "abandon", 0).show();
			Intent mIntent = new Intent(getContext(), MainActivity.class);
			getContext().startActivity(mIntent);
			((Activity) getContext()).finish();
			break;
		case R.id.register:
			boolean isOpen = mShpf.getBoolean("isOpen", true);
			mShpf.edit().putBoolean("isOpen", !isOpen).commit();
			if (!isOpen) {
				register.setText("教程已开");
				Toast.makeText(getContext(), "教程开", 0).show();
			} else {
				register.setText("教程已关");
				Toast.makeText(getContext(), "教程关", 0).show();
			}
			break;
		}
	}

	class IPagerAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			container.addView(list.get(position));
			return list.get(position);
		}

	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
		width = getWidth();
		height = getHeight();

		Log.e("JULY", "width=" + width);
		Log.e("JULY", "getChildCount=" + getChildCount());
		for (int i = 0; i < getChildCount(); i++) {
			View childView = getChildAt(i);
			if (childView instanceof TextView) {
				int childW = childView.getWidth();
				int childH = childView.getHeight();
				int top = height / 3 * 2 + ((height - height / 3 * 2) - childH)
						/ 2;
				int lf = (width - (childW * 2)) / 3;
				if (childView == login) {
					childView.layout(lf, top, lf + childW, top + childH);
				} else if (childView == register) {
					lf = lf + lf + childW;
					childView.layout(lf, top, lf + childW, top + childH);
				}
			}
		}
	}

}
