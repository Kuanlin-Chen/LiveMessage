package chen.kuanlin.livemessage;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import static chen.kuanlin.livemessage.MainActivity.user_color;

/**
 * Created by kuanlin on 2017/10/5.
 */

public class PenColor_dialog {

    private Context context;
    private PaintView paintView;

    public PenColor_dialog(Context context, PaintView paintView){
        this.context = context;
        this.paintView = paintView;
    }

    public void showPenColorDialog(){
        AlertDialog.Builder select_color = new AlertDialog.Builder(context).
                setSingleChoiceItems(new String[]{context.getString(R.string.color_red), context.getString(R.string.color_yellow), context.getString(R.string.color_green),
                                context.getString(R.string.color_blue), context.getString(R.string.color_white), context.getString(R.string.color_gray), context.getString(R.string.color_black)}, user_color,
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
        select_color.setPositiveButton(R.string.word_confirm, null);
        select_color.show();
    }

}
