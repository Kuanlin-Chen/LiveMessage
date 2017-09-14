package chen.kuanlin.livemessage;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button button_record, button_save, button_clear,
                   button_resolution, button_color, button_background;

    private PaintView paintView;
    private Recorder recorder;
    private Thread thread;

    private int user_rate = 4;
    private int user_color = 0;
    private int user_background = 0;
    private static boolean isRecording = false;
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 100;
    private boolean debugmode = true;
    private final String TAG = "[MainActivity] ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewgroup);
        paintView = (PaintView)findViewById(R.id.paintview);
        button_record = (Button)findViewById(R.id.button_record);
        button_save = (Button)findViewById(R.id.button_save);
        button_clear = (Button)findViewById(R.id.button_clear);
        button_resolution = (Button)findViewById(R.id.button_resolution);
        button_color = (Button)findViewById(R.id.button_color);
        button_background = (Button)findViewById(R.id.button_background);

        checkPermission();

        button_record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isRecording){
                    if(debugmode)Log.e(TAG, "start");
                    isRecording = true;
                    recorder = new Recorder(getApplicationContext(), paintView);
                    recorder.setRate(user_rate);
                    thread = new Thread(recorder);
                    thread.start();
                    button_record.setText("PAUSE");
                }else {
                    if(debugmode)Log.e(TAG, "pause");
                    recorder.terminate();
                    if(!(thread.isInterrupted())){
                        thread.interrupt();
                    }
                    isRecording = false;
                    button_record.setText("START");
                }
            }
        });

        button_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(debugmode)Log.e(TAG, "button_save");
                /*if(isRecording){
                    recorder.terminate();
                    if(!(thread.isInterrupted())){
                        thread.interrupt();
                    }
                    new StoreDataAsyncTask().execute();
                    isRecording = false;
                }else {
                    if(debugmode)Log.e(TAG,"is not Recording");
                }*/
            }
        });

        button_clear.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(debugmode)Log.e(TAG, "button_clear");
                paintView.clearPaint();
                paintView.setCanvasBackground(user_background);
            }
        });

        button_resolution.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(debugmode)Log.e(TAG, "button_resolution");
                AlertDialog.Builder select_rate = new AlertDialog.Builder(MainActivity.this).
                        setSingleChoiceItems(paintView.getResolution(), user_rate-1,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        switch (which){
                                            case 0:
                                                user_rate = 1;
                                                break;
                                            case 1:
                                                user_rate = 2;
                                                break;
                                            case 2:
                                                user_rate = 3;
                                                break;
                                            case 3:
                                                user_rate = 4;
                                                break;
                                        }
                                    }
                                });
                select_rate.show();
            }
        });

        button_color.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(debugmode)Log.e(TAG, "button_color");
                final String[] colorList = new String[]{"RED", "YELLOW", "GREEN", "BLUE", "WHITE", "GRAY", "BLACK"};
                AlertDialog.Builder select_color = new AlertDialog.Builder(MainActivity.this).
                        setSingleChoiceItems(colorList, user_color,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        switch (which){
                                            case 0:
                                                user_color = which;
                                                paintView.setPaintColor(user_color);
                                                break;
                                            case 1:
                                                user_color = which;
                                                paintView.setPaintColor(user_color);
                                                break;
                                            case 2:
                                                user_color = which;
                                                paintView.setPaintColor(user_color);
                                                break;
                                            case 3:
                                                user_color = which;
                                                paintView.setPaintColor(user_color);
                                                break;
                                            case 4:
                                                user_color = which;
                                                paintView.setPaintColor(user_color);
                                                break;
                                            case 5:
                                                user_color = which;
                                                paintView.setPaintColor(user_color);
                                                break;
                                            case 6:
                                                user_color = which;
                                                paintView.setPaintColor(user_color);
                                                break;
                                        }
                                    }
                                });
                select_color.show();
            }
        });

        button_background.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(debugmode)Log.e(TAG, "button_background");
                AlertDialog.Builder select_background = new AlertDialog.Builder(MainActivity.this).
                        setSingleChoiceItems(new String[]{"WHITE","BLACK","TRANSPARENT"}, user_background,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        switch (which){
                                            case 0:
                                                user_background = which;
                                                paintView.setCanvasBackground(user_background);
                                                break;
                                            case 1:
                                                user_background = which;
                                                paintView.setCanvasBackground(user_background);
                                                break;
                                            case 2:
                                                user_background = which;
                                                paintView.setCanvasBackground(user_background);
                                                break;
                                        }
                                    }
                                });
                select_background.show();
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
                        .setMessage("Give Me Permission, Please!")
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
