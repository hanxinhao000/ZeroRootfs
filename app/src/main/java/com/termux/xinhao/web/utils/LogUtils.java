package com.termux.xinhao.web.utils;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.IOException;

public class LogUtils {
    public static String TAG = "ZeroTermux";

    public static boolean isShow = false;
    public static void e(String tag, String msg) {
        Log.e(TAG + "--" + tag, msg);
    }
    public static void i(String tag, String msg) {
        Log.i(TAG + "--" + tag, msg);
    }
    public static void d(String tag, String msg) {
        Log.d(TAG + "--" + tag, msg);
    }
    public static void crawl(String tag, String msg, Exception e) {
        Log.d(TAG + "--" + tag, msg, e);
    }

}
