package com.lipl.ommcom.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by luminousinfoways on 09/10/15.
 */
public class News implements Parcelable {

    private String id;
    private String name;
    private String slug;
    private String youtubelink;
    private String image;
    private String short_description;
    private String long_description;
    private String file_path;
    private String approved_date;
    private String position;
    private String user_id;
    private String jounalist_name;
    private String section;
    private String url_link;
    private String category;
    private String cat_id;
    private String sub_cat_id;
    private String is_hot;
    private String tags;
    private String is_video;
    private String is_image;
    private String is_draft;
    private String is_publish;
    private String is_archive;
    private String publish_date;
    private String archive_date;
    private String last_save_time;
    private String is_approved;
    private String approved_by;
    private String is_enable;
    private String created_at;
    private String updated_at;
    private String allow_comment;
    private String is_featured;
    private String is_top_story;
    private String is_viral;
    private String meta_desc;
    private String meta_keywords;
    private int news_count;
    private String is_trash;
    private String categoryslug;
    private List<NewsImage> imgList;
    private List<NewsVideo> videoList;
    private List<Comment> commentList;
    private List<News> relatedNewsList;

    public String getCategoryslug() {
        return categoryslug;
    }

    public void setCategoryslug(String categoryslug) {
        this.categoryslug = categoryslug;
    }

    public String getIs_trash() {
        return is_trash;
    }

    public void setIs_trash(String is_trash) {
        this.is_trash = is_trash;
    }

    public List<NewsVideo> getVideoList() {
        return videoList;
    }

    public void setVideoList(List<NewsVideo> videoList) {
        this.videoList = videoList;
    }

    public List<News> getRelatedNewsList() {
        return relatedNewsList;
    }

    public void setRelatedNewsList(List<News> relatedNewsList) {
        this.relatedNewsList = relatedNewsList;
    }

    public List<Comment> getCommentList() {
        return commentList;
    }

    public void setCommentList(List<Comment> commentList) {
        this.commentList = commentList;
    }

    public List<NewsImage> getImgList() {
        return imgList;
    }

    public void setImgList(List<NewsImage> imgList) {
        this.imgList = imgList;
    }

    public String getCat_id() {
        return cat_id;
    }

    public void setCat_id(String cat_id) {
        this.cat_id = cat_id;
    }

    public String getSub_cat_id() {
        return sub_cat_id;
    }

    public void setSub_cat_id(String sub_cat_id) {
        this.sub_cat_id = sub_cat_id;
    }

    public String getIs_hot() {
        return is_hot;
    }

