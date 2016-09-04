package com.kylinwind.pim;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;

import android.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
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

public class FragmentCatalog extends Fragment {
    private static final String TAG = "FragmentCatalog";
    private ListView listView = null;
    private List<Catalog> cataloglist = new ArrayList<Catalog>();  //声明一个list，动态存储要显示的信
    public Context cont;
    private ImageButton ibAdd;
    private ImageButton ibDel;
    private ImageView ibSetting;
    EditText etNewCatalog;
    ListViewAdapter lva;
    public String curCatalogName;

    FragmentInfoList fragmentInfoList = new FragmentInfoList();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate方法执行");

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        cont = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_catalog, container, false);
        listView = (ListView) v.findViewById(R.id.listView);    //将listView与布局对象关联
        ibAdd = (ImageButton) v.findViewById(R.id.ibAdd);
        ibDel = (ImageButton) v.findViewById(R.id.ibDel);
        ibSetting = (ImageButton) v.findViewById(R.id.ibSetting);

        //初始化数据库，创建数据库或获得一个数据库
        ((MyApplication)getActivity().getApplication()).getDb();
        //初始化用户和目录数据
        initData();

        //取出所有目录数据
        Catalog c = new Catalog();
        cataloglist = c.findAll(Catalog.class);

        lva = new ListViewAdapter(cont);
        lva.setMode(Attributes.Mode.Single);
        listView.setAdapter(lva);


        //处理Item的点击事件
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                // ((SwipeLayout) (listView.getChildAt(i - listView.getFirstVisiblePosition()))).open(true);
                Catalog c = cataloglist.get(i);   //通过position获取所点击的对象
                String infoTitle = c.getName();    //获取信息标题
                String infoDetails = c.getName();    //获取信息详情
                //把已经打开的swipelayout关闭
                ((ListViewAdapter) listView.getAdapter()).closeAllItems();
                //Toast显示测试
                CharSequence msg = "信息:" + infoTitle + " , " + infoDetails;
                Toast.makeText(cont, msg, Toast.LENGTH_SHORT).show();
                //显示详细信息
                FragmentManager fm;
                FragmentTransaction ft;
                fm = getActivity().getFragmentManager();
                FragmentInfoList fragment = new FragmentInfoList();
                fragment.setIcon(c.getIcon());
                ft = fm.beginTransaction();
                ft.replace(R.id.fragment_container, fragment);
                ft.addToBackStack(null);
                ft.commit();


            }
        });
/*        listView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //Log.e("ListView", "OnTouch");
                return false;
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(cont, "OnItemLongClickListener", Toast.LENGTH_SHORT).show();
                return true;
            }
        });*/
/*        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                //Log.e("ListView", "onScrollStateChanged");
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });*/

