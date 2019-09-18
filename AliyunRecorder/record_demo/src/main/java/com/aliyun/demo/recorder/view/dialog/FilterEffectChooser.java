package com.aliyun.demo.recorder.view.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.widget.RadioGroup;

import com.aliyun.demo.R;
import com.aliyun.demo.recorder.view.effects.face.AlivcBeautyFaceFragment;
import com.aliyun.demo.recorder.view.effects.filter.AlivcFilterChooseFragment;
import com.aliyun.demo.recorder.view.effects.filter.EffectInfo;
import com.aliyun.demo.recorder.view.effects.filter.interfaces.OnFilterItemClickListener;
import com.aliyun.svideo.base.widget.beauty.BeautyParams;
import com.aliyun.svideo.base.widget.beauty.enums.BeautyLevel;
import com.aliyun.svideo.base.widget.beauty.listener.OnBeautyDetailClickListener;
import com.aliyun.svideo.base.widget.beauty.listener.OnBeautyFaceItemSeletedListener;
import com.aliyun.svideo.base.widget.beauty.listener.OnBeautyModeChangeListener;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zsy_18 data:2019/4/22
 */
public class FilterEffectChooser extends BasePageChooser {
    /**
     * 滤镜fragment
     */
    private AlivcFilterChooseFragment filterChooseFragment;

     /**
     * 滤镜item点击listener
     */
    private OnFilterItemClickListener onFilterItemClickListener;


    /**
     * 美颜fragment；
     */
    private AlivcBeautyFaceFragment beautyFaceFragment;

    /**
     * 美颜微调点击listener
     */
    private OnBeautyDetailClickListener onBeautyFaceDetailClickListener;

    /**
     * 美颜item选中listener
     */
    private OnBeautyFaceItemSeletedListener onItemSeletedListener;

    /**
     * 美颜模式切换listener (普通or高级)
     */
    private OnBeautyModeChangeListener onBeautyModeChangeListener;


    /**
     * 美颜参数
     */
    private BeautyParams beautyParams;


    private int filterPosition;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //适配有底部导航栏的手机，在full的style下会盖住部分视图的bug
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.QUDemoFullStyle);
    }

    @Override
    public List<Fragment> createPagerFragmentList() {
        List<Fragment> fragments = new ArrayList<>();
        filterChooseFragment = new AlivcFilterChooseFragment();
        beautyFaceFragment = new AlivcBeautyFaceFragment();
        initFilter();
        initBeautyFace();

        fragments.add(filterChooseFragment);
        fragments.add(beautyFaceFragment);
        return fragments;
    }

    @Override
    public void onStart() {
        super.onStart();
        filterChooseFragment.setFilterPosition(filterPosition);

    }

    private void initFilter() {
        filterChooseFragment.setOnFilterItemClickListener(new OnFilterItemClickListener() {
            @Override
            public void onItemClick(EffectInfo effectInfo, int index) {
                if (onFilterItemClickListener != null) {
                    onFilterItemClickListener.onItemClick(effectInfo, index);
                }
            }
        });
    }

    private void initBeautyFace(){
        beautyFaceFragment.setBeautyParams(beautyParams);

        // 档位选择
        beautyFaceFragment.setOnBeautyFaceItemSeletedListener(new OnBeautyFaceItemSeletedListener() {
            @Override
            public void onNormalSelected(int postion, BeautyLevel beautyLevel) {
                if (onItemSeletedListener != null) {
                    onItemSeletedListener.onNormalSelected(postion, beautyLevel);
                }
            }

            @Override
            public void onAdvancedSelected(int postion, BeautyLevel beautyLevel) {
                if (onItemSeletedListener != null) {
                    onItemSeletedListener.onAdvancedSelected(postion, beautyLevel);
                }
            }
        });

        // 高级or普通模式切换
        beautyFaceFragment.setOnBeautyModeChangeListener(new OnBeautyModeChangeListener() {
            @Override
            public void onModeChange(RadioGroup group, int checkedId) {
                if (onBeautyModeChangeListener != null) {
                    onBeautyModeChangeListener.onModeChange(group, checkedId);
                }
            }
        });

        // 高级详情
        beautyFaceFragment.setOnBeautyDetailClickListener(new OnBeautyDetailClickListener() {
            @Override
            public void onDetailClick() {
                if (onBeautyFaceDetailClickListener != null) {
                    onBeautyFaceDetailClickListener.onDetailClick();
                }
            }
        });
    }

    /**
     * 设置美颜美肌参数
     * @param beautyParams 美颜美肌参数
     */
    public void setBeautyParams(BeautyParams beautyParams) {
        this.beautyParams = beautyParams;
    }

    /**
     * 设置滤镜item点击listener
     *
     * @param listener OnFilterItemClickListener
     */
    public void setOnFilterItemClickListener(OnFilterItemClickListener listener) {
        this.onFilterItemClickListener = listener;
    }

    public void setFilterPosition(int filterPosition) {
        this.filterPosition = filterPosition;
    }

    /**
     * 设置美颜item点击listener
     * @param listener OnBeautyFaceItemSeletedListener
     */
    public void setOnBeautyFaceItemSeletedListener(OnBeautyFaceItemSeletedListener listener) {
        this.onItemSeletedListener = listener;
    }
}
