package com.lipl.ommcom.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.lipl.ommcom.R;
import com.lipl.ommcom.pojo.Comment;
import com.lipl.ommcom.util.Config;
import com.lipl.ommcom.util.CustomTextView;
import com.lipl.ommcom.util.Util;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

import java.util.List;

public class CommentListAdapter extends RecyclerView.Adapter<CommentListAdapter.ViewHolder> implements View.OnClickListener {

    public List<Comment> datas = null;
    private ImageLoader imageLoader;
    private OnItemClickListener mItemClickListener;
    private Context context;

    public CommentListAdapter(List<Comment> datas, Context context,
                              OnItemClickListener onItemClickListener) {
        this.datas = datas;
        this.mItemClickListener = onItemClickListener;
        imageLoader = ImageLoader.getInstance();
        this.context = context;
        imageLoader.init(ImageLoaderConfiguration.createDefault(context));
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.comment_list_item,viewGroup,false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        if(position % 2 == 0){
            viewHolder.layoutBody.setBackgroundColor(context.getResources().getColor(R.color.app_logo_color_blue));
        } else{
            viewHolder.layoutBody.setBackgroundColor(context.getResources().getColor(R.color.app_logo_color_maroon));
        }
        viewHolder.commentedBy.setText(datas.get(position).getName());
        viewHolder.comment.setText(datas.get(position).getComment());
        viewHolder.tvNewsPostedAt.setText(" " + Util.getTime(datas.get(position).getVerified_date()));

        String file_path = datas.get(position).getFile_path();

        if(datas.get(position).getIs_audio().trim().equalsIgnoreCase("1") ||
                datas.get(position).getIs_video().trim().equalsIgnoreCase("1") ||
                datas.get(position).getIs_image().trim().equalsIgnoreCase("1")){
            viewHolder.layoutFileToShow.setVisibility(View.VISIBLE);
        } else{
            viewHolder.layoutFileToShow.setVisibility(View.GONE);
        }

        if(datas.get(position).getIs_audio().trim().equalsIgnoreCase("1")){
            viewHolder.layoutAudio.setVisibility(View.VISIBLE);
            viewHolder.layoutVideo.setVisibility(View.GONE);
            viewHolder.layoutImage.setVisibility(View.GONE);
            try{
                setAudioToCommentRow(viewHolder.imgPlayAudio, file_path);
            } catch(Exception exception){
                Log.e("CommentListAdapter", "onBindViewHolder()", exception);
            }
        }

        if(datas.get(position).getIs_video().trim().equalsIgnoreCase("1")){
            viewHolder.layoutAudio.setVisibility(View.GONE);
            viewHolder.layoutVideo.setVisibility(View.VISIBLE);
            viewHolder.layoutImage.setVisibility(View.GONE);
            try {
                setVideoToCommentRow(viewHolder.imgPlayVideo, file_path);
            } catch(Exception exception){
                Log.e("CommentListAdapter", "onBindViewHolder()", exception);
            }
        }

        if(datas.get(position).getIs_image().trim().equalsIgnoreCase("1")){
            viewHolder.layoutAudio.setVisibility(View.GONE);
            viewHolder.layoutVideo.setVisibility(View.GONE);
            viewHolder.layoutImage.setVisibility(View.VISIBLE);
            try{
                setImageToCommentRow(viewHolder.image, file_path);
            } catch(Exception exception){
                Log.e("CommentListAdapter", "onBindViewHolder()", exception);
            }
        }
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout layoutBody;
        public CustomTextView commentedBy;
        public CustomTextView comment;
        public CustomTextView tvNewsPostedAt;
        public RelativeLayout mParentLayoutFile;
        public LinearLayout layoutAudio;
        public LinearLayout layoutVideo;
        public LinearLayout layoutImage;
        public ImageView imgPlayAudio;
        public ImageView imgPlayVideo;
        public ImageView image;
        public RelativeLayout layoutFileToShow;

        public ViewHolder(View view){
            super(view);
            layoutBody = (LinearLayout) view.findViewById(R.id.layoutBody);
            commentedBy = (CustomTextView) view.findViewById(R.id.commentedBy);
            comment = (CustomTextView) view.findViewById(R.id.comment);
            tvNewsPostedAt = (CustomTextView) view.findViewById(R.id.tvNewsPostedAt);
            mParentLayoutFile = (RelativeLayout) view.findViewById(R.id.layoutFileToShow);
            layoutAudio = (LinearLayout) view.findViewById(R.id.layoutAudio);
            layoutVideo = (LinearLayout) view.findViewById(R.id.layoutVideo);
            layoutImage = (LinearLayout) view.findViewById(R.id.layoutImage);
            imgPlayAudio = (ImageView) view.findViewById(R.id.imgPlay);
            imgPlayVideo = (ImageView) view.findViewById(R.id.imgPlayVideo);
            image = (ImageView) view.findViewById(R.id.image);
            layoutFileToShow = (RelativeLayout) view.findViewById(R.id.layoutFileToShow);
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

    private void setVideoToCommentRow(ImageView imgPlay, String file_path) throws Exception {

        if(file_path == null || file_path.trim().length() <= 0){
            return;
        }

        final String total_path = Config.IMAGE_DOWNLOAD_BASE_URL + Config.FOLDER_NEWS
                + Config.FOLDER_VIDEO + "/" + file_path;

        Log.i("Comment", "video File path : "+total_path);

//        final ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
//        final ImageView imgPlay = (ImageView) view.findViewById(R.id.imgPlay);
//        final VideoView myVideoView = (VideoView) view.findViewById(R.id.videoView);
//        MediaController mediaControls = new MediaController(context);
//        progressBar.setVisibility(View.VISIBLE);
//        try {
//            myVideoView.setMediaController(mediaControls);
//            myVideoView.setVideoURI(Uri.parse(total_path));
//        } catch (Exception e) {
//            Log.e("Error", e.getMessage());
//            e.printStackTrace();
//        }
//        myVideoView.requestFocus();
//        myVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//            public void onPrepared(MediaPlayer mediaPlayer) {
//                imgPlay.setVisibility(View.VISIBLE);
//            }
//        });
//        imgPlay.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                progressBar.setVisibility(View.GONE);
//                myVideoView.start();
//                imgPlay.setVisibility(View.GONE);
//            }
//        });
//        myVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//            @Override
//            public void onCompletion(MediaPlayer mp) {
//                imgPlay.setVisibility(View.VISIBLE);
//            }
//        });

        imgPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(total_path != null) {
                    Uri myUri = Uri.parse(total_path);
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(myUri, "video/*");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                } else{
                    Toast.makeText(context, "Some thing went wrong.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setAudioToCommentRow(ImageView imageViewPlay, String file_path) throws Exception {

        if(file_path == null || file_path.trim().length() <= 0){
            return;
        }

        final String total_file_path = Config.IMAGE_DOWNLOAD_BASE_URL + Config.FOLDER_NEWS + Config.FOLDER_VIDEO + "/" + file_path;

        imageViewPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(total_file_path != null) {
                    Uri myUri = Uri.parse(total_file_path);
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(myUri, "audio/*");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                } else{
                    Toast.makeText(context, "Some thing went wrong.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setImageToCommentRow(ImageView image, String file_path) throws Exception {

        if(file_path == null || file_path.trim().length() <= 0){
            return;
        }

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
        String image_url = Config.IMAGE_DOWNLOAD_BASE_URL + Config.FOLDER_NEWS +
                Config.getFolderForDP() + file_path;
        imageLoader.displayImage(image_url, image, options);
    }
}