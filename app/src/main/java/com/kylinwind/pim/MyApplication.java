package com.kylinwind.pim;

import android.app.Activity;
import android.app.admin.SystemUpdatePolicy;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.kylinwind.pim.model.SysUser;

import org.litepal.LitePalApplication;
import org.litepal.tablemanager.Connector;

import java.lang.ref.WeakReference;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by yangx on 2016/8/14.
 */
public class MyApplication extends LitePalApplication {
    private String TAG = "MyApplication";
    private Timer mTimer = null;
    private TimerTask mTimerTask = null;
    public Activity curActivity = null;
    private int interval_time;//通过preference保存的锁屏间隔时间
    public SQLiteDatabase db = null;

    public SQLiteDatabase getDb() {
        if (db == null) {
            //初始化数据库，创建数据库或获得一个数据库
            SQLiteDatabase db = Connector.getDatabase();
            Log.d(TAG, "Connector.getDatabase执行");
        }
        return db;
    }

    public void setDb(SQLiteDatabase db) {
        this.db = db;
    }

    //记录下来用户登录的最近的时间，用于控制是否需要解锁登录
    private static Calendar c = Calendar.getInstance();

    public void setCurActivity(Activity activity) {
        curActivity = activity;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "MyApplication onCreate 事件执行");

        startTimer();
        //刷新下时间
        c.setTime(new Date());

        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle bundle) {

            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {
                ((MyApplication) getApplicationContext()).setCurActivity(activity);
                Log.d(TAG, "当前activity是：" + activity.getClass().getName());
            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                //如果主activity结束了，那么就退出应用
                if (activity.getClass().equals(MainActivity.class)) {
                    mTimer.cancel();
                    mTimer.purge();
                    mTimerTask.cancel();
                    mTimerTask = null;
                    mTimer = null;
                    System.exit(0);
                }
            }
        });
    }

    private void getIntervalTime() {
        //取出锁屏间隔时间
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String intertime = sharedPreferences.getString("time", "120");
        this.interval_time = Integer.valueOf(intertime).intValue();
        Log.d(TAG, "锁屏时间间隔是：" + intertime + "秒");
    }

    private void startTimer() {
        if (mTimer == null) {
            mTimer = new Timer();
        }

        if (mTimerTask == null) {
            mTimerTask = new TimerTask() {
                @Override
                public void run() {
                    //检查无操作时间是否持续一分钟
                    startPatternLock();
                }
            };
        }
        //每1秒钟调用一次timertask，检查是否有操作，如果无操作持续时间超过偏好设置设置的时间，那么就自动锁定
        if (mTimer != null && mTimerTask != null)
            //每秒检查一次
            mTimer.schedule(mTimerTask, 0, 1000);
    }

    private void stopTimer() {
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
        if (mTimerTask != null) {
            mTimerTask.cancel();
            mTimerTask = null;
        }
    }

    public void startPatternLock() {
        //刷新下间隔时间
        this.getIntervalTime();
        //比较一下时间，超出10秒则需要重新登录
        Calendar c2 = Calendar.getInstance();
        c2.setTime(new Date());
        // 得微秒级时间差
        long val = c2.getTimeInMillis() - c.getTimeInMillis();
        // 换算后得到秒数
        long s = val / (1000);
        if (s > interval_time && this.curActivity != null && !this.curActivity.getClass().equals(MyConfirmPatternActivity.class)) {
            //启动解锁界面
            Intent intent = new Intent(this, MyConfirmPatternActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            refreshTime();
            this.startActivity(intent);
            Log.d(TAG, "需要启动解锁界面。。。");
        } else if (this.curActivity == null) {
            //如果还未有设置当前curActivity值那么返回
            return;
            //Log.d(TAG, "不需要启动解锁界面 class a：" + this.curActivity.getClass().getName());
            //Log.d(TAG, "不需要启动解锁界面 s：" + new Long(s).toString());
        } else if (this.curActivity.getClass() == MySetPatternActivity.class) {
            //如果正在设置密码，那么也返回
            return;
        }
    }

    public void refreshTime() {
        //刷新下时间
        c.setTime(new Date());
    }
}
