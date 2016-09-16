package com.kylinwind.pim;

import android.app.Activity;
import android.app.Fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.EditText;

import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
    private PersonalInfo pi;
    private int catalogid;
    boolean modify_flag = false;//是否修改过
    String oper = "";//是新增还是修改

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
                goBack();
            }
        });

        ibOk = (ImageButton) v.findViewById(R.id.ibOK);
        ibCancel = (ImageButton) v.findViewById(R.id.ibCancel);
        buttongroup = (RelativeLayout) v.findViewById(R.id.buttongroup);

        ibOk.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                //将输入法隐藏，mPasswordEditText 代表密码输入框
                //InputMethodManager imm = (InputMethodManager) getActivity().getApplication().getSystemService(Context.INPUT_METHOD_SERVICE);
                //imm.hideSoftInputFromWindow(listView.getWindowToken(), 0);
                //开始保存
                if (modify_flag) {
                    getUpdatedData();

                    int i = 0;
                    switch (type) {
                        case R.mipmap.bank:
                            pi.setType(value[0]);
                            pi.setBank(value[1]);
                            pi.setBank_user_name(value[2]);
                            pi.setCard(value[3]);
                            pi.setPassword(value[4]);
                            pi.setRemarks(value[5]);

                            break;
                        case R.mipmap.website:
                            pi.setTitle(value[0]);
                            pi.setUser_name(value[1]);
                            pi.setPassword(value[2]);
                            pi.setUrl(value[3]);
                            pi.setMail(value[4]);
                            pi.setRemarks(value[5]);
                            break;
                        default:
                            pi.setTitle(value[0]);
                            pi.setRemarks(value[1]);
                            break;
                    }
                    pi.setUp_catalog_id(catalogid);
                    //取消软键盘
                    listView.clearFocus();
                    if (!pi.save()) {
                        Toast.makeText(cont, "保存失败！", Toast.LENGTH_LONG);
                    } else {
                        Toast.makeText(cont, "保存成功！", Toast.LENGTH_LONG);
                        //将保存成功的pi传回到上一个fragment
                        sendResult(Activity.RESULT_OK);
                        //清除修改标志
                        modify_flag = false;
                    }

                } else {
                    Toast.makeText(cont, "您没有修改内容，无须保存！", Toast.LENGTH_LONG);
                }


            }
        });

        ibCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goBack();
            }
        });

        //设置按钮是否可见
        if (id > 0) {
            buttongroup.setVisibility(View.GONE);
        } else {
            buttongroup.setVisibility(View.VISIBLE);
        }

        return v;
    }

    private void sendResult(int resultOk) {
        if (getTargetFragment() == null) {
            return;
        } else {
            Intent i = new Intent();
            Bundle bundle = new Bundle();
            i.putExtra("opt", oper);
            bundle.putParcelable("object",pi);
            i.putExtras(bundle);
            getTargetFragment().onActivityResult(getTargetRequestCode(), resultOk, i);
        }
    }

    public void getUpdatedData() {
        //从listview里面取出修改后的数据，放到value数组里面
        int i = 0;
        switch (type) {
            case R.mipmap.bank:
                i = 6;
                break;
            case R.mipmap.website:
                i = 6;
                break;
            default:
                i = 2;
                break;
        }
        for (int j = 0; j < i; j++) {
            View v;
            HashMap m;
            EditText et;
            v = (View) listView.getAdapter().getItem(j);
            m = (HashMap) v.getTag();
            et = (EditText) m.get("3");
            value[j] = et.getText().toString();
        }
        if (type == R.mipmap.bank) {
            View v;
            HashMap m;
            Spinner s;
            v = (View) listView.getAdapter().getItem(0);
            m = (HashMap) v.getTag();
            s = (Spinner) m.get("4");
            value[0] = s.getSelectedItem().toString();
        }
    }

    public void goBack() {
        if (modify_flag) {
            TextView tv = new TextView(cont);
            tv.setText("是否确定取消修改?");
            new AlertDialog.Builder(cont).
                    setTitle("请确定是否取消").
                    setIcon(android.R.drawable.ic_dialog_info).
                    setView(tv).
                    setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        //which为点击按钮的标识符，是一个 整形的数据，对于这三个按钮而言，每个按钮使用不同的int类型数据进行标识：
                        // Positive（-1）、Negative(-2)、 Neutral(-3)。
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            getActivity().onBackPressed();
                        }
                    }).
                    setNegativeButton("取消", null).
                    show();

        } else {
            getActivity().onBackPressed();
        }

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        cont = context;
    }

    public void initData(String opt,int type, int id, int catalogid,PersonalInfo p) {
        this.oper = opt;
        this.type = type;
        this.id = id;
        this.catalogid = catalogid;
        this.pi = p ;
        //根据id初始化数据
        if (id > 0) {
            pi = DataSupport.find(PersonalInfo.class, id);
            int i = 0;
            switch (type) {
                case R.mipmap.bank:
                    i = 6;
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
                    i = 6;
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
           /* if (i > 0) {
                int m = 0;
                for (m = 0; m < i; m++) {
                    HashMap map = new HashMap();
                    map.put(data[m], value[m]);
                    infoDetailList.add(map);
                }
            }*/
        } else if (id == 0) {
            pi = new PersonalInfo();
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
           /* if (i > 0) {
                int m = 0;
                for (m = 0; m <= i; m++) {
                    HashMap map = new HashMap();
                    map.put(data[m], value[m]);
                    infoDetailList.add(map);
                }
            }*/
        }
    }

    public class ListViewAdapter extends BaseAdapter {
        public List<View> itemViews = new ArrayList();

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
            for (int i = 0; i < data.length; i++) {
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
            String strText = value[i];

            // 通过findViewById()方法实例R.layout.item内各组件
            TextView title = (TextView) view.findViewById(R.id.listitem_info_detail_tvTitle);
            title.setText(strTitle);    //填入相应的值
            EditText edtext = (EditText) view.findViewById(R.id.listitem_info_detail_title_edText);
            edtext.setText(strText);
            edtext.addTextChangedListener(new TextWatcher() {
                @Override
                public void afterTextChanged(Editable s) {
                    // TODO Auto-generated method stub
                    //修改标记
                    modify_flag = true;
                    //s:变化后的所有字符
                    //Toast.makeText(cont, "变化:" + s, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count,
                                              int after) {
                    // TODO Auto-generated method stub
                    //s:变化前的所有字符； start:字符开始的位置； count:变化前的总字节数；after:变化后的字节数
                    //Toast.makeText(getApplicationContext(), "变化前:" + s + ";" + start + ";" + count + ";" + after, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before,
                                          int count) {
                    // TODO Auto-generated method stub
                    //S：变化后的所有字符；start：字符起始的位置；before: 变化之前的总字节数；count:变化后的字节数
                    //Toast.makeText(cont, "变化后:" + s + ";" + start + ";" + before + ";" + count, Toast.LENGTH_SHORT).show();
                }
            });

            if (!data[i].equals("备注")) {
                edtext.setSingleLine();
            } else {
                edtext.setMinLines(3);
            }
            Spinner spinner = (Spinner) view.findViewById(R.id.listitem_info_detail_spinner);
            //只有类型是银行账户，并且是第一项时，才显示选择银行的spinner
            if (type == R.mipmap.bank && i == 0) {
                spinner.setVisibility(View.VISIBLE);
                edtext.setVisibility(View.INVISIBLE);
            } else {
                spinner.setVisibility(View.INVISIBLE);
                edtext.setVisibility(View.VISIBLE);
            }
            //将map里面的数据放到tag
            HashMap map = new HashMap();
            map.put("1", data[i]);
            map.put("2", value[i]);
            map.put("3", edtext);
            map.put("4", spinner);
            view.setTag(map);

            //设置修改标志
            modify_flag = false;
        }

    }
}
