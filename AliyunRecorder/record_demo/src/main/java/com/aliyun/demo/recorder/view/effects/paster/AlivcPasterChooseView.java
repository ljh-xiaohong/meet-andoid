package com.aliyun.demo.recorder.view.effects.paster;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aliyun.common.global.AppInfo;
import com.aliyun.demo.R;
import com.aliyun.demo.recorder.view.dialog.IPageTab;
import com.aliyun.demo.recorder.view.dialog.OnClearEffectListener;
import com.aliyun.demo.recorder.view.effects.EffectBody;
import com.aliyun.demo.recorder.view.effects.manager.EffectLoader;
import com.aliyun.downloader.FileDownloaderCallback;
import com.aliyun.svideo.sdk.external.struct.form.PreviewPasterForm;
import com.liulishuo.filedownloader.BaseDownloadTask;

import java.util.ArrayList;
import java.util.List;

/**
 * 人脸贴图
 */
public class AlivcPasterChooseView extends Fragment implements IPageTab,AlivcPasterAdapter.OnItemClickListener,OnClearEffectListener{
    //RecyclerView列数
    private static final int SPAN_COUNT = 5;
    private static final String TAG = AlivcPasterChooseView.class.getSimpleName();
    private RecyclerView rvPaterChooser;
    private EffectLoader mPaterLoader ;
    private ArrayList<PreviewPasterForm> mLoadingMv;
    private AlivcPasterAdapter mAdapter;
    private int mSelectedMVId = -1;
    private PasterSelectListener mPasterSelectListener;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.alivc_mv_choose_view, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvPaterChooser = view.findViewById(R.id.rv_mv_chooser);
        mAdapter = new AlivcPasterAdapter(getActivity());
        mAdapter.setmItemClickListener(this);
        //设置选中位置
        mAdapter.setSelectedMVId(mSelectedMVId);
        rvPaterChooser.setAdapter(mAdapter);
        mPaterLoader= new EffectLoader(getContext());
        rvPaterChooser.setLayoutManager(new GridLayoutManager(getActivity(), SPAN_COUNT));
        loadPaterEffect();
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    /**
     * 加载动图列表数据
     */
    private void loadPaterEffect(){
        mPaterLoader.loadAllPaster(AppInfo.getInstance().obtainAppSignature(getActivity().getApplicationContext()),
            new EffectLoader.LoadCallback<PreviewPasterForm>() {
                @Override
                public void onLoadCompleted(List<PreviewPasterForm> localInfos, List<PreviewPasterForm> remoteInfos,
                                            Throwable e) {
                    List<EffectBody<PreviewPasterForm>> remoteData = new ArrayList<>();
                    List<EffectBody<PreviewPasterForm>> localData = new ArrayList<>();
                    if (localInfos != null) {
                        EffectBody<PreviewPasterForm> body;
                        for (PreviewPasterForm form : localInfos) {
                            body = new EffectBody<PreviewPasterForm>(form, true);
                            localData.add(body);
                        }
                    }
                    if (remoteInfos != null) {
                        EffectBody<PreviewPasterForm> body;
                        for (PreviewPasterForm mv : remoteInfos) {
                            body = new EffectBody<PreviewPasterForm>(mv, false);
                            remoteData.add(body);
                        }
                    }
                    remoteData.addAll(0,localData);
                    mAdapter.syncData(remoteData);
                    mLoadingMv = new ArrayList<PreviewPasterForm>(remoteData.size());
                }
            });

    }

    @Override
    public String getTabTitle() {
        return "人脸贴纸";
    }
    @Override
    public int getTabIcon() {
        return R.mipmap.alivc_svideo_icon_tab_gif;
    }
    @Override
    public void onRemoteItemClick(final int position, final EffectBody<PreviewPasterForm> data) {
        //取消现在的应用的效果
        if (mPasterSelectListener!=null){
            mPasterSelectListener.onPasterSelected(mAdapter.getDataList().get(0).getData());
        }
        //下载
        final PreviewPasterForm pasterForm = data.getData();
        mSelectedMVId = pasterForm.getId();
        mPaterLoader.downloadPaster(pasterForm, new FileDownloaderCallback() {
            @Override
            public void onStart(int downloadId, long soFarBytes, long totalBytes, int preProgress) {
                super.onStart(downloadId, soFarBytes, totalBytes, preProgress);
                mAdapter.notifyDownloadingStart(data);
            }

            @Override
            public void onProgress(int downloadId, long soFarBytes, long totalBytes, long speed, int progress) {

                mAdapter.updateProcess(
                    (AlivcPasterAdapter.PasterViewHolder)rvPaterChooser.findViewHolderForAdapterPosition(position), progress, position);
            }

            @Override
            public void onFinish(int downloadId, String path) {
                if (mPasterSelectListener!=null && mSelectedMVId==pasterForm.getId()) {
                    mPasterSelectListener.onSelectPasterDownloadFinish(path);

                    if (!isDestory){
                        mPasterSelectListener.onPasterSelected(data.getData());
                    }
                }

                mAdapter.notifyDownloadingComplete(data, position,false);
            }

            @Override
            public void onError(BaseDownloadTask task, Throwable e) {
                super.onError(task, e);
                mAdapter.notifyDownloadingComplete(data, position,true);
            }
        });
    }
    private boolean isDestory = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isDestory = false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isDestory = true;
    }

    @Override
    public void onLocalItemClick(int position, EffectBody<PreviewPasterForm> data) {
        mSelectedMVId = data.getData().getId();
        if (mPasterSelectListener!=null){
            mPasterSelectListener.onPasterSelected(data.getData());
        }
    }

    public void setPasterSelectListener(PasterSelectListener mPasterSelectListener) {
        this.mPasterSelectListener = mPasterSelectListener;
    }

    @Override
    public void onClearEffectClick() {
        mSelectedMVId = mAdapter.getDataList().get(0).getData().getId();
        mAdapter.setSelectedMVId(mSelectedMVId);
        mAdapter.notifyDataSetChanged();
        if (mPasterSelectListener!=null){
            mPasterSelectListener.onPasterSelected(mAdapter.getDataList().get(0).getData());
        }
    }


}
