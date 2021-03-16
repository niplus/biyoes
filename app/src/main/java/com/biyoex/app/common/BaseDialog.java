package com.biyoex.app.common;

import android.app.Dialog;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class BaseDialog extends Dialog {

    private BaseDialog(@NonNull Context context) {
        super(context);
    }

    private BaseDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    private BaseDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

}
