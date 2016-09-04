package com.kylinwind.pim;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.SimpleSwipeListener;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.BaseSwipeAdapter;
import com.daimajia.swipe.util.Attributes;
import com.kylinwind.pim.model.Catalog;
import com.kylinwind.pim.model.PersonalInfo;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yangx on 2016/8/30.
 */
public class FragmentInfoDetail extends Fragment {
    public Context cont;
    private static final String TAG = "FragmentInfoDetail";
    List<Map<String, Object>> infoDetailList = new ArrayList<Map<String, Object>>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_info_detail, container, false);

        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        cont = context;
    }

    public void setData(int type, int id) {
        //根据id初始化数据
        if (id > 0) {
            PersonalInfo pi = DataSupport.find(PersonalInfo.class, id);
            int i = 0;
            String[] data ;
            switch (type) {
                case R.mipmap.bank:
                    i = 5;
                    data[0]="开户行";
                    data[1]="类别";
                    data[2]="户名";
                    data[3]="卡号";
                    data[4]="密码";
                    data[5]="备注";
                    break;
                case R.mipmap.website:

                    break;
                case R.mipmap.catalog:

                    break;
                default:

                    break;
            }

            for () {

            }
        }
    }

}
