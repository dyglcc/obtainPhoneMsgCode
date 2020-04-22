package com.kuaishan.obtainmsg.ui.bean;

/**
 * /**
 * `id`           INT         NOT NULL AUTO_INCREMENT,
 * `user_phone`      VARCHAR(32)               DEFAULT NULL,
 * `relate_phone`      VARCHAR(32)               DEFAULT NULL,
 * `created_at`   DATETIME             DEFAULT NULL,
 * `updated_at`   DATETIME
 */
public class Relation {
    private int id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String name;

    public String getUser_phone() {
        return user_phone;
    }

    public void setUser_phone(String user_phone) {
        this.user_phone = user_phone;
    }

    public String getRelate_phone() {
        return relate_phone;
    }

    public void setRelate_phone(String relate_phone) {
        this.relate_phone = relate_phone;
    }

    private String user_phone;
    private String relate_phone;

    public long getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(long updated_at) {
        this.updated_at = updated_at;
    }

    public long getCreated_at() {
        return created_at;
    }

    public void setCreated_at(long created_at) {
        this.created_at = created_at;
    }

    private long updated_at;
    private long created_at;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
