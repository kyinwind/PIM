package com.kylinwind.pim.model;

import com.kylinwind.pim.R;

import org.litepal.annotation.Column;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yangx on 2016/8/14.
 */
public class Catalog extends DataSupport {
    @Column(unique = true, defaultValue = "unknown")
    private int catalog_id;
    private String name;
    private String type;
    private int icon;
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

    public int getIcon() {
        //通过类型来设置icon
        switch (type) {
            case "bank":
                this.setIcon(R.mipmap.bank);
                break;
            case "website":
                this.setIcon(R.mipmap.website);
                break;
            case "others":
                this.setIcon(R.mipmap.catalog);
                break;
            default:
                this.setIcon(R.mipmap.catalog);
                break;
        }
        return icon;
    }

    public void setIcon(int icon) {
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

    public int getMaxCatalogId() {
        int result = DataSupport.max(Catalog.class, "catalog_id", int.class);
        return result;
    }
}
