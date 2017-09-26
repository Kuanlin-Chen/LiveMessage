package chen.kuanlin.livemessage;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by kuanlin on 2017/9/26.
 */

public class MySharedPreference {

    private Context context;

    public MySharedPreference(Context context){
        this.context = context;
    }

    //Set up each of column's name in SharedPreferences
    private SharedPreferences settings;
    private static final String DATA = "DATA";
    private static final String GUIDE = "GUIDE";

    public void setGuide(boolean hasRead){
        settings = context.getSharedPreferences(DATA,0);
        settings.edit().putBoolean(GUIDE, hasRead).commit();
    }

    public boolean getGuide(){
        settings = context.getSharedPreferences(DATA,0);
        return settings.getBoolean(GUIDE, false);
    }
}
