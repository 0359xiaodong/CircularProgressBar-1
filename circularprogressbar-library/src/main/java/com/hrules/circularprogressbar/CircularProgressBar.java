package com.hrules.circularprogressbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;

public class CircularProgressBar extends View {
    private static final float DEFAULT_MAX_VALUE = 360f;
    private static final float DEFAULT_VALUE = 0;
    private static final float DEFAULT_SPEED = 5f;

    private static final float DEFAULT_THICKNESS_DP = 4;
    private static final float DEFAULT_RADIUS_DP = 28;

    private static final int DEFAULT_PRIMARY_COLOR = Color.LTGRAY;
    private static final int DEFAULT_ACCENT_COLOR = Color.argb(255, 33, 150, 243);

    private static final boolean DEFAULT_ANIM_STATE = true;

    private float maxValue;
    private float currentValue;
    private float targetValue;
    private float speed;

    private float thickness;
    private float radius;

    private Paint primaryPaint;
    private Paint accentPaint;
    private int primaryColor;
    private int accentColor;

    private RectF bounds = new RectF();

    private CircularProgressBarListener listener;

    public CircularProgressBar(Context context) {
        this(context, null);
    }

    public CircularProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircularProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        DisplayMetrics metrics = getContext().getResources().getDisplayMetrics();
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.CircularProgressBar);

        maxValue = typedArray.getFloat(R.styleable.CircularProgressBar_cpb_maxValue, DEFAULT_MAX_VALUE);
        currentValue = typedArray.getFloat(R.styleable.CircularProgressBar_cpb_value, DEFAULT_VALUE);
        speed = typedArray.getFloat(R.styleable.CircularProgressBar_cpb_speed, DEFAULT_SPEED);

        primaryColor = typedArray.getColor(R.styleable.CircularProgressBar_cpb_primaryColor, DEFAULT_PRIMARY_COLOR);
        accentColor = typedArray.getColor(R.styleable.CircularProgressBar_cpb_accentColor, DEFAULT_ACCENT_COLOR);

        thickness = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_THICKNESS_DP, metrics);
        thickness = typedArray.getDimension(R.styleable.CircularProgressBar_cpb_thickness, thickness);
        radius = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_RADIUS_DP, metrics);
        radius = (int) typedArray.getDimension(R.styleable.CircularProgressBar_cpb_radius, radius);

        typedArray.recycle();

        primaryPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        accentPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        updatePaint();

        setValue(currentValue, false);

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                notifyOnClick();
            }
        });
    }

    private void updatePaint() {
        primaryPaint.setColor(primaryColor);
        primaryPaint.setDither(true);
        primaryPaint.setStyle(Paint.Style.STROKE);
        primaryPaint.setStrokeWidth(thickness);
        primaryPaint.setStrokeCap(Paint.Cap.ROUND);

        accentPaint.setColor(accentColor);
        accentPaint.setDither(true);
        accentPaint.setStyle(Paint.Style.STROKE);
        accentPaint.setStrokeWidth(thickness);
        accentPaint.setStrokeCap(Paint.Cap.ROUND);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawArc(bounds, 360, 360, false, primaryPaint);

        if (targetValue > currentValue) {
            currentValue = currentValue + (1 * speed);
        } else if (targetValue < currentValue) {
            currentValue = currentValue - (1 * speed);
        }

        canvas.drawArc(bounds, -90, 360 * currentValue / maxValue, false, accentPaint);

        notifyOnValueChanged(currentValue);
        notifyOnPercentValueChanged((float) Math.round((currentValue / maxValue * 100)) / 100);

        if (currentValue == targetValue || currentValue < 0) {
            notifyOnFinish();
        }

        if (currentValue != targetValue || currentValue < 0) {
            invalidate();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int viewWidth = (int) (radius + this.getPaddingLeft() + this.getPaddingRight());
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        int width = viewWidth;

        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else if (widthMode == MeasureSpec.AT_MOST) {
            width = Math.min(viewWidth, widthSize);
        }

        // width = height
        setMeasuredDimension(width, width);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        // width = height
        setBounds(w, w);
        invalidate();
    }

    private void setBounds(int width, int height) {
        bounds = new RectF(getPaddingLeft() + thickness, getPaddingTop() + thickness, width - getPaddingRight() - thickness, height - getPaddingBottom() - thickness);
    }

    public void setListener(CircularProgressBarListener listener) {
        this.listener = listener;
    }

    private void notifyOnClick() {
        if (listener != null) {
            listener.onClick();
        }
    }

    private void notifyOnFinish() {
        if (listener != null) {
            listener.onFinish();
        }
    }

    private void notifyOnValueChanged(float currentValue) {
        if (listener != null) {
            listener.onValueChanged(currentValue);
        }
    }

    private void notifyOnPercentValueChanged(float percent) {
        if (listener != null) {
            listener.onPercentValueChanged(percent);
        }
    }

    public float getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(float maxValue) {
        this.maxValue = maxValue;
        if (currentValue > maxValue) {
            currentValue = maxValue;
        }

        invalidate();
    }

    public float getValue() {
        return currentValue;
    }

    public void setValue(float value) {
        setValue(value, DEFAULT_ANIM_STATE);
    }

    public void setValue(float value, boolean anim) {
        targetValue = value;
        if (targetValue > maxValue) {
            this.targetValue = maxValue;
        }
        if (!anim) {
            currentValue = targetValue;
        }

        invalidate();
    }

    public void setPercentValue(float percent) {
        setPercentValue(percent, DEFAULT_ANIM_STATE);
    }

    public void setPercentValue(float percent, boolean anim) {
        setValue(maxValue * percent, anim);
    }

    public boolean isRunning() {
        return currentValue != targetValue;
    }

    public float getThickness() {
        return thickness;
    }

    public void setThickness(float thickness) {
        this.thickness = thickness;
        updatePaint();
        invalidate();
    }

    public int getPrimaryColor() {
        return primaryColor;
    }

    public void setPrimaryColor(int primaryColor) {
        this.primaryColor = primaryColor;
        updatePaint();
        invalidate();
    }

    public int getAccentColor() {
        return accentColor;
    }

    public void setAccentColor(int accentColor) {
        this.accentColor = accentColor;
        updatePaint();
        invalidate();
    }

    public void reset() {
        reset(DEFAULT_ANIM_STATE);
    }

    public void reset(boolean anim) {
        targetValue = 0;
        if (!anim) {
            currentValue = 0;
        }

        invalidate();
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }
}
