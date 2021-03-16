package com.biyoex.app.common.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.biyoex.app.R;

/**
 * Created by xxx on 2018/7/11.
 */

public class CircleIndector extends View {

    private int color = 0xff358dfa;
    private int alpha = 0x4d;
    private int padding;
    private Paint paint;

    public CircleIndector(Context context) {
        super(context);
    }

    public CircleIndector(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        paint.setColor(color);
        paint.setAntiAlias(true);
        padding = context.getResources().getDimensionPixelSize(R.dimen.indictor_padding);
    }

    public CircleIndector(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getWidth();

        paint.setAlpha(alpha);
        canvas.drawCircle(width / 2, width / 2, width / 2, paint);

        paint.setAlpha(0xff);
        canvas.drawCircle(width / 2, width / 2, width / 2 - padding, paint);
    }
}
