package com.smalljop.mytools.utils;


import com.smalljop.mytools.MyApplication;

import lombok.experimental.UtilityClass;

@UtilityClass
public class EnvironmentUtils {


    private final String baseUrl = "baseUrl";


    public final static String DEV_BASE_URL = "https://smalljop.utools.club";

    public final static String PROD_BASE_URL = "http://47.106.35.76:8091/";

    /**
     * 获取基本环境
     *
     * @return
     */
    public String getBaseUrl() {
        Object param = SharedPreferencesUtils.getParam(MyApplication.getContext(), baseUrl, PROD_BASE_URL);
        return param.toString();
    }


    /**
     * 获取基本环境
     *
     * @return
     */
    public void setBaseUrl(String url) {
        SharedPreferencesUtils.setParam(MyApplication.getContext(), baseUrl, url);
    }
}
