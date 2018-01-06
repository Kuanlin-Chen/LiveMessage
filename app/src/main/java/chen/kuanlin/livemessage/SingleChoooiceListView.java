package chen.kuanlin.livemessage;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.view.View;
import android.widget.ListView;

/**
 * Created by kuanlin on 2017/12/26.
 */

public class SingleChoooiceListView {
    private Activity activity;
    private Context context;
    private String[] textResource;
    private Integer[] imageResource;
    private int position;
    private String title;

    public SingleChoooiceListView(Activity activity, Context context, String[] textResource, Integer[] imageResource, int position){
        this.activity = activity;
        this.context = context;
        this.textResource = textResource;
        this.imageResource = imageResource;
        this.position = position;
        this.title = "Choose One";
    }

    public SingleChoooiceListView(Activity activity, Context context, String[] textResource, Integer[] imageResource, int position, String title){
        this.activity = activity;
        this.context = context;
        this.textResource = textResource;
        this.imageResource = imageResource;
        this.position = position;
        this.title = title;
    }

    public void showDialog(){
        //Create an instance of MyAdapter for Listview
        SingleChoooiceAdapter adapter = new SingleChoooiceAdapter(context, R.layout.list_item, textResource, imageResource);

        //Create an instance of ListView for AlertDialog
        View dialogView = activity.getLayoutInflater().inflate(R.layout.dialog_main, null);
        final ListView simpleListView = (ListView)dialogView.findViewById(R.id.simpleListView);
        simpleListView.setAdapter(adapter);
        simpleListView.setItemChecked(position,true);

        //Create an instance of AlertDialog
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle(title);
        dialog.setView(dialogView);
        dialog.setPositiveButton("Done", null);
        dialog.show();
    }
}
