package com.yuejian.meet.framents.home;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.mcxiaoke.bus.annotation.BusReceiver;
import com.netease.nim.uikit.app.AppConfig;
import com.netease.nim.uikit.app.entity.BusCallEntity;
import com.netease.nim.uikit.app.myenum.BusEnum;
import com.netease.nim.uikit.common.media.picker.activity.PickImageActivity;
import com.netease.nim.uikit.common.media.picker.model.PhotoInfo;
import com.netease.nim.uikit.common.media.picker.model.PickerContract;
import com.netease.nim.uikit.common.util.C;
import com.netease.nim.uikit.common.util.storage.StorageType;
import com.netease.nim.uikit.common.util.storage.StorageUtil;
import com.netease.nim.uikit.common.util.string.StringUtil;
import com.netease.nim.uikit.session.activity.CaptureVideoActivity;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.common.SelectMemberCityActivity;
import com.yuejian.meet.activities.common.SurnameActivity;
import com.yuejian.meet.activities.home.ActionMessageActivity;
import com.yuejian.meet.activities.home.ReleaseActionActivity;
import com.yuejian.meet.activities.mine.LoginActivity;
import com.yuejian.meet.adapters.ActionAdapter;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.api.http.ApiImp;
import com.yuejian.meet.bean.ActionAllEntity;
import com.yuejian.meet.bean.ActionEntity;
import com.yuejian.meet.bean.ActionUnreadMsgEntity;
import com.yuejian.meet.bean.CustomerInfo;
import com.yuejian.meet.bean.RegionEntity;
import com.yuejian.meet.common.Constants;
import com.yuejian.meet.framents.base.BaseFragment;
import com.yuejian.meet.utils.AppUitls;
import com.yuejian.meet.utils.ImgUtils;
import com.yuejian.meet.utils.StringUtils;
import com.yuejian.meet.utils.Utils;
import com.yuejian.meet.utils.ViewInject;
import com.yuejian.meet.widgets.springview.DefaultFooter;
import com.yuejian.meet.widgets.springview.DefaultHeader;
import com.yuejian.meet.widgets.springview.SpringView;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 动态
 */
public class ActionFragment extends BaseFragment implements SpringView.OnFreshListener, AdapterView.OnItemClickListener {
    @Bind(R.id.action_spring)
    SpringView springView;
    @Bind(R.id.action_lsit)
    ListView listView;
    @Bind(R.id.txt_action_all)
    TextView txt_action_all;
    @Bind(R.id.txt_action_attention)
    TextView txt_action_attention;
    @Bind(R.id.txt_action_recommend)
    TextView txt_action_recommend;
    @Bind(R.id.action_pull_down)
    ImageView action_pull_down;
    @Bind(R.id.select_window_action)
    View mLayout;
    ImageView img_action_me_header, img_collect_header, img_action_background;
    TextView txt_action_me_name, txt_action_collect_message;
    RelativeLayout rl_action_talk, action_msg_layout;
    TextView txt_action_surname, txt_action_city;

    private View mPoupView = null;
    protected LayoutInflater mInflater;
    private PopupWindow mPoupWindow = null;
    ActionAdapter mAdapter;
    List<ActionEntity> listData = new ArrayList<>();
    ActionUnreadMsgEntity actionUnreadMsg = new ActionUnreadMsgEntity();
    View viewHeader, viewDailog, viewVideo;
    ApiImp api = new ApiImp();
    private static final int OPENPIC = 11;
    private static final int OPENCAM = 12;
    private static final int CROP_PIC = 13;
    private String videoFilePath;
    int pageIndex = 1;//第几页
    String tab_type = "全部";
    String surname = "全部";
    String province = "全国";
    String areaName = "";
    String city = "";
    String area = "";
    public String listLastId="0";
    Intent intent;

