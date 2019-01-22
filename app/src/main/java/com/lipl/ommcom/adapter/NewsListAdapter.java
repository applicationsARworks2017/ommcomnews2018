package com.lipl.ommcom.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lipl.ommcom.R;
import com.lipl.ommcom.pojo.News;
import com.lipl.ommcom.util.Util;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

import java.util.ArrayList;

public class NewsListAdapter extends RecyclerView.Adapter<NewsListAdapter.ViewHolder> implements View.OnClickListener {

    public ArrayList<News> newsArrayList = null;
    private ImageLoader imageLoader;
    private OnItemClickListener mItemClickListener;

    public NewsListAdapter(ArrayList<News> newsArrayList, Context context,
                           OnItemClickListener onItemClickListener) {
        this.newsArrayList = newsArrayList;
        this.mItemClickListener = onItemClickListener;
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(context));
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item,viewGroup,false);
        ViewHolder vh = new ViewHolder(view);
        vh.mainlayout.setOnClickListener(this);
        return vh;
    }
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int position) {
        viewHolder.mTextView.setText(newsArrayList.get(position).getName());
        viewHolder.mainlayout.setTag(position);
        viewHolder.tvNewsPostedBy.setText(" " + newsArrayList.get(position).getJounalist_name());
        String postedAt = " " + Util.getTime(newsArrayList.get(position).getApproved_date());
        viewHolder.tvNewsPostedAt.setText(" " + postedAt);
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.stub)//ic_stub
                .showImageForEmptyUri(R.drawable.empty)//ic_empty
                .showImageOnFail(R.drawable.error)//ic_error
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.ARGB_8888)
                .displayer(new SimpleBitmapDisplayer())
                .build();

        String isVideo = newsArrayList.get(position).getIs_video();
        if(isVideo != null && isVideo.trim().length() > 0 && isVideo.trim().equalsIgnoreCase("1")){
            options = new DisplayImageOptions.Builder()
                    .showImageOnLoading(R.drawable.stub)//ic_stub
                    .showImageForEmptyUri(R.drawable.video_default)//ic_empty
                    .showImageOnFail(R.drawable.video_default)//ic_error
                    .cacheInMemory(true)
                    .cacheOnDisk(true)
                    .considerExifParams(true)
                    .bitmapConfig(Bitmap.Config.ARGB_8888)
                    .displayer(new SimpleBitmapDisplayer())
                    .build();

            viewHolder.mImgPlay.setVisibility(View.VISIBLE);
        } else {
            options = new DisplayImageOptions.Builder()
                    .showImageOnLoading(R.drawable.stub)//ic_stub
                    .showImageForEmptyUri(R.drawable.empty)//ic_empty
                    .showImageOnFail(R.drawable.error)//ic_error
                    .cacheInMemory(true)
                    .cacheOnDisk(true)
                    .considerExifParams(true)
                    .bitmapConfig(Bitmap.Config.ARGB_8888)
                    .displayer(new SimpleBitmapDisplayer())
                    .build();
            viewHolder.mImgPlay.setVisibility(View.GONE);
        }

        String image_url = "";
        if(newsArrayList.get(position) != null) {
            image_url = Util.getImageFilePathForNews(newsArrayList.get(position), viewHolder.mImgPlay);
            imageLoader.displayImage(image_url,
                    viewHolder.mImageView, options);
        }
    }
    @Override
    public int getItemCount() {
        return newsArrayList.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextView;
        public TextView tvNewsPostedBy;
        public TextView tvNewsPostedAt;
        public ImageView mImageView;
        public RelativeLayout mainlayout;
        public ImageView mImgPlay;

        public ViewHolder(View view){
            super(view);
            mTextView = (TextView) view.findViewById(R.id.tvTitle);
            tvNewsPostedBy = (TextView) view.findViewById(R.id.tvNewsPostedBy);
            tvNewsPostedAt = (TextView) view.findViewById(R.id.tvNewsPostedAt);
            mImageView = (ImageView) view.findViewById(R.id.imgView);
            mainlayout = (RelativeLayout) view.findViewById(R.id.mainlayout);
            mImgPlay = (ImageView) view.findViewById(R.id.imgPlay);
        }
    }

    @Override
    public void onClick(View view) {
        final int viewId = view.getId();
        if (viewId == R.id.mainlayout) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(view, (Integer) view.getTag());
            }
        }
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    public void setFilter(ArrayList<News> _newsArrayList, Context context,
                          OnItemClickListener onItemClickListener) {
        newsArrayList = new ArrayList<>();
        newsArrayList.addAll(_newsArrayList);
        notifyDataSetChanged();
    }
}
