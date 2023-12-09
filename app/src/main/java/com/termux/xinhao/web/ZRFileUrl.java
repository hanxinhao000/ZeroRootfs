package com.termux.xinhao.web;

import com.termux.shared.termux.TermuxConstants;
import com.termux.xinhao.web.utils.UUtils;

public class ZRFileUrl {
    public static String env ;
    public static String installRootfs;
    public static String installTool;
    public static String startRootfs;
    public static String homeSdcard;
    public static String deleteSh;

    public static void initData() {

        env = TermuxConstants.TERMUX_HOME_DIR_PATH + "/rootfs-fs/usr/bin/env";
        installRootfs = TermuxConstants.TERMUX_HOME_DIR_PATH + "/installrootfs.sh";
        startRootfs = TermuxConstants.TERMUX_HOME_DIR_PATH + "/startrootfs.sh";
        homeSdcard = TermuxConstants.TERMUX_HOME_DIR_PATH + "/sdcard";
        installTool = TermuxConstants.TERMUX_FILES_DIR_PATH + "/installtool.sh";
        deleteSh = TermuxConstants.TERMUX_INTERNAL_PRIVATE_APP_DATA_DIR_PATH + "/delete.sh";
    }
}