    @Override
    protected View inflaterView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        return inflater.inflate(R.layout.fragment_action, container, false);
    }

    @Override
    protected void initData() {
        springView.setFooter(new DefaultFooter(getContext()));
        springView.setHeader(new DefaultHeader(getContext()));
        springView.setListener(this);
        listView = (ListView) getRootView().findViewById(R.id.action_lsit);
        listView.setOnItemClickListener(this);
        viewHeader = View.inflate(getContext(), R.layout.item_action_header_layout, null);
        listView.addHeaderView(viewHeader);
        viewHeader.findViewById(R.id.layout_action_message).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(mContext, ActionMessageActivity.class);
                startActivityForResult(intent, 119);
            }
        });
        mAdapter = new ActionAdapter(listView, listData, R.layout.item_action_dynamic_layout, getActivity());
        listView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        initHeaderView();
        findCustomerInfo(AppConfig.CustomerId);
        pageIndex = 1;
        setSelectBg(tab_type.equals("全部"), tab_type.equals("关注"), tab_type.equals("推荐"));
    }

    @Override
    public void onUserVisible() {
        super.onUserVisible();
        Log.d("action_fragment", "onUserVisible");
        findCustomerInfo(AppConfig.CustomerId);
        if (listView != null && listView.getFirstVisiblePosition() <= 8) {
            pageIndex = 1;
        }
        requestData();
    }

    @Override
    protected void onFirstUserVisible() {
        requestData();
        super.onFirstUserVisible();
    }

    //动态头
    private void initHeaderView() {
        img_action_me_header = (ImageView) viewHeader.findViewById(R.id.img_action_me_header);
        img_collect_header = (ImageView) viewHeader.findViewById(R.id.img_collect_header);
        img_action_background = (ImageView) viewHeader.findViewById(R.id.img_action_background);
        txt_action_me_name = (TextView) viewHeader.findViewById(R.id.txt_action_me_name);
        txt_action_collect_message = (TextView) viewHeader.findViewById(R.id.txt_action_collect_message);
        rl_action_talk = (RelativeLayout) viewHeader.findViewById(R.id.rl_action_talk);
        action_msg_layout = (RelativeLayout) viewHeader.findViewById(R.id.action_msg_layout);
        rl_action_talk.setOnClickListener(this);
        img_action_me_header.setOnClickListener(this);
    }

    public void findCustomerInfo(String id) {
        if (StringUtils.isEmpty(id)) {
            txt_action_me_name.setText("注册|登录");
            Glide.with(mContext).load("").error(R.mipmap.ic_action_bg_url).into(img_action_background);
            return;
        }
        Map<String, Object> params = new HashMap<>();
        params.put("customer_id", id);
        api.findCustomerInfo(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                CustomerInfo customerInfo = JSON.parseObject(data, CustomerInfo.class);
                Glide.with(mContext).load(customerInfo.bg_url).error(R.mipmap.ic_action_bg_url).into(img_action_background);
                Glide.with(mContext).load(customerInfo.getPhoto()).error(R.mipmap.ic_action_bg_url).into(img_action_me_header);
                if (user != null) {
                    Glide.with(getActivity()).load(customerInfo.getPhoto()).asBitmap().placeholder(R.mipmap.ic_default).into(img_action_me_header);
                }
                txt_action_me_name.setText(user == null ? "注册|登录" : customerInfo.getSurname() + customerInfo.getName());
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {
            }
        });
    }

    //获取数据
    public void requestData() {
        final Map<String, Object> params = new HashMap<>();
        params.put("tab_type", tab_type.equals("全部") ? "0" : tab_type.equals("关注") ? "1" : "2");//0:全部, 1:关注, 2:推荐
        params.put("surname", surname);
        params.put("province", province);
        if (!province.equals("全国")) {
            params.put("area_name", areaName);
        }
        params.put("my_customer_id", user == null ? "" : user.getCustomer_id());
        params.put("pageIndex", pageIndex + "");
        params.put("pageItemCount", Constants.pageItemCount);
        params.put("listLastId", pageIndex==1?"0":listLastId);
        api.getActionList(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                try {
                    if (pageIndex == 1) {
                        listData.clear();
                    }

                    ActionAllEntity entity = JSON.parseObject(data, ActionAllEntity.class);
                    actionUnreadMsg = JSON.parseObject(entity.getUn_read_msg(), ActionUnreadMsgEntity.class);
                    List<ActionEntity> actionEntities = JSON.parseArray(entity.getAction_list(), ActionEntity.class);
                    if (actionEntities.isEmpty()) {
                        pageIndex--;
                        pageIndex = pageIndex < 0 ? 1 : pageIndex;
                    }

                    if(!listData.containsAll(actionEntities)) {
                        listData.addAll(actionEntities);
                        listLastId=actionEntities.get(actionEntities.size()-1).getAction_id();
                    }
                    mAdapter.refresh(listData);
                    loadingReadMsg();
                    if (pageIndex > 1) {
                        listView.post(new Runnable() {
                            @Override
                            public void run() {
                                listView.smoothScrollBy(120, 500);
                            }
                        });
                    }

                    if (springView != null) {
                        springView.onFinishFreshAndLoad();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    actionUnreadMsg = null;
                }
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {
                if (springView != null) {
                    springView.onFinishFreshAndLoad();
                }
            }
        });
    }

    /**
     * 加载未读红点数据
     */
    public void loadUnread() {
        if (StringUtil.isEmpty(AppConfig.CustomerId)) return;
        apiImp.getActioUNreadRemind(this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                actionUnreadMsg = JSON.parseObject(data, ActionUnreadMsgEntity.class);
                loadingReadMsg();
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {

            }
        });
    }

    public void loadingReadMsg() {
        if (actionUnreadMsg == null || actionUnreadMsg.getCount() == null || actionUnreadMsg.getCount().equals("0")) {
            action_msg_layout.setVisibility(View.GONE);
        } else {
            action_msg_layout.setVisibility(View.VISIBLE);
            if (listView.getHeaderViewsCount() == 0) {
                listView.addHeaderView(viewHeader);
            }
            txt_action_collect_message.setText("收到" + actionUnreadMsg.getCount() + "条消息");
            Glide.with(getContext()).load(actionUnreadMsg.getPhoto()).asBitmap().placeholder(R.mipmap.ic_default).into(img_collect_header);
        }
    }

    private long lastClick;

    @OnClick({R.id.action_pull_down, R.id.ll_action_all, R.id.txt_action_attention, R.id.txt_action_recommend, R.id.action_top})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_action_all://全部
                if (tab_type.equals("全部")) {
                    showBottomPopupWindow();
                } else {
                    setSelectBg(true, false, false);
                    requestData();
                }
                break;
            case R.id.action_pull_down://选择
                showBottomPopupWindow();
                break;
            case R.id.txt_action_attention://关注
                setSelectBg(false, true, false);
                requestData();
                break;
            case R.id.txt_action_recommend://推荐
                setSelectBg(false, false, true);
                requestData();
                break;
            case R.id.rl_action_talk://说说
                if (user == null) {
                    startActivity(new Intent(getContext(), LoginActivity.class));
                } else {
                    showDialog();
                }
                break;
            case R.id.img_action_me_header://注册登录
                if (user == null)
                    startActivity(new Intent(getContext(), LoginActivity.class));
                else {
                    AppUitls.goToPersonHome(mContext, user.getCustomer_id());
                }
                break;
            case R.id.txt_action_dialog_issue://发布动态
                intent = new Intent(getContext(), ReleaseActionActivity.class);
                startActivityForResult(intent, 119);
                dialog.dismiss();
                break;
            case R.id.txt_action_dialog_video://视频
