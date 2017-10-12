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
    private static final String RATE = "RATE";
    private static final String COLOR = "COLOR";
    private static final String BACKGROUND = "BACKGROUND";

    public void setGuide(boolean hasRead){
        settings = context.getSharedPreferences(DATA,0);
        settings.edit().putBoolean(GUIDE, hasRead).commit();
    }

    public boolean getGuide(){
        settings = context.getSharedPreferences(DATA,0);
        return settings.getBoolean(GUIDE, false);
    }

    public void savePreference(int rate, int color, int background){
        settings = context.getSharedPreferences(DATA,0);
        settings.edit()
                .putInt(RATE, rate)
                .putInt(COLOR, color)
                .putInt(BACKGROUND, background)
                .commit();
    }

    public void saveUserRate(int rate){
        settings = context.getSharedPreferences(DATA,0);
        settings.edit().putInt(RATE, rate).commit();
    }

    public void saveUserColor(int color){
        settings = context.getSharedPreferences(DATA,0);
        settings.edit().putInt(COLOR, color).commit();
    }

    public void saveUserBackground(int background){
        settings = context.getSharedPreferences(DATA,0);
        settings.edit().putInt(BACKGROUND, background).commit();
    }

    public int getUserRate(){
        settings = context.getSharedPreferences(DATA,0);
        return settings.getInt(RATE, 1);
    }

    public int getUserColor(){
        settings = context.getSharedPreferences(DATA,0);
        return settings.getInt(COLOR, 0);
    }

    public int getUserBackground(){
        settings = context.getSharedPreferences(DATA,0);
        return settings.getInt(BACKGROUND, 0);
    }
}
