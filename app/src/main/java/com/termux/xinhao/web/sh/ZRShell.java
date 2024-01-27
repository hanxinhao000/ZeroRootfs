package com.termux.xinhao.web.sh;

import com.termux.xinhao.web.utils.UUtils;
import com.xinhao.web.services.R;

import java.util.ArrayList;

public class ZRShell {
    public static String packageName;
    public static String container = "files";
    //#################安装解压工具
    public static String[] installTool;
    public static String[] deleteSh;
    //#################安装rootfs脚本
    public static String[] installRootfs;
    //#################rootfs启动脚本
    public static String[] startRootfs;
    public static void initData(String fileName) {
        packageName = UUtils.getContext().getPackageName();
        installRootfs = new String[]{
            "#!/system/bin/sh",
            "#################ZeroWeb##############",
            "#   ZeroWeb 安装ROOTFS工作脚本         #",
            "######################################",
            "cd /data/data/" + packageName +"/" + container +"/home",
            "osname='Rootfs'",
            "folder='rootfs-fs'",
            "imagedir='termux-ubuntu'",
            "tarball='" + fileName + "'",
            "mkdir \"$folder\"",
            "../busybox tar -xvf ${tarball} -C ${folder} --exclude=\"dev\"||:",
            "mkdir binds",
            "echo \"" + UUtils.getString(R.string.install_ok) +"\"",
        };
    }

    public static void initInstallTool() {
        packageName = UUtils.getContext().getPackageName();
        installTool = new String[] {
            "#!/system/bin/sh",
            "#################ZeroWeb##############",
            "#   ZeroWeb 安装解压工具工作脚本         #",
            "######################################",
            "cd /data/data/" + packageName +"/" + container + "/",
            "chmod 777 busybox",
            "./busybox unzip assets.zip",
            "chmod 700 -R bin",
            "chmod 700 -R libexec",
            "chmod 700 -R home",
            "mkdir tmp",
            "mkdir home/tmp",
        };
    }

    public static void deleteContainer(String cmd) {
        packageName = UUtils.getContext().getPackageName();
        deleteSh = new String[] {
            "#!/system/bin/sh",
            "cd /data/data/" + packageName,
            cmd,
        };
    }
    public static void initStartRootfs(String[] command) {
        packageName = UUtils.getContext().getPackageName();
        String[] head = new String[] {
            "#!/system/bin/sh",
            "cd /data/data/" + packageName +"/" + container + "/home/",
        };
        ArrayList<String> arrayList = new ArrayList<>();
        for (int i = 0; i < head.length; i++) {
            arrayList.add(head[i]);
        }
        for (int i = 0; i < command.length; i++) {
            arrayList.add(command[i]);
        }
        startRootfs = arrayList.toArray(new String[]{});
    }
}