    public void setIs_hot(String is_hot) {
        this.is_hot = is_hot;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getIs_video() {
        return is_video;
    }

    public void setIs_video(String is_video) {
        this.is_video = is_video;
    }

    public String getIs_image() {
        return is_image;
    }

    public void setIs_image(String is_image) {
        this.is_image = is_image;
    }

    public String getIs_draft() {
        return is_draft;
    }

    public void setIs_draft(String is_draft) {
        this.is_draft = is_draft;
    }

    public String getIs_publish() {
        return is_publish;
    }

    public void setIs_publish(String is_publish) {
        this.is_publish = is_publish;
    }

    public String getIs_archive() {
        return is_archive;
    }

    public void setIs_archive(String is_archive) {
        this.is_archive = is_archive;
    }

    public String getPublish_date() {
        return publish_date;
    }

    public void setPublish_date(String publish_date) {
        this.publish_date = publish_date;
    }

    public String getArchive_date() {
        return archive_date;
    }

    public void setArchive_date(String archive_date) {
        this.archive_date = archive_date;
    }

    public String getLast_save_time() {
        return last_save_time;
    }

    public void setLast_save_time(String last_save_time) {
        this.last_save_time = last_save_time;
    }

    public String getIs_approved() {
        return is_approved;
    }

    public void setIs_approved(String is_approved) {
        this.is_approved = is_approved;
    }

    public String getApproved_by() {
        return approved_by;
    }

    public void setApproved_by(String approved_by) {
        this.approved_by = approved_by;
    }

    public String getIs_enable() {
        return is_enable;
    }

    public void setIs_enable(String is_enable) {
        this.is_enable = is_enable;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getAllow_comment() {
        return allow_comment;
    }

    public void setAllow_comment(String allow_comment) {
        this.allow_comment = allow_comment;
    }

    public String getIs_featured() {
        return is_featured;
    }

    public void setIs_featured(String is_featured) {
        this.is_featured = is_featured;
    }

    public String getIs_top_story() {
        return is_top_story;
    }

    public void setIs_top_story(String is_top_story) {
        this.is_top_story = is_top_story;
    }

    public String getIs_viral() {
        return is_viral;
    }

    public void setIs_viral(String is_viral) {
        this.is_viral = is_viral;
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

    public int getNews_count() {
        return news_count;
    }

    public void setNews_count(int news_count) {
        this.news_count = news_count;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getUrl_link() {
        return url_link;
    }

    public void setUrl_link(String url_link) {
        this.url_link = url_link;
    }

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

    public String getyoutubelink() {
        return youtubelink;
    }

    public void setyoutubelink(String youtubelink) {
        this.youtubelink = youtubelink;
    }


    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getShort_description() {
        return short_description;
    }

    public void setShort_description(String short_description) {
        this.short_description = short_description;
    }

    public String getFile_path() {
        return file_path;
    }

    public void setFile_path(String file_path) {
        this.file_path = file_path;
    }

    public String getApproved_date() {
        return approved_date;
    }

    public void setApproved_date(String approved_date) {
        this.approved_date = approved_date;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getJounalist_name() {
        return jounalist_name;
    }

    public void setJounalist_name(String jounalist_name) {
        this.jounalist_name = jounalist_name;
    }

    public String getLong_description() {
        return long_description;
    }

    public void setLong_description(String long_description) {
        this.long_description = long_description;
    }

    public static final Creator<News> CREATOR = new Creator<News>() {

        @Override
        public News createFromParcel(Parcel source) {
            return new News(source);
        }

        @Override
        public News[] newArray(int size) {
            News[] currentLocations = new News[size];
            return currentLocations;
        }
    };

    public News(Parcel in){
        readFromParcel(in);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(slug);
        dest.writeString(image);
        dest.writeString(short_description);
        dest.writeString(long_description);
        dest.writeString(file_path);
        dest.writeString(approved_date);
        dest.writeString(position);
        dest.writeString(user_id);
        dest.writeString(jounalist_name);
        dest.writeString(section);
        dest.writeString(url_link);
        dest.writeString(category);
        dest.writeString(cat_id);
        dest.writeString(sub_cat_id);
        dest.writeString(is_hot);
        dest.writeString(tags);
        dest.writeString(is_video);
        dest.writeString(is_image);
        dest.writeString(is_draft);
        dest.writeString(is_publish);
        dest.writeString(is_archive);
        dest.writeString(publish_date);
        dest.writeString(archive_date);
        dest.writeString(last_save_time);
        dest.writeString(is_approved);
        dest.writeString(approved_by);
        dest.writeString(is_enable);
        dest.writeString(created_at);
        dest.writeString(updated_at);
        dest.writeString(allow_comment);
        dest.writeString(is_featured);
        dest.writeString(is_top_story);
        dest.writeString(is_viral);
        dest.writeString(meta_desc);
        dest.writeString(meta_keywords);
        dest.writeString(is_trash);
        dest.writeInt(news_count);
        dest.writeTypedList(imgList);
        dest.writeTypedList(commentList);
        dest.writeTypedList(relatedNewsList);
        dest.writeTypedList(videoList);
        dest.writeString(categoryslug);
    }

    private void readFromParcel(Parcel in){

        id = in.readString();
        name = in.readString();
        slug = in.readString();
        image = in.readString();
        short_description = in.readString();
        long_description = in.readString();
        file_path = in.readString();
        approved_date = in.readString();
        position = in.readString();
        user_id = in.readString();
        jounalist_name = in.readString();
        section = in.readString();
        url_link = in.readString();
        category = in.readString();
        cat_id = in.readString();
        sub_cat_id = in.readString();
        is_hot = in.readString();
        tags = in.readString();
        is_video = in.readString();
        is_image = in.readString();
        is_draft = in.readString();
        is_publish = in.readString();
        is_archive = in.readString();
        publish_date = in.readString();
        archive_date = in.readString();
        last_save_time = in.readString();
        is_approved = in.readString();
        approved_by = in.readString();
        is_enable = in.readString();
        created_at = in.readString();
        updated_at = in.readString();
        allow_comment = in.readString();
        is_featured = in.readString();
        is_top_story = in.readString();
        is_viral = in.readString();
        meta_desc = in.readString();
        meta_keywords = in.readString();
        is_trash = in.readString();
        news_count = in.readInt();
        imgList = new ArrayList<NewsImage>();
        in.readTypedList(imgList, NewsImage.CREATOR);
        commentList = new ArrayList<Comment>();
        in.readTypedList(commentList, Comment.CREATOR);
        relatedNewsList = new ArrayList<News>();
        in.readTypedList(relatedNewsList, News.CREATOR);
        videoList = new ArrayList<NewsVideo>();
        in.readTypedList(videoList, NewsVideo.CREATOR);
        categoryslug = in.readString();
    }
}
