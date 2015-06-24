package ir.rasen.charsoo.view.dialog;

import android.content.Context;

import ir.rasen.charsoo.R;
import ir.rasen.charsoo.view.widgets.material_library.widgets.Dialog;

public class DialogMessage extends Dialog{

    public DialogMessage(Context context, int errorId) {
        super(context, context.getString(R.string.error), context.getString(errorId));
        init();
    }
    public DialogMessage(Context context, String error) {
        super(context, context.getString(R.string.error), error);
        init();
    }
    public DialogMessage(Context context, String title, int messageId) {
        super(context, title, context.getString(messageId));
        init();
    }
    public DialogMessage(Context context, int titleId, int messageId) {
        super(context, context.getString(titleId), context.getString(messageId));
        init();
    }
    public DialogMessage(Context context, int titleId, String message) {
        super(context, context.getString(titleId), message);
        init();
    }
    public DialogMessage(Context context, String title, String message) {
        super(context, title, message);
        init();
    }

    private void init() {
        setOnAcceptButtonClickListener(null);
    }

}
