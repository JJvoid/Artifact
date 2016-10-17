package com.thinkrace.NewApplication.Uti;

import com.thingrace.newapplication.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Xfermode;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

public abstract class MaskedImage extends ImageView {
	private Context mContext;

	private int mBorderThickness = 0;
	// ���ֻ������һ����ֵ����ֻ��һ��Բ�α߿�
	private int mBorderOutsideColor = 0;
	private int mBorderInsideColor = 0;

	private int defaultColor = 0xFFFFFFFF;

	// �ؼ�Ĭ�ϳ�����
	private int defaultWidth = 0;
	private int defaultHeight = 0;

	private static final Xfermode MASK_XFERMODE;
	private Bitmap mask;
	private Paint paint;

	static {
		PorterDuff.Mode localMode = PorterDuff.Mode.DST_IN;
		MASK_XFERMODE = new PorterDuffXfermode(localMode);
	}

	public MaskedImage(Context paramContext) {
		super(paramContext);
		mContext = paramContext;
	}

	public MaskedImage(Context paramContext, AttributeSet paramAttributeSet) {
		super(paramContext, paramAttributeSet);
		mContext = paramContext;
		setCustomAttributes(paramAttributeSet);
	}

	public MaskedImage(Context paramContext, AttributeSet paramAttributeSet,
			int paramInt) {
		super(paramContext, paramAttributeSet, paramInt);
		mContext = paramContext;
		setCustomAttributes(paramAttributeSet);
	}

	private void setCustomAttributes(AttributeSet attrs) {
		TypedArray a = mContext.obtainStyledAttributes(attrs,
				R.styleable.roundedimageview);
		mBorderThickness = a.getDimensionPixelSize(
				R.styleable.roundedimageview_border_thickness, 0);
		mBorderOutsideColor = a
				.getColor(R.styleable.roundedimageview_border_outside_color,
						defaultColor);
		mBorderInsideColor = a.getColor(
				R.styleable.roundedimageview_border_inside_color, defaultColor);
	}

	public abstract Bitmap createMask();

	protected void onDraw(Canvas paramCanvas) {
		Drawable localDrawable = getDrawable();
		if (localDrawable == null)
			return;
		try {
			if (this.paint == null) {
				Paint localPaint1 = new Paint();
				this.paint = localPaint1;
				this.paint.setFilterBitmap(false);
				Paint localPaint2 = this.paint;
				Xfermode localXfermode1 = MASK_XFERMODE;
				@SuppressWarnings("unused")
				Xfermode localXfermode2 = localPaint2
						.setXfermode(localXfermode1);
			}
			float f1 = getWidth();
			float f2 = getHeight();
			int i = paramCanvas.saveLayer(0.0F, 0.0F, f1, f2, null, 31);
			int j = getWidth();
			int k = getHeight();
			localDrawable.setBounds(0, 0, j, k);
			localDrawable.draw(paramCanvas);
			if ((this.mask == null) || (this.mask.isRecycled())) {
				Bitmap localBitmap1 = createMask();
				this.mask = localBitmap1;
			}
			Bitmap localBitmap2 = this.mask;
			Paint localPaint3 = this.paint;

			if (defaultWidth == 0) {
				defaultWidth = getWidth();
			}
			if (defaultHeight == 0) {
				defaultHeight = getHeight();
			}
			int radius = 0;
			if (mBorderInsideColor != defaultColor
					&& mBorderOutsideColor != defaultColor) {// ���廭�����߿򣬷ֱ�Ϊ��Բ�߿����Բ�߿�
				radius = (defaultWidth < defaultHeight ? defaultWidth
						: defaultHeight) / 2 - 2 * mBorderThickness;
				// ����Բ
				drawCircleBorder(paramCanvas, radius + mBorderThickness / 2,
						mBorderInsideColor);
				// ����Բ
				drawCircleBorder(paramCanvas, radius + mBorderThickness
						+ mBorderThickness / 2, mBorderOutsideColor);
			} else if (mBorderInsideColor != defaultColor
					&& mBorderOutsideColor == defaultColor) {// ���廭һ���߿�
				radius = (defaultWidth < defaultHeight ? defaultWidth
						: defaultHeight) / 2 - mBorderThickness;
				drawCircleBorder(paramCanvas, radius + mBorderThickness / 2,
						mBorderInsideColor);
			} else if (mBorderInsideColor == defaultColor
					&& mBorderOutsideColor != defaultColor) {// ���廭һ���߿�
				radius = (defaultWidth < defaultHeight ? defaultWidth
						: defaultHeight) / 2 - mBorderThickness;
				drawCircleBorder(paramCanvas, radius + mBorderThickness / 2,
						mBorderOutsideColor);
			} else {// û�б߿�
				radius = (defaultWidth < defaultHeight ? defaultWidth
						: defaultHeight) / 2;
			}

			paramCanvas.drawBitmap(localBitmap2, 0.0F, 0.0F, localPaint3);
			paramCanvas.restoreToCount(i);
			return;
		} catch (Exception localException) {
			StringBuilder localStringBuilder = new StringBuilder()
					.append("Attempting to draw with recycled bitmap. View ID = ");
			System.out.println("localStringBuilder==" + localStringBuilder);
		}
	}

	/**
	 * ��Ե��Բ
	 */
	private void drawCircleBorder(Canvas canvas, int radius, int color) {
		Paint paint = new Paint();
		/* ȥ��� */
		paint.setAntiAlias(true);
		paint.setFilterBitmap(true);
		paint.setDither(true);
		paint.setColor(color);
		/* ����paint�ġ�style��ΪSTROKE������ */
		paint.setStyle(Paint.Style.STROKE);
		/* ����paint������� */
		paint.setStrokeWidth(mBorderThickness);
		canvas.drawCircle(defaultWidth / 2, defaultHeight / 2, radius, paint);
	}
}
