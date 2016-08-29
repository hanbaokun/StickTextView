package cn.rosen.sticktextview.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.PointF;
import android.graphics.RectF;
import android.support.v4.view.MotionEventCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import java.util.LinkedHashMap;

import cn.rosen.sticktextview.R;


/**
 * Created by sam on 14-8-14.
 */
public class StickerTextView extends View {

    private String TAG = "StickerView";

    public static final float MAX_SCALE_SIZE = 10;//3.2f;

    public static final float MIN_SCALE_SIZE = 0;//0.6f;

    private float[] mOriginPoints;
    private float[] mPoints;
    private RectF mOriginContentRect;
    private RectF mContentRect;
    private RectF mViewRect;


    private PointF mid = new PointF();

    private float mLastPointX, mLastPointY;
    private Bitmap mBitmap;
    private Bitmap originBitmap;

    private Bitmap mControllerBitmap, mDeleteBitmap;
    private Bitmap mCopyBitmap;
    private Matrix mMatrix;
    private Paint mPaint, mBorderPaint;
    private float mControllerWidth, mControllerHeight, mDeleteWidth, mDeleteHeight, mCopyWidth, mCopyHeight;

    private boolean mInController, mInMove;
    private boolean mDrawController = true;
    private float mStickerScaleSize = 1.0f;
    private DisplayMetrics dm;
    private boolean isMove;

    private float oldx1, oldy1, oldx2, oldy2;
    private float roatetAngle = 0;

    private OnStickerTextTouchListener mOnStickerTouchListener;
    private Paint mTextPaint;

    //存放文字的集合
    private LinkedHashMap<Integer, TextViewItem> bank = new LinkedHashMap<>();
    private int imageCount;


    public StickerTextView(Context context) {
        this(context, null);
    }

    public StickerTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StickerTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setFilterBitmap(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(4.0f);
        mPaint.setColor(Color.WHITE);
        //文字边框的画笔
        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setStyle(Paint.Style.STROKE);
        mTextPaint.setColor(Color.parseColor("#ff7700"));
        mTextPaint.setStrokeWidth(4);
        PathEffect effects = new DashPathEffect(new float[]{dpToPx(8), dpToPx(4)}, 1);
        mTextPaint.setPathEffect(effects);

        dm = getResources().getDisplayMetrics();
        //边框的画笔
        mBorderPaint = new Paint(mPaint);
        mBorderPaint.setColor(Color.BLACK);//#B2ffffff
        mBorderPaint.setShadowLayer(dpToPx(2.0f), 0, 0, Color.parseColor("#33000000"));

        mControllerBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.edit_wz_bg_btn3);
        mControllerWidth = mControllerBitmap.getWidth();
        mControllerHeight = mControllerBitmap.getHeight();

        mCopyBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.edit_wz_bg_btn2);
        mCopyWidth = mCopyBitmap.getWidth();
        mCopyHeight = mCopyBitmap.getHeight();

        mDeleteBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.edit_wz_bg_btn1);
        mDeleteWidth = mDeleteBitmap.getWidth();
        mDeleteHeight = mDeleteBitmap.getHeight();

    }

    public void setBackgroundBitmap(Bitmap bitmap) {
        if (bitmap != null) {
            originBitmap = bitmap;
            mBitmap = originBitmap.copy(Bitmap.Config.ARGB_8888, true);
        } else {
            mBitmap = bitmap;
        }
        setFocusable(true);
        try {
            if (mBitmap != null) {
                float px = mBitmap.getWidth();
                float py = mBitmap.getHeight();
                mOriginPoints = new float[]{0, 0, px, 0, px, py, 0, py, px / 2, py / 2};
                mPoints = new float[10];
                mOriginContentRect = new RectF(0, 0, px, py);
                mContentRect = new RectF();

                mMatrix = new Matrix();
                float transtLeft = ((float) dm.widthPixels - px) / 2;
                float transtTop = ((float) dm.heightPixels - py) / 2;

                mMatrix.postTranslate(transtLeft, transtTop);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        postInvalidate();
    }

    /**
     * 添加textView
     *
     * @param textView     textview对象
     * @param left         左边界
     * @param top          上边界
     * @param right        右边界
     * @param bottom       下边界
     * @param isSingleLine 是否单行，单行可自动调节边框大小
     * @param nums         字数限制
     */
    public void addTextDraw(TextView textView, float left, float top, float right, float bottom, boolean isSingleLine, int nums) {
        float x = dpToPx(left);
        float y = dpToPx(top);
        float r = dpToPx(right);
        float b = dpToPx(bottom);
        RectF mOriginTextRect;
        textView.setLeft((int) x);
        textView.setTop((int) y);
        if (isSingleLine) {
            textView.setRight((int) (getCharacterWidth(textView) + textView.getLeft()));
            mOriginTextRect = new RectF(x, y, (int) (getCharacterWidth(textView) + textView.getLeft()), b);
        } else {
            textView.setRight((int) r);
            mOriginTextRect = new RectF(x, y, r, b);
        }
        textView.setBottom((int) b);
        Canvas canvas = new Canvas();
        if (canvas != null) {
            canvas.translate(x, y);
        }
        canvas.setBitmap(mBitmap);
        bank.put(++imageCount, new TextViewItem(textView, canvas, isSingleLine, mOriginTextRect, nums));
        postInvalidate();
    }

    /**
     * 更新文字
     *
     * @param textView
     * @param isSingLine
     */
    public void updateTextDraw(TextView textView, boolean isSingLine) {
        if (isSingLine) {
            textView.setRight((int) (getCharacterWidth(textView) + textView.getLeft()));
        }
        invalidate();
    }

    //方法入口
    public float getCharacterWidth(TextView tv) {
        if (null == tv) return 0f;
        return getCharacterWidth(tv.getText().toString(), tv.getTextSize()) * tv.getScaleX();
    }

    //获取每个字符的宽度主方法：
    public float getCharacterWidth(String text, float size) {
        if (null == text || "".equals(text))
            return 0;
        Paint paint = new Paint();
        paint.setTextSize(size);
        float text_width = paint.measureText(text);//得到总体长度
        return text_width;
    }

    public Matrix getMarkMatrix() {
        return mMatrix;
    }

    @Override
    public void setFocusable(boolean focusable) {
        super.setFocusable(focusable);
        postInvalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mBitmap == null || mMatrix == null) {
            return;
        }
        //背景和边框映射
        mMatrix.mapPoints(mPoints, mOriginPoints);
        mMatrix.mapRect(mContentRect, mOriginContentRect);
        mBitmap = originBitmap.copy(Bitmap.Config.ARGB_8888, true);
        for (Integer id : bank.keySet()) {
            TextViewItem textViewItem = bank.get(id);
            int left = textViewItem.getTextView().getLeft();
            int top = textViewItem.getTextView().getTop();
            int right = textViewItem.getTextView().getRight();
            int bottom = textViewItem.getTextView().getBottom();
            float[] mOriginTextPoints;
            float[] mTextPoints = new float[8];
            if (TextUtils.isEmpty(textViewItem.getTextView().getText())) {
                RectF rectF = textViewItem.getmOriginTextRect();
                mOriginTextPoints = new float[]{rectF.left, rectF.top, rectF.right, rectF.top, rectF.right, rectF.bottom, rectF.left, rectF.bottom};
            } else {
                mOriginTextPoints = new float[]{left, top, right, top, right, bottom, left, bottom};
            }
            //文字和边框映射
            mMatrix.mapPoints(mTextPoints, mOriginTextPoints);
            if (mDrawController) {
                Path path = new Path();
                path.moveTo(mTextPoints[0], mTextPoints[1]);
                path.lineTo(mTextPoints[2], mTextPoints[3]);
                canvas.drawPath(path, mTextPaint);
                path.moveTo(mTextPoints[2], mTextPoints[3]);
                path.lineTo(mTextPoints[4], mTextPoints[5]);
                canvas.drawPath(path, mTextPaint);
                path.moveTo(mTextPoints[4], mTextPoints[5]);
                path.lineTo(mTextPoints[6], mTextPoints[7]);
                canvas.drawPath(path, mTextPaint);
                path.moveTo(mTextPoints[6], mTextPoints[7]);
                path.lineTo(mTextPoints[0], mTextPoints[1]);
                canvas.drawPath(path, mTextPaint);
            }
            //画字体
            textViewItem.getCanvas().setBitmap(mBitmap);
            textViewItem.getCanvas().setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
            textViewItem.getTextView().draw(textViewItem.getCanvas());
        }
        //花边框和控制点
        if (mDrawController) {
            canvas.drawLine(mPoints[0], mPoints[1], mPoints[2], mPoints[3], mBorderPaint);
            canvas.drawLine(mPoints[2], mPoints[3], mPoints[4], mPoints[5], mBorderPaint);
            canvas.drawLine(mPoints[4], mPoints[5], mPoints[6], mPoints[7], mBorderPaint);
            canvas.drawLine(mPoints[6], mPoints[7], mPoints[0], mPoints[1], mBorderPaint);
            canvas.drawBitmap(mControllerBitmap, mPoints[4] - mControllerWidth / 2, mPoints[5] - mControllerHeight / 2, mBorderPaint);
            canvas.drawBitmap(mCopyBitmap, mPoints[2] - mCopyWidth / 2, mPoints[3] - mCopyHeight / 2, mBorderPaint);
            canvas.drawBitmap(mDeleteBitmap, mPoints[0] - mDeleteWidth / 2, mPoints[1] - mDeleteHeight / 2, mBorderPaint);
        }
        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        canvas.drawBitmap(mBitmap, mMatrix, mPaint);
    }


    private float dpToPx(float pt) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, pt, getResources().getDisplayMetrics());
    }

    public void setShowDrawController(boolean show) {
        mDrawController = show;
        invalidate();
    }


    private boolean isInController(float x, float y) {
        int position = 4;
        //while (position < 8) {
        float rx = mPoints[position];
        float ry = mPoints[position + 1];
        RectF rectF = new RectF(rx - mControllerWidth / 2,
                ry - mControllerHeight / 2,
                rx + mControllerWidth / 2,
                ry + mControllerHeight / 2);
        if (rectF.contains(x, y)) {
            return true;
        }
        return false;

    }

    private boolean isInDelete(float x, float y) {
        int position = 0;
        //while (position < 8) {
        float rx = mPoints[position];
        float ry = mPoints[position + 1];
        RectF rectF = new RectF(rx - mDeleteWidth / 2,
                ry - mDeleteHeight / 2,
                rx + mDeleteWidth / 2,
                ry + mDeleteHeight / 2);
        if (rectF.contains(x, y)) {
            return true;
        }
        return false;

    }

    private boolean isInCopy(float x, float y) {
        int position = 2;
        float rx = mPoints[position];
        float ry = mPoints[position + 1];
        RectF rectF = new RectF(rx - mCopyWidth / 2,
                ry - mCopyHeight / 2,
                rx + mCopyWidth / 2,
                ry + mCopyHeight / 2);
        if (rectF.contains(x, y)) {
            return true;
        }
        return false;
    }

    private boolean mInDelete = false;

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        return super.dispatchTouchEvent(event);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mOnStickerTouchListener != null) {
            mOnStickerTouchListener.onTextMoveToHead(this);
        }
        if (mViewRect == null) {
            mViewRect = new RectF(0f, 0f, getMeasuredWidth(), getMeasuredHeight());
        }
        if (event.getPointerCount() == 1)//单点触控
        {
            float x = event.getX();
            float y = event.getY();
            int action = MotionEventCompat.getActionMasked(event);
            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    if (isInController(x, y)) {
                        mInController = true;
                        mLastPointY = y;
                        mLastPointX = x;
                        midPointToStartPoint(x, y);
                    } else if (isInDelete(x, y)) {
                        mInDelete = true;
                    } else if (isInCopy(x, y)) {
                        if (mOnStickerTouchListener != null) {
                            mOnStickerTouchListener.onTextCopy(this);
                        }
                    } else if (mContentRect.contains(x, y)) {
                        setShowDrawController(true);
                        mLastPointY = y;
                        mLastPointX = x;
                        mInMove = true;
                        isMove = false;

                        //单击
                        for (Integer id : bank.keySet()) {
                            TextViewItem textViewItem = bank.get(id);
                            int left = textViewItem.getTextView().getLeft();
                            int top = textViewItem.getTextView().getTop();
                            int right = textViewItem.getTextView().getRight();
                            int bottom = textViewItem.getTextView().getBottom();
                            RectF mOriginTextRect;
                            if (TextUtils.isEmpty(textViewItem.getTextView().getText())) {
                                mOriginTextRect = textViewItem.getmOriginTextRect();
                            } else {
                                mOriginTextRect = new RectF(left, top, right, bottom);
                            }
                            RectF mTextRect = new RectF();
                            mMatrix.mapRect(mTextRect, mOriginTextRect);
                            if (mTextRect.contains(x, y)) {
                                //单击文字
                                if (mOnStickerTouchListener != null) {
                                    mOnStickerTouchListener.onTextClickCurrentText(textViewItem);
                                }
                            }
                        }
                    } else {
                        setShowDrawController(false);
                        return false;
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    mInController = false;
                    if (isInDelete(x, y) && mInDelete) {
                        mInDelete = false;
                        doDeleteSticker();
                    }
                case MotionEvent.ACTION_CANCEL:
                    mLastPointX = 0;
                    mLastPointY = 0;
                    mInController = false;
                    mInMove = false;
                    mInDelete = false;
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (mInController) {
                        mMatrix.postRotate(rotation(event), mid.x, mid.y);//mPoints[8], mPoints[9]
                        float nowLenght = caculateLength(mPoints[4], mPoints[5]);
                        float touchLenght = caculateLength(event.getX(), event.getY());
                        if ((float) Math.sqrt((nowLenght - touchLenght) * (nowLenght - touchLenght)) > 0.0f) {
                            float scale = touchLenght / nowLenght;
                            float nowsc = mStickerScaleSize * scale;
                            if (nowsc >= MIN_SCALE_SIZE && nowsc <= MAX_SCALE_SIZE) {
                                mMatrix.postScale(scale, scale, mid.x, mid.y);
                                mStickerScaleSize = nowsc;
//                            Log.i(TAG, mStickerScaleSize +"缩放比例");
                            }
                        }
                        invalidate();
                        mLastPointX = x;
                        mLastPointY = y;
                        break;
                    }
                    if (mInMove) { //拖动的操作
                        float cX = x - mLastPointX;
                        float cY = y - mLastPointY;
                        mInController = false;
                        //判断手指抖动距离 加上isMove判断 只要移动过 都是true
                        if (!isMove && Math.abs(cX) < 0.5f
                                && Math.abs(cY) < 0.5f) {
                            isMove = false;
                        } else {
                            isMove = true;
                        }
                        mMatrix.postTranslate(cX, cY);
                        postInvalidate();
                        mLastPointX = x;
                        mLastPointY = y;
                        break;
                    }
                    invalidate();
                    return true;
            }
        } else {
            //多点触控
            switch (event.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_POINTER_DOWN:
                    float x1 = event.getX(0);
                    float x2 = event.getX(1);
                    float y1 = event.getY(0);
                    float y2 = event.getY(1);
                    if (mContentRect.contains(x1, y1) || mContentRect.contains(x2, y2)) {
                        setShowDrawController(true);
                        mInController = true;
                        oldx1 = x1;
                        oldy1 = y1;
                        oldx2 = x2;
                        oldy2 = y2;
                    } else {
                        setShowDrawController(false);
                        return false;
                    }
                    break;
                case MotionEvent.ACTION_MOVE:
                    x1 = event.getX(0);
                    x2 = event.getX(1);
                    y1 = event.getY(0);
                    y2 = event.getY(1);
                    if (mInController) {
                        float xa = oldx2 - oldx1;
                        float ya = oldy2 - oldy1;

                        float xb = x2 - x1;
                        float yb = y2 - y1;

                        float nowLenght = (float) Math.sqrt(xa * xa + ya * ya);
                        float touchLenght = (float) Math.sqrt(xb * xb + yb * yb);
                        if ((float) Math.sqrt((nowLenght - touchLenght) * (nowLenght - touchLenght)) > 0.0f) {
                            float scale = touchLenght / nowLenght;
                            float nowsc = mStickerScaleSize * scale;
                            if (nowsc >= MIN_SCALE_SIZE && nowsc <= MAX_SCALE_SIZE) {
                                mMatrix.postScale(scale, scale, mContentRect.centerX(), mContentRect.centerY());
                                mStickerScaleSize = nowsc;
                                //Log.i(TAG, mStickerScaleSize +"缩放比例");
                            }
                            double cos = (xa * xb + ya * yb) / (nowLenght * touchLenght);
                            if (cos > 1 || cos < -1) {
                                return false;
                            }
                            float angle = (float) Math.toDegrees(Math.acos(cos));

                            // 拉普拉斯定理
                            float calMatrix = xa * yb - xb * ya;// 行列式计算 确定转动方向

                            int flag = calMatrix > 0 ? 1 : -1;
                            angle = flag * angle;
                            // System.out.println("angle--->" + angle);
                            roatetAngle += angle;
                            mMatrix.postRotate(angle, mContentRect.centerX(), mContentRect.centerY());
                            invalidate();
                        }

                        oldx1 = x1;
                        oldy1 = y1;
                        oldx2 = x2;
                        oldy2 = y2;
                        break;
                    }

                case MotionEvent.ACTION_POINTER_UP:
                    mInController = false;
                    mInMove = false;
                    mInDelete = false;
                    break;
            }
        }
        invalidate();
        return true;
    }

    private void doDeleteSticker() {
        if (mOnStickerTouchListener != null) {
            mOnStickerTouchListener.onTextDelete(this);
        }
    }

    private boolean canStickerMove(float cx, float cy) {
        float px = cx + mPoints[8];
        float py = cy + mPoints[9];
        if (mViewRect.contains(px, py)) {
            return true;
        } else {
            return false;
        }
    }


    private float caculateLength(float x, float y) {
        float ex = x - mid.x;
        float ey = y - mid.y;
        return (float) Math.sqrt(ex * ex + ey * ey);
    }


    private float rotation(MotionEvent event) {
        float originDegree = calculateDegree(mLastPointX, mLastPointY);
        float nowDegree = calculateDegree(event.getX(), event.getY());
        return nowDegree - originDegree;
    }

    private float calculateDegree(float x, float y) {
        double delta_x = x - mid.x;
        double delta_y = y - mid.y;
        double radians = Math.atan2(delta_y, delta_x);
        return (float) Math.toDegrees(radians);
    }

    public interface OnStickerTextTouchListener {
        void onTextCopy(StickerTextView stickerView);

        void onTextDelete(StickerTextView stickerView);

        void onTextMoveToHead(StickerTextView stickerView);

        void onTextClickCurrentText(TextViewItem textViewItem);
    }

    /**
     * 触摸的位置和图片左上角位置的中点
     *
     * @param x
     * @param y
     */
    private void midPointToStartPoint(float x, float y) {
        float[] arrayOfFloat = new float[9];
        mMatrix.getValues(arrayOfFloat);
        float f1 = 0.0f * arrayOfFloat[0] + 0.0f * arrayOfFloat[1] + arrayOfFloat[2];
        float f2 = 0.0f * arrayOfFloat[3] + 0.0f * arrayOfFloat[4] + arrayOfFloat[5];
        float f3 = f1 + x;
        float f4 = f2 + y;
        mid.set(f3 / 2, f4 / 2);
    }

    public void setOnStickerTextTouchListener(OnStickerTextTouchListener listener) {
        mOnStickerTouchListener = listener;
    }
}
