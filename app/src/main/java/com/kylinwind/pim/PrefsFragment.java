package com.kylinwind.pim;

import android.content.Intent;
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
    private String patternstr = "";
    private EditTextPreference time;

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
        uname = (EditTextPreference) getPreferenceScreen().findPreference("userName");

        //如果锁屏图案已经存在，就取出来
        patternstr = sharedPreferences.getString("patternlock_string", "");

        //如果锁屏间隔已经有数据，就取出来显示
        time = (EditTextPreference) getPreferenceScreen().findPreference("time");
        time.setEnabled(b);
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
            EditTextPreference time = (EditTextPreference) findPreference("time");
            time.setEnabled(patternlock_yesorno.isChecked());
        }
        if ("patternlock".equals(preference.getKey())) {
            //打开图案设置窗口
            Intent intent = new Intent(getActivity(), MySetPatternActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("pattern", patternstr);
            intent.putExtras(bundle);
            this.startActivity(intent);//打开新的activity
        }
        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        /* get preference */

        if (key.equals("userName") || key.equals("time")) {
            InitTextSummary();
        }
    }

    public void InitTextSummary()
    {
        if (uname.getText() == null || uname.getText().equals("")) {
            uname.setSummary("该用户名用于您登录显示");
        } else {
            uname.setSummary(uname.getText());
        }
        if (time.getText() == null || time.getText().equals("")) {
            time.setSummary("应用空闲无操作锁屏间隔时间。单位：秒");
        } else {
            time.setSummary(time.getText());
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
