package chen.kuanlin.livemessage;

import android.view.View;
import android.widget.Button;

import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ViewTarget;

/**
 * Created by kuanlin on 2017/11/2.
 */

public class UpdateGuide {

    private MainActivity mainActivity;

    public UpdateGuide(MainActivity mainActivity){
        this.mainActivity = mainActivity;
    }

    public void showUpdateGuide(){
        ViewTarget styleTarget = new ViewTarget(mainActivity.button_style);
        ViewTarget colorTarget = new ViewTarget(mainActivity.button_color);

        Button styleButton=new Button(mainActivity);
        styleButton.setText(R.string.guide_next_step);
        Button colorButton=new Button(mainActivity);
        colorButton.setText(R.string.guide_end);

        final ShowcaseView styleShowcaseView=new ShowcaseView.Builder(mainActivity)
                .withMaterialShowcase()
                .setStyle(R.style.CustomShowcaseTheme)
                .setTarget(styleTarget)
                .setContentTitle(R.string.guide_style_title)
                .setContentText(R.string.guide_style_text)
                .replaceEndButton(styleButton)
                .build();
        final ShowcaseView colorShowcaseView=new ShowcaseView.Builder(mainActivity)
                .withMaterialShowcase()
                .setStyle(R.style.CustomShowcaseTheme)
                .setTarget(colorTarget)
                .setContentTitle(R.string.guide_color_title)
                .setContentText(R.string.guide_color_text)
                .replaceEndButton(colorButton)
                .build();
        colorShowcaseView.hide();

        styleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                styleShowcaseView.hide();
                colorShowcaseView.show();
            }
        });
        colorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                colorShowcaseView.hide();
            }
        });
    }
}