/*        listView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.e("ListView", "onItemSelected:" + position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.e("ListView", "onNothingSelected:");
            }
        });*/
        //长按菜单显示

        listView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            public void onCreateContextMenu(ContextMenu conMenu, View view, ContextMenu.ContextMenuInfo info) {
                conMenu.setHeaderTitle("操作:");
                conMenu.add(0, 0, 0, "修改名称");
                conMenu.add(0, 1, 1, "删除");
                conMenu.add(0, 2, 2, "新增");
            }
        });

        //为增加按钮添加监听器
        ibAdd.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        Log.d(TAG, "onClick方法执行");
                        addCatalog();
                    }
                }
        );
        ibSetting.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Context context = v.getContext();
                if (context instanceof MainActivity) {

                    FragmentTransaction t = getFragmentManager().beginTransaction();
                    PrefsFragment f = new PrefsFragment();
                    // Replace whatever is in the fragment_container view with this fragment,
                    // and add the transaction to the back stack
                    // transaction.replace(R.id.fragment_container, newFragment);
                    // transaction.addToBackStack(null);
                    // Commit the transaction  transaction.commit();
                    t.replace(R.id.fragment_container, f);
                    t.addToBackStack(null);
                    t.commit();
                }
            }
        });

        //设置焦点
        listView.setFocusable(true);
        listView.requestFocus();
        listView.setFocusableInTouchMode(true);
        return v;
    }

    //长按菜单处理函数
    public boolean onContextItemSelected(MenuItem aItem) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) aItem.getMenuInfo();
        switch (aItem.getItemId()) {
            case 0:
                Toast.makeText(cont, "你点击了条目一", Toast.LENGTH_SHORT).show();
                return true;
            case 1:
                //删除文件夹
                //listView.getSelectedItem()
                //Catalog delrec = Catalog.find(Catalog.class,aItem.getTitle());
                TextView titleview = (TextView) info.targetView.findViewById(R.id.title);
                //Toast.makeText(cont, "你点击了条目二"+titleview.getText().toString(), Toast.LENGTH_SHORT).show();
                this.curCatalogName = titleview.getText().toString();
                this.delCatalog();
                return true;
            case 2:
                //新增文件夹
                this.addCatalog();
                return true;
        }
        return false;
    }

    private class AddDialogListener implements DialogInterface.OnClickListener {
        //which为点击按钮的标识符，是一个 整形的数据，对于这三个按钮而言，每个按钮使用不同的int类型数据进行标识：
        // Positive（-1）、Negative(-2)、 Neutral(-3)。
        @Override
        public void onClick(DialogInterface dialog, int which) {
            //开始添加catalog
            addCatalog(etNewCatalog.getText().toString());

        }
    }

    public void addCatalog() {
        etNewCatalog = new EditText(cont);
        etNewCatalog.setSingleLine(true);
        new AlertDialog.Builder(cont).
                setTitle("请输入新文件名称").
                setIcon(android.R.drawable.ic_dialog_info).
                setView(etNewCatalog).
                setPositiveButton("确定", new AddDialogListener()).
                setNegativeButton("取消", null).
                show();
    }

    public Catalog addCatalog(String name) {
        if (name == null || name.equals("")) {
            Toast.makeText(cont, "请输入文件夹名称！", Toast.LENGTH_LONG).show();
            return null;
        }

        Catalog c = null;
        //检查名称是否有重复
        int i = 0;
        for (i = 0; i < cataloglist.size(); i++) {
            c = cataloglist.get(i);
            if (c.getName().equals(name)) {
                Toast.makeText(cont, "输入的名称重复!", Toast.LENGTH_LONG).show();
                return null;
            }
        }
        c = new Catalog();
        c.setOrd(0);
        c.setType("catalog");
        c.setIcon(c.getIcon());
        c.setCatalog_id(c.getMaxCatalogIdPlus1());
        c.setUp_catalog_id(0);
        c.setName(name);
        Log.d(TAG, c.toString());
        if (c.save()) {
            cataloglist.add(c);
            lva.addItem();
            this.lva.notifyDataSetChanged();
            CharSequence msg = "添加文件夹：" + name + "成功！";
            Toast.makeText(cont, msg, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(cont, "新增文件夹失败", Toast.LENGTH_LONG).show();
        }

        return c;
    }

    public void delCatalog() {
        //根据名称找到catalogid
        int i;
        Catalog c = null;
        for (i = 0; i < cataloglist.size(); i++) {
            c = cataloglist.get(i);
            if (c.getName().equals(curCatalogName)) {
                break;
            }
        }
        if (c != null) {
            if (c.getCatalog_id() <= 3) {
                Toast.makeText(cont, "您不能删除应用默认文件夹！", Toast.LENGTH_LONG).show();
                return;
            }

            TextView tv = new TextView(cont);
            tv.setText("是否确定删除文件夹:" + c.getName() + "?");

            new AlertDialog.Builder(cont).
                    setTitle("请确定是否删除").
                    setIcon(android.R.drawable.ic_dialog_info).
                    setView(tv).
                    setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        //which为点击按钮的标识符，是一个 整形的数据，对于这三个按钮而言，每个按钮使用不同的int类型数据进行标识：
                        // Positive（-1）、Negative(-2)、 Neutral(-3)。
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //开始删除catalog
                            int i;
                            Catalog c = null;
                            for (i = 0; i < cataloglist.size(); i++) {
                                c = cataloglist.get(i);
                                if (c.getName().equals(curCatalogName)) {
                                    break;
                                }
                            }
                            //检查是否有对应的personnalInfo记录，如果有则不允许删除
                            int count = DataSupport.where("catalog_id = ?", String.valueOf(c.getCatalog_id())).count(PersonalInfo.class);
                            if (count > 0) {
                                Toast.makeText(cont, "您删除的文件夹还有账号记录，请先删除账号记录再删除文件夹！", Toast.LENGTH_LONG).show();
                                return;
                            }

                            //删除某个文件夹
                            int rowdel = DataSupport.delete(Catalog.class, c.getId());
                            //从cataloglist里面remove
                            cataloglist.remove(i);
                            lva.delItem(i);
                            lva.notifyDataSetChanged();
                        }
                    }).
                    setNegativeButton("取消", null).
                    show();


        }

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
            for (int i = 0; i < cataloglist.size(); i++) {
                //Catalog getInfo = (Catalog) list.get(i);    //获取第i个对象
                //调用makeItemView，实例化一个Item
                //itemViews.add(makeItemView(getInfo.getName(), getInfo.getName(), getInfo.getIcon()));
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

       /* public ListViewAdapter(List<Catalog> list) {
            // TODO Auto-generated constructor stub
            for (int i = 0; i < list.size(); i++) {
                //Catalog getInfo = (Catalog) list.get(i);    //获取第i个对象
                //调用makeItemView，实例化一个Item
                //itemViews.add(makeItemView(getInfo.getName(), getInfo.getName(), getInfo.getIcon()));
                itemViews.add(null);
            }
        }*/

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

        //绘制Item的函数
       /* private View makeItemView(String strTitle, String strText, int resId) {
            LayoutInflater inflater = (LayoutInflater) cont
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // 使用View的对象itemView与R.layout.item关联
            View itemView = inflater.inflate(R.layout.listitem_catalog, null);
            // 通过findViewById()方法实例R.layout.item内各组件
            TextView title = (TextView) itemView.findViewById(R.id.title);
            title.setText(strTitle);    //填入相应的值
            TextView text = (TextView) itemView.findViewById(R.id.info);
            text.setText(strText);
            ImageView image = (ImageView) itemView.findViewById(R.id.catalog_img);
            image.setImageResource(resId);
            return itemView;
        }*/

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
            View v = inflater.inflate(R.layout.listitem_catalog, null);
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
                    curCatalogName = view.getTag().toString();
                    delCatalog();
                }
            });

            return v;
        }

        @Override
        public void fillValues(int i, View view) {
/*fill values to your item layout returned from `generateView`.
  The position param here is passed from the BaseAdapter's 'getView()*/
            String strTitle = cataloglist.get(i).getName();
            String strText = cataloglist.get(i).getName();
            int resId = cataloglist.get(i).getIcon();
            // 通过findViewById()方法实例R.layout.item内各组件
            TextView tvDelete = (TextView) view.findViewById(R.id.listitem_tvdelete);

            TextView title = (TextView) view.findViewById(R.id.title);
            title.setText(strTitle);    //填入相应的值
            TextView text = (TextView) view.findViewById(R.id.info);
            text.setText(strText);
            ImageView image = (ImageView) view.findViewById(R.id.catalog_img);
            image.setImageResource(resId);
            ViewHolder vh = new ViewHolder();
            vh.setIv(image);
            vh.setTvInfo(text);
            vh.setTvTitle(title);
            view.setTag(vh);
            //往删除tv上保存当前文件夹名称
            tvDelete.setTag(strTitle);
        }

