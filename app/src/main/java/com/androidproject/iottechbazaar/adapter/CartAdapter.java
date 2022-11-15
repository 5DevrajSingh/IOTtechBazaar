package com.androidproject.iottechbazaar.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
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
import com.androidproject.iottechbazaar.inter.OnUpdateStock;
import com.androidproject.iottechbazaar.model.Cart_Model;

import com.androidproject.iottechbazaar.util.QuickPickerZero;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.math.BigDecimal;
import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.MyViewHolder> {

    Context context;
    ArrayList<Cart_Model> cart_models;
    OnUpdateStock onRecycler;

    public CartAdapter(Context context, ArrayList<Cart_Model> cart_models, OnUpdateStock onRecycler) {
        this.context=context;
        this.cart_models=cart_models;
        this.onRecycler=onRecycler;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_model,parent,false);
       return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {

        Cart_Model cart_model=cart_models.get(position);
        String s=cart_model.getImg();
        RequestOptions requestOptionowner = new RequestOptions();
        requestOptionowner.placeholder(R.drawable.image_gallery);
        requestOptionowner.error(R.drawable.image_gallery);

        Glide.with(context)
                .setDefaultRequestOptions(requestOptionowner)
                .load(s).into(holder.image);

        holder.title.setText(cart_model.getProduct_name());
        holder.tv_cat.setText(cart_model.getCategory());
        holder.tv_selling_price.setText("₹"+cart_model.getCell_price());
//        holder.tv_actual_price.setText("₹"+cart_model.getActual_price());
//        holder.tv_actual_price.setBackground(context.getResources().getDrawable(R.drawable.line));

        if (!cart_model.getActual_price().equalsIgnoreCase("")) {
            String discount=cart_model.getDiscount();
            holder.tv_actual_price.setVisibility(View.VISIBLE);
            holder.disc.setVisibility(View.VISIBLE);
            holder.tv_actual_price.setText("₹"+cart_model.getActual_price());
//        holder.actual_price.setPaintFlags( holder.actual_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.tv_actual_price.setBackground(context.getResources().getDrawable(R.drawable.line));
            holder.disc.setText(discount+"% OFF");
        }
        if (cart_model.getActual_price().equalsIgnoreCase("")) {
            holder.tv_actual_price.setVisibility(View.GONE);
            holder.disc.setVisibility(View.GONE);
        }


        int stoc = Integer.parseInt(cart_model.getStock());
        if (stoc == 0) {
            holder.tv_stock.setTextColor(Color.parseColor("#D50000"));
            holder.tv_stock.setText(context.getResources().getString(R.string.Out_of_Stock));
        }
        if (stoc != 0) {
            holder.tv_stock.setTextColor(Color.parseColor("#1B5E20"));
            holder.tv_stock.setText(context.getResources().getString(R.string.In_Stock));
        }

        String price;
        String totalPri;

        String a = cart_model.getCell_price();
        price = a.replaceAll("\\s+", "");

        totalPri = String.valueOf(new BigDecimal(price)
                .multiply(new BigDecimal(cart_model.getQuantity())));

        cart_model.setShowPrice(totalPri);

        holder.tv_selling_price.setText("₹" + cart_model.getShowPrice());

        String actualprice;
        String actualtotalPri;

        String aa = cart_model.getActual_price();
        if (!aa.equalsIgnoreCase("")) {
            actualprice = aa.replaceAll("\\s+", "");

            actualtotalPri = String.valueOf(new BigDecimal(actualprice)
                    .multiply(new BigDecimal(cart_model.getQuantity())));

            cart_model.setShowActualPrice(actualtotalPri);

            holder.tv_actual_price.setText("₹" + cart_model.getShowActualPrice());
        }
        if (aa.equalsIgnoreCase("")) {
            String aaa = cart_model.getTotal_actal_price();
            actualprice = aaa.replaceAll("\\s+", "");

            actualtotalPri = String.valueOf(new BigDecimal(actualprice)
                    .multiply(new BigDecimal(cart_model.getQuantity())));

            cart_model.setShowActualPrice(actualtotalPri);

            holder.tv_actual_price.setText("₹" + cart_model.getShowActualPrice());
        }
//        holder.tv_take_quantity.setText("Qty: "+add_cart.getCquantity());

        int quan = Integer.parseInt(cart_model.getQuantity());

        holder.quantityPicker.setQuantitySelected(quan);

        holder.quantityPicker.setOnQuantityChangeListener(new QuickPickerZero.OnQuantityChangeListener() {
            @Override
            public void onValueChanged(int quantity, View v, String inc_dec) {
                // Returns the quantity selected on quantity selection

                String totall = null;
                String sQuan = String.valueOf(quantity);

                String price;
                String totalPri;




                String a = cart_model.getCell_price();
                price = a.replaceAll("\\s+", "");

                String actualprice;
                String actualtotalPri = null;
                String finalDiscount = null;




                String aa = cart_model.getActual_price();
                actualprice = aa.replaceAll("\\s+", "");


                String s = cart_model.getStock();

//                Toast.makeText(context, ""+s, Toast.LENGTH_SHORT).show();

                int stoc = Integer.parseInt(s);

                holder.quantityPicker.setMaxQuantity(stoc);

                if (stoc == quantity) {
                    Toast.makeText(context, context.getResources().getString(R.string.Sorry_cant_add_more_than) + " "+stoc +" "+ context.getResources().getString(R.string.quantity_of_a_product), Toast.LENGTH_SHORT).show();

                    cart_model.setQuantity(sQuan);

                    totalPri = String.valueOf(new BigDecimal(price)
                            .multiply(new BigDecimal(sQuan)));
                    holder.tv_selling_price.setText("₹" + totalPri);

                    cart_model.setShowPrice(totalPri);



                    if (!aa.equals("")) {

                        actualtotalPri = String.valueOf(new BigDecimal(actualprice)
                                .multiply(new BigDecimal(sQuan)));
                        holder.tv_actual_price.setText("₹" + actualtotalPri);

                        int discount= Integer.parseInt(cart_model.getDiscount());
                        int actualPri= Integer.parseInt(actualprice);
                        int finalPrice = (actualPri * discount) / 100;
                        finalDiscount= String.valueOf(finalPrice);

                    }
                    if (aa.equals("")) {
                        String aaa = cart_model.getTotal_actal_price();
                        actualprice = aaa.replaceAll("\\s+", "");

                        actualtotalPri = String.valueOf(new BigDecimal(actualprice)
                                .multiply(new BigDecimal(sQuan)));
                        holder.tv_actual_price.setText("₹" + actualtotalPri);

                    }

                    cart_model.setShowActualPrice(actualtotalPri);

//                        if (inc_dec.equalsIgnoreCase("d")) {
//                            totall = String.valueOf(new BigDecimal(tot)
//                                    .subtract(new BigDecimal(price)));
//                        }
//                        if (inc_dec.equalsIgnoreCase("i")) {
//                            totall = String.valueOf(new BigDecimal(tot)
//                                    .add(new BigDecimal(price)));
//                        }
//
//                        add_cart.setTotal(totall);


                    onRecycler.onDataPass(price,actualprice,finalDiscount, position, inc_dec);

                } else {
                    if (quantity == 0) {
                        holder.quantityPicker.setQuantitySelected(1);
                        cart_model.setQuantity("1");

                        totalPri = String.valueOf(new BigDecimal(price)
                                .multiply(new BigDecimal("1")));
                        holder.tv_selling_price.setText("₹" + totalPri);

                        cart_model.setShowPrice(totalPri);
                        if (!aa.equalsIgnoreCase("")) {
                            actualtotalPri = String.valueOf(new BigDecimal(actualprice)
                                    .multiply(new BigDecimal("1")));
                            holder.tv_actual_price.setText("₹" + actualtotalPri);

                            cart_model.setShowActualPrice(actualtotalPri);
                        }
                        if (aa.equalsIgnoreCase("")) {
                            String aaa = cart_model.getTotal_actal_price();
                            actualprice = aaa.replaceAll("\\s+", "");
                            actualtotalPri = String.valueOf(new BigDecimal(actualprice)
                                    .multiply(new BigDecimal("1")));
                            holder.tv_actual_price.setText("₹" + actualtotalPri);

                            cart_model.setShowActualPrice(actualtotalPri);
                        }

                    } else {



                        cart_model.setQuantity(sQuan);

                        totalPri = String.valueOf(new BigDecimal(price)
                                .multiply(new BigDecimal(sQuan)));
                        holder.tv_selling_price.setText("₹" + totalPri);

                        cart_model.setShowPrice(totalPri);
                        if (!aa.equalsIgnoreCase("")) {
                            actualtotalPri = String.valueOf(new BigDecimal(actualprice)
                                    .multiply(new BigDecimal(sQuan)));
                            holder.tv_actual_price.setText("₹" + actualtotalPri);

                            int discount= Integer.parseInt(cart_model.getDiscount());
                            int actualPri= Integer.parseInt(actualprice);
                            int finalPrice = (actualPri * discount) / 100;
                            finalDiscount= String.valueOf(finalPrice);

                            cart_model.setShowActualPrice(actualtotalPri);
                        }
                        if (aa.equalsIgnoreCase("")) {
                            String aaa = cart_model.getTotal_actal_price();
                            actualprice = aaa.replaceAll("\\s+", "");
                            actualtotalPri = String.valueOf(new BigDecimal(actualprice)
                                    .multiply(new BigDecimal(sQuan)));
                            holder.tv_actual_price.setText("₹" + actualtotalPri);


                            cart_model.setShowActualPrice(actualtotalPri);
                        }



//                        if (inc_dec.equalsIgnoreCase("d")) {
//                            totall = String.valueOf(new BigDecimal(tot)
//                                    .subtract(new BigDecimal(price)));
//                        }
//                        if (inc_dec.equalsIgnoreCase("i")) {
//                            totall = String.valueOf(new BigDecimal(tot)
//                                    .add(new BigDecimal(price)));
//                        }
//
//                        add_cart.setTotal(totall);


                        onRecycler.onDataPass(price,actualprice,finalDiscount, position, inc_dec);

                    }




                }


            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onRecycler.onClick(position);
            }
        });

        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onRecycler.onLongClick(position);
            }
        });






    }


    @Override
    public int getItemCount() {
        return cart_models.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView image,delete;
        TextView title,tv_cat,tv_selling_price,tv_stock,tv_actual_price,disc;
        QuickPickerZero quantityPicker;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            image=itemView.findViewById(R.id.image);
            delete=itemView.findViewById(R.id.delete);
            title=itemView.findViewById(R.id.title);
            tv_cat=itemView.findViewById(R.id.tv_cat);
            tv_selling_price=itemView.findViewById(R.id.tv_selling_price);
            tv_actual_price=itemView.findViewById(R.id.tv_actual_price);
            tv_stock=itemView.findViewById(R.id.tv_stock);
            quantityPicker=itemView.findViewById(R.id.quantityPicker);
            disc=itemView.findViewById(R.id.disc);
        }
    }
}
