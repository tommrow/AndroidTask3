package com.example.mh.experimentthree;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static com.example.mh.experimentthree.R.layout.goods;

public class second_activity extends AppCompatActivity {
    private boolean is_re = false;
    private String []goods;
    private String []prices;
    private String []extras;
    private String []letters;
    private String dynamic_action="com.example.broadcasttest.DYNAMICACTION";
    private int position;
    private String pos_str;
    private Map<String,Object> name_id;
    private Receiver receiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_activity);
        Intent intent =getIntent();
        pos_str=intent.getStringExtra("add");

        goods = getResources().getStringArray(R.array.goods);
        prices =getResources().getStringArray(R.array.prices);
        extras =getResources().getStringArray(R.array.extras);
        letters=getResources().getStringArray(R.array.firsts);
        // TODO: 2017/10/24 内部类
        name_id = new LinkedHashMap<>();
        for(int i=0;i<10;i++)
        {
            name_id.put(goods[i],i);
        }
        is_re=false;
        //学会使用日志文件 来调试
        //Log.i("SecondActivity::",data);

        //listview布局
        ListView listview=(ListView)findViewById(R.id.layout_buttom);
        List<Map<String,Object>> more_goods=new ArrayList<>();
        String []more_goods_info={"一键下单","分享商品","不感兴趣","查看更多商品促销信息"};
        for(int i=0;i<4;i++)
        {
            Map<String,Object> temp1 = new LinkedHashMap<>();
            temp1.put("name",more_goods_info[i]);
            more_goods.add(temp1);
        }

        SimpleAdapter simpleAdapter= new SimpleAdapter(this,more_goods,R.layout.more_goods_info,
                new String[]{"name"},new int []{R.id.more_goods_layout});
        listview.setAdapter(simpleAdapter);

        //设置数据
        ImageView goods_png=(ImageView)findViewById(R.id.goods_png);
        TextView goods_name=(TextView)findViewById(R.id.goods_name);
        TextView goods_price=(TextView)findViewById(R.id.goods_price);
        TextView goods_extra=(TextView)findViewById(R.id.goods_extra);
        final int imageId;
        TypedArray ar = getResources().obtainTypedArray(R.array.actions_images);
        position = (int)name_id.get(pos_str);
        imageId=ar.getResourceId(position,1);
        ar.recycle();
        goods_png.setImageResource(imageId);//设置图片
        goods_name.setText(goods[position]);
        goods_price.setText(prices[position]);
        goods_extra.setText(extras[position]);

        //购物车点击事件
        final ImageView shop_car=(ImageView)findViewById(R.id.shop_car);
        shop_car.setTag(0);
        shop_car.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                shop_car.setTag(1);
                Toast.makeText(second_activity.this,"商品已添加到购物车",Toast.LENGTH_SHORT).show();
                //购物车点击发送广播
                IntentFilter dynamic_filter = new IntentFilter();
                dynamic_filter.addAction(dynamic_action);
                receiver = new Receiver();
                registerReceiver(receiver,dynamic_filter);
                is_re=true;
                Bundle bundleShopcar = new Bundle();
                int pos = position;
                bundleShopcar.putInt("imageId",imageId);
                bundleShopcar.putString("name",goods[pos]);
                bundleShopcar.putString("price",prices[pos]);
                Intent intentBroadcast = new Intent(dynamic_action);
                intentBroadcast.putExtras(bundleShopcar);
                sendBroadcast(intentBroadcast);
                EventBus.getDefault().post(new Goods(goods[pos],letters[pos],prices[pos],"l",imageId));
                //// TODO: 2017/10/22 内部类
            }
        });
        //返回点击事件
        ImageView back_png = (ImageView)findViewById(R.id.back_png);
        back_png.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                finish();
            }
        });
        final ImageView goods_star=(ImageView)findViewById(R.id.goods_star);
        goods_star.setTag(0);
        goods_star.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if((int)goods_star.getTag()==0){
                    goods_star.setImageResource(R.drawable.fullstar);
                    goods_star.setTag(1);
                }
                else {
                    goods_star.setImageResource(R.drawable.emptystar);
                    goods_star.setTag(0);
                }
            }
        });

    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        if(is_re)
        {
            unregisterReceiver(receiver);
            is_re=false;
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
