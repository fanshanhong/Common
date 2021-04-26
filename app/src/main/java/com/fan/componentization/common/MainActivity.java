package com.fan.componentization.common;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.fan.componentization.apt_annotation.ARouter;
//import com.fan.componentization.apt_annotation.BindActivity;
//import com.fan.componentization.apt_annotation.BindView;

//@BindActivity
//@ARouter(path="/app/MainActivity")
public class MainActivity extends AppCompatActivity {

//    @BindView(R.id.text_view)
//    TextView ttt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


//        BBBBBMainActivityFuZhuLei.bindView(this);
//        ttt.setText("1111");
    }
}