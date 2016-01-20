package com.chrynan.guitartuner.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import com.chrynan.guitartuner.R;

/**
 * Created by chRyNaN on 1/17/2016.
 */
public class CircleView extends View {
    private Paint paint;
    private Paint textPaint;
    private Rect textBounds;

    private int color;
    private int textColor;

    private int textSize;
    private int diameter;
    private float circleCenterX;
    private float circleCenterY;
    private String text;
    private boolean centerX;
    private boolean centerY;

    public CircleView(Context context){
        super(context);
        init(null);
    }

    public CircleView(Context context, AttributeSet attrs){
        super(context, attrs);
        init(attrs);
    }

    public CircleView(Context context, AttributeSet attrs, int defStyleAttr){
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CircleView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    private void init(AttributeSet attrs){
        text = "A";
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        //set defaults
        paint.setStyle(Paint.Style.FILL);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setTextAlign(Paint.Align.CENTER);
        if(attrs != null){
            TypedArray a = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.CircleTunerView, 0, 0);
            try{
                color = a.getColor(R.styleable.CircleTunerView_innerCircleColor, CircleTunerView.DEFAULT_CIRCLE_COLOR);
                paint.setColor(color);
                textColor = a.getColor(R.styleable.CircleTunerView_innerCircleTextColor, CircleTunerView.DEFAULT_TEXT_COLOR);
                textPaint.setColor(textColor);
                centerX = a.getBoolean(R.styleable.CircleTunerView_centerX, true);
                centerY = a.getBoolean(R.styleable.CircleTunerView_centerY, true);
            }catch(Exception e){
                e.printStackTrace();
            }finally{
                a.recycle();
            }
        }else{
            color = CircleTunerView.DEFAULT_CIRCLE_COLOR;
            textColor = CircleTunerView.DEFAULT_TEXT_COLOR;
            paint.setColor(color);
            textPaint.setColor(textColor);
            centerX = true;
            centerY = true;
        }
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            setElevation();
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void setElevation(){
        setElevation(5f);
    }

    @Override
    protected void onSizeChanged(int width, int height, int oldWidth, int oldHeight) {
        float xPad = (float) (getPaddingLeft() + getPaddingRight());
        float yPad = (float) (getPaddingTop() + getPaddingBottom());
        //width minus the padding
        float xWidth = width - xPad;
        //height minus the padding
        float yHeight = height - yPad;
        int outerCircleDiameter = (int) Math.min(xWidth, yHeight);
        diameter = outerCircleDiameter / 2;
        if(centerX) {
            circleCenterX = (int) xWidth / 2;
        }else{
            circleCenterX = outerCircleDiameter / 2;
        }
        if(centerY) {
            circleCenterY = (int) yHeight / 2;
        }else{
            circleCenterY = outerCircleDiameter / 2;
        }
        textSize = diameter / 2;
        textPaint.setTextSize(textSize);
    }

    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
        //draw the inner circle
        canvas.drawCircle(circleCenterX, circleCenterY, diameter / 2, paint);
        textBounds = new Rect();
        textPaint.getTextBounds(text, 0, text.length(), textBounds);
        canvas.drawText(text, circleCenterX, circleCenterY + (textBounds.height() / 2), textPaint);
    }

    public int getTextSize(){
        return textSize;
    }

    public int getDiameter(){
        return diameter;
    }

    public float getCircleCenterX(){
        return circleCenterX;
    }

    public float getCircleCenterY(){
        return circleCenterY;
    }

    public String getText(){
        return text;
    }

    public void setText(String text) {
        this.text = text;
        invalidate();
    }

    public int getColor(){
        return color;
    }

    public void setColor(int color){
        this.color = color;
        this.paint.setColor(color);
        invalidate();
    }

    public int getTextColor(){
        return textColor;
    }

    public void setTextColor(int color){
        this.textColor = color;
        this.textPaint.setColor(textColor);
        invalidate();
    }

    public boolean isCenterX() {
        return centerX;
    }

    public void setCenterX(boolean centerX) {
        this.centerX = centerX;
        requestLayout();
    }

    public boolean isCenterY() {
        return centerY;
    }

    public void setCenterY(boolean centerY) {
        this.centerY = centerY;
        requestLayout();
    }

}
