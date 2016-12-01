package com.wxxiaomi.ming.bicyclewebmodule.ui_3.builder;

import android.content.Intent;
import android.view.ViewGroup;

/**
 * Created by Administrator on 2016/12/1.
 */

public interface Builder {
    ViewGroup buildView();
    void initPageData();
    void registerMethod();

    void onActivityResult(int requestCode, int resultCode, Intent data);
}
