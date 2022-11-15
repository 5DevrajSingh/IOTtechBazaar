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
import androidx.core.content.ContextCompat;
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

import java.math.BigDecimal;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.MyViewHolder> {

    Context context;
    ArrayList<Product_Model> product_models;
    OnRecycler onRecycler;
    String user_id;
    MyProgresbar myProgresbar;

    public ProductAdapter(Context context, ArrayList<Product_Model> product_models, String user_id, OnRecycler onRecycler) {
        this.context=context;
        this.product_models=product_models;
        this.onRecycler=onRecycler;
        this.user_id=user_id;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.product_model,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {

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
        holder.selling_price.setText("₹"+product_model.getCell_price());
        String actual_price=product_model.getActual_price();
        if (!actual_price.equalsIgnoreCase("")) {
            holder.actual_price.setVisibility(View.VISIBLE);
            holder.tv_dis.setVisibility(View.VISIBLE);
            holder.actual_price.setText("₹"+product_model.getActual_price());
//        holder.actual_price.setPaintFlags( holder.actual_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.actual_price.setBackground(context.getResources().getDrawable(R.drawable.line));
            holder.tv_dis.setText(product_model.getDiscount()+"% OFF");
        }
        if (actual_price.equalsIgnoreCase("")) {
            holder.actual_price.setVisibility(View.GONE);
            holder.tv_dis.setVisibility(View.GONE);
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

                hitApi(user_id, product_model.getId(), holder.like,position);

            }
        });



        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               onRecycler.onClick(position);



            }
        });

    }

    private void hitApi(String user_id, String id, ImageView like, int position) {
//        Log.i("vfbfj",user_id+" -"+id);
        SharedPreferences pref = context.getApplicationContext().getSharedPreferences("MyPref", 0);
        user_id = pref.getString("user_id", null);
        if (Utility.isNetworkAvailable(context)) {
            if (myProgresbar == null)
                myProgresbar = MyProgresbar.show(context, R.style.Theme_MyprogressDialog_yellow);
            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
            Call<String> stringCall = apiInterface.addWishList(user_id, id);
            stringCall.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    String sResponse = response.body();
                    if (myProgresbar != null) {
                        myProgresbar.dismiss();
                        myProgresbar = null;
                    }
                    try {
                        if (!TextUtils.isEmpty(sResponse)) {
                            JSONObject jsonObject = new JSONObject(sResponse);
                            String type = jsonObject.getString("type");
                                if (type.equalsIgnoreCase("Success")) {
                                    product_models.get(position).setWish("1");
                                    Toast.makeText(context, context.getResources().getString(R.string.Product_added_in_Wishlist), Toast.LENGTH_SHORT).show();
                                    like.setImageResource(R.drawable.ic_dark_like);
                                }
                                if (type.equalsIgnoreCase("Remove")) {
                                    product_models.get(position).setWish("0");
                                    Toast.makeText(context, context.getResources().getString(R.string.Removed_from_Wishlist), Toast.LENGTH_SHORT).show();
                                    like.setImageResource(R.drawable.ic_heart_light);
                                }
                                if (type.equalsIgnoreCase("Error")) {
                                    product_models.get(position).setWish("0");
                                    like.setImageResource(R.drawable.ic_heart_light);
                                    onRecycler.onLongClick(position);
                                }


                        } else {
                            Toast.makeText(context, context.getResources().getString(R.string.slow_network_connection), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(context, context.getResources().getString(R.string.slow_network_connection), Toast.LENGTH_SHORT).show();
                        if (myProgresbar != null) {
                            myProgresbar.dismiss();
                            myProgresbar = null;
                        }
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Toast.makeText(context, context.getResources().getString(R.string.slow_network_connection), Toast.LENGTH_SHORT).show();
                    if (myProgresbar != null) {
                        myProgresbar.dismiss();
                        myProgresbar = null;
                    }
                }
            });

        } else {
            Toast.makeText(context, context.getResources().getString(R.string.Please_Check_Your_Internet), Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    public int getItemCount() {
        return product_models.size();
    }

//    public void setUncheck(int pos, ImageView imageView) {
//
//        if (product_models.get(pos).isSelected() == false){
//            product_models.get(pos).setSelected(true);
//           imageView.setImageResource(R.drawable.ic_dark_like);
//
//        }
//
//    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView image,like;
        TextView title,selling_price,actual_price,tv_dis;

        public MyViewHolder(@NonNull View view) {
            super(view);
            image = (ImageView)view.findViewById(R.id.image);
            like = (ImageView)view.findViewById(R.id.like);
            title = (TextView) view.findViewById(R.id.title);
            selling_price = (TextView) view.findViewById(R.id.selling_price);
            actual_price = (TextView) view.findViewById(R.id.actual_price);
            tv_dis = (TextView) view.findViewById(R.id.tv_dis);
        }
    }
}
