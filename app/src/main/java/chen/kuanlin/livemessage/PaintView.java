package chen.kuanlin.livemessage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.icu.text.SimpleDateFormat;
import android.os.Environment;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by kuanlin on 2017/9/7.
 */

public class PaintView extends View {

    private float mov_x;
    private float mov_y;
    private static boolean isFirstPenTouch = false;
    private static boolean isMoving = false;
    private int StylePenID = -1;
    private Paint paint;
    private Canvas canvas;
    private Bitmap bitmap;
    private Path mPath = new Path();
    private boolean debugmode = false;
    private final String TAG = "[PaintView] ";

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
    }

    protected void onLayout(boolean changed, int left, int top, int right, int bottom){
        initPaintView();
    }

    private void initPaintView() {
        paint = new Paint(Paint.DITHER_FLAG);
        bitmap = Bitmap.createBitmap(getMeasuredWidth(), getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Log.e(TAG,"Width:"+String.valueOf(getMeasuredWidth())+" Height:"+String.valueOf(getMeasuredHeight()));
        canvas = new Canvas();
        canvas.setBitmap(bitmap);

        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);
        paint.setColor(Color.BLACK);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(bitmap,0,0,null);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //float paintsize = event.getSize();
        int index = event.getActionIndex();
        int ID = event.getPointerId(index);

        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.FILL);

        if(debugmode){
            Log.e(TAG,"index:" + index );
            Log.e(TAG,"ID:" + ID );
            Log.e(TAG,"Action::" + event.getAction() );
        }

        switch(event.getAction())
        {
            case MotionEvent.ACTION_MOVE:
                if(debugmode) Log.e(TAG,"Action Move 2" + event.getX() + "/" + event.getY());
                paint.setColor(Color.RED);
                paint.setAntiAlias(true);
                paint.setDither(true);
                paint.setStrokeWidth((float) 3.0);
                paint.setStyle(Paint.Style.STROKE);

                if((ID == StylePenID)) {
                    isMoving = true;
                    float x = event.getX();
                    float y = event.getY();
                    float cX = (x + mov_x) / 2;
                    float cY = (y + mov_y) / 2;
                    mPath.quadTo(cX,cY,x,y);
                    canvas.drawPath(mPath, paint);
                    mov_x = x;
                    mov_y = y;
                }
                invalidate();
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
                    mov_x = (int) event.getX();
                    mov_y = (int) event.getY();
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
}
