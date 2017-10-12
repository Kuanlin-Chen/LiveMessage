package chen.kuanlin.livemessage;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;

/**
 * Created by kuanlin on 2017/10/5.
 */

public class PenColor_dialog {

    private Context context;
    private PaintView paintView;
    private boolean debugmode = true;
    private final String TAG = "[PenColor_dialog] ";

    MySharedPreference mySharedPreference;

    public PenColor_dialog(Context context, PaintView paintView){
        this.context = context;
        this.paintView = paintView;
        mySharedPreference = new MySharedPreference(context);
    }

    public void showPenColorDialog(){
        ColorPickerDialogBuilder
                .with(context)
                .setTitle(R.string.dialog_select_pen_color)
                .initialColor(mySharedPreference.getUserColor())
                .wheelType(ColorPickerView.WHEEL_TYPE.CIRCLE)
                .density(6)
                .showLightnessSlider(true)
                .showAlphaSlider(false)
                .setOnColorSelectedListener(new OnColorSelectedListener() {
                    @Override
                    public void onColorSelected(int selectedColor) {
                        if(debugmode)Log.e(TAG,"onColorSelected: 0x"+Integer.toHexString(selectedColor));
                    }
                })
                .setPositiveButton(R.string.word_confirm, new ColorPickerClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
                        paintView.setPaintColor(selectedColor);
                        mySharedPreference.saveUserColor(selectedColor);
                    }
                })
                .build()
                .show();
    }
}
