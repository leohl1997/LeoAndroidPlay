package com.example.myapplication.CustomView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.View;
import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayout;
import com.google.android.flexbox.FlexboxLayoutManager;

/**
 * 创建时间: 2021/05/20 16:20 <br>
 * 作者: leo <br>
 * 描述: GridLayout自定义分割线
 */
public class MyDivider extends RecyclerView.ItemDecoration {

  private static final int[] ATTRS = new int[]{
      android.R.attr.listDivider
  };

  private Paint mPaint;

  private int offset;

  private int dividerWidth;

  ///**
  // * 用于绘制间隔样式
  // */
  //private Drawable mDivider;

  //public MyDivider(Context context) {
  //  // 获取默认主题的属性
  //  final TypedArray a = context.obtainStyledAttributes(ATTRS);
  //  mDivider = a.getDrawable(0);
  //  a.recycle();
  //}

  public MyDivider(Context context, @ColorInt int color,int width,int offset){
    this.offset = offset;
    this.dividerWidth = width;
    mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    mPaint.setColor(color);
    mPaint.setStyle(Paint.Style.FILL);
    mPaint.setStrokeWidth(width);
  }


  @SuppressLint("WrongConstant")
  @Override
  public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
    // 绘制间隔，每一个item，绘制右边和下方间隔样式
    int childCount = parent.getChildCount();
    int spanCount = getSpanCount(parent);
    int orientation = ((FlexboxLayoutManager)parent.getLayoutManager()).getFlexDirection();

    int maxLines = ((FlexboxLayoutManager) parent.getLayoutManager()).getFlexItemCount();

