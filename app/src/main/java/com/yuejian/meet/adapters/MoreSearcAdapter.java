package com.yuejian.meet.adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mcxiaoke.bus.Bus;
import com.netease.nim.uikit.app.entity.BusCallEntity;
import com.netease.nim.uikit.app.myenum.BusEnum;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.clan.ClanInfoActivity;
import com.yuejian.meet.activities.home.ArticleInfoActivity;
import com.yuejian.meet.activities.web.WebActivity;
import com.yuejian.meet.activities.zuci.ZuciInfoActivity;
import com.yuejian.meet.bean.Article;
import com.yuejian.meet.bean.MoreClanEntity;
import com.yuejian.meet.bean.MoreSearchEntity;
import com.yuejian.meet.common.Constants;
import com.yuejian.meet.utils.ImUtils;

import java.util.List;

public class MoreSearcAdapter extends Adapter<ViewHolder> {
    Context context;
    List<MoreSearchEntity> listDate;
    int type;
    public String bindingId="";
    public Boolean isBanin=false;


    public MoreSearcAdapter(Context paramContext, List<MoreSearchEntity> paramList, int paramInt) {
        this.context = paramContext;
        this.listDate = paramList;
        this.type = paramInt;
    }

    public int getItemCount() {
        if (this.listDate == null) {
            return 0;
        }
        return this.listDate.size();
    }
    public void setZuciType(String bindingId,Boolean isBanin){
        this.bindingId=bindingId;
        this.isBanin=isBanin;
    }

    public int getItemViewType(int paramInt) {
        return ((MoreSearchEntity)this.listDate.get(paramInt)).getType();
    }

