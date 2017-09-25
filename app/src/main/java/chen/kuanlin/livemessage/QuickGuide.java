package chen.kuanlin.livemessage;

import android.content.Context;
import android.view.View;
import android.widget.Button;

import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ViewTarget;

/**
 * Created by kuanlin on 2017/9/25.
 */

public class QuickGuide{

    private MainActivity mainActivity;

    public QuickGuide(MainActivity mainActivity){
        this.mainActivity = mainActivity;
    }

    public void registerGuide(){
        ViewTarget recordTarget = new ViewTarget(mainActivity.button_record);
        ViewTarget saveTarget = new ViewTarget(mainActivity.button_save);
        ViewTarget shareTarget = new ViewTarget(mainActivity.button_share);
        Button recordButton=new Button(mainActivity);
        recordButton.setText("下一步");
        Button saveButton=new Button(mainActivity);
        saveButton.setText("下一步");
        Button shareButton=new Button(mainActivity);
        shareButton.setText("開始使用");
        final ShowcaseView recordShowcaseView=new ShowcaseView.Builder(mainActivity)
                .withMaterialShowcase()
                .setTarget(recordTarget)
                .setContentTitle("紀錄與暫停")
                .setContentText("請按此")
                .replaceEndButton(recordButton)
                .build();
        final ShowcaseView saveShowcaseView=new ShowcaseView.Builder(mainActivity)
                .withMaterialShowcase()
                .setTarget(saveTarget)
                .setContentTitle("儲存")
                .setContentText("请输入密码")
                .replaceEndButton(saveButton)
                .build();
        saveShowcaseView.hide();
        final ShowcaseView shareShowcaseView=new ShowcaseView.Builder(mainActivity)
                .withMaterialShowcase()
                .setTarget(shareTarget)
                .setContentTitle("第三步")
                .setContentText("请点击注册按钮")
                .replaceEndButton(shareButton)
                .build();
        shareShowcaseView.hide();
        recordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recordShowcaseView.hide();
                saveShowcaseView.show();
            }
        });
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveShowcaseView.hide();
                shareShowcaseView.show();
            }
        });
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareShowcaseView.hide();
            }
        });
    }
}
