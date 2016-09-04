package com.kylinwind.pim;


import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;

public class MainActivity extends Activity {
    private static String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FragmentManager fm;
        FragmentTransaction ft;
        fm = getFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);
        if (fragment == null) {
            fragment = new FragmentCatalog();
            ft = fm.beginTransaction();
            ft.add(R.id.fragment_container, fragment);
            ft.addToBackStack(null);
            ft.commit();
        }

        //检查一下是否已经设置解锁密码，如果没有设置，那么就启动设置界面
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        //检查偏好里面是否设置了图案解锁，默认是需要
        Boolean b = sharedPreferences.getBoolean("patternlock_yesorno", true);
        //检查是否保存了解锁密码
        String password = sharedPreferences.getString("patternlock_string", "");
        if (b.booleanValue()) {
            //如果还未设置密码，那么开始设置密码
            if (password.equals("")) {
                Intent intent = new Intent(this, MySetPatternActivity.class);
                this.startActivity(intent);
            } else {
                //如果已经设置了密码，那么验证一下
                Intent intent = new Intent(this, MyConfirmPatternActivity.class);
                this.startActivity(intent);
            }

        }
    }

    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        //获得图案
        switch (resultCode) { //resultCode为回传的标记，我在B中回传的是RESULT_OK
            case RESULT_OK:
                Bundle b = data.getExtras(); //data为B中回传的Intent
                String patternstr = b.getString("pattern");//str即为回传的值
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("patternlock_string", patternstr);
                editor.commit();
                break;
            default:
                break;
        }
    }*/

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
    protected void onResume() {
        super.onResume();
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
    }

    /**
     * 监听Back键按下事件,方法1:
     * 注意:
     * super.onBackPressed()会自动调用finish()方法,关闭
     * 当前Activity.
     * 若要屏蔽Back键盘,注释该行代码即可
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.d(TAG, "按下了back键   onBackPressed()");
    }
    /**
     * 监听Back键按下事件,方法2:
     * 注意:
     * 返回值表示:是否能完全处理该事件
     * 在此处返回false,所以会继续传播该事件.
     * 在具体项目中此处的返回值视情况而定.
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            Log.d(TAG, "按下了back键   onKeyDown()");
        }
        return super.onKeyDown(keyCode, event);
    }

}
