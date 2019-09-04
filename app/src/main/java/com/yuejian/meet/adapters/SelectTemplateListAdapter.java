package com.yuejian.meet.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.aliyun.svideo.editor.effectmanager.EffectBody;
import com.aliyun.svideo.editor.effects.imv.IMVPreviewDialog;
import com.aliyun.svideo.sdk.external.struct.form.IMVForm;
import com.bumptech.glide.Glide;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.creation.MusicSelectTemplateActivity;
import com.yuejian.meet.utils.ScreenUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author : g000gle
 * @time : 2019/5/27 15:05
 * @desc : 选择视频模板 - 分类Fragment - 列表适配器
 */
public class SelectTemplateListAdapter extends RecyclerView.Adapter<SelectTemplateListAdapter.SelectTemplateListViewHolder> {

    private Context mContext;

    private static final int VIEW_TYPE_NO = 0;
    private static final int VIEW_TYPE_LOCAL = 1;
    private static final int VIEW_TYPE_REMOTE = 2;
    private static final int VIEW_TYPE_DOWNLOADING = 3;

    private List<EffectBody<IMVForm>> mDataList = new ArrayList<>();
    private ArrayList<IMVForm> mLoadingMV = new ArrayList<>(); //正在下载的的mv
    private Comparator<EffectBody<IMVForm>> mMVCompator = new IMVFormCompator();
    private SelectTemplateListAdapter.OnItemRightButtonClickListener mRightBtnClickListener;

    public SelectTemplateListAdapter(Context context) {
        mContext = context;
    }

