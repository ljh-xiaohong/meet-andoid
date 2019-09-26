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
public class CreationTitleView extends LinearLayout {

    @Bind(R.id.change)
    TextView change;
    private Context mContext;
//    @Bind(R.id.tv_title_one)
//    TextView mTitleOne;
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
    boolean isChange=false;
    public CreationTitleView(Context context) {
        this(context, null);
    }

    public CreationTitleView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CreationTitleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initView();
    }

    private void initView() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.creation_title_view, this, true);
        ButterKnife.bind(this, view);
//        mTitleOne.setOnClickListener(v -> mOnTitleViewClickListener.onTitleViewClick(0));
        mTitleTwo.setOnClickListener(v -> mOnTitleViewClickListener.onTitleViewClick(0));
        mTitleThree.setOnClickListener(v -> mOnTitleViewClickListener.onTitleViewClick(1));
        change.setOnClickListener(v -> {
            if (isChange){
                isChange=false;
                change.setText("竖版");
                mOnTitleViewClickListener.onTitleViewClick(3);
            }else {
                isChange=true;
                change.setText("横版");
                mOnTitleViewClickListener.onTitleViewClick(4);
            }

        });
    }

    public void setOnTitleViewClickListener(OnTitleViewClickListener onTitleViewClickListener) {
        mOnTitleViewClickListener = onTitleViewClickListener;
    }

    public void setSelectedTitle(int position) {
//        boolean isOne = false;
        boolean isTwo = false;
        boolean isThree = false;
        switch (position) {
            case 0:
//                isOne = true;
                isTwo = true;
                break;
            case 1:
//                isTwo = true;
                isThree = true;
                break;
//            case 2:
//                isThree = true;
//                break;
            default:
                isTwo = true;
                break;
        }
//        mTitleOne.setTypeface(isOne ? Typeface.DEFAULT_BOLD : Typeface.DEFAULT);
        mTitleTwo.setTypeface(isTwo ? Typeface.DEFAULT_BOLD : Typeface.DEFAULT);
        mTitleThree.setTypeface(isThree ? Typeface.DEFAULT_BOLD : Typeface.DEFAULT);
//        mUnderLineOne.setVisibility(isOne ? VISIBLE : INVISIBLE);
        mUnderLineTwo.setVisibility(isTwo ? VISIBLE : INVISIBLE);
        mUnderLineThree.setVisibility(isThree ? VISIBLE : INVISIBLE);
    }

    public interface OnTitleViewClickListener {
        void onTitleViewClick(int position);
    }
}
