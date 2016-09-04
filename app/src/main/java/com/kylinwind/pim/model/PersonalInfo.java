package com.kylinwind.pim.model;

import org.litepal.annotation.Column;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by yangx on 2016/8/14.
 Name	Code	Data Type	Length	Precision	Primary	Foreign Key	Mandatory
 编号	ID	NUMERIC(10)	10		TRUE	FALSE	TRUE
 上级目录编号	UP_CATALOG_ID	NUMERIC(10)	10		FALSE	FALSE	TRUE
 标题	TITLE	VARCHAR(50)	50		FALSE	FALSE	FALSE
 用户名	USER_NAME	VARCHAR(50)	50		FALSE	FALSE	FALSE
 密码	PASSWORD	VARCHAR(50)	50		FALSE	FALSE	FALSE
 网址	URL	VARCHAR(50)	50		FALSE	FALSE	FALSE
 邮箱	MAIL	VARCHAR(50)	50		FALSE	FALSE	FALSE
 备注	REMARKS	VARCHAR(1000)	1000		FALSE	FALSE	FALSE
 开户行	BANK	VARCHAR(50)	50		FALSE	FALSE	FALSE
 类别	TYPE	VARCHAR(50)	50		FALSE	FALSE	FALSE
 户名	BANK_USER_NAME	VARCHAR(50)	50		FALSE	FALSE	FALSE
 创建时间	CREATE_DATETIME	DATE			FALSE	FALSE	TRUE
 修改时间	MOD_DATETIME	DATE			FALSE	FALSE	TRUE
 */
public class PersonalInfo  extends DataSupport {
    @Column(unique = true, defaultValue = "unknown")
    private int id;
    private int up_catalog_id;
    private String title;
    private String user_name;
    private String password;
    private String url;
    private String mail;
    private String remarks;
    private String bank;
    private String type;
    private String bank_user_name;
    private String card;

    public String getCard() {
        return card;
    }

    public void setCard(String card) {
        this.card = card;
    }

    private Date create_datetime;
    private Date mod_datetime;

    private List<Files> filesList = new ArrayList<Files>();
    private Catalog catalog;

    public List<Files> getFilesList() {
        return filesList;
    }

    public void setFilesList(List<Files> filesList) {
        this.filesList = filesList;
    }

    public Catalog getCatalog() {
        return catalog;
    }

    public void setCatalog(Catalog catalog) {
        this.catalog = catalog;
    }
    // generated getters and setters.

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUp_catalog_id() {
        return up_catalog_id;
    }

    public void setUp_catalog_id(int up_catalog_id) {
        this.up_catalog_id = up_catalog_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBank_user_name() {
        return bank_user_name;
    }

    public void setBank_user_name(String bank_user_name) {
        this.bank_user_name = bank_user_name;
    }

    public Date getCreate_datetime() {
        return create_datetime;
    }

    public void setCreate_datetime(Date create_datetime) {
        this.create_datetime = create_datetime;
    }

    public Date getMod_datetime() {
        return mod_datetime;
    }

    public void setMod_datetime(Date mod_datetime) {
        this.mod_datetime = mod_datetime;
    }
}
