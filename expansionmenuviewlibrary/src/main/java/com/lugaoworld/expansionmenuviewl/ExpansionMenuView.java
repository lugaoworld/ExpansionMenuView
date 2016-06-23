package com.lugaoworld.expansionmenuviewl;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by lugaoworld on 2016/6/22.
 */


/**
 * Created by meicai on 2016/6/18.
 */
public class ExpansionMenuView extends View implements View.OnTouchListener/*,ValueAnimator.AnimatorUpdateListener*/ {

    private Paint mPaint;
    private Paint mTextPaint;
    private Paint mLinePaint;
    private int mRadius = 70;
    private float mTextSize = 12f;
    boolean mIsExpan = false;
    private final int CHECKED_COLOR = Color.WHITE;
    private final int UNCHECKED_COLOR = Color.GRAY;
    private int ANIMATION_DURATION = 200;
    int mWidth;
    int mHeight;
    RectF mOval;

    float mLineWidthSize = 1;
    int mExpanCloseWordIndex;
    private String[] mMenuArray;
    int[][] mMenuTextSize;
    int mMenuSize;
    Map<Integer, String> map = new HashMap<Integer, String>();

    int mInitLeft;
    int mEndLeft;
    OnMenuItemSelectListener listener;

    public ExpansionMenuView(Context context) {
        this(context, null);
    }

