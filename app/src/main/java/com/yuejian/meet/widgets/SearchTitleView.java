package com.yuejian.meet.widgets;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yuejian.meet.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author : g000gle
 * @time : 2019/05/09 16:00
 * @desc : 搜索- 顶部标题栏
 */
public class SearchTitleView extends LinearLayout {

    private Context mContext;
    @Bind(R.id.tv_title_one)
    TextView mTitleOne;
    @Bind(R.id.tv_title_two)
    TextView mTitleTwo;
    @Bind(R.id.tv_title_three)
    TextView mTitleThree;
    @Bind(R.id.tv_title_four)
    TextView mTitleFour;
    @Bind(R.id.tv_title_five)
    TextView mTitleFive;
    @Bind(R.id.title_underline_red_one)
    View mUnderLineOne;
    @Bind(R.id.title_underline_red_two)
    View mUnderLineTwo;
    @Bind(R.id.title_underline_red_three)
    View mUnderLineThree;
    @Bind(R.id.title_underline_red_four)
    View mUnderlineFour;
    @Bind(R.id.title_underline_red_five)
    View mUnderlineFive;

    OnTitleViewClickListener mOnTitleViewClickListener;

    public SearchTitleView(Context context) {
        this(context, null);
    }

    public SearchTitleView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SearchTitleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initView();
    }

    private void initView() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.search_title_view, this, true);
        ButterKnife.bind(this, view);
        mTitleOne.setOnClickListener(v -> mOnTitleViewClickListener.onTitleViewClick(0));
        mTitleTwo.setOnClickListener(v -> mOnTitleViewClickListener.onTitleViewClick(1));
        mTitleThree.setOnClickListener(v -> mOnTitleViewClickListener.onTitleViewClick(2));
        mTitleFour.setOnClickListener(v -> mOnTitleViewClickListener.onTitleViewClick(3));
        mTitleFive.setOnClickListener(v -> mOnTitleViewClickListener.onTitleViewClick(4));
    }

    public void setOnTitleViewClickListener(OnTitleViewClickListener onTitleViewClickListener) {
        mOnTitleViewClickListener = onTitleViewClickListener;
    }

    public void setSelectedTitle(int position) {
        boolean isOne = false;
        boolean isTwo = false;
        boolean isThree = false;
        boolean isFour = false;
        boolean isFive = false;
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
            case 3:
                isFour = true;
                break;
            case 4:
                isFive = true;
                break;
            default:
                isOne = true;
                break;
        }
        mTitleOne.setTypeface(isOne ? Typeface.DEFAULT_BOLD : Typeface.DEFAULT);
        mTitleTwo.setTypeface(isTwo ? Typeface.DEFAULT_BOLD : Typeface.DEFAULT);
        mTitleThree.setTypeface(isThree ? Typeface.DEFAULT_BOLD : Typeface.DEFAULT);
        mTitleFour.setTypeface(isFour ? Typeface.DEFAULT_BOLD : Typeface.DEFAULT);
        mTitleFive.setTypeface(isFive ? Typeface.DEFAULT_BOLD : Typeface.DEFAULT);
        mUnderLineOne.setVisibility(isOne ? VISIBLE : INVISIBLE);
        mUnderLineTwo.setVisibility(isTwo ? VISIBLE : INVISIBLE);
        mUnderLineThree.setVisibility(isThree ? VISIBLE : INVISIBLE);
        mUnderlineFour.setVisibility(isFour ? VISIBLE : INVISIBLE);
        mUnderlineFive.setVisibility(isFive ? VISIBLE : INVISIBLE);
    }

    public interface OnTitleViewClickListener {
        void onTitleViewClick(int position);
    }
}
