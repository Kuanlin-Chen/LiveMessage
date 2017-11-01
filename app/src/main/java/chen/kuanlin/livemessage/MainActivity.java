package chen.kuanlin.livemessage;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Locale;

/**
 * Created by kuanlin on 2017/9/7.
 */

public class MainActivity extends AppCompatActivity {

    protected ImageButton button_record, button_save, button_share, button_clear,
                        button_style, button_color, button_background, button_picture;

    private PaintView paintView;
    private AdView adView;
    private Recorder recorder;
    private Thread thread;
    private Drawable userDrawable;

    private static boolean isRecording = false;
    private static boolean isSaved = false;
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 100;
    private static long exitTime = 0;
    private boolean debugmode = true;
    private final String TAG = "[MainActivity] ";

    MySharedPreference mySharedPreference = new MySharedPreference(MainActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewgroup);
        paintView = (PaintView)findViewById(R.id.paintview);
        adView = (AdView)findViewById(R.id.adView);
        button_record = (ImageButton)findViewById(R.id.button_record);
        button_save = (ImageButton)findViewById(R.id.button_save);
        button_share = (ImageButton)findViewById(R.id.button_share);
        button_clear = (ImageButton)findViewById(R.id.button_clear);
        button_style = (ImageButton)findViewById(R.id.button_style);
        button_color = (ImageButton)findViewById(R.id.button_color);
        button_background = (ImageButton)findViewById(R.id.button_background);
        button_picture = (ImageButton)findViewById(R.id.button_picture);

        setLocale();
        checkPermission();

        MobileAds.initialize(this, String.valueOf(R.string.app_id));
        //AdRequest adRequest = new AdRequest.Builder().build();
        AdRequest adRequest = new AdRequest.Builder().addTestDevice(String.valueOf(R.string.test_device)).build();
        adView.loadAd(adRequest);

        if(mySharedPreference.getUserVersion()!=2){
            if(debugmode)Log.e(TAG, "Version != 2");
            setMySharedPreference(); //initialize preference
            mySharedPreference.saveUserVersion(2);
        }

        if(!mySharedPreference.getGuide()){
            QuickGuide quickGuide = new QuickGuide(MainActivity.this);
            quickGuide.showQuickGuide();
            mySharedPreference.setGuide(true);
        }

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
                    isSaved = true;
                }else {
                    if(debugmode){
                        if(isRecording){
                            Toast.makeText(MainActivity.this, R.string.button_save_is_recording, Toast.LENGTH_SHORT).show();
                        } else if(recorder==null){
                            Toast.makeText(MainActivity.this, R.string.button_save_recorder_null, Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });

        button_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(debugmode)Log.e(TAG, "button_share");
                if( (!isRecording) && (recorder!=null) && (isSaved)) {
                    Uri uri = Uri.fromFile(recorder.getPictureFile());
                    shareDialog(uri);
                }else {
                    if(debugmode){
                        if(isRecording){
                            Toast.makeText(MainActivity.this, R.string.button_share_is_recording, Toast.LENGTH_SHORT).show();
                        } else if(recorder==null){
                            Toast.makeText(MainActivity.this, R.string.button_share_recorder_null, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MainActivity.this, R.string.button_share_not_save, Toast.LENGTH_SHORT).show();
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
                clear();
            }
        });

        button_style.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(debugmode)Log.e(TAG, "button_style");
                penstyleDialog();
            }
        });

        button_color.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(debugmode)Log.e(TAG, "button_color");
                colorDialog();
            }
        });

        button_background.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(debugmode)Log.e(TAG, "button_background");
                backgroundDialog();
                userDrawable = null;
            }
        });

        button_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    protected void onResume(){
        super.onResume();
        if(debugmode)Log.e(TAG, "onResume()");
        //user_rate = mySharedPreference.getUserRate();
    }

    @Override
    protected void onPause(){
        super.onPause();
        if(debugmode)Log.e(TAG, "onPause()");
        //mySharedPreference.saveUserRate(user_rate);
    }

    private void startRecord(){
        if(debugmode)Log.e(TAG, "start record");
        if(recorder!=null){
            //Clear canvas before recording
            clear();
        }

        isRecording = true;
        recorder = new Recorder(getApplicationContext(), paintView);
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

    private void clear(){
        if(debugmode)Log.e(TAG, "clear()");
        recorder = null; //There is no reference to the object, it will be deleted by the GC
        isSaved = false;
        paintView.clearPaint();
        if(userDrawable==null){
            paintView.setCanvasBackground(mySharedPreference.getUserBackground());
        }else{
            paintView.setCanvasPicture(userDrawable);
        }
    }

    private void shareDialog(final Uri uri){
        Preview_dialog preview_dialog = new Preview_dialog(MainActivity.this, recorder, 1, uri);
        preview_dialog.showPreviewDialog();
    }

    private void penstyleDialog(){
        AlertDialog.Builder select_style = new AlertDialog.Builder(MainActivity.this).
                setSingleChoiceItems(new String[]{"Fine","Medium","Broad"}, (mySharedPreference.getUserStyle()/3)-1,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which){
                                    case 0:
                                        mySharedPreference.saveUserStyle(3);
                                        paintView.setPenStyle(3);
                                        break;
                                    case 1:
                                        mySharedPreference.saveUserStyle(6);
                                        paintView.setPenStyle(6);
                                        break;
                                    case 2:
                                        mySharedPreference.saveUserStyle(10);
                                        paintView.setPenStyle(10);
                                        break;
                                }
                            }
                        });
        select_style.setPositiveButton(R.string.word_confirm, null);
        select_style.show();
    }

    private void colorDialog(){
        PenColor_dialog penColor_dialog = new PenColor_dialog(MainActivity.this, paintView);
        penColor_dialog.showPenColorDialog();
    }

    private void backgroundDialog(){
        BackgroundColor_dialog backgroundColor_dialog = new BackgroundColor_dialog(MainActivity.this, paintView);
        backgroundColor_dialog.showBackgroundColorDialog();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            Uri pictureUri = data.getData();
            try {
                InputStream inputStream = this.getContentResolver().openInputStream(pictureUri);
                userDrawable = Drawable.createFromStream(inputStream, pictureUri.toString() );
                paintView.clearPaint();
                paintView.setCanvasPicture(userDrawable);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void setLocale(){
        Locale.setDefault(new Locale(Locale.getDefault().getLanguage()));
    }

    private void checkPermission(){
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                new AlertDialog.Builder(MainActivity.this)
                        .setMessage(R.string.give_me_permission)
                        .setPositiveButton(R.string.word_ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(MainActivity.this,
                                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);
                            }
                        })
                        .setNegativeButton(R.string.word_no, new DialogInterface.OnClickListener() {
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

    private void setMySharedPreference(){
        int init_color = 0xffff0000;
        int init_background = 0xffffffff;
        int init_style = 3;
        mySharedPreference.saveUserStyle(init_style);
        mySharedPreference.saveUserColor(init_color);
        mySharedPreference.saveUserBackground(init_background);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id){
            case R.id.menu_guide:
                QuickGuide quickGuide = new QuickGuide(MainActivity.this);
                quickGuide.showQuickGuide();
                break;
            case R.id.menu_feedback:
                Feedback feedback = new Feedback(MainActivity.this);
                feedback.sendFeedback();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if((System.currentTimeMillis()-exitTime) > 2000) {
                Toast.makeText(getApplicationContext(), R.string.click_button_twice,
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
