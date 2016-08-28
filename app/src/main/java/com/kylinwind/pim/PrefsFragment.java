package com.kylinwind.pim;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.util.Log;

/**
 * Created by yangx on 2016/8/25.
 */
public class PrefsFragment extends PreferenceFragment {
    public static String TAG = "PrefsFragment";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the setting from an XML resource
        addPreferencesFromResource(R.xml.setting);
        //设置缺省值
        //PreferenceManager.setDefaultValues(getActivity(), R.xml.setting, false);
        //设置“锁屏图案”的可用性
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        //.getApplicationContext().getSharedPreferences("com.kylinwind.pim_preferences", Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("userName", "");
        //Log.d(TAG, "锁屏图案username：" + username);
        boolean b = sharedPreferences.getBoolean("patternlock_yesorno", false);
        //Log.d(TAG, "锁屏图案Enabled：" + Boolean.valueOf(b).toString());
        PreferenceScreen patternlock = (PreferenceScreen) findPreference("patternlock");
        patternlock.setEnabled(b);
        //Log.d(TAG, "锁屏图案Enabled：" + Boolean.valueOf(b).toString());
    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen,
                                         Preference preference) {
        //如果“是否采用锁屏密码登录”这个按钮被选中，将进行括号里面的操作
        if ("patternlock_yesorno".equals(preference.getKey())) {
            CheckBoxPreference patternlock_yesorno = (CheckBoxPreference) findPreference("patternlock_yesorno");
            PreferenceScreen patternlock = (PreferenceScreen) findPreference("patternlock");
            //让editTextPreference和checkBoxPreference的状态保持一致
            patternlock.setEnabled(patternlock_yesorno.isChecked());
        }

        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }
}
