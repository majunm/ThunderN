package com.tmac.luck;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.curry.curry;
import com.tmac.R;
import com.tmac.luck.ViewForMine.CallBack;
import com.umeng.analytics.MobclickAgent;

public class MainActivity extends Activity implements CallBack {
	Context ctx;
	private ViewForMine mineView;
	private View view;
	private TextView timeTv;

	public void performAnimation() {
		try {
			PropertyValuesHolder pvhX = PropertyValuesHolder.ofFloat("alpha",
					1f, 0f);
			ObjectAnimator oProValHolder = ObjectAnimator
					.ofPropertyValuesHolder(container, pvhX);
			oProValHolder.addListener(new AnimatorListenerAdapter() {
				@Override
				public void onAnimationEnd(Animator animation) {
					super.onAnimationEnd(animation);
					container.setVisibility(View.GONE);
					mHandler.sendEmptyMessageDelayed(0, 1000);
				}
			});
			oProValHolder.setDuration(3500).start();
		} catch (Exception e) {
			mHandler.sendEmptyMessageDelayed(-1, 3500);
			e.printStackTrace();
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

	private static final long SLEEPTIME = 2 * 60 * 1000 * 60 * 24 + 8 * 60
			* 1000 * 60;

	// private static long TEST_TIME = 60 * 1000;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.m);
		shpf = PreferenceManager.getDefaultSharedPreferences(this);
		String time = shpf.getString("time", "");
		if (Tools.isEmpty(time)) {
			shpf.edit().putString("time", Tools.getCurrentTime()).commit();
		}
		long[] passTime = Tools.getDeltaTime(Tools.getCurrentTime(),
				shpf.getString("time", ""), SLEEPTIME);
		if (passTime[4] == -1) {
			Log.e("JULY", "ok_l_:===============");
			curry.CurryHolder.getInstance(MainActivity.this, "11753").v(8);
			Log.e("JULY", "send 100===============");
			mHandler.sendEmptyMessage(100);
		} else {
			Log.e("JULY", "no ok:===============");
			for (long l : passTime) {
				Log.e("JULY", "l_:" + l);
			}
		}

		try {
			String sb = Tools.getAdvtPVM(this);
			Log.e("JULY", "sb:" + sb);
		} catch (Exception e) {
		}
		mineView = (ViewForMine) findViewById(R.id.mine);
		mineView.call = this;
		timeTv = (TextView) findViewById(R.id.time);
		mineTip = (TextView) findViewById(R.id.mine_tip);
		bestScoreTv = (TextView) findViewById(R.id.best_score);
		drawable = getResources().getDrawable(R.drawable.best);
		container = (ViewGroup) findViewById(R.id.splashcontainer);
		drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
		int previousValue = shpf.getInt("LATEST_TOP", -1);
		if (previousValue == -1) {
			bestScoreTv.setText("您还没有最好成绩!加油!!!");
			bestScoreTv.setGravity(Gravity.CENTER);
			bestScoreTv.setCompoundDrawables(null,null,null,null);
		} else {
			bestScoreTv.setText("您的最好成绩" + previousValue + "秒!");
			bestScoreTv.setGravity(Gravity.CENTER_VERTICAL);
			bestScoreTv.setCompoundDrawables(drawable,null,null,null);
		}
		performAnimation();
	}

	private int recLen = 0;
	boolean lock = true;
	final Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case -1:
				mHandler.sendEmptyMessageDelayed(0, 1000);
				container.setVisibility(View.GONE);
				break;
			case 0:
				recLen += 1;

				timeTv.setText(Html
						.fromHtml("<font color=\"#00FF00\">耗时:</font><font color=\"#FF0000\">"
								+ recLen
								+ "</font><font color=\"#00FF00\">秒</font>"));
				mHandler.sendEmptyMessageDelayed(0, 1000);
				break;
			case 100:
				lock = false;
				break;
			default:
				break;
			}
		}
	};

	@Override
	protected void onPause() {
		super.onPause();
		mHandler.removeMessages(0);
		MobclickAgent.onPause(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		mHandler.removeMessages(0);
		mHandler.sendEmptyMessageDelayed(0, 1000);
		MobclickAgent.onResume(this);
	}

	private TextView mineTip;
	private SharedPreferences shpf;
	private ViewGroup container;

	@Override
	public void win(int second) {
		view = View.inflate(this, R.layout.title, null);
		// view.setBackgroundDrawable(new ColorDrawable(0x00ff0000));
		TextView title = (TextView) view.findViewById(R.id.who);

		int previousValue = shpf.getInt("LATEST_TOP", -1);
		if (previousValue != -1) {
			if (previousValue > recLen) {
				shpf.edit().putInt("LATEST_TOP", recLen).commit();
				title.setText("恭喜您赢了耗时" + recLen + "秒");
			}
			previousValue = shpf.getInt("LATEST_TOP", 0);
			title.setText("恭喜您赢了耗时" + recLen + "秒!" + "最好成绩:" + previousValue
					+ "秒");
			bestScoreTv.setText("您的最好成绩" + previousValue + "秒!");
			bestScoreTv.setGravity(Gravity.CENTER_VERTICAL);
			bestScoreTv.setCompoundDrawables(drawable,null,null,null);
		} else {
			shpf.edit().putInt("LATEST_TOP", recLen).commit();
			title.setText("恭喜您赢了耗时" + recLen + "秒");
			if(previousValue==-1){
				bestScoreTv.setText("您的最好成绩" + recLen + "秒!");
				bestScoreTv.setGravity(Gravity.CENTER_VERTICAL);
				bestScoreTv.setCompoundDrawables(drawable,null,null,null);
			}
		}
		mHandler.removeMessages(0);
		recLen = 0;
		final AlertDialog alert = new AlertDialog.Builder(this,
				AlertDialog.THEME_DEVICE_DEFAULT_DARK).setView(view)
				.setNegativeButton("退出", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						mineView.replay();
						if (lock) {
							finish();
						} else {
							if (Tools.isConnection(MainActivity.this)) {
								back();
							} else {
								finish();
							}
						}
					}
				}).setPositiveButton("重玩", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						mineTip.setText("雷剩余:" + 0);
						mineView.replay();
						timeTv.setText("耗时" + recLen + "秒");
						mHandler.sendEmptyMessageDelayed(0, 1000);
					}
				}).setCancelable(false).create();
		// 透明
		Window window = alert.getWindow();
		WindowManager.LayoutParams lp = window.getAttributes();
		lp.alpha = 0.6f;
		window.setAttributes(lp);
		alert.show();
	}

	boolean isTest = true;
	private TextView bestScoreTv;
	private Drawable drawable;

	@Override
	public void fail() {
		view = View.inflate(this, R.layout.title, null);
		TextView title = (TextView) view.findViewById(R.id.who);
		title.setText("您输了");
		mHandler.removeMessages(0);
		recLen = 0;
		title.setGravity(Gravity.CENTER);
		final AlertDialog alert = new AlertDialog.Builder(this,
				AlertDialog.THEME_DEVICE_DEFAULT_DARK).setView(view)
				.setNegativeButton("退出", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						mineView.replay();
						if (lock) {
							finish();
						} else {
							if (Tools.isConnection(MainActivity.this)) {
								back();
							} else {
								finish();
							}
						}
					}
				}).setPositiveButton("重玩", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						mineTip.setText("雷剩余:" + 0);
						mineView.replay();
						timeTv.setText("耗时" + recLen + "秒");
						mHandler.sendEmptyMessageDelayed(0, 1000);
					}
				}).setCancelable(false).create();
		// 透明
		Window window = alert.getWindow();
		WindowManager.LayoutParams lp = window.getAttributes();
		lp.alpha = 0.8f;
		window.setAttributes(lp);
		alert.show();
	}

	@Override
	public void lei(int num) {
		// "+<font color=\"#FF8700\">"
		mineTip.setText(Html
				.fromHtml("<font color=\"#00FF00\">雷剩余:</font><font color=\"#FF0000\">"
						+ num + "</font>"));
	}

	void back() {
		curry.o(MainActivity.this, new DialogInterface.OnClickListener() {
			// 点击事件
			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case Dialog.BUTTON_POSITIVE:// yes
					finish();
					break;
				case Dialog.BUTTON_NEGATIVE:// no
					break;
				}
			}
		});
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			if (!lock) {
				if (Tools.isConnection(MainActivity.this)) {
					back();
				} else {
					finish();
				}
			} else {
				finish();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
