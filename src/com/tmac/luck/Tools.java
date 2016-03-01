package com.tmac.luck;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * 功能概述:工具类封装<br/>
 * 创建日期：2015年7月3日上午11:47:46<br/>
 * 创建作者：majun<br/>
 * 联系作者:<a href="mailto:747673016@qq.com">@author majun</a><br/>
 * ======================================================<br/>
 * ======================================================<br/>
 */
public class Tools {

	/**
	 * @description :获取版本�?
	 */
	public static String getVersionInfo(Context context) {
		PackageManager packageManager = context.getPackageManager();
		try {
			PackageInfo packageInfo = packageManager.getPackageInfo(
					context.getPackageName(), 0);
			return packageInfo.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * @description :获取应用名称
	 */
	public static String getApplicationName(Activity context) {
		PackageManager packageManager = null;
		ApplicationInfo applicationInfo = null;
		try {
			packageManager = context.getPackageManager();
			applicationInfo = packageManager.getApplicationInfo(
					context.getPackageName(), 0);
		} catch (PackageManager.NameNotFoundException e) {
			applicationInfo = null;
		}
		return (String) packageManager.getApplicationLabel(applicationInfo);
	}

	/** 非空判断 */
	public static boolean isEmpty(String str) {
		return TextUtils.isEmpty(str);
	}

	/** �?��浏览�? */
	public static void openBrowser(Context context, Uri uri) {
		Uri mUri = Uri.parse("http://www.mumayi.com/android-680519.html");
		Intent in = new Intent(Intent.ACTION_VIEW, mUri);
		((Activity) context).startActivity(in);
	}

	public static void activityJumpForResultWithBundle(Activity context,
			Class<?> clazz, int requestCode, Bundle bundle) {
		Intent intent = new Intent(context, clazz);
		if (bundle != null) {
			intent.putExtras(bundle);
		}
		context.startActivityForResult(intent, requestCode);
		/** 取消系统默认的动画效果 */
		((Activity) context).overridePendingTransition(Animation.INFINITE,
				Animation.INFINITE);
	}

	public static void jumpForResultWithAnim(Activity context, Class<?> clazz,
			int requestCode) {
		// Intent intent = new Intent(context, clazz);
		// context.startActivityForResult(intent, requestCode);
		// ((Activity) context).overridePendingTransition(R.anim.slide_right_in,
		// R.anim.mini_hold);
		jumpForResulting(context, clazz, requestCode, null);
	}

	public static void jumpForResulting(Activity context, Class<?> clazz,
			int requestCode, Bundle bundle) {
		Intent intent = new Intent(context, clazz);
		if (bundle != null) {
			intent.putExtras(bundle);
		}
		context.startActivityForResult(intent, requestCode);
		// ((Activity) context).overridePendingTransition(R.anim.slide_right_in,
		// R.anim.mini_hold);
	}

	/**
	 * 
	 * @date :2015-3-23 上午11:49:49
	 * @param:@param context
	 * @param:@param clazz
	 * @param:@param requestCode
	 * @return :@param context
	 * @return :@param clazz
	 * @return :@param requestCode
	 * @description :up 方式 弹出
	 */
	public static void jumpForResulWithAnim(Activity context, Class<?> clazz,
			int requestCode) {
		Intent intent = new Intent(context, clazz);
		context.startActivityForResult(intent, requestCode);
		// ((Activity) context).overridePendingTransition(R.anim.up, 0);
	}

	/**
	 * 
	 * @date :2015-3-23 下午4:55:15
	 * @param:@param context
	 * @param:@param clazz
	 * @param:@param requestCode
	 * @param:@param bundle
	 * @return :@param context
	 * @return :@param clazz
	 * @return :@param requestCode
	 * @return :@param bundle
	 * @description :up 方式 弹出
	 */
	public static void jumpActivityWithAnim(Activity context, Class<?> clazz,
			Bundle bundle) {
		Intent intent = new Intent(context, clazz);
		if (bundle != null) {
			intent.putExtras(bundle);
		}
		// context.startActivityForResult(intent, requestCode);
		context.startActivity(intent);
		// ((Activity) context).overridePendingTransition(R.anim.up, 0);
	}

	private static final float TARGET_WIDTH = 400;
	private static final float TARGET_HEIGHT = 200;

	/** 按比例缩放图片 */
	public static Bitmap zoomBitmap(Bitmap target) {
		int width = target.getWidth();
		int height = target.getHeight();
		Matrix matrix = new Matrix();
		float scaleWidth = ((float) TARGET_WIDTH) / width;
		float scaleHeight = ((float) TARGET_HEIGHT) / height;
		matrix.postScale(scaleWidth, scaleHeight);
		Bitmap result = Bitmap.createBitmap(target, 0, 0, width, height,
				matrix, true);
		return result;
	}

	/**
	 * 获取手机是否链接网络
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isConnection(Context context) {
		ConnectivityManager manager = getConnectivityManager(context);
		if (manager == null) {
			return false;
		}
		NetworkInfo networkInfo = manager.getActiveNetworkInfo();
		boolean isAvailable;
		if (networkInfo != null) {
			isAvailable = networkInfo.isAvailable();
		} else {
			isAvailable = false;
		}
		return isAvailable;
	}

	/**
	 * 获取手机联网的类型
	 * 
	 * @param context
	 */
	public static String getConnectionType(Context context) {
		boolean connection = isConnection(context);
		if (connection) {
			ConnectivityManager manager = getConnectivityManager(context);
			NetworkInfo networkInfo = manager.getActiveNetworkInfo();
			String typeName = networkInfo.getTypeName();
			Log.i("ConnectionVerdict", typeName);
			return typeName;
		} else {
			return null;
		}
	}

	/**
	 * 判断WiFi开关是否打开
	 * 
	 * @return
	 */
	public boolean isWifiEnabled(Context context) {
		ConnectivityManager mgrConn = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		TelephonyManager mgrTel = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		NetworkInfo info = mgrConn.getActiveNetworkInfo();
		return ((info != null && info.getState() == NetworkInfo.State.CONNECTED) || mgrTel
				.getNetworkType() == TelephonyManager.NETWORK_TYPE_UMTS);
	}

	/**
	 * 判断当前使用的网络是否WiFi
	 * 
	 * @return
	 */
	public static boolean isWifi(Context context) {
		ConnectivityManager manager = getConnectivityManager(context);
		NetworkInfo networkINfo = manager.getActiveNetworkInfo();
		if (networkINfo != null
				&& networkINfo.getType() == ConnectivityManager.TYPE_WIFI) {
			return true;
		}
		return false;
	}

	/**
	 * 判断当前链接的网络是否是手机流量网络
	 * 
	 * @return
	 */
	public static boolean isMobile(Context context) {
		ConnectivityManager manager = getConnectivityManager(context);
		NetworkInfo networkINfo = manager.getActiveNetworkInfo();
		if (networkINfo != null
				&& networkINfo.getType() == ConnectivityManager.TYPE_MOBILE) {
			return true;
		}
		return false;
	}

	/**
	 * 获取联网的Manager
	 * 
	 * @param context
	 * @return
	 */
	private static ConnectivityManager getConnectivityManager(Context context) {
		ConnectivityManager mConnectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (mConnectivityManager == null) {
			return null;
		}
		return mConnectivityManager;
	}

	/**
	 * 判断网络是否连接
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isConnect(Context context) {
		boolean isConnected = false;
		ConnectivityManager manager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		State mobile = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
				.getState();
		State wifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
				.getState();
		if (mobile == State.CONNECTED || mobile == State.CONNECTING) {
			isConnected = true;
		} else if (wifi == State.CONNECTED || wifi == State.CONNECTING) {
			isConnected = true;
		}
		return isConnected;
	}

	/**
	 * @return 当前系统秒钟
	 */
	public static String currentSecond() {
		long time = System.currentTimeMillis();
		String formatTime = "00";
		SimpleDateFormat format = new SimpleDateFormat("ss",
				Locale.getDefault());
		formatTime = format.format(time);
		return formatTime;
	}

	/**
	 * @return 当前系统时间
	 */
	public static String getCurrentTime() {
		long time = System.currentTimeMillis();
		String formatTime = "00";
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
				Locale.getDefault());
		formatTime = format.format(time);
		return formatTime;
	}

	/**
	 * @return 当前系统秒钟
	 */
	public static String currentDay() {
		long time = System.currentTimeMillis();
		String formatTime = "00";
		SimpleDateFormat format = new SimpleDateFormat("dd",
				Locale.getDefault());
		formatTime = format.format(time);
		return formatTime;
	}

	/**
	 * 获取时间差 long[0]天long[1]小时long[2]分long[3]秒long[4]异常 异常代表时间到
	 */
	@SuppressLint("SimpleDateFormat")
	public static long[] getDeltaTime(String nowTime, String passedTime, long tm) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		long[] result = new long[5];
		try {
			java.util.Date now = df.parse(nowTime);
			java.util.Date date = df.parse(passedTime);
			Log.e("JULY", "expire:" + (date.getTime() + tm));
			Log.e("JULY", "   now:" + now.getTime());
			long l = (date.getTime() + tm) - now.getTime();
			Log.e("JULY", "     l:" + l);
			if (l <= 0) {
				result[4] = -1;
				return result;
			}
			long day = l / (24 * 60 * 60 * 1000);
			long hour = (l / (60 * 60 * 1000) - day * 24);
			long min = ((l / (60 * 1000)) - day * 24 * 60 - hour * 60);
			long s = (l / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
			result[0] = day;
			result[1] = hour;
			result[2] = min;
			result[3] = s;
		} catch (Exception e) {
			result[4] = -1;
		}
		return result;
	}

	/** 虚拟返回菜单键实际上就是navigation_bar */
	public static int getNavigationBarHeight(Activity mActivity) {
		Resources resources = mActivity.getResources();
		int resourceId = resources.getIdentifier("navigation_bar_height",
				"dimen", "android");
		int height = resources.getDimensionPixelSize(resourceId);
		return height;
	}

	/** 获取imageview中显示的图像 */
	public static Bitmap getBitmapFromImageView(ImageView imageView) {
		if (imageView != null) {
			imageView.setDrawingCacheEnabled(true);
			Bitmap drawingCache = imageView.getDrawingCache();
			if (drawingCache != null) {
				Bitmap bitmap = Bitmap.createBitmap(drawingCache);
				imageView.setDrawingCacheEnabled(false);
				return bitmap;
			}
			imageView.setDrawingCacheEnabled(false);
			return null;
		} else {
			return null;
		}
	}

	/** 屏幕dp输入目标值 */
	public static float destinationValue(Context context, int goalValue) {
		return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
				goalValue, context.getResources().getDisplayMetrics());
	}

