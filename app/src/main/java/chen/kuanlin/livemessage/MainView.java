package chen.kuanlin.livemessage;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by kuanlin on 2017/9/7.
 */

public class MainView extends ViewGroup {

    public MainView(Context context) {
        super(context);
    }

    public MainView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MainView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        measureChildren(widthMeasureSpec, heightMeasureSpec);
    }

    protected void onLayout(boolean changed, int left, int top, int right, int bottom){
        int mViewGroupWidth  = getMeasuredWidth();  //ViewGroup的總寬度
        int mViewGroupHeight = getMeasuredHeight(); //ViewGroup的總高度

        int mPainterPosX = left; //横坐標位置
        int mPainterPosY = top;  //縱坐標位置

        int childCount = getChildCount();
        for ( int i = 0; i < childCount; i++ ) {

            View childView = getChildAt(i);

            int width  = childView.getMeasuredWidth();
            int height = childView.getMeasuredHeight();

            MainView.LayoutParams margins = (MainView.LayoutParams)(childView.getLayoutParams());

            //ChildView占用的width  = width+leftMargin+rightMargin
            //ChildView占用的height = height+topMargin+bottomMargin
            //如果剩餘的空間不夠，則移到下一行開始位置
            if( mPainterPosX + width + margins.leftMargin + margins.rightMargin > mViewGroupWidth ) {
                mPainterPosX = left;
                mPainterPosY += height + margins.topMargin + margins.bottomMargin;
            }

            //執行ChildView的繪製
            childView.layout(mPainterPosX+margins.leftMargin, mPainterPosY+margins.topMargin,mPainterPosX+margins.leftMargin+width, mPainterPosY+margins.topMargin+height);

            mPainterPosX += width + margins.leftMargin + margins.rightMargin;
        }
    }

    public static class LayoutParams extends ViewGroup.MarginLayoutParams {
        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
        }
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MainView.LayoutParams(getContext(), attrs);
    }
}
