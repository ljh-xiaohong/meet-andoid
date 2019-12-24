package com.yuejian.meet.widgets;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yuejian.meet.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author : g000gle
 * @time : 2019/05/09 16:00
 * @desc : 首页 - 家圈 - 顶部标题栏
 */
public class NewMessageTitleView extends LinearLayout {

    @Bind(R.id.iv_family_circle_title_left_btn_point)
    View ivFamilyCircleTitleLeftBtnPoint;
    @Bind(R.id.tv_title_one_point)
    View tvTitleOnePoint;
    private Context mContext;
    @Bind(R.id.iv_family_circle_title_left_btn)
    ImageView leftBtn;
    @Bind(R.id.tv_family_circle_title_right_btn)
    TextView rightBtn;
    @Bind(R.id.tv_title_one)
    TextView mTitleOne;
    @Bind(R.id.tv_title_two)
    TextView mTitleTwo;
    @Bind(R.id.title_underline_red_one)
    View mUnderLineOne;
    @Bind(R.id.title_underline_red_two)
    View mUnderLineTwo;

    OnTitleViewClickListener mOnTitleViewClickListener;
    public interface OnTitleViewClickListener {
        void onTitleViewClick(int position);
    }
    public void setOnTitleViewClickListener(OnTitleViewClickListener onTitleViewClickListener) {
        mOnTitleViewClickListener = onTitleViewClickListener;
    }
    public NewMessageTitleView(Context context) {
        this(context, null);
    }

    public NewMessageTitleView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NewMessageTitleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initView();
    }

    private void initView() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.new_message_title, this, true);
        ButterKnife.bind(this, view);
        mTitleOne.setOnClickListener(v -> mOnTitleViewClickListener.onTitleViewClick(0));
        mTitleTwo.setOnClickListener(v -> mOnTitleViewClickListener.onTitleViewClick(1));
    }


    public void setSelectedTitle(int position) {
        boolean isOne = false;
        boolean isTwo = false;
        switch (position) {
            case 0:
                isOne = true;
                break;
            case 1:
                isTwo = true;
                break;
            default:
                isTwo = true;
                break;
        }
        mTitleOne.setTypeface(isOne ? Typeface.DEFAULT_BOLD : Typeface.DEFAULT);
        mTitleTwo.setTypeface(isTwo ? Typeface.DEFAULT_BOLD : Typeface.DEFAULT);
        mUnderLineOne.setVisibility(isOne ? VISIBLE : INVISIBLE);
        mUnderLineTwo.setVisibility(isTwo ? VISIBLE : INVISIBLE);
    }
    public void setPoint(boolean a,boolean b){
        if (a){
            ivFamilyCircleTitleLeftBtnPoint.setVisibility(VISIBLE);
        }else {
            ivFamilyCircleTitleLeftBtnPoint.setVisibility(GONE);
        }
        if (b){
            tvTitleOnePoint.setVisibility(VISIBLE);
        }else {
            tvTitleOnePoint.setVisibility(GONE);
        }
    }
    public void setImageBtnIcon(@DrawableRes int leftIconRes, @DrawableRes int rightIconRes, String rightTitle) {
        if (leftIconRes == -1) {
            leftBtn.setVisibility(INVISIBLE);
        } else {
            leftBtn.setVisibility(VISIBLE);
            leftBtn.setImageDrawable(getResources().getDrawable(leftIconRes));
        }
        if (rightIconRes == -1) {
            rightBtn.setVisibility(INVISIBLE);
        } else {
            rightBtn.setVisibility(VISIBLE);

            //  rightBtn.setImageDrawable(getResources().getDrawable(rightIconRes));
//            rightBtn.setText(TextUtil.notNull(rightTitle));
            Drawable drawable = getResources().getDrawable(rightIconRes);// 找到资源图片
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());// 设置图片宽高
            rightBtn.setCompoundDrawables(drawable, null, null, null);
        }
    }


    public void setImageBtnClick(OnClickListener leftListener, OnClickListener rightListener) {
        leftBtn.setOnClickListener(leftListener);
        rightBtn.setOnClickListener(rightListener);
    }

}
