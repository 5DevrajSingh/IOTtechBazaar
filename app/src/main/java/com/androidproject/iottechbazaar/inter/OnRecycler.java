package com.androidproject.iottechbazaar.inter;

import android.widget.ImageView;

public interface OnRecycler {

    public void onClick(int pos);
    public void onLongClick(int pos);
    public void onExtraClick(int pos, ImageView imageView);

}