    public void onBindViewHolder(ViewHolder paramViewHolder, int paramInt) {
        final MoreSearchEntity localObject = listDate.get(paramInt);
        if ((paramViewHolder instanceof MemberViewHolder)) {
            MemberViewHolder memberViewHolder = (MemberViewHolder)paramViewHolder;
            memberViewHolder.txt_search_clan_age.setSelected("1".equals(((MoreSearchEntity)localObject).getSex()));
            memberViewHolder.txt_search_clan_age.setText(" " + ((MoreSearchEntity)localObject).getAge());
            memberViewHolder.search_clan_name.setText((localObject).getSurname() + localObject.getName());
            Glide.with(this.context).load((localObject).getPhoto()).placeholder(R.mipmap.ic_default).into(memberViewHolder.search_clan_picture);
            memberViewHolder.view.setOnClickListener(new OnClickListener()
            {
                public void onClick(View paramAnonymousView)
                {
                    Intent intent = new Intent(MoreSearcAdapter.this.context, WebActivity.class);
                    intent.putExtra("url", Constants.PERSON_HOME + "&customer_id=" + localObject.customer_id);
                    MoreSearcAdapter.this.context.startActivity(intent);
                }
            });
        }
            if ((paramViewHolder instanceof ZuciViewHolder)) {
                ZuciViewHolder zuciViewHolder = (ZuciViewHolder)paramViewHolder;
                zuciViewHolder.search_zuci_name.setText(((MoreSearchEntity)localObject).getName());
                zuciViewHolder.banding_zuci.setVisibility(isBanin?View.VISIBLE:View.GONE);
                zuciViewHolder.banding_zuci.setSelected(localObject.getId().equals(bindingId));
                zuciViewHolder.banding_zuci.setText(localObject.getId().equals(bindingId)?"已绑定":"绑定");
                zuciViewHolder.search_zuci_location.setText(((MoreSearchEntity)localObject).getProvince() + ((MoreSearchEntity)localObject).city + ((MoreSearchEntity)localObject).getDetailed_place());
                Glide.with(this.context).load(((MoreSearchEntity)localObject).getFirst_figure()).placeholder(R.mipmap.ic_default).into(zuciViewHolder.search_zuci_photo);
                zuciViewHolder.view.setOnClickListener(new OnClickListener()
                {
                    public void onClick(View paramAnonymousView)
                    {
                        Intent intent = new Intent(MoreSearcAdapter.this.context, ZuciInfoActivity.class);
                        intent.putExtra("id", localObject.getId());
                        intent.putExtra("zuciName", localObject.getName());
                        MoreSearcAdapter.this.context.startActivity(intent);
                    }
                });
                zuciViewHolder.banding_zuci.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder builder=new AlertDialog.Builder(context);
                        builder.setTitle("");
                        builder.setMessage("确定绑定此祖祠到宗会吗?");
                        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                BusCallEntity entity=new BusCallEntity();
                                entity.setData(localObject.getName());
                                entity.setId(localObject.getId());
                                entity.setCallType(BusEnum.ZUCI_SELECT);
                                Bus.getDefault().post(entity);
                                ((Activity)context).finish();
                            }
                        });
                        builder.setNegativeButton("取消",null);
                        builder.show();

                    }
                });
                return;
            }
            if ((paramViewHolder instanceof GroupViewHolder)) {
                GroupViewHolder groupViewHolder = (GroupViewHolder)paramViewHolder;
                groupViewHolder.search_group_name.setText(((MoreSearchEntity)localObject).getGroup_name());
                Glide.with(this.context).load(((MoreSearchEntity)localObject).getGroup_photo()).placeholder(R.mipmap.ic_default).into(groupViewHolder.search_group_photo);
                groupViewHolder.view.setOnClickListener(new OnClickListener()
                {
                    public void onClick(View paramAnonymousView)
                    {
                        ImUtils.toTeamSession(MoreSearcAdapter.this.context, localObject.getGroup_id(), "0");
                    }
                });
                return;
            }
            if ((paramViewHolder instanceof ArticlViewHolder)) {
                ArticlViewHolder articlViewHolder = (ArticlViewHolder)paramViewHolder;
                articlViewHolder.search_article_name.setText(localObject.getArticle_title());
                Glide.with(this.context).load(localObject.getArticle_photo()).placeholder(R.mipmap.ic_default).into(articlViewHolder.search_article_photo);
                articlViewHolder.view.setOnClickListener(new OnClickListener() {
                    public void onClick(View paramAnonymousView) {
                        Article article = new Article();
                        article.setArticle_id(localObject.getArticle_id());
                        article.setArticle_title(localObject.getArticle_title());
                        article.setCustomer_id(localObject.getCustomer_id());
                        Intent localIntent = new Intent(MoreSearcAdapter.this.context, ArticleInfoActivity.class);
                        localIntent.putExtra("article", article);
                        MoreSearcAdapter.this.context.startActivity(localIntent);
                    }
                });
                return;
            }
            if ((paramViewHolder instanceof ZonqinhuiViewHolder)) {
                ZonqinhuiViewHolder zonqinhuiViewHolder = (ZonqinhuiViewHolder)paramViewHolder;
                zonqinhuiViewHolder.search_clan_name.setText(((MoreSearchEntity)localObject).getAssociation_name());
                zonqinhuiViewHolder.clan_km.setVisibility(localObject.getDistance().equals("-1")?View.GONE:View.VISIBLE);
                zonqinhuiViewHolder.clan_km.setText(" " + ((MoreSearchEntity)localObject).getDistance() + "km");
                zonqinhuiViewHolder.clan_ren_cont.setText(((MoreSearchEntity)localObject).getAssociation_cnt() + "人");
                Glide.with(this.context).load(localObject.getAssociation_img()).placeholder(R.mipmap.ic_default).into(zonqinhuiViewHolder.search_clan_picture);
                zonqinhuiViewHolder.view.setOnClickListener(new OnClickListener()
                    {
                        public void onClick(View paramAnonymousView)
                        {
                            MoreClanEntity moreClanEntity = new MoreClanEntity();
                            moreClanEntity.setAssociation_id(localObject.getAssociation_id());
                            moreClanEntity.setAssociation_name(localObject.getAssociation_name());
                            moreClanEntity.setAssociation_img(localObject.getAssociation_img());
                            Intent localIntent = new Intent(MoreSearcAdapter.this.context, ClanInfoActivity.class);
                            localIntent.putExtra("clanEntity", moreClanEntity);
                            MoreSearcAdapter.this.context.startActivity(localIntent);
                        }
                    });
                    return;
            }
