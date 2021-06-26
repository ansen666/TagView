package com.ansen.tagview;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.ansen.shape.AnsenEditText;
import com.ansen.shape.AnsenTextView;
import com.ansen.tagview.module.Tag;
import com.ansen.tagview.util.DisplayHelper;
import com.google.android.material.internal.FlowLayout;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private FlowLayout flSelectTag,flAllTag;
    private AnsenEditText etTag;

    private List<Tag> selectTagList=new ArrayList<>();
    private List<Tag> allTagList=new ArrayList<>();

    private int paddingTop;
    private int paddingLeft;
    private int cornersRadius;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        paddingTop=DisplayHelper.dp2px(this,4);
        paddingLeft=DisplayHelper.dp2px(this,10);
        cornersRadius=DisplayHelper.dp2px(this,200);

        initDate();

        flSelectTag=findViewById(R.id.fl_select_tag);
        flAllTag=findViewById(R.id.fl_all_tag);

        for(int i=0;i<selectTagList.size();i++){
            Tag selectTag=selectTagList.get(i);
            flSelectTag.addView(getSelectTag(selectTag,i));
        }

        for(int i=0;i<allTagList.size();i++){
            Tag allTag=allTagList.get(i);
            flAllTag.addView(getAllTag(allTag,i));
        }

        flSelectTag.addView(getEditextTag());
    }

    public AnsenTextView getSelectTag(Tag tag,int index){
        AnsenTextView textView=new AnsenTextView(this);
        textView.setTag(tag.getName());
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP,12);

        textView.setPadding(paddingLeft,paddingTop,paddingLeft,paddingTop);
        textView.setCornersRadius(cornersRadius);

        int red=0xffFE6971;
        textView.setStrokeColor(red);
        textView.setStrokeWidth(DisplayHelper.dp2px(this,1));
        textView.setSolidColor(0xffffffff);
        textView.getShape().textColor=red;
        textView.setTextColor(red);
        textView.setText(tag.getName());
        textView.getShape().text=tag.getName();

        textView.getShape().selectStrokeColor=red;
        textView.getShape().selectStrokeWidth=DisplayHelper.dp2px(this,1);
        textView.getShape().selectSolidColor=red;
        textView.getShape().selectTextColor=0xffffffff;
        textView.getShape().selectText=tag.getName()+"x";

        textView.resetBackground();
        textView.setOnClickListener(selectTagOnClickListener);

        Log.i("ansen","getSelectTag 是否选中:"+textView.isSelected());
        return textView;
    }

    //选中标签点击
    private View.OnClickListener selectTagOnClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String name= (String) view.getTag();
            int index=getSelectTagIndexByName(name);
            if(index==-1){
                Log.i("ansen","selectTagOnClickListener 名字没找到");
                return ;
            }
            Tag selectTag=selectTagList.get(index);
            if(selectTag.isSelect()){//删除
                selectTagList.remove(index);
                flSelectTag.removeViewAt(index);

                changeAllTagSelect(selectTag.getName(),false);
            }else{
                resetSelect();//重置选中记录
                selectTag.setSelect(true);
                flSelectTag.getChildAt(index).setSelected(true);
            }
        }
    };

    private int getSelectTagIndexByName(String name){
        for(int i=0;i<selectTagList.size();i++){
            Tag selectTag=selectTagList.get(i);
            if(TextUtils.equals(name,selectTag.getName())){
                return i;
            }
        }
        return -1;
    }

    //所有标签点击
    private View.OnClickListener allTagOnClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            resetSelect();

            int index= (int) view.getTag();
            Tag allTag=allTagList.get(index);
            if(allTag.isSelect()){//之前是选中状态 那从上面删除
                deleteSelectTag(allTag.getName());
            }else{//
                addSelectTag(allTag.getName());
            }
            allTag.setSelect(!allTag.isSelect());
            flAllTag.getChildAt(index).setSelected(allTag.isSelect());
        }
    };

    public AnsenTextView getAllTag(Tag tag,int index){
        AnsenTextView textView=new AnsenTextView(this);
        textView.setTag(index);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP,12);

        textView.setPadding(paddingLeft,paddingTop,paddingLeft,paddingTop);
        textView.setCornersRadius(cornersRadius);
        textView.setText(tag.getName());

        int red=0xffFE6971;
        textView.setStrokeColor(0xffffffff);
        textView.setStrokeWidth(DisplayHelper.dp2px(this,1));
        textView.setSolidColor(0xffffffff);

        textView.getShape().textColor=0xff323232;
        textView.setTextColor(0xff323232);

        textView.getShape().selectStrokeColor=red;
        textView.getShape().selectStrokeWidth=DisplayHelper.dp2px(this,1);
        textView.getShape().selectSolidColor=0xffffffff;
        textView.getShape().selectTextColor=red;

        boolean isSelect=tagIsSelect(tag.getName());
        textView.setSelected(isSelect);
        tag.setSelect(isSelect);
        Log.i("ansen","getAllTag isSelect:"+isSelect+" 标签名称:"+tag.getName());
        textView.resetBackground();
        textView.setOnClickListener(allTagOnClickListener);
        return textView;
    }

    private boolean tagIsSelect(String name){
        for(int i=0;i<selectTagList.size();i++){
            Tag selectTag=selectTagList.get(i);
            if(TextUtils.equals(name,selectTag.getName())){
                return true;
            }
        }
        return false;
    }

    public AnsenEditText getEditextTag(){
        etTag=new AnsenEditText(this);
        etTag.setHint("+新增标签");
        etTag.setHintTextColor(0xFFB2B2B2);
        etTag.setStrokeColor(0xffCECECE);
        etTag.setTextSize(TypedValue.COMPLEX_UNIT_SP,12);
        etTag.setStrokeWidth(DisplayHelper.dp2px(this,1));
        etTag.getShape().dashWidth=DisplayHelper.dp2px(this,4);
        etTag.getShape().dashGap=DisplayHelper.dp2px(this,2);
        etTag.setCornersRadius(cornersRadius);
        etTag.setTextColor(0xff333333);
        etTag.setPadding(paddingLeft,paddingTop,paddingLeft,paddingTop);
        etTag.setMaxLines(1);
        etTag.setSingleLine(true);
        etTag.setFilters(new InputFilter[]{new InputFilter.LengthFilter(17)});
        etTag.resetBackground();
        etTag.setOnKeyListener(onKeyListener);
        etTag.addTextChangedListener(textWatcher);
        return etTag;
    }

    private View.OnKeyListener onKeyListener=new View.OnKeyListener() {
        @Override
        public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
            if(keyEvent.getAction()!= KeyEvent.ACTION_UP){
                return false;
            }

            Log.i("ansen","keyCode:"+keyCode);
            if(keyCode==KeyEvent.KEYCODE_ENTER){//确定按钮
                enter();
                return true;
            }else if(keyCode==KeyEvent.KEYCODE_DEL){//删除
                delete();
            }
            return false;
        }
    };

    private void delete(){
        String name=etTag.getText().toString().trim();
        if(!TextUtils.isEmpty(name)){
            Log.i("ansen","输入框有内容不处理");
            return ;
        }

        if(selectTagList==null||selectTagList.size()<=0){
            Log.i("ansen","没有选中的标签不处理");
            return ;
        }

        for(int i=selectTagList.size()-1;i>=0;i--){
            Tag selectTag=selectTagList.get(i);
//            Log.i("ansen","delete 是否选中:"+selectTag.isSelect()+" "+selectTag.getName());
            if(selectTag.isSelect()&&i!=selectTagList.size()-1){//已经选中 但是不是最后一个
                selectTag.setSelect(false);
                flSelectTag.getChildAt(i).setSelected(false);
            }
        }

        int lastIndex=selectTagList.size()-1;
        Tag lastTag=selectTagList.get(lastIndex);//取最后一个
        if(lastTag.isSelect()){
            selectTagList.remove(lastIndex);
            flSelectTag.removeViewAt(lastIndex);

            changeAllTagSelect(lastTag.getName(),false);//存在就取消选中
        }else{
            lastTag.setSelect(true);
            flSelectTag.getChildAt(lastIndex).setSelected(true);
        }
    }

    private void deleteSelectTag(String name){
        for(int i=selectTagList.size()-1;i>=0;i--){
            Tag selectTag=selectTagList.get(i);
            if(TextUtils.equals(name,selectTag.getName())){//已经存在 删除之前的
                selectTagList.remove(i);
                flSelectTag.removeViewAt(i);
            }
        }
    }

    private void enter(){
        String name=etTag.getText().toString().trim();
        if(TextUtils.isEmpty(name)){
            return ;
        }

        deleteSelectTag(name);
        addSelectTag(name);

        etTag.setText("");
        changeAllTagSelect(name,true);//存在就选中
    }

    private void addSelectTag(String name){
        Tag tag=new Tag();
        tag.setName(name);
        flSelectTag.addView(getSelectTag(tag,selectTagList.size()),flSelectTag.getChildCount()-1);
        selectTagList.add(tag);
    }

    /**
     * 改变所有标签的选中状态
     * @param name
     * @param select
     */
    private void changeAllTagSelect(String name,boolean select){
        Log.i("ansen","changeAllTagSelect name:"+name);
        for(int i=allTagList.size()-1;i>=0;i--){
            Tag allTag=allTagList.get(i);
            if(TextUtils.equals(allTag.getName(),name)){//新增的标签在所有标签里面存在
                allTag.setSelect(select);
                flAllTag.getChildAt(i).setSelected(select);
            }
        }
    }

    private TextWatcher textWatcher=new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
            MainActivity.this.onTextChanged(charSequence);
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    private void onTextChanged(CharSequence charSequence){
        Log.i("ansen","charSequence:"+charSequence);

        if(!TextUtils.isEmpty(charSequence)){//
            resetSelect();
        }
    }

    private void resetSelect(){
        for(int i=selectTagList.size()-1;i>=0;i--){
            Tag selectTag=selectTagList.get(i);
            if(selectTag.isSelect()){//有选中记录 重置
                selectTag.setSelect(false);
                flSelectTag.getChildAt(i).setSelected(false);
            }
        }
    }

    private void initDate(){
        selectTagList.add(new Tag("性感"));
        selectTagList.add(new Tag("白富美"));
        selectTagList.add(new Tag("萝莉"));
        selectTagList.add(new Tag("御姐"));
        selectTagList.add(new Tag("网红"));
        selectTagList.add(new Tag("女汉子"));
        selectTagList.add(new Tag("女神"));
        selectTagList.add(new Tag("女强人"));
        selectTagList.add(new Tag("傻白甜"));

        allTagList.add(new Tag("性感"));
        allTagList.add(new Tag("白富美"));
        allTagList.add(new Tag("萝莉"));
        allTagList.add(new Tag("御姐"));
        allTagList.add(new Tag("网红"));
        allTagList.add(new Tag("女汉子"));
        allTagList.add(new Tag("女神"));
        allTagList.add(new Tag("女强人"));
        allTagList.add(new Tag("傻白甜"));
        allTagList.add(new Tag("屌丝"));
        allTagList.add(new Tag("帅哥"));
        allTagList.add(new Tag("高富帅"));
        allTagList.add(new Tag("人傻钱多"));
    }
}