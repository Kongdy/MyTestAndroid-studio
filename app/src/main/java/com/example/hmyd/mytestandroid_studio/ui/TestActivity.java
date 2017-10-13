package com.example.hmyd.mytestandroid_studio.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.hmyd.mytestandroid_studio.R;
import com.example.hmyd.mytestandroid_studio.adapter.TestAdapter;

import java.util.ArrayList;
import java.util.List;


public class TestActivity extends AppCompatActivity {


    private RecyclerView recyclerView;

    @Override
    @SuppressWarnings("unchecked")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        recyclerView = (RecyclerView) findViewById(R.id.rv_list);

        List<String> stringList = new ArrayList<String>()
        {{
            add("aaaa");
            add("aaaa");
            add("aaaa");
            add("aaaa");
            add("aaaa");
            add("aaaa");
            add("aaaa");
        }};

        recyclerView.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL,false));
        TestAdapter testAdapter = new TestAdapter(stringList);
        recyclerView.setAdapter(testAdapter);


       //aciv_test = (AppCompatImageView) findViewById(R.id.aciv_test);

      //  Glide.with(this).load("file:///android_asset/big_image_test.png").into(aciv_test);

//
//        List<Model1> list = new ArrayList<>();
//
//        for(int i = 0;i < 10;i++)
//        {
//            Model1 aa = new Model1();
//            aa.model2s = new ArrayList<>();
//            for(int j = 0;j < 10;j++)
//            {
//                Model2 model2 = new Model2();
//                model2.name = "i-j:"+i+"-"+j;
//                aa.model2s.add(model2);
//            }
//            list.add(aa);
//        }
//
//
//        List<List<String>> needCollectList = new ArrayList<>();
//        List<String> tempList = new ArrayList<>();
//
//        for (Model1 model1 : list)
//        {
//            tempList.clear();
//            for (Model2 model2 : model1.model2s)
//            {
//                tempList.add(model2.name);
//            }
//            needCollectList.add(tempList);
//        }

//        View s =
//                s.setScaleX();
//        List<String> aa = new ArrayList<>();
//        List<List<String>> bb = new ArrayList<>();
//        Observable.fromIterable(list)
//                .groupBy(new Function<Model1, Object>() {
//                    @Override
//                    public Object apply(Model1 model1) throws Exception {
//                        return model1.name;
//                    }
//                });
//        .concatMap(new Function<List<String>, ObservableSource<?>>() {
//            @Override
//            public ObservableSource<?> apply(List<String> strings) throws Exception {
//                return Observable.fromIterable(strings);
//            }
//        }).map(new Function<Object, String>() {
//
//            @Override
//            public String apply(Object o) throws Exception {
//                return o.toString();
//            }
//        }).toList()
//                .subscribe(new Consumer<List<String>>() {
//                    @Override
//                    public void accept(List<String> strings) throws Exception {
//                        bb.add(strings);
//                    }
//                });

    }

//    public class Model1
//    {
//        public String name;
//        public List<Model2> model2s;
//    }
//    public class Model2
//    {
//        public String name;
//    }
}
