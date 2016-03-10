package com.example.hmyd.mytestandroid_studio;

import com.example.hmyd.mytestandroid_studio.adapter.MySimpleAdapter;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import java.lang.reflect.Field;

/**
 * app首页
 *
 * @author wangk
 */
public class HomeActivity extends BasicActivity {

    private String[] function_sets = {"recyclerView的使用【MainActivity】","动态浮动标题栏【T1Activity】",
            "图片处理【PicPowerActivity】"};

    private ListView function_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setParams() {
        setContentView(R.layout.activity_home);
        function_list = (ListView) findViewById(R.id.function_list);
        MySimpleAdapter adapter = new MySimpleAdapter(getApplicationContext(),
                function_sets);
        function_list.setAdapter(adapter);

        function_list.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
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
                    default:
                        break;
                }
                startActivity(intent);
            }
        });
    }


}
