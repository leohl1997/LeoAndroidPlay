package com.example.myapplication;

import android.content.Context;
import android.util.AttributeSet;
import androidx.recyclerview.widget.GridLayoutManager;

/**
 * 创建时间: 2021/05/24 17:15 <br>
 * 作者: leo <br>
 * 描述: 自适应GridLayoutManager
 */
public class GridAutofitLayoutManager extends GridLayoutManager {

  public GridAutofitLayoutManager(Context context, AttributeSet attrs,
      int defStyleAttr, int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
  }

  public GridAutofitLayoutManager(Context context, int spanCount) {
    super(context, spanCount);
  }

  public GridAutofitLayoutManager(Context context, int spanCount, int orientation,
      boolean reverseLayout) {
    super(context, spanCount, orientation, reverseLayout);
  }


}
