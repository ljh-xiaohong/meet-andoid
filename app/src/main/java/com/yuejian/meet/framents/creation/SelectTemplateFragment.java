package com.yuejian.meet.framents.creation;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.aliyun.common.utils.CommonUtil;
import com.aliyun.common.utils.ToastUtil;
import com.aliyun.downloader.DownloaderManager;
import com.aliyun.downloader.FileDownloaderCallback;
import com.aliyun.downloader.FileDownloaderModel;
import com.aliyun.svideo.base.AlivcSvideoEditParam;
import com.aliyun.svideo.base.http.EffectService;
import com.aliyun.svideo.editor.MediaActivity;
import com.aliyun.svideo.editor.effectmanager.EffectBody;
import com.aliyun.svideo.editor.effectmanager.EffectLoader;
import com.aliyun.svideo.editor.effectmanager.TasksManager;
import com.aliyun.svideo.editor.effects.control.EffectInfo;
import com.aliyun.svideo.editor.effects.control.UIEditorPage;
import com.aliyun.svideo.sdk.external.struct.form.AspectForm;
import com.aliyun.svideo.sdk.external.struct.form.IMVForm;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.yuejian.meet.R;
import com.yuejian.meet.adapters.SelectTemplateListAdapter;
import com.yuejian.meet.framents.base.BaseFragment;
import com.yuejian.meet.ui.SpacesItemDecoration;
import com.yuejian.meet.utils.ViewInject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * @author : g000gle
 * @time : 2019/5/27 11:44
 * @desc : 选择视频模板 - 分类Fragment
 */
public class SelectTemplateFragment extends BaseFragment implements SelectTemplateListAdapter.OnItemRightButtonClickListener {

    @Bind(R.id.rv_select_template_list)
    RecyclerView mRecyclerView;
    @Bind(R.id.ll_select_template_list_empty)
    LinearLayout mEmptyList;

    private EffectLoader mMVLoader = new EffectLoader();
    private List<IMVForm> mLoadingMv = null;
    private SelectTemplateListAdapter mAdapter;

    public static SelectTemplateFragment newInstance() {

        Bundle args = new Bundle();

        SelectTemplateFragment fragment = new SelectTemplateFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected View inflaterView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        return inflater.inflate(R.layout.fragment_select_template, container, false);
    }

    @Override
    protected void initWidget(View parentView) {
        super.initWidget(parentView);

        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mAdapter = new SelectTemplateListAdapter(getActivity());
        SpacesItemDecoration decoration = new SpacesItemDecoration(20);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addItemDecoration(decoration);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setRightBtnClickListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();

//        String tag = getArguments().getString("tag");
//        if (tag != null && tag.equals("empty")) {
//            mEmptyList.setVisibility(View.VISIBLE);
//        } else {
            mEmptyList.setVisibility(View.GONE);
            mMVLoader.loadAllMV((localInfos, remoteInfos, e) -> {
                int localSize = localInfos == null ? 0 : localInfos.size();
                int remoteSize = remoteInfos == null ? 0 : remoteInfos.size();
                Log.d("moreMv", "localSize : " + localSize + " ,remoteSize : " + remoteSize);
                List<EffectBody<IMVForm>> remoteData = new ArrayList<>();
                List<EffectBody<IMVForm>> localData = new ArrayList<>();
                if (localInfos != null) {
                    EffectBody<IMVForm> body;
                    for (IMVForm form : localInfos) {
                        body = new EffectBody<IMVForm>(form, true);
                        localData.add(body);
                    }
                }
                if (remoteInfos != null) {
                    EffectBody<IMVForm> body;
                    for (IMVForm mv : remoteInfos) {
                        body = new EffectBody<IMVForm>(mv, false);
                        remoteData.add(body);
                    }
                }
                remoteData.addAll(localData);
                mAdapter.syncData(remoteData);
                mLoadingMv = new ArrayList<IMVForm>(remoteData.size());
            });
//        }
    }

    @Override
    public void onRemoteItemClick(int position, EffectBody<IMVForm> data) {
        if (!CommonUtil.hasNetwork(getContext())) {
            ViewInject.shortToast(getContext(), "网络不给力，请检查网络");
            return;
        }

        if (CommonUtil.SDFreeSize() < 10 * 1000 * 1000) {
            ViewInject.shortToast(getContext(), "存储空间不足");
            return;
        }
        if (mLoadingMv.contains(data.getData())) {//如果已经在下载中了，则不能重复下载
            return;
        }
        mLoadingMv.add(data.getData());
        //下载
        IMVForm mv = data.getData();
        final List<AspectForm> aspects = mv.getAspectList();
        final List<FileDownloaderModel> tasks = new ArrayList<>();

        TasksManager tasksManager = new TasksManager();
        if (aspects != null) {
            FileDownloaderModel model;
            List<FileDownloaderModel> list = new ArrayList<>();
            for (final AspectForm aspect : aspects) {
                model = new FileDownloaderModel();
                model.setEffectType(EffectService.EFFECT_MV);
                model.setTag(mv.getTag());
                model.setKey(mv.getKey());
                model.setName(mv.getName());
                model.setId(mv.getId());
                model.setCat(mv.getCat());
                model.setLevel(mv.getLevel());
                model.setPreviewpic(mv.getPreviewPic());
                model.setIcon(mv.getIcon());
                model.setPreviewmp4(mv.getPreviewMp4());
                model.setSort(mv.getSort());
                model.setSubtype(mv.getType());
                model.setMd5(aspect.getMd5());
                model.setDownload(aspect.getDownload());
                model.setUrl(aspect.getDownload());
                model.setAspect(aspect.getAspect());
                model.setDuration(mv.getDuration());
                model.setIsunzip(1);
                final FileDownloaderModel task = DownloaderManager.getInstance().addTask(model, model.getDownload());
                tasksManager.addTask(task.getTaskId(), new FileDownLoadCallBack(this, data, position, tasks));
            }
            tasksManager.startTask();
        }
    }