    @Override
    public SelectTemplateListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_article_music_template, parent, false);
        return new SelectTemplateListAdapter.SelectTemplateListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SelectTemplateListViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        holder.mTvRightButton.setVisibility(View.VISIBLE);
        holder.progressView.setVisibility(View.GONE);
        switch (viewType) {
            case VIEW_TYPE_LOCAL:
                holder.mTvRightButton.setVisibility(View.INVISIBLE);
                holder.downloadFinish.setVisibility(View.VISIBLE);
                holder.updateData(position, mDataList.get(position));
                break;
            case VIEW_TYPE_REMOTE:
                holder.mTvRightButton.setBackgroundResource(com.aliyun.svideo.editor.R.drawable.aliyun_svideo_shape_caption_manager_bg);
                holder.updateData(position, mDataList.get(position));
                break;
            case VIEW_TYPE_DOWNLOADING:
                holder.mTvRightButton.setText(mContext.getResources().getString(com.aliyun.svideo.editor.R.string.downloading_effect_edit));
                holder.updateData(position, mDataList.get(position));
                break;
            default:
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    @Override
    public int getItemViewType(int position) {
        int type = VIEW_TYPE_NO;
        if (position >= 0 && position < mDataList.size()) {
            EffectBody<IMVForm> data = mDataList.get(position);
            if (data.isLocal()) {
                return VIEW_TYPE_LOCAL;
            } else if (data.isLoading()) {
                return VIEW_TYPE_DOWNLOADING;
            } else {
                return VIEW_TYPE_REMOTE;
            }
        }
        return type;
    }

    public void updateProcess(SelectTemplateListViewHolder viewHolder, int process, int position) {
        if (viewHolder != null && viewHolder.mPosition == position) {
            viewHolder.mTvRightButton.setBackgroundColor(Color.TRANSPARENT);
            viewHolder.progressView.setVisibility(View.VISIBLE);
            viewHolder.progressView.setProgress(process);
        }
    }

    public void notifyDownloadingStart(EffectBody<IMVForm> mvBody) {
        if (!mLoadingMV.contains(mvBody.getData())) {
            mLoadingMV.add(mvBody.getData());
            mvBody.setLoading(true);
        }
    }

    public synchronized void notifyDownloadingComplete(EffectBody<IMVForm> mvBody, int position) {
        mvBody.setLocal(true);
        mvBody.setLoading(false);
        mLoadingMV.remove(mvBody.getData());
        notifyDataSetChanged();
    }

    public void syncData(List<EffectBody<IMVForm>> syncDataList) {
        if (mLoadingMV.size() != 0 ) {
            //在没有下载任务的时候才进行同步，避免出现任务进度更新时的position错乱
            return;
        }
        if (syncDataList == null) {
            return;
        }
        ArrayList<EffectBody<IMVForm>> delList = new ArrayList<>();
        for (EffectBody<IMVForm> item : mDataList) {
            if (!syncDataList.contains(item)) {
                delList.add(item);
            }
        }
        mDataList.removeAll(delList);
        for (EffectBody<IMVForm> item : syncDataList) {
            if (!mDataList.contains(item)) {
                mDataList.add(item);
            }
        }
        Collections.sort(mDataList, mMVCompator);
        notifyDataSetChanged();
    }

    public class SelectTemplateListViewHolder extends RecyclerView.ViewHolder {

        ImageView templateImage;
        TextView templateTitle;
        ImageView downloadFinish;
        ProgressBar progressView;
        TextView mTvRightButton;
        private EffectBody<IMVForm> mData;
        private int mPosition;

        public SelectTemplateListViewHolder(View itemView) {
            super(itemView);
            templateImage = (ImageView) itemView.findViewById(R.id.iv_template_image);
            templateTitle = (TextView) itemView.findViewById(R.id.iv_template_title);

            downloadFinish = (ImageView)itemView.findViewById(R.id.iv_download_finish);
            progressView = (ProgressBar) itemView.findViewById(R.id.download_progress);
            mTvRightButton = (TextView)itemView.findViewById(R.id.tv_right_button);
            mTvRightButton.setText("下载");
            mTvRightButton.setOnClickListener(v -> {
                if (mRightBtnClickListener != null) {
                    if (mData.isLocal()) {
                        mRightBtnClickListener.onLocalItemClick(mPosition, mData);
                    } else {
                        mRightBtnClickListener.onRemoteItemClick(mPosition, mData);
                    }
                }
            });
            downloadFinish.setOnClickListener(v -> {
                if (mRightBtnClickListener != null) {
                    mRightBtnClickListener.onLocalItemClick(mPosition, mData);
                }
            });
            itemView.setOnClickListener(v -> {
                if (mData.getData().getPreviewMp4() != null && !"".equals(mData.getData().getPreviewMp4())) {
                    IMVPreviewDialog imvDialog = IMVPreviewDialog.newInstance(mData.getData().getPreviewMp4(),
                            mData.getData().getPreviewPic());
                    imvDialog.show(((MusicSelectTemplateActivity)mContext).getSupportFragmentManager(), "iMV");
                }
            });
        }

        void updateData(int position, EffectBody<IMVForm> data) {
            this.mData = data;
            this.mPosition = position;
            IMVForm paster = data.getData();
            templateTitle.setText(paster.getName());

            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) templateImage.getLayoutParams();
            float itemWidth = (ScreenUtils.getScreenWidth(templateImage.getContext()) - 20 * 3) / 2;
            layoutParams.width = (int) itemWidth;
            layoutParams.height = (int) (layoutParams.width / 0.618);
            templateImage.setLayoutParams(layoutParams);
            Glide.with(templateImage.getContext()).load(paster.getIcon()).into(templateImage);
        }
    }

    public interface OnItemRightButtonClickListener {
        void onRemoteItemClick(int position, EffectBody<IMVForm> data);

        void onLocalItemClick(int position, EffectBody<IMVForm> data);
    }

    public void setRightBtnClickListener(SelectTemplateListAdapter.OnItemRightButtonClickListener listener) {
        this.mRightBtnClickListener = listener;
    }

    class IMVFormCompator implements Comparator<EffectBody<IMVForm>> {
        @Override
        public int compare(EffectBody<IMVForm> o1, EffectBody<IMVForm> o2) {
            if (o1 == null && o2 == null) {
                return 0; // o1 = o2
            } else if (o1 == null && o2 != null) {
                return -1; // o1 < o2
            } else if (o1 != null && o2 == null) {
                return 1; //o1 > o2
            } else {
                if (o1.isLocal() && !o2.isLocal()) {
                    return -1; // o1 < o2
                } else if (!o1.isLocal() && o2.isLocal()) {
                    return 1;   //o1 > o2
                } else {//o1 is local && o2 is local
                    if (o1.getData() == null && o2.getData() == null) {
                        return 0; // o1 = o2
                    } else if (o1.getData() != null && o2.getData() == null) {
                        return 1;   //o1 > o2
                    } else if (o1.getData() == null && o2.getData() != null) {
                        return -1; //o1 < o2
                    } else {
                        IMVForm ep1 = o1.getData();
                        IMVForm ep2 = o2.getData();
                        if (ep1.getId() < ep2.getId()) {
                            return -1; // o1 < o2
                        } else if (ep1.getId() == ep2.getId()) {
                            return 0;   // o1 = o2
                        } else {
                            return 1;   // o1 > o2
                        }
                    }
                }
            }
        }
    }
}
