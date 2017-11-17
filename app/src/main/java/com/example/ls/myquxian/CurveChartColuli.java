package com.example.ls.myquxian;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.DashPathEffect;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;


/**
 * Created by 路很长~ on 2017/2/26.
 */

public class CurveChartColuli extends View {
    public int mWidth;
    public int mHeight;
    private PaintFlagsDrawFilter drawFilter;
    private Paint paint;
    private Paint textPaint;
    private Paint dashPaint;
    private float[] xValues;
    private float[] yValues;
    private float xStart;
    private long yStart;
    private float xEnd, yEnd, compareValue;
    private boolean isFillDownLineColor;
    private float scaleLen;
    private float scaleDistance;
    private float perLengthX;
    private float perLengthY;
    private Paint linePaint;
    private boolean stop;
    private int numCount;
    private boolean fillDownLineColor;
    private int fillColor;
    private float fraction;
    private float endCircleX, endCircleY;

    public CurveChartColuli(Context context) {
        super(context);
    }

    public CurveChartColuli(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        //在画布上去除锯齿

        //在画布上去除锯齿
        drawFilter = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG
                | Paint.FILTER_BITMAP_FLAG);
        paint = new Paint();
        paint.setColor(Color.BLACK);
        textPaint = new Paint();
        dashPaint = new Paint();

