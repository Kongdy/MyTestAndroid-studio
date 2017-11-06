package com.example.hmyd.mytestandroid_studio.ui;

import android.os.Build;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.transition.ChangeBounds;
import android.support.transition.Scene;
import android.support.transition.TransitionManager;
import android.transition.Explode;
import android.view.View;

import com.example.hmyd.mytestandroid_studio.R;

public class TransitionActivity extends BasicActivity {

    private ConstraintLayout cl_bottom_container;

    @Override
    public void setParams(Bundle savedInstanceState) {
        setContentView(R.layout.activity_transition);

        cl_bottom_container = (ConstraintLayout) findViewById(R.id.cl_bottom_container);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.acbtn_do_transition:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    android.transition.Scene scene = android.transition.Scene.getSceneForLayout(cl_bottom_container, R.layout.layout_bottom_sun_moon, this);
                    android.transition.Visibility highAnim = new Explode();
                    android.transition.TransitionManager.go(scene, highAnim);
                } else {
                    Scene scene = Scene.getSceneForLayout(cl_bottom_container, R.layout.layout_bottom_sun_moon, this);
                    ChangeBounds anim = new ChangeBounds();
                    TransitionManager.go(scene, anim);
                }
//                Scene scene = Scene.getSceneForLayout(cl_bottom_container, R.layout.layout_bottom_sun_moon, this);
//                MyTransition anim = new MyTransition();
//                TransitionManager.go(scene, anim);
                break;
        }
    }
}
