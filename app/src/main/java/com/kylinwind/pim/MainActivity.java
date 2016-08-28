package com.kylinwind.pim;


import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;

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
    }

}
