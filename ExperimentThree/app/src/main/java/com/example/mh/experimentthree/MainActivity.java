package com.example.mh.experimentthree;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.media.Image;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.StringTokenizer;

import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.OvershootInLeftAnimator;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private CommonAdapter commonAdapter;
    private List<Map<String,Object>> listItems;
    private List<Map<String,Object>> shoppingList;
    private SimpleAdapter simpleAdapter;
    private String[] goods= {"Enchated Forest","Arla Milk","Devondale Milk","Kindle Oasis",
            "waitrose 早餐麦片","Mcvitie's 饼干","Ferrero Rocher","Maltesers","Lindt","Borggreve"};
    private String[] firsts={"E","A","D","K","W","M","F","M","L","B"};
    private String[] prices={"¥ 5.00","¥ 59.00","¥ 79.00","¥ 2399.00","¥ 179.00",
            "¥ 14.90","¥ 132.59","¥ 141.43","¥ 139.43","¥ 28.90"};
    private String[] extras={"作者    Johanna Basford","产地    德国","产地    澳大利亚","版本    8GB","重量    2Kg" ,
            "产地    英国","重量    300g","重量    118g","重量    249g","重量    640g"};
    private boolean is_shoping_car;
    private ListView listView;
    private int p;
    private List<String> record_shopping;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //************初始化*************//
        listItems=new ArrayList<>();
        is_shoping_car=true;
        for(int i=0;i<10;i++){
            Map<String,Object> temp = new LinkedHashMap<>();
            temp.put("name",goods[i]);
            temp.put("firstLetter",firsts[i]);
            listItems.add(temp);
        }


        //***********RecyclerView****************//
        mRecyclerView=(RecyclerView)findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setHasFixedSize(true);

        //**************************商品列表适配器************//
        commonAdapter = new CommonAdapter  (this, R.layout.goods_linearlayout_item, listItems)
        {
            @Override
            public void convert(ViewHolder holder, Map<String, Object> s) {
                TextView name = holder.getView(R.id.name);//两种id
                name.setText(s.get("name").toString());
                TextView first = holder.getView(R.id.first);
                first.setText(s.get("firstLetter").toString());
            }
        };
        //*******************实现接口函数**********//
        commonAdapter.setOnItemClickListener(new CommonAdapter.OnItemClickListener(){
            @Override
            public void onClick(final int position) {

                //********切换到商品详情界面**********//
                //setContentView(layout_second);//gao ding 666 但是为什么啊//没有setContent的时候
                //find函数是无效的 这个R.id是在哪里找view 不是整个肯定是在contentView里面找啊  这个R表示的是什么啊
                //Resource你懂就好 嗯嗯 好的 谢谢大神啊
                //视图之间的切换 最好还是有使用不同的activity  只有在setcontentView的时候才会分配内存 之前定义的东西还要重新定义 太过麻烦
                //// TODO: 2017/10/22 使用activity
                    Intent intent= new Intent(MainActivity.this,second_activity.class);
                    intent.putExtra("add",goods[position]);
                    startActivityForResult(intent,1);
            }

            @Override
            public void onLongClick(final int position) {
                final AlertDialog.Builder alterDialog = new AlertDialog.Builder(MainActivity.this);
                alterDialog.setTitle("移除商品");
                alterDialog.setMessage("从商品列表中移除"+goods[position]);
                alterDialog.setNegativeButton("取消",null);
                   alterDialog.setPositiveButton("确定",new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog,int which)
                    {
                            listItems.remove(position);
                            for(int i=position;i<9;i++)
                            {
                                goods[i]=goods[i+1];
                                firsts[i]=firsts[i+1];
                                prices[i]=prices[i+1];
                                extras[i]=extras[i+1];
                            }
                            //commonAdapter.notifyDataSetChanged();
                            commonAdapter.notifyItemRemoved(position);
                    }
                }).create().show();
            }
        });
        //mRecyclerView.setAdapter(commonAdapter);
        //****************动画效果
        ScaleInAnimationAdapter animationAdapter=new ScaleInAnimationAdapter(commonAdapter);
        animationAdapter.setDuration(1000);
        mRecyclerView.setAdapter(animationAdapter);
        mRecyclerView.setItemAnimator(new OvershootInLeftAnimator());



        //************ListView*****************//
        listView=(ListView)findViewById(R.id.shoppinglist);
        shoppingList=new ArrayList<>();
        record_shopping=new ArrayList<>();
        Map<String,Object> temp1 = new LinkedHashMap<>();
        temp1.put("letter","*");
        temp1.put("name","商品");
        temp1.put("price","价格");
        shoppingList.add(temp1);
        simpleAdapter= new SimpleAdapter(this,shoppingList,R.layout.shopping,
                new String[]{"letter","name","price"},new int []{R.id.shop_round,R.id.shop_name,R.id.shop_price});
        listView.setAdapter(simpleAdapter);
        //***************listView点击事件**********//
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position!=0){
                    Intent intent= new Intent(MainActivity.this,second_activity.class);
                    Iterator<Map.Entry<String,Object>>entries =shoppingList.get(position).entrySet().iterator();
                    while(entries.hasNext()){
                        Map.Entry<String,Object> entry=entries.next();
                        if(entry.getKey()=="name")
                        {
                            intent.putExtra("add",entry.getValue().toString());
                            startActivityForResult(intent,1);
                            break;
                        }
                    }
                }
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick(AdapterView<?>parent, View view ,final int pos, long id)
            {
                if(pos!=0){
                    final AlertDialog.Builder alterDialog = new AlertDialog.Builder(MainActivity.this);
                    alterDialog.setTitle("移除商品");
                    alterDialog.setMessage("从购物车移除"+goods[pos]);
                    alterDialog.setNegativeButton("取消",null);
                    alterDialog.setPositiveButton("确定",new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialog,int which)
                        {
                            shoppingList.remove(pos);
                            simpleAdapter.notifyDataSetChanged();
                            //simpleAdapter.notifyItemRemoved(pos);
                        }
                    }).create().show();
                }
                return true;
            }
        });
        //*************可见不可见***********//
        listView.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
        //****************shopping car*********//
        final ImageView fab=(ImageView)findViewById(R.id.fab);
        is_shoping_car=false;
        fab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if(is_shoping_car==false){
                    fab.setImageResource(R.drawable.shoplist);
                    is_shoping_car=true;
                    listView.setVisibility(View.INVISIBLE);
                    mRecyclerView.setVisibility(View.VISIBLE);
                }else if(is_shoping_car==true){
                    fab.setImageResource(R.drawable.mainpage);
                    is_shoping_car=false;
                    listView.setVisibility(View.VISIBLE);
                    mRecyclerView.setVisibility(View.INVISIBLE);
                }
            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent indent1)
    {
        switch (requestCode){
            case 1:
                if(resultCode==RESULT_OK){
                    Bundle bundle=indent1.getExtras();
                    String tmp=bundle.getString("position");
                    p=find_index(tmp);
                    boolean is_add=bundle.getBoolean("is_add");
                    //不加判断
                    Log.d("test1",p+"");

                    if(is_add)
                    {

                        Map<String,Object> temp1 = new LinkedHashMap<>();
                        temp1.put("letter",firsts[p]);
                        temp1.put("name",goods[p]);
                        temp1.put("price",prices[p]);
                        shoppingList.add(temp1);

                        //// TODO: 2017/10/22 list实现
                        record_shopping.add(goods[p]);
                        simpleAdapter.notifyDataSetChanged();
                    }
                }
            default:
        }
    }
    private int find_index(String s)
    {
        for(int i=0;i<goods.length;i++)
        {
            if(s.equals(goods[i]))
            {
                return i;
            }
        }
        return 0;
    }

}