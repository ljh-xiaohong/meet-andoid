package com.yuejian.meet.activities.message;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;
import android.widget.TextView;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.base.BaseActivity;
import com.yuejian.meet.adapters.MyFragmentPagerAdapter;
import com.yuejian.meet.framents.message.CommentZanFragment;
import com.yuejian.meet.widgets.CommentZanTitleView;
import java.util.ArrayList;
import butterknife.Bind;
import butterknife.ButterKnife;

public class CommentZanActivity extends BaseActivity implements ViewPager.OnPageChangeListener, CommentZanTitleView.OnTitleViewClickListener {
    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.address_book_title_view)
    CommentZanTitleView addressBookTitleView;
    @Bind(R.id.vp_contact)
    ViewPager vpContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comment_zan_activity);
        ButterKnife.bind(this);
        initView();
    }
    private CommentZanFragment mFriendFragment;
    private CommentZanFragment attentionFragment;
    private void initView() {
        ArrayList<Fragment> mFragmentList = new ArrayList<>();

        mFriendFragment = new CommentZanFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type",1);
        mFriendFragment.setArguments(bundle);
        mFragmentList.add(mFriendFragment);

        attentionFragment = new CommentZanFragment();
        Bundle bundle1 = new Bundle();
        bundle1.putInt("type",2);
        attentionFragment.setArguments(bundle1);
        mFragmentList.add(attentionFragment);

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
