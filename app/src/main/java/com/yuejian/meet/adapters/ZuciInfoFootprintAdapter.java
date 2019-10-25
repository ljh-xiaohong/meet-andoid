package com.yuejian.meet.adapters;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.netease.nim.uikit.app.AppConfig;
import com.netease.nim.uikit.common.util.string.StringUtil;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.yuejian.meet.R;
import com.yuejian.meet.bean.ZuciFootprintsEntity;
import com.yuejian.meet.utils.StringUtils;

import java.util.List;

public class ZuciInfoFootprintAdapter extends Adapter<ZuciInfoFootprintAdapter.ViewHolder> {
    Context context;
    List<ZuciFootprintsEntity> listData;
    Dialog dialog = null;

    public int getItemCount() {
        if (this.listData == null) {
            return 0;
        }
        return this.listData.size();
    }

    public void onBindViewHolder(ViewHolder paramViewHolder, int paramInt) {
        final ZuciFootprintsEntity localZuciFootprintsEntity = (ZuciFootprintsEntity)this.listData.get(paramInt);
        paramViewHolder.name.setText(localZuciFootprintsEntity.getSurname() + localZuciFootprintsEntity.getName());
        paramViewHolder.age.setText(" " + localZuciFootprintsEntity.getAge());
        TextView localTextView = paramViewHolder.age;
        localTextView.setSelected(localZuciFootprintsEntity.getSex().equals("0")?false:true);
        Glide.with(this.context).load(localZuciFootprintsEntity.getPhoto()).into(paramViewHolder.photo);
        paramViewHolder.footprint_but.setVisibility(View.VISIBLE);
        if (localZuciFootprintsEntity.getCustomer_id().equals(AppConfig.CustomerId)) {
            paramViewHolder.footprint_but.setVisibility(View.INVISIBLE);
        }
        paramViewHolder.footprint_but.setOnClickListener(new OnClickListener() {
            public void onClick(View paramAnonymousView) {
                View view=View.inflate(context,R.layout.dialog_mi_zuci_greet_sb_layout,null);
//                    ImUtils.toP2PCaht(context, localZuciFootprintsEntity.getCustomer_id());

                AlertDialog.Builder builder=new AlertDialog.Builder(context);
                final EditText editText= (EditText) view.findViewById(R.id.content);
                TextView title_name= (TextView) view.findViewById(R.id.title_name);
                title_name.setText(localZuciFootprintsEntity.getSurname()+localZuciFootprintsEntity.getName());
                builder.setView(view);
                view.findViewById(R.id.cancel).setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                view.findViewById(R.id.confirm).setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        String content=editText.getText().toString();
                        IMMessage textMessage = MessageBuilder.createTextMessage(localZuciFootprintsEntity.customer_id, SessionTypeEnum.P2P, StringUtils.isEmpty(content)?"你好,能跟你认识下么":content);
                        NIMClient.getService(MsgService.class).sendMessage(textMessage, false);
                    }
                });
                dialog=builder.show();
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                dialog.show();
            }
        });
    }

    public ViewHolder onCreateViewHolder(ViewGroup paramViewGroup, int paramInt) {
        this.context = paramViewGroup.getContext();
        return new ViewHolder(View.inflate(this.context, R.layout.item_zuci_info_footprint_layout, null));
    }

    public void setListData(List<ZuciFootprintsEntity> paramList) {
        this.listData = paramList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView age;
        public TextView footprint_but;
        public TextView name;
        public ImageView photo;

        public ViewHolder(View paramView) {
            super(paramView);
            this.photo = ((ImageView)paramView.findViewById(R.id.item_footprint_photo));
            this.name = ((TextView)paramView.findViewById(R.id.item_footprint_name));
            this.age = ((TextView)paramView.findViewById(R.id.item_footprints_age));
            this.footprint_but = ((TextView)paramView.findViewById(R.id.footprint_but));
        }
    }
}
