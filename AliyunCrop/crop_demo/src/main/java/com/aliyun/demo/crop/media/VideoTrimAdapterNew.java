package com.aliyun.demo.crop.media;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.aliyun.demo.crop.R;
import com.aliyun.svideo.base.widget.SquareFrameLayout;
import com.aliyun.video.common.utils.DensityUtils;

import java.lang.ref.SoftReference;
import java.util.ArrayList;

public class VideoTrimAdapterNew extends BaseAdapter {
    private Context mContext;
    private int screenWidth;
    private ArrayList<SoftReference<Bitmap>> mBitmaps;

    public VideoTrimAdapterNew(Context context, ArrayList<SoftReference<Bitmap>> bitmaps) {

        mContext = context;

        this.mBitmaps = bitmaps;

        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);

        assert wm != null;
        int oldScreenWidth = wm.getDefaultDisplay().getWidth();

        screenWidth = oldScreenWidth - DensityUtils.dip2px(mContext, 40);


    }

    /**
     * 添加一个元素
     */
    public void add(SoftReference<Bitmap> data) {
        if (mBitmaps == null) {
            mBitmaps = new ArrayList<>();
        }
        mBitmaps.add(data);
        notifyDataSetChanged();


    }

    private int getItemCount() {

        return mBitmaps == null ? 0 : mBitmaps.size();
    }


    @Override
    public int getCount() {
        return getItemCount();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        VideoTrimAdapterNew.ViewHolder holder = null;
        if (convertView == null) {
            holder = new VideoTrimAdapterNew.ViewHolder();

            convertView = LayoutInflater.from(mContext).inflate(R.layout.aliyun_svideo_item_qupai_trim_video_thumbnail, parent, false);
            holder.thumblayout = (SquareFrameLayout) convertView.findViewById(R.id.aliyun_video_tailor_frame);
            holder.thumbImage = (ImageView) convertView.findViewById(R.id.aliyun_video_tailor_img_item);
            convertView.setTag(holder);
        } else {
            holder = (VideoTrimAdapterNew.ViewHolder) convertView.getTag();

        }

        ViewGroup.LayoutParams lParam = holder.thumblayout.getLayoutParams();

        lParam.width = screenWidth / 10;
        lParam.height = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.creation_progress_left).getHeight()-10;

        holder.thumblayout.setLayoutParams(lParam);

        Bitmap bitmap = mBitmaps.get(position).get();
        if (bitmap != null) {
            holder.thumbImage.setImageBitmap(bitmap);
        }


        return convertView;
    }


    class ViewHolder {
        private SquareFrameLayout thumblayout;
        private ImageView thumbImage;
    }
}
