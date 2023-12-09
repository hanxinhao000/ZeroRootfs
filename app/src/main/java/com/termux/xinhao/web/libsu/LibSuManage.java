package com.termux.xinhao.web.libsu;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;


import com.termux.shared.termux.TermuxConstants;
import com.termux.xinhao.web.utils.LogUtils;
import com.termux.xinhao.web.utils.UUtils;
import com.topjohnwu.superuser.CallbackList;
import com.topjohnwu.superuser.Shell;
import com.xinhao.web.services.BuildConfig;
import com.xinhao.web.services.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.List;

public class LibSuManage {
    private static LibSuManage mLibSuManage = null;
    private static final String TAG = "LibSuManage";
    private static final String BASHRC_TERMUX_UBUNTU_DIR_FILE = TermuxConstants.TERMUX_HOME_DIR_PATH;
    private static final String BASHRC_TERMUX_PROOT_DIR_FILE = TermuxConstants.TERMUX_FILES_DIR_PATH;
    public static final String BASHRC_TERMUX_UBUNTU_TGZ_FILE = TermuxConstants.TERMUX_HOME_DIR_PATH + "/rootfs.tar.xz";
    private static final String BASHRC_TERMUX_SH_TGZ_FILE = TermuxConstants.TERMUX_HOME_DIR_PATH + "/startrootfs.sh";
    private static final String BASHRC_TERMUX_PROOT_FILE = BASHRC_TERMUX_PROOT_DIR_FILE + "/assets.zip";
    private static final String BASHRC_TERMUX_BUSYBOX_FILE = BASHRC_TERMUX_PROOT_DIR_FILE + "/busybox";
    private static final String BASHRC_TERMUX_RESOLV_TGZ_FILE = TermuxConstants.TERMUX_HOME_DIR_PATH + "/ubuntu-fs/etc/resolv.conf";
    ///data/data/com.xinhao.web.services/files/home/ubuntu-fs/etc/resolv.conf

    private int cunt = 0;

    public int getCunt() {
        return cunt;
    }

    public void setCunt(int cunt) {
        this.cunt = cunt;
    }

    private TimerListener mTimerListener;
    private final List<String> mConsoleList = new TimerCallbackList();

    private Thread mThread;
    private ShellLogRunnable mShellLogRunnable;

    private boolean isRun = false;

    private LibSuManage(){

    }
    public static LibSuManage getInstall() {
        if (mLibSuManage == null) {
            synchronized (LibSuManage.class) {
                if (mLibSuManage == null) {
                    mLibSuManage = new LibSuManage();
                    return mLibSuManage;
                } else {
                    return mLibSuManage;
                }
            }
        } else {
            return mLibSuManage;
        }
    }

