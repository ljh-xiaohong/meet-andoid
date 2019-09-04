package com.yuejian.meet.activities.family;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.OnClick;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.netease.nim.uikit.app.AppConfig;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.base.BaseActivity;
import com.yuejian.meet.activities.home.StoreWebActivity;
import com.yuejian.meet.activities.mine.InviteOriginateActivity;
import com.yuejian.meet.activities.web.WebActivity;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.api.http.UrlConstant;
import com.yuejian.meet.bean.InviteRank;
import com.yuejian.meet.bean.Mine;
import com.yuejian.meet.common.Constants;
import com.yuejian.meet.utils.DensityUtils;
import com.yuejian.meet.utils.GlideCircleTransform;
import com.yuejian.meet.utils.Utils;
import com.yuejian.meet.widgets.CornersTransform;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 邀请好友
 */
public class InviteFamilyActivity extends BaseActivity {
    ImageView invite_header_n1,invite_header_n2,invite_header_n3;
    TextView header_name_n1,header_name_n2,header_name_n3;
    TextView invite_number_n1,invite_number_n2,invite_number_n3;
    @Bind(R.id.invite_gold)
    TextView invite_gold;
    @Bind(R.id.invite_integral)
    TextView invite_integral;
    @Bind(R.id.invite_rankeng)
    TextView invite_rankeng;
    private InviteRangeAdapter adapter = null;
    private String[] decs =null;
    private View header = null;
    private List<InviteRank> inviteRanks = new ArrayList();
    private List<InviteRank> listTop = new ArrayList();
    private InviteRank myRank = null;
    private PopupWindow popupQrCodeWindow;
    private String[] titles ;
    Intent intent;

