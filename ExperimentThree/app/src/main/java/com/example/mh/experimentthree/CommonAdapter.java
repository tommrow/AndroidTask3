package com.example.mh.experimentthree;

/**
 * Created by mh on 2017/10/19.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import java.util.List;
import java.util.Map;
/**
 * Created by mh on 2017/10/19.
 */

public  abstract class CommonAdapter extends RecyclerView.Adapter <ViewHolder>//泛型，
{
    private Context mContext;
    private int mLayoutId;
    private List<Map<String,Object>> mDatas;
    private OnItemClickListener mOnItemClickListener;


    public CommonAdapter(Context context,int layoutId,List datas){
        this.mContext=context;
        this.mLayoutId=layoutId;
        this.mDatas=datas;
    }
    public interface OnItemClickListener//接口对象
    {
        void onClick(int position);
        void onLongClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener)//实例化接口
    {
        this.mOnItemClickListener=onItemClickListener;
    }
    @Override
    public int getItemCount()
    {
        return mDatas.size();
    }
    @Override
    public ViewHolder onCreateViewHolder (final ViewGroup parent , int viewType)//创建视图，返回相应的Viewholder
    {
        ViewHolder viewHolder = ViewHolder.get(mContext,parent,mLayoutId);
        return viewHolder;
    }
    @Override
    public void onBindViewHolder(final ViewHolder holder,int position)//这里的类型要是RecyclerView.ViewHolder
    {
        convert(holder,mDatas.get(position));//绑定点击数据
        if(mOnItemClickListener!=null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onClick(holder.getAdapterPosition());
                }
            });

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mOnItemClickListener.onLongClick(holder.getAdapterPosition());
                    return false;
                }
            });
        }
    }
    protected abstract void convert(ViewHolder holder, Map<String, Object> stringObjectMap);//convert必须是一个抽象函数
}


