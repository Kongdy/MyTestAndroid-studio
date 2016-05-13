package com.example.hmyd.mytestandroid_studio;

import android.os.Bundle;
import android.view.View;

import com.example.hmyd.mytestandroid_studio.view.MyPathPracticeView;


/**
 * 2048游戏界面
 */
public class Game2048Activity extends BasicActivity {


    private MyPathPracticeView myPathP;

    @Override
    public void onClick(View v) {

    }

    @Override
    public void setParams(Bundle savedInstanceState) {
        setContentView(R.layout.activity_game2048);
        myPathP = (MyPathPracticeView) findViewById(R.id.myPathP);
    }
}
