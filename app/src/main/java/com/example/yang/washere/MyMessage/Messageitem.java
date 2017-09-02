package com.example.yang.washere.MyMessage;

/**
 * Created by Yang on 2017/4/28.
 */

public class Messageitem {
    private String post_id;
    private String p_title;
    private String p_type;

    public String getPost_id() {
        return post_id;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }

    public String getP_title() {
        return p_title;
    }

    public void setP_title(String p_title) {
        this.p_title = p_title;
    }

    public String getP_type() {
        return p_type;
    }

    public void setP_type(String p_type) {
        this.p_type = p_type;
    }

    public String getU_id() {
        return u_id;
    }

    public void setU_id(String u_id) {
        this.u_id = u_id;
    }

    public String getHead_logo() {
        return head_logo;
    }

    public void setHead_logo(String head_logo) {
        this.head_logo = head_logo;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getPublish_time() {
        return publish_time;
    }

    public void setPublish_time(String publish_time) {
        this.publish_time = publish_time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMulti_media() {
        return multi_media;
    }

    public void setMulti_media(String multi_media) {
        this.multi_media = multi_media;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getPage_view() {
        return page_view;
    }

    public void setPage_view(int page_view) {
        this.page_view = page_view;
    }

    private String u_id;
    private String head_logo;
    private String user_name;
    private String publish_time;
    private String content;
    private String multi_media;
    private double longitude;
    private double latitude;
    private int level;
    private int page_view;


    public Messageitem() {

    }


}
