package chen.kuanlin.livemessage;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private Button button_start;
    private Button button_stop;
    private Button button_clear;
    private PaintView paintView;
    private Recorder recorder;
    private Thread thread;
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 100;
    private boolean debugmode = true;
    private final String TAG = "[MainActivity] ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewgroup);
        button_start = (Button)findViewById(R.id.button_start);
        button_stop = (Button)findViewById(R.id.button_stop);
        button_clear = (Button)findViewById(R.id.button_clear);
        paintView = (PaintView)findViewById(R.id.paintview);

        checkPermission();

        button_start.setOnClickListener(new ViewGroup.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(debugmode)Log.e(TAG, "button_start");
                recorder = new Recorder(getApplicationContext(), paintView);
                thread = new Thread(recorder);
                thread.start();
            }
        });

        button_stop.setOnClickListener(new ViewGroup.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(debugmode)Log.e(TAG, "button_stop");
                recorder.terminate();
                if(!(thread.isInterrupted())){
                    thread.interrupt();
                }
                new StoreDataAsyncTask().execute();
            }
        });
    }

    public class StoreDataAsyncTask extends AsyncTask<String, Integer, Integer>
    {
        private ProgressDialog myDialog;

        @Override
        protected void onPreExecute() {
            //在背景執行之前要做的事，寫在這裡
            //初始化進度條
            myDialog = new ProgressDialog(MainActivity.this);
            myDialog.setMessage("Saving Data");
            myDialog.setCancelable(false);
            //myDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            myDialog.show();
            super.onPreExecute();
        }
        @Override
        protected Integer doInBackground(String... param) {
            //一定必須覆寫的方法
            //背景執行的內容放此
            //這裡不能和UI有任何互動
            recorder.storeGIF();
            return 1;
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            //此方法會取得一個數值，可以用來計算目前執行進度
            //通常用來改變進度列(Progressbar)
            //myDialog.setProgress(progress[0]);
            super.onProgressUpdate(progress);
        }

        @Override
        protected void onPostExecute(Integer result) {
            //doInBackground執行完後就會執行此方法
            //通常用來傳資料給UI顯示
            super.onPostExecute(result);
            if(result.equals(1)){
                myDialog.dismiss();
            }
        }
    }

    private void checkPermission(){
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                new AlertDialog.Builder(MainActivity.this)
                        .setMessage("我真的沒有要做壞事, 給我權限吧?")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(MainActivity.this,
                                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        })
                        .show();
            } else {

                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);
            }
        }
    }
}
