package com.example.hmyd.mytestandroid_studio.model;

import com.mcxtzhang.indexlib.IndexBar.bean.BaseIndexPinyinBean;

/**
 * @author kongdy
 * @date 2017/10/13 14:04
 * @describe TODO
 **/

public class ContactModel extends BaseIndexPinyinBean {
    private String name;
    private boolean isTop;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isTop() {
        return isTop;
    }

    public void setTop(boolean top) {
        isTop = top;
    }

    @Override
    public String getTarget() {
        return name;
    }

    @Override
    public boolean isNeedToPinyin() {
        return !isTop;
    }

    @Override
    public boolean isShowSuspension() {
        return !isTop;
    }
}
