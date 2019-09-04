package com.yuejian.meet.activities.clan;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.netease.nim.uikit.common.util.sys.TimeUtil;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.base.BaseActivity;
import com.yuejian.meet.adapters.clan.ClanPhotoAdapter;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.bean.ClanPhotoArrlyEntity;
import com.yuejian.meet.bean.ClanPhotoEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

public class ClanPhotoActivity extends BaseActivity {
    public static int type = 0;
    String clanId;
    List<ClanPhotoArrlyEntity> listData = new ArrayList();
    List<ClanPhotoEntity> listPhoto = new ArrayList();
    @Bind(R.id.photo_list)
    ListView listView;
    ClanPhotoAdapter mAdapter;
    String time;

    protected void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent) {
        super.onActivityResult(paramInt1, paramInt2, paramIntent);
        requstData();
    }

    @OnClick({R.id.add_photo})
    public void onClick(View paramView){
//        super.onClick(paramView);
        switch (paramView.getId()){
            case R.id.add_photo:
                Intent intent = new Intent(this, ClanPhotoReleaseActivity.class);
                intent.putExtra("clanId", this.clanId);
                startActivityForResult(intent, 110);
                break;
        }
    }

    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.activity_clan_photo);
        this.clanId = getIntent().getStringExtra("clanId");
        type = getIntent().getIntExtra("type", 0);
        if (type != 1) {
            findViewById(R.id.add_photo).setVisibility(View.GONE);
        }
        setTitleText("相册");
        this.mAdapter = new ClanPhotoAdapter(this.listView, this.listData, R.layout.photo_album_item);
        this.listView.setAdapter(this.mAdapter);
        requstData();
    }

    protected void onDestroy() {
        super.onDestroy();
        type = 0;
    }

    @Override
    public void onActivityReenter(int resultCode, Intent data) {
        super.onActivityReenter(resultCode, data);
        if (resultCode==RESULT_OK){
            requstData();
        }
    }

    public void requstData() {
        HashMap localHashMap = new HashMap();
        localHashMap.put("association_id", this.clanId);
        this.apiImp.clanPhoto(localHashMap, this, new DataIdCallback<String>() {
            public void onFailed(String paramAnonymousString1, String paramAnonymousString2, int paramAnonymousInt) {}

            public void onSuccess(String paramAnonymousString, int paramAnonymousInt) {
                ClanPhotoActivity.this.listData.clear();
                ClanPhotoActivity.this.listPhoto = JSON.parseArray(paramAnonymousString, ClanPhotoEntity.class);
                if (ClanPhotoActivity.this.listPhoto.size() == 0) {
                    return;
                }
                ClanPhotoActivity.this.time = TimeUtil.getFormatDatetimeYearMonth(Long.parseLong(((ClanPhotoEntity)ClanPhotoActivity.this.listPhoto.get(0)).getPhoto_createtime()));
                List<ClanPhotoEntity> clanPhotoEntities = new ArrayList();
                ClanPhotoArrlyEntity localClanPhotoArrlyEntity = new ClanPhotoArrlyEntity();
                for (int i=0;i<listPhoto.size();i++){
                    String tiemYear = TimeUtil.getFormatDatetimeYearMonth(Long.parseLong(((ClanPhotoEntity)ClanPhotoActivity.this.listPhoto.get(i)).getPhoto_createtime()));
                    if (ClanPhotoActivity.this.time.equals(tiemYear)) {
                        clanPhotoEntities.add(ClanPhotoActivity.this.listPhoto.get(i));
                    }else {
                        localClanPhotoArrlyEntity.setListPhoto(clanPhotoEntities);
                        localClanPhotoArrlyEntity.setTime((listPhoto.get(paramAnonymousInt)).getPhoto_createtime());
                        localClanPhotoArrlyEntity.setYear(TimeUtil.getFormatDatetimeYear(Long.parseLong(((ClanPhotoEntity)ClanPhotoActivity.this.listPhoto.get(paramAnonymousInt)).getPhoto_createtime())));
                        localClanPhotoArrlyEntity.setMonth(TimeUtil.getFormatDatetimeMonth(Long.parseLong(((ClanPhotoEntity)ClanPhotoActivity.this.listPhoto.get(paramAnonymousInt)).getPhoto_createtime())));
                        ClanPhotoActivity.this.listData.add(localClanPhotoArrlyEntity);
                        ClanPhotoActivity.this.time = TimeUtil.getFormatDatetimeYearMonth(Long.parseLong(((ClanPhotoEntity)ClanPhotoActivity.this.listPhoto.get(0)).getPhoto_createtime()));
                        clanPhotoEntities = new ArrayList();
                        clanPhotoEntities.add(ClanPhotoActivity.this.listPhoto.get(i));

                    }
                }

                localClanPhotoArrlyEntity = new ClanPhotoArrlyEntity();
                localClanPhotoArrlyEntity.setListPhoto(clanPhotoEntities);
                localClanPhotoArrlyEntity.setTime(((ClanPhotoEntity)ClanPhotoActivity.this.listPhoto.get(ClanPhotoActivity.this.listPhoto.size() - 1)).getPhoto_createtime());
                localClanPhotoArrlyEntity.setYear(TimeUtil.getFormatDatetimeYear(Long.parseLong(((ClanPhotoEntity)ClanPhotoActivity.this.listPhoto.get(ClanPhotoActivity.this.listPhoto.size() - 1)).getPhoto_createtime())));
                localClanPhotoArrlyEntity.setMonth(TimeUtil.getFormatDatetimeMonth(Long.parseLong(((ClanPhotoEntity)ClanPhotoActivity.this.listPhoto.get(ClanPhotoActivity.this.listPhoto.size() - 1)).getPhoto_createtime())));
                ClanPhotoActivity.this.listData.add(localClanPhotoArrlyEntity);
                ClanPhotoActivity.this.mAdapter.refresh(ClanPhotoActivity.this.listData);
            }
        });
    }
}
