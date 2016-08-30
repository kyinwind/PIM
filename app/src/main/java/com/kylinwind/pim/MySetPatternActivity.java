package com.kylinwind.pim;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Toast;

import java.util.List;

import me.zhanghai.android.patternlock.PatternUtils;
import me.zhanghai.android.patternlock.PatternView;
import me.zhanghai.android.patternlock.SetPatternActivity;

/**
 * Created by yangx on 2016/8/29.
 */

public class MySetPatternActivity extends SetPatternActivity {

    @Override
    protected void onSetPattern(List<PatternView.Cell> pattern) {
        String patternstr = PatternUtils.patternToSha1String(pattern);
        // TODO: Save patternSha1 in SharedPreferences.
        CharSequence msg = "图案:" + patternstr ;
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("patternlock_string", patternstr);
        editor.commit();
    }


}
