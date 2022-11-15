package com.androidproject.iottechbazaar.activity;

import android.animation.Animator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.androidproject.iottechbazaar.R;
import com.androidproject.iottechbazaar.databinding.ActivityProductDetailBindingImpl;
import com.androidproject.iottechbazaar.frag.AllImageFrag;
import com.androidproject.iottechbazaar.frag.HomeFragment;
import com.androidproject.iottechbazaar.model.CategoryModel;
import com.androidproject.iottechbazaar.model.Product_Model;
import com.androidproject.iottechbazaar.retro.ApiClient;
import com.androidproject.iottechbazaar.retro.ApiInterface;
import com.androidproject.iottechbazaar.util.BaseActivity;
import com.androidproject.iottechbazaar.util.CircleAnimationUtil;
import com.androidproject.iottechbazaar.util.MyProgresbar;
import com.androidproject.iottechbazaar.util.QuantityPicker;
import com.androidproject.iottechbazaar.util.Utility;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductDetailActivity extends BaseActivity {

    String id;
    ActivityProductDetailBindingImpl binding;
    Context context;
    String[] values;
    String user_id;
    SharedPreferences pref;
    String banner_1,check,quan;
    int stoc;
    String cell_price;
    String totalPri;
    String actual_price;
    private int itemCounter = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        makeFullScreen();
//        setContentView(R.layout.activity_product_detail);
        binding=DataBindingUtil.setContentView(ProductDetailActivity.this,R.layout.activity_product_detail);
        changeStatusBarColor(ContextCompat.getColor(this, R.color.purple_500));
        hideToolbar();
        Intent intent=getIntent();
        id=intent.getStringExtra("id");
        check=intent.getStringExtra("check");
        pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        user_id = pref.getString("user_id", null);
        banner_1 = pref.getString("banner_1", null);

        quan="1";

        context=ProductDetailActivity.this;

        binding.quantityPicker.setTextStyle(QuantityPicker.BOLD);
        binding.quantityPicker.setMinQuantity(1);
        binding.quantityPicker.setQuantityPicker(true);


        binding.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int state) {
            }

            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            public void onPageSelected(int position) {
                binding.pageIndicatorView.setSelection(position);
                AllImageFrag frag1 = (AllImageFrag) binding.viewPager
                        .getAdapter()
                        .instantiateItem(binding.viewPager, binding.viewPager.getCurrentItem());
                frag1.getCatId(values[position]);

            }
        });

        binding.rlTool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        binding.llWish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hitAddWishApi();
            }
        });


        binding.quantityPicker.setOnQuantityChangeListener(new QuantityPicker.OnQuantityChangeListener() {
            @Override
            public void onValueChanged(int quantity) {
                // Returns the quantity selected on quantity selection

                if (stoc == quantity) {
                    Toast.makeText(ProductDetailActivity.this, getResources().getString(R.string.Sorry_cant_add_more_than)+" " + stoc +" "+ getResources().getString(R.string.quantity_of_a_product), Toast.LENGTH_SHORT).show();
                }

                if (stoc == 0) {
                    Toast.makeText(ProductDetailActivity.this, getResources().getString(R.string.Sorry_product_is_out_of_stock), Toast.LENGTH_SHORT).show();
                }

                quan = String.valueOf(quantity);
                String a = cell_price;
                cell_price = a.replaceAll("\\s+", "");
//                int sell_price = Integer.parseInt(price);
//
//                int totalprice = sell_price * quantity;
//
//                totalPri = String.valueOf(totalprice);
//
//                tv_price.setText("₹" + totalprice);
                totalPri = String.valueOf(new BigDecimal(cell_price)
                        .multiply(new BigDecimal(quan)));
                binding.sellingPrice.setText("₹" + totalPri);

                String aa = actual_price;
                actual_price = aa.replaceAll("\\s+", "");


//                int mrp_price = Integer.parseInt(mrp);
//
//                int totalmrpprice = mrp_price * quantity;

//                tv_mrp.setText("₹" + totalmrpprice);
                if (!actual_price.equalsIgnoreCase("")) {
                    binding.actualPrice.setText("₹" + new BigDecimal(actual_price)
                            .multiply(new BigDecimal(quan)));
                    binding.actualPrice.setBackground(context.getResources().getDrawable(R.drawable.line));
                }

//                if (tv_saving.getVisibility() == View.VISIBLE) {
//                    String Mrp = tv_mrp.getText().toString().substring(1);
//                    String sell = tv_price.getText().toString().substring(1);
//                    String saving = String.valueOf(new BigDecimal(Mrp)
//                            .subtract(new BigDecimal(sell)));
//                    tv_saving.setText("Saving ₹" + saving);
//                } else {
//
//                }

            }
        });


        binding.aadToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (stoc == 0) {
//                    Toast.makeText(ProductDetailActivity.this, getResources().getString(R.string.product_is_out_of_stock), Toast.LENGTH_SHORT).show();
//                } else {
//                    hitAddToCart();
//                }



                AllImageFrag fragment = (AllImageFrag) binding.viewPager
                        .getAdapter()
                        .instantiateItem(binding.viewPager, binding.viewPager.getCurrentItem());
                makeFlyAnimation(((AllImageFrag)fragment).refreshT());


            }
        });


        
        hitApi();

    }

    private void makeFlyAnimation(ImageView targetView) {

        RelativeLayout destView = (RelativeLayout) findViewById(R.id.cartRelativeLayout);

        new CircleAnimationUtil().attachActivity(this).setTargetView(targetView).setMoveDuration(1000).setDestView(destView).setAnimationListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                addItemToCart();
                Toast.makeText(ProductDetailActivity.this, "Continue Shopping...", Toast.LENGTH_SHORT).show();
                binding.viewPager.setAdapter(new PagerAdapter(getSupportFragmentManager()));
                if (binding.viewPager.getCurrentItem() == 0) {
                    AllImageFrag frag1 = (AllImageFrag)  binding.viewPager
                            .getAdapter()
                            .instantiateItem( binding.viewPager,  binding.viewPager.getCurrentItem());
                    frag1.getCatId(values[0]);
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        }).startAnimation();


    }
    private void addItemToCart() {
        TextView textView = (TextView) findViewById(R.id.textNotify);
        textView.setText(String.valueOf(++itemCounter));
    }

    private void hitAddToCart() {
        pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        user_id = pref.getString("user_id", null);
        if (Utility.isNetworkAvailable(context)) {
            showProgressBar();
            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
            Call<String> stringCall = apiInterface.addToCart(user_id, id,quan,totalPri);
            stringCall.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    String sResponse = response.body();
                    hideProgressBar();
                    try {
                        if (!TextUtils.isEmpty(sResponse)) {
                            JSONObject jsonObject = new JSONObject(sResponse);
                            String type = jsonObject.getString("type");
                            if (type.equalsIgnoreCase("Success")) {
                                Toast.makeText(context, context.getResources().getString(R.string.added_in_cart_done), Toast.LENGTH_SHORT).show();
                            }
                            if (type.equalsIgnoreCase("Error")) {
                                Intent intent=new Intent(context, LoginActivity.class);
                                intent.putExtra("banner_1",banner_1);
                                startActivity(intent);
                                overridePendingTransition(R.anim.slide_out_bottom, R.anim.slide_in_bottom);
                            }


                        } else {
                            Toast.makeText(context, context.getResources().getString(R.string.slow_network_connection), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(context, context.getResources().getString(R.string.slow_network_connection), Toast.LENGTH_SHORT).show();
                        hideProgressBar();
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Toast.makeText(context, context.getResources().getString(R.string.slow_network_connection), Toast.LENGTH_SHORT).show();
                    hideProgressBar();
                }
            });

        } else {
            Toast.makeText(context, context.getResources().getString(R.string.Please_Check_Your_Internet), Toast.LENGTH_SHORT).show();
        }

    }

    private void hitAddWishApi() {
        pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        user_id = pref.getString("user_id", null);
        if (Utility.isNetworkAvailable(context)) {
            showProgressBar();
            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
            Call<String> stringCall = apiInterface.addWishList(user_id, id);
            stringCall.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    String sResponse = response.body();
                    hideProgressBar();
                    try {
                        if (!TextUtils.isEmpty(sResponse)) {
                            JSONObject jsonObject = new JSONObject(sResponse);
                            String type = jsonObject.getString("type");
                            if (type.equalsIgnoreCase("Success")) {
                                Toast.makeText(context, context.getResources().getString(R.string.Product_added_in_Wishlist), Toast.LENGTH_SHORT).show();
                                binding.ivWish.setImageResource(R.drawable.ic_dark_like);
                            }
                            if (type.equalsIgnoreCase("Remove")) {
                                Toast.makeText(context, context.getResources().getString(R.string.Removed_from_Wishlist), Toast.LENGTH_SHORT).show();
                                binding.ivWish.setImageResource(R.drawable.ic_heart_light);
                            }
                            if (type.equalsIgnoreCase("Error")) {
                                binding.ivWish.setImageResource(R.drawable.ic_heart_light);
                                Intent intent=new Intent(ProductDetailActivity.this, LoginActivity.class);
                                intent.putExtra("banner_1",banner_1);
                                startActivity(intent);
                                overridePendingTransition(R.anim.slide_out_bottom, R.anim.slide_in_bottom);
                            }


                        } else {
                            Toast.makeText(context, context.getResources().getString(R.string.slow_network_connection), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(context, context.getResources().getString(R.string.slow_network_connection), Toast.LENGTH_SHORT).show();
                       hideProgressBar();
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Toast.makeText(context, context.getResources().getString(R.string.slow_network_connection), Toast.LENGTH_SHORT).show();
                   hideProgressBar();
                }
            });

        } else {
            Toast.makeText(context, context.getResources().getString(R.string.Please_Check_Your_Internet), Toast.LENGTH_SHORT).show();
        }

    }

    private void hitApi() {

        Call<String> stringCall = null;
        if (Utility.isNetworkAvailable(context)) {
            showProgressBar();
            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
            stringCall = apiInterface.getProductDetail(id,user_id);
//            Toast.makeText(this, ""+id, Toast.LENGTH_SHORT).show();
            stringCall.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    String sResponse = response.body();
//                    Log.i("efef", sResponse);

                        hideProgressBar();

                        if (!TextUtils.isEmpty(sResponse)) {

                            try {

                                JSONObject jsonObject = new JSONObject(sResponse);
                                String type = jsonObject.getString("type");


                                if (type.equalsIgnoreCase("Success")) {

                                    JSONArray jsonArray=jsonObject.getJSONArray("data");
                                    JSONObject jsonObject1=jsonArray.getJSONObject(0);
                                    String id=jsonObject1.getString("id");
                                    String sku_code=jsonObject1.getString("sku_code");
                                    String category=jsonObject1.getString("category");
                                    String product_name=jsonObject1.getString("product_name");
                                    binding.productName.setText(product_name);
                                    actual_price=jsonObject1.getString("actual_price");

                                    if (!actual_price.equalsIgnoreCase("")) {
                                        String discount=jsonObject1.getString("discount");
                                        binding.actualPrice.setVisibility(View.VISIBLE);
                                        binding.disc.setVisibility(View.VISIBLE);
                                        binding.actualPrice.setText("₹"+actual_price);
//        holder.actual_price.setPaintFlags( holder.actual_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                                        binding.actualPrice.setBackground(context.getResources().getDrawable(R.drawable.line));
                                        binding.disc.setText(discount+"% OFF");
                                    }
                                    if (actual_price.equalsIgnoreCase("")) {
                                        binding.actualPrice.setVisibility(View.GONE);
                                        binding.disc.setVisibility(View.GONE);
                                    }


//                                    binding.actualPrice.setText("₹"+actual_price);
//                                    binding.actualPrice.setBackground(context.getResources().getDrawable(R.drawable.line));
                                    cell_price=jsonObject1.getString("cell_price");
                                    totalPri=jsonObject1.getString("cell_price");
                                    binding.sellingPrice.setText("₹"+cell_price);

                                    String offer=jsonObject1.getString("offer");
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                        binding.offer.setText(Html.fromHtml(offer, Html.FROM_HTML_MODE_COMPACT));
                                    } else {
                                        binding.offer.setText(Html.fromHtml(offer));
                                    }
                                    String key_feauture=jsonObject1.getString("key_feauture");
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                        binding.keyfeatures.setText(Html.fromHtml(key_feauture, Html.FROM_HTML_MODE_COMPACT));
                                    } else {
                                        binding.keyfeatures.setText(Html.fromHtml(key_feauture));
                                    }
                                    String specification=jsonObject1.getString("specification");
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                        binding.speci.setText(Html.fromHtml(specification, Html.FROM_HTML_MODE_COMPACT));
                                    } else {
                                        binding.speci.setText(Html.fromHtml(specification));
                                    }
                                    String overview=jsonObject1.getString("overview");
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                        binding.overvieww.setText(Html.fromHtml(overview, Html.FROM_HTML_MODE_COMPACT));
                                    } else {
                                        binding.overvieww.setText(Html.fromHtml(overview));
                                    }
                                    String description=jsonObject1.getString("description");
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                        binding.description.setText(Html.fromHtml(description, Html.FROM_HTML_MODE_COMPACT));
                                    } else {
                                        binding.description.setText(Html.fromHtml(description));
                                    }
                                    String img=jsonObject1.getString("img");
                                    String img1=jsonObject1.getString("img1");
                                    String img2=jsonObject1.getString("img2");

                                    String allImg=img+","+img1+","+img2;

                                    values = allImg.split(",");
                                    binding.viewPager.setAdapter(new PagerAdapter(getSupportFragmentManager()));
                                    if (binding.viewPager.getCurrentItem() == 0) {
                                        AllImageFrag frag1 = (AllImageFrag)  binding.viewPager
                                                .getAdapter()
                                                .instantiateItem( binding.viewPager,  binding.viewPager.getCurrentItem());
                                        frag1.getCatId(values[0]);
                                    }

                                    String wish=jsonObject1.getString("wish");
                                    if (wish.equalsIgnoreCase("1")) {
                                        binding.ivWish.setImageResource(R.drawable.ic_dark_like);
                                    }
                                    if (wish.equalsIgnoreCase("0")) {
                                       binding.ivWish.setImageResource(R.drawable.ic_heart_light);
                                    }

                                    String stock=jsonObject1.getString("stock");
                                    binding.tvInStock.setText(stock);
                                    stoc = Integer.parseInt(stock);
                                    if (stoc == 0) {
                                        binding.tvInStock.setTextColor(Color.parseColor("#D50000"));
                                        binding.tvInStock.setText(getResources().getString(R.string.Out_of_Stock));
                                    }
                                    if (stoc != 0) {
                                        binding.tvInStock.setTextColor(Color.parseColor("#1B5E20"));
                                        binding.tvInStock.setText(getResources().getString(R.string.In_Stock));
                                    }
                                    binding.quantityPicker.setMaxQuantity(stoc);



                                }
                            }catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(context, "" + e.toString(), Toast.LENGTH_SHORT).show();
                              hideProgressBar();
                            }
                        } else {
                            Toast.makeText(context, getResources().getString(R.string.slow_network_connection), Toast.LENGTH_SHORT).show();
                        }
                    }




                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Toast.makeText(context, getResources().getString(R.string.slow_network_connection), Toast.LENGTH_SHORT).show();
                   hideProgressBar();
                }
            });

        } else {
            Toast.makeText(context, getResources().getString(R.string.please_check_your_internet), Toast.LENGTH_SHORT).show();
           hideProgressBar();
        }

    }

    public class PagerAdapter extends FragmentStatePagerAdapter {
        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            return new AllImageFrag();
        }

        @Override
        public int getCount() {
            return values.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return values[position];
        }
    }

    @Override
    public void onBackPressed() {
        if (check.equalsIgnoreCase("home"))
        {
            startActivity(new Intent(ProductDetailActivity.this,MainActivity.class));
            finish();
            overridePendingTransition(R.anim.slide_out_bottom, R.anim.slide_in_bottom);
        }

        if (check.equalsIgnoreCase("wish"))
        {
            Intent intent=new Intent(ProductDetailActivity.this,MainActivity.class);
            intent.putExtra("check",check);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.slide_out_bottom, R.anim.slide_in_bottom);
        }
        if (check.equalsIgnoreCase("cart"))
        {
            Intent intent=new Intent(ProductDetailActivity.this,MainActivity.class);
            intent.putExtra("check",check);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.slide_out_bottom, R.anim.slide_in_bottom);
        }
    }
}
