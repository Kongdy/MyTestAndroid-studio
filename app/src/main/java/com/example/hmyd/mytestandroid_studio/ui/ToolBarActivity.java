package com.example.hmyd.mytestandroid_studio.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.hmyd.mytestandroid_studio.R;

public class ToolBarActivity extends BasicActivity {

    private Button btn_hide_toolbar;

    @Override
    public void onClick(View v) {


    }

    @Override
    public void setParams(Bundle savedInstanceState) {
        setContentView(R.layout.activity_tool_bar);

        btn_hide_toolbar = (Button) findViewById(R.id.btn_hide_toolbar);
        btn_hide_toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getToolbar().getVisibility() == View.VISIBLE) {
                    btn_hide_toolbar.setText("点击显示bar");
                    getToolbar().setVisibility(View.GONE);
                }
                else {
                    getToolbar().setVisibility(View.VISIBLE);
                    btn_hide_toolbar.setText("点击隐藏bar");
                }

            }
        });
    }
}
