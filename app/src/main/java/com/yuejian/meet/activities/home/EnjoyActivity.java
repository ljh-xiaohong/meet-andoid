package com.yuejian.meet.activities.home;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.base.BaseActivity;
import com.yuejian.meet.activities.mine.LoginActivity;
import com.yuejian.meet.activities.web.WebActivity;
import com.yuejian.meet.adapters.BotActityAdapter;
import com.yuejian.meet.adapters.EnjoyActityAdapter;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.api.http.UrlConstant;
import com.yuejian.meet.bean.BotEntity;
import com.yuejian.meet.bean.EnjoyEntity;
import com.yuejian.meet.common.Constants;
import com.yuejian.meet.utils.DensityUtils;
import com.yuejian.meet.utils.PreferencesUtil;
import com.yuejian.meet.utils.StringUtils;
import com.yuejian.meet.widgets.springview.DefaultFooter;
import com.yuejian.meet.widgets.springview.DefaultHeader;
import com.yuejian.meet.widgets.springview.SpringView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 宗享会
 */
/**
 * @author :
 * @time   : 2018/11/11 11:28
 * @desc   : 创建宗享会
 * @version: V1.0
 * @update : 2018/11/11 11:28
 */

public class EnjoyActivity extends BaseActivity implements SpringView.OnFreshListener{

    @Bind(R.id.holp)
    ImageView holp;
    @Bind(R.id.enjoy_spring)
    SpringView springView;
    @Bind(R.id.enjoy_list)
    ListView listView;
    @Bind(R.id.launch_enjoy)
    TextView launch_enjoy;
    @Bind(R.id.enjoy_top_layout)
    LinearLayout enjoy_top_layout;
    @Bind(R.id.enjoy_tab1)
    TextView enjoy_tab1;
    @Bind(R.id.enjoy_tab2)
    TextView enjoy_tab2;
    EnjoyActityAdapter mAdapter;
    BotActityAdapter botActityAdapter;
    List<EnjoyEntity> listData=new ArrayList<>();
    List<BotEntity> botList=new ArrayList<>();
    int pageIndex=1;
    int botPageIndex=1;
    int type=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enjoy);
        initData();
    }
    public void initData(){
        enjoy_top_layout.setVisibility(View.VISIBLE);
        enjoy_tab1.setSelected(true);
        holp.setVisibility(View.VISIBLE);
        springView.setHeader(new DefaultHeader(this));
        springView.setFooter(new DefaultFooter(this));
        springView.setListener(this);

        mAdapter=new EnjoyActityAdapter(listView,listData,R.layout.item_enjoy_list_layout);
        listView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

        botActityAdapter=new BotActityAdapter(listView,botList,R.layout.item_bot_list_layout);

        setTitleText(getString(R.string.Clansmen_association_1));
        getData();
        getBotData();
//        showBottomPopupWindow();
    }

    @OnClick({R.id.holp,R.id.launch_enjoy,R.id.enjoy_tab1,R.id.enjoy_tab2,R.id.bu_next})
    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.holp:
                Intent intent= new Intent(this, WebActivity.class);
                intent.putExtra(Constants.URL, UrlConstant.getWebUrl()+"clanEnjoyAssociationMeeting/help.html");
                startActivity(intent);
                break;
            case R.id.launch_enjoy:
                startActivity(new Intent(this,CreateEnjoyActivity.class));
                break;
            case R.id.bu_next:
                if (user==null){
                    startActivity(new Intent(this, LoginActivity.class));
                }else {
                    startActivityIfNeeded(new Intent(this,CreateBotActivity.class),110);
                }
                break;
            case R.id.enjoy_tab1:
                enjoy_tab1.setSelected(true);
                enjoy_tab2.setSelected(false);
//                launch_enjoy.setText(R.string.enjoy_text_1);
                type=1;
                listView.setAdapter(mAdapter);
                listView.setBackgroundColor(getResources().getColor(R.color.white));
                listView.setDividerHeight(DensityUtils.dip2px(this,15));
                listView.setDivider(new ColorDrawable(getResources().getColor(R.color.setting_bg)));
                listView.setDividerHeight(DensityUtils.dip2px(this,15));
                break;
            case R.id.enjoy_tab2:
                enjoy_tab1.setSelected(false);
                enjoy_tab2.setSelected(true);
//                launch_enjoy.setText("发布项目");
                type=2;
                listView.setAdapter(botActityAdapter);
                listView.setBackgroundColor(getResources().getColor(R.color.setting_bg));
                listView.setDivider(new ColorDrawable(getResources().getColor(R.color.white)));
                listView.setDividerHeight(DensityUtils.dip2px(this,15));
                break;

        }
    }
    public void getData(){
        Map<String,Object> params=new HashMap<>();
        params.put("pageIndex",pageIndex+"");
        params.put("pageItemCount","20");
        apiImp.getEnjoy(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                if (pageIndex==1){
                    listData.clear();
                    showBottomPopupWindow();
                }
                listData.addAll(JSON.parseArray(data,EnjoyEntity.class));
                mAdapter.refresh(listData);
                springView.onFinishFreshAndLoad();
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {
                springView.onFinishFreshAndLoad();
            }
        });
    }
    public void getBotData(){
        Map<String,Object> params=new HashMap<>();
        params.put("pageIndex",botPageIndex+"");
        params.put("pageItemCount","20");
        apiImp.getBotAll(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                if (botPageIndex==1){
                    botList.clear();
                }
                botList.addAll(JSON.parseArray(data,BotEntity.class));
                botActityAdapter.refresh(botList);
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {

            }
        });
    }
    private View mPoupView = null;
    protected LayoutInflater mInflater;
    private PopupWindow mPoupWindow = null;
    /**
     * 底部PopupWindow
     */
    private void showBottomPopupWindow() {
        if (!StringUtils.isEmpty(PreferencesUtil.get(this,PreferencesUtil.KEY_ENJOY,""))){
            return;
        }
        PreferencesUtil.put(this,PreferencesUtil.KEY_ENJOY,"1");
        if (mPoupView == null) {
            mInflater = LayoutInflater.from(this);
            mPoupView = mInflater.inflate(R.layout.item_enjoy_dialog_widdow_layout, null);
            bindPopMenuEvent(mPoupView);
            mPoupWindow = new PopupWindow(mPoupView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            mPoupWindow.setAnimationStyle(R.style.PopupAnimation);
            mPoupWindow.setTouchable(true);
            mPoupWindow.setFocusable(true);
            mPoupWindow.setOutsideTouchable(true);
            mPoupWindow.setTouchInterceptor(new View.OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return false;
                }
            });
            mPoupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    backgroundAlpha(1f);
                }
            });
            ColorDrawable dw = new ColorDrawable(00000000);
            mPoupWindow.setBackgroundDrawable(dw);
        }
        mPoupWindow.showAtLocation(this.getWindow().getDecorView(), Gravity.CENTER, 0, 0);
        backgroundAlpha(0.7f);
    }
    public void bindPopMenuEvent(View view){
        view.findViewById(R.id.window_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null != mPoupWindow)
                    mPoupWindow.dismiss();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            botPageIndex=1;
            getBotData();
        }
    }

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
    }

    @Override
    public void onRefresh() {
        if (type==1){
            pageIndex=1;
            getData();
        }else {
            botPageIndex=1;
            getBotData();
        }
    }

    @Override
    public void onLoadmore() {
        if (type==1){
            pageIndex+=1;
            getData();
        }else {
            botPageIndex+=1;
            getBotData();
        }
    }
}
