package com.wxxiaomi.ming.bicyclewebmodule.ui_3;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.wxxiaomi.ming.bicyclewebmodule.R;
import com.wxxiaomi.ming.bicyclewebmodule.ui_3.builder.impl.TabBuilder;
import com.wxxiaomi.ming.bicyclewebmodule.ui_3.builder.impl.SimpleBuilder;

public class TestWebActivity extends AppCompatActivity {

    Director director;
    private LinearLayout content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_web);
        content = (LinearLayout) findViewById(R.id.content);
        boolean isMany = getIntent().getBooleanExtra("many",false);
        if(isMany){
            director = new Director(new TabBuilder(this));
        }else{
            director = new Director(new SimpleBuilder(this));
        }

        content.addView(director.construct());
        director.PageInit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        director.onActivityResult(requestCode,resultCode,data);
        super.onActivityResult(requestCode, resultCode, data);
    }
}
