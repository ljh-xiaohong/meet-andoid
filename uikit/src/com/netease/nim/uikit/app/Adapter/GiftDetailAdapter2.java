package com.netease.nim.uikit.app.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.netease.nim.uikit.R;
import com.netease.nim.uikit.api.utils.PreferencesIm;
import com.netease.nim.uikit.api.utils.Utils;
import com.netease.nim.uikit.app.AppConfig;
import com.netease.nim.uikit.app.entity.GiftAllEntity;
import java.util.List;
/**
 * Created by idea on 2016/10/9.
 */

public class GiftDetailAdapter2 extends BaseAdapter {

    private final List<GiftAllEntity> giftBeanList;
    private final Context context;
    private final AbsListView abslistview;


    public GiftDetailAdapter2(AbsListView abslistview, Context context, List<GiftAllEntity> giftBeanList) {
        this.context = context;
        this.giftBeanList = giftBeanList;
        this.abslistview = abslistview;
    }

    @Override
    public int getCount() {
        return giftBeanList.size();
    }

    @Override
    public GiftAllEntity getItem(int position) {
        return giftBeanList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = View.inflate(context, R.layout.item_gift_image, null);
            int v = (int) (AppConfig.width * 0.7) / 5;
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(v, v);
            viewHolder.mIvImage = (ImageView) convertView.findViewById(R.id.gift_iv_image);
            viewHolder.mIvImage.setLayoutParams(params);
            viewHolder.mTvPrice = (TextView) convertView.findViewById(R.id.gift_tv_price);
            viewHolder.mTvName = (TextView) convertView.findViewById(R.id.gift_tv_name);
            viewHolder.gift_ll_container = (LinearLayout) convertView.findViewById(R.id.gift_item_layotu);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        GiftAllEntity giftBean = giftBeanList.get(position);
        viewHolder.gift_ll_container.setSelected(giftBean.isSelected);
//        viewHolder.mIvSelect.setVisibility(giftBean.isSelected ? View.VISIBLE : View.GONE);
        viewHolder.mTvPrice.setText(giftBean.gift_price+ "金币");
        viewHolder.mTvName.setText(giftBean.gift_name);
        Bitmap bitmap = BitmapFactory.decodeFile(AppConfig.chatGiftPath+"/"+ Utils.getFileName(giftBean.getGift_image()));
        if (bitmap != null) {
            bitmap.recycle();
            try {
                Glide.with(context).load(AppConfig.chatGiftPath+"/"+ Utils.getFileName(giftBean.getGift_image())).into(viewHolder.mIvImage);
            }catch (Exception e){}
//            viewHolder.mIvImage.setImageBitmap(bitmap);
        } else {
            PreferencesIm.write(context,PreferencesIm.giftDownload,false);
            Glide.with(context).load(giftBean.gift_image).into(viewHolder.mIvImage);
            PreferencesIm.write(context,PreferencesIm.giftDownload,false);
        }
        return convertView;
    }

    class ViewHolder {
        public TextView mTvPrice;
        public TextView mTvName;
        public ImageView mIvImage;
        public LinearLayout gift_ll_container;
    }

}
