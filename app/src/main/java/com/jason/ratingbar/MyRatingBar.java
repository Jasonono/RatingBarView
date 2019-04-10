package com.jason.ratingbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class MyRatingBar extends View {

    private Bitmap starNormal;
    private Bitmap starFocus;
    private Paint paint;
    private int starNormalId;
    private int starFocusId;
    private int starNumber;
    private int starFocusNumber = 0;
    private int paddingLeft;
    private int paddingRight;
    private int paddingTop;
    private int paddingBottom;
    private int starSpace;
    private int starWidth;

    public MyRatingBar(Context context) {
        this(context, null);
    }

    public MyRatingBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyRatingBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.MyRatingBar);
        starNumber = array.getInt(R.styleable.MyRatingBar_starNumber, 5);
        starNormalId = array.getResourceId(R.styleable.MyRatingBar_starNormal, starNormalId);
        starFocusId = array.getResourceId(R.styleable.MyRatingBar_starFocus, starFocusId);
        starSpace = (int) array.getDimension(R.styleable.MyRatingBar_starSpace, dip2px(context, 10));
        starNormal = BitmapFactory.decodeResource(getResources(), starNormalId);
        starFocus = BitmapFactory.decodeResource(getResources(), starFocusId);

        array.recycle();

        paint = new Paint();
        paint.setAntiAlias(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        paddingLeft = getPaddingLeft();
        paddingRight = getPaddingRight();
        paddingTop = getPaddingTop();
        paddingBottom = getPaddingBottom();

        starWidth = starNormal.getWidth();

        int height = starNormal.getHeight() + paddingTop + paddingBottom;
        int width = starWidth * starNumber + paddingLeft + paddingRight + (starNumber - 1) * starSpace;

        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < starNumber; i++) {
            if (i >= starFocusNumber) {
                canvas.drawBitmap(starNormal, paddingLeft + i * starWidth + i * starSpace,
                        paddingTop, paint);
            } else {
                canvas.drawBitmap(starFocus, paddingLeft + i * starWidth + i * starSpace, paddingTop,
                        paint);
            }
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                float moveX = event.getX();
                int count = (int) (moveX / (starWidth + starSpace)) + 1;
                if (count <= 0) {
                    count = 0;
                }
                if (count > starNumber) {
                    count = starNumber;
                }

                if (count != starFocusNumber) {
                    starFocusNumber = count;
                    invalidate();
                }

                break;
        }
        return true;
    }

    /**
     * 将dp转换成px
     *
     * @param context
     * @param dpValue
     * @return
     */
    public static int dip2px(Context context, int dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


}
