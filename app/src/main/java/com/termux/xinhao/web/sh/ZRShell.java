package com.termux.xinhao.web.sh;

import com.termux.xinhao.web.utils.UUtils;
import com.xinhao.web.services.R;

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
    public static void initStartRootfs() {
        packageName = UUtils.getContext().getPackageName();
        startRootfs = new String[] {
            "#!/system/bin/sh",
            "export PROOT_TMP_DIR=\"tmp\"",
            "cd /data/data/" + packageName +"/" + container + "/home/",
            "    command=\"../bin/proot\"",
            "    command+=\" --link2symlink\"",
            "    command+=\" -0\"",
            "    command+=\" -r rootfs-fs\"",
            "    if [ -n \"$(ls -A binds)\" ]; then",
            "        for f in binds/* ;do",
            "          . $f",
            "        done",
            "    fi",
            "    command+=\" -b /dev\"",
            "    command+=\" -b /proc\"",
            "    command+=\" -b rootfs-fs/root:/dev/shm\"",
            "    ## uncomment the following line to have access to the home directory of termux",
            "    #command+=\" -b /data/data/com.termux/files/home:/root\"",
            "    ## uncomment the following line to mount /sdcard directly to /",
            "    command+=\" -b /sdcard\"",
            "    command+=\" -w /root\"",
            "    command+=\" /usr/bin/env -i\"",
            "    command+=\" HOME=/root\"",
            "    command+=\" PATH=/usr/local/sbin:/usr/local/bin:/bin:/usr/bin:/sbin:/usr/sbin:/usr/games:/usr/local/games\"",
            "    command+=\" TERM=$TERM\"",
            "    command+=\" LANG=C.UTF-8\"",
            "    command+=\" /bin/bash --login\"",
            "    com=\"$@\"",
            "    exec $command",
        };
    }
}
