package com.wxxiaomi.ming.bicyclewebmodule;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * 模拟多线程调度工作
 */
public class RxActivity extends AppCompatActivity {
private List<Observable<String>> list = new ArrayList<>();
    private Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx);

        Observable<String> ob = Observable.create(new Observable.OnSubscribe<String>() {

            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext("的啊:"+Thread.currentThread().getName());
            }
        })
                .subscribeOn(Schedulers.newThread());
        for(int i=0;i<8;i++){
            list.add(ob);
        }
        btn = (Button) findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                start();
            }
        });

    }

    private void start() {
        Observable.merge(list)
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        Log.i("wang","result:"+s);
                    }
                });
    }
}
