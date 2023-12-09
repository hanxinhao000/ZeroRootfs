package com.termux.xinhao.web.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;


import com.termux.xinhao.web.bean.ReadSystemBean;
import com.termux.xinhao.web.utils.UUtils;
import com.termux.xinhao.web.viewholder.CreateSystemVieHolder;
import com.termux.xinhao.web.viewholder.ViewHolder;
import com.xinhao.web.services.R;

import java.util.List;


public class CreateSystemAdapter extends ListBaseAdapter<ReadSystemBean> {

    private Activity mActivity;

    public CreateSystemAdapter(List<ReadSystemBean> list, Activity activity) {
        super(list);
        mActivity = activity;
    }


    @Override
    public ViewHolder getViewHolder() {
        return new CreateSystemVieHolder(View.inflate(UUtils.getContext(), R.layout.list_create_system, null));
    }

    @Override
    public void initView(int position, ReadSystemBean readSystemBean, ViewHolder viewHolder) {

        CreateSystemVieHolder createSystemVieHolder = (CreateSystemVieHolder) viewHolder;

        createSystemVieHolder.title.setText(readSystemBean.name);

        createSystemVieHolder.msg.setText(readSystemBean.dir);


        if (readSystemBean.isCkeck) {

            createSystemVieHolder.title.setText(readSystemBean.name + "   <——");
            createSystemVieHolder.title.setTextColor(Color.parseColor("#ad0015"));

        } else {
            createSystemVieHolder.title.setText(readSystemBean.name);
            createSystemVieHolder.title.setTextColor(Color.parseColor("#ffffff"));
        }


    }
}
