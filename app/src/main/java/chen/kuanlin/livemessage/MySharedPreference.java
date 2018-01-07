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
    private static final String STYLE = "STYLE";
    private static final String COLOR = "COLOR";
    private static final String BACKGROUND = "BACKGROUND";
    private static final String VERSION = "VERSION";
    private static final String WIDTH = "WIDTH";

    public void setGuide(boolean hasRead){
        settings = context.getSharedPreferences(DATA,0);
        settings.edit().putBoolean(GUIDE, hasRead).commit();
    }

    public boolean getGuide(){
        settings = context.getSharedPreferences(DATA,0);
        return settings.getBoolean(GUIDE, false);
    }

    public void savePreference(int style, int color, int background){
        settings = context.getSharedPreferences(DATA,0);
        settings.edit()
                .putInt(STYLE, style)
                .putInt(COLOR, color)
                .putInt(BACKGROUND, background)
                .commit();
    }

    public void saveUserStyle(int style){
        settings = context.getSharedPreferences(DATA,0);
        settings.edit().putInt(STYLE, style).commit();
    }

    public void saveUserColor(int color){
        settings = context.getSharedPreferences(DATA,0);
        settings.edit().putInt(COLOR, color).commit();
    }

    public void saveUserBackground(int background){
        settings = context.getSharedPreferences(DATA,0);
        settings.edit().putInt(BACKGROUND, background).commit();
    }

    public void saveUserVersion(int version){
        settings = context.getSharedPreferences(DATA,0);
        settings.edit().putInt(VERSION, version).commit();
    }

    public void saveUserWidth(int width){
        settings = context.getSharedPreferences(DATA,0);
        settings.edit().putInt(WIDTH, width).commit();
    }

    public int getUserStyle(){
        settings = context.getSharedPreferences(DATA,0);
        return settings.getInt(STYLE, 0);
    }

    public int getUserColor(){
        settings = context.getSharedPreferences(DATA,0);
        return settings.getInt(COLOR, 0);
    }

    public int getUserBackground(){
        settings = context.getSharedPreferences(DATA,0);
        return settings.getInt(BACKGROUND, 0);
    }

    public int getUserVersion(){
        settings = context.getSharedPreferences(DATA,0);
        return settings.getInt(VERSION, 0);
    }

    public int getUserWidth(){
        settings = context.getSharedPreferences(DATA,0);
        return settings.getInt(WIDTH, 0);
    }
}
