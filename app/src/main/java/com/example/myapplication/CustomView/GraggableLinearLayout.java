package com.example.myapplication.CustomView;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowMetrics;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import androidx.annotation.Nullable;

/**
 * 创建时间: 2021/07/29 18:34 <br>
 * 作者: leo <br>
 * 描述: 可拖动的LinearLayout
 */
public class GraggableLinearLayout extends LinearLayout {

  private int lastY;
  private final int screenHeight;
  private final int screenWidth;
  public GraggableLinearLayout(Context context) {
    this(context,null);
  }

  public GraggableLinearLayout(Context context,
      @Nullable AttributeSet attrs) {
    this(context,attrs,0);
  }

  public GraggableLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    DisplayMetrics dm = getResources().getDisplayMetrics();
    screenHeight = dm.heightPixels ;
    screenWidth = dm.widthPixels;
  }

  @Override
  protected void onLayout(boolean changed, int l, int t, int r, int b) {
    super.onLayout(changed, l, t, r, b);
  }


  @Override
  public boolean onTouchEvent(MotionEvent ev) {
    Log.d("onTouchEvent","1");
    switch(ev.getAction()){
      case MotionEvent.ACTION_DOWN:
        lastY = (int)ev.getRawY();
        break;
      case MotionEvent.ACTION_MOVE:
        int dy = (int) (ev.getRawY()) - lastY;

        int top = this.getTop() + dy;
        int bottom = this.getBottom() + dy;

        if(top < 0){
          top = 0;
          bottom = top + this.getHeight();
        }
        if(bottom > screenHeight){
          bottom = screenHeight;
          top = bottom - this.getHeight();
        }
        this.layout(getLeft(),top,getRight(),bottom);
        lastY = (int) ev.getRawY();
        break;
      case MotionEvent.ACTION_UP:
        break;
    }
    return true;
  }
}
