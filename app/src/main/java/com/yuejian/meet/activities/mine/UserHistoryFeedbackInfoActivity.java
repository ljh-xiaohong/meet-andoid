package com.yuejian.meet.activities.mine;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.LeadingMarginSpan;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.netease.nim.uikit.common.util.string.StringUtil;
import com.netease.nim.uikit.common.util.sys.TimeUtil;
import com.netease.nimlib.sdk.msg.model.RecentContact;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.base.BaseActivity;
import com.yuejian.meet.activities.message.ServerCenterActivity;
import com.yuejian.meet.adapters.FeedbackCommentAdapter;
import com.yuejian.meet.adapters.NoticeInfoAdapter;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.bean.ActionAllEntity;
import com.yuejian.meet.bean.FeebackCommentEntity;
import com.yuejian.meet.bean.Server;
import com.yuejian.meet.bean.UserFeedbackEntity;
import com.yuejian.meet.dialogs.LoadingDialogFragment;
import com.yuejian.meet.utils.ImUtils;
import com.yuejian.meet.utils.TimeUtils;
import com.yuejian.meet.utils.ViewInject;
import com.yuejian.meet.widgets.InnerGridView;
import com.yuejian.meet.widgets.InnerListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * bug反馈详情
 */
public class UserHistoryFeedbackInfoActivity extends BaseActivity {
    @Bind(R.id.item_bug_type_name)
    TextView item_bug_type_name;
    @Bind(R.id.feedback_info_content)
    TextView feedback_info_content;
    @Bind(R.id.feedback_date)
    TextView feedback_date;
    @Bind(R.id.but_layout)
    LinearLayout but_layout;
    InnerGridView gridView;
    InnerListView listView;
    NoticeInfoAdapter mAdapterPhoto;
    List<String> listPhotoData=new ArrayList<>();
    UserFeedbackEntity entity=new UserFeedbackEntity();
    List<FeebackCommentEntity> listComment=new ArrayList<>();
    FeedbackCommentAdapter mAdapter;
    String id,status;
    LoadingDialogFragment dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_history_feedback_info);
        id=getIntent().getStringExtra("id");
        status=getIntent().getStringExtra("status");
        initView();
    }
    public void initView(){
        setTitleText(getString(R.string.history_feedback));
        dialog=LoadingDialogFragment.newInstance(getString(R.string.in_operation));
        item_bug_type_name.setSelected(true);
        gridView= (InnerGridView) findViewById(R.id.feedback_grid_photo);
        listView= (InnerListView) findViewById(R.id.feedback_comment);
        mAdapterPhoto=new NoticeInfoAdapter(gridView,listPhotoData,R.layout.item_notice_info_img_layout);
        gridView.setAdapter(mAdapterPhoto);
        mAdapterPhoto.notifyDataSetChanged();
        mAdapter=new FeedbackCommentAdapter(listView,listComment,R.layout.feedback_comment_info_layout);
        listView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        if (status.equals("1")){
            but_layout.setVisibility(View.GONE);
        }
        requstData();
    }
    public void requstData(){
        Map<String,Object> params=new HashMap<>();
        params.put("feedback_id",id);
        apiImp.getFeedbackInfo(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                ActionAllEntity actionAllEntity=JSON.parseObject(data,ActionAllEntity.class);
                entity= JSON.parseObject(actionAllEntity.getFeedback(),UserFeedbackEntity.class);
                item_bug_type_name.setText(entity.getFeedback_type().equals("1")?getString(R.string.user_feedback_bug1):entity.getFeedback_type().equals("2")?getString(R.string.user_feedback_bug2):entity.getFeedback_type().equals("3")?getString(R.string.user_feedback_bug3):getString(R.string.user_feedback_bug4));
                calculateTag2(item_bug_type_name,feedback_info_content,entity.getFeedback_question());
                but_layout.setVisibility(entity.getFeedback_status().equals("1")?View.GONE:View.VISIBLE);
//                String content="        ";
//                for (int i=0;i<item_bug_type_name.getText().length();i++){
//                    content+="    ";
//                    if (i==3){
//                        break;
//                    }
//                }

//                feedback_info_content.setText(content+entity.getFeedback_question());
                feedback_date.setText(TimeUtils.getBugTimeTwo(entity.getFeedback_create_time()));
                if (!StringUtil.isEmpty(actionAllEntity.getReplyList())){
                    listComment=JSON.parseArray(actionAllEntity.getReplyList(),FeebackCommentEntity.class);
                    if (listComment.size()>0){
                        listView.setVisibility(View.VISIBLE);
                    }
                    mAdapter.refresh(listComment);
                }
                if (!StringUtil.isEmpty(entity.getFeedback_imgs())){
                    String[] array=entity.getFeedback_imgs().split(",");
                    listPhotoData= Arrays.asList(array);
                    mAdapterPhoto.setLsit(listPhotoData);
                    mAdapterPhoto.refresh(listPhotoData);
                }
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {

            }
        });
    }
    //方案二：动态设置缩进距离的方式
    public void calculateTag2(final TextView tag, final TextView title, final String text) {
        ViewTreeObserver observer = tag.getViewTreeObserver();
        observer.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                SpannableString spannableString = new SpannableString(text);
                //这里没有获取margin的值，而是直接写死的
                LeadingMarginSpan.Standard what = new LeadingMarginSpan.Standard(tag.getWidth() + dip2px(tag.getContext(), 10), 0);
                spannableString.setSpan(what, 0, spannableString.length(), SpannableString.SPAN_INCLUSIVE_INCLUSIVE);
                title.setText(spannableString);
                tag.getViewTreeObserver().removeOnPreDrawListener(
                        this);
                return false;
            }
        });

    }

    public int dip2px(Context context, double dpValue) {
        float density = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * density + 0.5);
    }


    @OnClick({R.id.bug_solve,R.id.bug_unsolve})
    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.bug_solve:
                if (null != dialog)
                    dialog.show(getFragmentManager(),"");
                Map<String,Object> params=new HashMap<>();
                params.put("feedback_id",id);
                apiImp.postFeedbackSolving(params, this, new DataIdCallback<String>() {
                    @Override
                    public void onSuccess(String data, int id) {
                        ViewInject.toast(getApplication(),R.string.history_feedback2);
                        setResult(RESULT_OK,new Intent());
                        finish();
                    }

                    @Override
                    public void onFailed(String errCode, String errMsg, int id) {
                        if (dialog != null)
                            dialog.dismiss();
                    }
                });

                break;
            case R.id.bug_unsolve:
                AlertDialog.Builder builder=new AlertDialog.Builder(this);
                builder.setTitle(" ");
                builder.setMessage(R.string.history_feedback3);
                builder.setNegativeButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        kf();
                    }
                });
                builder.setNeutralButton(R.string.cancel,null);
                builder.show();

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
