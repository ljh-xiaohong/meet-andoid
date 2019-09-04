package com.yuejian.meet.activities.applet;

import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alipay.sdk.app.PayTask;
import com.netease.nim.uikit.app.entity.BusCallEntity;
import com.netease.nim.uikit.app.myenum.BusEnum;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.base.BaseActivity;
import com.yuejian.meet.activities.clan.ClanInfoActivity;
import com.yuejian.meet.activities.mine.InCashActivity;
import com.yuejian.meet.activities.ui.ClanPayUi;
import com.yuejian.meet.adapters.ExclusAppletAdapter;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.bean.AppletJsonEntity;
import com.yuejian.meet.bean.AppletJsonListEntity;
import com.yuejian.meet.utils.StringUtils;
import com.yuejian.meet.utils.ViewInject;
import com.yuejian.meet.utils.WxPayOrderInfo;
import com.yuejian.meet.widgets.InnerListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 专属小程序
 */
public class ExclusiveAppletActivity extends BaseActivity {
    @Bind(R.id.list1)
    InnerListView list1;
    @Bind(R.id.list2)
    InnerListView list2;
    @Bind(R.id.list3)
    InnerListView list3;
    @Bind(R.id.price)
    TextView price;
    @Bind(R.id.phone)
    EditText phone;
    ExclusAppletAdapter mAdapter1;
    ExclusAppletAdapter mAdapter2;
    ExclusAppletAdapter mAdapter3;
    List<AppletJsonEntity> list=new ArrayList<>();
    List<AppletJsonListEntity> listData1=new ArrayList<>();
    List<AppletJsonListEntity> listData2=new ArrayList<>();
    List<AppletJsonListEntity> listData3=new ArrayList<>();
    ClanPayUi clanPayUi;
    public static boolean appletPal=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exclusive_applet);
        setTitleText(getString(R.string.applet_name1));
        initData();
        appletPal=true;
    }
    public void initData(){
        mAdapter1=new ExclusAppletAdapter(list1,listData1,R.layout.item_applet_list_1_layout,1);
        list1.setAdapter(mAdapter1);
        mAdapter2=new ExclusAppletAdapter(list2,listData2,R.layout.item_applet_list_1_layout,2);
        list2.setAdapter(mAdapter2);
        mAdapter3=new ExclusAppletAdapter(list3,listData3,R.layout.item_applet_list_1_layout,3);
        list3.setAdapter(mAdapter3);
        getJson();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        appletPal=false;
    }

    @OnClick({R.id.pal_button})
    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.pal_button:
                pal();
                break;
        }
    }

    public void select(AppletJsonListEntity item, int type){
        if (type==1){
            for (int i=0;i<listData1.size();i++){
                if (item.getId().equals(listData1.get(i).getId())){
                    listData1.get(i).setSelect(true);
                }else {
                    listData1.get(i).setSelect(false);
                }
            }
            mAdapter1.refresh(listData1);
            setPrice();
        }else if (type==2){
            for (int i=0;i<listData2.size();i++){
                if (item.getId().equals(listData2.get(i).getId())){
                    listData2.get(i).setSelect(true);
                }else {
                    listData2.get(i).setSelect(false);
                }
            }
            mAdapter2.refresh(listData2);
        }else if (type==3){
            for (int i=0;i<listData3.size();i++){
                if (item.getId().equals(listData3.get(i).getId())){
                    listData3.get(i).setSelect(true);
                }else {
                    listData3.get(i).setSelect(false);
                }
            }
            mAdapter3.refresh(listData3);
            setPrice();
        }
    }
    public void getJson(){
        apiImp.getMiniJson(new HashMap<String, Object>(), this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                list= JSON.parseArray(data,AppletJsonEntity.class);
                listData1=JSON.parseArray(list.get(0).getList(),AppletJsonListEntity.class);
                listData2=JSON.parseArray(list.get(1).getList(),AppletJsonListEntity.class);
                listData3=JSON.parseArray(list.get(2).getList(),AppletJsonListEntity.class);
                listData1.get(0).setSelect(true);
                listData2.get(0).setSelect(true);
                listData3.get(0).setSelect(true);
                mAdapter1.refresh(listData1);
                mAdapter2.refresh(listData2);
                mAdapter3.refresh(listData3);
                setPrice();
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {

            }
        });
    }
    public int number=0;
    public void setPrice(){
        for (int i=0;i<listData1.size();i++){
            if (listData1.get(i).isSelect){
                number=listData1.get(i).getPrice();
                break;
            }
        }
        for (int i=0;i<listData3.size();i++){
            if (listData3.get(i).isSelect){
                number+=listData3.get(i).getPrice();
                break;
            }
        }
        price.setText(getString(R.string.applet_name2)+number+"元");
    }
    public void pal(){
        if (StringUtils.isEmpty(phone.getText().toString())){
            ViewInject.toast(this,R.string.applet_name3);
            return;
        }
        if (this.clanPayUi == null) {
            this.clanPayUi = new ClanPayUi(this);
        }
        clanPayUi.doInCash(2, number,getMini());
//        Map<String,String> params=new HashMap<>();
//        params.put("customer_id","");
//        params.put("cny","");
//        params.put("source_type","2");
//        params.put("mini","2");
//        apiImp.doInCash(params, this, new DataIdCallback<String>() {
//            @Override
//            public void onSuccess(String data, int id) {
////                    final WxPayOrderInfo orderInfo = JSON.parseObject(data, WxPayOrderInfo.class);
////                    PayReq request = new PayReq();
////                    request.appId = APP_ID;
////                    request.partnerId = PARTNER_ID;
////                    request.prepayId = orderInfo.prepay_id;
////                    request.packageValue = "Sign=WXPay";
////                    request.nonceStr = orderInfo.nonceStr;
////                    request.timeStamp = orderInfo.timeStamp;
////                    request.sign = orderInfo.paySign;
////                    iwxapi.sendReq(request);
//
//            }
//
//            @Override
//            public void onFailed(String errCode, String errMsg, int id) {
//            }
//        });
    }
//    服务类型:1980元/2年,小程序类型:酒店小程序,制作：制作服务费(含认证费300元),电话:1201210011
    public String getMini(){
        String value="";
        for (int i=0;i<listData1.size();i++){
            if (listData1.get(i).isSelect){
                value="服务类型:"+listData1.get(i).getName();
                break;
            }
        }
        for (int i=0;i<listData2.size();i++){
            if (listData2.get(i).isSelect){
                value+=",小程序类型:"+listData2.get(i).getName();
                break;
            }
        }
        for (int i=0;i<listData3.size();i++){
            if (listData3.get(i).isSelect){
                value+=","+listData3.get(i).getDesc();
                break;
            }
        }
        value+=","+phone.getText().toString();
        return value;
    }

    @Override
    public void onBusCallback(BusCallEntity event) {
        super.onBusCallback(event);
        if (event.getCallType() == BusEnum.payment_success){
            finish();
        }
    }
}
