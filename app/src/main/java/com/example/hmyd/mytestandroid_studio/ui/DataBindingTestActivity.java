package com.example.hmyd.mytestandroid_studio.ui;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.hmyd.mytestandroid_studio.R;
import com.example.hmyd.mytestandroid_studio.databinding.ActivityDataBindingTestBinding;
import com.example.hmyd.mytestandroid_studio.model.JobBean;


/**
 * data binding示例
 */
public class DataBindingTestActivity extends BasicActivity implements SwipeRefreshLayout.OnRefreshListener {

    private ActivityDataBindingTestBinding binding;

    private JobBean jobBean;

    private int refresh_count = 0;


    @Override
    public void setParams(Bundle savedInstanceState) {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_data_binding_test);

        binding.srlContent.setColorSchemeResources(R.color.colorPrimary);

        binding.srlContent.setRefreshing(true);
        binding.srlContent.setOnRefreshListener(this);
        onRefresh();
    }

    @Override
    public void onClick(View v) {
    }

    @Override
    public void onRefresh() {
        getWindow().getDecorView().postDelayed(()->{
            Long time = System.currentTimeMillis()-((refresh_count++)+24L)*31536000000L;
            binding.srlContent.setRefreshing(false);
            jobBean = new JobBean("Kongdy","安卓开发",time,"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1493029351955&di=0c8cac5193055bd007b36515f75c7227&imgtype=0&src=http%3A%2F%2Fupload.chinaz.com%2F2016%2F0414%2F1460598808209.png");
            binding.setJob(jobBean);
            Glide.with(getApplicationContext())
                    .load(jobBean.getImage())
                    .apply(new RequestOptions().skipMemoryCache(false).diskCacheStrategy(DiskCacheStrategy.ALL))
                    .into(binding.acivAndroidLogo);
        },2000);
    }
}