    public ExpansionMenuView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ExpansionMenuView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(context);
    }

    private void init(Context context) {

        mPaint = new Paint();
        mTextPaint = new Paint();
        mLinePaint = new Paint();

        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.RED);
        mPaint.setStyle(Paint.Style.FILL);

        mTextPaint.setAntiAlias(true);
        mTextPaint.setColor(CHECKED_COLOR);
        mTextPaint.setStyle(Paint.Style.FILL);
        mTextPaint.setTextSize(sp2px(context, mTextSize));

        mLinePaint.setAntiAlias(true);
        mLinePaint.setColor(Color.WHITE);
        mLinePaint.setStrokeWidth(dip2px(context, mLineWidthSize));
        mLinePaint.setStyle(Paint.Style.FILL);

        setClickable(true);
        setOnTouchListener(this);

    }

    public void setUpValue(int length) {

        mOval = new RectF();
        mOval.left = 0;
        mOval.top = 0;
        mOval.right = 2 * mRadius;
        mOval.bottom = 2 * mRadius;

        mWidth = mHeight = 2 * mRadius;

        clearMapValue();

        for (int i = 0; i < length; i++) {
            map.put(i + 1, (i * 2 * mRadius) + "#" + (i + 1) * 2 * mRadius);
        }
    }

    void clearMapValue() {

        if (map != null) {
            map.clear();
        }
    }

    public void setMenuDataSource(String[] array) {

        this.mMenuArray = array;
        this.mMenuSize = array.length;
        mExpanCloseWordIndex = mMenuSize - 1;

        setUpValue(mMenuSize);

        mMenuTextSize = calueMenuArrayTextSize(array);

        mIsExpan = false;

        mEndLeft = 0;

        invalidate();
        requestLayout();
    }

    public int[][] calueMenuArrayTextSize(String[] menus) {

        int[][] array = new int[menus.length][2];

        Rect bounds = null;
        for (int i = 0; i < menus.length; i++) {
            bounds = new Rect();
            mTextPaint.getTextBounds(menus[i], 0, menus[i].length(), bounds);
            array[i][0] = bounds.width();
            array[i][1] = bounds.height();
        }
        return array;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        setMeasuredDimension(mWidth, mHeight);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        mInitLeft = getLeft();
        Log.i("AA", "onLayout  getLeft():" + getLeft());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Log.i("AA", "onDraw mWidth:" + mWidth);
        canvas.drawRoundRect(mOval, mRadius, mRadius, mPaint);////第二个参数是x半径，第三个参数是y半径
        if (getExpanState()) {

            for (int i = 0; i < mMenuSize - 1; i++) {
                if (i == mExpanCloseWordIndex) {
                    mTextPaint.setColor(CHECKED_COLOR);
                } else {
                    mTextPaint.setColor(UNCHECKED_COLOR);
                }
                canvas.drawText(mMenuArray[i], i * 2 * mRadius + mRadius - mMenuTextSize[i][0] / 2, mRadius + mMenuTextSize[i][1] / 2, mTextPaint);
                canvas.drawLine((i + 1) * 2 * mRadius - mLineWidthSize / 2, mRadius - mMenuTextSize[i][1] / 2, (i + 1) * 2 * mRadius - mLineWidthSize / 2, mRadius + mMenuTextSize[i][1] / 2, mLinePaint);
            }
            if (mMenuSize - 1 == mExpanCloseWordIndex) {
                mTextPaint.setColor(CHECKED_COLOR);
            } else {
                mTextPaint.setColor(UNCHECKED_COLOR);
            }
            canvas.drawText(mMenuArray[mMenuSize - 1], (mMenuSize - 1) * 2 * mRadius + mRadius - mMenuTextSize[mMenuSize - 1][0] / 2, mRadius + mMenuTextSize[mMenuSize - 1][1] / 2, mTextPaint);

        } else {
            mTextPaint.setColor(CHECKED_COLOR);
            canvas.drawText(mMenuArray[mExpanCloseWordIndex], mRadius - mMenuTextSize[mExpanCloseWordIndex][0] / 2, mRadius + mMenuTextSize[mExpanCloseWordIndex][1] / 2, mTextPaint);
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:

                if (getExpanState()) {
                    Log.i("AA", "getExpanState 1");
                    mIsExpan = false;
                    int pos = caluePosByEvent(event.getX());
                    mExpanCloseWordIndex = pos - 1;

                    if (pos != -1) {
                        listener.selected(mMenuArray[pos - 1], pos);
                        startExpanAnimation(1);
                    }
                } else {
                    if (mMenuSize == 1) {
                        listener.selected(mMenuArray[0], 1);
                        break;
                    }
                    Log.i("AA", "getExpanState 2");
                    mIsExpan = true;
                    startExpanAnimation(0);
                }
                break;
        }
        return true;
    }

    int caluePosByEvent(float event) {

        for (Iterator it = map.keySet().iterator(); it.hasNext(); ) {
            int key = (Integer) it.next();
            String value = map.get(key);
            if (event > Float.valueOf(value.split("#")[0]) && event < Float.valueOf(value.split("#")[1])) {
                Log.i("AA", "caluePosByEvent ----------event pos：" + event + "," + key);

                return (int) key;
            }
        }
        return -1;

    }

    public void setOnMenuItemSelectListener(OnMenuItemSelectListener listener) {

        this.listener = listener;
    }

    public interface OnMenuItemSelectListener {

        void selected(String menu, int position);
    }

    public void AnimationUpdate(float value) {

        if (getExpanState()) {

            Log.i("AA", "AnimationUpdate 1 mInitLeft value:" + mInitLeft + "," + value);
            mOval.right = mMenuSize * 2 * mRadius;
            mOval.left = 0;
            mEndLeft = (int) (mInitLeft - value);
            setLeft(mEndLeft);
        } else {
            Log.i("AA", "AnimationUpdate 2  mOval.right value:" + mOval.right + "" + value);
            mOval.right = 2 * mRadius;
            mOval.left = 0;
            setLeft((int) (mEndLeft + value));
        }
        invalidate();
    }

    private void startExpanAnimation(int directType) {

        ValueAnimator mAnim = null;
        if (directType == 0) {
            mAnim = ValueAnimator.ofFloat(0, (mMenuSize - 1) * 2 * mRadius);
        } else {
            mAnim = ValueAnimator.ofFloat(0, (mMenuSize - 1) * 2 * mRadius);
        }

        ValueAnimator.AnimatorUpdateListener listener = new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                AnimationUpdate(value);
            }
        };
        mAnim.addUpdateListener(listener);
        mAnim.setDuration(ANIMATION_DURATION);
        mAnim.start();
    }

    boolean getExpanState() {
        return mIsExpan;
    }

    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }
}
