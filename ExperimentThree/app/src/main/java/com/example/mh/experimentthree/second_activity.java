package com.example.mh.experimentthree;

import android.content.Intent;
import android.content.res.TypedArray;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.example.mh.experimentthree.R.layout.goods;

public class second_activity extends AppCompatActivity {
    private boolean is_add = false;
    private String[] goods= {"Enchated Forest","Arla Milk","Devondale Milk","Kindle Oasis",
            "waitrose 早餐麦片","Mcvitie's 饼干","Ferrero Rocher","Maltesers","Lindt","Borggreve"};
    private String[] firsts={"E","A","D","K","W","M","F","M","L","B"};
    private String[] prices={"¥ 5.00","¥ 59.00","¥ 79.00","¥ 2399.00","¥ 179.00",
            "¥ 14.90","¥ 132.59","¥ 141.43","¥ 139.43","¥ 28.90"};
    private String[] extras={"作者    Johanna Basf    ord","产地    德国","产地    澳大利亚","版本    8GB","重量    2Kg" ,
            "产地    英国","重量    300g","重量    118g","重量    249g","重量    640g"};
    private int position;
    private String pos_str;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_activity);
        Intent intent =getIntent();
        pos_str=intent.getStringExtra("add");
        for(int i=0;i<goods.length;i++)
        {
            if(pos_str.equals(goods[i]))
            {
                position=i;
                break;
            }
            else position=0;
        }
        String data=""+position;
        is_add=false;

        Log.d("SecondActivity",data);
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
        int imageId;
        TypedArray ar = getResources().obtainTypedArray(R.array.actions_images);
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
              is_add=true; //这里需要注意是不是写成功
                shop_car.setTag(1);
                Toast.makeText(second_activity.this,"商品已添加到购物车",Toast.LENGTH_SHORT).show();//这里没法正常  还有一点就是 这个位置
                //为神马不能直接getContext这个吗？嗯嗯  内部类 还是去看java吧   嗯嗯 谢谢
                //// TODO: 2017/10/22 内部类
            }
        });
        //返回点击事件
        ImageView back_png = (ImageView)findViewById(R.id.back_png);
        back_png.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                Bundle bundle = new Bundle();
                bundle.putString("position",pos_str);
                if((int)shop_car.getTag()==1)
                bundle.putBoolean("is_add",true);
                else bundle.putBoolean("is_add",false);
                Intent intent=new Intent();
                intent.putExtras(bundle);
                setResult(RESULT_OK,intent);
                finish();
                //setContentView(layout_main);//切换到主界面
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
