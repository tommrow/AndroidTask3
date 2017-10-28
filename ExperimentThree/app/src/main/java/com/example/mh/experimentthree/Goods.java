package com.example.mh.experimentthree;

/**
 * Created by mh on 2017/10/26.
 */

public class Goods {
    private String name ;
    private String letter;
    private String price;
    private String extras;
    private int imageId;
    public Goods(String n,String l,String p,String e,int i)
    {
        name=n;
        letter=l;
        price=p;
        extras=e;
        imageId=i;
    }
    public String getName(){
        return name;
    }
    public String getLetter(){
        return letter;
    }
    public String getPrice(){
        return price;
    }

}
