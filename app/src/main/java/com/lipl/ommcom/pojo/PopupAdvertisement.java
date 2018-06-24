package com.lipl.ommcom.pojo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by luminousinfoways on 09/10/15.
 */
public class PopupAdvertisement implements Parcelable {

    private String id;
    private String name;
    private String file_path;
    private String file_type;
    private String url_link;
    private String sponsor_id;
    private String user_id;
    private String cat_id;
    private String created_at;
    private String updated_at;
    private String is_publish;
    private String publish_date;
    private String start_date;
    private String end_date;
    private String advertisement_type_id;
    private String advertisement_section_id;
    private String is_enable;
    private String is_url;
    private String priority;
    private String is_trash;

    public String getSponsor_id() {
        return sponsor_id;
    }

    public void setSponsor_id(String sponsor_id) {
        this.sponsor_id = sponsor_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getCat_id() {
        return cat_id;
    }

    public void setCat_id(String cat_id) {
        this.cat_id = cat_id;
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

    public String getIs_publish() {
        return is_publish;
    }

    public void setIs_publish(String is_publish) {
        this.is_publish = is_publish;
    }

    public String getPublish_date() {
        return publish_date;
    }

    public void setPublish_date(String publish_date) {
        this.publish_date = publish_date;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getAdvertisement_type_id() {
        return advertisement_type_id;
    }

    public void setAdvertisement_type_id(String advertisement_type_id) {
        this.advertisement_type_id = advertisement_type_id;
    }

    public String getAdvertisement_section_id() {
        return advertisement_section_id;
    }

    public void setAdvertisement_section_id(String advertisement_section_id) {
        this.advertisement_section_id = advertisement_section_id;
    }

    public String getIs_enable() {
        return is_enable;
    }

    public void setIs_enable(String is_enable) {
        this.is_enable = is_enable;
    }

    public String getIs_url() {
        return is_url;
    }

    public void setIs_url(String is_url) {
        this.is_url = is_url;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getIs_trash() {
        return is_trash;
    }

    public void setIs_trash(String is_trash) {
        this.is_trash = is_trash;
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

    public String getFile_path() {
        return file_path;
    }

    public void setFile_path(String file_path) {
        this.file_path = file_path;
    }

    public String getFile_type() {
        return file_type;
    }

    public void setFile_type(String file_type) {
        this.file_type = file_type;
    }

    public static final Creator<PopupAdvertisement> CREATOR = new Creator<PopupAdvertisement>() {

        @Override
        public PopupAdvertisement createFromParcel(Parcel source) {
            return new PopupAdvertisement(source);
        }

        @Override
        public PopupAdvertisement[] newArray(int size) {
            PopupAdvertisement[] currentLocations = new PopupAdvertisement[size];
            return currentLocations;
        }
    };

    public PopupAdvertisement(Parcel in){
        readFromParcel(in);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(name);
        dest.writeString(file_path);
        dest.writeString(id);
        dest.writeString(file_type);
        dest.writeString(url_link);
        dest.writeString(sponsor_id);
        dest.writeString(user_id);
        dest.writeString(cat_id);
        dest.writeString(created_at);
        dest.writeString(updated_at);
        dest.writeString(is_publish);
        dest.writeString(publish_date);
        dest.writeString(start_date);
        dest.writeString(end_date);
        dest.writeString(advertisement_type_id);
        dest.writeString(advertisement_section_id);
        dest.writeString(is_enable);
        dest.writeString(is_url);
        dest.writeString(priority);
        dest.writeString(is_trash);
    }

    private void readFromParcel(Parcel in){

        name = in.readString();
        file_path = in.readString();
        id = in.readString();
        file_type = in.readString();
        url_link = in.readString();
        sponsor_id = in.readString();
        user_id = in.readString();
        cat_id = in.readString();
        created_at = in.readString();
        updated_at = in.readString();
        is_publish = in.readString();
        publish_date = in.readString();
        start_date = in.readString();
        end_date = in.readString();
        advertisement_type_id = in.readString();
        advertisement_section_id = in.readString();
        is_enable = in.readString();
        is_url = in.readString();
        priority = in.readString();
        is_trash = in.readString();
    }
}