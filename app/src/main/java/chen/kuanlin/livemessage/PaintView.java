package chen.kuanlin.livemessage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by kuanlin on 2017/9/7.
 */

public class PaintView extends View{

    private int StylePenID = -1;
    private float pre_x;
    private float pre_y;
    private float mCurveEndX;
    private float mCurveEndY;
    private int widthPixels;
    private int heightPixels;
    private static boolean isFirstPenTouch = false;
    private static boolean isMoving = false;

    private Paint paint;
    private Canvas canvas;
    private Bitmap bitmap;
    private Path mPath = new Path();
    private Rect mInvalidRect = new Rect();

    private String TAG = "[PaintView] ";
    private boolean debugmode = true;

    public PaintView(Context context) {
        super(context);
    }

    public PaintView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PaintView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if(debugmode)Log.e(TAG,"Width:"+String.valueOf(widthMeasureSpec)+" Height:"+String.valueOf(heightMeasureSpec));
    }

    protected void onLayout(boolean changed, int left, int top, int right, int bottom){
        initPaintView();
    }

    private void initPaintView() {
        paint = new Paint(Paint.DITHER_FLAG);
        widthPixels = getMeasuredWidth();
        heightPixels = getMeasuredHeight();
        bitmap = Bitmap.createBitmap(getMeasuredWidth(), getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        if(debugmode)Log.e(TAG,"widthPixels:"+String.valueOf(widthPixels)+" heightPixels:"+String.valueOf(heightPixels));
        canvas = new Canvas();
        canvas.setBitmap(bitmap);
        setCanvasBackground(0);

        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);
        paint.setColor(Color.RED);
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setStrokeWidth((float) 3.0);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(bitmap,0,0,null);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int index = event.getActionIndex();
        int ID = event.getPointerId(index);

        if(debugmode){
            Log.e(TAG,"index:" + index );
            Log.e(TAG,"ID:" + ID );
            Log.e(TAG,"Action::" + event.getAction() );
        }

        switch(event.getAction())
        {
            case MotionEvent.ACTION_MOVE:
                if(debugmode) Log.e(TAG,"Action Move 2" + event.getX() + "/" + event.getY());

                if((ID == StylePenID)) {
                    isMoving = true;
                    Rect rect = RectMove(event);
                    if (rect != null) {
                        invalidate(rect);
                    }
                    invalidate();
                }
                break;

            case MotionEvent.ACTION_POINTER_DOWN:
                if(debugmode) Log.e(TAG,"Point Down :" + event.getX() + "/" + event.getY());
                break;

            case MotionEvent.ACTION_DOWN:
                if(debugmode) Log.e(TAG,"Down :" + event.getX() + "/" + event.getY());
                //單點觸控：僅第一個觸控點畫線或點，其餘皆過慮掉
                if( isFirstPenTouch == false) {
                    isFirstPenTouch = true;
                    StylePenID = ID;
                }
                if ( ( isFirstPenTouch == true ) && (ID == StylePenID) ) {
                    pre_x = (int) event.getX();
                    pre_y = (int) event.getY();
                    mPath.moveTo( event.getX(), event.getY());
                    //canvas.drawPoint(mov_x, mov_y, paint);
                }
                invalidate();
                break;

            case MotionEvent.ACTION_POINTER_UP:
                if(debugmode) Log.e(TAG,"Point UP");
                if(ID == StylePenID) {
                    StylePenID = -1;
                    isFirstPenTouch = false;
                    mPath.rewind();
                }
                break;

            case MotionEvent.ACTION_UP:
                if(debugmode) Log.e(TAG,"UP :");
                if(ID == StylePenID) {
                    StylePenID = -1;
                    isFirstPenTouch = false;
                    mPath.rewind();
                }
                break;

            case MotionEvent.ACTION_CANCEL:
                if(debugmode) Log.e(TAG,"Cancel");
                if(ID == StylePenID) {
                    StylePenID = -1;
                    isFirstPenTouch = false;
                }
                break;
        }
        return true;
    }

    private Rect RectMove(MotionEvent event) {
        Rect areaToRefresh = null;

        final float x = event.getX();
        final float y = event.getY();

        final float previousX = pre_x;
        final float previousY = pre_y;

        final float dx = Math.abs(x - previousX);
        final float dy = Math.abs(y - previousY);

        if (dx >= 3 || dy >= 3) {
            areaToRefresh = mInvalidRect;
            areaToRefresh.set((int) mCurveEndX, (int) mCurveEndY,
                    (int) mCurveEndX, (int) mCurveEndY);

            //設定貝賽爾曲線的操作點為起點和終點的一半
            float cX = mCurveEndX = (x + previousX) / 2;
            float cY = mCurveEndY = (y + previousY) / 2;

            //實現繪製貝賽爾平滑曲線；previousX, previousY為操作點，cX, cY為終點
            mPath.quadTo(previousX, previousY, cX, cY);
            //mPath.lineTo(x, y);

            // union with the control point of the new curve
            //areaToRefresh矩形擴大了border(寬和高擴大了兩倍border)，
            //border值由設定手勢畫筆粗細值决定

            areaToRefresh.union((int) previousX, (int) previousY,
                    (int) previousX, (int) previousY);
            /* areaToRefresh.union((int) x, (int) y,
                    (int) x, (int) y);*/


            // union with the end point of the new curve
            areaToRefresh.union((int) cX, (int) cY,
                    (int) cX, (int) cY);

            //第二次執行時，第一次结束調用的座標值將作為第二次調用的初始坐標值
            pre_x = x;
            pre_y = y;
            canvas.drawPath(mPath, paint);
        }
        return areaToRefresh;
    }

    public void setPaintColor(int user_color){
        switch (user_color){
            case 0:
                paint.setColor(Color.RED);
                break;
            case 1:
                paint.setColor(Color.YELLOW);
                break;
            case 2:
                paint.setColor(Color.GREEN);
                break;
            case 3:
                paint.setColor(Color.BLUE);
                break;
            case 4:
                paint.setColor(Color.WHITE);
                break;
            case 5:
                paint.setColor(Color.GRAY);
                break;
            case 6:
                paint.setColor(Color.BLACK);
                break;
        }
    }

    public void setCanvasBackground(int user_background){
        switch (user_background){
            case 0:
                canvas.drawColor(Color.BLACK);
                invalidate();
                break;
            case 1:
                canvas.drawColor(Color.WHITE);
                invalidate();
                break;
        }
    }

    public void setCanvasPicture(Drawable userDrawable){
        userDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        userDrawable.draw(canvas);
    }

    public void showPreview(Bitmap preBitmap){
        canvas.setBitmap(preBitmap);
    }

    public void clearPaint(){
        canvas.drawColor(0,PorterDuff.Mode.CLEAR);
        invalidate();
    }

    public String[] getResolution(){
        String[] resolutionList = new String[4];
        resolutionList[0] = "Origin ("+widthPixels+"x"+heightPixels+")";
        resolutionList[1] = "Half ("+(widthPixels/2)+"x"+(heightPixels/2)+")";
        resolutionList[2] = "One Third ("+(widthPixels/3)+"x"+(heightPixels/3)+")";
        resolutionList[3] = "Quarter ("+(widthPixels/4)+"x"+(heightPixels/4)+")";

        return resolutionList;
    }
}
