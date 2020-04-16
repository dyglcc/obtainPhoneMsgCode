package com.kuaishan.obtainmsg.core;

import com.kuaishan.obtainmsg.BuildConfig;

public class Constants {
    public static class Url {
        //-----------------------------------I
        // ---------------------------------------------------websocket
        public static final String mServer = BuildConfig.WebsocketServer;
        //--------------------------------------------------------------------------------------获取试验最新更新接口
        public static final String REGISTER = BuildConfig.FlagServer + "api/v1/regist";
        public static final String LOGIN = BuildConfig.FlagServer + "api/v1/login";
        public static final String ADDRELATION = BuildConfig.FlagServer + "api/v1/addrelation";
        public static final String GETRELATION = BuildConfig.FlagServer + "api/v1/relations";
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
