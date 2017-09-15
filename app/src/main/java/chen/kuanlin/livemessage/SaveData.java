package chen.kuanlin.livemessage;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

/**
 * Created by kuanlin on 2017/9/15.
 */

public class SaveData extends AsyncTask<String, Integer, Integer> {

    private ProgressDialog myDialog;
    private Context context;
    private Recorder recorder;

    public SaveData(Context context, Recorder recorder){
        this.context = context;
        this.recorder = recorder;
    }

    @Override
    protected void onPreExecute() {
        //在背景執行之前要做的事，寫在這裡
        //初始化進度條
        myDialog = new ProgressDialog(context);
        myDialog.setMessage("Saving Data");
        myDialog.setCancelable(false);
        myDialog.show();
        super.onPreExecute();
    }
    @Override
    protected Integer doInBackground(String... param) {
        //一定必須覆寫的方法
        //背景執行的內容放此
        //這裡不能和UI有任何互動
        recorder.storeGIF();
        return 1;
    }

    @Override
    protected void onProgressUpdate(Integer... progress) {
        //此方法會取得一個數值，可以用來計算目前執行進度
        //通常用來改變進度列(Progressbar)
        super.onProgressUpdate(progress);
    }

    @Override
    protected void onPostExecute(Integer result) {
        //doInBackground執行完後就會執行此方法
        //通常用來傳資料給UI顯示
        super.onPostExecute(result);
        if(result.equals(1)){
            myDialog.dismiss();
        }
    }
}
