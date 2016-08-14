package com.kylinwind.pim.model;

import org.litepal.annotation.Column;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yangx on 2016/8/14.
 */
public class Catalog  extends DataSupport {
    @Column(unique = true, defaultValue = "unknown")
    private int catalog_id;
    private String name;
    private String type;
    private String icon;
    private int up_catalog_id;
    private int ord;

    private List<PersonalInfo> personalInfoList = new ArrayList<PersonalInfo>();

    public List<PersonalInfo> getPersonalInfoList() {
        return personalInfoList;
    }

    public void setPersonalInfoList(List<PersonalInfo> personalInfoList) {
        this.personalInfoList = personalInfoList;
    }
// generated getters and setters.


    public int getCatalog_id() {
        return catalog_id;
    }

    public void setCatalog_id(int catalog_id) {
        this.catalog_id = catalog_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getUp_catalog_id() {
        return up_catalog_id;
    }

    public void setUp_catalog_id(int up_catalog_id) {
        this.up_catalog_id = up_catalog_id;
    }

    public int getOrd() {
        return ord;
    }

    public void setOrd(int ord) {
        this.ord = ord;
    }
}
