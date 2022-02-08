package com.example.myapplication;

import android.widget.Checkable;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 创建时间: 2021/03/22 16:26 <br>
 * 作者: leo <br>
 * 描述:
 */
public class Date  implements Checkable {
  private String date;
  private boolean isChecked;

  public Date(String date,boolean isChecked){
    this.date = date;
    this.isChecked = isChecked;
  }

  public String getDate(){
    return date;
  }

  public Boolean getStatus(){
    return isChecked;
  }

  @Override
  public void setChecked(boolean checked) {

  }

  @Override
  public boolean isChecked() {
    return false;
  }

  @Override
  public void toggle() {

  }
}