    protected void onCreate(@Nullable Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.activity_invite_family);
        setTitleText(getString(R.string.Invite_your_family));
        initBackButton(true);
        ListView listView = (ListView) findViewById(R.id.list);
        initHeader();
        listView.addHeaderView(this.header);
        this.adapter = new InviteRangeAdapter();
        listView.setAdapter(this.adapter);
        inviteRanking();
        inviteRankingMyRank();
    }

    private void initHeader() {
        decs = new String[]{
                getString(R.string.Invite_title1),
                getString(R.string.Invite_title2),
                getString(R.string.Invite_title3),
                getString(R.string.Invite_title4),
                getString(R.string.Invite_title5),
                getString(R.string.Invite_title6)};
        titles = new String[]{
                getString(R.string.Invite_s_title1),
                getString(R.string.Invite_s_title2),
                getString(R.string.Invite_s_title3),
                getString(R.string.Invite_s_title4),
                getString(R.string.Invite_s_title5),
                getString(R.string.Invite_s_title6)};
        this.header = View.inflate(this, R.layout.header_invite_family, null);
        this.header.findViewById(R.id.share_weixin).setOnClickListener(this);
        this.header.findViewById(R.id.share_pyq).setOnClickListener(this);
        this.header.findViewById(R.id.share_qq).setOnClickListener(this);
        this.header.findViewById(R.id.share_qr_code).setOnClickListener(this);
        this.header.findViewById(R.id.jfshangcheng).setOnClickListener(this);
        this.header.findViewById(R.id.share_qr_sms).setOnClickListener(this);
        invite_header_n1= (ImageView) header.findViewById(R.id.invite_header_n1);
        invite_header_n2= (ImageView) header.findViewById(R.id.invite_header_n2);
        invite_header_n3= (ImageView) header.findViewById(R.id.invite_header_n3);
        header_name_n1= (TextView) header.findViewById(R.id.header_name_n1);
        header_name_n2= (TextView) header.findViewById(R.id.header_name_n2);
        header_name_n3= (TextView) header.findViewById(R.id.header_name_n3);
        invite_number_n1= (TextView) header.findViewById(R.id.invite_number_n1);
        invite_number_n2= (TextView) header.findViewById(R.id.invite_number_n2);
        invite_number_n3= (TextView) header.findViewById(R.id.invite_number_n3);
        invite_header_n1.setOnClickListener(this);
        invite_header_n2.setOnClickListener(this);
        invite_header_n3.setOnClickListener(this);
    }

    private void inviteRanking() {
        HashMap localHashMap = new HashMap();
        localHashMap.put("pageItemCount", String.valueOf(100));
        this.apiImp.inviteRanking(localHashMap, this, new DataIdCallback<String>() {
            public void onFailed(String paramAnonymousString1, String paramAnonymousString2, int paramAnonymousInt) {
            }

            public void onSuccess(String paramAnonymousString, int paramAnonymousInt) {
                listTop.clear();
                inviteRanks.clear();
                inviteRanks.addAll(JSON.parseArray(paramAnonymousString, InviteRank.class));
                for (int i=0;i<3;i++){
                    listTop.add(inviteRanks.get(0));
                    inviteRanks.remove(0);
                }
                adapter.notifyDataSetChanged();
                initTopHeader();
            }
        });
    }

    private void inviteRankingMyRank() {
        HashMap<String, Object> localHashMap = new HashMap();
        localHashMap.put("customer_id", AppConfig.CustomerId);
        this.apiImp.inviteRankingMyRank(localHashMap, this, new DataIdCallback<String>() {
            public void onFailed(String paramAnonymousString1, String paramAnonymousString2, int paramAnonymousInt) {
            }

            public void onSuccess(String paramAnonymousString, int paramAnonymousInt) {
                myRank = JSON.parseObject(paramAnonymousString, InviteRank.class);
                ((TextView) findViewById(R.id.my_name)).setText(String.valueOf(myRank.surname + myRank.name));
                ImageView localImageView = (ImageView) findViewById(R.id.my_photo);
                Glide.with(getBaseContext()).load(myRank.photo).into(localImageView);
                invite_rankeng.setText("第"+myRank.getRank());
                invite_integral.setText(myRank.getJf());
                invite_gold.setText(myRank.getGold());
                ((TextView) findViewById(R.id.my_cnt)).setText(myRank.invite_cnt+"人");
//                ((TextView) findViewById(R.id.my_cnt)).setText(String.valueOf(getString(R.string.Invite_the_number) + myRank.invite_cnt + getString(R.string.Invite_the_number2) + myRank.jf + getString(R.string.Invite_the_number3) + myRank.rank));
            }
        });
    }

    public void initTopHeader(){
        Glide.with(this).load(listTop.get(0).getPhoto()).transform(new CornersTransform(this)).into(invite_header_n1);
        Glide.with(this).load(listTop.get(1).getPhoto()).transform(new CornersTransform(this)).into(invite_header_n2);
        Glide.with(this).load(listTop.get(2).getPhoto()).transform(new CornersTransform(this)).into(invite_header_n3);
        header_name_n1.setText(listTop.get(0).getSurname()+listTop.get(0).getName());
        header_name_n2.setText(listTop.get(1).getSurname()+listTop.get(1).getName());
        header_name_n3.setText(listTop.get(2).getSurname()+listTop.get(2).getName());
        invite_number_n1.setText(getString(R.string.Invite_the_number)+listTop.get(0).getInvite_cnt());
        invite_number_n2.setText(getString(R.string.Invite_the_number)+listTop.get(1).getInvite_cnt());
        invite_number_n3.setText(getString(R.string.Invite_the_number)+listTop.get(2).getInvite_cnt());
    }


    private void popupQrCode() {
        if (AppConfig.userEntity == null) return;
        if (popupQrCodeWindow == null) {
            int width = DensityUtils.getScreenW(getBaseContext());
            int height = DensityUtils.getScreenH(getBaseContext());
            popupQrCodeWindow = new PopupWindow(width, height);
            ViewGroup contentView = (ViewGroup) View.inflate(getBaseContext(), R.layout.layout_qr_code_window, null);
            Glide.with(getBaseContext()).load(AppConfig.userEntity.getPhoto()).into((ImageView) contentView.findViewById(R.id.qr_code_customer_photo));
            final TextView name = (TextView) contentView.findViewById(R.id.qr_code_customer_name);
            name.setText(AppConfig.userEntity.surname + AppConfig.userEntity.name);
            TextView id = (TextView) contentView.findViewById(R.id.qr_code_customer_id);
            id.setText(getString(R.string.project_name) + AppConfig.userEntity.customer_id);
            Button copy = (Button) contentView.findViewById(R.id.copy_qr_link);
            Button share = (Button) contentView.findViewById(R.id.share_qr_code);
            Button invite = (Button) contentView.findViewById(R.id.invite_originate);
            if ("1".equals(AppConfig.userEntity.is_family_master)) {
                invite.setVisibility(View.VISIBLE);
                invite.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        HashMap<String, Object> params = new HashMap<>();
                        params.put("customer_id", AppConfig.CustomerId);
                        apiImp.findMyInfo(params, InviteFamilyActivity.this, new DataIdCallback<String>() {
                            @Override
                            public void onSuccess(String data, int id) {
                                Mine mine = JSON.parseObject(data, Mine.class);
                                Intent intent = new Intent(getBaseContext(), InviteOriginateActivity.class);
                                intent.putExtra("mine", mine);
                                startActivity(intent);
                            }

                            @Override
                            public void onFailed(String errCode, String errMsg, int id) {
                            }
                        });
                    }
                });
            }
            ImageView close = (ImageView) contentView.findViewById(R.id.qr_window_close);
            ImageView qrCodeImg = (ImageView) contentView.findViewById(R.id.qr_code_img);
            copy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Utils.copyText(getBaseContext(), UrlConstant.ExplainURL.QRCODE_SHARE
                            + "?customer_id=" + AppConfig.userEntity.customer_id
                            + "&inviteCode=" + AppConfig.userEntity.invite_code);
                }
            });
            share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Glide.with(InviteFamilyActivity.this).load(myRank.photo).asBitmap().listener(new RequestListener<String, Bitmap>() {
                        @Override
                        public boolean onException(Exception e, String s, Target<Bitmap> target, boolean b) {
                            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.app_logo);
                            Utils.umengShareByList(InviteFamilyActivity.this,
                                    bitmap,
                                    getString(R.string.Your_friends_invite_you_into_the_family),
                                    getString(R.string.Your_friends_invite_you_into_the_family2),
                                    UrlConstant.ExplainURL.QRCODE_SHARE
                                            + "?customer_id=" + AppConfig.userEntity.customer_id
                                            + "&inviteCode=" + AppConfig.userEntity.invite_code);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Bitmap bitmap, String s, Target<Bitmap> target, boolean b, boolean b1) {
                            if (bitmap == null) {
                                bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.app_logo);
                            }
                            Utils.umengShareByList(InviteFamilyActivity.this,
                                    bitmap,
                                    getString(R.string.Your_friends_invite_you_into_the_family),
                                    getString(R.string.Your_friends_invite_you_into_the_family2),
                                    UrlConstant.ExplainURL.QRCODE_SHARE
                                            + "?customer_id=" + AppConfig.userEntity.customer_id
                                            + "&inviteCode=" + AppConfig.userEntity.invite_code);
                            return false;
                        }
                    }).preload();
                }
            });
            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupQrCodeWindow.dismiss();
                }
            });
            int size = (int) getResources().getDimension(R.dimen.px_300);
            Bitmap qrCode = Utils.generateBitmap(UrlConstant.ExplainURL.QRCODE_SHARE + "?customer_id=" + AppConfig.userEntity.customer_id + "&inviteCode=" + AppConfig.userEntity.invite_code, size, size);
            qrCodeImg.setImageBitmap(qrCode);
            popupQrCodeWindow.setContentView(contentView);
        }
        popupQrCodeWindow.showAtLocation(getWindow().getDecorView(), Gravity.CENTER, 0, 0);
    }

    public void onBackPressed() {
        if ((this.popupQrCodeWindow != null) && (this.popupQrCodeWindow.isShowing())) {
            this.popupQrCodeWindow.dismiss();
            return;
        }
        super.onBackPressed();
    }

    @OnClick({R.id.go_to_detail,R.id.my_photo})
    public void onClick(View paramView) {
        super.onClick(paramView);
        int i = (int) (Math.random() * 6.0D);
        if (i == 6) {
            i--;
        }
        String title = this.titles[i];
        String dec = this.decs[i];
        String shareUrl = UrlConstant.ExplainURL.QRCODE_SHARE2 + "?inviteCode=" + AppConfig.userEntity.invite_code;
        switch (paramView.getId()) {
            case R.id.share_weixin:
                Utils.umengShareForPhatForm(SHARE_MEDIA.WEIXIN, this, BitmapFactory.decodeResource(getResources(), R.mipmap.app_logo), title, dec, shareUrl);
                return;
            case R.id.share_pyq:
                Utils.umengShareForPhatForm(SHARE_MEDIA.WEIXIN_CIRCLE, this, BitmapFactory.decodeResource(getResources(), R.mipmap.app_logo), title, dec, shareUrl);
                return;
            case R.id.share_qq:
              //  Utils.umengShareForPhatForm(SHARE_MEDIA.QQ, this, BitmapFactory.decodeResource(getResources(), R.mipmap.app_logo), title, dec, shareUrl);
                return;
            case R.id.share_qr_code:
                popupQrCode();
                return;
            case R.id.go_to_detail:
                startActivity(new Intent(this, MyInviteRangeActivity.class));
                return;
            case R.id.jfshangcheng:
                intent = new Intent(this, StoreWebActivity.class);
                intent.putExtra("url", "http://shangyu.yuejianchina.com/yuejian/h5/store?storetype=1&storeid=2&userId=" + AppConfig.CustomerId + "&type=app");
                startActivity(intent);
                break;
            case R.id.share_qr_sms:
                sendSmsWithBody(getString(R.string.invite_sms_content)+shareUrl);
                break;
            case R.id.invite_header_n1:
                intent = new Intent(getBaseContext(), WebActivity.class);
                intent.putExtra("url", Constants.PERSON_HOME + "&customer_id=" + listTop.get(0).customer_id);
                startActivity(intent);
                break;
            case R.id.invite_header_n2:
                intent = new Intent(getBaseContext(), WebActivity.class);
                intent.putExtra("url", Constants.PERSON_HOME + "&customer_id=" + listTop.get(1).customer_id);
                startActivity(intent);
                break;
            case R.id.invite_header_n3:
                intent = new Intent(getBaseContext(), WebActivity.class);
                intent.putExtra("url", Constants.PERSON_HOME + "&customer_id=" + listTop.get(2).customer_id);
                startActivity(intent);
                break;
            case R.id.my_photo:
                intent = new Intent(getBaseContext(), WebActivity.class);
                intent.putExtra("url", Constants.PERSON_HOME + "&customer_id=" + AppConfig.CustomerId);
                startActivity(intent);
                break;
        }
    }

    /**
     * 调用系统界面，给指定的号码发送短信，并附带短信内容
     *
     * @param body
     */
    public void sendSmsWithBody( String body) {
        Intent sendIntent = new Intent(Intent.ACTION_SENDTO);
        sendIntent.setData(Uri.parse("smsto:" ));
        sendIntent.putExtra("sms_body", body);
        startActivity(sendIntent);
    }

    private class InviteRangeAdapter extends BaseAdapter {
        private InviteRangeAdapter() {
        }

        public int getCount() {
            return inviteRanks.size();
        }

        public Object getItem(int paramInt) {
            return inviteRanks.get(paramInt);
        }

        public long getItemId(int paramInt) {
            return paramInt;
        }

        public View getView(int paramInt, View paramView, ViewGroup paramViewGroup) {
            ViewHolder holder = null;
            if (paramView == null) {
                holder = new ViewHolder();
                paramView = View.inflate(getBaseContext(), R.layout.item_inviate_range, null);
                holder.name = ((TextView) paramView.findViewById(R.id.name));
                holder.inviteCount = ((TextView) paramView.findViewById(R.id.invite_count));
                holder.photo = ((ImageView) paramView.findViewById(R.id.photo));
                holder.age = ((TextView) paramView.findViewById(R.id.age));
                holder.range = ((TextView) paramView.findViewById(R.id.range));
                paramView.setTag(holder);
            } else {
                holder = (ViewHolder) paramView.getTag();
            }
//            paramView.findViewById(R.id.header_title).setVisibility(paramInt == 0 ? View.VISIBLE : View.GONE);
            final InviteRank inviteRank = inviteRanks.get(paramInt);
            Glide.with(getBaseContext()).load(inviteRank.photo).into(holder.photo);
            holder.name.setText(String.valueOf(inviteRank.surname + inviteRank.name));
            Utils.setAgeAndSexView(getBaseContext(), holder.age, inviteRank.sex, inviteRank.age);
            holder.inviteCount.setText(String.valueOf(getString(R.string.Invite_the_number) + inviteRank.invite_cnt));
            ImageView commend = (ImageView) paramView.findViewById(R.id.commend);
//            commend.setVisibility(View.VISIBLE);
//            if (paramInt == 0) {
//                commend.setImageResource(R.mipmap.n1);
//            } else if (paramInt == 1) {
//                commend.setImageResource(R.mipmap.n2);
//            } else if (paramInt == 2) {
//                commend.setImageResource(R.mipmap.n3);
//            } else {
                commend.setVisibility(View.GONE);
                holder.range.setText(String.valueOf(paramInt + 4));
//            }
            paramView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getBaseContext(), WebActivity.class);
                    intent.putExtra("url", Constants.PERSON_HOME + "&customer_id=" + inviteRank.customer_id);
                    startActivity(intent);
                }
            });
            return paramView;
        }

        class ViewHolder {
            TextView age;
            TextView inviteCount;
            TextView name;
            ImageView photo;
            TextView range;

            ViewHolder() {
            }
        }
    }
}