	/** 获取指定dip值 */
	public static int getDipValue(Context context, int des) {
		DisplayMetrics metrics = context.getResources().getDisplayMetrics();
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
				des, metrics);
	}

	// 获取包名 版本号 市场
	public static String getPVM(Context context) {
		String version; // 版本号
		String packageNames; // 包名
		String market; // 市场
		ApplicationInfo ainfo;
		PackageInfo info;
		String pvm = "";
		try {
			PackageManager manager = context.getPackageManager();
			info = manager.getPackageInfo(context.getPackageName(), 0);
			ainfo = context.getPackageManager().getApplicationInfo(
					context.getPackageName(), PackageManager.GET_META_DATA);
			version = info.versionName;
			packageNames = info.packageName;
			market = ainfo.metaData.getString("UMENG_CHANNEL");
			pvm = "&p=" + packageNames + "&v=" + version + "&m=" + market;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return pvm;
	}

	// 获取包名 版本号 市场
	public static String getAdvtPVM(Context context) {
		String version; // 版本号
		String packageNames; // 包名
		String market; // 市场
		ApplicationInfo ainfo;
		PackageInfo info;
		String pvm = "";
		// &PackageName=com.pcpop.popapk&Version=3.6.2&Channel=360
		try {
			PackageManager manager = context.getPackageManager();
			info = manager.getPackageInfo(context.getPackageName(), 0);
			ainfo = context.getPackageManager().getApplicationInfo(
					context.getPackageName(), PackageManager.GET_META_DATA);
			version = info.versionName;
			packageNames = info.packageName;
			market = ainfo.metaData.getString("UMENG_CHANNEL");
			pvm = "&PackageName=" + packageNames + "&Version=" + version
					+ "&Channel=" + market;
		} catch (Exception e) {
		}
		return pvm;
	}

	public static boolean rename(Context context, String oldName, String newName) {
		File filesDir = context.getFilesDir();
		if (filesDir == null) {
			return false;
		}
		// renameFile(filesDir.getAbsolutePath(), "teok", "teok.zip");
		return renameFile(filesDir.getAbsolutePath(), oldName, newName);
	}

	public static void unZipFile(Context context, String finalName)
			throws IOException {
		File filesDir = context.getFilesDir();
		if (filesDir == null) {
			return;
		}
		String out = filesDir.getAbsolutePath() + File.separator
				+ finalName.replace(".zip", "");
		File file = new File(out);
		if (file.exists() && file.length() > 0) {
			file.delete();
		}
		if (!file.exists()) {
			file.mkdirs();
		}
		InputStream inputStream = null;
		// renameFile(filesDir.getAbsolutePath(), "teok", "teok.zip");
		inputStream = new FileInputStream(filesDir.getAbsolutePath()
				+ File.separator + finalName);
		ZipInputStream zipInputStream = new ZipInputStream(inputStream);
		ZipEntry zipEntry = zipInputStream.getNextEntry();
		byte[] buffer = new byte[1024 * 1024];
		int count = 0;
		while (zipEntry != null) {
			if (zipEntry.isDirectory()) {
				file = new File(out + File.separator + zipEntry.getName());
				file.mkdir();
			} else {
				file = new File(out + File.separator + zipEntry.getName());
				file.createNewFile();
				FileOutputStream fileOutputStream = new FileOutputStream(file);
				while ((count = zipInputStream.read(buffer)) > 0) {
					fileOutputStream.write(buffer, 0, count);
				}
				fileOutputStream.close();
			}
			zipEntry = zipInputStream.getNextEntry();
		}
		zipInputStream.close();
	}

	public static boolean renameFile(String path, String oldname, String newname) {
		if (!oldname.equals(newname)) {// 新的文件名和以前文件名不同时,才有必要进行重命名
			File oldfile = new File(path + File.separator + oldname);
			File newfile = new File(path + File.separator + newname);
			if (!oldfile.exists()) {
				return false;// 重命名文件不存在
			}
			if (!newfile.exists()) {
				return oldfile.renameTo(newfile);
			}
		} else {
		}
		return false;
	}

	public static void showToast(Context context, String string) {
		Toast.makeText(context, string, 0).show();
	}

	public static void showNetisDead(Context mContext) {
		showToast(mContext, "网络未连接");
	}

}
