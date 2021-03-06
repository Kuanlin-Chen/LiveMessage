package chen.kuanlin.livemessage;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
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

    protected static boolean isRecording = false;
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
        if(!hasPermission()){
            setupPermission();
        }

        MobileAds.initialize(this, String.valueOf(R.string.app_id));
        //AdRequest adRequest = new AdRequest.Builder().build();
        AdRequest adRequest = new AdRequest.Builder().addTestDevice(String.valueOf(R.string.test_device)).build();
        adView.loadAd(adRequest);

        if(mySharedPreference.getUserVersion()!=3){
            if(debugmode)Log.e(TAG, "Version != 3");
            setMySharedPreference(); //initialize preference
            mySharedPreference.saveUserVersion(3);
            if(mySharedPreference.getGuide()){
                UpdateGuide updateGuide = new UpdateGuide(MainActivity.this);
                updateGuide.showUpdateGuide();
            }
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
                saveData();
            }
        });

        button_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(debugmode)Log.e(TAG, "button_share");
                if( (!isRecording) && (recorder!=null) && (isSaved)) {
                    //Uri uri = Uri.fromFile(recorder.getPictureFile());
                    Uri uri = FileProvider.getUriForFile(MainActivity.this, BuildConfig.APPLICATION_ID, recorder.getPictureFile());
                    Preview_dialog preview_dialog = new Preview_dialog(MainActivity.this, recorder, 1, uri);
                    preview_dialog.showPreviewDialog();
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
                PenStyle_dialog penStyle_dialog = new PenStyle_dialog(MainActivity.this, MainActivity.this, paintView);
                penStyle_dialog.showPenStyleDialog();
            }
        });

        button_color.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(debugmode)Log.e(TAG, "button_color");
                PenColor_dialog penColor_dialog = new PenColor_dialog(MainActivity.this, paintView);
                penColor_dialog.showPenColorDialog();
            }
        });

        button_background.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(debugmode)Log.e(TAG, "button_background");
                BackgroundColor_dialog backgroundColor_dialog = new BackgroundColor_dialog(MainActivity.this, MainActivity.this, paintView);
                backgroundColor_dialog.showBackgroundColorDialog();
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
        recorder = new Recorder(this, MainActivity.this, paintView);
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

    protected void saveData(){
        if(debugmode)Log.e(TAG, "saveData");
        if( (!isRecording) && (recorder!=null) ){
            if(isSaved){
                Toast.makeText(MainActivity.this, R.string.dialog_saved, Toast.LENGTH_SHORT).show();
            }else{
                if(hasPermission()){
                    SaveData saveData = new SaveData(MainActivity.this,recorder);
                    saveData.execute();
                    isSaved = true;
                }else {
                    Toast.makeText(MainActivity.this, R.string.give_me_permission, Toast.LENGTH_SHORT).show();
                    setupPermission();
                }
            }
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

    protected void clear(){
        if(debugmode)Log.e(TAG, "clear()");
        if(recorder!=null){
            recorder.releaseBitmapList(); //release memory
        }
        recorder = null; //There is no reference to the object, it will be deleted by the GC
        isSaved = false;
        paintView.clearPaint();
        if(userDrawable==null){
            paintView.setCanvasBackground(mySharedPreference.getUserBackground());
        }else{
            paintView.setCanvasPicture(userDrawable);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            Uri pictureUri = data.getData();
            try {
                //clear();
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

    private boolean hasPermission(){
        return ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED;
    }

    private void setupPermission(){
        if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_READ_CONTACTS);
        } else {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_READ_CONTACTS);
        }
    }

    private void setMySharedPreference(){
        int init_color = 0xffff0000;
        int init_background = 0xffffffff;
        int init_style = 0;
        int init_width = 10;
        mySharedPreference.saveUserStyle(init_style);
        mySharedPreference.saveUserColor(init_color);
        mySharedPreference.saveUserBackground(init_background);
        mySharedPreference.saveUserWidth(init_width);
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
