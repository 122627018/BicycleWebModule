package com.wxxiaomi.ming.bicyclewebmodule.ui_3;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.wxxiaomi.ming.bicyclewebmodule.R;
import com.wxxiaomi.ming.bicyclewebmodule.action.forward.ManyH5Action;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/1.
 */

public class TabBuilder implements Builder {

    private Context context;
    private ManyH5Action action;
    private ViewGroup allView;
    private List<Builder> fragBuilders = new ArrayList<>();

    public TabBuilder(Context context){
        this.context = context;
    }

    @Override
    public ViewGroup buildView() {
        action = (ManyH5Action)((AppCompatActivity)context).getIntent().getBundleExtra("value").get("action");
        LayoutInflater lf = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        allView = (ViewGroup) lf.inflate(R.layout.activity_topic_personal,null);
//        for(int i=0;i<action.pages.size();i++){
//            FragBuilder b = new FragBuilder(context,action.pages.get(i),action.datas.get(i));
//            fragBuilders.add(b);
//        }
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
