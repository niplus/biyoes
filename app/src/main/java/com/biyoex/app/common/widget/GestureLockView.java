package com.biyoex.app.common.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Abstract class for user to customize the GestureLock block style
 */
public abstract class GestureLockView extends View {

    private static final boolean DEBUG = false;

    private int mWidth, mHeight;
    private int mCenterX, mCenterY;

    private Paint mPaint;

    public enum LockerState{
        LOCKER_STATE_NORMAL, LOCKER_STATE_ERROR, LOCKER_STATE_SELECTED
    }

    private LockerState mState = LockerState.LOCKER_STATE_NORMAL;

    private int errorArrow = -1;

    public GestureLockView(Context context){
        this(context, null);
    }

    public GestureLockView(Context context, AttributeSet attrs){
        this(context, attrs, 0);
    }

    public GestureLockView(Context context, AttributeSet attrs, int defStyle){
        super(context, attrs, defStyle);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh){

        mWidth = w;
        mHeight = h;

        mCenterX = mWidth / 2;
        mCenterY = mHeight / 2;
    }

    public void setLockerState(LockerState state){
        mState = state;
        invalidate();
    }

    public LockerState getLockerState(){
        return mState;
    }

    public void setArrow(int arrow){
        errorArrow = arrow;

        invalidate();
    }

    public int getArrow(){
        return errorArrow;
    }

    /**
     * override this method to do GestureLock block drawing work
     * @param state current state of this GustureLock block
     * @param canvas
     */
    protected abstract void doDraw(LockerState state, Canvas canvas);

    /**
     * override this method to do Arrow drawing, you should notice
     * @param canvas
     */
    protected abstract void doArrowDraw(Canvas canvas);

    @Override
    public void onDraw(Canvas canvas){

        if(DEBUG){
            if(mPaint == null){
                mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            }
            mPaint.setColor(0xFFFF0000);
            mPaint.setTextSize(20);
            canvas.drawText(getId() + "", 0, getHeight(), mPaint);
        }

        doDraw(mState, canvas);

        if(errorArrow != -1){

            canvas.save();
            canvas.rotate(errorArrow, mCenterX, mCenterY);
//            doArrowDraw(canvas);

            canvas.restore();
        }

    }

}
