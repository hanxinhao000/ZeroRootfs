package com.termux.xinhao.web.application;

import android.app.Application;
import android.content.Context;
import android.os.Handler;

import com.termux.app.TermuxApplication;
import com.termux.shared.termux.TermuxConstants;
import com.termux.shared.zerorootfs.ZeroUtils;
import com.termux.xinhao.web.ZRFileUrl;
import com.termux.xinhao.web.libsu.LibSuManage;
import com.termux.xinhao.web.utils.UUtils;

public class XHApplication extends TermuxApplication {
    private static Context mContext;
    private static Handler mHandler;
    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        mHandler = new Handler();
        UUtils.initUUtils(mContext, mHandler);
       // TermuxConstants.TERMUX_PACKAGE_NAME = getPackageName();
        ZRFileUrl.initData();
        LibSuManage.getInstall().initTimer();
        ZeroUtils.setContext(this);
    }
}
