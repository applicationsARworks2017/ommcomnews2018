package com.lipl.ommcom.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Parcel;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lipl.ommcom.R;
import com.lipl.ommcom.activity.AllOdishaNewsList;
import com.lipl.ommcom.activity.HomeActivity;
import com.lipl.ommcom.activity.NewsDetailsActivity;
import com.lipl.ommcom.pojo.News;
import com.lipl.ommcom.pojo.OdishaNews;
import com.lipl.ommcom.util.Config;
import com.lipl.ommcom.util.Util;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Amaresh on 7/11/18.
 */

public class AllOdishaNewsAdapter extends  RecyclerView.Adapter<AllOdishaNewsAdapter.ViewHolder> {
    ViewHolder vh;
    Context _context;
    private ImageLoader imageLoader;
    private DisplayImageOptions options;


    ArrayList<OdishaNews> v_list;

    public AllOdishaNewsAdapter(AllOdishaNewsList allOdishaNewsList, ArrayList<OdishaNews> arrayList_odishanews) {

        this._context = allOdishaNewsList;
        this.v_list = arrayList_odishanews;
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(_context));

    }


    @Override
    public void onBindViewHolder(AllOdishaNewsAdapter.ViewHolder holder, int position) {
        final OdishaNews _pos = v_list.get(position);

        holder.tvTitle.setTag(position);
        holder.tvNewsPostedAt.setTag(position);
        holder.imgView.setTag(position);
        holder.imgPlay.setTag(position);
        holder.mainlayout.setTag(position);

        holder.tvTitle.setText(_pos.getName());
        holder.tvNewsPostedAt.setText(Util.getTime(_pos.getApproved_date()));
       // String image_url = Util.getImageFilePathForNews(newsArrayList.get(position), viewHolder.mImgPlay);
        imageLoader.displayImage(_pos.getFeatured_image(),
                vh.imgView, options);
        if(_pos.getIs_video().contentEquals("0")){
            holder.imgPlay.setVisibility(View.GONE);
        }
        else{
            holder.imgPlay.setVisibility(View.VISIBLE);
        }
        holder.mainlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                News featured_news_new = new News(Parcel.obtain());
                featured_news_new.setSlug(_pos.getSlug());
                _context.getSharedPreferences(Config.SHARED_PREFERENCE_KEY, MODE_PRIVATE).edit().putInt(Config.SP_IS_FROM_CHILD_ACTIVITY, 1).commit();
                Intent intent = new Intent(_context, NewsDetailsActivity.class);
                intent.putExtra("news", featured_news_new);
                _context.startActivity(intent);
            }
        });
        /*

        * */
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.allnews, viewGroup, false);
        vh = new ViewHolder(view);

        return vh;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return v_list.size();
    }

    @Override
    public int getItemViewType(int position) {

        return position;
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle,tvNewsPostedAt;
        ImageView imgView,imgPlay;
        LinearLayout visitlin;
        RelativeLayout rel_vlist;
        RelativeLayout mainlayout;

        public ViewHolder(View view) {
            super(view);
            tvTitle = (TextView) view.findViewById(R.id.tvTitle);
            tvNewsPostedAt = (TextView) view.findViewById(R.id.tvNewsPostedAt);
            imgView = (ImageView) view.findViewById(R.id.imgView);
            imgPlay = (ImageView) view.findViewById(R.id.imgPlay);
            mainlayout = (RelativeLayout) view.findViewById(R.id.mainlayout);

        }
    }
}
