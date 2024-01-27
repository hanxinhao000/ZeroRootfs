package com.termux.xinhao.web.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.PrecomputedTextCompat;
import androidx.core.widget.TextViewCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.blankj.molihuan.utilcode.util.ToastUtils;
import com.molihuan.pathselector.PathSelector;
import com.molihuan.pathselector.entity.FileBean;
import com.molihuan.pathselector.entity.FontBean;
import com.molihuan.pathselector.fragment.BasePathSelectFragment;
import com.molihuan.pathselector.fragment.impl.PathSelectFragment;
import com.molihuan.pathselector.listener.CommonItemListener;
import com.molihuan.pathselector.listener.FileItemListener;
import com.molihuan.pathselector.utils.MConstants;
import com.molihuan.pathselector.utils.Mtools;
import com.termux.app.TermuxActivity;
import com.termux.shared.zerorootfs.UserSetManage;
import com.termux.shared.zerorootfs.bean.ZTUserBean;
import com.termux.x11.MainActivity;
import com.termux.xinhao.web.ZRFileUrl;
import com.termux.xinhao.web.libsu.LibSuManage;
import com.termux.xinhao.web.sh.ZRShell;
import com.termux.xinhao.web.utils.UUtils;
import com.xinhao.web.services.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class XHWebActivity extends AppCompatActivity implements LibSuManage.TimerListener {

    private LinearLayout install;
    private LinearLayout open_termux;
    private LinearLayout settings;
    private LinearLayout install_rootfs_other;
    private LinearLayout switch_container;
    private ScrollView scroll_view;
    private TextView console;
    private ArrayList<String> arrays = new ArrayList<>();
    private String[] fileType = new String[]{"tar.gz", "tar.xz", "zr"};

    private boolean isRunThread = false;

    @SuppressLint("HandlerLeak")
    private Handler mHanlder = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            try {
                console.setText(arrays.toString());
            } catch (Exception e) {
               e.printStackTrace();
            }
            scroll_view.postDelayed(() -> scroll_view.fullScroll(ScrollView.FOCUS_DOWN), 10);
        }
    };
    private String msg = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xhweb);
        install = findViewById(R.id.install);
        open_termux = findViewById(R.id.open_termux);
        settings = findViewById(R.id.settings);
        scroll_view = findViewById(R.id.scroll_view);
        console = findViewById(R.id.console);
        switch_container = findViewById(R.id.switch_container);
        install_rootfs_other = findViewById(R.id.install_rootfs_other);
        switch_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UUtils.showMsg(UUtils.getString(R.string.debug));
                startActivity(new Intent(XHWebActivity.this, MainActivity.class));
            }
        });
        try {
            PackageInfo packageInfo = getApplicationContext()
                .getPackageManager().getPackageInfo(getApplicationContext().getPackageName(), 0);
            console.setText(UUtils.getString(R.string.waiting_installation) + packageInfo.versionName);
        } catch (PackageManager.NameNotFoundException e) {
           e.printStackTrace();
        }
        LibSuManage.getInstall().setTimerListener(this);
        open_termux.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!new File(ZRFileUrl.env).exists()) {
                    UUtils.showMsg(UUtils.getString(R.string.not_installation));
                    return;
                }
                if (isRunThread) {
                    UUtils.showMsg(UUtils.getString(R.string.not_start_termux));
                    return;
                }
                startActivity(new Intent(XHWebActivity.this, TermuxActivity.class));
            }
        });
        install.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isRunThread) {
                    UUtils.showMsg(UUtils.getString(R.string.not_install));
                  return;
                }
                isRunThread = true;
                runThread();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        installRootfs();
                    }
                }).start();
            }
        });
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(XHWebActivity.this, ZRSettingsActivity.class), 2);
            }
        });
        install_rootfs_other.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileDialog();
            }
        });
        ZTUserBean ztUserBean = UserSetManage.getInstall1().getZTUserBean();
        if (ztUserBean.isStartTermux()) {
            startActivity(new Intent(XHWebActivity.this, TermuxActivity.class));
            finish();
        }
    }

    /**
     *
     * 注意，此方法需要用子线程运行!
     */
    private void installRootfs() {
        FileBean fileBean = new FileBean();
        fileBean.setName("rootfs.tar.xz");
        fileBean.setPath(LibSuManage.BASHRC_TERMUX_UBUNTU_TGZ_FILE);
        startInstallRootfs(fileBean, true);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                console.setText(console.getText() + "\n" + UUtils.getString(R.string.install_ok));
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 6) {
            startActivity(new Intent(XHWebActivity.this, TermuxActivity.class));
            finish();
        }
    }

    @Override
    public void onAddElement(String msg) {
        this.msg = msg;
/*        if (arrays.size() < 100) {
            arrays.add(msg);
        } else {
            arrays.remove(arrays.size() - 1);
            arrays.add(0, msg);
        }
        PrecomputedTextCompat precomputedTextCompat = PrecomputedTextCompat.create(
            msg,
            TextViewCompat.getTextMetricsParams(console)
        );*/
        Log.i("INSTALL", "onAddElement: " + Thread.currentThread().getName());
      //  mHanlder.sendEmptyMessageDelayed(0, 100);
    }

    private synchronized void runThread() {
        isRunThread = true;
        new Thread(new Runnable() {
            private String tmp = "";
            @Override
            public void run() {
                while (true) {
                    if (!isRunThread) {
                        Log.i("TAG", "LogServices-----log Thread is End. ");
                        break;
                    }
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if ( tmp != null && msg != null && !tmp.equals(msg)) {
                        if (arrays.size() < 100) {
                            arrays.add(msg + "\n");
                        } else {
                            arrays.add(arrays.size() - 1,  msg + "\n");
                            arrays.remove(0);
                        }
                        tmp = msg;
                        mHanlder.sendEmptyMessageDelayed(0, 100);
                    }

                }

            }
        }).start();
    }
    private void showFileDialog() {
        PathSelectFragment selector = PathSelector.build(this, MConstants.BUILD_DIALOG)
            //.setBuildType(MConstants.BUILD_DIALOG)//已经在build中已经设置了
            //.setContext(this)//已经在build中已经设置了
            .setRootPath("/storage/emulated/0/")//初始路径
            .setShowSelectStorageBtn(true)//是否显示内部存储选择按钮
            .setShowTitlebarFragment(true)//是否显示标题栏
            .setShowTabbarFragment(true)//是否显示面包屑
            .setAlwaysShowHandleFragment(true)//是否总是显示长按弹出选项
            //.setShowFileTypes(fileTypes)//只显示(没有后缀)或(后缀为mp3)或(后缀为mp4)的文件
            //.setSelectFileTypes(fileTypes)//只能选择(没有后缀)或(后缀为mp3)的文件
            .setMaxCount(3)//最多可以选择3个文件,默认是-1不限制
            .setRadio()//单选(如果需要单选文件夹请使用setMaxCount(0)来替换)
            .setSortType(MConstants.SortRules.SORT_NAME_ASC)//按名称排序
            .setTitlebarMainTitle(new FontBean("check File"))//设置标题栏主标题,还可以设置字体大小,颜色等
            .setTitlebarBG(UUtils.getColor(R.color.theme_color))//设置标题栏颜色
            .setFileItemListener(//设置文件item点击回调(点击是文件才会回调,如果点击是文件夹则不会)
                new FileItemListener() {
                    @Override
                    public boolean onClick(View v, FileBean fileBean, String currentPath, BasePathSelectFragment pathSelectFragment) {
                        String name = fileBean.getName();
                        if (name == null || name.isEmpty()) {
                            return false;
                        }
                        if (!(name.toLowerCase().endsWith(fileType[0])
                            || name.toLowerCase().endsWith(fileType[1])
                            || name.toLowerCase().endsWith(fileType[2]))) {
                            ToastUtils.showShort(R.string.file_type_error);
                            return false;
                        }
                        pathSelectFragment.dismiss();
                        if (isRunThread) {
                            UUtils.showMsg(UUtils.getString(R.string.not_install));
                            return false;
                        }
                        isRunThread = true;
                        runThread();
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                startInstallRootfs(fileBean, false);
                            }
                        }).start();
                        return false;
                    }
                }
            )
            .setMorePopupItemListeners(//设置右上角选项回调
                new CommonItemListener("SelectAll") {
                    @Override
                    public boolean onClick(View v, TextView tv, List<FileBean> selectedFiles, String currentPath, BasePathSelectFragment pathSelectFragment) {
                        pathSelectFragment.selectAllFile(true);
                        return false;
                    }
                },
                new CommonItemListener("DeselectAll") {
                    @Override
                    public boolean onClick(View v, TextView tv, List<FileBean> selectedFiles, String currentPath, BasePathSelectFragment pathSelectFragment) {
                        pathSelectFragment.selectAllFile(false);
                        return false;
                    }
                }
            )
            .setHandleItemListeners(//设置长按弹出选项回调
                new CommonItemListener("OK") {
                    @Override
                    public boolean onClick(View v, TextView tv, List<FileBean> selectedFiles, String currentPath, BasePathSelectFragment pathSelectFragment) {

                        return false;
                    }
                },
                new CommonItemListener("cancel") {
                    @Override
                    public boolean onClick(View v, TextView tv, List<FileBean> selectedFiles, String currentPath, BasePathSelectFragment pathSelectFragment) {
                        pathSelectFragment.openCloseMultipleMode(false);
                        pathSelectFragment.dismiss();

                        return false;
                    }
                }
            )
            .show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isRunThread = false;
        mHanlder.removeCallbacksAndMessages(null);
    }

    /**
     * 开始安装自定义Rootfs 1.输出安装脚本到本地目录
     */
    private void startInstallRootfs(FileBean fileBean, boolean isOutputRootfs) {
        //输出工具包
        //######################1.输出工具包到私有目录
        LibSuManage.getInstall().outputFile(isOutputRootfs);

        //######################2.输出工具包加压sh脚本
        ZRShell.initInstallTool();
        writerShellSh(ZRShell.installTool, ZRFileUrl.installTool);

        //######################3.解压工具包
        LibSuManage.getInstall().shellCommandExec("unzip_tool");

        //######################4.输出解压Rootfs脚本到目录
        ZRShell.initData(fileBean.getPath());
        writerShellSh(ZRShell.installRootfs, ZRFileUrl.installRootfs);

        //######################5.解压rootfs脚本
        LibSuManage.getInstall().shellCommandExec("unzip_rootfs");

        //######################6.写入启动脚本
        String[] assetsStartRootfs = getAssetsStartRootfs();
        ZRShell.initStartRootfs(assetsStartRootfs);
        writerShellSh(ZRShell.startRootfs, ZRFileUrl.startRootfs);
        LibSuManage.getInstall().shellCommandExec("unzip_start_rootfs");
        //######################7.写入hosts文件，不然无法上网
        try {
            LibSuManage.getInstall().writerResolvFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        isRunThread = false;
        //######################8.程序所有工作已经完成...
    }

    //读取assets文件
    private String[] getAssetsStartRootfs() {
        try {
            ArrayList<String> arrayList = new ArrayList<>();
            InputStreamReader inputReader = new InputStreamReader( getResources().getAssets().open("startrootfs.sh") );
            BufferedReader bufReader = new BufferedReader(inputReader);
            String line="";
            String Result="";
            while((line = bufReader.readLine()) != null) {
                arrayList.add(line);
            }
            return arrayList.toArray(new String[]{});
        } catch (IOException e) {
           e.printStackTrace();
        }
        return new String[]{};
    }

    private void writerShellSh(String[] sh, String filePath) {
        File file = new File(filePath);
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            PrintWriter  printWriter = new PrintWriter(new OutputStreamWriter(new FileOutputStream(file)));
            for (int i = 0; i < sh.length; i++) {
                printWriter.print(sh[i] + "\n");
                printWriter.flush();
            }
            printWriter.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
