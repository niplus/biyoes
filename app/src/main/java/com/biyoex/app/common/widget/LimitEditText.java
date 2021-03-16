package com.biyoex.app.common.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputConnectionWrapper;

import com.biyoex.app.common.utils.RegexUtils;

public class LimitEditText extends androidx.appcompat.widget.AppCompatEditText {


    public LimitEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
        return new InnerInputConnecttion(super.onCreateInputConnection(outAttrs),
                false);
    }


    class InnerInputConnecttion extends InputConnectionWrapper implements InputConnection{

        /**
         * Initializes a wrapper.
         *
         * <p><b>Caveat:</b> Although the system can accept {@code (InputConnection) null} in some
         * places, you cannot emulate such a behavior by non-null {@link InputConnectionWrapper} that
         * has {@code null} in {@code target}.</p>
         *
         * @param target  the {@link InputConnection} to be proxied.
         * @param mutable set {@code true} to protect this object from being reconfigured to target
         *                another {@link InputConnection}.  Note that this is ignored while the target is {@code null}.
         */
        public InnerInputConnecttion(InputConnection target, boolean mutable) {
            super(target, mutable);
        }

        @Override
        public boolean commitText(CharSequence text, int newCursorPosition) {
            //限制emoji的输入
            if (RegexUtils.findEmoji(text + "")){
                return false;
            }
            return super.commitText(text, newCursorPosition);
        }
    }
}
