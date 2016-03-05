package com.vzl.activate.view;

import com.vzl.activate.R;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageButton;

public class ProgressView extends ImageButton {

    public static final String TAG = ProgressView.class.getSimpleName();
    private int mMinSize = 200;
    private RectF mCircleArea;
    private RectF mEffectiveArea;
    private float CIRClE_STROKE_WIDTH = 60;
    private Paint mCirclePaint;
    private Paint mCircleOverlayPaint;
    private int mProgress;
    private float mLoadingCircleRadius;
    private Paint mLoadingPaint;
    private ObjectAnimator mLoadingAnimator;

    public ProgressView(Context context) {
        this(context, null, 0);
    }

    public ProgressView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCirclePaint.setColor(Color.GRAY);
        mCirclePaint.setStrokeWidth(CIRClE_STROKE_WIDTH);
        mCirclePaint.setAlpha(100);
        mCirclePaint.setStyle(Paint.Style.STROKE);

        mCircleOverlayPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCircleOverlayPaint.setColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
        mCircleOverlayPaint.setStrokeWidth(CIRClE_STROKE_WIDTH);
        mCircleOverlayPaint.setStyle(Paint.Style.STROKE);
        mCircleOverlayPaint.setStrokeCap(Paint.Cap.ROUND);

        mLoadingPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLoadingPaint.setColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
        mLoadingPaint.setStrokeWidth(CIRClE_STROKE_WIDTH);
        mLoadingPaint.setAlpha(150);
        mLoadingPaint.setStyle(Paint.Style.FILL);

        mEffectiveArea = new RectF();
        mCircleArea = new RectF();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width;
        int height;

        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else if (widthMode == MeasureSpec.AT_MOST) {
            width = Math.min(mMinSize, widthSize);
        } else {
            width = mMinSize;
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else if (heightMode == MeasureSpec.AT_MOST) {
            height = Math.min(mMinSize, heightSize);
        } else {
            height = mMinSize;
        }
        setMeasuredDimension(width, height);

        mEffectiveArea.set(getPaddingLeft(), getPaddingTop(), width - getPaddingRight(),
                height - getPaddingBottom());
        mCircleArea.set(mEffectiveArea);
        mCircleArea.inset(CIRClE_STROKE_WIDTH, CIRClE_STROKE_WIDTH);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mLoadingAnimator == null) {
            canvas.drawArc(mCircleArea, 0, 360, false, mCirclePaint);
            canvas.drawArc(mCircleArea, 270, 360f * 1f / (100f / mProgress), false, mCircleOverlayPaint);
        } else {
            mLoadingPaint.setAlpha((int) (255 - mLoadingCircleRadius));
            canvas.drawCircle(mCircleArea.centerX(), mCircleArea.centerY(), mLoadingCircleRadius, mLoadingPaint);
        }
    }

    public void setLoadingCircleRadius(float radius) {
        mLoadingCircleRadius = radius;
        invalidate();
    }

    public float getLoadingCircleRadius() {
        return mLoadingCircleRadius;
    }

    public void setProgress(int progress) {
        mProgress = progress;
        invalidate();
    }

    public int getProgress() {
        return mProgress;
    }

    public void startLoadingAnimation() {
        mLoadingAnimator = ObjectAnimator.ofFloat(this, "loadingCircleRadius", 20, 120);
        mLoadingAnimator.setDuration(1500);
        mLoadingAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mLoadingAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        mLoadingAnimator.setRepeatMode(ValueAnimator.REVERSE);
        mLoadingAnimator.start();

    }

    public void cancelLoadingAnimation() {
        if (mLoadingAnimator != null) {
            mLoadingAnimator.cancel();
            mLoadingAnimator = null;
        }
    }
}
