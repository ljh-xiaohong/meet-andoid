package com.yuejian.meet.activities.message;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;


import com.yuejian.meet.R;
import com.yuejian.meet.activities.base.BaseActivity;
import com.yuejian.meet.adapters.MyFragmentPagerAdapter;
import com.yuejian.meet.framents.message.FansFragment;
import com.yuejian.meet.widgets.AddressBookTitleView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ContactActivity extends BaseActivity implements ViewPager.OnPageChangeListener, AddressBookTitleView.OnTitleViewClickListener {
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.vp_contact)
    ViewPager vpContact;
    @Bind(R.id.address_book_title_view)
    AddressBookTitleView addressBookTitleView;
    @Bind(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @Bind(R.id.back)
    ImageView back;
    private FansFragment mFriendFragment;
    private FansFragment attentionFragment;
    private FansFragment mutualPowderFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_activity_contacts);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        title.setText("通讯录");
        ArrayList<Fragment> mFragmentList = new ArrayList<>();

        mFriendFragment = new FansFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type",0);
        mFriendFragment.setArguments(bundle);
        mFragmentList.add(mFriendFragment);

        attentionFragment = new FansFragment();
        Bundle bundle1 = new Bundle();
        bundle1.putInt("type",1);
        attentionFragment.setArguments(bundle1);
        mFragmentList.add(attentionFragment);

        mutualPowderFragment = new FansFragment();
        Bundle bundle2 = new Bundle();
        bundle2.putInt("type",2);
        mutualPowderFragment.setArguments(bundle2);
        mFragmentList.add(mutualPowderFragment);

        setCurrentItem(0);
        MyFragmentPagerAdapter adapter = new MyFragmentPagerAdapter(this.getSupportFragmentManager(), mFragmentList);
        vpContact.setAdapter(adapter);
        vpContact.setOffscreenPageLimit(2);
        vpContact.addOnPageChangeListener(this);
        addressBookTitleView.setOnTitleViewClickListener(this);
        back.setOnClickListener(v -> finish());
    }

    /**
     * 切换页面
     *
     * @param position 分类角标
     */
    private void setCurrentItem(int position) {
        vpContact.setCurrentItem(position);
        addressBookTitleView.setSelectedTitle(position);
        if (position == 1) {
            //打开手势滑动
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        } else {
            //禁止手势滑动
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        }
    }

    @Override
    public void onTitleViewClick(int position) {
        switch (position) {
            case 0:
                setCurrentItem(0);
                break;
            case 1:
                setCurrentItem(1);
                break;
            case 2:
                setCurrentItem(2);
                break;
            default:
                break;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        switch (position) {
            case 0:
                setCurrentItem(0);
                break;
            case 1:
                setCurrentItem(1);
                break;
            case 2:
                setCurrentItem(2);
                break;
            default:
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
