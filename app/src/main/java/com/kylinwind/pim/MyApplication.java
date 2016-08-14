package com.kylinwind.pim;

import android.app.admin.SystemUpdatePolicy;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import com.kylinwind.pim.model.SysUser;

import org.litepal.LitePalApplication;
import org.litepal.tablemanager.Connector;

import java.util.List;

/**
 * Created by yangx on 2016/8/14.
 */
public class MyApplication extends LitePalApplication {
    private String TAG = "MyApplication";

    @Override
    public void onCreate() {
        super.onCreate();

        Log.d(TAG, "MyApplication onCreate 事件执行");
    }
}