/*        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder vh = null;
            if (convertView == null) {
                vh = new ViewHolder();
                View itemView = itemViews.get(position);
                // 通过findViewById()方法实例R.layout.item内各组件
                TextView title = (TextView) itemView.findViewById(R.id.title);
                vh.setTvTitle(title);
                TextView text = (TextView) itemView.findViewById(R.id.info);
                vh.setTvInfo(text);
                ImageView image = (ImageView) itemView.findViewById(R.id.catalog_img);
                vh.setIv(image);
                itemView.setTag(vh);
                return itemViews.get(position);
            } else {
                vh = (ViewHolder) convertView.getTag();
                vh.getIv().setImageResource(cataloglist.get(position).getIcon());
                vh.getTvTitle().setText(cataloglist.get(position).getName());
                vh.getTvInfo().setText(cataloglist.get(position).getName());
                return convertView;
            }
        }*/

    }

    //如果是第一次运行，那么初始化数据
    public void initData() {
        //看看是否已经有了系统用户数据，如果没有，那么就增加一个
        int i = SysUser.count(SysUser.class);
        Log.d(TAG, "SysUser.count(SysUser.class)执行 i = " + i);
        if (i == 0) {
            SysUser su = new SysUser();
            su.setUser_name(getResources().getString(R.string.temp_user));
            su.setPassword("");
            if (su.save()) {
                Toast.makeText(cont, "初始化用户数据成功", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(cont, "初始化用户数据失败", Toast.LENGTH_LONG).show();
            }
        }
        //看看是否有三个默认目录：银行账号，网站账号，其他，如果没有就加上
        Catalog c1 = new Catalog();
        Catalog c2 = new Catalog();
        Catalog c3 = new Catalog();
        c1.setCatalog_id(1);
        c1.setUp_catalog_id(0);
        c1.setIcon(R.mipmap.bank);
        c1.setName("银行账号");
        c1.setOrd(0);
        c1.setType("bank");

        c2.setCatalog_id(2);
        c2.setUp_catalog_id(0);
        c2.setIcon(R.mipmap.website);
        c2.setName("网站账号");
        c2.setOrd(1);
        c2.setType("website");


        c3.setCatalog_id(3);
        c3.setUp_catalog_id(0);
        c3.setIcon(R.mipmap.catalog);
        c3.setName("其他");
        c3.setOrd(2);
        c3.setType("others");
        i = Catalog.count(Catalog.class);
        Log.d(TAG, "Catalog.count(Catalog.class)执行 i = " + i);
        if (i == 0) {
            if (c1.save() && c2.save() && c3.save()) {
                Toast.makeText(cont, "初始化目录数据成功", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(cont, "初始化目录数据失败", Toast.LENGTH_LONG).show();
            }
        }
    }

}
