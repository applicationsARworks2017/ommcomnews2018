package com.lipl.ommcom.adapter;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.lipl.ommcom.R;
import com.lipl.ommcom.activity.NewsDetailsActivity;
import com.lipl.ommcom.pojo.FImageListItem;
import com.lipl.ommcom.pojo.News;
import com.lipl.ommcom.pojo.NewsDetailsForFlipModel;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class SlidingImage_Adapter extends PagerAdapter {
    private ArrayList<String> imageModelArrayList;
    private LayoutInflater inflater;
    private Context context;

    private int repeatCount = 1;

    private List<NewsDetailsForFlipModel> forFlipModelList;
    private String title;
    private DisplayImageOptions options;
    private ImageLoader imageLoader;
    private String posted_time;
    private String posted_by;
    private List<FImageListItem> imageListItems;
    private FlipAdapter.OnShareClick onShareClick;
    private FlipAdapter.OnCommentClick onCommentClick;
    private News mNews;
    private int facebookCount;
    private int twitterCount;
    private int gplusCount;

    public SlidingImage_Adapter(Context applicationContext,
                                NewsDetailsActivity newsDetailsActivity, String title,
                                String posted_at, String posted_by, List<FImageListItem> imageListItems,
                                NewsDetailsActivity newsDetailsActivity1,
                                NewsDetailsActivity newsDetailsActivity2,
                                News news, int mFBCount, int mTwitterCount, int mGPlusCount) {
        this.title = title;
        this.posted_time = posted_time;
        this.context = context;
        this.posted_by = posted_by;
        this.imageListItems = imageListItems;
        inflater = LayoutInflater.from(context);
        this.mNews = news;
        this.facebookCount = facebookCount;
        this.twitterCount = twitterCount;
        this.gplusCount = gplusCount;

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return imageModelArrayList.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        View imageLayout = inflater.inflate(R.layout.slidingimages_layout, view, false);

        assert imageLayout != null;
        final ImageView imageView = (ImageView) imageLayout
                .findViewById(R.id.image);

     //   Picasso.with(context).load(imageModelArrayList.get(position).getPhoto()).into(imageView);

        view.addView(imageLayout, 0);

        return imageLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }

}
