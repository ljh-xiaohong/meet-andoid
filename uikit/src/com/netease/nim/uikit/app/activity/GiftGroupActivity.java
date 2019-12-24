package com.netease.nim.uikit.app.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.mcxiaoke.bus.Bus;
import com.netease.nim.uikit.R;
import com.netease.nim.uikit.api.DataCallback;
import com.netease.nim.uikit.api.NetApi;
import com.netease.nim.uikit.api.utils.PreferencesIm;
import com.netease.nim.uikit.api.utils.PreferencesMessage;
import com.netease.nim.uikit.api.utils.Utils;
import com.netease.nim.uikit.api.utils.UtilsIm;
import com.netease.nim.uikit.app.Adapter.GiftDetailAdapter2;
import com.netease.nim.uikit.app.Adapter.GiftPagerAdapter;
import com.netease.nim.uikit.app.Adapter.LineGridView;
import com.netease.nim.uikit.app.AppConfig;
import com.netease.nim.uikit.app.entity.BusCallEntity;
import com.netease.nim.uikit.app.entity.GiftAllEntity;
import com.netease.nim.uikit.app.entity.GroupGiftSendEntity;
import com.netease.nim.uikit.app.entity.MonyEntity;
import com.netease.nim.uikit.app.myenum.BusEnum;
import com.netease.nim.uikit.app.widgets.ChatGiftIndicatorView;

import com.alibaba.fastjson.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 群发礼物
 */
public class GiftGroupActivity extends Activity implements View.OnClickListener{

    ViewPager mGiftVpagerContainer;
    ChatGiftIndicatorView mLlPointGroup;
    TextView user_number_money;
    ChatGiftIndicatorView ll_point_group;
    TextView gift_tv_recharge;
    TextView gift_count;
    TextView gift_benediction;
    TextView gift_group_recharge;
    Button gift_send;
    ImageButton titlebar_imgBtn_back;
    LinearLayout gift_more_layout;

