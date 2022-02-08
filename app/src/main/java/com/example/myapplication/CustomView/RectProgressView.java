package com.example.myapplication.CustomView;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import androidx.annotation.Nullable;

/**
 * 创建时间: 2021/08/26 11:42 <br>
 * 作者: leo <br>
 * 描述:
 */
public class RectProgressView extends View {
  public RectProgressView(Context context) {
    super(context);
  }

  public RectProgressView(Context context,
      @Nullable AttributeSet attrs) {
    super(context, attrs);
  }

  public RectProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  @Override
  protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);
  }
}
