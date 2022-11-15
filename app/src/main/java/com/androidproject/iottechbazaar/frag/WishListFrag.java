package com.androidproject.iottechbazaar.frag;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.androidproject.iottechbazaar.R;
import com.androidproject.iottechbazaar.activity.MainActivity;
import com.androidproject.iottechbazaar.activity.ProductDetailActivity;
import com.androidproject.iottechbazaar.adapter.WishListAdapter;
import com.androidproject.iottechbazaar.databinding.FragmentWishBinding;
import com.androidproject.iottechbazaar.inter.OnRecycler;
import com.androidproject.iottechbazaar.model.CategoryModel;
import com.androidproject.iottechbazaar.model.Product_Model;
import com.androidproject.iottechbazaar.retro.ApiClient;
import com.androidproject.iottechbazaar.retro.ApiInterface;
import com.androidproject.iottechbazaar.util.MyProgresbar;
import com.androidproject.iottechbazaar.util.Utility;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WishListFrag extends Fragment {

    FragmentWishBinding binding;
    View view;
    String user_id;
    SharedPreferences pref;
    ArrayList<Product_Model> product_models;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.fragment_wish,container,false);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_wish, container, false);
        view = binding.getRoot();

        pref = getActivity().getApplicationContext().getSharedPreferences("MyPref", 0);
        user_id = pref.getString("user_id", null);

        binding.recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));

        hitApi();

        return view;
    }

    private void hitApi() {
        product_models = new ArrayList<>();
        Call<String> stringCall = null;
        if (Utility.isNetworkAvailable(getActivity())) {
            ((MainActivity) getActivity()).showProgressBar();
            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
            stringCall = apiInterface.getWishList(user_id);
//            Toast.makeText(this, ""+id, Toast.LENGTH_SHORT).show();
            stringCall.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    String sResponse = response.body();
//                    Log.i("efef", sResponse);
                    binding.logoWish.setVisibility(View.GONE);
                    if (isAdded()) {
                        ((MainActivity) getActivity()).hideProgressBar();

                        if (!TextUtils.isEmpty(sResponse)) {

                            try {
                                JSONObject jsonObject = new JSONObject(sResponse);
                                String type = jsonObject.getString("type");


                                if (type.equalsIgnoreCase("Success")) {


                                    JSONArray jsonArray1 = jsonObject.getJSONArray("data");

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

                                        product_models.add(new Product_Model(id, sku_code, category, product_name, actual_price, cell_price, discount, offer, key_feauture, specification, overview, description, img, img1, img2, wish));
                                    }

                                    setProductAdapter(product_models);


                                }
                                if (type.equalsIgnoreCase("Error"))
                                {
                                    setProductAdapter(product_models);
                                    binding.logoWish.setVisibility(View.VISIBLE);
                                }
                                if (type.equalsIgnoreCase("Empty"))
                                {
                                    setProductAdapter(product_models);
                                    binding.logoWish.setVisibility(View.VISIBLE);
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

    private void setProductAdapter(ArrayList<Product_Model> product_models) {
        WishListAdapter wishListAdapter = new WishListAdapter(getActivity(), product_models, new OnRecycler() {
            @Override
            public void onClick(int pos) {
                String id=product_models.get(pos).getId();
                Intent intent=new Intent(getActivity(), ProductDetailActivity.class);
                intent.putExtra("check","wish");
                intent.putExtra("id",id);
                startActivity(intent);
                getActivity().finish();
            }

            @Override
            public void onLongClick(int pos) {
                hitApi(pos);
            }

            @Override
            public void onExtraClick(int pos, ImageView imageView) {

            }
        });
        binding.recyclerview.setAdapter(wishListAdapter);
    }


    private void hitApi(int position) {
        String id = product_models.get(position).getId();
        if (Utility.isNetworkAvailable(getActivity())) {
            ((MainActivity) getActivity()).showProgressBar();
            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
            Call<String> stringCall = apiInterface.addWishList(user_id, id);
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
                                Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.Product_added_in_Wishlist), Toast.LENGTH_SHORT).show();
                            }
                            if (type.equalsIgnoreCase("Remove")) {
                                Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.Removed_from_Wishlist), Toast.LENGTH_SHORT).show();
                                hitApi();
                            }
                            if (type.equalsIgnoreCase("Error")) {
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
