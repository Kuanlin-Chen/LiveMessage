package chen.kuanlin.livemessage;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

/**
 * Created by kuanlin on 2017/11/6.
 */

public class OutOfMemory_dialog {

    private Context context;
    private MainActivity parent;

    public OutOfMemory_dialog(MainActivity parent, Context context){
        this.parent = parent;
        this.context = context;
    }

    public void showOutOfMemoryDialog(){
        //pause
        parent.isRecording = false;
        parent.button_record.setImageResource(R.drawable.ic_media_play);
        //show dialog
        AlertDialog.Builder outofmemory_dialog = new AlertDialog.Builder(context).
                setTitle(R.string.dialog_outmemory_title).
                setMessage(R.string.dialog_outmemory_message);
        outofmemory_dialog.setNegativeButton(R.string.word_no, null);
        outofmemory_dialog.setPositiveButton(R.string.word_save_now, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int fix) {
                parent.saveData();
            }
        });
        outofmemory_dialog.show();
    }
}
