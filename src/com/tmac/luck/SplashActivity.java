package com.tmac.luck;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.tmac.R;

public class SplashActivity extends Activity {
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main);
		SharedPreferences mShpf = PreferenceManager
				.getDefaultSharedPreferences(this);
		boolean isOpen = mShpf.getBoolean("isOpen", true);
		if (!isOpen) {
			Intent mIntent = new Intent(this, MainActivity.class);
			startActivity(mIntent);
			finish();
		}
	}

	public void ctx() {
		Window win = getWindow();
		WindowManager.LayoutParams params = win.getAttributes();
		win.setBackgroundDrawable(new ColorDrawable(0x00ff0000));
		params.gravity = Gravity.CENTER;
		DisplayMetrics mDisplayMetrics = new DisplayMetrics();
		((WindowManager) this.getSystemService(Context.WINDOW_SERVICE))
				.getDefaultDisplay().getMetrics(mDisplayMetrics);
		params.height = (int) (mDisplayMetrics.density * 300);
		params.width = (int) (mDisplayMetrics.density * 300);

		win.setAttributes(params);

	}

}
