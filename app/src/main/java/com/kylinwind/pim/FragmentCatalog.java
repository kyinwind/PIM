package com.kylinwind.pim;

import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.kylinwind.pim.model.Catalog;
import com.kylinwind.pim.model.SysUser;

import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FragmentCatalog extends Fragment {
    private static final String TAG = "FragmentCatalog";
    private SQLiteDatabase db;
    private ListView listView = null;
    private List<Catalog> cataloglist = new ArrayList<Catalog>();  //声明一个list，动态存储要显示的信
    public Context cont;
    private ImageButton ibAdd;
    private ImageButton ibDelete;

    EditText etNewCatalog;
    ListViewAdapter lva;
    String curCatalogName;

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
        ibDelete = (ImageButton) v.findViewById(R.id.ibDelete);
        //初始化数据库，创建数据库或获得一个数据库
        db = Connector.getDatabase();
        Log.d(TAG, "Connector.getDatabase执行");
        //初始化用户和目录数据
        initData();

        //取出所有目录数据
        Catalog c = new Catalog();
        cataloglist = c.findAll(Catalog.class);

        lva = new ListViewAdapter(cataloglist);
        listView.setAdapter(lva);


        //处理Item的点击事件
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Catalog c = cataloglist.get(i);   //通过position获取所点击的对象
                String infoTitle = c.getName();    //获取信息标题
                String infoDetails = c.getName();    //获取信息详情
                //Toast显示测试
                CharSequence msg = "信息:" + infoTitle + " , " + infoDetails;

                Toast.makeText(cont, msg, Toast.LENGTH_SHORT).show();
            }
        });

        //长按菜单显示

        listView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            public void onCreateContextMenu(ContextMenu conMenu, View view, ContextMenu.ContextMenuInfo info) {
                conMenu.setHeaderTitle("操作");
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

                this.delCatalog(titleview.getText().toString());
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
        new AlertDialog.Builder(cont).
                setTitle("请输入").
                setIcon(android.R.drawable.ic_dialog_info).
                setView(etNewCatalog).
                setPositiveButton("确定", new AddDialogListener()).
                setNegativeButton("取消", null).
                show();
    }

    public Catalog addCatalog(String name) {
        Catalog c = new Catalog();
        c.setOrd(0);
        c.setType("catalog");
        c.setIcon(c.getIcon());
        c.setCatalog_id(c.getMaxCatalogId());
        c.setUp_catalog_id(0);
        c.setName(name);
        Log.d(TAG, c.toString());
        if (c.save()) {
            this.cataloglist.add(c);
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
        Catalog c=null;
        for (i = 0; i < cataloglist.size(); i++) {
            c = cataloglist.get(i);
            if (c.getName().equals(curCatalogName)) {
                break;
            }
        }
        if (c != null) {
            this.deleteCatalogById(c.getCatalog_id());
        }

    }

    public void delCatalog(String name) {
        TextView tv = new TextView(cont);
        tv.setText("是否确定删除文件夹:" + name + "?");
        curCatalogName = name;
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
                        delCatalog();
                    }
                }).
                setNegativeButton("取消", null).
                show();
    }

    public void deleteCatalogById(int catalogid) {
        //删除某个文件夹
        DataSupport.delete(Catalog.class, catalogid);
        //从cataloglist里面remove
        int i;
        for (i = 0; i < cataloglist.size(); i++) {
            Catalog c = cataloglist.get(i);
            if (c.getCatalog_id() == catalogid) {
                break;
            }
        }
        cataloglist.remove(i);
        lva.notifyDataSetChanged();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate方法执行");

    }


    public class ListViewAdapter extends BaseAdapter {
        View[] itemViews;

        public ListViewAdapter(List<Catalog> list) {
            // TODO Auto-generated constructor stub
            itemViews = new View[list.size()];
            for (int i = 0; i < list.size(); i++) {
                Catalog getInfo = (Catalog) list.get(i);    //获取第i个对象
                //调用makeItemView，实例化一个Item
                itemViews[i] = makeItemView(
                        getInfo.getName(), getInfo.getName(), R.mipmap.catalog
                );
            }
        }

        public int getCount() {
            return itemViews.length;
        }

        public View getItem(int position) {
            return itemViews[position];
        }

        public long getItemId(int position) {
            return position;
        }

        //绘制Item的函数
        private View makeItemView(String strTitle, String strText, int resId) {
            LayoutInflater inflater = (LayoutInflater) cont
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            // 使用View的对象itemView与R.layout.item关联
            View itemView = inflater.inflate(R.layout.listitem, null);

            // 通过findViewById()方法实例R.layout.item内各组件
            TextView title = (TextView) itemView.findViewById(R.id.title);
            title.setText(strTitle);    //填入相应的值
            TextView text = (TextView) itemView.findViewById(R.id.info);
            text.setText(strText);
            ImageView image = (ImageView) itemView.findViewById(R.id.catalog_img);
            image.setImageResource(resId);

            return itemView;
        }


        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null)
                return itemViews[position];
            return convertView;
        }

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
