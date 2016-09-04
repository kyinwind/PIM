package com.kylinwind.pim;

import android.app.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.BaseAdapter;
import android.widget.EditText;

import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

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
    List<Map<String, String>> infoDetailList = new ArrayList<Map<String, String>>();
    ListViewAdapter lva;
    private ListView listView = null;
    String[] data;
    String[] value;
    int id;
    int type;
    ImageButton ibBack;
    ImageButton ibOk;
    ImageButton ibCancel;
    RelativeLayout buttongroup;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_info_detail, container, false);
        listView = (ListView) v.findViewById(R.id.listView_info_detail);    //将listView与布局对象关联
        lva = new ListViewAdapter(cont);
        listView.setAdapter(lva);

        ibBack = (ImageButton) v.findViewById(R.id.ibBackFromInfoDetail);
        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });

        ibOk = (ImageButton) v.findViewById(R.id.ibOK);
        ibCancel = (ImageButton) v.findViewById(R.id.ibCancel);
        buttongroup = (RelativeLayout) v.findViewById(R.id.buttongroup);

        ibOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        ibCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        //设置按钮是否可见
        if (id > 0) {
            buttongroup.setVisibility(View.INVISIBLE);
        } else {
            buttongroup.setVisibility(View.VISIBLE);
        }

        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        cont = context;
    }

    public void setData(int type, int id) {
        this.type = type;
        this.id = id;
        //根据id初始化数据
        if (id > 0) {
            PersonalInfo pi = DataSupport.find(PersonalInfo.class, id);
            int i = 0;
            switch (type) {
                case R.mipmap.bank:
                    i = 5;
                    data = new String[i];
                    value = new String[i];
                    data[0] = "类别";
                    data[1] = "开户行";
                    data[2] = "户名";
                    data[3] = "卡号";
                    data[4] = "密码";
                    data[5] = "备注";
                    value[0] = pi.getType();
                    value[1] = pi.getBank();
                    value[2] = pi.getBank_user_name();
                    value[3] = pi.getCard();
                    value[4] = pi.getPassword();
                    value[5] = pi.getRemarks();
                    break;
                case R.mipmap.website:
                    i = 5;
                    data = new String[i];
                    value = new String[i];
                    data[0] = "标题";
                    data[1] = "用户名";
                    data[2] = "密码";
                    data[3] = "网址";
                    data[4] = "邮箱";
                    data[5] = "备注";
                    value[0] = pi.getTitle();
                    value[1] = pi.getUser_name();
                    value[2] = pi.getPassword();
                    value[3] = pi.getUrl();
                    value[4] = pi.getMail();
                    value[5] = pi.getRemarks();
                    break;
                default:
                    i = 2;
                    data = new String[i];
                    value = new String[i];
                    data[0] = "标题";
                    data[1] = "内容";
                    value[0] = pi.getTitle();
                    value[1] = pi.getRemarks();
                    break;

            }
            if (i > 0) {
                int m = 0;
                for (m = 0; m < i; m++) {
                    HashMap map = new HashMap();
                    map.put(data[m], value[m]);
                    infoDetailList.add(map);
                }
            }
        } else if (id == 0) {
            //新建一条记录
            int i = 0;

            switch (type) {
                case R.mipmap.bank:
                    i = 5;
                    data = new String[i + 1];
                    value = new String[i + 1];
                    data[0] = "类别";
                    data[1] = "开户行";
                    data[2] = "户名";
                    data[3] = "卡号";
                    data[4] = "密码";
                    data[5] = "备注";
                    value[0] = "";
                    value[1] = "";
                    value[2] = "";
                    value[3] = "";
                    value[4] = "";
                    value[5] = "";
                    break;
                case R.mipmap.website:
                    i = 5;
                    data = new String[i + 1];
                    value = new String[i + 1];
                    data[0] = "标题";
                    data[1] = "用户名";
                    data[2] = "密码";
                    data[3] = "网址";
                    data[4] = "邮箱";
                    data[5] = "备注";
                    value[0] = "";
                    value[1] = "";
                    value[2] = "";
                    value[3] = "";
                    value[4] = "";
                    value[5] = "";
                    break;
                default:
                    i = 1;
                    data = new String[i + 1];
                    value = new String[i + 1];
                    data[0] = "标题";
                    data[1] = "内容";
                    value[0] = "";
                    value[1] = "";
                    break;

            }
            if (i > 0) {
                int m = 0;
                for (m = 0; m <= i; m++) {
                    HashMap map = new HashMap();
                    map.put(data[m], value[m]);
                    infoDetailList.add(map);
                }
            }
        }
    }

    public class ListViewAdapter extends BaseAdapter {
        List<View> itemViews = new ArrayList();

        private Context mContext;

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            if (convertView == null) {
                v = this.generateView(position, parent);
            }
            this.fillValues(position, v);
            return v;
        }

        @Override
        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();
        }

        public ListViewAdapter(Context mContext) {
            this.mContext = mContext;
            for (int i = 0; i < infoDetailList.size(); i++) {
                itemViews.add(null);
            }
        }


        @Override
        public int getCount() {
            return itemViews.size();
        }

        @Override
        public View getItem(int position) {
            return itemViews.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public View generateView(int position, ViewGroup viewGroup) {
            //render a new item layout.
            LayoutInflater inflater = (LayoutInflater) cont
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = inflater.inflate(R.layout.listitem_info_detail, null);
            itemViews.set(position, v);
            return v;
        }

        public void fillValues(int i, View view) {
/*fill values to your item layout returned from `generateView`.
  The position param here is passed from the BaseAdapter's 'getView()*/
            String strTitle = data[i];
            String strText = infoDetailList.get(i).get(data[i]);

            // 通过findViewById()方法实例R.layout.item内各组件
            TextView title = (TextView) view.findViewById(R.id.listitem_info_detail_tvTitle);
            title.setText(strTitle);    //填入相应的值
            EditText edtext = (EditText) view.findViewById(R.id.listitem_info_detail_title_edText);
            edtext.setText(strText);
            Spinner spinner = (Spinner) view.findViewById(R.id.listitem_info_detail_spinner);
            //只有类型是银行账户，并且是第一项时，才显示选择银行的spinner
            if (type == R.mipmap.bank && i == 0) {
                spinner.setVisibility(View.VISIBLE);
                edtext.setVisibility(View.INVISIBLE);
            } else {
                spinner.setVisibility(View.INVISIBLE);
                edtext.setVisibility(View.VISIBLE);
            }
        }

    }
}
