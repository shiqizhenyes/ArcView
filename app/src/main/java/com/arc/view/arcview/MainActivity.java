package com.arc.view.arcview;


import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.arc.view.arcview.view.ArcView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button bt = findViewById(R.id.bt);
        final ArcView av = findViewById(R.id.av);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                av.setStartColor(getResources().getColorStateList(R.color.green));
                av.setEndColor(getResources().getColorStateList(R.color.colorAccent));
                av.setGradation(true);
                av.refresh();
            }
        });

    }
}