    private int preclickPagerIndex = -1;
    private int preclickItemIndex = -1;
    private static final int PAGESIZE = 8;
    private List<GridView> mGiftViews;
    private List<List<GiftAllEntity>> mListGv;
    private GiftPagerAdapter giftPagerGvAdapter;
    private GiftAllEntity mCurrentGift;
    private static OnSendGiftListener sendGiftLister;
    private static String account;
    private String count;
    Activity mContext;
    NetApi api=new NetApi();
    ProgressDialog wait;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        setContentView(R.layout.activity_gift_group);
        mContext=this;
        initView();
    }
    public void initView(){
        wait = new ProgressDialog(this);
        //设置风格为圆形
        wait.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        wait.setTitle(null);
        wait.setIcon(null);
        //设置提示信息
        wait.setMessage("正在发红包..");
        //设置是否可以通过返回键取消
        wait.setCancelable(true);
        mGiftVpagerContainer= (ViewPager) findViewById(R.id.gift_vpager_container);
        mLlPointGroup= (ChatGiftIndicatorView)findViewById(R.id.ll_point_group);
        gift_tv_recharge= (TextView) findViewById(R.id.gift_group_recharge);
        user_number_money= (TextView) findViewById(R.id.user_number_money);
        gift_count= (TextView) findViewById(R.id.gift_count);
        gift_benediction= (TextView) findViewById(R.id.gift_benediction);
        gift_send= (Button) findViewById(R.id.gift_send);
        titlebar_imgBtn_back= (ImageButton) findViewById(R.id.titlebar_imgBtn_back);
        gift_more_layout= (LinearLayout) findViewById(R.id.gift_more_layout);
        gift_send.setSelected(true);
        gift_tv_recharge.setOnClickListener(this);
        titlebar_imgBtn_back.setOnClickListener(this);
        gift_send.setOnClickListener(this);
        DecimalFormat df = new DecimalFormat("0.00");
        user_number_money.setText("总金币: "+df.format(AppConfig.moneySun)+"");
        if (getIntent().hasExtra("count")){
            count=getIntent().getStringExtra("count");
            gift_more_layout.setVisibility(View.GONE);
        }
        initData();
    }

    @Override
    public void onClick(View v) {
        int viewId=v.getId();
        if (viewId==R.id.titlebar_imgBtn_back){
            finish();
        }else if (viewId==R.id.gift_group_recharge){//充值
            BusCallEntity entity=new BusCallEntity();
            entity.setCallType(BusEnum.In_CashActivity);
            Bus.getDefault().post(entity);
        }else if (viewId==R.id.gift_send){//发礼物

            if (PreferencesMessage.get(mContext,PreferencesMessage.family_id+AppConfig.CustomerId,"0").equals("0")){
                final AlertDialog.Builder builder=new AlertDialog.Builder(mContext);
                builder.setMessage("您需要先填写所在地方可进行消费");
                builder.setPositiveButton("前往填写", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        BusCallEntity busCallEntity=new BusCallEntity();
                        busCallEntity.setCallType(BusEnum.Bangding_Family);
                        Bus.getDefault().post(busCallEntity);
                    }
                });
                builder.setNegativeButton("取消",null);
                builder.show();
                return;
            }


            if (sendGiftLister != null && mCurrentGift != null) {
                if (gift_count.getText().toString().equals("")&&count==null){
                    Toast.makeText(this,"请输入红包数",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (gift_count.getText().toString().equals("0")){
                    Toast.makeText(this,"红包数不能为0",Toast.LENGTH_SHORT).show();
                    return;
                }
                mCurrentGift.setCount(gift_count.getText().toString().equals("")?count==null?"1":count:gift_count.getText().toString());
                if (!gift_benediction.getText().toString().trim().equals(""))
                    mCurrentGift.setGift_benediction(gift_benediction.getText().toString().trim());
                if (count==null){
                    sedGift(mCurrentGift);
                }else {
                    sendGiftLister.sendGift(mCurrentGift,null);
                    finish();
                }
            } else {
                Toast.makeText(mContext,"请选择礼物",Toast.LENGTH_SHORT).show();
            }
        }
    }
    public void sedGift(final GiftAllEntity entity){
        wait.show();
        Map<String,String> params=new HashMap<>();
        params.put("gift_id",entity.getId());
        params.put("cnt",entity.getCount());
        params.put("t_id",account);
        params.put("content",entity.getGift_benediction());
        api.sendGroupRedPacket(params, this, new DataCallback<String>() {
            @Override
            public void onSuccess(String data) {
                if (wait!=null)wait.dismiss();
                getMiMoney();
                GroupGiftSendEntity groupGiftSendEntity = new GroupGiftSendEntity();
                groupGiftSendEntity.setContent(entity.getGift_benediction());
                groupGiftSendEntity.setGift_count(Long.parseLong(entity.getCount()));
                groupGiftSendEntity.setGift_id(Long.parseLong(entity.getId()));
                groupGiftSendEntity.setGift_name(entity.getGift_name());
                groupGiftSendEntity.setGiftMoney(entity.getGift_price());
                groupGiftSendEntity.setBag_id(Long.parseLong(data));
                groupGiftSendEntity.setState(true);
                JSONObject jsondata= (JSONObject) JSON.toJSON(groupGiftSendEntity);
                sendGiftLister.sendGift(mCurrentGift,jsondata);
                finish();
            }

            @Override
            public void onSuccess(String data, int id) {

            }

            @Override
            public void onFailed(String errCode, String errMsg) {
                if (wait!=null)wait.dismiss();
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {

            }
        });
//        groupGiftSendEntity.se
//        RedEnvelopeEntity redEnvelopeEntity=new
    }
    public void getMiMoney(){
        api.getBal(AppConfig.CustomerId, this, new DataCallback<String>() {
            @Override
            public void onSuccess(String data) {
                MonyEntity monyEntity= JSON.parseObject(data,MonyEntity.class);
                UtilsIm.setMyMoney(getApplication(),monyEntity);
            }

            @Override
            public void onSuccess(String data, int id) {

            }

            @Override
            public void onFailed(String errCode, String errMsg) {

            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {

            }
        });
    }
    public void initData() {
        mListGv = new ArrayList<>();
        // 获取屏幕宽度

        int screenWidth = (int) (AppConfig.width );
        // item的间距
        int spacing = Utils.dp2px(this, 2);
        // 动态计算item的宽度和高度
        int itemWidth = screenWidth / 5;

        int heighttv = Utils.dp2px(this, 33);
        //动态计算gridview的总高度
        int gvHeight = itemWidth * 2 +heighttv+ spacing * 2;
        List<GiftAllEntity> giftimg = JSON.parseArray(Utils.s2tOrT2s(PreferencesIm.get(mContext,PreferencesIm.gift_image,"[]")),GiftAllEntity.class);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(screenWidth, gvHeight);
        mGiftVpagerContainer.setLayoutParams(params);
        mGiftViews = new ArrayList<>();
        initPager(screenWidth, gvHeight, giftimg);
        //初始化指示器
        mLlPointGroup.initIndicator(mGiftViews.size());
        // 将多个GridView添加显示到ViewPager中
        giftPagerGvAdapter = new GiftPagerAdapter(mGiftViews);
        mGiftVpagerContainer.setAdapter(giftPagerGvAdapter);

        initListener();
    }
    private void initListener() {
        mGiftVpagerContainer.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            int oldPagerPos = 0;

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mLlPointGroup.playByStartPointToNext(oldPagerPos, position);
                oldPagerPos = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
    private void initPager(int screenWidth, int gvHeight, List<GiftAllEntity> giftimg) {
        List<GiftAllEntity> giftPrices = new ArrayList<>();
        // 遍历所有的表情的key
        int pagerindex = 0;
        for (GiftAllEntity giftBean : giftimg) {
            giftPrices.add(giftBean);
            if (giftPrices.size() == PAGESIZE) {
                mListGv.add(giftPrices);
                GridView gv = createGiftGridView(giftPrices, screenWidth, gvHeight, pagerindex);
                mGiftViews.add(gv);
//                // 添加完一组表情,重新创建一个表情名字集合
                giftPrices = new ArrayList<>();
                pagerindex++;
            }
        }
//         判断最后是否有不足8个表情的剩余情况
        if (giftPrices.size() > 0) {
            mListGv.add(giftPrices);
            GridView gv = createGiftGridView(giftPrices, screenWidth, gvHeight, pagerindex);
            mGiftViews.add(gv);
        }
    }
    /**
     * 创建显示表情的GridView
     */
    private GridView createGiftGridView(List<GiftAllEntity> giftList, int gvWidth, int gvHeight, int pagerindex) {
        // 创建GridView
        LineGridView gv = new LineGridView(mContext);
//        设置点击背景透明
        gv.setSelector(android.R.color.transparent);
        //设置4列
        gv.setNumColumns(PAGESIZE / 2);
        //设置GridView的宽高
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(gvWidth, gvHeight);
        gv.setLayoutParams(params);
        // 给GridView设置表情图片
        GiftDetailAdapter2 adapter = new GiftDetailAdapter2(gv, mContext, giftList);
        gv.setAdapter(adapter);
        //设置全局点击事件
        gv.setOnItemClickListener(new ItemClickListener(pagerindex));
        return gv;
    }

    class ItemClickListener implements AdapterView.OnItemClickListener {
        private int pageindex;

        ItemClickListener(int pageindex) {
            this.pageindex = pageindex;
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            List<GiftAllEntity> giftBeen = mListGv.get(pageindex);
            mCurrentGift = giftBeen.get(position);
            if (preclickPagerIndex != -1 && preclickItemIndex != -1) {//改变以前的
                mListGv.get(preclickPagerIndex).get(preclickItemIndex).isSelected = false;
                BaseAdapter adapter = (BaseAdapter) mGiftViews.get(preclickPagerIndex).getAdapter();
                adapter.notifyDataSetChanged();
            }
            mCurrentGift.isSelected = !mCurrentGift.isSelected;
            int clickPagerIndex = pageindex;

            BaseAdapter adapter = (BaseAdapter) mGiftViews.get(clickPagerIndex).getAdapter();
            adapter.notifyDataSetChanged();
            preclickPagerIndex = pageindex;
            preclickItemIndex = position;
        }
    }

    public void setOnSendGiftLister(OnSendGiftListener sendGiftLister,String account) {
        this.sendGiftLister = sendGiftLister;
        this.account=account;
    }
    public interface OnSendGiftListener {
        void sendGift(GiftAllEntity giftBean,JSONObject jsonData);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sendGiftLister=null;
        account=null;
    }
}
