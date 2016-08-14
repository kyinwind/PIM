package com.kylinwind.pim;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View.OnClickListener;

import com.kylinwind.pim.model.Catalog;
import com.kylinwind.pim.model.SysUser;

import org.litepal.tablemanager.Connector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private SQLiteDatabase db;
    private ListView listView = null;
    private List<Catalog> mlistInfo = new ArrayList<Catalog>();  //声明一个list，动态存储要显示的信

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate方法执行");
        listView = (ListView) this.findViewById(R.id.listView);    //将listView与布局对象关联

        //初始化数据库，创建数据库或获得一个数据库
        db = Connector.getDatabase();
        Log.d(TAG, "Connector.getDatabase执行");
        //初始化用户和目录数据
        this.initData();

        //取出所有目录数据
        Catalog c = new Catalog();
        mlistInfo = c.findAll(Catalog.class);

        listView.setAdapter(new ListViewAdapter(mlistInfo));


        //处理Item的点击事件
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Catalog c = mlistInfo.get(i);   //通过position获取所点击的对象
                String infoTitle = c.getName();    //获取信息标题
                String infoDetails = c.getName();    //获取信息详情
                //Toast显示测试
                CharSequence msg = "信息:"+infoTitle+" , "+infoDetails;

                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });

        //长按菜单显示
        listView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            public void onCreateContextMenu(ContextMenu conMenu, View view, ContextMenu.ContextMenuInfo info) {
                conMenu.setHeaderTitle("菜单");
                conMenu.add(0, 0, 0, "条目一");
                conMenu.add(0, 1, 1, "条目二");
                conMenu.add(0, 2, 2, "条目三");
            }
        });
    }

    //长按菜单处理函数
    public boolean onContextItemSelected(MenuItem aItem) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) aItem.getMenuInfo();
        switch (aItem.getItemId()) {
            case 0:
                Toast.makeText(this, "你点击了条目一", Toast.LENGTH_SHORT).show();
                return true;
            case 1:
                Toast.makeText(this, "你点击了条目二", Toast.LENGTH_SHORT).show();

                return true;
            case 2:
                Toast.makeText(this, "你点击了条目三", Toast.LENGTH_SHORT).show();
                return true;
        }
        return false;
    }

    public class ListViewAdapter extends BaseAdapter {
        View[] itemViews;

        public ListViewAdapter(List<Catalog> mlistInfo) {
            // TODO Auto-generated constructor stub
            itemViews = new View[mlistInfo.size()];
            for (int i = 0; i < mlistInfo.size(); i++) {
                Catalog getInfo = (Catalog) mlistInfo.get(i);    //获取第i个对象
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
            LayoutInflater inflater = (LayoutInflater) MainActivity.this
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
                Toast.makeText(this, "初始化用户数据成功", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "初始化用户数据失败", Toast.LENGTH_LONG).show();
            }
        }
        //看看是否有三个默认目录：银行账号，网站账号，其他，如果没有就加上
        Catalog c1 = new Catalog();
        Catalog c2 = new Catalog();
        Catalog c3 = new Catalog();
        c1.setCatalog_id(1);
        c1.setUp_catalog_id(0);
        c1.setIcon("");
        c1.setName("银行账号");
        c1.setOrd(0);
        c1.setType("bank");

        c2.setCatalog_id(2);
        c2.setUp_catalog_id(0);
        c2.setIcon("");
        c2.setName("网站账号");
        c2.setOrd(1);
        c2.setType("website");


        c3.setCatalog_id(3);
        c3.setUp_catalog_id(0);
        c3.setIcon("");
        c3.setName("其他");
        c3.setOrd(2);
        c3.setType("others");
        i = Catalog.count(Catalog.class);
        Log.d(TAG, "Catalog.count(Catalog.class)执行 i = " + i);
        if (i == 0) {
            if (c1.save() && c2.save() && c3.save()) {
                Toast.makeText(this, "初始化目录数据成功", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "初始化目录数据失败", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart方法执行");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause方法执行");
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        Log.d(TAG, "onResume方法执行");
    }


    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop方法执行");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.d(TAG, "onDestroy方法执行");
        db.close();
        Log.d(TAG, "db.close执行");
    }

}
