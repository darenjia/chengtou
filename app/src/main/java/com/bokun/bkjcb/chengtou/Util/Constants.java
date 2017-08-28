package com.bokun.bkjcb.chengtou.Util;

/**
 * Created by DengShuai on 2017/7/19.
 */

public class Constants {


    public static final String HTTPURL = "http://IP_ADDRESS:8083/WebService1.asmx";
//    public static final String GET_TABLT_DATA_URL = "http://192.168.100.123:8081/JBPM/phone/huibiantongji?biaoming=";
    //    public static final String GET_DETAIL_URL = "http://192.168.100.123:8081/JBPM/phone/xinxichaxun?year=2017&id=";

    //    public static final String HTTPURL = "http://192.168.34.240:8083/WebService1.asmx";
    public static final String GET_DETAIL_URL = "http://IP_ADDRESS:8081/JBPM/phone/xinxichaxun?year=2017&id=";
    public static final String GET_TABLT_DATA_URL = "http://IP_ADDRESS:8081/JBPM/phone/huibiantongji?biaoming=";
    public static  String IP_1 = "172.27.35.1";
    public static final String IP_3 = "192.168.100.123";
    public static String HTTP_URL = HTTPURL.replace("IP_ADDRESS", IP_1);
    public static String HTTP_DERAIL_URL = GET_DETAIL_URL.replace("IP_ADDRESS", IP_1);
    public static String HTTP_TABLE_URL = GET_TABLT_DATA_URL.replace("IP_ADDRESS", IP_1);
}
