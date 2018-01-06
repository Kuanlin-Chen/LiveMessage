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
        this.textResource = new String[]{context.getString(R.string.word_fine),
                context.getString(R.string.word_medium),context.getString(R.string.word_bold)};
        this.imageResource = new Integer[]{R.drawable.pen_fine,R.drawable.pen_medium,R.drawable.pen_bold};
        mySharedPreference = new MySharedPreference(context);
        this.defaultItem = (mySharedPreference.getUserStyle()/3)-1;
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
        //Create an instance of AlertDialog
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle(context.getString(R.string.dialog_select_pen_style));
        dialog.setView(dialogView);
        dialog.setPositiveButton("Done", null);
        dialog.show();
    }
}
