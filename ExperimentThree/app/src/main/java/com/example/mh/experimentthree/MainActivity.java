package com.example.mh.experimentthree;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.usage.UsageEvents;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.EventLog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.StringTokenizer;

import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.SlideInRightAnimationAdapter;
import jp.wasabeef.recyclerview.animators.FadeInAnimator;
import jp.wasabeef.recyclerview.animators.FadeInLeftAnimator;
import jp.wasabeef.recyclerview.animators.FadeInRightAnimator;
import jp.wasabeef.recyclerview.animators.OvershootInLeftAnimator;
import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private CommonAdapter commonAdapter;
    private List<Map<String,Object>> listItems;
    private List<Map<String,Object>> shoppingList;
    private SimpleAdapter simpleAdapter;
    private String []goods;
    private String []firsts;
    private String []prices;
    private String []extras;
    private boolean is_shoping_car;
    private ListView listView;
    private int p;
    private List<String> record_shopping;
    private final String static_action="com.example.broadcasttest.MY_BROADCAST";

    private static final String TAG="MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        //注册订阅者
        EventBus.getDefault().register(this);
        //************初始化*************//
        goods=getResources().getStringArray(R.array.goods);
        firsts=getResources().getStringArray(R.array.firsts);
        prices=getResources().getStringArray(R.array.prices);
        extras=getResources().getStringArray(R.array.extras);
        //日志调试信息
        Log.d(TAG, "onCreate: ");

        listItems=new ArrayList<>();
        is_shoping_car=true;
        for(int i=0;i<10;i++){
            Map<String,Object> temp = new LinkedHashMap<>();
            temp.put("name",goods[i]);
            temp.put("firstLetter",firsts[i]);
            listItems.add(temp);
        }


        //开启软件 发送广播
        Bundle bundleBroadcast = new Bundle();
        int n=10;
        Random random =new Random();
        int pos=random.nextInt(n);
        bundleBroadcast.putInt("pos",pos);
        TypedArray ar = getResources().obtainTypedArray(R.array.actions_images);
        int imageId=ar.getResourceId(pos,1);
        bundleBroadcast.putInt("imageId",imageId);
        bundleBroadcast.putString("name",goods[pos]);
        bundleBroadcast.putString("price",prices[pos]);
        Intent intentBroadcast = new Intent(static_action);
        intentBroadcast.putExtras(bundleBroadcast);
        sendBroadcast(intentBroadcast);

        //********************  ***RecyclerView****************//
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
                //setContentView(layout_second); 视图之间的切换 最好还是有使用不同的activity  只有在setcontentView的时候才会分配内存 之前定义的东西还要重新定义 太过麻烦
                //TODO: 2017/10/22 使用activity
                    Intent intent= new Intent(MainActivity.this,second_activity.class);
                    //intent.putExtra("add",goods[position]);
                    intent.putExtra("add",listItems.get(position).get("name").toString());//传递数据为商品名称
                    startActivityForResult(intent,1);
            }

            @Override
            public void onLongClick(final int position) {
                String stemp=position+"";
                Toast.makeText(MainActivity.this, "移除第"+stemp+"个商品", Toast.LENGTH_SHORT).show();
                listItems.remove(position);
                //Todo 这里最好不要操作string数组  直接操作listview即可
                //commonAdapter.notifyDataSetChanged();
                commonAdapter.notifyItemRemoved(position);
            }
        });
        //mRecyclerView.setAdapter(commonAdapter);
        //****************动画效果*************//
        //ScaleInAnimationAdapter animationAdapter=new ScaleInAnimationAdapter(commonAdapter);
        AlphaInAnimationAdapter alphaInAnimationAdapter = new AlphaInAnimationAdapter(commonAdapter);
        //animationAdapter.setDuration(1000);
        alphaInAnimationAdapter.setDuration(2000);
        //mRecyclerView.setAdapter(animationAdapter);
        //slideInRightAnimationAdapter.setInterpolator(new OvershootInterpolator());
        //slideInRightAnimationAdapter.setDuration(2000);
        mRecyclerView.setAdapter(alphaInAnimationAdapter);

        //mRecyclerView.setItemAnimator(new OvershootInLeftAnimator());
        mRecyclerView.setItemAnimator(new FadeInRightAnimator());



        //************ListView*****************//
        listView=(ListView)findViewById(R.id.shoppinglist);//引入ListView控件
        //初始化shoppinglist
        shoppingList=new ArrayList<>();
        record_shopping=new ArrayList<>();
        //添加第一栏信息
        Map<String,Object> temp1 = new LinkedHashMap<>();
        temp1.put("letter","*");
        temp1.put("name","商品");
        temp1.put("price","价格");
        shoppingList.add(temp1);
        //初始化适配器 将shoppinglist作为数据
        simpleAdapter= new SimpleAdapter(this,shoppingList,R.layout.shopping,
                new String[]{"letter","name","price"},new int []{R.id.shop_round,R.id.shop_name,R.id.shop_price});
        listView.setAdapter(simpleAdapter);//设置适配器

        //***************listView点击事件**********//
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position!=0){
                    Intent intent= new Intent(MainActivity.this,second_activity.class);
                    intent.putExtra("add",shoppingList.get(position).get("name").toString());
                    startActivity(intent);
                    //startActivityForResult(intent,1);
                }
            }
        });

        //**************ListView长按事件****************//
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick(AdapterView<?>parent, View view ,final int pos, long id)
            {
                if(pos!=0){
                    final AlertDialog.Builder alterDialog = new AlertDialog.Builder(MainActivity.this);
                    alterDialog.setTitle("移除商品");

                    alterDialog.setMessage("从购物车移除"+shoppingList.get(pos).get("name")+"?");
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

        //****************shopping car图片切换*********//
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
                    //String tmp=bundle.getString("position");
                    //p=find_index(tmp);
                    p = bundle.getInt("position");
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
                        record_shopping.add(goods[p]);
                        simpleAdapter.notifyDataSetChanged();
                    }
                }
            default:
        }
    }
    //订阅者事件
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void  onMessageEvent(Goods c)
    {
        Map<String,Object> listItem = new LinkedHashMap<>();
        listItem.put("letter",c.getLetter());
        listItem.put("name",c.getName());
        listItem.put("price",c.getPrice());
        shoppingList.add(listItem);
        simpleAdapter.notifyDataSetChanged();
        is_shoping_car=false;
        listView.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.INVISIBLE);
    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}