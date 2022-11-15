package com.androidproject.iottechbazaar.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.androidproject.iottechbazaar.R;
import com.androidproject.iottechbazaar.inter.OnRecycler;
import com.androidproject.iottechbazaar.model.CategoryModel;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

public class CatagoryAdapter extends RecyclerView.Adapter<CatagoryAdapter.MyViewHolder> {

    Context context;
    ArrayList<CategoryModel> categoryModels;
    OnRecycler onRecycler;

    public CatagoryAdapter(Context context, ArrayList<CategoryModel> categoryModels, OnRecycler onRecycler) {
        this.context=context;
        this.categoryModels=categoryModels;
        this.onRecycler=onRecycler;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.category_model,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        CategoryModel categoryModel=categoryModels.get(position);

        String s=categoryModel.getCat_image();

        RequestOptions requestOptionowner = new RequestOptions();
        requestOptionowner.placeholder(R.drawable.image_gallery);
        requestOptionowner.error(R.drawable.image_gallery);

        Glide.with(context)
                .setDefaultRequestOptions(requestOptionowner)
                .load(s).into(holder.iv);

//        holder.iv.setImageResource(categoryModel.getImage());

    }


    @Override
    public int getItemCount() {
        return categoryModels.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView iv;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            iv=itemView.findViewById(R.id.iv);

        }
    }
}
