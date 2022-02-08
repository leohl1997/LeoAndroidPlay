package com.example.myapplication.CustomView;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.Region;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import com.example.myapplication.R;

/**
 * 创建时间: 2021/09/02 11:02 <br>
 * 作者: honglei009 <br>
 * 描述: 扇形遮罩进度条
 */
public class ArcProgressView extends View {

  private static final int PI_RADIUS = 180; //PI弧度对应的角度

  private int mProgress;
  private int mStartAngle; //进度条初始角度
  private int mBackGroundColor; //进度条遮招颜色

  private int width;
  private int height;
  private int mMax;
  private PointF mCenter;  //背景图片中心
  private PointF mStart; //起始点角度在圆上对应的横坐标
  private float mRadius; //背景图片的外切圆半径
  private RectF mBackGround; //被裁剪的底层背景图片
  private Path mClipBgPath = new Path(); //整个view的部分 A
  private Path mClipArcPath = new Path(); //要被裁掉的部分 B, 需要绘制的部分是 A-B
  private RectF mEnclosingRectF; //整个view的外切圆的外切矩形
  private Paint mPaint = new Paint();

  public ArcProgressView(Context context) {
    super(context);
  }

  public ArcProgressView(Context context,
      @Nullable AttributeSet attrs) {
    super(context, attrs);
    init(context, attrs);
  }

  public ArcProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init(context, attrs);
  }

  //设置进度，并且按照进度更新UI
  public synchronized void setProgress(int progress) {
    if (progress < 0) {
      throw new IllegalArgumentException("progress not less than 0");
    }
    if (progress > mMax) {
      progress = mMax;
    }
    if (progress <= mMax) {
      this.mProgress = progress;
      postInvalidate();
    }
  }

  private void init(Context context, AttributeSet attrs) {
    TypedArray typedArray =
        context.obtainStyledAttributes(attrs, R.styleable.chatui_ArcProgressView);

    mStartAngle = typedArray.getInt(R.styleable.chatui_ArcProgressView_chatui_startAngle, 0);
    mProgress = typedArray.getInt(R.styleable.chatui_ArcProgressView_chatui_circleProgress, 0);
    mBackGroundColor = typedArray.getColor(R.styleable.chatui_ArcProgressView_chatui_progressColor,
        Color.BLACK);
    mMax = typedArray.getInteger(R.styleable.chatui_ArcProgressView_max, 100);
    typedArray.recycle();

    mPaint.setStyle(Paint.Style.FILL);
    mPaint.setAntiAlias(true);
    mPaint.setColor(mBackGroundColor);
  }

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
  }

  @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
  @Override
  protected void onSizeChanged(int w, int h, int oldw, int oldh) {
    super.onSizeChanged(w, h, oldw, oldh);
    width = w;
    height = h;
    float rw = (width - getPaddingStart() - getPaddingEnd()) / 2f;
    float rh = (height - getPaddingTop() - getPaddingBottom()) / 2f;
    mRadius = (float) Math.sqrt(rw * rw + rh * rh);
    mCenter = new PointF(getPaddingStart() + rw, getPaddingTop() + rh);
    mStart = new PointF((float) (mCenter.x + mRadius * Math.cos(mStartAngle * Math.PI / PI_RADIUS)),
        (float) (mCenter.y + mRadius * Math.sin(mStartAngle * Math.PI / PI_RADIUS)));
    mBackGround = new RectF(getPaddingStart(), getPaddingTop(), width - getPaddingEnd(),
        height - getPaddingBottom());
    mEnclosingRectF = new RectF(mCenter.x - mRadius, mCenter.y - mRadius, mCenter.x + mRadius,
        mCenter.y + mRadius);
    mClipBgPath.reset();
    mClipBgPath.addRect(mBackGround, Path.Direction.CW);
  }

  @Override
  protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);
    canvas.save();
    canvas.clipPath(mClipBgPath);
    canvas.clipPath(getClipPart(360 * mProgress / 100f + mStartAngle), Region.Op.DIFFERENCE);
    canvas.drawRect(mBackGround, mPaint);
    canvas.restore();
  }

  private Path getClipPart(float sweepAngle) {
    mClipArcPath.reset();
    mClipArcPath.moveTo(mCenter.x, mCenter.y);
    mClipArcPath.lineTo(mStart.x, mStart.y);
    mClipArcPath.lineTo((float) (mCenter.x + mRadius * Math.cos(sweepAngle * Math.PI / PI_RADIUS)),
        (float) (mCenter.y + mRadius * Math.sin(sweepAngle * Math.PI / PI_RADIUS)));
    mClipArcPath.close();
    mClipArcPath.addArc(mEnclosingRectF, mStartAngle, sweepAngle - mStartAngle);
    return mClipArcPath;
  }
}
