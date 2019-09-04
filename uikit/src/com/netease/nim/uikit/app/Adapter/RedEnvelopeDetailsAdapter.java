package com.netease.nim.uikit.app.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.netease.nim.uikit.R;
import com.netease.nim.uikit.api.utils.PreferencesIm;
import com.netease.nim.uikit.api.utils.Utils;
import com.netease.nim.uikit.app.AppConfig;
import com.netease.nim.uikit.app.entity.GiftAllEntity;
import com.netease.nim.uikit.app.entity.RedEnvelopeDetailsSeedEntity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 抢红包adapter
 * Created by idea on 2016/10/9.
 */

public class RedEnvelopeDetailsAdapter extends BaseAdapter {

    private final List<RedEnvelopeDetailsSeedEntity> giftBeanList;
    private final Context context;
    private final AbsListView abslistview;


    public RedEnvelopeDetailsAdapter(AbsListView abslistview, Context context, List<RedEnvelopeDetailsSeedEntity> giftBeanList) {
        this.context = context;
        this.giftBeanList = giftBeanList;
        this.abslistview = abslistview;
    }

    @Override
    public int getCount() {
        return giftBeanList.size();
    }

    @Override
    public RedEnvelopeDetailsSeedEntity getItem(int position) {
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
            convertView = View.inflate(context, R.layout.item_red_envelope_image, null);
            viewHolder.txt_op_name = (TextView) convertView.findViewById(R.id.txt_op_name);
            viewHolder.txt_time = (TextView) convertView.findViewById(R.id.txt_time);
            viewHolder.item_img_icon = (ImageView) convertView.findViewById(R.id.item_img_icon);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        RedEnvelopeDetailsSeedEntity giftBean = giftBeanList.get(position);
        viewHolder.txt_op_name.setText(giftBean.getSurname()+giftBean.getName());
        Date currentTime = new Date(Long.parseLong(giftBean.getCreate_time()));
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd HH:mm");
        String dateString = formatter.format(currentTime);
        viewHolder.txt_time.setText(dateString);
        Glide.with(context).load(giftBean.getPhoto()).error(R.mipmap.ic_default).into(viewHolder.item_img_icon);
        return convertView;
    }

    class ViewHolder {
        public TextView txt_op_name;
        public TextView txt_time;
        public ImageView item_img_icon;
    }

}
