package com.wxxiaomi.ming.bicyclewebmodule.ui_3;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.ViewGroup;

/**
 * Created by Administrator on 2016/12/1.
 */

public class FragBuilder implements Builder {
    public FragBuilder(Fragment fragment){
        fragment.getArguments();
    }

    @Override
    public ViewGroup buildView() {
        return null;
    }

    @Override
    public void registerMethod() {

    }

    @Override
    public void initPageUI() {

    }

    @Override
    public void initPageData() {

    }
}
