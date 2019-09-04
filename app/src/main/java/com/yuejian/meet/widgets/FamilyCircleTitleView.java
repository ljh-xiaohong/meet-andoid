package com.yuejian.meet.widgets;

import android.content.Context;
import android.graphics.Paint;
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
import com.yuejian.meet.utils.StringUtils;
import com.yuejian.meet.utils.TextUtil;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author : g000gle
 * @time : 2019/05/09 16:00
 * @desc : 首页 - 家圈 - 顶部标题栏
 */
public class FamilyCircleTitleView extends LinearLayout {

    private Context mContext;
    @Bind(R.id.iv_family_circle_title_left_btn)
    ImageView leftBtn;
    @Bind(R.id.tv_family_circle_title_right_btn)
    TextView rightBtn;
    @Bind(R.id.tv_title_one)
    TextView mTitleOne;
    @Bind(R.id.tv_title_two)
    TextView mTitleTwo;
    @Bind(R.id.tv_title_three)
    TextView mTitleThree;
    @Bind(R.id.title_underline_red_one)
    View mUnderLineOne;
    @Bind(R.id.title_underline_red_two)
    View mUnderLineTwo;
    @Bind(R.id.title_underline_red_three)
    View mUnderLineThree;

    OnTitleViewClickListener mOnTitleViewClickListener;

    public FamilyCircleTitleView(Context context) {
        this(context, null);
    }

    public FamilyCircleTitleView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FamilyCircleTitleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initView();
    }

    private void initView() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.widget_family_circle_title_view, this, true);
        ButterKnife.bind(this, view);

        mTitleOne.setOnClickListener(v -> mOnTitleViewClickListener.onTitleViewClick(0));
        mTitleTwo.setOnClickListener(v -> mOnTitleViewClickListener.onTitleViewClick(1));
        mTitleThree.setOnClickListener(v -> mOnTitleViewClickListener.onTitleViewClick(2));
    }

    public void setOnTitleViewClickListener(OnTitleViewClickListener onTitleViewClickListener) {
        mOnTitleViewClickListener = onTitleViewClickListener;
    }

    public void setSelectedTitle(int position) {
        boolean isOne = false;
        boolean isTwo = false;
        boolean isThree = false;
        switch (position) {
            case 0:
                isOne = true;
                break;
            case 1:
                isTwo = true;
                break;
            case 2:
                isThree = true;
                break;
            default:
                isTwo = true;
                break;
        }
        mTitleOne.setTypeface(isOne ? Typeface.DEFAULT_BOLD : Typeface.DEFAULT);
        mTitleTwo.setTypeface(isTwo ? Typeface.DEFAULT_BOLD : Typeface.DEFAULT);
        mTitleThree.setTypeface(isThree ? Typeface.DEFAULT_BOLD : Typeface.DEFAULT);
        mUnderLineOne.setVisibility(isOne ? VISIBLE : INVISIBLE);
        mUnderLineTwo.setVisibility(isTwo ? VISIBLE : INVISIBLE);
        mUnderLineThree.setVisibility(isThree ? VISIBLE : INVISIBLE);
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
            rightBtn.setText(TextUtil.notNull(rightTitle));
            Drawable drawable = getResources().getDrawable(rightIconRes);// 找到资源图片
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());// 设置图片宽高
            rightBtn.setCompoundDrawables(drawable, null, null, null);
        }
    }


    public void setImageBtnClick(OnClickListener leftListener, OnClickListener rightListener) {
        leftBtn.setOnClickListener(leftListener);
        rightBtn.setOnClickListener(rightListener);
    }

    public interface OnTitleViewClickListener {
        void onTitleViewClick(int position);
    }
}
