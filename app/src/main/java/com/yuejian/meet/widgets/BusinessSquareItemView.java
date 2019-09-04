package com.yuejian.meet.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yuejian.meet.R;
import com.yuejian.meet.bean.CoverPersonListItemEntity;
import com.yuejian.meet.bean.HotChatGroupEntity;
import com.yuejian.meet.bean.TagEntity;

public class BusinessSquareItemView extends LinearLayout implements View.OnClickListener {


    private LinearLayout top;
    private RelativeLayout chat, tag;

    private ImageView img_top, img_chat, img_tag;

    private TextView tv_top_title, tv_top_content, tv_chat_name, tv_chat_people, tv_chat_master, tv_tag_name, tv_tag_info, tv_tag_count, tv_tag_address, tv_top_date;

    private TYPE type;

    private CoverPersonListItemEntity CoverEntity;

    private CoverPersonListItemEntity.mapList CoverListEntity;

    private HotChatGroupEntity chatGroupEntity;

    private TagEntity tagEntity;

    private OnSquareItemViewClickListener listener;

    public BusinessSquareItemView(Context context) {
        this(context, null);
    }

    public BusinessSquareItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BusinessSquareItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);

    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.item_demand_layout, this);
        top = (LinearLayout) this.findViewById(R.id.item_demand_layout_top);
        chat = (RelativeLayout) this.findViewById(R.id.item_demand_layout_chatgroup);
        tag = (RelativeLayout) this.findViewById(R.id.item_demand_layout_tag);

        img_top = (ImageView) this.findViewById(R.id.item_demand_layout_top_img);
        img_chat = (ImageView) this.findViewById(R.id.item_demand__hottalk_img);
        img_tag = (ImageView) this.findViewById(R.id.item_demand__hittip_img);

        tv_top_title = (TextView) this.findViewById(R.id.item_demand_layout_top_info);
        tv_top_content = (TextView) this.findViewById(R.id.item_demand_layout_top_status);
        tv_top_date = (TextView) this.findViewById(R.id.item_demand_layout_top_date);

        tv_chat_name = (TextView) this.findViewById(R.id.item_demand__hottalk_groupname);
        tv_chat_people = (TextView) this.findViewById(R.id.item_demand__hottalk_people);
        tv_chat_master = (TextView) this.findViewById(R.id.item_demand__hottalk_master);
        ;
        tv_tag_name = (TextView) this.findViewById(R.id.item_demand__hittip_name);
        tv_tag_info = (TextView) this.findViewById(R.id.item_demand__hittip_status);
        tv_tag_count = (TextView) this.findViewById(R.id.item_demand__hittip_statuscount);
        tv_tag_address = (TextView) this.findViewById(R.id.item_demand__hittip_address);
        this.setOnClickListener(this);

    }

    public void setType(TYPE type, Object entity, OnSquareItemViewClickListener listener) {
        this.listener = listener;
        this.type = type;
        this.setTag(entity);
        switch (type) {
            case TAG:

                tag.setVisibility(VISIBLE);
                if (entity instanceof TagEntity) {
                    tagEntity = (TagEntity) entity;
                    tv_tag_name.setText(tagEntity.getName());
                    tv_tag_address.setText(tagEntity.getAdress());

                    Glide.with(getContext()).load(tagEntity.getPhoto()).asBitmap().placeholder(R.mipmap.ic_default).into(img_tag);
                }

                break;

            case TOP:

                top.setVisibility(VISIBLE);

                if (entity instanceof CoverPersonListItemEntity) {
                    CoverEntity = (CoverPersonListItemEntity) entity;
                    tv_top_title.setText(String.format("%s:%s", CoverEntity.username, CoverEntity.motto));
                    tv_top_content.setText(String.format("%s | %s | %s", CoverEntity.reputation, CoverEntity.getPositions(), CoverEntity.getContributor()));
                    tv_top_date.setText(String.format("◆  第%s期  %s  ◆", CoverEntity.year, CoverEntity.month));
                    Glide.with(getContext()).load(CoverEntity.getCover_url()).asBitmap().placeholder(R.mipmap.ic_default).into(img_top);
                }

                if (entity instanceof CoverPersonListItemEntity.mapList) {
                    CoverListEntity = (CoverPersonListItemEntity.mapList) entity;
                    tv_top_title.setText(String.format("%s:%s", CoverListEntity.getUsername(), CoverListEntity.getMotto()));
                    tv_top_content.setText(String.format("%s | %s | %s", CoverListEntity.getReputation(), CoverListEntity.getPositions(), CoverListEntity.getContributor()));
                    tv_top_date.setText(String.format("◆  第%s期  %s  ◆", CoverListEntity.getYear(), CoverListEntity.getMonth()));
                    Glide.with(getContext()).load(CoverListEntity.getCover_url()).asBitmap().placeholder(R.mipmap.ic_default).into(img_top);
                }

                break;

            case CHATGROUP:
                chat.setVisibility(VISIBLE);
                if (entity instanceof HotChatGroupEntity) {
                    chatGroupEntity = (HotChatGroupEntity) entity;
                    tv_chat_name.setText(chatGroupEntity.getGroup_name());
                    tv_chat_master.setText(chatGroupEntity.getGroup_manager());
                    tv_chat_people.setText(String.format(" (%s)人", chatGroupEntity.getPeople_num()));
                    Glide.with(getContext()).load(chatGroupEntity.getGroup_photo()).asBitmap().placeholder(R.mipmap.ic_default).into(img_chat);
                }


                break;
        }
    }

    @Override
    public void onClick(View view) {
        this.listener.onSquareItemClick(view, type);
    }


    public enum TYPE {
        TOP, CHATGROUP, TAG
    }

    public interface OnSquareItemViewClickListener {
        void onSquareItemClick(View view, TYPE type);
    }

    public void setOnSquareItemViewClickListener(OnSquareItemViewClickListener listener) {
        this.listener = listener;
    }

}
