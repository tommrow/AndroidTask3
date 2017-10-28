package com.example.mh.experimentthree;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import java.util.Random;

/**
 * Created by mh on 2017/10/26.
 */

public class Receiver extends BroadcastReceiver {
    private String dynamic_action="com.example.broadcasttest.DYNAMICACTION";

    private String static_action="com.example.broadcasttest.MY_BROADCAST";
    @Override
    public void onReceive(Context context, Intent intent)
    {

        if(intent.getAction().equals(static_action))
        {

            Bundle bundle=intent.getExtras();
//
            int pos = bundle.getInt("pos");
            int imageId=bundle.getInt("imageId");
            String tempName=bundle.getString("name");
            String tempPrice=bundle.getString("price");


            //bitmap编码格式
            Bitmap bm = BitmapFactory.decodeResource(context.getResources(),imageId);
            //// TODO: 2017/10/26 修改图片
            //获取通知栏管理
            NotificationManager manger =(NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            //实例化通知栏构造器
            Notification.Builder builder = new Notification.Builder(context);
            builder.setContentTitle("新商品热卖")
                    .setContentText(tempName+"仅售"+tempPrice+"!")
                    .setTicker("您有一条新消息")//通知首次出出现在通知栏 上升动画
                    .setLargeIcon(bm)//设置大icon
                    .setSmallIcon(imageId)
                    .setAutoCancel(true);//设置单机面板取消
            Intent mintent = new  Intent (context,second_activity.class);
            mintent.putExtra("add",tempName);
            PendingIntent mpendingIntent = PendingIntent.getActivity(context,0,mintent,PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(mpendingIntent);
            Notification notify = builder.getNotification();
            //// TODO: 2017/10/26 maybe
            manger.notify(0,notify);

        }
        else if(intent.getAction().equals(dynamic_action))
        {
            Bundle bundle=intent.getExtras();
            int imageId=bundle.getInt("imageId");
            String tempName=bundle.getString("name");
            //bitmap编码格式
            Bitmap bm = BitmapFactory.decodeResource(context.getResources(),imageId);
            //// TODO: 2017/10/26 修改图片
            //获取通知栏管理
            NotificationManager manger =(NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            //实例化通知栏构造器
            Notification.Builder builder = new Notification.Builder(context);
            builder.setContentTitle("马上下单")
                    .setContentText(tempName+"已添加到购物车")
                    .setTicker("您有一条新消息")//通知首次出出现在通知栏 上升动画
                    .setLargeIcon(bm)//设置大icon
                    .setSmallIcon(imageId)
                    .setAutoCancel(true);//设置单机面板取消

            Intent mintent = new  Intent (context,MainActivity.class);
            //todo event bus
            mintent.putExtra("add",tempName);
            PendingIntent mpendingIntent = PendingIntent.getActivity(context,0,mintent,PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(mpendingIntent);
            Notification notify = builder.getNotification();
            manger.notify(0,notify);

        }
    }

}