//        } while (!(paramViewHolder instanceof TopViewHolder));
        if (paramViewHolder instanceof TopViewHolder){
            String topName="";
            if (this.type == 1) {
                topName = "宗亲";
            }
            if (this.type == 2) {
                topName = "祖祠";
            } else if (this.type == 3) {
                topName = "宗亲会";
            } else if (this.type == 4) {
                topName = "群聊";
            } else {
                topName = "文章";
            }
            ((TopViewHolder)paramViewHolder).search_top_name.setText(topName);
        }
    }

    public ViewHolder onCreateViewHolder(ViewGroup paramViewGroup, int paramInt) {
        if (paramInt == 0) {
            if (this.type == 1) {
                return new MemberViewHolder(LayoutInflater.from(paramViewGroup.getContext()).inflate(R.layout.item_search_clan_layout, paramViewGroup, false));
            }
            if (this.type == 4) {
                return new GroupViewHolder(LayoutInflater.from(paramViewGroup.getContext()).inflate(R.layout.item_search_chat_group_layout, paramViewGroup, false));
            }
            if (this.type == 2) {
                return new ZuciViewHolder(LayoutInflater.from(paramViewGroup.getContext()).inflate(R.layout.item_search_zuci_layout, paramViewGroup, false));
            }
            if (this.type == 3) {
                return new ZonqinhuiViewHolder(LayoutInflater.from(paramViewGroup.getContext()).inflate(R.layout.item_search_zongqinhui_layout, paramViewGroup, false));
            }
            if (this.type == 5) {
                return new ArticlViewHolder(LayoutInflater.from(paramViewGroup.getContext()).inflate(R.layout.item_search_article_layout, paramViewGroup, false));
            }
        }
        return new TopViewHolder(LayoutInflater.from(paramViewGroup.getContext()).inflate(R.layout.item_search_top_layout, paramViewGroup, false));
    }

    public void refresh(List<MoreSearchEntity> paramList) {
        this.listDate = paramList;
        notifyDataSetChanged();
    }

    class ArticlViewHolder extends ViewHolder {
        TextView search_article_name;
        ImageView search_article_photo;
        View view;

        public ArticlViewHolder(View paramView) {
            super(paramView);
            this.search_article_photo = ((ImageView)paramView.findViewById(R.id.search_article_photo));
            this.search_article_name = ((TextView)paramView.findViewById(R.id.search_article_name));
            this.view = paramView;
        }
    }

    class GroupViewHolder extends ViewHolder {
        TextView search_group_name;
        ImageView search_group_photo;
        View view;

        public GroupViewHolder(View paramView) {
            super(paramView);
            this.search_group_photo = ((ImageView)paramView.findViewById(R.id.search_group_photo));
            this.search_group_name = ((TextView)paramView.findViewById(R.id.search_group_name));
            this.view = paramView;
        }
    }

    class MemberViewHolder extends ViewHolder {
        TextView search_clan_name;
        ImageView search_clan_picture;
        TextView txt_search_clan_age;
        View view;

        public MemberViewHolder(View paramView) {
            super(paramView);
            this.search_clan_picture = ((ImageView)paramView.findViewById(R.id.search_clan_picture));
            this.search_clan_name = ((TextView)paramView.findViewById(R.id.search_clan_name));
            this.txt_search_clan_age = ((TextView)paramView.findViewById(R.id.txt_search_clan_age));
            this.view = paramView;
        }
    }

    class TopViewHolder extends ViewHolder {
        TextView search_top_name;

        public TopViewHolder(View paramView) {
            super(paramView);
            this.search_top_name = ((TextView)paramView.findViewById(R.id.search_top_name));
        }
    }

    class ZonqinhuiViewHolder extends ViewHolder {
        TextView clan_km;
        TextView clan_ren_cont;
        TextView search_clan_name;
        ImageView search_clan_picture;
        View view;

        public ZonqinhuiViewHolder(View paramView) {
            super(paramView);
            this.search_clan_picture = ((ImageView)paramView.findViewById(R.id.search_clan_picture));
            this.search_clan_name = ((TextView)paramView.findViewById(R.id.search_clan_name));
            this.clan_km = ((TextView)paramView.findViewById(R.id.clan_km));
            this.clan_ren_cont = ((TextView)paramView.findViewById(R.id.clan_ren_cont));
            this.view = paramView;
        }
    }

    class ZuciViewHolder extends ViewHolder {
        TextView search_zuci_location;
        TextView search_zuci_name;
        TextView banding_zuci;
        ImageView search_zuci_photo;
        View view;

        public ZuciViewHolder(View paramView) {
            super(paramView);
            this.search_zuci_photo = ((ImageView)paramView.findViewById(R.id.search_zuci_photo));
            this.search_zuci_name = ((TextView)paramView.findViewById(R.id.search_zuci_name));
            this.banding_zuci = ((TextView)paramView.findViewById(R.id.banding_zuci));
            this.search_zuci_location = ((TextView)paramView.findViewById(R.id.search_zuci_location));
            this.view = paramView;
        }
    }
}
