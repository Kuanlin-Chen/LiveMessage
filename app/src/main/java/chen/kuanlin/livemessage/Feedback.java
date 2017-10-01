package chen.kuanlin.livemessage;

import android.content.Context;
import android.content.Intent;

/**
 * Created by kuanlin on 2017/10/1.
 */

public class Feedback {

    private Context context;

    public Feedback(Context context){
        this.context = context;
    }

    public void sendFeedback() {
        final Intent _Intent = new Intent(android.content.Intent.ACTION_SEND);
        _Intent.setType("text/html");
        _Intent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{ context.getString(R.string.feedback_email) });
        _Intent.putExtra(android.content.Intent.EXTRA_SUBJECT, context.getString(R.string.feedback_subject));
        _Intent.putExtra(android.content.Intent.EXTRA_TEXT, context.getString(R.string.feedback_message));
        context.startActivity(Intent.createChooser(_Intent, context.getString(R.string.feedback_title)));
    }
}
