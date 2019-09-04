package com.yuejian.meet.activities.mine;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.yuejian.meet.R;
import com.yuejian.meet.activities.base.BaseActivity;
import com.yuejian.meet.framents.mine.EssayDeliveredFragment;
import com.yuejian.meet.framents.mine.EssayExamineFragment;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by zh02 on 2017/8/24.
 */

public class MyEssayActivity extends BaseActivity {
    @Bind(R.id.my_essay_tab)
    TabLayout tabLayout;
    @Bind(R.id.my_essay_vp)
    ViewPager viewPager;

    private String[] titles ;
    private ArrayList<Fragment> fragments = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_essay);
        setTitleText(getString(R.string.mine_My_article));
        titles=new String[]{getString(R.string.essay_text_1),getString(R.string.verify_center_text)};
        findViewById(R.id.titlebar_create_essy).setVisibility(View.VISIBLE);
        fragments.add(new EssayDeliveredFragment());
        fragments.add(new EssayExamineFragment());
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setAdapter(new CommFragmentPageAdapter(getSupportFragmentManager()));
        viewPager.setOffscreenPageLimit(1);
        viewPager.setCurrentItem(0);
    }

    @OnClick({R.id.titlebar_create_essy})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.titlebar_create_essy:
                showMenuListDialog();
                break;
        }
    }

    private void showMenuListDialog() {
        View inflate = View.inflate(mContext, R.layout.layout_essay_menu_list, null);
        final AlertDialog dialog = new AlertDialog
                .Builder(mContext)
                .setView(inflate).create();
        TextView mrl = (TextView) inflate.findViewById(R.id.mrl);
        mrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent i = new Intent(mContext, CreateArticleActivity.class);
                i.putExtra("create_type", 2);
                startActivity(i);
            }
        });
        TextView xmqy = (TextView) inflate.findViewById(R.id.xmqy);
        xmqy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent i = new Intent(mContext, CreateArticleActivity.class);
                i.putExtra("create_type", 1);
                startActivity(i);
            }
        });
        TextView sj = (TextView) inflate.findViewById(R.id.sj);
        sj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent i = new Intent(mContext, CreateArticleActivity.class);
                i.putExtra("create_type", 3);
                startActivity(i);
            }
        });
        dialog.show();
    }

    class CommFragmentPageAdapter extends FragmentStatePagerAdapter {
        public CommFragmentPageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int pos) {
            return fragments.get(pos);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }

        @Override
        public int getItemPosition(Object object) {
            return PagerAdapter.POSITION_NONE;
        }
    }
}
