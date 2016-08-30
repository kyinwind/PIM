package com.kylinwind.pim;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import java.util.List;

import me.zhanghai.android.patternlock.ConfirmPatternActivity;
import me.zhanghai.android.patternlock.PatternUtils;
import me.zhanghai.android.patternlock.PatternView;

/**
 * Created by yangx on 2016/8/30.
 */
public class MyConfirmPatternActivity extends ConfirmPatternActivity {

    @Override
    protected boolean isStealthModeEnabled() {
        // TODO: Return the value from SharedPreferences.
        return false;
    }

    @Override
    protected boolean isPatternCorrect(List<PatternView.Cell> pattern) {
        // TODO: Get saved pattern sha1.
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        String patternSha1 = sharedPreferences.getString("patternlock_string", "");

        boolean result = TextUtils.equals(PatternUtils.patternToSha1String(pattern), patternSha1);
        //将自动锁定的时间刷新
        if (result) {
            ((MyApplication) getApplicationContext()).refreshTime();
        }
        return result;
    }

    @Override
    protected void onForgotPassword() {
        startActivity(new Intent(this, MySetPatternActivity.class));
        // Finish with RESULT_FORGOT_PASSWORD.
        super.onForgotPassword();
    }

}