    public void initTimer() {
        try {
            Shell.enableVerboseLogging = BuildConfig.DEBUG;
            Shell.setDefaultBuilder(Shell.Builder.create()
                .setFlags(Shell.FLAG_REDIRECT_STDERR)
                .setInitializers(ZeroTermuxInitializer.class)
            );
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void outputFile(boolean isOutputRootfs) {

        LogUtils.e(TAG, "initTimer init.....");
        File prootFileDir = new File(BASHRC_TERMUX_PROOT_DIR_FILE);
        if (!prootFileDir.exists()) {
            prootFileDir.mkdirs();
        }
        File ubuntuFileDir = new File(BASHRC_TERMUX_UBUNTU_DIR_FILE);
        if (!ubuntuFileDir.exists()) {
            ubuntuFileDir.mkdirs();
        }
        try {
            if (!writerFile(isOutputRootfs)) {
                LogUtils.e(TAG, "initTimer timer file init fail, bashrc.sh not exists..");
                return;
            }
        } catch (Exception e) {
            LogUtils.e(TAG, "initTimer timer file init fail, e: " + e);
            e.printStackTrace();
            return;
        }
    }

    public boolean writerFile(boolean isOutputRootfs) throws IOException {
        if (isOutputRootfs) {
            return writerProotFile() && writerUbuntuFile() && writerBusyBoxFile() && writerShFile();
        } else {
            return writerProotFile() && writerBusyBoxFile() && writerShFile();
        }

    }

    private boolean writerProotFile() throws IOException {
        File prootDir = new File(BASHRC_TERMUX_PROOT_DIR_FILE);
        File prootFile = new File(BASHRC_TERMUX_PROOT_FILE);
        if (!prootDir.exists()) {
            prootDir.mkdirs();
        }
        LogUtils.i(TAG, "writerTermuxFile TermuxFile is : " + prootFile.exists());
        if (prootFile.exists()) {
            return true;
        }
        UUtils.writerFileInput(prootFile, UUtils.getContext().getAssets().open("assets.zip"), null);
        return prootFile.exists();
    }

    private boolean writerUbuntuFile() throws IOException {
        File prootDir = new File(BASHRC_TERMUX_UBUNTU_DIR_FILE);
        File prootFile = new File(BASHRC_TERMUX_UBUNTU_TGZ_FILE);
        if (!prootDir.exists()) {
            prootDir.mkdirs();
        }
        LogUtils.i(TAG, "writerTermuxFile TermuxFile is : " + prootFile.exists());
        if (prootFile.exists()) {
            return true;
        }
        UUtils.writerFileInput(prootFile, UUtils.getContext().getAssets().open("rootfs.tar.xz"), null);
        return prootFile.exists();
    }

    private boolean writerShFile() throws IOException {
        File prootDir = new File(BASHRC_TERMUX_UBUNTU_DIR_FILE);
        File prootFile = new File(BASHRC_TERMUX_SH_TGZ_FILE);
        if (!prootDir.exists()) {
            prootDir.mkdirs();
        }
        LogUtils.i(TAG, "writerTermuxFile TermuxFile is : " + prootFile.exists());
        if (prootFile.exists()) {
            return true;
        }
        UUtils.writerFileInput(prootFile, UUtils.getContext().getAssets().open("startrootfs.sh"), null);
        return prootFile.exists();
    }

    public boolean writerResolvFile() throws IOException {
        File resolvFile = new File(BASHRC_TERMUX_RESOLV_TGZ_FILE);
        LogUtils.i(TAG, "writerTermuxFile TermuxFile is : " + resolvFile.exists());
        if (resolvFile.exists()) {
            return true;
        }
        UUtils.writerFileInput(resolvFile, UUtils.getContext().getAssets().open("resolv.conf"), null);
        return resolvFile.exists();
    }

    private boolean writerBusyBoxFile() throws IOException {
        File prootDir = new File(BASHRC_TERMUX_PROOT_DIR_FILE);
        File prootFile = new File(BASHRC_TERMUX_BUSYBOX_FILE);
        if (!prootDir.exists()) {
            prootDir.mkdirs();
        }
        LogUtils.i(TAG, "writerTermuxFile TermuxFile is : " + prootFile.exists());
        if (prootFile.exists()) {
            return true;
        }
        UUtils.writerFileInput(prootFile, UUtils.getContext().getAssets().open("busybox"), null);
        return prootFile.exists();
    }


    public void shellCommandExec(String funName) {
        try {
            new ShellCommandExecRunnable(funName, mConsoleList).run();
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.e(TAG, "shellCommandExec is error: " + e);
        }

    }

    public void stop() {
        try {
            Shell shell = Shell.getCachedShell();
            if (shell != null && shell.isAlive()) {
                shell.close();
            }

        } catch (IOException e) {
            Log.e(TAG, "Error when closing shell", e);
        }
    }

    public void logThreadStop() {
        if (mShellLogRunnable != null) {
            mShellLogRunnable.stop();
            mShellLogRunnable = null;
        }
    }

    public boolean isRun() {
        if (mShellLogRunnable == null) {
            return false;
        }
        return !mShellLogRunnable.isStop;
    }

    public void shellCommandSubmit(String funName) {
        mThread = new Thread(new ShellCommandSubmitRunnable(funName, mConsoleList));
        mThread.start();
    }

    private static class ShellCommandExecRunnable implements Runnable {
        private String mFunName;
        private List<String> mConsoleList;
        public ShellCommandExecRunnable(String funName, List<String> consoleList) {
            this.mFunName = funName;
            this.mConsoleList = consoleList;
        }
        @Override
        public void run() {
           Shell.cmd(mFunName).to(mConsoleList).exec();
            LogUtils.e(TAG, "ShellCommandExecRunnable is end.");
        }
    }

    private static class ShellKillAllCommandExecRunnable implements Runnable {
        private String mFunName;
        private List<String> mConsoleList;
        public ShellKillAllCommandExecRunnable(String funName, List<String> consoleList) {
            this.mFunName = funName;
            this.mConsoleList = consoleList;
        }
        @Override
        public void run() {
            Shell.cmd(mFunName).to(mConsoleList).submit();
            LogUtils.e(TAG, "ShellKillAllCommandExecRunnable is end.");
        }
    }

    private static class ShellCommandSubmitRunnable implements Runnable {
        private String mFunName;
        private List<String> mConsoleList;
        public ShellCommandSubmitRunnable(String funName, List<String> consoleList) {
            this.mFunName = funName;
            this.mConsoleList = consoleList;
        }
        @Override
        public void run() {
            Shell.cmd(mFunName).to(mConsoleList).submit();
            LogUtils.e(TAG, "ShellCommandSubmitRunnable is end.");
        }
    }



    static class ZeroTermuxInitializer extends Shell.Initializer {
        @Override
        public boolean onInit(@NonNull Context context, @NonNull Shell shell) {
            // Load our init script
            LogUtils.e(TAG, "ZeroTermuxInitializer init.....");
            InputStream bashrc = context.getResources().openRawResource(R.raw.bashrc);
            shell.newJob().add(bashrc).exec();
            return true;
        }
    }

    class TimerCallbackList extends CallbackList<String> {
        @Override
        public void onAddElement(String s) {
            if (mTimerListener != null) {
                mTimerListener.onAddElement(s);
            }
            //输出LOG到指定目录
            if (mShellLogRunnable != null) {
                mShellLogRunnable.writerString(s);
            }
        }
    }


    private static class ShellLogRunnable implements Runnable {
        private  File mFilePath;
        private  boolean isStop;
        private  String message = "";
        PrintWriter printWriter;
        public ShellLogRunnable(File mFilePath) {
            this.mFilePath = mFilePath;
            isStop = false;
            try {
                if (mFilePath.exists()) {
                    mFilePath.createNewFile();
                }
                printWriter = new PrintWriter(new OutputStreamWriter(new FileOutputStream(mFilePath)));
            } catch (FileNotFoundException e) {
              e.printStackTrace();
            } catch (IOException e) {
              e.printStackTrace();
            }
        }
        @Override
        public void run() {
            while (true) {
                if (isStop) {
                    LogUtils.e(TAG, "ShellLogRunnable stop...");
                    break;
                }
                try {
                    Thread.sleep(500);
                    if (mFilePath.exists()) {
                        mFilePath.createNewFile();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if(printWriter != null && (message != null || !message.isEmpty())) {
                    printWriter.print(message);
                    printWriter.flush();
                    message = "";
                }
            }
        }

        public void stop() {
            isStop = true;
        }

        public void writerString(String msg) {
            message += "\n" + msg;
        }
    }
    public void setTimerListener(TimerListener timerListener) {
        this.mTimerListener = timerListener;
    }

    public interface TimerListener {
        public void onAddElement(String msg);
    }

    private String getLogName() {
        return "ZeroTermuxTimer_" + System.currentTimeMillis() + ".log";
    }
}
