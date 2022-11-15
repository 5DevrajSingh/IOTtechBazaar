package com.androidproject.iottechbazaar.inter;

public interface OnUpdateStock {
    public void onClick(int pos);
    public void onLongClick(int pos);
    public void onDataPass(String value,String MRP,String discount,int pos,String inc_dec);
    public void onDelete(int pos);
}
