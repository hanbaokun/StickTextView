package cn.rosen.sticktextview.view;

import android.graphics.Canvas;
import android.graphics.RectF;
import android.widget.TextView;

/**
 * Created by lenovo on 2016/6/17.
 */
public class TextViewItem {

    public int getNums() {
        return nums;
    }

    private final int nums;

    public RectF getmOriginTextRect() {
        return mOriginTextRect;
    }

    private final RectF mOriginTextRect;
    private TextView textView;
    private Canvas canvas;
    private float[] mOriginTextPoints;
    private float[] mTextPoints;

    public boolean isSingleLine() {
        return isSingleLine;
    }

    public void setSingleLine(boolean singleLine) {
        isSingleLine = singleLine;
    }

    private boolean isSingleLine;

    public Canvas getCanvas() {
        return canvas;
    }

    public void setCanvas(Canvas canvas) {
        this.canvas = canvas;
    }


    public float[] getmOriginTextPoints() {
        return mOriginTextPoints;
    }

    public void setmOriginTextPoints(float[] mOriginTextPoints) {
        this.mOriginTextPoints = mOriginTextPoints;
    }

    public float[] getmTextPoints() {
        return mTextPoints;
    }

    public void setmTextPoints(float[] mTextPoints) {
        this.mTextPoints = mTextPoints;
    }

    public TextView getTextView() {
        return textView;
    }

    public void setTextView(TextView textView) {
        this.textView = textView;
    }

    public TextViewItem(TextView textView, Canvas canvas, boolean isSingleLine, RectF mOriginTextRect, int nums) {
        this.mOriginTextRect = mOriginTextRect;
        this.textView = textView;
        this.canvas = canvas;
        this.isSingleLine = isSingleLine;
        this.nums = nums;
    }
}
