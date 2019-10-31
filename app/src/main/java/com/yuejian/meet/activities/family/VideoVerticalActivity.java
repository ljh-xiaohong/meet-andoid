package com.yuejian.meet.activities.family;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.mcxiaoke.bus.Bus;
import com.yuejian.meet.R;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.api.http.ApiImp;
import com.yuejian.meet.bean.SlideIDEntity;
import com.yuejian.meet.framents.family.VideoPlayFragment;
import com.yuejian.meet.widgets.CirculatoryViewPager.CirculatoryFragmentStatePagerAdapter;
import com.yuejian.meet.widgets.CirculatoryViewPager.CirculatoryViewPager;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

public class VideoVerticalActivity extends AppCompatActivity implements VideoPlayFragment.OnchangeDataListener {


    private Context mContext;

    private VideoPagerAdapter adapter;

    WeakReference<VideoVerticalActivity> reference;

    private List<Fragment> Ids;

    @Bind(R.id.video_vertical_viewpager)
    CirculatoryViewPager verticalViewPager;

    private ApiImp apiImp = new ApiImp();

    private boolean isCancel = false;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        reference = new WeakReference<>(this);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        Bus.getDefault().register(this);
        setContentView(R.layout.video_player);
        ButterKnife.bind(this);
        init();
        Ids = new ArrayList<>();
        getDataFromNet();
    }

    private boolean checkIsLife() {
        return reference == null || reference.get() == null || reference.get().isFinishing();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Bus.getDefault().unregister(this);
    }

    private void getDataFromNet() {
        Map<String, Object> params = new HashMap<>();
        params.put("customerId", getIntent().getStringExtra("VideoActivity.customerId"));
        params.put("cId", getIntent().getStringExtra("VideoActivity.contentId"));
        apiImp.findSlideIds(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                if(checkIsLife())return;
                if (data == null) return;
                if (JSON.parseObject(data) == null || !JSON.parseObject(data).getString("code").equals("0"))
                    return;
                List<SlideIDEntity> datas = JSONArray.parseArray(JSON.parseObject(data).getString("data"), SlideIDEntity.class);
                if (datas == null) return;
                for (int i = 0; i < datas.size(); i++) {
                    VideoPlayFragment fragment = new VideoPlayFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("crID", datas.get(i).getId());
//                    bundle.putInt("position", i);
                    fragment.setArguments(bundle);
                    fragment.setOnchangeDataListener(VideoVerticalActivity.this);
                    Ids.add(fragment);
                }

                adapter = new VideoPagerAdapter(getSupportFragmentManager(), Ids);
//                verticalViewPager.setLoop(Ids.size());
                verticalViewPager.setAdapter(adapter);
                verticalViewPager.setOffscreenPageLimit(5);
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {

            }
        });
    }


    public static void startActivity(Context context, String contentId, String customerId, boolean SCREEN_MATCH) {
        Intent intent = new Intent(context, VideoVerticalActivity.class);
        intent.putExtra("VideoActivity.contentId", contentId);
        intent.putExtra("VideoActivity.customerId", customerId);
        intent.putExtra("VideoActivity.SCREEN_MATCH", SCREEN_MATCH);
        context.startActivity(intent);
    }

    public static void startActivityForResult(Activity context, String contentId, String customerId, int requesCode, boolean SCREEN_MATCH) {
        Intent intent = new Intent(context, VideoVerticalActivity.class);
        intent.putExtra("VideoActivity.contentId", contentId);
        intent.putExtra("VideoActivity.customerId", customerId);
        intent.putExtra("VideoActivity.SCREEN_MATCH", SCREEN_MATCH);
        intent.putExtra("VideoActivity.requesCode", requesCode);
        context.startActivityForResult(intent, requesCode);
    }

    private void init() {

////        verticalViewPager.setAdapter(adapter);
        verticalViewPager.addOnPageChangeListener(new CirculatoryViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
//                Toast.makeText(mContext, "" + position, Toast.LENGTH_SHORT).show();
//                fragmentPosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    @Override
    public void notInterested(Fragment fragment) {
        if(checkIsLife())return;
        adapter.removeFragment(fragment);
         isCancel = true;
        if (adapter.getFragmentSize() == 0) {
            if (isCancel) {
                setResult(RESULT_OK);
            }

            finish();
        }
    }

    @Override
    public void cancel(Fragment fragment) {
        if(checkIsLife())return;
        adapter.removeFragment(fragment);
        isCancel = true;
        if (adapter.getFragmentSize() == 0) {
            if (isCancel) {
                setResult(RESULT_OK);
            }
            finish();
        }
    }

    @Override
    public void finishFragment() {
        if(checkIsLife())return;
        if (isCancel) {
            setResult(RESULT_OK);
        }
        finish();
    }

    @Override
    public void onBackPressed() {
        if(checkIsLife())return; if (isCancel) {
            setResult(RESULT_OK);
        }
        super.onBackPressed();

    }

    class VideoPagerAdapter extends CirculatoryFragmentStatePagerAdapter {

        private List<Fragment> videos;


        public void removeFragment(Object postion) {
            videos.remove(postion);
            notifyDataSetChanged();
        }

        public int getFragmentSize() {
            return videos.size();
        }


        public VideoPagerAdapter(FragmentManager fm, List<Fragment> videos) {
            super(fm);
            this.videos = videos;
        }

        @Override
        public int getCount() {
            return videos.size();
        }

        @Override
        public Fragment getItem(int position) {

            return videos.get(position);
        }

        @Override
        public int getItemPosition(@NonNull Object object) {
            return FragmentStatePagerAdapter.POSITION_NONE;
        }

//        @Override
//        public void destroyItem(ViewGroup container, int position, Object object) {
//            container.removeView((View) object);
//            ;
//        }
    }


}
