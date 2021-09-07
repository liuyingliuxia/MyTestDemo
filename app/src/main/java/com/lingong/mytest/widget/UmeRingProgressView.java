package com.lingong.mytest.widget;

/**
 * @Author: Miracle.Lin
 * @Date:2021/8/30
 * @Desc:
 */

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.lingong.mytest.R;
import com.lingong.mytest.utils.LogUtil;
/**
 * @description 圆环进度条：
 */
public class UmeRingProgressView extends View {

    private Paint innerPaint;
    private Paint outerPaint;
    private Paint smallCirclePaint;
    private Paint leftProgressSmallCirclePaint;//左边进度小圆
    private Paint rightProgressSmallCirclePaint;//右边进度小圆
    private Paint progressPaint;

    private int ringWidth = dp2px(20);//圆环宽度
    private int smallCircleRadius = ringWidth / 2;//进度小圆半径
    private float progress = 0f;//进度
    private int angle = 20;// 15°//往内弯的角度
    private int endColor = ContextCompat.getColor(getContext(), R.color.progress_end);//渐变结束的颜色值
    private int startColor = ContextCompat.getColor(getContext(), R.color.progress_start);//渐变开始颜色值
    private int ringBackGroundColor = ContextCompat.getColor(getContext(), R.color.transparent);

    public UmeRingProgressView(Context context) {
        super(context);
        init();
    }

    @SuppressLint("ResourceType")
    private void getAttrs(Context context, AttributeSet attributeSet) {
        try {
            TypedArray array = context.obtainStyledAttributes(attributeSet, R.styleable.UmeRingProgressView);
            ringBackGroundColor = array.getColor(R.styleable.UmeRingProgressView_background_ring_color, ContextCompat.getColor(getContext(), R.color.transparent));
            startColor = array.getColor(R.styleable.UmeRingProgressView_start_progress_color, ContextCompat.getColor(getContext(), R.color.progress_start));
            endColor = array.getColor(R.styleable.UmeRingProgressView_end_progress_color, ContextCompat.getColor(getContext(), R.color.progress_end));
            ringWidth = (int) array.getDimension(R.styleable.UmeRingProgressView_ring_width, dp2px(10));
            angle = array.getInt(R.styleable.UmeRingProgressView_circle_angle, 20);
            smallCircleRadius = ringWidth / 2;
            array.recycle();
        } catch (Exception e) {
          LogUtil.e("TAG", e.getMessage());
        }
    }

