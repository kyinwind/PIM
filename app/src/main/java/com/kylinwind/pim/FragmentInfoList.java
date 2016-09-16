package com.kylinwind.pim;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
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
import com.kylinwind.pim.model.SysUser;

import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yangx on 2016/8/30.
 */
public class FragmentInfoList extends Fragment {
    public Context cont;
    private static final String TAG = "FragmentInfoList";
    private ImageButton ibAdd;
    private ImageButton ibDel;
    private ListView listView = null;
    private List<PersonalInfo> infolist = new ArrayList<PersonalInfo>();  //声明一个list，动态存储要显示的信
    int curInfoId;
    ListViewAdapter lva;
    ImageButton ibBack;
    private int icon;
    private String type;
    private int catalogid;//上级catalogid

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getCatalogid() {
        return catalogid;
    }

    public void setCatalogid(int catalogid) {
        this.catalogid = catalogid;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_info_list, container, false);
        listView = (ListView) v.findViewById(R.id.listView);    //将listView与布局对象关联
        ibAdd = (ImageButton) v.findViewById(R.id.ibAdd);
        ibDel = (ImageButton) v.findViewById(R.id.ibDel);
        ibBack = (ImageButton) v.findViewById(R.id.ibBackFromInfoList);
        Log.d(TAG, "调用onCreateView............");
        loadData();

