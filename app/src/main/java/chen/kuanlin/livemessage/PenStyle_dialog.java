package chen.kuanlin.livemessage;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

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
        //Create an instance of AlertDialog
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle(R.string.dialog_select_pen_style);
        dialog.setView(dialogView);
        dialog.setPositiveButton(R.string.word_confirm, null);
        dialog.show();
    }
}
