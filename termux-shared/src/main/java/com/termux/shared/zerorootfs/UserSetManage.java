package com.termux.shared.zerorootfs;

import com.google.gson.Gson;
import com.termux.shared.zerorootfs.bean.ZTUserBean;


public class UserSetManage {
    private static UserSetManage mUserSetManage;
    private UserSetManage(){}
    public static UserSetManage getInstall1() {
        if (mUserSetManage == null) {
            synchronized (UserSetManage.class) {
                if (mUserSetManage == null) {
                    mUserSetManage = new UserSetManage();
                    return mUserSetManage;
                } else {
                    return mUserSetManage;
                }
            }
        } else {
            return mUserSetManage;
        }
    }

    public ZTUserBean getZTUserBean() {
        String zTUserBeanJson = SaveData.getData(ZTUserBean.zrKey);
        if (zTUserBeanJson == null || "def".equals(zTUserBeanJson)) {
            return new ZTUserBean();
        }

        try {
            return new Gson().fromJson(zTUserBeanJson, ZTUserBean.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ZTUserBean();
    }

    public void setZTUserBean(ZTUserBean mZTUserBean) {
        String toJson = new Gson().toJson(mZTUserBean);
        SaveData.saveData(ZTUserBean.zrKey, toJson);
    }
}
