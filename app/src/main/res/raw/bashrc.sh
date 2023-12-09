#!/system/bin/sh
#################ZeroWeb##############
#       ZeroWeb 安装工作脚本           #
######################################
###这边写入你的包名
rootDir="com.xinhao.web.services"

unzip_tool() {
  cd /data/data/$rootDir/files
  chmod 777 installtool.sh
  ./installtool.sh
}

unzip_rootfs() {
  cd /data/data/$rootDir/files/home
  chmod 777 installrootfs.sh
  ./installrootfs.sh
}

unzip_start_rootfs() {
     cd /data/data/$rootDir/files/home
     chmod 777 startrootfs.sh
}

unzip_delete_rm_container() {
    cd /data/data/$rootDir
    chmod 777 busybox
    chmod 777 delete.sh
    ./delete.sh
}
