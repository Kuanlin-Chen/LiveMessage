package chen.kuanlin.livemessage;

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

    public void showQuickGuide(){
        ViewTarget recordTarget = new ViewTarget(mainActivity.button_record);
        ViewTarget saveTarget = new ViewTarget(mainActivity.button_save);
        ViewTarget shareTarget = new ViewTarget(mainActivity.button_share);
        ViewTarget clearTarget = new ViewTarget(mainActivity.button_clear);
        ViewTarget resolutionTarget = new ViewTarget(mainActivity.button_resolution);
        ViewTarget colorTarget = new ViewTarget(mainActivity.button_color);
        ViewTarget backgroundTarget = new ViewTarget(mainActivity.button_background);
        ViewTarget pictureTarget = new ViewTarget(mainActivity.button_picture);

        Button recordButton=new Button(mainActivity);
        recordButton.setText(R.string.guide_next_step);
        Button saveButton=new Button(mainActivity);
        saveButton.setText(R.string.guide_next_step);
        Button shareButton=new Button(mainActivity);
        shareButton.setText(R.string.guide_next_step);
        Button clearButton=new Button(mainActivity);
        clearButton.setText(R.string.guide_next_step);
        Button resolutionButton=new Button(mainActivity);
        resolutionButton.setText(R.string.guide_next_step);
        Button colorButton=new Button(mainActivity);
        colorButton.setText(R.string.guide_next_step);
        Button backgroundButton=new Button(mainActivity);
        backgroundButton.setText(R.string.guide_next_step);
        Button pictureButton=new Button(mainActivity);
        pictureButton.setText(R.string.guide_end);

        final ShowcaseView recordShowcaseView=new ShowcaseView.Builder(mainActivity)
                .withMaterialShowcase()
                .setStyle(R.style.CustomShowcaseTheme)
                .setTarget(recordTarget)
                .setContentTitle(R.string.guide_record_title)
                .setContentText(R.string.guide_record_text)
                .replaceEndButton(recordButton)
                .build();
        final ShowcaseView saveShowcaseView=new ShowcaseView.Builder(mainActivity)
                .withMaterialShowcase()
                .setStyle(R.style.CustomShowcaseTheme)
                .setTarget(saveTarget)
                .setContentTitle(R.string.guide_save_title)
                .setContentText(R.string.guide_save_text)
                .replaceEndButton(saveButton)
                .build();
        saveShowcaseView.hide();
        final ShowcaseView shareShowcaseView=new ShowcaseView.Builder(mainActivity)
                .withMaterialShowcase()
                .setStyle(R.style.CustomShowcaseTheme)
                .setTarget(shareTarget)
                .setContentTitle(R.string.guide_share_title)
                .setContentText(R.string.guide_share_text)
                .replaceEndButton(shareButton)
                .build();
        shareShowcaseView.hide();
        final ShowcaseView clearShowcaseView=new ShowcaseView.Builder(mainActivity)
                .withMaterialShowcase()
                .setStyle(R.style.CustomShowcaseTheme)
                .setTarget(clearTarget)
                .setContentTitle(R.string.guide_clear_title)
                .setContentText(R.string.guide_clear_text)
                .replaceEndButton(clearButton)
                .build();
        clearShowcaseView.hide();
        final ShowcaseView resolutionShowcaseView=new ShowcaseView.Builder(mainActivity)
                .withMaterialShowcase()
                .setStyle(R.style.CustomShowcaseTheme)
                .setTarget(resolutionTarget)
                .setContentTitle(R.string.guide_resolution_title)
                .setContentText(R.string.guide_resolution_text)
                .replaceEndButton(resolutionButton)
                .build();
        resolutionShowcaseView.hide();
        final ShowcaseView colorShowcaseView=new ShowcaseView.Builder(mainActivity)
                .withMaterialShowcase()
                .setStyle(R.style.CustomShowcaseTheme)
                .setTarget(colorTarget)
                .setContentTitle(R.string.guide_color_title)
                .setContentText(R.string.guide_color_text)
                .replaceEndButton(colorButton)
                .build();
        colorShowcaseView.hide();
        final ShowcaseView backgroundShowcaseView=new ShowcaseView.Builder(mainActivity)
                .withMaterialShowcase()
                .setStyle(R.style.CustomShowcaseTheme)
                .setTarget(backgroundTarget)
                .setContentTitle(R.string.guide_background_title)
                .setContentText(R.string.guide_background_text)
                .replaceEndButton(backgroundButton)
                .build();
        backgroundShowcaseView.hide();
        final ShowcaseView pictureShowcaseView=new ShowcaseView.Builder(mainActivity)
                .withMaterialShowcase()
                .setStyle(R.style.CustomShowcaseTheme)
                .setTarget(pictureTarget)
                .setContentTitle(R.string.guide_picture_title)
                .setContentText(R.string.guide_picture_text)
                .replaceEndButton(pictureButton)
                .build();
        pictureShowcaseView.hide();

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
                clearShowcaseView.show();
            }
        });
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearShowcaseView.hide();
                resolutionShowcaseView.show();
            }
        });
        resolutionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resolutionShowcaseView.hide();
                colorShowcaseView.show();
            }
        });
        colorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                colorShowcaseView.hide();
                backgroundShowcaseView.show();
            }
        });
        backgroundButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backgroundShowcaseView.hide();
                pictureShowcaseView.show();
            }
        });
        pictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pictureShowcaseView.hide();
            }
        });
    }
}
