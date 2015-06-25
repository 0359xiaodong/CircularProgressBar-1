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

public class CircularProgressIndeterminateBar extends View {
    private static final float DEFAULT_SPEED = 5f;

    private static final float DEFAULT_THICKNESS_DP = 4;
    private static final float DEFAULT_RADIUS_DP = 28;
    private static final boolean DEFAULT_FILLED_STATE = false;

    private static final int DEFAULT_PRIMARY_COLOR = Color.LTGRAY;
    private static final int DEFAULT_ACCENT_COLOR = Color.argb(255, 33, 150, 243);

    private static final boolean DEFAULT_RUNNING_STATE = false;

    private boolean spinning;
    private boolean stopping;
    private float currentValue;
    private float speed;

    private float thickness;
    private float radius;
    private boolean filled;

    private Paint primaryPaint;
    private Paint accentPaint;
    private int primaryColor;
    private int accentColor;

    private RectF bounds = new RectF();

    private CircularProgressBarListener listener;

    private boolean growing = true;
    private float length = 0;

    public CircularProgressIndeterminateBar(Context context) {
        this(context, null);
    }

    public CircularProgressIndeterminateBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircularProgressIndeterminateBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        DisplayMetrics metrics = getContext().getResources().getDisplayMetrics();
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.CircularProgressBar);

        speed = typedArray.getFloat(R.styleable.CircularProgressBar_cpb_speed, DEFAULT_SPEED);

        primaryColor = typedArray.getColor(R.styleable.CircularProgressBar_cpb_primaryColor, DEFAULT_PRIMARY_COLOR);
        accentColor = typedArray.getColor(R.styleable.CircularProgressBar_cpb_accentColor, DEFAULT_ACCENT_COLOR);

        thickness = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_THICKNESS_DP, metrics);
        thickness = typedArray.getDimension(R.styleable.CircularProgressBar_cpb_thickness, thickness);
        radius = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_RADIUS_DP, metrics);
        radius = (int) typedArray.getDimension(R.styleable.CircularProgressBar_cpb_radius, radius);
        filled = typedArray.getBoolean(R.styleable.CircularProgressBar_cpb_filled, DEFAULT_FILLED_STATE);

        primaryPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        accentPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        updatePaint();

        currentValue = 0;
        spinning = typedArray.getBoolean(R.styleable.CircularProgressBar_cpb_running, DEFAULT_RUNNING_STATE);
        stopping = false;

        typedArray.recycle();

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
        primaryPaint.setStyle(filled ? Paint.Style.FILL : Paint.Style.STROKE);
        primaryPaint.setStrokeWidth(thickness);
        primaryPaint.setStrokeCap(Paint.Cap.ROUND);

        accentPaint.setColor(accentColor);
        accentPaint.setDither(true);
        accentPaint.setStyle(filled ? Paint.Style.FILL : Paint.Style.STROKE);
        accentPaint.setStrokeWidth(thickness);
        accentPaint.setStrokeCap(Paint.Cap.ROUND);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawArc(bounds, 360, 360, filled, primaryPaint);

        if (spinning) {
            currentValue += speed;

            float from = currentValue - 90;
            if (growing) {
                length += speed;
                growing = length < 230f;
            } else {
                length = length - speed;
                currentValue += speed;

                if (stopping) {
                    if (length <= speed) {
                        stopping = false;
                        spinning = false;
                        notifyOnStop();
                    }
                } else {
                    growing = length < 15f;
                }
            }

            if (currentValue > 360) {
                currentValue -= 360f;
            }

            if (isInEditMode()) {
                from = -90;
                length = 90;
            }
            canvas.drawArc(bounds, from, length, filled, accentPaint);

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

    private void notifyOnStop() {
        if (listener != null) {
            listener.onStopSpinning();
        }
    }

    private void notifyOnStart() {
        if (listener != null) {
            listener.onStartSpinning();
        }
    }

    public void start() {
        growing = true;
        currentValue = 0f;
        length = 0;

        stopping = false;
        spinning = true;
        invalidate();

        notifyOnStart();
    }

    public void stop() {
        growing = false;

        stopping = true;
        invalidate();
    }

    public boolean isSpinning() {
        return spinning;
    }

    public void setSpinning(boolean spinning) {
        if (spinning) {
            start();
        } else {
            stop();
        }
    }

    public boolean isStopping() {
        return stopping;
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

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public boolean isFilled() {
        return filled;
    }

    public void setFilled(boolean filled) {
        this.filled = filled;
        updatePaint();
        invalidate();
    }
}
