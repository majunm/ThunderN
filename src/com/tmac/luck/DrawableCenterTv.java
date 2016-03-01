package com.tmac.luck;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * 
 * 文件名称:	DrawableCenterTextView.java <br/>
 * 创建日期：2015年10月10日下午6:11:28<br/>
 * 创建作者：majun<br/>
 * 联系作者:<a href="mailto:747673016@qq.com">@author majun</a><br/>
 * @Description:随文本居中textview,当设置drawableLeft时,<br/>
 * gravity则为"center_vertical"<br/>
 * ======================================================<br/>
 * 版权声明：Copyright 2011 majun 保留所有权利。<br/>
 * ======================================================<br/>
 */
public class DrawableCenterTv extends TextView {

	public DrawableCenterTv(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
	}

	public DrawableCenterTv(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public DrawableCenterTv(Context context) {
		super(context);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		Drawable[] drawables = getCompoundDrawables();
		if (drawables != null) {
			Drawable drawableLeft = drawables[0];
			if (drawableLeft != null) {
				float textWidth = getPaint().measureText(getText().toString());
				int drawablePadding = getCompoundDrawablePadding();
				int drawableWidth = 0;
				drawableWidth = drawableLeft.getIntrinsicWidth();
				float bodyWidth = textWidth + drawableWidth + drawablePadding;
				canvas.translate((getWidth() - bodyWidth) / 2, 0);
			}
		}
		super.onDraw(canvas);
	}
}
