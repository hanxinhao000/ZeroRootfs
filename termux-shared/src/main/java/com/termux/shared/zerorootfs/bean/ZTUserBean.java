package com.termux.shared.zerorootfs.bean;

public class ZTUserBean {
    public static final String zrKey = "zr_settings";
    private boolean isBug = false;
    private boolean isStartTermux = false;

    public boolean isBug() {
        return isBug;
    }

    public void setBug(boolean bug) {
        isBug = bug;
    }

    public boolean isStartTermux() {
        return isStartTermux;
    }

    public void setStartTermux(boolean startTermux) {
        isStartTermux = startTermux;
    }
}
