package com.yuejian.meet.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.netease.nim.uikit.app.AppConfig;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.adapter.ReleasePicAdapter;
import com.yuejian.meet.activities.custom.view.RoundAngleImageView;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.api.http.ApiImp;
import com.yuejian.meet.bean.CommodityBean;
import com.yuejian.meet.bean.DynamicPrivatePicBean;
import com.yuejian.meet.bean.FamilyFollowEntity;
import com.yuejian.meet.bean.Image;
import com.yuejian.meet.bean.ZanBean;
import com.yuejian.meet.utils.CommonUtil;
import com.yuejian.meet.utils.FolderTextView;
import com.yuejian.meet.utils.ScreenUtils;
import com.yuejian.meet.utils.TimeUtils;
import com.yuejian.meet.utils.Utils;
import com.yuejian.meet.widgets.CircleImageView;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author : g000gle
 * @time : 2019/5/14 19:20
 * @desc : 家圈关注列表适配器
 */
public class CommodityListAdapter extends RecyclerView.Adapter<CommodityListAdapter.FamilyCircleFollowListViewHolder> {

    private List<CommodityBean.DataBean> mFollowEntities;
    private Context mContext;
    private Activity mActivity;
    private OnFollowListItemClickListener mListItemClickListener;
    private ApiImp apiImp;
    public String article_photo="http://yuejian-app.oss-cn-shenzhen.aliyuncs.com/genealogy/2019062821201820186833.png";
    private int check_num=0;
    private boolean isCheckeds;
    private onClickListener mOnClickListener;
    private boolean isOne;

    public void setOnClickListener(onClickListener onClickListener) {
        this.mOnClickListener = onClickListener;
    }

    public interface onClickListener {
        void onClick(int position, boolean me);
        void onComment(int position);
    }
    public CommodityListAdapter(Context context, OnFollowListItemClickListener listItemClickListener, ApiImp apiImp, Activity activity) {
        mContext = context;
        mActivity =activity;
        mListItemClickListener = listItemClickListener;
        this.apiImp = apiImp;
    }

    @Override
    public FamilyCircleFollowListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.commodity_item, parent, false);
        return new FamilyCircleFollowListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FamilyCircleFollowListViewHolder holder, int position) {
        CommodityBean.DataBean entity = mFollowEntities.get(position);
        String headUrl = entity.getGPhoto();
        if (!TextUtils.isEmpty(headUrl)) {
            Glide.with(mContext).load(headUrl).into(holder.msg_img);
            int itemWidth = (ScreenUtils.getScreenWidth(mContext) - 20 * 3) / 2;
            int itemHeight = itemWidth;
            holder.msg_img.getLayoutParams().height =itemHeight;
        }
        holder.title.setText(entity.getGName());
        if (entity.getGPriceVip()>0){
            holder.vip_lay.setVisibility(View.VISIBLE);
            holder.money.setText(entity.getGPriceVip()+"");
        }else {
            holder.vip_lay.setVisibility(View.GONE);
        }
        holder.original_money.setText(entity.getGPriceOriginal()+"");
    }

    @Override
    public int getItemCount() {
        if (mFollowEntities == null) return 0;
        return mFollowEntities.size();
    }

    public void refresh(List<CommodityBean.DataBean> followEntities) {
        if (this.mFollowEntities == null) {
            this.mFollowEntities = new ArrayList<>();
        }
        this.mFollowEntities = followEntities;
        notifyDataSetChanged();
    }

    public void Loadmore(List<CommodityBean.DataBean> followEntities) {
        if (this.mFollowEntities == null) {
            this.mFollowEntities = new ArrayList<>();
        }
            notifyDataSetChanged();
    }

    public interface OnFollowListItemClickListener {
        void onListItemClick(int type, int id);
    }

    class FamilyCircleFollowListViewHolder extends RecyclerView.ViewHolder {

        RoundAngleImageView msg_img;
        TextView title,money,original_money;
        ImageView vip_img;
        LinearLayout vip_lay;
        public FamilyCircleFollowListViewHolder(View itemView) {
            super(itemView);
            msg_img = (RoundAngleImageView) itemView.findViewById(R.id.msg_img);
            title = (TextView) itemView.findViewById(R.id.title);
            money = (TextView) itemView.findViewById(R.id.money);
            original_money = (TextView) itemView.findViewById(R.id.original_money);
            vip_img = (ImageView) itemView.findViewById(R.id.vip_img);
            vip_lay = (LinearLayout) itemView.findViewById(R.id.vip_lay);
        }
    }
}
