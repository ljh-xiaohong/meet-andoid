package com.yuejian.meet.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.home.ArticleInfoActivity;
import com.yuejian.meet.bean.Article;
import com.yuejian.meet.utils.StringUtils;
import com.yuejian.meet.utils.Utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by zh02 on 2017/8/25.
 */

public class ArticleAdapter extends BaseAdapter {
    private List<Article> dataSource = new ArrayList<>();
    private Context context;

    public ArticleAdapter(Context context, List<Article> dataSource) {
        this.context = context;
        if (dataSource != null) {
            this.dataSource = dataSource;
        }
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
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(context, R.layout.item_essay, null);
            holder.title = (TextView) convertView.findViewById(R.id.essay_title);
            holder.createTime = (TextView) convertView.findViewById(R.id.essay_create_time);
            holder.source = (TextView) convertView.findViewById(R.id.essay_source);
            holder.pic = (ImageView) convertView.findViewById(R.id.essay_pic);
            holder.rootView = convertView;
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final Article article = dataSource.get(position);
        holder.source.setText(article.article_from);
        holder.createTime.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(new Date(Long.valueOf(article.create_time))));
        holder.title.setText(article.article_title);
        Glide.with(context).load(article.article_photo).into(holder.pic);
        holder.pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (context instanceof Activity) {
                    List<String> list = new ArrayList<String>();
                    list.add(article.article_photo);
                    Utils.displayImages((Activity) context, list, 0, null);
                }
            }
        });
        holder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ArticleInfoActivity.class);
                intent.putExtra("article", article);
                context.startActivity(intent);
            }
        });
        return convertView;
    }

    private class ViewHolder {
        TextView title;
        TextView createTime;
        TextView source;
        ImageView pic;
        View rootView;
    }
}
