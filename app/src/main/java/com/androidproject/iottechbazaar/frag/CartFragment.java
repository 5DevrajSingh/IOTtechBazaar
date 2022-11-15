package com.androidproject.iottechbazaar.frag;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.androidproject.iottechbazaar.R;
import com.androidproject.iottechbazaar.activity.MainActivity;
import com.androidproject.iottechbazaar.activity.ProductDetailActivity;
import com.androidproject.iottechbazaar.adapter.CartAdapter;
import com.androidproject.iottechbazaar.databinding.FragmentCartBinding;
import com.androidproject.iottechbazaar.inter.OnUpdateStock;
import com.androidproject.iottechbazaar.model.Cart_Model;
import com.androidproject.iottechbazaar.model.Product_Model;
import com.androidproject.iottechbazaar.retro.ApiClient;
import com.androidproject.iottechbazaar.retro.ApiInterface;
import com.androidproject.iottechbazaar.util.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartFragment extends Fragment {

    FragmentCartBinding binding;
    View view;

    String user_id;
    SharedPreferences pref;
    ArrayList<Cart_Model> cart_models;
    String total_selling_price,total_mrp,total_discount;
    int jsonlength;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.fragment_cart,container,false);
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_cart,container,false);
        view=binding.getRoot();

        pref = getActivity().getApplicationContext().getSharedPreferences("MyPref", 0);
        user_id = pref.getString("user_id", null);

        binding.recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));

        hitApi();

        return view;
    }

    private void hitApi() {

        cart_models = new ArrayList<>();
        Call<String> stringCall = null;
        if (Utility.isNetworkAvailable(getActivity())) {
            ((MainActivity) getActivity()).showProgressBar();
            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
            stringCall = apiInterface.getCartList(user_id);
//            Toast.makeText(this, ""+id, Toast.LENGTH_SHORT).show();
            stringCall.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    String sResponse = response.body();
//                    Log.i("efef", sResponse);
                    binding.logoWish.setVisibility(View.GONE);
                    binding.rlStrip.setVisibility(View.VISIBLE);
                    binding.llBag.setVisibility(View.VISIBLE);
                    if (isAdded()) {
                        ((MainActivity) getActivity()).hideProgressBar();

                        if (!TextUtils.isEmpty(sResponse)) {

                            try {
                                JSONObject jsonObject = new JSONObject(sResponse);
                                String type = jsonObject.getString("type");


                                if (type.equalsIgnoreCase("Success")) {

                                    total_selling_price = jsonObject.getString("total_selling_price");
                                    binding.tvTotalBag.setText("₹ "+total_selling_price);
                                    binding.tvTotlalSellingBag.setText("₹ "+total_selling_price);
                                    total_mrp = jsonObject.getString("total_mrp");
                                    binding.tvMrp.setText("₹ "+total_mrp);
                                    total_discount = jsonObject.getString("total_discount");
                                    binding.tvTotalDiscount.setText("- ₹ "+total_discount);


                                    JSONArray jsonArray1 = jsonObject.getJSONArray("data");

                                    jsonlength=jsonArray1.length();

                                    binding.tvTotal.setText(jsonArray1.length()+" "+"Items | "+"₹"+total_selling_price);

                                    for (int i = 0; i < jsonArray1.length(); i++) {
                                        JSONObject jsonObject1 = jsonArray1.getJSONObject(i);
                                        String id = jsonObject1.getString("id");
                                        String sku_code = jsonObject1.getString("sku_code");
                                        String category = jsonObject1.getString("category");
                                        String product_name = jsonObject1.getString("product_name");
                                        String actual_price = jsonObject1.getString("actual_price");
                                        String cell_price = jsonObject1.getString("cell_price");
                                        String discount = jsonObject1.getString("discount");
                                        String offer = jsonObject1.getString("offer");
                                        String key_feauture = jsonObject1.getString("key_feauture");
                                        String specification = jsonObject1.getString("specification");
                                        String overview = jsonObject1.getString("overview");
                                        String description = jsonObject1.getString("description");
                                        String img = jsonObject1.getString("img");
                                        String img1 = jsonObject1.getString("img1");
                                        String img2 = jsonObject1.getString("img2");
                                        String wish = jsonObject1.getString("wish");
                                        String stock = jsonObject1.getString("stock");
                                        String quantity = jsonObject1.getString("quantity");
                                        String total_price = jsonObject1.getString("total_price");
                                        String showPrice = jsonObject1.getString("cell_price");
                                        String showActualPrice = jsonObject1.getString("actual_price");
                                        String total_actal_price = jsonObject1.getString("total_actal_price");

                                        cart_models.add(new Cart_Model(id,sku_code,category,product_name,actual_price,cell_price,discount,offer,key_feauture,specification,overview,description,img,img1,img2,wish,stock,quantity,total_price,showPrice,showActualPrice,total_actal_price,false));
                                    }

                                    setCartAdapter(cart_models);


                                }
                                if (type.equalsIgnoreCase("Error"))
                                {
                                    setCartAdapter(cart_models);
                                    binding.logoWish.setVisibility(View.VISIBLE);
                                    binding.rlStrip.setVisibility(View.GONE);
                                    binding.llBag.setVisibility(View.GONE);
                                }
                                if (type.equalsIgnoreCase("Empty"))
                                {
                                    setCartAdapter(cart_models);
                                    binding.logoWish.setVisibility(View.VISIBLE);
                                    binding.rlStrip.setVisibility(View.GONE);
                                    binding.llBag.setVisibility(View.GONE);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(getActivity(), "" + e.toString(), Toast.LENGTH_SHORT).show();
                                ((MainActivity) getActivity()).hideProgressBar();
                            }
                        } else {
                            Toast.makeText(getActivity(), getResources().getString(R.string.slow_network_connection), Toast.LENGTH_SHORT).show();
                        }
                    }


                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.slow_network_connection), Toast.LENGTH_SHORT).show();
                    ((MainActivity) getActivity()).hideProgressBar();
                }
            });

        } else {
            Toast.makeText(getActivity(), getResources().getString(R.string.please_check_your_internet), Toast.LENGTH_SHORT).show();
            ((MainActivity) getActivity()).hideProgressBar();
        }

    }

    private void setCartAdapter(ArrayList<Cart_Model> cart_models) {

        CartAdapter cartAdapter=new CartAdapter(getActivity(),cart_models, new OnUpdateStock() {
            @Override
            public void onClick(int pos) {
                hitDeleteCartApi(pos);
            }

            @Override
            public void onLongClick(int pos) {
                String id=cart_models.get(pos).getId();
                Intent intent=new Intent(getActivity(), ProductDetailActivity.class);
                intent.putExtra("check","cart");
                intent.putExtra("id",id);
                startActivity(intent);
                getActivity().finish();
            }

            @Override
            public void onDataPass(String value,String MRP,String discount, int pos, String inc_dec) {

                if (!TextUtils.isEmpty(inc_dec)) {

                    if (inc_dec.equalsIgnoreCase("d")) {
                        total_selling_price = String.valueOf(new BigDecimal(total_selling_price)
                                .subtract(new BigDecimal(value)));
                        if (!TextUtils.isEmpty(MRP)) {
                            total_mrp = String.valueOf(new BigDecimal(total_mrp)
                                    .subtract(new BigDecimal(MRP)));

                        }
                        if (!TextUtils.isEmpty(discount)) {
                            total_discount = String.valueOf(new BigDecimal(total_discount)
                                    .subtract(new BigDecimal(discount)));
                        }
                    }
                    if (inc_dec.equalsIgnoreCase("i")) {
                        total_selling_price = String.valueOf(new BigDecimal(total_selling_price)
                                .add(new BigDecimal(value)));
                        if (!TextUtils.isEmpty(MRP)) {
                            total_mrp = String.valueOf(new BigDecimal(total_mrp)
                                    .add(new BigDecimal(MRP)));
                        }
                        if (!TextUtils.isEmpty(discount)) {
                            total_discount = String.valueOf(new BigDecimal(total_discount)
                                    .add(new BigDecimal(discount)));
                        }
                    }

                    binding.tvTotal.setText(jsonlength+" "+"Items | "+"₹"+total_selling_price);
                    binding.tvMrp.setText("₹ "+total_mrp);
                    binding.tvTotlalSellingBag.setText("₹ "+total_selling_price);
                    binding.tvTotalBag.setText("₹ "+total_selling_price);
                    binding.tvTotalDiscount.setText("- ₹ "+total_discount);
                }


            }

            @Override
            public void onDelete(int pos) {

            }
        });

        binding.recyclerview.setAdapter(cartAdapter);

    }

    private void hitDeleteCartApi(int pos) {

        String id = cart_models.get(pos).getId();
        if (Utility.isNetworkAvailable(getActivity())) {
            ((MainActivity) getActivity()).showProgressBar();
            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
            Call<String> stringCall = apiInterface.deleteCart(user_id, id);
            stringCall.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    String sResponse = response.body();
                    ((MainActivity) getActivity()).hideProgressBar();
                    try {
                        if (!TextUtils.isEmpty(sResponse)) {
                            JSONObject jsonObject = new JSONObject(sResponse);
                            String type = jsonObject.getString("type");
                            if (type.equalsIgnoreCase("Success")) {
                                Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.Product_Removed_sucessfully), Toast.LENGTH_SHORT).show();
                                hitApi();
                            }



                        } else {
                            Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.slow_network_connection), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.slow_network_connection), Toast.LENGTH_SHORT).show();
                        ((MainActivity) getActivity()).hideProgressBar();
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.slow_network_connection), Toast.LENGTH_SHORT).show();
                    ((MainActivity) getActivity()).hideProgressBar();
                }
            });

        } else {
            Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.Please_Check_Your_Internet), Toast.LENGTH_SHORT).show();
        }

    }


}