        scaleLen = dip2px(context, scaleLen);
        isFillDownLineColor = true;
        xStart = 0;
        yStart = 0;
        xEnd = 40;
        yEnd = 100;
        compareValue = 95;
    }


    public CurveChartColuli(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CurveChartColuli(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSpectMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSpectSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpectMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSpectSize = MeasureSpec.getSize(heightMeasureSpec);
        if (widthSpectMode == MeasureSpec.AT_MOST
                && heightSpectMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(mWidth, mHeight);
        } else if (widthSpectMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(mWidth, heightSpectSize);
        } else if (heightSpectMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(widthSpectSize, mHeight);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.setDrawFilter(drawFilter);
        drawCoordinate(canvas);
        drawPoint(canvas);
    }

    /**
     * 画坐标系
     *
     * @param canvas
     */
    private void drawCoordinate(Canvas canvas) {
        Rect rect = new Rect();
        textPaint.getTextBounds("300", 1, 3, rect);
        // 所画的坐标系的原点位置
        int startX = (int) (getPaddingLeft() + rect.width() + scaleDistance);
        int startY = (int) (mHeight - rect.height() - getPaddingBottom() - scaleDistance);
        // X轴的长度
        int lengthX = mWidth - getPaddingRight() - startX;
        // Y轴的长度
        int lengthY = startY - getPaddingTop();
        float countX, countY;
        countX = xEnd - xStart;
        countY = yEnd - yStart;
        // x轴每个刻度的长度
        perLengthX = 1.0f * lengthX / countX - 1;
        // y轴每个刻度的长度
        perLengthY = 1.0f * lengthY / countY;
        paint.setColor(Color.parseColor("#000000"));
        // 画横坐标
        canvas.drawLine(startX, startY, mWidth - getPaddingRight(), startY, paint);
        // 画纵坐标
        canvas.drawLine(startX, startY, startX, getPaddingTop(), paint);
        // 画x轴的刻度
        textPaint.setTextSize(18);
        for (int i = 0; i <= countX; i++) {
            if (i == 0) {
                // 画原点的数字
                canvas.drawText("1", startX, mHeight - getPaddingBottom(), textPaint);
                continue;
            }
            float x = startX + i * perLengthX;
            float y1 = startY - scaleLen;
            float y2 = startY - 2 * scaleLen;
            textPaint.setColor(Color.GRAY);
            if (i % 1 == 0) {
                canvas.drawLine(x, startY, x, y2, paint);
                if (i <6) {
                    // 加长一点
                    // 画下面的数字
                    canvas.drawText("" + (int) (xStart + i), x - rect.width() / 2, mHeight - getPaddingBottom(), textPaint);
                } else if (i ==6) {
                    textPaint.setColor(Color.parseColor("#ffa302"));
                    canvas.drawText("7", x - rect.width() / 2, mHeight - getPaddingBottom(), textPaint);

                }
                if (i == 8) {
                    textPaint.setColor(Color.parseColor("#ffffff"));
                    canvas.drawText("0" + (int) (xStart + i), x - rect.width() / 2, mHeight - getPaddingBottom(), textPaint);
                }
            } else {
                canvas.drawLine(x, startY, x, y1, paint);
            }
        }
        // 画y轴的刻度
        for (int i = 0; i <= countY; i++) {
            if (i == 0) {
                textPaint.setColor(Color.parseColor("#ffffff"));
                canvas.drawText("" + (int) yStart, getPaddingLeft(), startY, textPaint);
                continue;
            }
            float y = startY - i * perLengthY;
            float x1 = startX + scaleLen;
            float x2 = startX + 2 * scaleLen;
            paint.setColor(Color.BLACK);
            textPaint.setColor(Color.GRAY);
           /* if (i % 10 == 0) {
                // 加长一点
                canvas.drawLine(startX, y, x2, y, paint);
                canvas.drawText("" + (int) (yStart + i), getPaddingLeft(), y + rect.height() / 2, textPaint);
            } else {
                canvas.drawLine(startX, y, x1, y, paint);
            }*/
        }
    }

    private void drawPoint(Canvas canvas) {
        // 用于保存y值大于compareValue的值
        if (xValues != null) {
            float[] storageX = new float[xValues.length];
            float[] storageY = new float[xValues.length];
            Rect rect = new Rect();
            textPaint.getTextBounds("300", 0, 3, rect);
            int startX = (int) (getPaddingLeft() + rect.width() + scaleDistance);
            int startY = (int) (mHeight - rect.height() - getPaddingBottom()
                    - scaleDistance);
            linePaint = new Paint();
            linePaint.setColor(Color.parseColor("#feb029"));
            // 把拐点设置成圆的形式，参数为圆的半径，这样就可以画出曲线了
            PathEffect pe = new CornerPathEffect(60);
            //linePaint.setPathEffect(pe);
            if (!isFillDownLineColor) {
                linePaint.setStyle(Paint.Style.STROKE);
            }
            Path path = new Path();
            Path path2 = new Path();
            path.moveTo(startX + (xValues[0] - xStart) * perLengthX, startY
                    - (yValues[0] - yStart) * perLengthY * fraction);
            int count = xValues.length;
            for (int i = 0; i < count - 1; i++) {
                float x, y, x2, y2, x3, y3, x4, y4;
                x = startX + (xValues[i] - xStart) * perLengthX;
                x4 = (startX + (xValues[i + 1] - xStart) * perLengthX);
                x2 = x3 = (x + x4) / 2;
                // 乘以这个fraction是为了添加动画特效
                y = startY - (yValues[i] - yStart) * perLengthY * fraction;
                y4 = startY - (yValues[i + 1] - yStart) * perLengthY * fraction;
                y2 = y;
                y3 = y4;
                if (yValues[i] > compareValue) {
                    storageX[i] = x;
                    storageY[i] = y;
                }
                if (!isFillDownLineColor && i == 0) {
                    path2.moveTo(x, y);
                    path.moveTo(x, y);
                    continue;
                }
                // 填充颜色
                if (isFillDownLineColor && i == 0) {
                    // 形成封闭的图形
                    path2.moveTo(x, y);
                    path.moveTo(x, startY);
                    path.lineTo(x, y);
                }

                // // 填充颜色
                // if (isFillDownLineColor && i == count - 1) {
                // path.lineTo(x, startY);
                // }
                path.cubicTo(x2, y2, x3, y3, x4, y4);
                path2.cubicTo(x2, y2, x3, y3, x4, y4);
            }
            if (isFillDownLineColor) {
                // 形成封闭的图形
                path.lineTo(startX + (xValues[count - 1] - xStart) * perLengthX, startY);
            }
            Paint rectPaint = new Paint();
            rectPaint.setColor(Color.BLUE);
            float left = startX + (xValues[0] - xStart) * perLengthX;
            float top = getPaddingTop();
            float right = startX + (xValues[count - 1] - xStart) * perLengthX;
            float bottom = startY;
            // 渐变的颜色
            LinearGradient lg = new LinearGradient(left, top, left, bottom, Color.parseColor("#00ffffff"),
                    Color.parseColor("#bFffffff"), Shader.TileMode.CLAMP);// CLAMP重复最后一个颜色至最后
            rectPaint.setShader(lg);
            rectPaint.setXfermode(new PorterDuffXfermode(
                    android.graphics.PorterDuff.Mode.SRC_ATOP));
            if (isFillDownLineColor) {
                canvas.drawPath(path, linePaint);
            }
            canvas.drawRect(left, top, right, bottom, rectPaint);
            // canvas.restoreToCount(layerId);
            rectPaint.setXfermode(null);
            linePaint.setStyle(Paint.Style.STROKE);
            linePaint.setStrokeWidth((float) 2.3);
            linePaint.setColor(Color.parseColor("#FFFE8C29"));//设置曲线的颜色的
            canvas.drawPath(path2, linePaint);
            linePaint.setPathEffect(null);
            drawDashAndPoint(storageX, storageY, startY, canvas);
            if (!stop)
                performAnimator();
            if (fraction > 0.99) {
                performAnimator();
            }
        }
    }


    private void drawDashAndPoint(float[] x, float[] y, float startY,
                                  Canvas canvas) {
        PathEffect pe = new DashPathEffect(new float[]{10, 10}, 1);
        // 要设置不是填充的，不然画一条虚线是没显示出来的
        dashPaint.setStyle(Paint.Style.STROKE);
        dashPaint.setPathEffect(pe);
        dashPaint.setColor(Color.BLACK);
        Paint pointPaint = new Paint();
        pointPaint.setColor(Color.BLACK);
        for (int i = 0; i < x.length; i++) {
            if (y[i] > 1) {

                canvas.drawCircle(x[i], y[i], 11, pointPaint);
                Path path = new Path();
                path.moveTo(x[i], startY);
                path.lineTo(x[i], y[i]);
                canvas.drawPath(path, dashPaint);
            }
        }

    }

    public void performAnimator() {
        if (numCount > 3)
            return;
        ValueAnimator va = ValueAnimator.ofFloat(0, 1);
        if (numCount == 1) {
            va = ValueAnimator.ofFloat(0, 1);
        } else if (numCount == 2) {
            va = ValueAnimator.ofFloat(0.85f, 1);
        } else if (numCount == 3) {
            va = ValueAnimator.ofFloat(0.95f, 1);
        }
        numCount++;
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                fraction = (Float) animation.getAnimatedValue();
                stop = true;
                postInvalidate();
            }
        });
        va.setDuration(1000);
        va.start();
    }

    public void setFillDownLineColor(boolean fillDownLineColor) {
        this.fillDownLineColor = fillDownLineColor;
    }

    public void setFillColor(int fillColor) {
        this.fillColor = fillColor;
    }

    public void setCompareValue(float compareValue) {
        this.compareValue = compareValue;
    }



    public static class CurveChartBuilder {
        private static CurveChartColuli curveChart;
        private static CurveChartBuilder cBuilder;

        private CurveChartBuilder() {
        }

        public static CurveChartBuilder createBuilder(CurveChartColuli curve) {
            curveChart = curve;
            synchronized (CurveChartBuilder.class) {
                if (cBuilder == null) {
                    cBuilder = new CurveChartBuilder();
                }
            }
            return cBuilder;
        }

        /**
         * 设置x，y轴的刻度
         *
         * @param xStart X轴开始的刻度
         * @param xEnd   X轴结束的刻度
         * @param yStart
         * @param yEnd
         * @return
         */
        public static CurveChartBuilder setXYCoordinate(float xStart, float xEnd, float yStart, float yEnd) {
            curveChart.setxStart(xStart, xEnd);
            curveChart.setyStart(yStart, yEnd);
            return cBuilder;
        }

        /**
         * 是否填充曲线下面的颜色，默认值为true，
         *
         * @param isFillDownLineColor
         * @return
         */
        public static CurveChartBuilder setIsFillDownColor(boolean isFillDownLineColor) {
            curveChart.setFillDownLineColor(isFillDownLineColor);
            return cBuilder;
        }

        /**
         * 设置填充的颜色
         *
         * @param fillColor
         * @return
         */
        public static CurveChartBuilder setFillDownColor(int fillColor) {
            curveChart.setFillColor(fillColor);
            return cBuilder;
        }

        /**
         * 比较的值，比这个值大就把这个点也绘制出来
         *
         * @param compareValue
         * @return
         */
        public static CurveChartBuilder setCompareValue(float compareValue) {
            curveChart.setCompareValue(compareValue);
            return cBuilder;
        }

        public static CurveChartBuilder setXYValues(float[] xValues, float[] yValues) {
            curveChart.setxValues(xValues);
            curveChart.setyValues(yValues);
            return cBuilder;
        }

        public void show() {
            if (curveChart == null) {
                throw new NullPointerException("CurveChart is null");
            }
            cBuilder.show();
        }
    }

    public void setxValues(float[] xValues) {
        this.xValues = xValues;
    }

    public void setyValues(float[] yValues) {
        this.yValues = yValues;
    }

    private void setyStart(float yStart, float yEnd) {

    }

    private void setxStart(float xStart, float xEnd) {
        this.xStart = xStart;
        this.xEnd = xEnd;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