    public UmeRingProgressView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        getAttrs(context, attrs);
        init();
    }

    public UmeRingProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        getAttrs(context, attrs);
        init();
    }

    private void init() {
        //禁止硬件加速，图层混合模式有些不支持
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        // 内圆画笔：
        innerPaint = new Paint();
        innerPaint.setColor(ContextCompat.getColor(getContext(), R.color.white));
        innerPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        innerPaint.setAntiAlias(true);
        // 外圆画笔：
        outerPaint = new Paint();
        outerPaint.setColor(ringBackGroundColor);
        outerPaint.setAntiAlias(true);
        // 小圆：
        smallCirclePaint = new Paint();
        smallCirclePaint.setColor(ringBackGroundColor);
        smallCirclePaint.setAntiAlias(true);
        // 画进度圆环：
        progressPaint = new Paint();
        progressPaint.setColor(ContextCompat.getColor(getContext(), R.color.progress_end));
        progressPaint.setAntiAlias(true);
        // 画进度圆环左边小圆：
        leftProgressSmallCirclePaint = new Paint();
        leftProgressSmallCirclePaint.setColor(startColor);
        leftProgressSmallCirclePaint.setAntiAlias(true);
        // 画进度圆环右边小圆
        rightProgressSmallCirclePaint = new Paint();
        rightProgressSmallCirclePaint.setColor(endColor);
        rightProgressSmallCirclePaint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawOutreCircle(canvas);
        drawInnerCircle(canvas);
    }

    /**
     * 画进度圆：
     */
    private void drawProgressCircle(Canvas canvas, int totalDegrees, double acos) {
        if (progress == 0) {
            return;
        }
        float startDegrees = (float) (180 - acos);
        float endDegrees = totalDegrees * progress;

        RectF rectF = new RectF(0, 0, getWidth(), getWidth());
        if (progress > 0.1) {
            leftProgressSmallCirclePaint.setColor(startColor);
            rightProgressSmallCirclePaint.setColor(endColor);
            // 进度很小的时候就不要渐变色了。。。。
            // https://blog.csdn.net/u010126792/article/details/85238050
            float angleRate = startDegrees / 360f;
            float roteAngle = endDegrees + startDegrees;
            float roteAngleRate = roteAngle / 360f;
            // 渐变色：
            SweepGradient linearGradient = new SweepGradient(getWidth() >> 1, getWidth() >> 1
                    , new int[]{endColor, endColor, startColor, endColor}
                    , new float[]{0f, angleRate, angleRate, roteAngleRate});
            progressPaint.setShader(linearGradient);
        } else {
            progressPaint.setShader(null);
            // 进度很小的时候没必要有渐变色：
            leftProgressSmallCirclePaint.setColor(startColor);
            rightProgressSmallCirclePaint.setColor(startColor);
        }
        canvas.drawArc(rectF, startDegrees, endDegrees, true, progressPaint);

        // 画左右两边的小圆：
        drawLeftLittleCircle(canvas, acos, true);
        drawRightLittleCircle(canvas, startDegrees + (float) Math.abs(totalDegrees) * progress, true);
    }

    /**
     * 画左边的小圆：
     */
    private void drawLeftLittleCircle(Canvas canvas, double acos, boolean progressCircle) {
        Paint paint;
        if (progressCircle) {
            paint = leftProgressSmallCirclePaint;
        } else {
            paint = smallCirclePaint;
        }
        int outRadius = getWidth() >> 1;
        float x = (float) (outRadius - (outRadius - smallCircleRadius) * Math.cos(angleToPI(acos)));
        float y = (float) (outRadius + (outRadius - smallCircleRadius) * Math.sin(angleToPI(acos)));
        canvas.drawCircle(x, y, smallCircleRadius, paint);
    }


    /**
     * 将角度转为弧度值：
     */
    private double angleToPI(double angle) {
        return Math.PI * angle / 180f;
    }

    /**
     * 画右边的小圆：
     */
    private void drawRightLittleCircle(Canvas canvas, double angle, boolean progressCircle) {
        Paint paint;
        if (progressCircle) {
            // 画的是进度右边的小圆：
            paint = rightProgressSmallCirclePaint;
            int outRadius = getWidth() >> 1;
            if (angle <= 180) {
                double realAngle = angleToPI(180 - angle);
                float y = (float) (outRadius + (outRadius - smallCircleRadius) * Math.sin(realAngle));
                float x = (float) (outRadius - (outRadius - smallCircleRadius) * Math.cos(realAngle));
                canvas.drawCircle(x, y, smallCircleRadius, paint);
            } else if (angle <= 270) {
                double realAngle = angleToPI(270 - angle);
                float y = (float) (outRadius - (outRadius - smallCircleRadius) * Math.cos(realAngle));
                float x = (float) (outRadius - (outRadius - smallCircleRadius) * Math.sin(realAngle));
                canvas.drawCircle(x, y, smallCircleRadius, paint);
            } else if (angle <= 360) {
                double realAngle = angleToPI(360 - angle);
                float y = (float) (outRadius - (outRadius - smallCircleRadius) * Math.sin(realAngle));
                float x = (float) (outRadius + (outRadius - smallCircleRadius) * Math.cos(realAngle));
                canvas.drawCircle(x, y, smallCircleRadius, paint);
            } else if (angle <= 450) {
                double realAngle = angleToPI(450 - angle);
                float y = (float) (outRadius + (outRadius - smallCircleRadius) * Math.cos(realAngle));
                float x = (float) (outRadius + (outRadius - smallCircleRadius) * Math.sin(realAngle));
                canvas.drawCircle(x, y, smallCircleRadius, paint);
            }
        } else {
            // 画的是背景中的右边小圆：
            paint = smallCirclePaint;
            int outRadius = getWidth() >> 1;
            float y = (float) (outRadius + (outRadius - smallCircleRadius) * Math.sin(angleToPI(angle)));
            float x = (float) (outRadius + (outRadius - smallCircleRadius) * Math.cos(angleToPI(angle)));
            canvas.drawCircle(x, y, smallCircleRadius, paint);
        }
    }

    /**
     * 画中间盖住大圆部分的小圆
     */
    private void drawInnerCircle(Canvas canvas) {
        canvas.save();
        int width = getWidth();
        int radius = width / 2 - ringWidth;
        canvas.drawCircle(width >> 1, width >> 1, radius, innerPaint);
        canvas.restore();
    }

    /**
     * 画最外面的大圆
     */
    private void drawOutreCircle(Canvas canvas) {
        int startDegrees = angle;
        int endDegrees = -180 - startDegrees * 2;
        RectF rectF = new RectF(0, 0, getWidth(), getWidth());
        canvas.drawArc(rectF, startDegrees, endDegrees, true, outerPaint);

        drawLeftLittleCircle(canvas, angle, false);
        drawRightLittleCircle(canvas, angle, false);
        drawProgressCircle(canvas, Math.abs(endDegrees), angle);
    }

    public void setProgress(float progress) {
        this.progress = progress > 1 ? 1 : progress;
        init();
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width;
        if (MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.EXACTLY) {
            width = MeasureSpec.getSize(widthMeasureSpec);
        } else {
            width = dp2px(150);
        }
        int radius = width >> 1;
        int height = (int) (radius + (radius - smallCircleRadius) * Math.sin(angleToPI(angle)) + smallCircleRadius);
        setMeasuredDimension(width, height);
    }

    private int dp2px(final float dpValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public void setRingBackGroundColor(int ringBackGroundColor) {
        this.ringBackGroundColor = ringBackGroundColor;
        init();
        invalidate();
    }
}