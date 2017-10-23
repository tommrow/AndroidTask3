package com.example.mh.experimentthree;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by mh on 2017/10/22.
 */

public class listviewAdapter extends BaseAdapter{
    private Context context;
    private List<Map<String,Object>> shoppingList;
    private AdapterView.OnItemClickListener mOnItemClickListener;//// TODO: 2017/10/22
    //shoppingList=new ArrayList<>();
    public listviewAdapter(Context context,List<Map<String,Object>>shoppingList){
        this.context=context;
        this.shoppingList=shoppingList;
    }
    @Override
    public int getCount(){
        if(shoppingList==null)
        {
            return 0;
        }
        return shoppingList.size();
    }
    @Override
    public Object getItem(int i){
        if(shoppingList==null)
        {
            return null;
        }
        return shoppingList.get(i);
    }
    @Override
    public long getItemId(int i){
        return i;
    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup){
        View convertView;
        ViewHolder viewHolder;
        if(view == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.shopping,null);
            viewHolder = new ViewHolder();
            viewHolder.shop_round=(TextView)convertView.findViewById(R.id.shop_round);
            viewHolder.shop_name=(TextView)convertView.findViewById(R.id.shop_name);
            viewHolder.shop_price=(TextView)convertView.findViewById(R.id.shop_price);
            convertView.setTag(viewHolder);
        }
        else {
            convertView=view;
            viewHolder=(ViewHolder)convertView.getTag();
        }
        return convertView;
    }
    private class ViewHolder{
        public TextView shop_round;
        public TextView shop_name;
        public TextView shop_price;
    }
}