    @Override
    public void onLocalItemClick(int position, EffectBody<IMVForm> data) {
        // TODO 使用该MV
//        Intent intent = new Intent();
//        intent.putExtra(SELECTD_ID, data.getData().getId());
//        setResult(Activity.RESULT_OK, intent);
//        finish();

        EffectInfo effectInfo = new EffectInfo();
        effectInfo.type = UIEditorPage.MV;
        effectInfo.list = data.getData().getAspectList();
        effectInfo.id = data.getData().getId();

        AlivcSvideoEditParam param = new AlivcSvideoEditParam.Build().build();
        Intent intent = new Intent(getActivity(), MediaActivity.class);
        intent.putExtra(AlivcSvideoEditParam.VIDEO_BITRATE, param.getBitrate());
        intent.putExtra(AlivcSvideoEditParam.VIDEO_FRAMERATE, param.getFrameRate());
        intent.putExtra(AlivcSvideoEditParam.VIDEO_GOP, param.getGop());
        intent.putExtra(AlivcSvideoEditParam.VIDEO_RATIO, param.getRatio());
        intent.putExtra(AlivcSvideoEditParam.VIDEO_QUALITY, param.getVideoQuality());
        intent.putExtra(AlivcSvideoEditParam.VIDEO_RESOLUTION, param.getResolutionMode());
        intent.putExtra(AlivcSvideoEditParam.VIDEO_CROP_MODE, param.getCropMode());
        intent.putExtra(AlivcSvideoEditParam.TAIL_ANIMATION, param.isHasTailAnimation());
        intent.putExtra(AlivcSvideoEditParam.INTENT_PARAM_KEY_ENTRANCE, "svideo" );
        intent.putExtra(AlivcSvideoEditParam.VIDEO_CODEC, param.getVideoCodec() );
        intent.putExtra("effect_info", effectInfo);
        startActivity(intent);
    }

    /**
     * 文件下载回调
     */
    private static class FileDownLoadCallBack extends FileDownloaderCallback {

        private WeakReference<SelectTemplateFragment> weakReference;
        private EffectBody<IMVForm> data;
        private int position;
        private List<FileDownloaderModel> tasks;

        public FileDownLoadCallBack(SelectTemplateFragment selectTemplateFragment, EffectBody<IMVForm> data,
                                    int position, List<FileDownloaderModel> tasks) {
            weakReference = new WeakReference<>(selectTemplateFragment);
            this.data = data;
            this.position = position;
            this.tasks = tasks;
        }

        @Override
        public void onStart(int downloadId, long soFarBytes, long totalBytes, int preProgress) {
            super.onStart(downloadId, soFarBytes, totalBytes, preProgress);
            SelectTemplateFragment selectTemplateFragment = weakReference.get();
            if (selectTemplateFragment != null) {
                selectTemplateFragment.mAdapter.notifyDownloadingStart(data);
            }
        }

        @Override
        public void onFinish(int downloadId, String path) {
            super.onFinish(downloadId, path);
            SelectTemplateFragment selectTemplateFragment = weakReference.get();
            if (selectTemplateFragment != null) {
                selectTemplateFragment.mLoadingMv.remove(data.getData());
                Log.d("SelectTemplateFragment", "下载完成");
                selectTemplateFragment.mAdapter.notifyDownloadingComplete(data, position);
            }
        }

        @Override
        public void onProgress(int downloadId, long soFarBytes, long totalBytes, long speed, int progress) {
            super.onProgress(downloadId, soFarBytes, totalBytes, speed, progress);
            Log.d("SelectTemplateFragment", "当前下载了" + (soFarBytes * 1.0f / totalBytes));
            SelectTemplateFragment selectTemplateFragment = weakReference.get();
            if (selectTemplateFragment != null) {
                selectTemplateFragment.mAdapter.updateProcess(
                        (SelectTemplateListAdapter.SelectTemplateListViewHolder) selectTemplateFragment.mRecyclerView.findViewHolderForAdapterPosition(position), progress,
                        position);
            }
        }

        @Override
        public void onError(BaseDownloadTask task, Throwable e) {
            super.onError(task, e);
            SelectTemplateFragment selectTemplateFragment = weakReference.get();
            if (selectTemplateFragment != null) {
                ToastUtil.showToast(selectTemplateFragment.mContext, R.string.material_downloading_failure);
                synchronized (tasks) {
                    for (FileDownloaderModel t : tasks) {//删除该套MV的所有Task
                        DownloaderManager.getInstance().deleteTaskByTaskId(t.getTaskId());
                    }
                    tasks.clear();
                }
                //清空已插入到数据库中的该套MV的信息
                DownloaderManager.getInstance().getDbController().deleteTaskById(data.getData().getId());
            }
        }
    }
}
