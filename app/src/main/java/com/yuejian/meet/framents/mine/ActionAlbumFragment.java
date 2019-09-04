package com.yuejian.meet.framents.mine;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.yuejian.meet.R;
import com.yuejian.meet.adapters.base.AdapterHolder;
import com.yuejian.meet.adapters.base.FKAdapter;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.bean.PhotoItem;
import com.yuejian.meet.common.Constants;
import com.yuejian.meet.framents.base.BaseFragment;
import com.yuejian.meet.utils.DensityUtils;
import com.yuejian.meet.utils.StringUtils;
import com.yuejian.meet.utils.Utils;
import com.yuejian.meet.widgets.InnerGridView;
import com.yuejian.meet.widgets.springview.DefaultFooter;
import com.yuejian.meet.widgets.springview.SpringView;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * Created by Administrator on 2018/1/18/018.
 */

public class ActionAlbumFragment extends BaseFragment implements SpringView.OnFreshListener {
    private List<PhotoItem> dataSource = new ArrayList<>();
    private PhotoAdapter adapter = null;
    private SpringView springView = null;
    private String actionId = "";
    private String customerId = "";


    @Override
    protected View inflaterView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        View inflate = inflater.inflate(R.layout.fragment_action_album, null);
        springView = (SpringView) inflate.findViewById(R.id.spring_view);
        ListView albumListView = (ListView) inflate.findViewById(R.id.photo_list);
        springView.setFooter(new DefaultFooter(getActivity()));
        springView.setListener(this);
        adapter = new PhotoAdapter();
        albumListView.setAdapter(adapter);
        customerId= getActivity().getIntent().getStringExtra(Constants.KEY_CUSTOMER_ID);
        return inflate;
    }

    @Override
    protected void initData() {
        super.initData();
        getActionPhotos(actionId);
    }

    public void getActionPhotos(String aId) {
        if (StringUtils.isNotEmpty(customerId)) {
            final HashMap<String, Object> params = new HashMap<>();
            params.put("customer_id", customerId);
            params.put("action_id", aId);
            apiImp.getActionPhotos(params, this, new DataIdCallback<String>() {
                @Override
                public void onSuccess(String data, int id) {
                    try {
                        JSONObject object = new JSONObject(data);
                        actionId = object.optString("action_id");
                        String string = object.getString("photos");
                        List<PhotoItem> photos = JSON.parseArray(string, PhotoItem.class);
                        if (photos.isEmpty() && dataSource.isEmpty()) {
                            getRootView().findViewById(R.id.no_data_tips_layout).setVisibility(View.VISIBLE);
                        } else {
                            getRootView().findViewById(R.id.no_data_tips_layout).setVisibility(View.GONE);
                        }

                        if (!photos.isEmpty()) {
                            PhotoItem item;
                            if (dataSource.isEmpty()) {
                                item = new PhotoItem();
                                item.photos = new ArrayList<>();
                                item.create_time = photos.get(0).create_time;
                            } else {
                                item = dataSource.get(dataSource.size() - 1);
                            }
                            if (photos.size() > 0) {
                                for (int i = 0; i < photos.size(); i++) {
                                    Calendar calendar1 = Calendar.getInstance();
                                    calendar1.setTimeInMillis(item.create_time);

                                    Calendar calendar2 = Calendar.getInstance();
                                    calendar2.setTimeInMillis(photos.get(i).create_time);

                                    if (calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR)
                                            && calendar1.get(Calendar.MONTH) == calendar2.get(Calendar.MONTH)) {
                                        item.photos.add(photos.get(i).photo);
                                        if(i == photos.size() -1) {
                                            dataSource.add(item);
                                        }
                                    } else {
                                        if(!dataSource.contains(item)){
                                            dataSource.add(item);
                                        }
                                        item = new PhotoItem();
                                        item.create_time = photos.get(i).create_time;
                                        item.photos = new ArrayList<>();
                                        item.photos.add(photos.get(i).photo);
                                        if(i == photos.size() -1) {
                                            dataSource.add(item);
                                        }
                                    }

                                }
                            } else {
                                dataSource.add(item);
                            }
                            adapter.notifyDataSetChanged();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    springView.onFinishFreshAndLoad();
                }

                @Override
                public void onFailed(String errCode, String errMsg, int id) {

                }
            });
        } else {
            springView.onFinishFreshAndLoad();
        }
    }

    @Override
    public void onRefresh() {
    }

    @Override
    public void onLoadmore() {
        getActionPhotos(actionId);
    }


    private class PhotoAdapter extends BaseAdapter {
        private SimpleDateFormat dateFormat = null;
        private int itemWidth = 0;
        private Activity activity = null;

        public PhotoAdapter() {
            activity = getActivity();
            dateFormat = new SimpleDateFormat("yyyy年MM月", Locale.getDefault());
            itemWidth = (DensityUtils.getScreenW(activity) - DensityUtils.dip2px(activity, 34)) / 3;
        }

        @Override
        public int getCount() {
            return dataSource.size();
        }

        @Override
        public Object getItem(int position) {
            return dataSource.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = View.inflate(activity, R.layout.photo_album_item, null);
                holder.dateView = (TextView) convertView.findViewById(R.id.date);
                holder.photosGrid = (InnerGridView) convertView.findViewById(R.id.photo_grid);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            final PhotoItem item = dataSource.get(position);
            holder.dateView.setText(dateFormat.format(new Date(item.create_time)));
            holder.photosGrid.setColumnWidth(itemWidth);
            holder.photosGrid.setNumColumns(3);
            InnerGridAdapter gvImgAdapter = new InnerGridAdapter(holder.photosGrid, item.photos, R.layout.layout_dynamic_img_item);
            holder.photosGrid.setAdapter(gvImgAdapter);
            holder.photosGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Utils.displayImages(activity, item.photos, position, null);
                }
            });
            gvImgAdapter.refresh(item.photos);
            return convertView;
        }

        private class ViewHolder {
            TextView dateView = null;
            InnerGridView photosGrid = null;
        }
    }
//[{"create_time":1515396014000,"photo":"http:\/\/yuejian-app.oss-cn-shenzhen.aliyuncs.com\/photoalbum\/2775011515396013351.jpg"}]

    private class InnerGridAdapter extends FKAdapter<String> {
        private int mWidth = 0;
        private ColorDrawable placeHolder = new ColorDrawable(Color.parseColor("#e8e8e8"));

        public InnerGridAdapter(AbsListView view, List<String> item, int itemLayoutId) {
            super(view, item, itemLayoutId);
            mWidth = (DensityUtils.getScreenW(mCxt) - DensityUtils.dip2px(mCxt, 34)) / 3;
        }

        @Override
        public void convert(AdapterHolder helper, String url, boolean isScrolling, int position) {
            super.convert(helper, url, isScrolling, position);
            helper.getView(R.id.img_dynamic_pic_item).getLayoutParams().width = mWidth;
            helper.getView(R.id.img_dynamic_pic_item).getLayoutParams().height = mWidth;
            Glide.with(mCxt).load(url).asBitmap()
                    .centerCrop()
                    .placeholder(placeHolder)
                    .error(R.mipmap.load_error)
                    .into((ImageView) helper.getView(R.id.img_dynamic_pic_item));
        }
    }

    @Override
    public void onUserVisible() {
        super.onUserVisible();
        dataSource.clear();
        getActionPhotos("");
    }
}
