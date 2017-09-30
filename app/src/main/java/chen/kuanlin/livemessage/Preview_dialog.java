package chen.kuanlin.livemessage;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;

import java.io.IOException;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

/**
 * Created by kuanlin on 2017/9/23.
 */

public class Preview_dialog {

    private Context context;
    private Recorder recorder;
    private int mode;
    private Uri uri;

    public Preview_dialog(Context context, Recorder recorder, int mode, Uri uri){
        this.context = context;
        this.recorder = recorder;
        this.mode = mode;
        this.uri = uri;
    }

    public void showPreviewDialog(){
        LayoutInflater factory = LayoutInflater.from(context);
        View view = factory.inflate(R.layout.preview_dialog, null);
        GifImageView gifImageView = (GifImageView)view.findViewById(R.id.imageView_preview);

        try {
            GifDrawable gifFromPath = new GifDrawable(recorder.getPictureFile());
            gifImageView.setImageDrawable(gifFromPath);
        }catch (IOException ioe){
            ioe.printStackTrace();
        }

        AlertDialog.Builder preview_dialog = new AlertDialog.Builder(context);
        if(mode==0){
            //Display preview after saving
            preview_dialog.setMessage(R.string.dialog_saved);
            preview_dialog.setPositiveButton(R.string.word_done, null);
        }else if(mode==1){
            //Display preview before sharing
            preview_dialog.setMessage(R.string.dialog_share);
            preview_dialog.setNegativeButton(R.string.word_no, null);
            preview_dialog.setPositiveButton(R.string.word_yes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int fix) {
                    Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
                    shareIntent.setType("image/gif");
                    shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
                    context.startActivity(Intent.createChooser(shareIntent, context.getString(R.string.dialog_sharing)));
                }
            });
        }
        preview_dialog.setView(view);
        preview_dialog.show();
    }
}
