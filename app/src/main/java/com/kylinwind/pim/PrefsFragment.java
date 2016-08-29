package com.kylinwind.pim;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;

/**
 * Created by yangx on 2016/8/25.
 */
public class PrefsFragment extends PreferenceFragment implements android.content.SharedPreferences.OnSharedPreferenceChangeListener {
    public static String TAG = "PrefsFragment";
    private EditTextPreference uname;

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
        uname = (EditTextPreference)getPreferenceScreen().findPreference("userName");

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

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        /* get preference */

        if (key.equals("userName")) {
            InitTextSummary();
        }
    }


    public void InitTextSummary() {


        if (uname.getText().equals("")) {
            uname.setSummary("该用户名用于您登录显示");
        } else {
            uname.setSummary(uname.getText());
        }
    }


    @Override
    public void onResume() {
        super.onResume();

        InitTextSummary();
        getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
        super.onPause();
    }
}
