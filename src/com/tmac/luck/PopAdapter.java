package com.tmac.luck;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tmac.R;

public class PopAdapter extends BaseAdapter {
	private List<String> list = new ArrayList<String>();
	private Context context;

	public PopAdapter(List<String> list, Context context) {
		super();
		this.list = list;
		this.context = context;
	}

	private boolean useNewItem;

	public PopAdapter(List<String> list, Context context, boolean useNewItem) {
		super();
		this.list = list;
		this.context = context;
		this.useNewItem = useNewItem;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public void notifya(){
		
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = View.inflate(context, R.layout.title, null);
		}
		TextView test = Tools.getWidget(convertView, R.id.who);
		if (useNewItem) {
		}
		test.setGravity(Gravity.CENTER);
		//test.setBackgroundColor(color)
		//test.setBackground(null);
		//test.setBackgroundDrawable(null);
		test.setTextColor(Color.parseColor("#450945"));
		test.setText(list.get(position) + "");
		return convertView;
	}

}
