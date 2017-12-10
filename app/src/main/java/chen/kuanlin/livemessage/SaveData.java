package chen.kuanlin.livemessage;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.widget.Toast;

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
        myDialog.setMessage(context.getString(R.string.dialog_saving));
        myDialog.setCancelable(false);
        myDialog.show();
        super.onPreExecute();
    }
    @Override
    protected Integer doInBackground(String... param) {
        //一定必須覆寫的方法
        //背景執行的內容放此
        //這裡不能和UI有任何互動
        boolean myException = recorder.generateJniGIF();
        if (myException){
            return 1;
        }else {
            return 0;
        }
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

            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            intent.setData(Uri.fromFile(recorder.getPictureFile()));
            context.sendBroadcast(intent);

            Preview_dialog preview_dialog = new Preview_dialog(context, recorder, 0, null);
            preview_dialog.showPreviewDialog();
        }else {
            myDialog.dismiss();
            Toast.makeText(context,"An unexpected error occurred, report has been sent to the developer!",Toast.LENGTH_SHORT).show();
        }
    }
}
