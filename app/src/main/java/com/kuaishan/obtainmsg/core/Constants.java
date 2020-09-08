package com.kuaishan.obtainmsg.core;

import com.kuaishan.obtainmsg.BuildConfig;

public class Constants {

    public static class COMMON {
        public static final String SHARE_NAME = "kuaishan";
        public static final long TENDAYS = 10 * 24 * 3600 * 1000;
        public static String TIME_TOKEN = "time_token";
        public static String ALIAS = "alias";
        public static String FIRST_OPEN = "firstOpen";
    }
    public static class Url {
        //-----------------------------------I
        // ---------------------------------------------------websocket
        public static final String mHost = BuildConfig.FlagServer;
        //--------------------------------------------------------------------------------------获取试验最新更新接口
        public static final String REGISTER = BuildConfig.FlagServer + "api/v1/regist";
        public static final String D_SUB_ACCOUNT = BuildConfig.FlagServer + "api/v1/dSubAccount";
        public static final String G_SINGLE_MSG = BuildConfig.FlagServer + "api/v1" +
                "/getSingleMessage";
        public static final String LOGIN = BuildConfig.FlagServer + "api/v1/login";
        public static final String testjavasdk = BuildConfig.FlagServer + "api/v1/getlist";
        public static final String FORGOT_PASS = BuildConfig.FlagServer + "api/v1/forgotPass";
        public static final String ADDRELATION = BuildConfig.FlagServer + "api/v1/addRelation";
        public static final String GETRELATION = BuildConfig.FlagServer + "api/v1/relations";
        public static final String SAVESMS = BuildConfig.FlagServer + "api/v1/submitticket";
        public static final String MESSAGES = BuildConfig.FlagServer + "api/v1/messages";
        public static final String GROUPS = BuildConfig.FlagServer + "api/v1/groups";
        public static final String APPS = BuildConfig.FlagServer + "api/v1/apps";
        public static final String ADDGROUP = BuildConfig.FlagServer + "api/v1/addGroup";
        public static final String FINDSUBACCOUNT = BuildConfig.FlagServer + "api/v1/findSubAccount";
        //--------------------------------------------------------------------------------------变量服务器地址
        public static final String ADHOC_SERVER_GETFLAGS = BuildConfig.FlagServer +
                "get_flags_async";
        //--------------------------------------------------------------------------------------获取SDK配置
        public static final String GET_CONFIG_DATA = BuildConfig.QiNiuServer + "apps/{app_id" +
                "}/sdk_config";
        //--------------------------------------------------------------------------------------tracker
        public static final String TRACKER_SERVER_URL = BuildConfig.TrackServer + "tracker";
        //--------------------------------------------------------------------------------------强制加入试验
        public static final String FORCE_URL = BuildConfig.FlagServer + "force_clients";
        public static final String GET_NEW_VERSION = "https://api.github" +
                ".com/repos/AppAdhoc/AdhocSDK-Android/releases/latest";
        //--------------------------------------------------------------------------------------强制退出试验
        public final static String FORCE_DELETE = BuildConfig.FlagServer +
                "delete_all_force_clients";
        public final static String UPLOAD_FILE = "http://h5.dev.appadhoc.com/codeScreenShot";
    }
}
