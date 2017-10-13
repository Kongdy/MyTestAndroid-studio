package com.example.hmyd.mytestandroid_studio.ui;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.hmyd.mytestandroid_studio.R;
import com.example.hmyd.mytestandroid_studio.adapter.HeaderRecyclerAndFooterWrapperAdapter;
import com.example.hmyd.mytestandroid_studio.adapter.T2ListAdapter;
import com.example.hmyd.mytestandroid_studio.adapter.ViewHolder;
import com.example.hmyd.mytestandroid_studio.model.ContactModel;
import com.mcxtzhang.indexlib.IndexBar.widget.IndexBar;
import com.mcxtzhang.indexlib.suspension.SuspensionDecoration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class T2Activity extends AppCompatActivity {

    private RecyclerView rvList;
    private IndexBar idbContactIndex;
    private AppCompatTextView actvIdbIndexShow;

    private LinearLayoutManager mManager;

    private HeaderRecyclerAndFooterWrapperAdapter mHeaderAdapter;
    private T2ListAdapter t2ListAdapter;
    private SuspensionDecoration mDecoration;

    private List<ContactModel> contacts = new ArrayList<>();

    private ContentResolver cr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_t2);

        rvList = (RecyclerView) findViewById(R.id.rv_list);
        idbContactIndex = (IndexBar) findViewById(R.id.idb_contact_index);
        actvIdbIndexShow = (AppCompatTextView) findViewById(R.id.actv_idb_index_show);

        rvList.setLayoutManager(mManager = new LinearLayoutManager(this));

        t2ListAdapter = new T2ListAdapter(contacts);
        mHeaderAdapter = new HeaderRecyclerAndFooterWrapperAdapter(t2ListAdapter) {
            @Override
            protected void onBindHeaderHolder(ViewHolder holder, int headerPos, int layoutId, Object o) {
                holder.setText(R.id.actv_contact_name, (String) o);
            }
        };

        rvList.setAdapter(mHeaderAdapter);
        rvList.addItemDecoration(mDecoration = new SuspensionDecoration(this, contacts).setHeaderViewCount(mHeaderAdapter.getHeaderViewCount()));

        idbContactIndex.setmLayoutManager(mManager) //设置RecyclerView的LayoutManager
                .setNeedRealIndex(true)//设置需要真实的索引
                .setmPressedShowTextView(actvIdbIndexShow); //设置HintTextView

        cr = getContentResolver();

        initData();
    }

    private void initData() {
        rvList.postDelayed(new Runnable() {
            @Override
            public void run() {
                Uri uri = Uri.parse("content://com.android.contacts/raw_contacts");
                Cursor cs = cr.query(uri, null, null, null, null, null);
                if(null != cs)
                {
                    while (cs.moveToNext()) {
                        //拿到联系人id 跟name
                        int id = cs.getInt(cs.getColumnIndex("_id"));
                        String name = cs.getString(cs.getColumnIndex("display_name"));
                        //得到这个id的所有数据（data表）
                        Uri uri1 = Uri.parse("content://com.android.contacts/raw_contacts/" + id + "/data");
                        Cursor cs2 = cr.query(uri1, null, null, null, null, null);
                        String txt = "";
                        if(null != cs2)
                        {
                            while (cs2.moveToNext()) {
                                //得到data这一列 ，包括很多字段
                                String data1=cs2.getString(cs2.getColumnIndex("data1"));
                                //得到data中的类型
                                String type=cs2.getString(cs2.getColumnIndex("mimetype"));
                                String str=type.substring(type.indexOf("/")+1,type.length());//截取得到最后的类型
                                if("name".equals(str)){//匹配是否为联系人名字
                                    txt += data1;
                                }if("phone_v2".equals(str)){//匹配是否为电话
                                    txt += " "+data1;
                                }
                            }
                            cs2.close();
                        }
                        ContactModel contactModel = new ContactModel();
                        contactModel.setName(txt);//设置城市名称
                        contacts.add(contactModel);
                    }
                    cs.close();
                }
                idbContactIndex.setmSourceDatas(contacts)
                        .setHeaderViewCount(mHeaderAdapter.getHeaderViewCount())
                        .invalidate();
                mHeaderAdapter.notifyDataSetChanged();
                mDecoration.setmDatas(contacts);
            }
        }, 100);
    }
}
