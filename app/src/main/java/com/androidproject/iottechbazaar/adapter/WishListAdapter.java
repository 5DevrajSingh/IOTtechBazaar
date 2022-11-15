package com.androidproject.iottechbazaar.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.androidproject.iottechbazaar.R;
import com.androidproject.iottechbazaar.inter.OnRecycler;
import com.androidproject.iottechbazaar.model.Product_Model;
import com.androidproject.iottechbazaar.retro.ApiClient;
import com.androidproject.iottechbazaar.retro.ApiInterface;
import com.androidproject.iottechbazaar.util.MyProgresbar;
import com.androidproject.iottechbazaar.util.Utility;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WishListAdapter extends RecyclerView.Adapter<WishListAdapter.MyViewholder> {

    Context context;
    ArrayList<Product_Model> product_models;
    OnRecycler onRecycler;
    MyProgresbar myProgresbar;

    public WishListAdapter(Context context, ArrayList<Product_Model> product_models, OnRecycler onRecycler) {
        this.context=context;
        this.product_models=product_models;
        this.onRecycler=onRecycler;
    }

    @NonNull
    @Override
    public MyViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.wish_list,parent,false);
        return new MyViewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewholder holder, @SuppressLint("RecyclerView") int position) {
        Product_Model product_model=product_models.get(position);

        String s=product_model.getImg();
        RequestOptions requestOptionowner = new RequestOptions();
        requestOptionowner.placeholder(R.drawable.image_gallery);
        requestOptionowner.error(R.drawable.image_gallery);

        Glide.with(context)
                .setDefaultRequestOptions(requestOptionowner)
                .load(s).into(holder.image);

        String wish = product_model.getWish();
        if (wish.equalsIgnoreCase("1")) {
            holder.like.setImageResource(R.drawable.ic_dark_like);
        }
        if (wish.equalsIgnoreCase("0")) {
            holder.like.setImageResource(R.drawable.ic_heart_light);
        }
        holder.title.setText(product_model.getProduct_name());
        holder.tv_cat.setText(product_model.getCategory());
        holder.tv_selling_price.setText("₹"+product_model.getCell_price());
//        holder.tv_actual_price.setText("₹"+product_model.getActual_price());
//        holder.tv_actual_price.setPaintFlags( holder.tv_actual_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
//        holder.tv_actual_price.setBackground(context.getResources().getDrawable(R.drawable.line));

        String actual_price=product_model.getActual_price();
        if (!actual_price.equalsIgnoreCase("")) {
            holder.tv_actual_price.setVisibility(View.VISIBLE);
            holder.tv_actual_price.setVisibility(View.VISIBLE);
            holder.tv_actual_price.setText("₹"+product_model.getActual_price());
//        holder.actual_price.setPaintFlags( holder.actual_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.tv_actual_price.setBackground(context.getResources().getDrawable(R.drawable.line));
            holder.disc.setText(product_model.getDiscount()+"% OFF");
        }
        if (actual_price.equalsIgnoreCase("")) {
            holder.tv_actual_price.setVisibility(View.GONE);
            holder.disc.setVisibility(View.GONE);
        }

        holder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                if (product_model.isSelected() == true){
//                    product_model.setSelected(false);
//
//                    holder.like.setImageResource(R.drawable.ic_dark_like);
//
//                }else {
//                    product_model.setSelected(true);
//                    holder.like.setImageResource(R.drawable.ic_heart_light);
//                }

                onRecycler.onLongClick(position);

            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onRecycler.onClick(position);
            }
        });

    }


    @Override
    public int getItemCount() {
        return product_models.size();
    }



    public class MyViewholder extends RecyclerView.ViewHolder {
        ImageView image,like;
        TextView title,tv_cat,tv_selling_price,tv_actual_price,disc;
        public MyViewholder(@NonNull View itemView) {
            super(itemView);
            image=itemView.findViewById(R.id.image);
            like=itemView.findViewById(R.id.like);
            title=itemView.findViewById(R.id.title);
            tv_cat=itemView.findViewById(R.id.tv_cat);
            tv_selling_price=itemView.findViewById(R.id.tv_selling_price);
            tv_actual_price=itemView.findViewById(R.id.tv_actual_price);
            disc=itemView.findViewById(R.id.disc);
        }
    }
}
