package com.example.myapplication.CustomView;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import androidx.annotation.RequiresApi;

/**
 * 创建时间: 2022/01/26 17:56 <br>
 * 作者: leo <br>
 * 描述: 自定义气泡布局
 */
public class BubbleRelativeLayout extends RelativeLayout {
  public static int PADDING = 30;
  public static int LEG_HALF_BASE = 30;
  public static float STROKE_WIDTH = 2.0f;
  public static float CORNER_RADIUS = 8.0f;
  public static int SHADOW_COLOR = Color.argb(100, 0, 0, 0);
  public static float MIN_LEG_DISTANCE = PADDING + LEG_HALF_BASE;
  public BubbleRelativeLayout(Context context) {
    super(context);
  }
  public BubbleRelativeLayout(Context context, AttributeSet attrs) {
    super(context, attrs);
  }
  public BubbleRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init(context, attrs);
  }

  private void init(final Context context, final AttributeSet attrs) {

    ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.MATCH_PARENT);
    setLayoutParams(params);
  }

  /**
   * 气泡尖角方向
   */
  public enum BubbleLegOrientation {
    TOP, LEFT, RIGHT, BOTTOM, NONE
  }
}
