package com.example.myapplication;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.CustomView.ArcProgressView;
import com.example.myapplication.CustomView.MyDivider;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayout;
import com.google.android.flexbox.FlexboxLayoutManager;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

  private static final ThreadLocal<SimpleDateFormat> sYY_MM_ddChineseFormat =
      new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
          return new SimpleDateFormat("yyyy年MM月dd日", Locale.US);
        }
      };
  public List<Date> data = new ArrayList<>();
  private PopupWindow popupWindow;
  private DateAdapter dateAdapter;
  private MyAdapter myAdapter;
  private ListView listView;
  private RecyclerView recyclerView;
  private TextView tv;
  private TextView name;
  private TextView mScrollTv;
  private TextView mJiajiTv;
  private String testString = getTestString();
  private WindowManager sWindowManager;
  private View sFloatView;
  private ArcProgressView mProgressView;
  private int mProgress;
  private final Runnable mAction = new Runnable() {
    @Override
    public void run() {
      int progress = mProgress++ % 100;
      mProgressView.setProgress(progress);
      mProgressView.postDelayed(mAction, 50);
    }
  };
  private int progress = 0;
  private Handler mHandler = new Handler() {
    @SuppressLint("HandlerLeak")
    @Override
    public void handleMessage(@NonNull Message msg) {
      super.handleMessage(msg);
      switch (msg.what) {
        case 1:
          if (mProgress < 100) {
            mProgressView.setProgress(progress);
            progress++;
            mHandler.sendEmptyMessageDelayed(1, 200);
          } else {
            Toast.makeText(MainActivity.this, "上传已完成", Toast.LENGTH_LONG).show();
          }
          break;
        default:
          throw new IllegalStateException("Unexpected value: " + msg.what);
      }
    }
  };

  public static String formatTimeLineInChinese(long milliseconds) {
    java.util.Date date = new java.util.Date(milliseconds);
    return sYY_MM_ddChineseFormat.get().format(date);
  }

  /**
   * 按本周星期 格式格式化时间
   */
  public static String formatTimeLineInWeek(long milliseconds) {
    String[] weekDays = new String[] { "星期天", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
    Calendar calendar = Calendar.getInstance();
    boolean isFirstSunday = (calendar.getFirstDayOfWeek() == Calendar.SUNDAY);
    int day = calendar.get(Calendar.DAY_OF_WEEK);
    if (isFirstSunday) {
      day = day - 1;
      if (day == 0) {
        day = 7;
      }
    }
    return weekDays[day];
  }

  public static boolean isExternalStorageDocument(Uri uri) {
    return "com.android.externalstorage.documents".equals(uri.getAuthority());
  }

  public static String getDataColumn(Context context, Uri uri, String selection,
      String[] selectionArgs) {
    Cursor cursor = null;
    final String column = "_data";
    final String[] projection = { column };
    try {
      cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
          null);
      if (cursor != null && cursor.moveToFirst()) {
        final int column_index = cursor.getColumnIndexOrThrow(column);
        return cursor.getString(column_index);
      }
    } finally {
      if (cursor != null) {
        cursor.close();
      }
    }
    return null;
  }

  @SuppressLint("NewApi")
  public static String getPath(final Context context, final Uri uri) {
    final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
    // DocumentProvider
    if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
      final String docId = DocumentsContract.getDocumentId(uri);
      // ExternalStorageProvider
      if (isExternalStorageDocument(uri)) {
        final String[] split = docId.split(":");
        final String type = split[0];

        if ("primary".equalsIgnoreCase(type)) {
          return Environment.getExternalStorageState() + "/" + split[1];
        }
      }
      // DownloadsProvider
      else if (isDownloadsDocument(uri)) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {//判断有没有超过android 8，区分开来，不然崩溃崩溃崩溃崩溃
          final Uri contentUri = ContentUris.withAppendedId(
              Uri.parse("content://downloads/public_downloads"), Long.parseLong(docId));
          return getDataColumn(context, contentUri, null, null);
        } else {
          final String[] split = docId.split(":");
          if (split.length >= 2) {
            return split[1];
          }
        }
      }
      // MediaProvider
      else if (isMediaDocument(uri)) {
        final String[] split = docId.split(":");
        final String type = split[0];

        Uri contentUri = null;
        if ("image".equals(type)) {
          contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        } else if ("video".equals(type)) {
          contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        } else if ("audio".equals(type)) {
          contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        }

        final String selection = "_id=?";
        final String[] selectionArgs = new String[] { split[1] };

        return getDataColumn(context, contentUri, selection, selectionArgs);
      }
    }
    // MediaStore (and general)
    else if ("content".equalsIgnoreCase(uri.getScheme())) {
      // Return the remote address
                /*if (isGooglePhotosUri(uri)) {
                    return uri.getLastPathSegment();
                } else*/
      //判断华为手机的uri，重点重点重点重点重点重点重点重点重点重点重点重点重点重点重点重点重点重点重点重点重点重点重点重点重点重点重点重点重点重点重点重点重点重点重点重点重点重点重点重点重点重点重点重点重点重点重点重点重点重点重点重点重点重点重点重点重点重点重点重点重点重点重点重点重点重点
      if (isHuaWeiUri(uri)) {
        String uriPath = uri.getPath();
        //content://com.huawei.hidisk.fileprovider/root/storage/emulated/0/Android/data/com.xxx.xxx/
        if (uriPath != null && uriPath.startsWith("/root")) {
          return uriPath.replaceAll("/root", "");
        }
      }
      return getDataColumn(context, uri, null, null);
    }
    // File
    else if ("file".equalsIgnoreCase(uri.getScheme())) {
      return uri.getPath();
    }
    return null;
  }

  public static boolean isDownloadsDocument(Uri uri) {
    return "com.android.providers.downloads.documents".equals(uri.getAuthority());
  }

  public static boolean isHuaWeiUri(Uri uri) {
    return "com.huawei.hidisk.fileprovider".equals(uri.getAuthority());
  }

  public static boolean isMediaDocument(Uri uri) {
    return "com.android.providers.media.documents".equals(uri.getAuthority());
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    int REQUEST_EXTERNAL_STORAGE = 1;
    String[] PERMISSIONS_STORAGE = {
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    int permission = ActivityCompat.checkSelfPermission(MainActivity.this,
        Manifest.permission.WRITE_EXTERNAL_STORAGE);
    int permissionRead = ActivityCompat.checkSelfPermission(MainActivity.this,
        Manifest.permission.READ_EXTERNAL_STORAGE);
    if (permission != PackageManager.PERMISSION_GRANTED
        || permissionRead != PackageManager.PERMISSION_GRANTED) {
      // We don't have permission so prompt the user
      ActivityCompat.requestPermissions(
          MainActivity.this,
          PERMISSIONS_STORAGE,
          REQUEST_EXTERNAL_STORAGE
      );
    }

    super.onCreate(savedInstanceState);

    setContentView(R.layout.activity_main);
    data = initData();
    Button button = findViewById(R.id.btn);
    button.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        showPopUp(view);
      }
    });
    Button button1 = findViewById(R.id.btn2);
    button1.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        //showCustomDialog(getApplicationContext(),);
      }
    });

    Button button2 = findViewById(R.id.btn3);
    button2.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        //Log.d("time now", formatTimeLineInChinese(System.currentTimeMillis()));
        //showPopUp2(v);
        Thread thread = new Thread(new Runnable() {
          @Override
          public void run() {
            mHandler.sendEmptyMessage(1);
          }
        });
        thread.start();
      }
    });

    Button file_selector = findViewById(R.id.btn_file_selector);
    file_selector.setOnClickListener(new View.OnClickListener() {
      @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
      @Override
      public void onClick(View v) {
        //Intent chooseFile;
        //Intent intent;
        //chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
        //chooseFile.setType("file/*");
        //chooseFile.addCategory(Intent.CATEGORY_OPENABLE);
        //chooseFile.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        //intent = Intent.createChooser(chooseFile, "Choose a file");
        //startActivityForResult(intent, 1);
        Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
        chooseFile.setType("*/*");
        //Intent intent = new Intent(Settings.ACTION_INTERNAL_STORAGE_SETTINGS);
        //intent.addCategory(Intent.CATEGORY_OPENABLE);
        chooseFile.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        Intent intent = Intent.createChooser(chooseFile, "title");
        startActivityForResult(intent, 1);
      }
    });

    Button add_float_view_button = findViewById(R.id.btn_add_float_view);
    add_float_view_button.setOnClickListener(v -> {
      if (Build.VERSION.SDK_INT >= 23 && !Settings.canDrawOverlays(getApplicationContext())) {
        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
        intent.setData(Uri.parse("package:" + getBaseContext().getPackageName()));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getBaseContext().startActivity(intent);
        return;
      }
      sFloatView =
          LayoutInflater.from(getApplicationContext()).inflate(R.layout.float_dig_dialog, null);
      ((FrameLayout) getWindow().getDecorView()).addView(sFloatView);
    });

    tv = findViewById(R.id.test_tv);
    tv.setText(testString);

    name = findViewById(R.id.test_name_tv);
    name.setText(testString);

    mScrollTv = findViewById(R.id.scroll_test);
    //mScrollTv.setText(testString);

    mJiajiTv = findViewById(R.id.tv_jiaji);
    mJiajiTv.setText(testString);

    mProgressView = findViewById(R.id.iv_file_progress);
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (resultCode == Activity.RESULT_OK) {
      if (requestCode == 1) {
        Log.d("return success", "111");
        Uri uri = data.getData();
        //ClipData uri = data.getClipData();
        //String path = null;
        //Cursor cursor = this.getContentResolver().query(uri, null, null, null, null);
        //if (cursor == null) {
        //  path = uri.getPath();
        //  return;
        //}
        //if (cursor.moveToFirst()) {
        //  path = cursor.getString(cursor.getColumnIndex("_data"));
        //}
        //cursor.close();
        String path = getPath(this, uri);
        //Log.d("file path", path);
        File file = new File(path);
        Toast.makeText(this, getFileType(file.getName()), Toast.LENGTH_SHORT).show();
      }
    }
  }

  private String getFileType(String fileName) {
    return fileName == null ? null : fileName.substring(fileName.lastIndexOf(".") + 1);
  }

  private void addView(Context context, View view) {
    sWindowManager =
        (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
    final WindowManager.LayoutParams params = new WindowManager.LayoutParams();
    // 类型
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      params.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
    } else {
      params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
    }
    //设置悬浮窗外可点击
    params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
    // 不设置这个弹出框的透明遮罩显示为黑色
    params.format = PixelFormat.TRANSLUCENT;
    params.width = WindowManager.LayoutParams.WRAP_CONTENT;
    params.height = WindowManager.LayoutParams.WRAP_CONTENT;
    params.gravity = Gravity.TOP;
    sWindowManager.addView(view, params);
  }

  private String getTestString() {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < 5; i++) {
      sb.append("我是洪磊");
    }
    //sb.append("小红");
    //sb.append("shdasdakd  熟空间很大了对话拉升空间很大 sdasdaSDSAJLD到 asdjkasd          as");
    return sb.toString();
  }

  private boolean isItemChecked(SparseBooleanArray sparseBooleanArray, int position) {
    return sparseBooleanArray.get(position);
  }

  private void setItemChecked(SparseBooleanArray sparseBooleanArray, int position,
      boolean isChecked) {
    sparseBooleanArray.put(position, isChecked);
  }

  private void showCustomDialog(Context context, final TextView tv, final String[] args) {
    //AlertDialog.Builder builder = new AlertDialog.Builder()
  }

  @SuppressLint("ResourceAsColor")
  private void showPopUp(View view) {
    View contentView = LayoutInflater.from(MainActivity.this).inflate(R.layout.popup_layout, null);

    //设置rv的样式
    recyclerView = contentView.findViewById(R.id.menu_layout);
    FlexboxLayoutManager flexboxLayoutManager = new FlexboxLayoutManager(this);
    flexboxLayoutManager.setFlexWrap(FlexWrap.WRAP);
    flexboxLayoutManager.setFlexDirection(FlexDirection.ROW);

    //RecyclerView.ItemDecoration vertical =
    //    new DividerItemDecoration(this, RecyclerView.VERTICAL);
    //
    recyclerView.addItemDecoration(new MyDivider(this, R.color.purple_200, 5, 20));
    //recyclerView.addItemDecoration(vertical);

    recyclerView.setLayoutManager(flexboxLayoutManager);

    //设置rv适配器
    myAdapter = new MyAdapter(data);
    recyclerView.setAdapter(myAdapter);

    popupWindow = new PopupWindow(contentView, LinearLayout.LayoutParams.WRAP_CONTENT,
        LinearLayout.LayoutParams.WRAP_CONTENT, true);
    //popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
    //    @Override
    //    public void onDismiss() {
    //        backGroundAlpha(1.0f);
    //    }
    //});
    popupWindow.setOutsideTouchable(true);
    popupWindow.setBackgroundDrawable(new BitmapDrawable());
    popupWindow.setFocusable(true);
    int popupWidth = view.getMeasuredWidth();
    int popupHeight = view.getMeasuredHeight();
    int[] location = new int[2];
    view.getLocationOnScreen(location);
    popupWindow.showAtLocation(view, Gravity.NO_GRAVITY,
        (location[0] + view.getWidth() / 2) - popupWidth / 2, location[1] + popupHeight);
  }

  private void showPopUp2(View view) {
    View contentView =
        LayoutInflater.from(MainActivity.this).inflate(R.layout.popup_layout_second, null);

    FlexboxLayout flexLayout = contentView.findViewById(R.id.flex_layout);
    final View arrow = contentView.findViewById(R.id.menu_arrow);
    arrow.setVisibility(View.GONE);

    for (int i = 0; i < data.size(); i++) {
      final Date menuItem = data.get(i);
      if (menuItem == null) break;
      TextView textView = (TextView) LayoutInflater.from(this)
          .inflate(R.layout.menu_text, flexLayout, false);
      flexLayout.addView(textView);
      View dividerView = LayoutInflater.from(this)
          .inflate(R.layout.menu_divider, flexLayout, false);
      flexLayout.addView(dividerView);
      textView.setText(menuItem.getDate());
      textView.setClickable(true);
      textView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
        }
      });
    }

    contentView.getViewTreeObserver().addOnGlobalLayoutListener(
        new ViewTreeObserver.OnGlobalLayoutListener() {
          @Override
          public void onGlobalLayout() {
            arrow.setVisibility(View.VISIBLE);
            Log.d("save", "12");
            int pos[] = new int[2];
            contentView.getLocationOnScreen(pos);
            int leftPos = pos[0];
            view.getLocationOnScreen(pos);
            int anchorLeftPos = pos[0];
            int arrowLeftMargin = 0;
            if (anchorLeftPos < leftPos) {
              arrowLeftMargin =
                  anchorLeftPos - leftPos + view.getWidth() / 2 - arrow.getWidth() / 2;
            } else {
              arrowLeftMargin = contentView.getMeasuredWidth() / 2 - arrow.getWidth();
            }

            LinearLayout.LayoutParams arrowParams =
                (LinearLayout.LayoutParams) arrow.getLayoutParams();
            arrowParams.leftMargin = arrowLeftMargin;
            Log.e("arrowHeight", String.valueOf(arrow.getHeight()));
            contentView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
          }
        });

    int last = flexLayout.getFlexLines().size();

    int tmp = flexLayout.getSumOfCrossSize();

    popupWindow = new PopupWindow(contentView, FlexboxLayout.LayoutParams.WRAP_CONTENT,
        FlexboxLayout.LayoutParams.WRAP_CONTENT, true);
    popupWindow.setOutsideTouchable(true);
    popupWindow.setBackgroundDrawable(new BitmapDrawable());
    popupWindow.setFocusable(true);
    int type = View.MeasureSpec.makeMeasureSpec(1000, View.MeasureSpec.AT_MOST);
    contentView.measure(type, type);
    int popupWidth = contentView.getMeasuredWidth();
    int popupHeight = contentView.getMeasuredHeight();
    arrow.measure(type, type);
    int arrowHeight = arrow.getMeasuredHeight();
    int arrowWidth = arrow.getMeasuredWidth();
    int[] location = new int[2];
    view.getLocationOnScreen(location);
    popupWindow.showAtLocation(view, Gravity.NO_GRAVITY,
        (location[0] + view.getWidth() / 2) - popupWidth / 2 - arrowWidth / 2,
        location[1] - popupHeight - arrowHeight / 2);
  }

  private List<Date> initData() {
    List<Date> data = new ArrayList<>();
    data.add(new Date("每周", false));
    //data.add(new Date("每周", false));
    //data.add(new Date("每周", false));
    //data.add(new Date("添加表情包", false));
    //data.add(new Date("添加表情包", false));
    //data.add(new Date("每周", false));
    //data.add(new Date("每周", false));
    return data;
  }

  private void backGroundAlpha(float f) {
    WindowManager.LayoutParams layoutParams = MainActivity.this.getWindow().getAttributes();
    layoutParams.alpha = f;
    MainActivity.this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
    MainActivity.this.getWindow().setAttributes(layoutParams);
  }

  //private void initview(){
  //    setContentView(R.layout.activity_main);
  //}

  public static class DateAdapter extends ArrayAdapter<Date> {
    private final int id;

    public DateAdapter(@NonNull Context context, int resource, List<Date> list) {
      super(context, resource, list);
      id = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
      Date date = getItem(position);
      @SuppressLint("ViewHolder") View view =
          LayoutInflater.from(getContext()).inflate(id, parent, false);
      TextView textView = view.findViewById(R.id.text);
      textView.setText(date.getDate());
      return view;
    }
  }

  public static class MyAdapter extends RecyclerView.Adapter<MyAdapter.DateViewHolder> {

    private List<Date> mlist;
    //SparseBooleanArray mSelectedPositions = new SparseBooleanArray();

    public MyAdapter(List<Date> list) {
      mlist = list;
    }

    @NonNull
    @Override
    public DateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      View v = LayoutInflater.from(parent.getContext())
          .inflate(R.layout.custom_list_item, parent, false);
      return new DateViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull DateViewHolder holder, int position) {
      holder.tv_date.setText(mlist.get(position).getDate());
      holder.lv_test.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          //if(isItemChecked(position)){
          //    setItemChecked(position,false);
          //}else{
          //    setItemChecked(position,true);
          //}
        }
      });
    }

    @Override
    public int getItemCount() {
      return mlist.size();
    }

    //private boolean isItemChecked(int position){
    //    return mSelectedPositions.get(position);
    //}
    //private void setItemChecked(int position,boolean isChecked){
    //    mSelectedPositions.put(position,isChecked);
    //}

    static class DateViewHolder extends RecyclerView.ViewHolder {

      private TextView tv_date;
      private LinearLayout lv_test;

      public DateViewHolder(@NonNull View itemView) {
        super(itemView);
        this.tv_date = itemView.findViewById(R.id.text);
        this.lv_test = itemView.findViewById(R.id.lv_test);
        //ViewGroup.LayoutParams lp = itemView.getLayoutParams();
        //if(lp instanceof FlexboxLayoutManager.LayoutParams){
        //    FlexboxLayoutManager.LayoutParams flexLp = (FlexboxLayoutManager.LayoutParams) lp;
        //    flexLp.setFlexGrow(1.0f);
        //}
      }
    }
  }
}