package com.kylinwind.pim;

import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.kylinwind.pim.model.Catalog;
import com.kylinwind.pim.model.SysUser;

import org.litepal.tablemanager.Connector;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate方法执行");
        //初始化数据库，创建数据库或获得一个数据库
        db = Connector.getDatabase();
        Log.d(TAG, "Connector.getDatabase执行");
        this.initData();

    }

    //如果是第一次运行，那么初始化数据
    public void initData() {
        //看看是否已经有了系统用户数据，如果没有，那么就增加一个
        int i = SysUser.count(SysUser.class);
        Log.d(TAG, "SysUser.count(SysUser.class)执行 i = " + i);
        if (i == 0) {
            SysUser su = new SysUser();
            su.setUser_name(getResources().getString(R.string.temp_user));
            su.setPassword("");
            if (su.save()) {
                Toast.makeText(this, "初始化用户数据成功", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "初始化用户数据失败", Toast.LENGTH_LONG).show();
            }
        }
        //看看是否有三个默认目录：银行账号，网站账号，其他，如果没有就加上
        Catalog c1 = new Catalog();
        Catalog c2 = new Catalog();
        Catalog c3 = new Catalog();
        c1.setCatalog_id(1);
        c1.setUp_catalog_id(0);
        c1.setIcon("");
        c1.setName("银行账号");
        c1.setOrd(0);
        c1.setType("bank");

        c2.setCatalog_id(2);
        c2.setUp_catalog_id(0);
        c2.setIcon("");
        c2.setName("网站账号");
        c2.setOrd(1);
        c2.setType("website");


        c3.setCatalog_id(3);
        c3.setUp_catalog_id(0);
        c3.setIcon("");
        c3.setName("其他");
        c3.setOrd(2);
        c3.setType("others");
        i = Catalog.count(Catalog.class);
        Log.d(TAG, "Catalog.count(Catalog.class)执行 i = " + i);
        if (i == 0) {
            if (c1.save() && c2.save() && c3.save()) {
                Toast.makeText(this, "初始化目录数据成功", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "初始化目录数据失败", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart方法执行");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause方法执行");
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        Log.d(TAG, "onResume方法执行");
    }


    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop方法执行");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.d(TAG, "onDestroy方法执行");
        db.close();
        Log.d(TAG, "db.close执行");
    }

}
