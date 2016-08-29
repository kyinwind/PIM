package com.kylinwind.pim;

import android.content.Intent;
import android.os.Bundle;
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
        Bundle bundle = new Bundle();
        Intent intent=new Intent();
        bundle.putString("pattern", patternstr);
        intent.putExtras(bundle);
        setResult(RESULT_OK, intent); //intent为A传来的带有Bundle的intent，当然也可以自己定义新的Bundle
        finish();//此处一定要调用finish()方法
    }


}
