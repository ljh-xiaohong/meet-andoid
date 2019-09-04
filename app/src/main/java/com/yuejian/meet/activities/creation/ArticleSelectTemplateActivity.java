package com.yuejian.meet.activities.creation;

import com.yuejian.meet.activities.base.BaseTabActivity;
import com.yuejian.meet.framents.creation.SelectTemplateFragment;

import java.util.ArrayList;

/**
 * @author : g000gle
 * @time : 2019/5/27 11:44
 * @desc : 选择文章模板 (废弃)
 */
public class ArticleSelectTemplateActivity extends BaseTabActivity {

    @Override
    protected void setTitleTxt() {
        mTitleView.setText("文章模板");
    }

    @Override
    protected void setFragmentList() {
        mFragmentList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            SelectTemplateFragment fragment = SelectTemplateFragment.newInstance();
            mFragmentList.add(fragment);
        }
    }

    @Override
    protected void setTabTitleList() {
        mTabTitleList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            String title = "Title " + i;
            mTabTitleList.add(title);
        }
    }

    @Override
    protected void setChildCount() {
        mPageCount = 10;
    }

}
