package chen.kuanlin.livemessage;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.os.Handler;
import android.view.View;

import com.waynejo.androidndkgif.GifEncoder;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by kuanlin on 2017/9/7.
 */

public class Recorder implements Runnable {

    private Context context;
    private PaintView paintView;
    private Bitmap image;
    private File pictureFile;
    private ArrayList<Bitmap> bitmapList = new ArrayList<Bitmap>();
    private Handler handler = new Handler();
    private MainActivity parent;

    private static int rate = 1;
    private static boolean isContinue;

    private static int bitmapWidth = 1; //for generateJniGIF()
    private static int bitmapHeight = 1; //for generateJniGIF()

    public Recorder(MainActivity parent, Context context, PaintView paintView){
        this.parent = parent;
        this.context = context;
        this.paintView = paintView;
        isContinue = true;
    }

    public void terminate(){
        isContinue = false;
    }

    @Override
    public void run(){
        try {
            while (isContinue){
                image = getBitmapFromView(paintView);
                bitmapList.add(image);
                Thread.sleep(500);
            }
        } catch(InterruptedException e){
            e.printStackTrace();
        } catch(OutOfMemoryError outOfMemoryError){
            terminate();
            bitmapList.remove(bitmapList.size()-1); //remove last element
            bitmapList.remove(bitmapList.size()-1); //remove second-last element
            showDialog();
        }
    }

    private void showDialog(){
        handler.post(new Runnable() {
            @Override
            public void run() {
                OutOfMemory_dialog outOfMemory_dialog = new OutOfMemory_dialog(parent, context);
                outOfMemory_dialog.showOutOfMemoryDialog();
            }
        });
    }

    public static Bitmap getBitmapFromView(View view) {
        //Define a bitmap with the same size as the view
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        //Bind a canvas to it
        Canvas canvas = new Canvas(returnedBitmap);
        //Get the view's background
        Drawable bgDrawable =view.getBackground();
        if (bgDrawable!=null)
            //has background drawable, then draw it on the canvas
            bgDrawable.draw(canvas);
        else
            //does not have background drawable, then draw white background on the canvas
            canvas.drawColor(Color.WHITE);
        // draw the view on the canvas
        view.draw(canvas);
        //resize the bitmap
        bitmapWidth = (view.getMeasuredWidth()/rate);
        bitmapHeight = (view.getMeasuredHeight()/rate);
        Bitmap resizeBitmap = Bitmap.createScaledBitmap(returnedBitmap, (view.getMeasuredWidth()/rate),(view.getMeasuredHeight()/rate), true);
        //return the bitmap
        return resizeBitmap;
    }

    private File getOutputMediaFile(){
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory()
                + "/Pictures/" + getApplicationName(context) );
                //+ "/Android/data/" + context.getPackageName() + "/Files");

        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                return null;
            }
        }
        // Create a media file name
        String timeStamp = new SimpleDateFormat("HHmmss").format(new Date());
        File mediaFile;
        String mImageName="My_"+ timeStamp +".gif";
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + mImageName);
        return mediaFile;
    }

    public void generateJniGIF(){
        pictureFile = getOutputMediaFile();
        if (pictureFile == null) {
            return;
        }

        GifEncoder gifEncoder = new GifEncoder();
        try {
            gifEncoder.init(bitmapWidth, bitmapHeight, pictureFile.toString(), GifEncoder.EncodingType.ENCODING_TYPE_SIMPLE_FAST);
        }catch (FileNotFoundException ffe){
            ffe.printStackTrace();
        }

        // Bitmap is MUST ARGB_8888.
        for (Bitmap bitmap : bitmapList){
            //gifEncoder.encodeFrame(bitmap, delayMs);
            gifEncoder.encodeFrame(bitmap, 100);
        }

        //release memory
        releaseBitmapList();

        gifEncoder.close();
    }

    public String getApplicationName(Context context) {
        ApplicationInfo applicationInfo = context.getApplicationInfo();
        int stringId = applicationInfo.labelRes;
        return stringId == 0 ? applicationInfo.nonLocalizedLabel.toString() : context.getString(stringId);
    }

    public File getPictureFile(){
        return pictureFile;
    }

    public void releaseBitmapList(){
        bitmapList = null;
    }
}
