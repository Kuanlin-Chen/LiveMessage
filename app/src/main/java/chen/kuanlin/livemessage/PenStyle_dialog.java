package chen.kuanlin.livemessage;

import android.app.Activity;
import android.content.Context;

/**
 * Created by kuanlin on 2017/11/1.
 */

public class PenStyle_dialog {

    private Activity activity;
    private Context context;
    private PaintView paintView;
    private String[] textResource;
    private Integer[] imageResource;
    private int defaultItem;
    private boolean debugmode = true;
    private final String TAG = "[PenStyle_dialog] ";

    MySharedPreference mySharedPreference;

    public PenStyle_dialog(Activity activity, Context context, PaintView paintView){
        this.activity = activity;
        this.context = context;
        this.paintView = paintView;
        this.textResource = new String[]{context.getString(R.string.word_fine),
                context.getString(R.string.word_medium),context.getString(R.string.word_bold)};
        this.imageResource = new Integer[]{R.drawable.pen_fine,R.drawable.pen_medium,R.drawable.pen_bold};
        mySharedPreference = new MySharedPreference(context);
        this.defaultItem = (mySharedPreference.getUserStyle()/3)-1;
    }

    public void showPenStyleDialog(){
        /*AlertDialog.Builder select_style = new AlertDialog.Builder(context).
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
        select_style.show();*/
        SingleChoooiceListView singleChoooiceListView = new SingleChoooiceListView(activity, context, textResource, imageResource, defaultItem, context.getString(R.string.dialog_select_pen_style));
        singleChoooiceListView.showDialog();
    }
}
