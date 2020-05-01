package com.kuaishan.obtainmsg.ui.bean;

import java.io.Serializable;

/**
 * `created_at`   DATETIME             DEFAULT NULL,
 * `updated_at`
 */
public class AppShares implements Serializable {
    private int id;
    private String icon_url;

    public int getGroup_id() {
        return group_id;
    }

    public void setGroup_id(int group_id) {
        this.group_id = group_id;
    }

    private int group_id;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    private int status;

    public String getIcon_url() {
        return icon_url;
    }

    public void setIcon_url(String icon_url) {
        this.icon_url = icon_url;
    }

    public String getApp_name() {
        return app_name;
    }

    public void setApp_name(String app_name) {
        this.app_name = app_name;
    }

    private String app_name;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


}
