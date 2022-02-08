package com.example.myapplication.CustomView;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;
import androidx.annotation.Nullable;

/**
 * 创建时间: 2021/04/14 18:37 <br>
 * 作者: leo <br>
 * 描述:
 */
public class MarqueeText extends androidx.appcompat.widget.AppCompatTextView {

  public MarqueeText(Context context) {
    super(context);
  }

  public MarqueeText(Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
  }

  public MarqueeText(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  @Override
  public boolean isFocused(){
    return true;
  }
}
