package com.ansen.tagview.module;

import android.util.Log;

public class Tag{
    private String id;
    private String name;//标签名称

    private boolean select;//选中状态

    public Tag(){

    }

    public Tag(String name) {
        this.name = name;
    }

    public Tag(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;

//        Log.i("ansen","设置选中:"+select+" 名字:"+name);
    }
}
