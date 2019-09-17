package com.yuejian.meet.widgets.springview;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.yuejian.meet.R;
import com.yuejian.meet.widgets.container.BaseHeader;


/**
 * Created by Administrator on 2016/3/21.
 */
public class DefaultHeader extends BaseHeader {
    private Context context;
    private int rotationSrc;
    private int arrowSrc;

    private long freshTime;

    private final int ROTATE_ANIM_DURATION = 180;
    private RotateAnimation mRotateUpAnim;
    private RotateAnimation mRotateDownAnim;

    private TextView headerTitle;
    private TextView headerTime;
    private ImageView headerArrow;
    private ProgressBar headerProgressbar;

    public DefaultHeader(Context context) {
        this(context, R.drawable.progress_small, R.mipmap.ic_pull_icon_big);
        this.context = context;
    }

    public DefaultHeader(Context context, int rotationSrc, int arrowSrc) {
        this.context = context;
        this.rotationSrc = rotationSrc;
        this.arrowSrc = arrowSrc;

        mRotateUpAnim = new RotateAnimation(0.0f, -180.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        mRotateUpAnim.setDuration(ROTATE_ANIM_DURATION);
        mRotateUpAnim.setFillAfter(true);
        mRotateDownAnim = new RotateAnimation(-180.0f, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        mRotateDownAnim.setDuration(ROTATE_ANIM_DURATION);
        mRotateDownAnim.setFillAfter(true);
    }

    @Override
    public int getDragLimitHeight(View rootView) {
        return rootView.getMeasuredHeight() / 2;
    }

    @Override
    public View getView(LayoutInflater inflater, ViewGroup viewGroup) {
        View view = inflater.inflate(R.layout.default_header, viewGroup, true);
        headerTitle = (TextView) view.findViewById(R.id.default_header_title);
        headerTime = (TextView) view.findViewById(R.id.default_header_time);
        headerArrow = (ImageView) view.findViewById(R.id.default_header_arrow);
        headerProgressbar = (ProgressBar) view.findViewById(R.id.default_header_progressbar);
//        headerProgressbar.setIndeterminateDrawable(ContextCompat.getDrawable(context, rotationSrc));
        headerArrow.setImageResource(arrowSrc);
        return view;
    }

    @Override
    public void onPreDrag(View rootView) {
        if (freshTime == 0) {
            freshTime = System.currentTimeMillis();
        } else {
            int m = (int) ((System.currentTimeMillis() - freshTime) / 1000 / 60);
            if (m >= 1 && m < 60) {
                headerTime.setText(m + context.getString(R.string.oto_minute_ago));
            } else if (m >= 60) {
                int h = m / 60;
                headerTime.setText(h + context.getString(R.string.oto_hour_ago));
            } else if (m > 60 * 24) {
                int d = m / (60 * 24);
                headerTime.setText(d + context.getString(R.string.oto_day_ago));
            } else if (m == 0) {
                headerTime.setText(R.string.spring_text3);
            }
        }
    }

    @Override
    public void onDropAnim(View rootView, int dy) {
    }

    @Override
    public void onLimitDes(View rootView, boolean upORdown) {
        if (!upORdown) {
            headerTitle.setText(R.string.spring_text1);
            if (headerArrow.getVisibility() == View.VISIBLE)
                headerArrow.startAnimation(mRotateUpAnim);
        } else {
            headerTitle.setText(R.string.spring_text);
            if (headerArrow.getVisibility() == View.VISIBLE)
                headerArrow.startAnimation(mRotateDownAnim);
        }
    }

    @Override
    public void onStartAnim() {
        freshTime = System.currentTimeMillis();
        headerTitle.setText(R.string.spring_text2);
        headerArrow.setVisibility(View.INVISIBLE);
        headerArrow.clearAnimation();
        headerProgressbar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onFinishAnim() {
        headerArrow.setVisibility(View.VISIBLE);
        headerProgressbar.setVisibility(View.INVISIBLE);
    }
}