    boolean isDrawHorizontalDivider = true;
    boolean isDrawVerticalDivider = true;
    int extra = childCount % spanCount;
    extra = extra == 0 ? spanCount : extra;
    for(int i = 0; i < childCount; i++) {
      isDrawVerticalDivider = true;
      isDrawHorizontalDivider = true;
      //// 如果是竖直方向，最右边一列不绘制竖直方向的间隔
      //if(orientation == FlexDirection.COLUMN && (i + 1) % spanCount == 0) {
      //  isDrawVerticalDivider = false;
      //}
      //// 如果是竖直方向，最后一行不绘制水平方向间隔
      //if(orientation == FlexDirection.COLUMN && i >= childCount - extra) {
      //  isDrawHorizontalDivider = false;
      //}
      //
      //// 如果是水平方向，最下面一行不绘制水平方向的间隔
      //if(orientation == FlexDirection.ROW && (i + 1) % spanCount == 0) {
      //  isDrawHorizontalDivider = false;
      //}
      //
      //// 如果是水平方向，最后一列不绘制竖直方向间隔
      //if(orientation == FlexDirection.ROW && i >= childCount - extra) {
      //  isDrawVerticalDivider = false;
      //}

      if(isDrawHorizontalDivider) {
        drawHorizontalDivider(c, parent, i);
      }

      if(isDrawVerticalDivider) {
        drawVerticalDivider(c, parent, i, offset);
      }
    }
  }

  @Override
  public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
    //int childCount = parent.getLayoutManager().getChildCount();
    //int orientation = ((FlexboxLayoutManager)parent.getLayoutManager()).getFlexDirection();
    //int position = parent.getChildLayoutPosition(view);
    //int right = ((FlexboxLayoutManager) parent.getLayoutManager()).getSumOfCrossSize();
    //if(orientation == FlexDirection.COLUMN && (position + 1) % spanCount == 0) {
    //  outRect.set(0, 0, 0, 5);
    //  return;
    //}
    //
    //if(orientation == FlexDirection.ROW && (position + 1) % spanCount == 0) {
    //  outRect.set(0, 0, 5, 0);
    //  return;
    //}
    //
    //outRect.set(0, 0, 5,5);
    super.getItemOffsets(outRect, view, parent, state);

    //if (parent.getChildAdapterPosition(view) != 0) {
    //  //直接设置为1px
    //  outRect.set(0, 0, dividerWidth,dividerWidth);
    //}
    outRect.set(0, 0, dividerWidth,0);
  }

  /**
   * 绘制竖直间隔线
   *
   * @param canvas
   * @param parent
   *              父布局，RecyclerView
   * @param position
   *              irem在父布局中所在的位置
   */
  private void drawVerticalDivider(Canvas canvas, RecyclerView parent, int position,int offset) {
    final View child = parent.getChildAt(position);
    final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
        .getLayoutParams();
    final int top = child.getTop() + offset;
    final int bottom = child.getBottom() - offset;
    final int left = child.getRight() + params.rightMargin;
    final int right = left + dividerWidth;
    canvas.drawRect(left,top,right,bottom,mPaint);
  }

  /**
   * 绘制水平间隔线
   *
   * @param canvas
   * @param parent
   *              父布局，RecyclerView
   * @param position
   *              item在父布局中所在的位置
   */
  private void drawHorizontalDivider(Canvas canvas, RecyclerView parent, int position) {
    final View child = parent.getChildAt(position);
    final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
        .getLayoutParams();
    final int top = child.getBottom() + params.bottomMargin;
    final int bottom = top + dividerWidth;
    final int left = child.getLeft() - params.leftMargin;
    final int right = child.getRight() + params.rightMargin + child.getWidth();
    canvas.drawRect(left,top,right,bottom,mPaint);
  }

  private int getSpanCount(RecyclerView parent) {
    // 列数
    int spanCount = -1;
    RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
    if (layoutManager instanceof GridLayoutManager) {

      spanCount = ((GridLayoutManager) layoutManager).getSpanCount();
    } else if (layoutManager instanceof StaggeredGridLayoutManager) {
      spanCount = ((StaggeredGridLayoutManager) layoutManager)
          .getSpanCount();
    }
    return spanCount;
  }

  //private boolean isfirstRow(RecyclerView parent, int pos, int spanCount,
  //    int childCount) {
  //  RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
  //  if (layoutManager instanceof GridLayoutManager) {
  //    // childCount = childCount - childCount % spanCount;
  //    int lines = childCount % spanCount == 0 ? childCount / spanCount : childCount / spanCount + 1;
  //    //如是第一行则返回true
  //    return (pos / spanCount + 1) == 1;
  //  } else if (layoutManager instanceof StaggeredGridLayoutManager) {
  //    int orientation = ((StaggeredGridLayoutManager) layoutManager)
  //        .getOrientation();
  //    // StaggeredGridLayoutManager 且纵向滚动
  //    if (orientation == StaggeredGridLayoutManager.VERTICAL) {
  //      childCount = childCount - childCount % spanCount;
  //      // 如果是最后一行，则不需要绘制底部
  //      return pos >= childCount;
  //    } else {
  //      // 如果是最后一行，则不需要绘制底部
  //      return (pos + 1) % spanCount == 0;
  //    }
  //  }
  //  return false;
  //}
  //
  //private boolean isLastRow(RecyclerView parent, int pos, int spanCount,
  //    int childCount) {
  //  RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
  //  if (layoutManager instanceof GridLayoutManager) {
  //    // childCount = childCount - childCount % spanCount;
  //    int lines = childCount % spanCount == 0 ? childCount / spanCount : childCount / spanCount + 1;
  //    return lines == pos / spanCount + 1;
  //  } else if (layoutManager instanceof StaggeredGridLayoutManager) {
  //    int orientation = ((StaggeredGridLayoutManager) layoutManager)
  //        .getOrientation();
  //    // StaggeredGridLayoutManager 且纵向滚动
  //    if (orientation == StaggeredGridLayoutManager.VERTICAL) {
  //      childCount = childCount - childCount % spanCount;
  //      // 如果是最后一行，则不需要绘制底部
  //      return pos >= childCount;
  //    } else {
  //      // 如果是最后一行，则不需要绘制底部
  //      return (pos + 1) % spanCount == 0;
  //    }
  //  }
  //  return false;
  //}
  //
  //private boolean isLastColumn(RecyclerView parent, int pos, int spanCount,
  //    int childCount) {
  //  RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
  //  if (layoutManager instanceof GridLayoutManager) {
  //    // 如果是最后一列，则不需要绘制右边
  //    return (pos + 1) % spanCount == 0;
  //  } else if (layoutManager instanceof StaggeredGridLayoutManager) {
  //    int orientation = ((StaggeredGridLayoutManager) layoutManager)
  //        .getOrientation();
  //    if (orientation == StaggeredGridLayoutManager.VERTICAL) {
  //      // 如果是最后一列，则不需要绘制右边
  //      return (pos + 1) % spanCount == 0;
  //    } else {
  //      childCount = childCount - childCount % spanCount;
  //      // 如果是最后一列，则不需要绘制右边
  //      return pos >= childCount;
  //    }
  //  }
  //  return false;
  //}



}