package com.kylinwind.pim.model;

import org.litepal.annotation.Column;
import org.litepal.crud.DataSupport;

import java.util.Date;

/**
 * Created by yangx on 2016/8/14.
 * Name	Code	Data Type	Length	Precision	Primary	Foreign Key	Mandatory
 编号	ID	NUMERIC(10)	10		TRUE	FALSE	TRUE
 文件名称	NAME	VARCHAR(1000)	1000		FALSE	FALSE	FALSE
 类型	TYPE	VARCHAR(1000)	1000		FALSE	FALSE	FALSE
 创建时间	CREATE_DATETIME	DATE			FALSE	FALSE	TRUE
 */
public class Files  extends DataSupport {
    @Column(unique = true, defaultValue = "unknown")
    private int id;
    private String name;
    private byte[] file_byte;
    private Date create_datetime;
    private PersonalInfo personalInfo;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreate_datetime() {
        return create_datetime;
    }

    public void setCreate_datetime(Date create_datetime) {
        this.create_datetime = create_datetime;
    }

    public byte[] getFile_byte() {
        return file_byte;
    }

    public void setFile_byte(byte[] file_byte) {
        this.file_byte = file_byte;
    }

    public PersonalInfo getPersonalInfo() {
        return personalInfo;
    }

    public void setPersonalInfo(PersonalInfo personalInfo) {
        this.personalInfo = personalInfo;
    }


}
