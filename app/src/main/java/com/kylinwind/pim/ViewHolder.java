package com.kylinwind.pim;

import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by yangx on 2016/9/4.
 */
public class ViewHolder {

    public ImageView iv;
    public TextView tvTitle;
    public TextView tvInfo;
    public int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ImageView getIv() {
        return iv;
    }

    public void setIv(ImageView iv) {
        this.iv = iv;
    }

    public TextView getTvTitle() {
        return tvTitle;
    }

    public void setTvTitle(TextView tvTitle) {
        this.tvTitle = tvTitle;
    }

    public TextView getTvInfo() {
        return tvInfo;
    }

    public void setTvInfo(TextView tvInfo) {
        this.tvInfo = tvInfo;
    }

}
