package com.kylinwind.pim.model;

import org.litepal.annotation.Column;
import org.litepal.crud.DataSupport;

/**
 * Created by yangx on 2016/8/14.
 * 根据LitePal的数据类型支持，可以进行对象关系映射的数据类型一共有8种，int、short、long、float、double、boolean、String和Date,byte[]。
 * 只要是声明成这8种数据类型的字段都会被自动映射到数据库表中，并不需要进行任何额外的配置
 */
public class SysUser  extends DataSupport {
    @Column(unique = true, defaultValue = "unknown")
    private String user_name;
    private String password;
    // generated getters and setters.


    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
