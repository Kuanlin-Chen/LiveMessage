package chen.kuanlin.livemessage;

import android.app.Application;
import android.content.Context;

import org.acra.ACRA;
import org.acra.annotation.ReportsCrashes;

/**
 * Created by kuanlin on 2017/12/10.
 */

@ReportsCrashes(
        formUri = "http://www.backendofyourchoice.com/reportpath"
)
public class MyApplication extends Application {
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);

        // The following line triggers the initialization of ACRA
        ACRA.init(this);
    }
}
