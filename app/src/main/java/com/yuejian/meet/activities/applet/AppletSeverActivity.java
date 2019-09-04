package com.yuejian.meet.activities.applet;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.base.BaseActivity;
import com.yuejian.meet.activities.message.ServerCenterActivity;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.bean.Server;
import com.yuejian.meet.utils.ImUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.OnClick;

public class AppletSeverActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applet_sever);
        setTitleText(getString(R.string.applet_name9));
    }

    @OnClick({R.id.kf_server,R.id.applet_pal})
    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.kf_server:
                kf();
                break;
            case R.id.applet_pal:
                startActivity(new Intent(this,ExclusiveAppletActivity.class));
                break;
        }
    }
    private ArrayList<Server> serverList = new ArrayList<>();

    public void kf(){
        HashMap<String, Object> params = new HashMap<>();
        apiImp.getKfList(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(final String data, int id) {
                serverList.clear();
                List<Server> servers = JSON.parseArray(data, Server.class);
                if (serverList.isEmpty()) {
                    serverList.addAll(servers);
                }
                if (serverList.size()==1){
                    ImUtils.toP2PCaht(mContext, serverList.get(0).getCustomer_id());
                }else {
                    Intent intent = new Intent(getApplication(), ServerCenterActivity.class);
                    intent.putExtra("server_list", serverList);
                    startActivity(intent);
                }

            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {
            }
        });
    }
}
