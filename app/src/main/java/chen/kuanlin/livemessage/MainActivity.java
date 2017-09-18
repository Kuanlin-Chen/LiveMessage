package chen.kuanlin.livemessage;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private ImageButton button_record, button_save, button_clear,
                        button_resolution, button_color, button_background;

    private PaintView paintView;
    private Recorder recorder;
    private Thread thread;

    private int user_rate = 4;
    private int user_color = 0;
    private int user_background = 0;
    private static boolean isRecording = false;
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 100;
    private static long exitTime = 0;
    private boolean debugmode = true;
    private final String TAG = "[MainActivity] ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewgroup);
        paintView = (PaintView)findViewById(R.id.paintview);
        button_record = (ImageButton)findViewById(R.id.button_record);
        button_save = (ImageButton)findViewById(R.id.button_save);
        button_clear = (ImageButton)findViewById(R.id.button_clear);
        button_resolution = (ImageButton)findViewById(R.id.button_resolution);
        button_color = (ImageButton)findViewById(R.id.button_color);
        button_background = (ImageButton)findViewById(R.id.button_background);

        checkPermission();

        button_record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isRecording){
                    startRecord();
                }else {
                    pauseRecord();
                }
            }
        });

        button_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(debugmode)Log.e(TAG, "button_save");
                if( (!isRecording) && (recorder!=null) ){
                    SaveData saveData = new SaveData(MainActivity.this,recorder);
                    saveData.execute();
                }else {
                    if(debugmode){
                        if(isRecording){
                            Toast.makeText(MainActivity.this,"Cannot Save While Recording", Toast.LENGTH_SHORT).show();
                        } else if(recorder==null){
                            Toast.makeText(MainActivity.this,"You Did Not Draw Anything", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });

        button_clear.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(debugmode)Log.e(TAG, "button_clear");
                if(isRecording){
                    pauseRecord();
                }
                recorder = null; //There is no reference to the object, it will be deleted by the GC
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
                        setSingleChoiceItems(new String[]{"BLACK","WHITE"}, user_background,
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
                                        }
                                    }
                                });
                select_background.show();
            }
        });
    }

    private void startRecord(){
        if(debugmode)Log.e(TAG, "start record");
        if(recorder!=null){
            //Clear canvas before recording
            recorder = null;
            paintView.clearPaint();
            paintView.setCanvasBackground(user_background);
        }
        isRecording = true;
        recorder = new Recorder(getApplicationContext(), paintView);
        recorder.setRate(user_rate);
        thread = new Thread(recorder);
        thread.start();
        button_record.setImageResource(R.drawable.ic_media_pause);
    }

    private void pauseRecord(){
        if(debugmode)Log.e(TAG, "pause record");
        recorder.terminate();
        if(!(thread.isInterrupted())){
            thread.interrupt();
        }
        isRecording = false;
        button_record.setImageResource(R.drawable.ic_media_play);
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if((System.currentTimeMillis()-exitTime) > 2000) {
                Toast.makeText(getApplicationContext(), "Click Back Button Twice to Exit",
                        Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
