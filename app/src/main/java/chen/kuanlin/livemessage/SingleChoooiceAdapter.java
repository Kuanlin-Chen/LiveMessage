package chen.kuanlin.livemessage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by kuanlin on 2017/12/26.
 */

public class SingleChoooiceAdapter extends ArrayAdapter{
    private Context context;
    private String[] textResource;
    private Integer[] imageResource;

    public SingleChoooiceAdapter(Context context, int resource, String[] textResource, Integer[] imageResource){
        super(context, resource, textResource);
        this.context = context;
        this.textResource = textResource;
        this.imageResource = imageResource;
    }

    @Override
    public int getCount(){
        return super.getCount();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = LayoutInflater.from(context);
        View myView = inflater.inflate(R.layout.list_item, null);
        TextView textView = (TextView)myView.findViewById(R.id.textview);
        ImageView imageView = (ImageView)myView.findViewById(R.id.imageview);

        textView.setText(textResource[position]);
        imageView.setImageResource(imageResource[position]);

        return myView;
    }
}