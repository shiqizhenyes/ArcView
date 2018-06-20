package com.arc.view.arcview.view;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Shader;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.arc.view.arcview.R;


/**
 * Created by XIAS on 2017/12/25.
 */

public class ArcView extends View {

    private boolean rabbet = false;//弧形是凸还是凹，默认是凸
    private boolean gradation = false;
    private int mArcHeight = 80;//弧形高度
    private ColorStateList startColor;
    private ColorStateList endColor;
    private LinearGradient linearGradient;


    public boolean isRabbet() {
        return rabbet;
    }

    public void setRabbet(boolean rabbet) {
        this.rabbet = rabbet;
    }

    public int getmArcHeight() {
        return mArcHeight;
    }

    public void setmArcHeight(int mArcHeight) {
        this.mArcHeight = mArcHeight;
    }

    public boolean isGradation() {
        return gradation;
    }

    public void setGradation(boolean gradation) {
        this.gradation = gradation;
    }

    public ColorStateList getStartColor() {
        return startColor;
    }

    public void setStartColor(ColorStateList startColor) {
        this.startColor = startColor;
    }

    public ColorStateList getEndColor() {
        return endColor;
    }

    public void setEndColor(ColorStateList endColor) {
        this.endColor = endColor;
    }

    private Paint mPaint;//画笔
    private Path mPath;//贝塞尔曲线路径
    private PointF mSPointF, mCPointF, mEPointF;//贝塞尔曲线关键点 分别为起始点，控制点，终止点
    private int w, h;

    public ArcView(Context context) {
        this(context, null);
    }

    public ArcView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }


    public ArcView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context,attrs,defStyleAttr,0);
    }

    public ArcView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr);

        Resources.Theme theme = context.getTheme();
        TypedArray a = theme.obtainStyledAttributes(attrs,
                R.styleable.ArcView, defStyleAttr, defStyleRes);
        int n = a.getIndexCount();
        for (int i = 0; i < n ; i++) {
            int attr = a.getIndex(i);
            switch (attr) {
                case R.styleable.ArcView_rabbet:
                    setRabbet(a.getBoolean(attr,rabbet));
                    break;
                case R.styleable.ArcView_gradation:
                    setGradation(a.getBoolean(attr,gradation));
                    break;
                case R.styleable.ArcView_arcHeight:
                    setmArcHeight(a.getDimensionPixelSize(attr,mArcHeight));
                    break;
                case R.styleable.ArcView_startColor:
                    setStartColor(a.getColorStateList(attr));
                    break;
                case R.styleable.ArcView_endColor:
                    setEndColor(a.getColorStateList(attr));
                    break;
            }
        }

        init();
    }


    private void init(){
        //初始化画笔
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPath = new Path();
        mSPointF = new PointF();
        mCPointF = new PointF();
        mEPointF = new PointF();
    }

    public void refresh() {
        linearGradient = new LinearGradient(0,0,w,h, getStartColor()
                .getColorForState(getDrawableState(),0),
                getEndColor().getColorForState(getDrawableState(),0),
                Shader.TileMode.MIRROR);
        if (isGradation()) {
            mPaint.setShader(linearGradient);
        }else {
            mPaint.setColor(getStartColor().getColorForState(getDrawableState(),0));
        }
        invalidate();
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.w = w;
        this.h = h;
    }

    private void drawArc(){
        mPath.reset();
        linearGradient = new LinearGradient(0,0,w,h,
                getStartColor().getColorForState(getDrawableState(),0),
                getEndColor().getColorForState(getDrawableState(),0),
                Shader.TileMode.MIRROR);
        mPath.moveTo(0, 0);
        if(rabbet)
            mPath.addRect(0, 0, w, h, Path.Direction.CCW);
        else
            mPath.addRect(0, 0, w, h - mArcHeight, Path.Direction.CCW);
        mSPointF.x = 0;
        if(rabbet)
            mSPointF.y = h;
        else
            mSPointF.y = h - mArcHeight;

        mEPointF.x = w;
        if(rabbet)
            mEPointF.y = h;
        else
            mEPointF.y = h - mArcHeight;

        mCPointF.x = w / 2;
        if(rabbet)
            mCPointF.y = h - mArcHeight;
        else
            mCPointF.y = h + mArcHeight;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawArc();
        if (isGradation()) {
            mPaint.setShader(linearGradient);
        }else {
            mPaint.setColor(getStartColor().getColorForState(getDrawableState(),0));
        }
        mPath.moveTo(mSPointF.x, mSPointF.y);
        mPath.quadTo(mCPointF.x, mCPointF.y, mEPointF.x, mEPointF.y);
        canvas.drawPath(mPath, mPaint);
        super.onDraw(canvas);
    }

}
