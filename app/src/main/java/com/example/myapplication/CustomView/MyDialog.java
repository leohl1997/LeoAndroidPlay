package com.example.myapplication.CustomView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.example.myapplication.R;
import java.util.List;

/**
 * 创建时间: 2021/03/23 15:40 <br>
 * 作者: leo <br>
 * 描述:
 */
public class MyDialog extends Dialog implements View.OnClickListener{

  private Context context;
  private int layoutResID;
  private int[] listenedItem;

  private ListView mListView;
  private TextView tv_date_dialog;
  private CheckBox cb_date_dialog;

  private RelativeLayout rl_dialog;
  private TextView mBtnCancel;
  private TextView mBtnFinish;

  //private DateAdapter myAdapter;




  public MyDialog(@NonNull Context context) {
    super(context, R.style.MyDialog);
  }

  public MyDialog(@NonNull Context context, int themeResId) {
    super(context, themeResId);
  }

  protected MyDialog(@NonNull Context context, boolean cancelable,
      @Nullable OnCancelListener cancelListener) {
    super(context, cancelable, cancelListener);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.custom_dialog_layout);
    initView();
    initData();
  }



  public void initView(){
    mBtnFinish = findViewById(R.id.finish);
    mBtnCancel = findViewById(R.id.cancel);
    rl_dialog = findViewById(R.id.rl_custom_dialog);
    mListView = findViewById(R.id.lv_custom_dialog);


  }

  private void initData(){
    mBtnCancel.setOnClickListener(this);
    mBtnFinish.setOnClickListener(this);

  }

  @Override
  public void onClick(View v) {


  }
}
