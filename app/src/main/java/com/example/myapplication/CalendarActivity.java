package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * 创建时间: 2021/11/19 14:21 <br>
 * 作者: leo <br>
 * 描述:
 */
public class CalendarActivity extends AppCompatActivity implements View.OnClickListener {

  private EditText mFirstContactStart;
  private EditText mFirstContactEnd;

  private EditText mFinalContactStart;
  private EditText mFinalContactEnd;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.calendar_activity);
    initView();
  }

  private void initView() {
    mFirstContactStart = findViewById(R.id.contact_start);
    mFirstContactStart.setOnClickListener(this);
    mFirstContactEnd = findViewById(R.id.contact_end);
    mFirstContactEnd.setOnClickListener(this);
    mFinalContactStart = findViewById(R.id.final_start);
    mFinalContactStart.setOnClickListener(this);
    mFinalContactEnd = findViewById(R.id.final_end);
    mFinalContactEnd.setOnClickListener(this);
  }

  @Override
  public void onClick(View v) {
    int id = v.getId();
    if (id == R.id.final_end
        || id == R.id.final_start
        || id == R.id.contact_end
        || id == R.id.contact_start) {
      showCalenderPopUpWindow(v);
    }
  }

  private void showCalenderPopUpWindow(View v) {
    PopupWindow calendarWindow = new PopupWindow(this);
    LinearLayout layout = (LinearLayout) View.inflate(this, R.layout.calender_view, null);
    CalendarView calendarView = layout.findViewById(R.id.calender);
    calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
      @Override
      public void onSelectedDayChange(@NonNull CalendarView view, int year, int month,
          int dayOfMonth) {

      }
    });
    calendarWindow.setContentView(layout);
    calendarWindow.showAsDropDown(v);
    calendarWindow.setOutsideTouchable(true);

  }
}
