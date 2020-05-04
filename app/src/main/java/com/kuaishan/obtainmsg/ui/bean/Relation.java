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


    public String getMain_account() {
        return main_account;
    }

    public void setMain_account(String main_account) {
        this.main_account = main_account;
    }

    public String getSub_account() {
        return sub_account;
    }

    public void setSub_account(String sub_account) {
        this.sub_account = sub_account;
    }

    private String main_account;
    private String sub_account;

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
