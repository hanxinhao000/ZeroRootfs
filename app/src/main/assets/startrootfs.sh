#!/system/bin/sh
rootDir="com.xinhao.web.services"
cd /data/data/$rootDir/files/home
export PROOT_TMP_DIR="tmp"
    command="../bin/proot"
    command+=" --link2symlink"
    command+=" -0"
    command+=" -r ubuntu-fs"
    if [ -n "$(ls -A binds)" ]; then
        for f in binds/* ;do
          . $f
        done
    fi
    command+=" -b /dev"
    command+=" -b /proc"
    command+=" -b ubuntu-fs/root:/dev/shm"
    ## uncomment the following line to have access to the home directory of termux
    #command+=" -b /data/data/com.termux/files/home:/root"
    ## uncomment the following line to mount /sdcard directly to /
    command+=" -b /sdcard"
    command+=" -w /root"
    command+=" /usr/bin/env -i"
    command+=" HOME=/root"
    command+=" PATH=/usr/local/sbin:/usr/local/bin:/bin:/usr/bin:/sbin:/usr/sbin:/usr/games:/usr/local/games"
    command+=" TERM=$TERM"
    command+=" LANG=C.UTF-8"
    command+=" /bin/bash --login"
    com="$@"

    exec $command
