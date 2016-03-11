package com.tmac.luck;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import com.tmac.R;

/**
 * 
 * @author 1
 */
@SuppressLint("DrawAllocation")
public class ThunderView extends View {
	private static int MATRIXLEN = 14;
	/** 矩阵 */
	public int[][] matrix = new int[MATRIXLEN + 3][MATRIXLEN];

	int[][] mines = new int[matrix.length][matrix[0].length];
	/** 画笔 */
	private Paint mPaint;
	private Paint numPaint;
	/** 屏幕宽 */
	private int width;
	/** 屏幕高 */
	private int height;
	/** 初始化 */
	private boolean isInit;
	/** 起始点x */
	private float startX;
	/** 起始点y */
	private float startY;
	/** 矩形的宽度 */
	private static float RECT_WIDTH = 80;
	/** 矩形的高度 */
	private static float RECT_HEIGHT = 80;
	private IPoint[][] numIPoints;
	private int[][] seed = new int[matrix.length][matrix[0].length];
	RectF[][] rects = new RectF[matrix.length][matrix[0].length];
	/** 触摸状态 */
	private TouchStatus[][] touchStatus = new TouchStatus[matrix.length][matrix[0].length];
	int[] colors = new int[] { Color.parseColor("#414fbc"),// 1
			Color.parseColor("#206803"),// 2
			Color.parseColor("#af0204"),// 3
			Color.parseColor("#010182"),// 4
			Color.parseColor("#6b030c"), // 5
			Color.parseColor("#690305"), // 6
			Color.parseColor("#77830c"), // 7
			Color.parseColor("#84560c"), // 8
			Color.parseColor("#99990c"), // 9
			Color.parseColor("#09990c") // 10
	};
	private boolean isLock = false;
	Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 10:
				loadData();
				break;
			case 101:
				isperformLongTask = false;
				break;
			default:
				invalidate();
				break;
			}
		}
	};
	public static int TOTAL_NUM = 0;
	public static int MINE_NUM = 0;
	public static int RESULT_NUM = 0;
	public static int THUNDER_COUNTER = 0;
	public int level = 0;
	/** 雷的数目 */
	int[] level_thunder_num = { 0, 40, 42, 45, 47, 50, 60 };

	public void loadData() {
		TOTAL_NUM = 0;
		MINE_NUM = 0;
		THUNDER_COUNTER = 0;
		new Thread() {
			public void run() {
				for (int i = 0; i < matrix.length; i++) {
					for (int j = 0; j < matrix[i].length; j++) {
						int rd = Math.random() > 0.5 ? 0 : 1;
						// rd = new Random().nextInt(5);
						mines[i][j] = rd;
						TOTAL_NUM += 1;
					}
				}
				// 将地雷标记为9
				for (int i = 0; i < matrix.length; i++) {
					for (int j = 0; j < matrix[i].length; j++) {
						if (mines[i][j] == 1) {
							int rd = Math.random() > 0.5 ? 0 : 1;
							mines[i][j] = rd;
						}
					}
				}
				for (int i = 0; i < matrix.length; i++) {
					for (int j = 0; j < matrix[i].length; j++) {
						if (mines[i][j] == 1) {
							int rd = Math.random() > 0.5 ? 0 : 1;
							mines[i][j] = rd;
						}
					}
				}
				// 将地雷标记为9
				for (int i = 0; i < matrix.length; i++) {
					for (int j = 0; j < matrix[i].length; j++) {
						if (mines[i][j] == 1) {
							mines[i][j] = 9;
							THUNDER_COUNTER += 1;
							MINE_NUM += 1;
						}
					}
				}
				levelDispose(MINE_NUM, level_thunder_num[level]);

				System.out.println("===================");
				for (int i = 0; i < mines.length; i++) {
					for (int j = 0; j < mines[i].length; j++) {
						if (j % mines.length == 0)
							System.out.println();
						System.out.print(mines[i][j]);
					}
				}
				System.out.println();
				System.out.println("===================");
				// 初始化seed,全部赋值0
				for (int i = 0; i < matrix.length; i++) {
					for (int j = 0; j < matrix[i].length; j++) {
						seed[i][j] = 0;
					}
				}
				mineCreate(mines, seed);
				for (int i = 0; i < seed.length; i++) {
					for (int j = 0; j < seed[i].length; j++) {
						if (j % seed.length == 0)
							System.out.println();
						System.out.print(seed[i][j]);
					}
				}
				mHandler.sendEmptyMessage(0);
			}
		}.start();
	}

	/**
	 * 难度,雷的处理
	 */
	public void levelDispose(int num, int maxThunderNum) {
		if (num == 0 || maxThunderNum == 0) {
			return;
		}
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[i].length; j++) {
				if (mines[i][j] == 9) {
					continue;
				}
				int rd = Math.random() > 0.5 ? 0 : 1;
				mines[i][j] = rd;
			}
		}
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[i].length; j++) {
				if (mines[i][j] == 1) {
					int rd = Math.random() > 0.5 ? 0 : 1;
					mines[i][j] = rd;
				}
			}
		}
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[i].length; j++) {
				if (mines[i][j] == 1) {
					int rd = Math.random() > 0.5 ? 0 : 1;
					mines[i][j] = rd;
				}
			}
		}
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[i].length; j++) {
				if (THUNDER_COUNTER == maxThunderNum) {
					break;
				}
				if (mines[i][j] == 1) {
					mines[i][j] = 9;
					THUNDER_COUNTER += 1;
					MINE_NUM += 1;
				}
			}
		}
		// 25 +20 =45 < 60
		if (THUNDER_COUNTER <= maxThunderNum) {
			levelDispose(maxThunderNum - THUNDER_COUNTER, maxThunderNum);
		}
	}

	public void init(Context context) {
		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		displayMetrics = context.getResources().getDisplayMetrics();
		float strokeWidth = TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 2, displayMetrics);
		mPaint.setColor(Color.BLACK);
		mPaint.setStrokeWidth(strokeWidth);
		mPaint.setStyle(Style.STROKE);
		mFlagRed = BitmapFactory.decodeResource(getResources(),
				R.drawable.flag_red_).copy(Bitmap.Config.ARGB_8888, true);
		mMineIcon = BitmapFactory.decodeResource(getResources(),
				R.drawable.mine).copy(Bitmap.Config.ARGB_8888, true);
		mBmpWidth = mFlagRed.getWidth();
		mBmpHeight = mFlagRed.getHeight();
		numPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		numPaint.setStyle(Style.STROKE);
		numPaint.setColor(Color.GREEN);
		numPaint.setTextSize(TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 10, displayMetrics));
		numIPoints = new IPoint[matrix.length][matrix[0].length];

		System.out.println();
		System.out.println("========================================");
		// 打印地雷图
		// Log.e("DEBUG", "init....................");
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		width = getWidth();
		height = getHeight();
	}

	public ThunderView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context);
	}

	public ThunderView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public ThunderView(Context context) {
		super(context);
		init(context);
	}

	public interface CallBack {
		public void win(int second);

		public void fail();

		public void lei(int num);
	}

	public CallBack call;

	public ThunderView(Context context, CallBack call) {
		super(context);
		this.call = call;
		init(context);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		if (!isInit) {
			if (mp == null) {
				mp = new Paint(Paint.ANTI_ALIAS_FLAG);
				mp.setColor(Color.RED);
				mp.setStyle(Style.FILL);
				mp.setTextSize(TypedValue.applyDimension(
						TypedValue.COMPLEX_UNIT_DIP, 12, displayMetrics));
			}
			if (matrix.length * RECT_HEIGHT >= height - 50) {
				RECT_HEIGHT = (height - 50) / matrix.length;
				Log.e("TEST", "--" + RECT_HEIGHT);
			}
			if (matrix[0].length * RECT_WIDTH >= width - 50) {
				RECT_WIDTH = (width - 50) / matrix[0].length;
			}
			if (RECT_HEIGHT < RECT_WIDTH) {
				RECT_HEIGHT = RECT_WIDTH;
			}
			numPaint.setTextSize(TypedValue.applyDimension(
					TypedValue.COMPLEX_UNIT_DIP, 12, displayMetrics));
			loadData();
			isInit = true;
			startX = (width - matrix[0].length * RECT_WIDTH) / 2;
			startY = (height - matrix.length * RECT_HEIGHT) / 2;
			for (int i = 0; i < matrix.length; i++) {
				for (int j = 0; j < matrix[i].length; j++) {
					float xPonit = startX + RECT_WIDTH * j;
					float yPonit = startY + RECT_HEIGHT * i;
					float xEndPoint = startX + RECT_WIDTH * (j + 1);
					float yEndPoint = startY + RECT_HEIGHT * (i + 1);
					numIPoints[i][j] = new IPoint(xPonit, yPonit, xEndPoint,
							yEndPoint);

				}
			}
		}

		Log.e("DEBUG", "width=" + width);
		Log.e("DEBUG", "startX=" + startX);
		Log.e("DEBUG", "startY=" + startY);

		if (p == null) {
			p = new Paint(Paint.ANTI_ALIAS_FLAG);
		}
		LinearGradient lg = null;
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[i].length; j++) {
				IPoint iPonit = numIPoints[i][j];
				RectF rect = new RectF(startX + RECT_WIDTH * j, startY
						+ RECT_HEIGHT * i, startX + RECT_WIDTH * (j + 1),
						startY + RECT_HEIGHT * (i + 1));
				rects[i][j] = rect;
				if (touchStatus[i][j] == null) {
					touchStatus[i][j] = TouchStatus.NORMAL;
				}
				canvas.drawRect(rect, mPaint);
				int mine = seed[i][j];
				if (mine > 0) {
					if (mine == 9) {
						numPaint.setColor(Color.RED);
						canvas.drawBitmap(mMineIcon,
								(iPonit.xEndPoint - iPonit.xPonit) / 2
										+ iPonit.xPonit - mMineIcon.getWidth()
										/ 2,
								(iPonit.yEndPoint - iPonit.yPonit) / 2
										+ iPonit.yPonit - mMineIcon.getHeight()
										/ 2, mp);
					} else {
						numPaint.setColor(colors[mine - 1]);
						String txt = mine + "";
						Rect bounds = new Rect();
						numPaint.getTextBounds(txt, 0, txt.length(), bounds);
						/** 获取文本宽,高撒 */
						int txtWidth = bounds.left + bounds.right;
						int txtHeight = bounds.top + bounds.bottom;
						canvas.drawText(
								txt,
								((iPonit.xEndPoint - iPonit.xPonit) - txtWidth)
										/ 2 + iPonit.xPonit,
								((iPonit.yEndPoint - iPonit.yPonit) - txtHeight)
										/ 2 + iPonit.yPonit, numPaint);
					}
				} else {
				}
				if (isInit) {
					// continue;
				}
				Log.e("DEBUG", touchStatus[i][j].status + "=============");
				if (touchStatus[i][j] != TouchStatus.TOUCHED) {
					if (lg == null) {
						lg = new LinearGradient(iPonit.xPonit, iPonit.yPonit,
								iPonit.xEndPoint, iPonit.yEndPoint,
								Color.parseColor("#6184E7"),
								Color.parseColor("#5f9cff"),
								Shader.TileMode.MIRROR); //
						p.setShader(lg);
					}
					canvas.drawRoundRect(rect, 1, 1, p);
				}
				if (call != null) {
					call.lei((MINE_NUM - counter) > 0 ? (MINE_NUM - counter)
							: 0);
				}
				if (numIPoints[i][j].isShowing) {
					canvas.drawBitmap(mFlagRed,
							(iPonit.xEndPoint - iPonit.xPonit) / 2
									+ iPonit.xPonit - mBmpWidth / 2,
							(iPonit.yEndPoint - iPonit.yPonit) / 2
									+ iPonit.yPonit - mBmpHeight / 2, mp);
				}
			}
		}
		Log.e("DEBUG", "--------ondraw-----------=");
	}

	class IPoint {
		private float xPonit;
		private float yPonit;
		private float xEndPoint;
		private float yEndPoint;
		boolean isShowing = false;

		public IPoint(float xPonit, float yPonit, float xEndPoint,
				float yEndPoint) {
			super();
			this.xPonit = xPonit;
			this.yPonit = yPonit;
			this.xEndPoint = xEndPoint;
			this.yEndPoint = yEndPoint;
		}

		public IPoint(float xPonit, float yPonit) {
			super();
			this.xPonit = xPonit;
			this.yPonit = yPonit;
		}
	}

	public boolean isWin = false;

	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int pointerCount = event.getPointerCount();
		if (pointerCount > 1) {
			return true;
		}
		switch (event.getAction()) {

		case MotionEvent.ACTION_UP:
			if (isWin) {
				return true;
			}
			if (isperformLongTask) {
				mHandler.removeMessages(101);
				mHandler.sendEmptyMessageDelayed(101, 488);
			}
			if (!isLock && !isperformLongTask) {
				for (int i = 0; i < rects.length; i++) {
					for (int j = 0; j < rects[i].length; j++) {
						RectF rect = rects[i][j];
						if (rect.contains((int) event.getX(),
								(int) event.getY())) {
							// Toast.makeText(getContext(), "i=" + i + " j=" +
							// j,
							// 0).show();
							// Log.e("JULY", "TOTAL_NUM=" + TOTAL_NUM);
							// Log.e("JULY", "MINE_NUM=" + MINE_NUM);
							// Log.e("JULY", "RESULT_NUM=" + RESULT_NUM);
							if (touchStatus[i][j] == TouchStatus.NORMAL
									&& !numIPoints[i][j].isShowing) {
								if (seed[i][j] == 0) {
									// Toast.makeText(getContext(),
									// "Zero_" + seed[i][j], 0).show();
									openLogical(i, j);

								} else if (seed[i][j] == 9) {
									// Toast.makeText(getContext(), "Game-Over",
									// 0)
									// .show();
									if (numIPoints[i][j].isShowing) {
										numIPoints[i][j].isShowing = false;
									}
									for (int x = 0; x < matrix.length; x++) {
										for (int y = 0; y < matrix[x].length; y++) {
											if (mines[x][y] == 9) {
												if (numIPoints[x][y].isShowing) {
													numIPoints[x][y].isShowing = false;
												}
												touchStatus[x][y] = TouchStatus.TOUCHED;
											}
										}
									}
									if (call != null) {
										call.fail();
									}
									// if (isInit) {
									// mHandler.sendEmptyMessageDelayed(10,
									// 500);
									// return true;
									// }
								} else if (seed[i][j] > 0) {
									RESULT_NUM += 1;
									touchStatus[i][j] = TouchStatus.TOUCHED;
									// Toast.makeText(getContext(),
									// "No_Zero_" + seed[i][j], 0).show();
								}
								postInvalidate();
							}
							if (TOTAL_NUM != 0) {
								if (RESULT_NUM == TOTAL_NUM - MINE_NUM) {
									if (call != null) {
										isWin = true;
										call.win(10);
									}
								}
							}
							break;
							// post(new Runnable() {
							//
							// @Override
							// public void run() {
							// }
							// });
						}
					}
				}

				break;
			}
		}
		return true;
	}

	private static final int NORMAL_STATUS = -1;
	static final int TOUCH_STATUS = 0;
	private DisplayMetrics displayMetrics;

	public enum TouchStatus {
		NORMAL(false, NORMAL_STATUS), TOUCHED(false, NORMAL_STATUS);
		public boolean lock = false;
		public int status = NORMAL_STATUS;

		private TouchStatus(boolean lock, int status) {
			this.lock = lock;
			this.status = status;
		}
	}

	private void mineCreate(int[][] a, int[][] b) {
		for (int i = 0; i < a.length; i++) {
			for (int j = 0; j < a[i].length; j++) {
				if (a[i][j] == 0) {
					coreLogical(a, b, i, j, false);
				} else if (a[i][j] == 9) {
					seed[i][j] = 9;
				} else {
					System.out.println("-1");
				}
			}
		}
	}

	private void coreLogical(int[][] a, int[][] b, int i, int j, boolean boom) {
		if (i == 0 && j == 0) {
			leftTop(a, b); // OK
		} else if (i == 0 && j == a[0].length - 1) {
			rightTop(a, b);// ok
		} else if (j == 0 && i == a.length - 1) {
			leftBottom(a, b);// 0k
		} else if (i == a.length - 1 && j == a[0].length - 1) {
			rightBottom(a, b);// 0k
		} else if (i == 0 && j > 0 && j <= a[0].length - 2) {
			topSide(a, b, j); // OK
		} else if (i == a.length - 1 && j > 0 && j <= a[0].length - 2) {
			bottomSide(a, b, j);// OK
		} else if (j == 0 && i > 0 && i <= a.length - 2) {
			leftVertical(a, b, i);// OK
		} else if (j == a[0].length - 1 && i > 0 && i <= a.length - 2) {
			Log.e("JULY", "RIGHTVERTICAL==");
			rightVertical(a, b, i); // OK
		} else if (i > 0 && i < a.length - 1 && j > 0 && j < a[0].length - 1) {
			calcuNine(a, b, i, j);
		}
	}

	private void rightVertical(int[][] a, int[][] b, int i) {
		for (int x = i - 1; x <= i + 1; x++) {
			for (int y = a[0].length - 1; y >= a[0].length - 2; y--) {
				if (a[x][y] == 9) {
					b[i][a[0].length - 1]++;
				}
			}
		}
	}

	private void leftVertical(int[][] a, int[][] b, int i) {
		for (int x = i - 1; x <= i + 1; x++) {
			for (int y = 0; y < 2; y++) {
				if (a[x][y] == 9) {
					b[i][0]++;
				}
			}
		}
	}

	private void calcuNine(int[][] a, int[][] b, int i, int j) {
		for (int x = i - 1; x <= i + 1; x++) {
			for (int y = j - 1; y <= j + 1; y++) {
				if (a[x][y] == 9) {
					b[i][j]++;
				}
			}
		}
	}

	private void bottomSide(int[][] a, int[][] b, int j) {
		Log.e("JULY", "BOTTOMSIDE==" + j);
		for (int x = a.length - 1; x >= a.length - 2; x--) {
			for (int y = j - 1; y <= j + 1; y++) {
				if (a[x][y] == 9) {
					b[a.length - 1][j]++;
				}
			}
		}
	}

	private void topSide(int[][] a, int[][] b, int j) {
		for (int x = 0; x < 2; x++) {
			for (int y = j - 1; y <= j + 1; y++) {
				if (a[x][y] == 9) {
					b[0][j]++;
				}
			}
		}
	}

	private void rightBottom(int[][] a, int[][] b) {
		for (int x = a.length - 1; x >= a.length - 2; x--) {
			for (int y = a[0].length - 1; y >= a[0].length - 2; y--) {
				if (a[x][y] == 9) {
					b[a.length - 1][a[0].length - 1]++;
				}
			}
		}
	}

	private void leftBottom(int[][] a, int[][] b) {
		for (int x = a.length - 1; x >= a.length - 2; x--) {
			for (int y = 0; y < 2; y++) {
				if (a[x][y] == 9) {
					b[a.length - 1][0]++;
				}
			}
		}
	}

	private void rightTop(int[][] a, int[][] b) {
		for (int x = 0; x < 2; x++) {
			for (int y = a[0].length - 1; y >= a[0].length - 2; y--) {
				if (a[x][y] == 9) {
					b[0][a[0].length - 1]++;
				}
			}
		}
	}

	private void leftTop(int[][] a, int[][] b) {
		for (int x = 0; x < 2; x++) {
			for (int y = 0; y < 2; y++) {
				if (a[x][y] == 9) {
					b[0][0]++;
				}
			}
		}
	}

	/**
	 * 重玩
	 */
	public void replay() {
		RESULT_NUM = 0;
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[i].length; j++) {
				touchStatus[i][j] = TouchStatus.NORMAL;
				counter = 0;
				numIPoints[i][j].isShowing = false;
			}
		}
		loadData();
	}

	/**
	 * 重玩
	 */
	public void choiceLevel(int level) {
		this.level = level;
		RESULT_NUM = 0;
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[i].length; j++) {
				touchStatus[i][j] = TouchStatus.NORMAL;
				counter = 0;
				numIPoints[i][j].isShowing = false;
			}
		}
		loadData();
	}

	public void openLogical(int x, int y) {
		if (x < 0 || y < 0 || x > mines.length - 1 || y > mines[0].length - 1) {
			return;
		}
		if (touchStatus[x][y] == TouchStatus.TOUCHED) {
			return;
		}
		if (numIPoints[x][y] != null && numIPoints[x][y].isShowing) { // 如果是标记
			return;
		}
		if (seed[x][y] == 9) {
			return;
		}
		if (seed[x][y] > 0 && touchStatus[x][y] != TouchStatus.TOUCHED) {
			touchStatus[x][y] = TouchStatus.TOUCHED;
			RESULT_NUM += 1;
			return;
		}
		touchStatus[x][y] = TouchStatus.TOUCHED;
		RESULT_NUM += 1;
		openLogical(x + 1, y + 1);
		openLogical(x + 1, y - 1);
		openLogical(x + 1, y);
		openLogical(x - 1, y + 1);
		openLogical(x - 1, y - 1);
		openLogical(x - 1, y);
		openLogical(x, y + 1);
		openLogical(x, y - 1);
		openLogical(x, y);
	}

	private int mLastMotionX, mLastMotionY;
	private boolean isMoved;

	// 长按的runnable
	private class IRunnable implements Runnable {
		float x, y;

		@Override
		public void run() {
			performLongClick(x, y);
		}
	}

	private IRunnable mLongPressRunnable = new IRunnable();
	private int counter = 0;

	public boolean performLongClick(float x, float y) {
		isperformLongTask = true;
		// Toast.makeText(getContext(), "performLongClick", 0).show();
		for (int i = 0; i < rects.length; i++) {
			for (int j = 0; j < rects[i].length; j++) {
				RectF rect = rects[i][j];
				if (rect.contains((int) x, (int) y)) {
					if (numIPoints[i][j] != null
							&& touchStatus[i][j] != TouchStatus.TOUCHED) {
						numIPoints[i][j].isShowing = !numIPoints[i][j].isShowing;
						if (numIPoints[i][j].isShowing) {
							counter += 1;
						} else {
							counter -= 1;
						}
					}
				}
			}
		}
		mHandler.sendEmptyMessage(0);
		return true;
	}

	// 移动的阈值
	private static final int TOUCH_SLOP = 20;
	private boolean isperformLongTask = false;
	private Paint mp;
	private Paint p;
	private Bitmap mFlagRed;
	private Bitmap mMineIcon;
	private int mBmpWidth;
	private int mBmpHeight;

	public boolean dispatchTouchEvent(MotionEvent event) {
		int x = (int) event.getX();
		int y = (int) event.getY();
		// event.getAction()&MotionEvent.ACTION_MASK
		int pointerCount = event.getPointerCount();
		if (pointerCount > 1) {
			return true;
		}
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			mLastMotionX = x;
			mLastMotionY = y;
			isMoved = false;
			if (mLongPressRunnable != null) {
				mLongPressRunnable.x = event.getX();
				mLongPressRunnable.y = event.getY();
				/*
				 * 将mLongPressRunnable放进任务队列中，到达设定时间后开始执行 这里的长按时间采用系统标准长按时间
				 */
				postDelayed(mLongPressRunnable,
						ViewConfiguration.getLongPressTimeout());
			}
			break;
		case MotionEvent.ACTION_MOVE:

			if (isMoved) {
				break;
			}
			if (Math.abs(mLastMotionX - x) > TOUCH_SLOP
					|| Math.abs(mLastMotionY - y) > TOUCH_SLOP) {
				// 移动超过阈值，则表示移动了
				isMoved = true;
				isperformLongTask = false;
				removeCallbacks(mLongPressRunnable);
			}
			break;
		case MotionEvent.ACTION_UP:
			// isperformLongTask = false;
			// 释放了
			removeCallbacks(mLongPressRunnable);
			break;
		}
		return super.dispatchTouchEvent(event);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		// setMeasuredDimension(measure(widthMeasureSpec, true),
		// measure(widthMeasureSpec, false));
	}

	public int measure(int measureSpec, boolean isWidth) {
		int result;
		int size = MeasureSpec.getSize(measureSpec);
		int mode = MeasureSpec.getMode(measureSpec);
		int padding = isWidth ? getPaddingLeft() + getPaddingRight()
				: getPaddingBottom() + getPaddingTop();
		if (mode == MeasureSpec.EXACTLY) { // 精确模式
			result = size;
		} else {
			result = isWidth ? getSuggestedMinimumWidth()
					: getSuggestedMinimumHeight();
			result += padding;
			if (mode == MeasureSpec.AT_MOST) { // 最大模式
				if (isWidth) {
					result = Math.max(result, size);
				} else {
					result = Math.min(result, size);
				}
			}
		}
		return result;
	}

	/**
	 * 修正地图
	 */
	public void fixMap(int type, int level) {
		if (type == 0) {
			return;
		}
		// 1 2 3 4 5
		// 6 7 8 9 10 11 12 13 14 15
		//
		if (level > 10) {
			level = level - 10;
		} else if (level > 5) {
			level = level - 5;
		}
		matrix = new int[MATRIXLEN + level % 6][MATRIXLEN + (type - 1)];
		Log.e("JULY_", "matrix.="+matrix.length);
		Log.e("JULY_", "matrix[0].="+matrix[0].length);
		mines = new int[matrix.length][matrix[0].length];
		seed = new int[matrix.length][matrix[0].length];
		rects = new RectF[matrix.length][matrix[0].length];
		touchStatus = new TouchStatus[matrix.length][matrix[0].length];
		numIPoints = new IPoint[matrix.length][matrix[0].length];
		RESULT_NUM = 0;
		isInit = false;
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[i].length; j++) {
				touchStatus[i][j] = TouchStatus.NORMAL;
				counter = 0;
				if (numIPoints[i][j] != null) {
					numIPoints[i][j].isShowing = false;
				}
			}
		}
		loadData();
	}
}
