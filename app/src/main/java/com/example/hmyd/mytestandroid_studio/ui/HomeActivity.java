package com.example.hmyd.mytestandroid_studio.ui;

import com.example.hmyd.mytestandroid_studio.R;
import com.example.hmyd.mytestandroid_studio.adapter.MySimpleAdapter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

/**
 * app首页
 *
 * @author wangk
 */
public class HomeActivity extends BasicActivity {

    private String[] function_sets = {"recyclerview的使用,和内存究极优化【MainActivty】","动态浮动标题栏【T1Activity】",
            "图片处理【PicPowerActivity】","游戏2048【Game2048Activity】","toolbar操作【ToolBarActivity】",
                "dataBinding【DataBindingTestActivity】","测试大图片加载","仿微信通讯录"};

    private ListView function_list;

    @Override
    public void onClick(View v) {

    }

    @Override
    public void setParams(Bundle savedInstanceState) {
        setContentView(R.layout.activity_home);
        function_list = (ListView) findViewById(R.id.function_list);
        MySimpleAdapter adapter = new MySimpleAdapter(getApplicationContext(),
                function_sets);
        function_list.setAdapter(adapter);

        function_list.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent();
            switch (position) {
                case 0:
                    intent.setClass(HomeActivity.this,MainActivity.class);
                    break;
                case 1:
                    intent.setClass(HomeActivity.this, T1Activity.class);
                    break;
                case 2:
                    intent.setClass(HomeActivity.this, PicPowerActivity.class);
                    break;
                case 3:
                    intent.setClass(HomeActivity.this,Game2048Activity.class);
                    break;
                case 4:
                    intent.setClass(HomeActivity.this,ToolBarActivity.class);
                    break;
                case 5:
                    intent.setClass(HomeActivity.this,DataBindingTestActivity.class);
                    break;
                case 6:
                    intent.setClass(HomeActivity.this,TestActivity.class);
                    break;
                case 7:
                    intent.setClass(HomeActivity.this,T2Activity.class);
                    break;
                default:
                    break;
            }
            startActivity(intent);
        });
    }


}
