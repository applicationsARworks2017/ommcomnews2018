package com.lipl.ommcom.pojo;

/**
 * Created by Amaresh on 7/3/18.
 */

public class OdishaNews {
    String id,name,odia_name,slug,meta_title,featured_image,journalist_name,youtube_link,
            short_description,long_description,tags,publish_date,meta_desc,meta_keywords,news_count,is_video,approved_date;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOdia_name() {
        return odia_name;
    }

    public void setOdia_name(String odia_name) {
        this.odia_name = odia_name;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getMeta_title() {
        return meta_title;
    }

    public void setMeta_title(String meta_title) {
        this.meta_title = meta_title;
    }

    public String getFeatured_image() {
        return featured_image;
    }

    public void setFeatured_image(String featured_image) {
        this.featured_image = featured_image;
    }

    public String getJournalist_name() {
        return journalist_name;
    }

    public void setJournalist_name(String journalist_name) {
        this.journalist_name = journalist_name;
    }

    public String getYoutube_link() {
        return youtube_link;
    }

    public void setYoutube_link(String youtube_link) {
        this.youtube_link = youtube_link;
    }

    public String getShort_description() {
        return short_description;
    }

    public void setShort_description(String short_description) {
        this.short_description = short_description;
    }

    public String getLong_description() {
        return long_description;
    }

    public void setLong_description(String long_description) {
        this.long_description = long_description;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getPublish_date() {
        return publish_date;
    }

    public void setPublish_date(String publish_date) {
        this.publish_date = publish_date;
    }

    public String getMeta_desc() {
        return meta_desc;
    }

    public void setMeta_desc(String meta_desc) {
        this.meta_desc = meta_desc;
    }

    public String getMeta_keywords() {
        return meta_keywords;
    }

    public void setMeta_keywords(String meta_keywords) {
        this.meta_keywords = meta_keywords;
    }

    public String getNews_count() {
        return news_count;
    }

    public void setNews_count(String news_count) {
        this.news_count = news_count;
    }

    public String getIs_video() {
        return is_video;
    }

    public void setIs_video(String is_video) {
        this.is_video = is_video;
    }

    public String getApproved_date() {
        return approved_date;
    }

    public void setApproved_date(String approved_date) {
        this.approved_date = approved_date;
    }

    public OdishaNews(String id, String name, String odia_name, String slug, String meta_title,
                      String featured_image, String journalist_name, String youtube_link, String short_description,
                      String long_description, String tags, String publish_date, String meta_desc,
                      String meta_keywords, String news_count, String is_video, String approved_date) {

        this.id=id;
        this.name=name;
        this.odia_name=odia_name;
        this.slug=slug;
        this.meta_title=meta_title;
        this.featured_image=featured_image;
        this.journalist_name=journalist_name;
        this.youtube_link=youtube_link;
        this.short_description=short_description;
        this.long_description=long_description;
        this.tags=tags;
        this.publish_date=publish_date;
        this.meta_desc=meta_desc;
        this.meta_keywords=meta_keywords;
        this.news_count=news_count;
        this.is_video=is_video;
        this.approved_date=approved_date;




    }
}
