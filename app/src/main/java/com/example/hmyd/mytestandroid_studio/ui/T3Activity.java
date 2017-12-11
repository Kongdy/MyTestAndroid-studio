package com.example.hmyd.mytestandroid_studio.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.example.hmyd.mytestandroid_studio.R;
import com.example.hmyd.mytestandroid_studio.view.RotateImage;

public class T3Activity extends BasicActivity {

    private RotateImage ri_image;

    private Handler handler = new Handler(
            new Handler.Callback() {
                @Override
                public boolean handleMessage(Message msg) {
                    ri_image.doRotate();
                    return false;
                }
            }
    );

    @Override
    public void setParams(Bundle savedInstanceState) {
        setContentView(R.layout.activity_t3);
        ri_image = (RotateImage) findViewById(R.id.ri_image);

        Thread thread = new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        for(;;)
                        {
                            try {
                                Thread.sleep(50);
                                handler.sendEmptyMessage(1);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
        );
        thread.start();
    }


    @Override
    public void onClick(View v) {

    }
}
