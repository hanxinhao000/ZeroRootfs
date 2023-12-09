package com.termux.shared.zerorootfs;

import android.content.Context;
import android.content.SharedPreferences;


public class SaveData {
    public static void saveData(String key, String values) {
        SharedPreferences xinhao = ZeroUtils.getContext().getSharedPreferences("xinhao", Context.MODE_PRIVATE);
        xinhao.edit().putString(key, values).apply();
    }
    public static String getData(String key) {
        SharedPreferences xinhao = ZeroUtils.getContext().getSharedPreferences("xinhao", Context.MODE_PRIVATE);
        String def = xinhao.getString(key, "def");
        return def;
    }
}
