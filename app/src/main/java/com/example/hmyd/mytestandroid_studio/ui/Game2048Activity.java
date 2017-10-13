package com.example.hmyd.mytestandroid_studio.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.hmyd.mytestandroid_studio.R;
import com.example.hmyd.mytestandroid_studio.view.MyPathPracticeView;

import tyrantgit.explosionfield.ExplosionField;


/**
 * 2048游戏界面
 */
public class Game2048Activity extends BasicActivity {


    private MyPathPracticeView myPathP;

    private ImageView iv_test_img;

    private ExplosionField ef_view;

    @Override
    public void onClick(View v) {

    }

    @Override
    public void setParams(Bundle savedInstanceState) {
        setContentView(R.layout.activity_game2048);
        myPathP = (MyPathPracticeView) findViewById(R.id.myPathP);

        ef_view = ExplosionField.attach2Window(this);

        iv_test_img = (ImageView) findViewById(R.id.iv_test_img);


        iv_test_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ef_view.explode(v);
                v.setVisibility(View.GONE);
            }
        });
    }
}
