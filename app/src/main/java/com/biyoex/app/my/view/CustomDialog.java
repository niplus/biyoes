package com.biyoex.app.my.view;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.StyleRes;
import androidx.viewpager.widget.ViewPager;

import com.biyoex.app.R;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mac on 20/3/5.
 */

public class CustomDialog extends Dialog {

    public interface MyDialogListener {
        void onClick(View view, int dialogId, CustomDialog dialog);
    }

    private MyDialogListener listener;
    private int dialogId = -1;
    private DialogOnClickListener onClickListener = new DialogOnClickListener();
    private ViewGroup contentView;
    public Map<Integer, View> viewMap = new HashMap<>();
    private Object object;

    public CustomDialog(Context context) {
        super(context);
    }

    public CustomDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }

    public void setDialogId(int dialogId) {
        this.dialogId = dialogId;
    }

    private void setRootView(ViewGroup viewGroup) {
        this.contentView = viewGroup;
    }

    private void traverseParent(View view) {
        if (view instanceof ViewGroup) {
            if (view instanceof ViewPager) {
                if (view.getId() != -1) {
                    addViewMap(view);
                }
            } else {
                for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                    if (((ViewGroup) view).getChildAt(i).getId() != -1) {
                        addViewMap(((ViewGroup) view).getChildAt(i));
                    }
                    if (((ViewGroup) view).getChildAt(i) instanceof ViewGroup) {
                        traverseParent(((ViewGroup) view).getChildAt(i));
                    }
                }
            }
        } else {
            if (view.getId() != -1) {
                addViewMap(view);
            }
        }
    }

    private void addViewMap(View view) {
        view.setOnClickListener(onClickListener);
        view = contentView.findViewById(view.getId());
        viewMap.put(view.getId(), view);
    }


    public void setDialogListener(MyDialogListener listener) {
        this.listener = listener;
    }


    private class DialogOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            if (listener != null) {
                listener.onClick(v, dialogId, CustomDialog.this);
            }
        }
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    private static class MyDialogParams {
        public boolean canceledOnTouchOutside = true, cancelable = true;
        public int width = ViewGroup.LayoutParams.MATCH_PARENT, height = ViewGroup.LayoutParams.WRAP_CONTENT,
                gravity = Gravity.CENTER, dialogId = -1, styleId = -1, layoutId = -1;
        public MyDialogListener listener = null;
        private ViewGroup view;
        private Context context;

        public MyDialogParams(Context context) {
            this.context = context;
        }

        public void initDialog(CustomDialog dialog) {
            dialog.setCanceledOnTouchOutside(canceledOnTouchOutside);
            dialog.setCancelable(cancelable);
            if (gravity != 0) {
                dialog.getWindow().setGravity(gravity);
            }
            if (gravity == Gravity.BOTTOM) {
                dialog.getWindow().setWindowAnimations(R.style.BottomDialogAnimation);
            }
            view = (ViewGroup) LayoutInflater.from(context).inflate(layoutId, null);
            dialog.setRootView(view);
            dialog.setContentView(view);
            view.getLayoutParams().width = width;
            view.getLayoutParams().height = height;
            if (listener != null) {
                dialog.setDialogListener(listener);
            }
            if (dialogId != -1) {
                dialog.setDialogId(dialogId);
            }
            dialog.traverseParent(view);
        }


    }


    public static class Builder {

        private MyDialogParams p;
        private Context context;

        public Builder(Context context) {
            this.context = context;
            p = new MyDialogParams(context);
        }

        public Builder setContentView(int layoutId) {
            p.layoutId = layoutId;
            return this;
        }

        public Builder setCancelable(boolean cancelable) {
            p.cancelable = cancelable;
            return this;
        }

        public Builder setCanceledOnTouchOutside(boolean canceledOnTouchOutside) {
            p.canceledOnTouchOutside = canceledOnTouchOutside;
            return this;
        }

        public Builder setDialogListener(MyDialogListener listener) {
            p.listener = listener;
            return this;
        }

        public Builder setDialogWidth(int width) {
            p.width = width;
            return this;
        }

        public Builder setDialogHeight(int height) {
            p.height = height;
            return this;
        }

        public Builder setDialogGravity(int gravity) {
            p.gravity = gravity;
            return this;
        }

        public Builder setDialogId(int dialogId) {
            p.dialogId = dialogId;
            return this;
        }

        public Builder setStyleId(int styleId) {
            p.styleId = styleId;
            return this;
        }

        public CustomDialog create() {
            if (p.layoutId == -1) {
                try {
                    throw new Exception("请先设置layoutId");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            CustomDialog dialog;
            if (p.styleId == -1) {
                dialog = new CustomDialog(this.context);
            } else {
                dialog = new CustomDialog(this.context, p.styleId);
            }
            p.initDialog(dialog);
            return dialog;
        }

    }

}
