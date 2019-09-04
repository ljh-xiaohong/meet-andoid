package com.yuejian.meet.activities.creation;

import com.yuejian.meet.activities.base.BaseTabActivity;
import com.yuejian.meet.framents.creation.SelectTemplateFragment;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author : g000gle
 * @time : 2019/5/27 14:47
 * @desc : 选择音乐相册模板
 */
public class MusicSelectTemplateActivity extends BaseTabActivity {

    String[] titles = new String[]{"推荐", "节日", "商务", "简约", "国风"};

    @Override
    protected void setTitleTxt() {
        mTitleView.setText("模板");
    }

    @Override
    protected void setFragmentList() {
        mFragmentList = new ArrayList<>();
        for (int i = 0; i < titles.length; i++) {

                SelectTemplateFragment fragment = SelectTemplateFragment.newInstance();
                mFragmentList.add(fragment);

        }
    }

    @Override
    protected void setTabTitleList() {
        mTabTitleList = new ArrayList<>();
        mTabTitleList.addAll(Arrays.asList(titles));
    }

    @Override
    protected void setChildCount() {
        mPageCount = titles.length;
    }
}
