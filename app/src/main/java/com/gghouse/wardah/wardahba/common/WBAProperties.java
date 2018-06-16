package com.gghouse.wardah.wardahba.common;

import com.gghouse.wardah.wardahba.enumeration.ModeEnum;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by michaelhalim on 1/24/17.
 */

public interface WBAProperties {
    /*
     * Development Mode
     */
//    ModeEnum mode = ModeEnum.DEVELOPMENT;
    ModeEnum mode = ModeEnum.DUMMY_DEVELOPMENT;
//    ModeEnum mode = ModeEnum.PRODUCTION;

    /*
     * REST API URL
     */
//    String BASE_URL = "http://192.168.8.104:8081";
//    String BASE_URL = "http://192.168.0.205:8081";
//    String BASE_URL = "http://10.0.2.2:8081";
//    String BASE_URL = "http://192.168.0.254:8081";
    String BASE_URL = "http://inspiring.wardahbeauty.com:8081";
    String PROD_URL = "http://inspiring.wardahbeauty.com:8081";

    /*
     *
     */
    int CONNECT_TIMEOUT = 30;
    int READ_TIMEOUT = 30;
    int CODE_99 = 99;
    int CODE_200 = 200;
    int CODE_211 = 211;
    int CODE_401 = 401;
    String notificationSort = "periodEnd,DESC";

    /*
     * Log
     */
    boolean LOG_ENABLE = true;
    String LOG_BEGIN = "- BEGIN : ";
    String LOG_END = "- END : ";
    String ON_RESPONSE = "ON RESPONSE";
    String ON_FAILURE = "ON FAILURE";
    String ON_ERROR = "ON ERROR";
    String STATUS = "STATUS";

    // Michael Halim : Properties
    int SPLASH_SCREEN_DELAY = 3000;
    int NOTIF_ITEM_PER_PAGE = 3;
    int SALES_ITEM_PER_PAGE = 5;
    int PELANGGAN_ITEM_PER_PAGE = 10;
    int TEST_HISTORY_ITEM_PER_PAGE = 10;

    // Michael Halim : Validation
    boolean BYPASS_LOGIN_VALIDATION = false;

    // Michael Halim : Simple Date Format
    Locale locale = new Locale("in", "ID");
    SimpleDateFormat sdfDateOnly = new SimpleDateFormat("dd", locale);
    SimpleDateFormat sdfMonthYear = new SimpleDateFormat("MMMM, yyyy", locale);
    SimpleDateFormat sdfNotif = new SimpleDateFormat("dd MMMM yyyy", locale);
    SimpleDateFormat sdfDay = new SimpleDateFormat("EEEE", locale);
    SimpleDateFormat sdfFilter = new SimpleDateFormat("dd/MM/yyyy", locale);
    SimpleDateFormat sdfDate = new SimpleDateFormat("EEEE, dd MMMM yyyy", locale);

    // Michael Halim : Test Tab
    int MAX_SCORE = 100;

    int DELAY_LOAD_MORE = 3000; //ms
}
