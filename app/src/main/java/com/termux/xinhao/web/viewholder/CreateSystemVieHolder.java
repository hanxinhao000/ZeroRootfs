package com.termux.xinhao.web.viewholder;

import android.view.View;
import android.widget.TextView;

import com.xinhao.web.services.R;


public class CreateSystemVieHolder extends ViewHolder {


    public TextView title;

    public TextView msg;



    public CreateSystemVieHolder(View mView) {
        super(mView);

        title = (TextView) findViewById(R.id.title);

        msg = (TextView) findViewById(R.id.msg);


    }
}
