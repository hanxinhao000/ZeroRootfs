package com.termux.xinhao.web.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.system.ErrnoException;
import android.system.Os;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;


import com.termux.app.TermuxService;
import com.termux.app.activities.SettingsActivity;
import com.termux.shared.termux.TermuxConstants;
import com.termux.shared.zerorootfs.UserSetManage;
import com.termux.shared.zerorootfs.bean.ZTUserBean;
import com.termux.xinhao.web.ZRFileUrl;
import com.termux.xinhao.web.utils.UUtils;
import com.xinhao.web.services.R;

import java.io.File;

public class ZRSettingsActivity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout zr_open_debug_ll;
    private SwitchCompat zr_open_debug_switch;

    private LinearLayout start_termux_ll;
    private SwitchCompat start_termux_switch;

    private CardView zr_create_sdcard_ll;
    private CardView termux_settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zrsettings);
        zr_open_debug_ll = findViewById(R.id.zr_open_debug_ll);
        zr_open_debug_switch = findViewById(R.id.zr_open_debug_switch);

        start_termux_ll = findViewById(R.id.start_termux_ll);
        start_termux_switch = findViewById(R.id.start_termux_switch);

        zr_create_sdcard_ll = findViewById(R.id.zr_create_sdcard_ll);
        termux_settings = findViewById(R.id.termux_settings);

        setSwitchStatus(zr_open_debug_switch, zr_open_debug_ll);
        setSwitchStatus(start_termux_switch, start_termux_ll);
        zr_create_sdcard_ll.setOnClickListener(this);
        termux_settings.setOnClickListener(this);
        ZTUserBean ztUserBean = UserSetManage.getInstall1().getZTUserBean();
        zr_open_debug_switch.setChecked(ztUserBean.isBug());
        start_termux_switch.setChecked(ztUserBean.isStartTermux());

    }

    private void setSwitchStatus(SwitchCompat switchCompat, LinearLayout linearLayout) {
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchCompat.setChecked(!switchCompat.isChecked());
            }
        });
        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ZTUserBean ztUserBean = UserSetManage.getInstall1().getZTUserBean();
                if (switchCompat == zr_open_debug_switch) {
                    ztUserBean.setBug(switchCompat.isChecked());
                    Intent intent = new Intent(ZRSettingsActivity.this, TermuxService.class);
                    intent.setAction(TermuxConstants.TERMUX_APP.TERMUX_SERVICE.ACTION_STOP_SERVICE);
                    startService(intent);
                } else if (switchCompat == start_termux_switch) {
                    ztUserBean.setStartTermux(switchCompat.isChecked());
                    if (ztUserBean.isStartTermux()) {
                        setResult(6);
                    } else {
                        setResult(-1);
                    }
                }
                UserSetManage.getInstall1().setZTUserBean(ztUserBean);
            }
        });
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.zr_create_sdcard_ll:
                createLink(ZRFileUrl.homeSdcard);
                break;
            case R.id.termux_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                break;
        }
    }

    //创建sdcard
    private void createLink(String path) {
        File file = new File(path);
        if (file.exists()) {
            UUtils.showMsg(UUtils.getString(R.string.exists_sdcard));
            return;
        }
        File sharedDir = Environment.getExternalStorageDirectory();
        try {
            Os.symlink(sharedDir.getAbsolutePath(), path);
            UUtils.showMsg(UUtils.getString(R.string.create_sdcard));
        } catch (ErrnoException e) {
            e.printStackTrace();
        }
    }
}
