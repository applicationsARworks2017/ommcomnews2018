package com.lipl.ommcom.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.commonsware.cwac.anddown.AndDown;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.lipl.ommcom.R;
import com.lipl.ommcom.activity.VideoPlayerActivity;
import com.lipl.ommcom.fliputil.AphidLog;
import com.lipl.ommcom.fliputil.UI;
import com.lipl.ommcom.pojo.CitizenJournalistVideos;
import com.lipl.ommcom.pojo.FImageListItem;
import com.lipl.ommcom.pojo.News;
import com.lipl.ommcom.pojo.NewsDetailsForFlipModel;
import com.lipl.ommcom.pojo.NewsVideo;
import com.lipl.ommcom.util.Config;
import com.lipl.ommcom.util.MyTagHandler;
import com.lipl.ommcom.util.SliderViewCustom;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Android Luminous on 5/9/2016.
 */
public class CJFlipAdapter extends BaseAdapter implements BaseSliderView.OnSliderClickListener,
        ViewPagerEx.OnPageChangeListener {
    private LayoutInflater inflater;

    private int repeatCount = 1;

    private List<NewsDetailsForFlipModel> forFlipModelList;
    private String title;
    private DisplayImageOptions options;
    private ImageLoader imageLoader;
    private String posted_time;
    private String posted_by;
    private Context context;
    private List<FImageListItem> imageListItems;
    private OnShareClick onShareClick;
    private OnCommentClick onCommentClick;
    private CitizenJournalistVideos mNews;
    private int facebookCount;
    private int twitterCount;
    private int gplusCount;

    public CJFlipAdapter(Context context, List<NewsDetailsForFlipModel> forFlipModelList,
                         String title, String posted_time, String posted_by, List<FImageListItem> imageListItems
    , OnShareClick onShareClick, OnCommentClick onCommentClick, CitizenJournalistVideos news,
                         int facebookCount, int twitterCount, int gplusCount) {
        inflater = LayoutInflater.from(context);
        this.forFlipModelList = forFlipModelList;
        this.title = title;
        this.posted_time = posted_time;
        this.context = context;
        this.posted_by = posted_by;
        this.imageListItems = imageListItems;
        this.onShareClick = onShareClick;
        this.onCommentClick = onCommentClick;
        this.mNews = news;
        this.facebookCount = facebookCount;
        this.twitterCount = twitterCount;
        this.gplusCount = gplusCount;

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

        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(context));
    }

    @Override
    public int getCount() {
        return forFlipModelList.size() * repeatCount;
    }

    public int getRepeatCount() {
        return repeatCount;
    }

    public void setRepeatCount(int repeatCount) {
        this.repeatCount = repeatCount;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        View layout = convertView;
        if (convertView == null) {
            layout = inflater.inflate(R.layout.complex3, null);
            AphidLog.d("created new view from adapter: %d", position);
        }

        ImageView imgComment = UI.<ImageView>findViewById(layout, R.id.imgComment);
        imgComment.setVisibility(View.GONE);

        RelativeLayout d_layout = UI.<RelativeLayout>findViewById(layout, R.id.d_layout);
        d_layout.setVisibility(View.VISIBLE);
        final NewsDetailsForFlipModel data = forFlipModelList.get(position % forFlipModelList.size());
        RelativeLayout description_layout = UI.<RelativeLayout>findViewById(layout, R.id.d_layout);
        RelativeLayout slider_image = UI.<RelativeLayout>findViewById(layout, R.id.slider_image);
        RelativeLayout mainl = UI.<RelativeLayout>findViewById(layout, R.id.mainl);

        TextView tvNewsCount = UI.<TextView>findViewById(layout, R.id.tvNewsCount);
        String news_count = "";
        if(mNews != null){
            news_count = mNews.getNews_count() + "";
            tvNewsCount.setVisibility(View.VISIBLE);
            tvNewsCount.setText(news_count+ " Views");
        } else {
            tvNewsCount.setVisibility(View.GONE);
        }

        TextView hint = UI.<TextView>findViewById(layout, R.id.hint);
        if(position == 0){
            if(forFlipModelList.size() > 1) {
                hint.setVisibility(View.VISIBLE);
            } else{
                hint.setVisibility(View.INVISIBLE);
            }
            slider_image.setVisibility(View.VISIBLE);
            description_layout.setVisibility(View.VISIBLE);
            mainl.setVisibility(View.VISIBLE);
            SliderLayout mDemoSlider =  UI.<SliderLayout>findViewById(layout, R.id.slider);

//            TextView tvFacebookCount = UI.<TextView>findViewById(layout, R.id.tvFacebookCount);
//            tvFacebookCount.setText(facebookCount+"");
//            TextView tvTwitterCount = UI.<TextView>findViewById(layout, R.id.tvTwitterCount);
//            tvTwitterCount.setText(twitterCount+"");
//            TextView tvGPlusCount = UI.<TextView>findViewById(layout, R.id.tvGPlusCount);
//            tvGPlusCount.setText(gplusCount+"");

            HashMap<String,NewsVideo> file_maps = new HashMap<String, NewsVideo>();
            for(int i = 0; i < imageListItems.size(); i++){
                FImageListItem newsImage = imageListItems.get(i);
                NewsVideo newsImage1 = new NewsVideo(Parcel.obtain());
                if(newsImage.getIs_video() != null
                        && newsImage.getIs_video().trim().length() > 0
                        && newsImage.getIs_video().trim().equalsIgnoreCase("1")){
                    newsImage1.setIs_video("1");
                } else if(newsImage.getIs_audio() != null
                        && newsImage.getIs_audio().trim().length() > 0
                        && newsImage.getIs_audio().trim().equalsIgnoreCase("1")){
                    newsImage1.setIs_audio("1");
                } else {
                    newsImage1.setIs_video("0");
                    newsImage1.setIs_audio("0");
                }
                newsImage1.setFile_link(newsImage.getFile_path());
                file_maps.put(imageListItems.get(i).getName(), newsImage1);
            }

            for(String name : file_maps.keySet()){
                // initialize a SliderLayout

                String img_file = "";
                boolean show_play = false;
                if(file_maps.get(name).getIs_video() != null
                        && file_maps.get(name).getIs_video().trim().length() > 0
                        && file_maps.get(name).getIs_video().trim().equalsIgnoreCase("1")) {
                    String[] io = file_maps.get(name).getFile_link().split("\\.");
                    if (io != null && io.length > 0) {
                        img_file = Config.IMAGE_DOWNLOAD_BASE_URL + Config.FOLDER_CITIZEN_NEWS
                                + Config.FOLDER_VIDEO
                                + "/" + io[0] + ".jpg";
                        show_play = true;
                    }
                } else if(file_maps.get(name).getIs_audio() != null
                        && file_maps.get(name).getIs_audio().trim().length() > 0
                        && file_maps.get(name).getIs_audio().trim().equalsIgnoreCase("1")) {
                    String[] io = file_maps.get(name).getFile_link().split("\\.");
                    if (io != null && io.length > 0) {
                        img_file = Config.IMAGE_DOWNLOAD_BASE_URL + Config.FOLDER_CITIZEN_NEWS
                                + Config.FOLDER_VIDEO
                                + "/" + io[0] + ".jpg";
                        show_play = true;
                    }
                } else{
                    img_file = Config.IMAGE_DOWNLOAD_BASE_URL + Config.FOLDER_CITIZEN_NEWS +
                            Config.getFolderForDP() + file_maps.get(name).getFile_link();
                    show_play = false;
                }
                SliderViewCustom textSliderView = new SliderViewCustom(context, show_play);
                if(file_maps.get(name).getIs_video() != null
                        && file_maps.get(name).getIs_video().trim().length() > 0
                        && file_maps.get(name).getIs_video().trim().equalsIgnoreCase("1")){
                    //Video
                    textSliderView
                            .description(name)
                            .image(img_file)
                            .empty(R.drawable.empty)
                            .error(R.drawable.error)
                            .setScaleType(BaseSliderView.ScaleType.Fit)
                            .setOnSliderClickListener(this);

                } else if(file_maps.get(name).getIs_audio() != null
                        && file_maps.get(name).getIs_audio().trim().length() > 0
                        && file_maps.get(name).getIs_audio().trim().equalsIgnoreCase("1")){
                    //Audio
                    textSliderView
                            .description(name)
                            .image(R.mipmap.ic_headset_black)
                            .empty(R.mipmap.ic_headset_black)
                            .error(R.mipmap.ic_headset_black)
                            .setScaleType(BaseSliderView.ScaleType.Fit)
                            .setOnSliderClickListener(this);

                } else{
                    //Image
                    textSliderView
                            .description(name)
                            .image(img_file)
                            .empty(R.drawable.empty)
                            .error(R.drawable.error)
                            .setScaleType(BaseSliderView.ScaleType.Fit)
                            .setOnSliderClickListener(this);

                }

                //add your extra information
                textSliderView.bundle(new Bundle());
                Bundle bundle = textSliderView.getBundle();
                bundle.putString("is_video", file_maps.get(name).getIs_video());
                bundle.putString("is_audio", file_maps.get(name).getIs_audio());
                String video_url = Config.IMAGE_DOWNLOAD_BASE_URL + Config.FOLDER_CITIZEN_NEWS
                        + Config.FOLDER_VIDEO
                        + "/" +file_maps.get(name).getFile_link();
                bundle.putString("video_url", video_url);
                bundle.putString("audio_url", video_url);
                bundle.putString("extra",name);

                mDemoSlider.addSlider(textSliderView);
            }
            mDemoSlider.addOnPageChangeListener(this);
            if(file_maps.size() > 1){
                mDemoSlider.setCustomAnimation(new DescriptionAnimation());
                mDemoSlider.setDuration(4000);
                mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
                mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
                mDemoSlider.setIndicatorVisibility(PagerIndicator.IndicatorVisibility.Visible);
            } else{
                mDemoSlider.setIndicatorVisibility(PagerIndicator.IndicatorVisibility.Invisible);
                mDemoSlider.stopAutoCycle();
            }

        } else{
            hint.setVisibility(View.INVISIBLE);
            slider_image.setVisibility(View.GONE);
            description_layout.setVisibility(View.GONE);
            mainl.setVisibility(View.GONE);
        }

        String markdownString = "";
        String _title = "";
        String _short_description = "";
        String posted_by = "";
        String posted_at = "";
        Spannable spanString = new SpannableString("");
        Spannable spanStringSd = new SpannableString("");
        Spannable paspanString = new SpannableString("");
        Spannable pbspanString = new SpannableString("");
        if(position == 0) {
            String[] ret = data.getText_to_show().split("\n");
            _title = "";
            if (ret.length > 0) {
                _title = ret[0] + "\n";
            }
            posted_by = "";
            if (ret.length > 1) {
                posted_by = ret[1] + ", ";
            }
            posted_at = "";
            if (ret.length > 2) {
                posted_at = ret[2];
            }
            _short_description = "";
            if (ret.length > 3) {
                _short_description = "\n\n" + ret[3] + "\n\n";
            }

            Typeface typeface = Typeface.createFromAsset(context.getAssets(),
                    context.getResources().getString(R.string.text_font_family_for_news_long_desc));

            spanString = new SpannableString(_title);
            spanString.setSpan(new ForegroundColorSpan(Color.BLACK), 0, _title.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            spanString.setSpan(new RelativeSizeSpan(1.8f), 0, _title.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            spanString.setSpan(new StyleSpan(typeface.getStyle()), 0, _title.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            pbspanString = new SpannableString(posted_by);
            pbspanString.setSpan(new ForegroundColorSpan(Color.BLACK), 0, posted_by.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            pbspanString.setSpan(new RelativeSizeSpan(0.9f), 0, posted_by.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            pbspanString.setSpan(new StyleSpan(typeface.getStyle()), 0, posted_by.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            paspanString = new SpannableString(posted_at);
            paspanString.setSpan(new ForegroundColorSpan(Color.BLACK), 0, posted_at.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            paspanString.setSpan(new RelativeSizeSpan(0.9f), 0, posted_at.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            paspanString.setSpan(new StyleSpan(typeface.getStyle()), 0, posted_at.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            spanStringSd = new SpannableString(_short_description);
            spanStringSd.setSpan(new ForegroundColorSpan(Color.BLACK), 0,
                    _short_description.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            spanStringSd.setSpan(new RelativeSizeSpan(0.9f), 0,
                    _short_description.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            spanStringSd.setSpan(new StyleSpan(typeface.getStyle()), 0,
                    _short_description.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            if (ret.length > 4) {
                markdownString = ret[ret.length - 1];
            }

            imgComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onCommentClick.onCommentClick();
                }
            });
            ImageView imgShare = UI.<ImageView>findViewById(layout, R.id.imgShare);
            imgShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onShareClick.onShareClick(mNews);
                }
            });

        } else {
            markdownString = data.getText_to_show();
        }

        TextView markdownView = UI.<TextView>findViewById(layout, R.id.description_with_img_only);
        AndDown converter=new AndDown();

        String cooked = converter.markdownToHtml(markdownString);
        CharSequence cs = TextUtils.concat(spanString, pbspanString, paspanString, spanStringSd, Html.fromHtml(cooked, null, new MyTagHandler()));
        markdownView.setText(cs);
        markdownView.setTypeface(Typeface.createFromAsset(context.getAssets(),
                context.getResources().getString(R.string.text_font_family_for_news_long_desc)));

        int current_page = position + 1;
        String page_number = "Page "+current_page+" of "+forFlipModelList.size();
        TextView pageNumber = UI
                .<TextView>findViewById(layout, R.id.pageNumber);
        pageNumber.setVisibility(View.VISIBLE);
        pageNumber.setText(page_number);

        return layout;
    }

    public void removeData(int index) {
        if (forFlipModelList.size() > 1) {
            forFlipModelList.remove(index);
        }
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {
        //Toast.makeText(context,slider.getBundle().get("extra") + "", Toast.LENGTH_SHORT).show();
        String is_video = (String) slider.getBundle().get("is_video");
        if(is_video != null && is_video.trim().length() > 0 && is_video.trim().equalsIgnoreCase("1")){
            String video_url = (String) slider.getBundle().get("video_url");

            if(video_url != null && video_url.trim().length() > 0) {
                Intent intent = new Intent(context, VideoPlayerActivity.class);
                intent.putExtra("video_url", video_url);
                context.startActivity(intent);
            }
        }

        String is_audio = (String) slider.getBundle().get("is_audio");
        if(is_audio != null && is_audio.trim().length() > 0 && is_audio.trim().equalsIgnoreCase("1")){
            String audio_url = (String) slider.getBundle().get("audio_url");

            if(audio_url != null && audio_url.trim().length() > 0) {
                Uri myUri = Uri.parse(audio_url);
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW);
                intent.setDataAndType(myUri, "audio/*");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            } else {
                Toast.makeText(context, "Some thing went wrong.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

    @Override
    public void onPageSelected(int position) {
        Log.d("Slider Demo", "Page Changed: " + position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public interface OnShareClick{
        public void onShareClick(CitizenJournalistVideos mNews);
    }

    public interface OnCommentClick{
        public void onCommentClick();
    }
}