        //处理Item的点击事件
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                PersonalInfo p = infolist.get(i);   //通过position获取所点击的对象
                //String infoTitle = p.getTitle();    //获取信息标题
                //String infoDetails = p.getBank();    //获取信息详情
                int id = p.getId();
                //把已经打开的swipelayout关闭
                ((ListViewAdapter) listView.getAdapter()).closeAllItems();
                //Toast显示测试
                //CharSequence msg = "信息:" + infoTitle + " , " + infoDetails;
                //Toast.makeText(cont, msg, Toast.LENGTH_SHORT).show();
                //显示详细信息
                FragmentManager fm;
                FragmentTransaction ft;
                fm = getFragmentManager();
                Fragment fragment = fm.findFragmentById(R.id.fragment_container);
                FragmentInfoDetail fragmentInfoDetail = new FragmentInfoDetail();
                fragmentInfoDetail.setTargetFragment(FragmentInfoList.this, 0);
                fragmentInfoDetail.initData("mod", icon, id, catalogid, p);
                ft = fm.beginTransaction();
                ft.add(R.id.fragment_container, fragmentInfoDetail);
                ft.addToBackStack(null);
                ft.commit();
            }
        });
        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStack();
            }
        });
        ibAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //新增一个记录
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft;
                Fragment fragment = fm.findFragmentById(R.id.fragment_container);
                FragmentInfoDetail fragmentInfoDetail = new FragmentInfoDetail();
                fragmentInfoDetail.setTargetFragment(FragmentInfoList.this, 0);
                fragmentInfoDetail.initData("add", icon, 0, catalogid, null);
                ft = fm.beginTransaction();
                ft.replace(R.id.fragment_container, fragmentInfoDetail);
                ft.addToBackStack(null);
                ft.commit();
            }
        });

        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        //取出pi
        if (resultCode != Activity.RESULT_OK) {
            return;
        } else {

            if (intent != null) {
                if (intent.hasExtra("opt")) {
                    if (intent.getStringExtra("opt").equals("add")) {
                        Bundle b = intent.getExtras();
                        PersonalInfo pi = b.getParcelable("object");
                        infolist.add(pi);
                        lva.notifyDataSetChanged();
                    }
                    if (intent.getStringExtra("opt").equals("mod")) {
                        lva.notifyDataSetChanged();
                    }
                }
            }
        }

        super.onActivityResult(requestCode, resultCode, intent);
    }

    public void loadData() {
        //取出所有记录数据
        if (listView != null) {
            infolist = DataSupport.where("up_catalog_id = ?", Integer.valueOf(catalogid).toString()).find(PersonalInfo.class);
            lva = new ListViewAdapter(cont);
            lva.setMode(Attributes.Mode.Single);

            listView.setAdapter(lva);
            lva.notifyDataSetChanged();
        }
    }

    public void initData(String type, int icon, int catid) {
        this.type = type;
        this.catalogid = catid;
        this.icon = icon;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        cont = context;
        Log.d(TAG, "调用onAttach............");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "调用onResume............");
    }

    public class ListViewAdapter extends BaseSwipeAdapter {
        List<View> itemViews = new ArrayList();

        private Context mContext;

        @Override
        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();
            //把已经打开的swipelayout关闭
            this.closeAllItems();
        }

        public ListViewAdapter(Context mContext) {
            this.mContext = mContext;
            for (int i = 0; i < infolist.size(); i++) {
                itemViews.add(null);
            }
        }

        public void addItem() {
            itemViews.add(null);
        }

        public void delItem(String name) {
            int i = 0;
            for (i = 0; i < itemViews.size(); i++) {
                View v = itemViews.get(i);
                TextView title = (TextView) v.findViewById(R.id.title);
                ;    //填入相应的值
                if (title.getText().equals(name)) {
                    break;
                }

            }
            this.delItem(i);
        }

        public void delItem(int position) {
            itemViews.remove(position);
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

        @Override
        public int getSwipeLayoutResourceId(int i) {
            //return the `SwipeLayout` resource id in your listview | gridview item layout.
            return R.id.swipe;
        }

        @Override
        public View generateView(int position, ViewGroup viewGroup) {
            //render a new item layout.
            LayoutInflater inflater = (LayoutInflater) cont
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = inflater.inflate(R.layout.listitem_info, null);
            itemViews.set(position, v);
            SwipeLayout swipeLayout = (SwipeLayout) v.findViewById(getSwipeLayoutResourceId(position));

            swipeLayout.addSwipeListener(new SimpleSwipeListener() {
                @Override
                public void onOpen(SwipeLayout layout) {

                }
            });
            swipeLayout.setOnDoubleClickListener(new SwipeLayout.DoubleClickListener() {
                @Override
                public void onDoubleClick(SwipeLayout layout, boolean surface) {
                    //Toast.makeText(cont, "DoubleClick", Toast.LENGTH_SHORT).show();
                }
            });
            v.findViewById(R.id.listitem_tvdelete).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Toast.makeText(cont, view.getTag().toString(), Toast.LENGTH_SHORT).show();
                    //找到当前item的name
                    Object o = view.getTag();
                    curInfoId = Integer.valueOf(o.toString()).intValue();
                    delInfo();
                }
            });

            return v;
        }

        @Override
        public void fillValues(int i, View view) {
/*fill values to your item layout returned from `generateView`.
  The position param here is passed from the BaseAdapter's 'getView()*/
            String strTitle = "";
            String strText = "";
            switch (icon) {
                case R.mipmap.bank:
                    strTitle = infolist.get(i).getType();
                    strText = infolist.get(i).getCard();
                    break;
                case R.mipmap.website:
                    strTitle = infolist.get(i).getTitle();
                    strText = infolist.get(i).getUrl();
                    break;
                default:
                    strTitle = infolist.get(i).getTitle();
                    int l = 0;
                    if (infolist.get(i).getRemarks().length() >= 20) {
                        strText = infolist.get(i).getRemarks().substring(0, 19).concat("...");
                    } else {
                        strText = infolist.get(i).getRemarks();
                    }
                    break;
            }

            int resId = R.mipmap.bank;
            int id = infolist.get(i).getId();
            // 通过findViewById()方法实例R.layout.item内各组件
            TextView tvDelete = (TextView) view.findViewById(R.id.listitem_tvdelete);
            //往删除tv上保存当前记录的id
            tvDelete.setTag(Integer.valueOf(id));

            TextView title = (TextView) view.findViewById(R.id.listitem_info_title);
            title.setText(strTitle);    //填入相应的值
            TextView text = (TextView) view.findViewById(R.id.listitem_info_info);
            text.setText(strText);
            ImageView image = (ImageView) view.findViewById(R.id.listitem_info_img);
            image.setImageResource(icon);
            ViewHolder vh = new ViewHolder();
            vh.setIv(image);
            vh.setTvInfo(text);
            vh.setTvTitle(title);
            vh.setId(id);
            view.setTag(vh);

        }

    }

    public void delInfo() {
        //根据名称找到id
        int i;
        PersonalInfo p = null;
        for (i = 0; i < infolist.size(); i++) {
            p = infolist.get(i);
            if (p.getId() == curInfoId) {
                break;
            }
        }
        if (p != null) {
            TextView tv = new TextView(cont);
            switch (icon) {
                case R.mipmap.bank:
                    tv.setText("是否确定删除该记录:\n银行类型:" + p.getType() + "\n卡号：" + p.getCard() + "?");
                    break;
                case R.mipmap.website:
                    tv.setText("是否确定删除该记录:\n标题:" + p.getTitle() + "\n网址URL:" + ((p.getUrl() == null) ? "" : p.getUrl()) + "?");
                    break;
                case R.mipmap.catalog:
                    tv.setText("是否确定删除该记录:" + p.getTitle() + "?");
                    break;
                default:
                    tv.setText("是否确定删除该记录" + p.getTitle() + "?");
                    break;
            }

            new AlertDialog.Builder(cont).
                    setTitle("请确定是否删除").
                    setIcon(android.R.drawable.ic_dialog_info).
                    setView(tv).
                    setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        //which为点击按钮的标识符，是一个 整形的数据，对于这三个按钮而言，每个按钮使用不同的int类型数据进行标识：
                        // Positive（-1）、Negative(-2)、 Neutral(-3)。
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //开始删除
                            int i;
                            PersonalInfo c = null;
                            for (i = 0; i < infolist.size(); i++) {
                                c = infolist.get(i);
                                if (c.getId() == curInfoId) {
                                    break;
                                }
                            }
                            //检查是否有对应的personnalInfo记录，如果有则不允许删除
                            /*int count = DataSupport.where("catalog_id = ?", String.valueOf(c.getCatalog_id())).count(PersonalInfo.class);
                            if (count > 0) {
                                Toast.makeText(cont, "您删除的文件夹还有账号记录，请先删除账号记录再删除文件夹！", Toast.LENGTH_LONG).show();
                                return;
                            }*/

                            //删除某个记录
                            int rowdel = DataSupport.delete(PersonalInfo.class, c.getId());
                            //从infolist里面remove
                            infolist.remove(i);
                            lva.delItem(i);
                            lva.notifyDataSetChanged();
                        }
                    }).
                    setNegativeButton("取消", null).
                    show();


        }

    }

}
