package chen.kuanlin.livemessage;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

/**
 * Created by kuanlin on 2017/11/1.
 */

public class PenStyle_dialog {

    private Context context;
    private PaintView paintView;
    private boolean debugmode = true;
    private final String TAG = "[PenStyle_dialog] ";

    MySharedPreference mySharedPreference;

    public PenStyle_dialog(Context context, PaintView paintView){
        this.context = context;
        this.paintView = paintView;
        mySharedPreference = new MySharedPreference(context);
    }

    public void showPenStyleDialog(){
        AlertDialog.Builder select_style = new AlertDialog.Builder(context).
                setTitle(R.string.dialog_select_pen_style).
                setSingleChoiceItems(R.array.penstyle, (mySharedPreference.getUserStyle()/3)-1,
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
}
