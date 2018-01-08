package chen.kuanlin.livemessage;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

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

    MySharedPreference mySharedPreference;

    public PenStyle_dialog(Activity activity, Context context, PaintView paintView){
        this.activity = activity;
        this.context = context;
        this.paintView = paintView;
        this.textResource = new String[]{context.getString(R.string.word_normal),
                context.getString(R.string.word_dash),context.getString(R.string.word_discrete)};
        this.imageResource = new Integer[]{R.drawable.pen_normal,R.drawable.pen_dash,R.drawable.pen_discrete};
        mySharedPreference = new MySharedPreference(context);
        this.defaultItem = (mySharedPreference.getUserStyle());
    }

    public void showPenStyleDialog(){
        //Create an instance of MyAdapter for Listview
        SingleChoooiceAdapter adapter = new SingleChoooiceAdapter(context, R.layout.list_item, textResource, imageResource);
        //Create an instance of ListView for AlertDialog
        View dialogView = activity.getLayoutInflater().inflate(R.layout.dialog_main, null);
        final ListView simpleListView = (ListView)dialogView.findViewById(R.id.simpleListView);
        simpleListView.setAdapter(adapter);
        simpleListView.setItemChecked(defaultItem,true);
        //Set up listener
        simpleListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        mySharedPreference.saveUserStyle(0);
                        paintView.setPenStyle(0);
                        break;
                    case 1:
                        mySharedPreference.saveUserStyle(1);
                        paintView.setPenStyle(1);
                        break;
                    case 2:
                        mySharedPreference.saveUserStyle(2);
                        paintView.setPenStyle(2);
                        break;
                }
            }
        });
        //Set up textView
        final TextView textView_width = (TextView)dialogView.findViewById(R.id.textView_width);
        textView_width.setText(context.getString(R.string.word_size)+":"+mySharedPreference.getUserWidth());
        //Set up seekbar
        final SeekBar seekBar_width = (SeekBar)dialogView.findViewById(R.id.seekBar_width);
        seekBar_width.setProgress((mySharedPreference.getUserWidth()/2)-1);
        seekBar_width.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textView_width.setText(context.getString(R.string.word_size)+":"+(progress+1)*2);
                mySharedPreference.saveUserWidth((progress+1)*2);
                paintView.setPenWidth((progress+1)*2);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        //Create an instance of AlertDialog
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle(R.string.dialog_select_pen_style);
        dialog.setView(dialogView);
        dialog.setPositiveButton(R.string.word_confirm, null);
        dialog.show();
    }
}
