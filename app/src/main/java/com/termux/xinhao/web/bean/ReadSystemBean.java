package com.termux.xinhao.web.bean;

public class ReadSystemBean {

    public String dir;

    public String name;

    public boolean isCkeck = false;

    @Override
    public String toString() {
        return "ReadSystemBean{" +
            "dir='" + dir + '\'' +
            ", name='" + name + '\'' +
            '}';
    }
}