//                chooseVideoFromCamera();
                dialog.dismiss();
                selVideo();
                break;
            case R.id.txt_action_dialog_photograph_img://拍照
                dialog.dismiss();
                showSelector(false);
                break;
            case R.id.txt_action_dialog_select_img://选择本地照片
                dialog.dismiss();
                showSelector(true);
                break;
            case R.id.action_choose_surname://姓氏
                intent = new Intent(getContext(), SurnameActivity.class);
                if (user != null)
                    intent.putExtra("meSurname", user.getSurname());
                else
                    intent.putExtra("meSurname", "全部");
                startActivity(intent);
                break;
            case R.id.actoin_choose_city://城市
                if (AppConfig.city != null) {
                    intent = new Intent(getContext(), SelectMemberCityActivity.class);
                    if (StringUtils.isAutonomy(AppConfig.province)) {
                        intent.putExtra("city", AppConfig.province);
                    } else {
                        intent.putExtra("city", AppConfig.district);
                    }
                    startActivity(intent);
                }
                break;
            case R.id.txt_action_dialog_shoot:///拍摄
                dialog.dismiss();
                chooseVideoFromCamera();
                break;
            case R.id.txt_action_dialog_locality:///本地选择视频
                dialog.dismiss();
                if (Build.VERSION.SDK_INT >= 19) {
                    new Utils().chooseVideoFromLocalKitKat(getActivity(), 113);
                } else {
                    new Utils().chooseVideoFromLocalBeforeKitKat(getActivity(), 113);
                }
                break;
            case R.id.action_top:
                // 两次点击小于300ms 也就是连续点击
                if (System.currentTimeMillis() - lastClick < 1000) {// 判断是否是执行了双击事件
                    if (listData.size() > 0)
                        listView.setSelection(0);
                }
                lastClick = System.currentTimeMillis();
                break;
        }
    }

    public static Boolean isSel = false;

    /**
     * 底部PopupWindow
     */
    private void showBottomPopupWindow() {
        isSel = true;
        if (mPoupView == null) {
            mInflater = LayoutInflater.from(getContext());
            mPoupView = mInflater.inflate(R.layout.layout_action_select_header, null);
            bindPopMenuEvent(mPoupView);
            mPoupWindow = new PopupWindow(mPoupView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//            mPoupWindow.setAnimationStyle(R.style.PopupAnimation);
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
                    isSel = false;
                }
            });
            ColorDrawable dw = new ColorDrawable(0x90000000);
            mPoupWindow.setBackgroundDrawable(dw);
        }
        int[] location = new int[2];
        mLayout.getLocationOnScreen(location);
        mPoupWindow.showAtLocation(mLayout, Gravity.NO_GRAVITY, 0, location[1] + mLayout.getHeight());
    }

    public void bindPopMenuEvent(View view) {
        view.findViewById(R.id.action_choose_surname).setOnClickListener(this);
        view.findViewById(R.id.actoin_choose_city).setOnClickListener(this);
        txt_action_city = (TextView) view.findViewById(R.id.txt_action_city);
        txt_action_surname = (TextView) view.findViewById(R.id.txt_action_surname);
    }

    //头部选择
    public void setSelectBg(Boolean b1, Boolean b2, Boolean b3) {
        txt_action_all.setSelected(b1);
        action_pull_down.setSelected(b1);
        txt_action_attention.setSelected(b2);
        txt_action_recommend.setSelected(b3);
        tab_type = b1 ? "全部" : b2 ? "关注" : "推荐";
        pageIndex = 1;
    }

    /**
     * 拍摄视频
     */
    protected void chooseVideoFromCamera() {
        if (!StorageUtil.hasEnoughSpaceForWrite(getContext(),
                StorageType.TYPE_VIDEO, true)) {
            return;
        }
        videoFilePath = StorageUtil.getWritePath(
                getContext(), StringUtil.get36UUID()
                        + C.FileSuffix.MP4, StorageType.TYPE_TEMP);

        // 启动视频录制
        CaptureVideoActivity.start(getActivity(), videoFilePath, 114);
    }

    AlertDialog dialog;

    public void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        viewDailog = View.inflate(getContext(), R.layout.dialog_action_issue_layout, null);
        viewDailog.findViewById(R.id.txt_action_dialog_issue).setOnClickListener(this);
        viewDailog.findViewById(R.id.txt_action_dialog_video).setOnClickListener(this);
        viewDailog.findViewById(R.id.txt_action_dialog_photograph_img).setOnClickListener(this);
        viewDailog.findViewById(R.id.txt_action_dialog_select_img).setOnClickListener(this);
        builder.setView(viewDailog);
        dialog = builder.show();
        dialog.show();
    }

    //选择本地视频或拍摄
    public void selVideo() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        viewVideo = View.inflate(getContext(), R.layout.dialog_action_video_sel_layout, null);
        viewVideo.findViewById(R.id.txt_action_dialog_shoot).setOnClickListener(this);
        viewVideo.findViewById(R.id.txt_action_dialog_locality).setOnClickListener(this);
        builder.setView(viewVideo);
        dialog = builder.show();
        dialog.show();
    }

    String outputPath = "";

    /**
     * 打开图片选择器
     */
    private void showSelector(Boolean isOpen) {
        outputPath = ImgUtils.imgTempFile();
        int from = PickImageActivity.FROM_LOCAL;
        if (isOpen) {
            PickImageActivity.start(getActivity(), OPENPIC, from, outputPath, true,
                    9, true, false, 0, 0);//不截图
        } else {
            from = PickImageActivity.FROM_CAMERA;
            PickImageActivity.start(getActivity(), OPENCAM, from, outputPath, false, 1,
                    true, false, 0, 0);//不截图
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (Activity.RESULT_OK == resultCode) {
            switch (requestCode) {
                case OPENPIC://相册选择照片返回
                    try {
                        List<PhotoInfo> photosInfo = PickerContract.getPhotos(data);
                        if (photosInfo.size() > 0) {
                            intent = new Intent(getContext(), ReleaseActionActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("photosList", (Serializable) photosInfo);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }
                    } catch (Exception e) {
                        ViewInject.shortToast(getContext(), "图片出错");
                    }
                    break;
                case OPENCAM://拍完照返回
                    intent = new Intent(getContext(), ReleaseActionActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("photo", outputPath);
                    intent.putExtras(bundle);
                    startActivityForResult(intent, 119);
                    break;
                case CROP_PIC:
                    break;
                case 114:
                    intent = new Intent(getContext(), ReleaseActionActivity.class);
                    bundle = new Bundle();
                    bundle.putString("videoUrl", videoFilePath);
                    intent.putExtras(bundle);
                    startActivityForResult(intent, 119);
                    break;
                case 113://选择本地视频
                    try {
                        videoFilePath = filePathFromIntent(data);
                        MediaPlayer player = new MediaPlayer();
                        try {
                            player.setDataSource(videoFilePath);  //recordingFilePath（）为音频文件的路径
                            player.prepare();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        double duration = player.getDuration();//获取音频的时间
                        if (duration >= (11 * 1000) && !videoFilePath.contains("WeiXin")) {
                            hintVideoDialog();
                        } else {
                            if (getFileSize(new File(videoFilePath)) > (1024 * 1024 * 6)) {
                                hintVideoDialog();
                            } else {
                                intent = new Intent(getContext(), ReleaseActionActivity.class);
                                bundle = new Bundle();
                                bundle.putString("videoUrl", videoFilePath);
                                intent.putExtras(bundle);
                                startActivityForResult(intent, 119);
                            }
                        }
                        player.release();//记得释放资源

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 119:
                    pageIndex = 1;
                    requestData();
                    break;
            }
        }
    }

    public void hintVideoDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("提示");
        builder.setMessage("视频不能大于10秒");
        builder.setPositiveButton("确定", null);
        builder.show();
    }

    /**
     * 获取指定文件大小
     *
     * @return
     * @throws Exception
     */
    public static long getFileSize(File file) throws Exception {
        long size = 0;
        if (file.exists()) {
            FileInputStream fis = null;
            fis = new FileInputStream(file);
            size = fis.available();
        } else {
            file.createNewFile();
            Log.e("获取文件大小", "文件不存在!");
        }
        return size;
    }

    /**
     * 获取文件路径
     *
     * @param data intent数据
     * @return
     */
    private String filePathFromIntent(Intent data) {
        Uri uri = data.getData();
        try {
            Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
            if (cursor == null) {
                //miui 2.3 有可能为null
                return uri.getPath();
            } else {
                cursor.moveToFirst();
                return cursor.getString(cursor.getColumnIndex("_data")); // 文件路径
            }
        } catch (Exception e) {
            return null;
        }
    }

    @BusReceiver
    @Override
    public void onBusCallback(BusCallEntity event) {
        if (!isSel) return;
        if (event.getCallType() == BusEnum.SURNAME_UPDATE) {
            surname = event.getData();
            txt_action_surname.setText(surname);
            pageIndex = 1;
            requestData();
        } else if (event.getCallType() == BusEnum.district) {
            if (AppConfig.district.equals(event.getData())) {
                province = AppConfig.province;
                city = AppConfig.city;
                area = AppConfig.district;
                getAreaName();
            } else if (StringUtils.isAutonomy(event.getData())) {
                province = event.getData();
                city = event.getData();
                area = event.getData();
            } else if ("全部".equals(event.getData())) {
                province = "全国";
                area = "全国";
            } else {
                if (event.getStateType().equals("2")) {
                    city = event.getData();
                    area = "";
                } else {
                    area = event.getData();
                }
            }
            areaName = event.getAreaName();
            txt_action_city.setText(StringUtil.isEmpty(area) ? city : area);
            pageIndex = 1;
            requestData();
        } else if (event.getCallType() == BusEnum.CITY) {
            city = event.getData();
        } else if (event.getCallType() == BusEnum.PROVINCE) {
            province = event.getData();
        } else if (event.getCallType() == BusEnum.ACTION_UNREAD) {
            loadUnread();
        }else if (event.getCallType()==BusEnum.NETWOR_CONNECTION){
            if (listData.size()==0){
                requestData();
            }
        }
    }

    public void getAreaName() {
        Map<String, Object> params = new HashMap<>();
        params.put("province", province);
        params.put("city", city);
        params.put("area", area);
        api.getAreaName(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                RegionEntity entity = JSON.parseObject(data, RegionEntity.class);
                areaName = entity.getArea_name() == null ? "" : entity.getArea_name();
                try {
                    area = areaName.substring(areaName.indexOf(" ") + 1, areaName.length());
                    txt_action_city.setText(StringUtil.isEmpty(area) ? city : area);
                } catch (Exception e) {
                }
                pageIndex = 1;
                requestData();
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {

            }
        });
    }

    @Override
    public void onRefresh() {
        pageIndex = 1;
        requestData();
    }

    @Override
    public void onLoadmore() {
        pageIndex++;
        requestData();
    }

    @Override
    public void onResume() {
        super.onResume();
        Glide.with(getContext()).load(user == null ? "" : user.getPhoto()).asBitmap().placeholder(R.mipmap.ic_default).into(img_action_me_header);
        txt_action_me_name.setText(user == null ? "注册|登录" : user.getSurname() + user.getName());
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    }

    @Override
    public void receiverBus(String event) {
        super.receiverBus(event);
        if ("action_refresh".equals(event)) {
            listView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    listView.smoothScrollToPosition(0);
                    springView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            springView.callFresh();
                        }
                    }, 200);
                }
            }, 0);
        } else if ("ACTION_UNREAD".equals(event)) {
            loadUnread();
        }
    }

}
