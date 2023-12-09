package com.termux.shared.zerorootfs;

import android.content.Context;

public class ZeroUtils {
    private static Context mContext = null;
    public static Context getContext() {
        return mContext;
    }
    public static void setContext(Context context) {
        mContext = context;
    }
}
