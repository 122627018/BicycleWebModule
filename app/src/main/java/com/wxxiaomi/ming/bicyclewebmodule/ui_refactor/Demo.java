package com.wxxiaomi.ming.bicyclewebmodule.ui_refactor;

import android.content.Context;
import android.util.Log;
import android.widget.EditText;

/**
 * Created by Administrator on 2016/11/30.
 */

public class Demo {
    private Context mCtx;
    private EditText mTextView;
    private static Demo ourInstance = null;
    private Demo() {
    }

    private Demo(Context context) {
        this.mCtx = context;
    }
    public static Demo getInstance(Context context) {
        if (ourInstance == null) {
            ourInstance = new Demo(context);
        }
        return ourInstance;
    }

    public void demo(EditText mTextView){
        mTextView.setText(mCtx.getString(android.R.string.ok));
    }
}
