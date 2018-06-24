package com.lipl.ommcom.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daimajia.easing.linear.Linear;
import com.lipl.ommcom.R;
import com.lipl.ommcom.pojo.CitizenJournalistVideos;
import com.lipl.ommcom.util.Config;
import com.lipl.ommcom.util.CustomTextView;
import com.lipl.ommcom.util.Util;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

import java.util.ArrayList;
import java.util.List;

public class VideoListAdapter extends RecyclerView.Adapter<VideoListAdapter.ViewHolder> implements View.OnClickListener {

    public List<CitizenJournalistVideos> datas = null;
    private ImageLoader imageLoader;
    private OnItemClickListener mItemClickListener;
    private boolean isMostViewed;

    public VideoListAdapter(List<CitizenJournalistVideos> datas, Context context,
                            OnItemClickListener onItemClickListener, boolean isMostViewed) {
        this.datas = datas;
        this.mItemClickListener = onItemClickListener;
        this.isMostViewed = isMostViewed;
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(context));
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.video_list_item,viewGroup,false);
        ViewHolder vh = new ViewHolder(view);
        vh.mainlayout.setOnClickListener(this);
        return vh;
    }
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        viewHolder.mainlayout.setTag(position);
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

        String title = datas.get(position).getDescription();
        String posted_by = datas.get(position).getName();
        String is_anonymous = datas.get(position).getIs_anonymous();
        viewHolder.videoTitle.setText(title);
        viewHolder.tvNewsPostedBy.setText(posted_by);
        String file_type = datas.get(position).getFile_type();
        String img_file_path = "";

        String postedAt = " " + Util.getTime(datas.get(position).getUpdated_at());
        viewHolder.tvNewsPostedAt.setText(postedAt);

        if(file_type != null){
            img_file_path = Util.getImageFilePathForCitizen(datas.get(position));
        }

        if(file_type != null && file_type.trim().equalsIgnoreCase("Video")){
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
        }

        if(file_type != null && file_type.trim().equalsIgnoreCase("Audio")
                && img_file_path == null || img_file_path.trim().length() <= 0){
            //Audio
            options = new DisplayImageOptions.Builder()
                    .showImageOnLoading(R.mipmap.ic_headset_white)//ic_stub
                    .showImageForEmptyUri(R.mipmap.ic_headset_white)//ic_empty
                    .showImageOnFail(R.mipmap.ic_headset_white)//ic_error
                    .cacheInMemory(true)
                    .cacheOnDisk(true)
                    .considerExifParams(true)
                    .bitmapConfig(Bitmap.Config.ARGB_8888)
                    .displayer(new SimpleBitmapDisplayer())
                    .build();
        }

        imageLoader.displayImage(img_file_path,
                viewHolder.mImageView, options);

        if(isMostViewed) {
            viewHolder.tvNumberOfViews.setVisibility(View.VISIBLE);
            viewHolder.layoutView.setVisibility(View.VISIBLE);
            String number_of_views = datas.get(position).getNews_count();
            if(number_of_views == null || number_of_views.trim().length() <= 0){
                number_of_views = "0";
            }
            viewHolder.tvNumberOfViews.setText(number_of_views + " views");
        } else {
            viewHolder.tvNumberOfViews.setVisibility(View.GONE);
            viewHolder.layoutView.setVisibility(View.GONE);
        }
    }
    @Override
    public int getItemCount() {
        return datas.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView mImageView;
        public CustomTextView videoTitle;
        public CustomTextView tvNewsPostedBy;
        public RelativeLayout mainlayout;
        public CustomTextView tvNumberOfViews;
        public CustomTextView tvNewsPostedAt;
        public LinearLayout layoutView;
        public ViewHolder(View view){
            super(view);
            mainlayout = (RelativeLayout) view.findViewById(R.id.mainlayout);
            mImageView = (ImageView) view.findViewById(R.id.imgVideoThumbnail);
            videoTitle = (CustomTextView) view.findViewById(R.id.videoTitle);
            tvNewsPostedBy = (CustomTextView) view.findViewById(R.id.tvNewsPostedBy);
            tvNumberOfViews = (CustomTextView) view.findViewById(R.id.tvNumberOfViews);
            tvNewsPostedAt = (CustomTextView) view.findViewById(R.id.tvNewsPostedAt);
            layoutView = (LinearLayout) view.findViewById(R.id.layoutView);
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

    public void setFilter(List<CitizenJournalistVideos> _datas, Context context,
                          OnItemClickListener onItemClickListener, boolean isMostViewed) {
        datas = new ArrayList<>();
        datas.addAll(_datas);
        notifyDataSetChanged();
    }
}
