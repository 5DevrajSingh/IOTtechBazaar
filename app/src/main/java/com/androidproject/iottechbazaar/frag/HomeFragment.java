package com.androidproject.iottechbazaar.frag;

import static android.app.Activity.RESULT_OK;
import static com.androidproject.iottechbazaar.util.BaseActivity.isValid;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.androidproject.iottechbazaar.R;
import com.androidproject.iottechbazaar.activity.LoginActivity;
import com.androidproject.iottechbazaar.activity.MainActivity;
import com.androidproject.iottechbazaar.activity.ProductDetailActivity;
import com.androidproject.iottechbazaar.adapter.CatagoryAdapter;
import com.androidproject.iottechbazaar.adapter.ProductAdapter;
import com.androidproject.iottechbazaar.databinding.FragmentHomeBinding;
import com.androidproject.iottechbazaar.inter.OnRecycler;
import com.androidproject.iottechbazaar.model.CategoryModel;
import com.androidproject.iottechbazaar.model.Product_Model;
import com.androidproject.iottechbazaar.otpedittext.OnCompleteListener;
import com.androidproject.iottechbazaar.otpedittext.OtpEditText;
import com.androidproject.iottechbazaar.retro.ApiClient;
import com.androidproject.iottechbazaar.retro.ApiInterface;
import com.androidproject.iottechbazaar.util.RxBus;
import com.androidproject.iottechbazaar.util.SmsBroadcastReceiver;
import com.androidproject.iottechbazaar.util.Utility;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    View view;
    FragmentHomeBinding binding;
    ArrayList<CategoryModel> categoryModels;
    ArrayList<Product_Model> product_models;
    OtpEditText oev_view;
    String banner_1;
    ProductAdapter productAdapter;
    String user_id;
    SharedPreferences pref;
    String cart;




    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        view = binding.getRoot();

        pref = getActivity().getApplicationContext().getSharedPreferences("MyPref", 0);
        user_id = pref.getString("user_id", null);


//        binding.textHome.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(getActivity(), "asc", Toast.LENGTH_SHORT).show();
//            }
//        });

        intialise();

        hitApi();




        return view;
    }

    private void hitApi() {

        Call<String> stringCall = null;
        if (Utility.isNetworkAvailable(getActivity())) {
            ((MainActivity) getActivity()).showProgressBar();
            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
            stringCall = apiInterface.getDashData(user_id);
//            Toast.makeText(this, ""+id, Toast.LENGTH_SHORT).show();
            stringCall.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    String sResponse = response.body();
//                    Log.i("efef", sResponse);
                    if (isAdded()) {
                        ((MainActivity) getActivity()).hideProgressBar();

                        if (!TextUtils.isEmpty(sResponse)) {

                            try {

                                RequestOptions requestOptionowner = new RequestOptions();
                                requestOptionowner.placeholder(R.drawable.image_gallery);
                                requestOptionowner.error(R.drawable.image_gallery);

                                JSONObject jsonObject = new JSONObject(sResponse);
                                String type = jsonObject.getString("type");


                                if (type.equalsIgnoreCase("Success")) {

                                    banner_1 = jsonObject.getString("banner_1");
                                    pref = getActivity().getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
                                    SharedPreferences.Editor editor = pref.edit();
                                    editor.putString("banner_1", banner_1);
                                    editor.commit();
                                    String banner_2 = jsonObject.getString("banner_2");
                                    String postar_1 = jsonObject.getString("postar_1");
                                    String postar_2 = jsonObject.getString("postar_2");
                                    String postar_3 = jsonObject.getString("postar_3");
                                    String postar_4 = jsonObject.getString("postar_4");
                                    cart = jsonObject.getString("cart");
//                                    ((MainActivity) getActivity()).changeCartCount(cart);

//                                    Activity activity = getActivity();
//                                    if(activity instanceof MainActivity) {
//                                        ((MainActivity) activity).changeCartCount(cart);
//                                    }

//                                    RxBus.getSubject().onNext("10") ;

//

                                    Glide.with(getActivity())
                                            .setDefaultRequestOptions(requestOptionowner)
                                            .load(banner_1)
                                            .into(new SimpleTarget<Drawable>() {
                                                @Override
                                                public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                                    binding.bannerOne.setBackgroundDrawable(resource);
                                                }
                                            });

                                    Glide.with(getActivity())
                                            .setDefaultRequestOptions(requestOptionowner)
                                            .load(banner_2)
                                            .into(new SimpleTarget<Drawable>() {
                                                @Override
                                                public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                                    binding.bannerTwo.setBackgroundDrawable(resource);
                                                }
                                            });


                                    Glide.with(getActivity())
                                            .setDefaultRequestOptions(requestOptionowner)
                                            .load(postar_1).into(binding.posterOne);

                                    Glide.with(getActivity())
                                            .setDefaultRequestOptions(requestOptionowner)
                                            .load(postar_2).into(binding.posterTwo);

                                    Glide.with(getActivity())
                                            .setDefaultRequestOptions(requestOptionowner)
                                            .load(postar_3).into(binding.posterThree);

                                    Glide.with(getActivity())
                                            .setDefaultRequestOptions(requestOptionowner)
                                            .load(postar_4).into(binding.posterFour);

                                    JSONArray jsonArray = jsonObject.getJSONArray("category");
                                    categoryModels = new ArrayList<>();
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                        String id = jsonObject1.getString("id");
                                        String name = jsonObject1.getString("name");
                                        String cat_image = jsonObject1.getString("cat_image");
                                        categoryModels.add(new CategoryModel(id, name, cat_image));
                                    }

                                    setAdapter(categoryModels);

                                    JSONArray jsonArray1 = jsonObject.getJSONArray("product");
                                    product_models = new ArrayList<>();
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

        productAdapter = new ProductAdapter(getActivity(), product_models, user_id, new OnRecycler() {
            @Override
            public void onClick(int pos) {
                String id=product_models.get(pos).getId();
                Intent intent=new Intent(getActivity(), ProductDetailActivity.class);
                intent.putExtra("check","home");
                intent.putExtra("id",id);
                startActivity(intent);
                getActivity().finish();
            }

            @Override
            public void onLongClick(int pos) {



                Intent intent=new Intent(getActivity(), LoginActivity.class);
                intent.putExtra("banner_1",banner_1);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_out_bottom, R.anim.slide_in_bottom);


            }

            @Override
            public void onExtraClick(int pos, ImageView imageView) {
//                ((ProductAdapter) productAdapter)
//                        .setUncheck(pos,imageView);
            }
        });

        binding.trendingRecyclerview.setAdapter(productAdapter);

    }







    private void getOtpFromMessage(String message) {
        // This will match any 6 digit number in the message
        Pattern pattern = Pattern.compile("(|^)\\d{6}");
        Matcher matcher = pattern.matcher(message);
        if (matcher.find()) {
            oev_view.setText(matcher.group(0));
        }
    }







    private void setAdapter(ArrayList<CategoryModel> categoryModels) {

        CatagoryAdapter catagoryAdapter = new CatagoryAdapter(getActivity(), categoryModels, new OnRecycler() {
            @Override
            public void onClick(int pos) {

            }

            @Override
            public void onLongClick(int pos) {

            }

            @Override
            public void onExtraClick(int pos, ImageView imageView) {

            }
        });


        binding.favouritRecyclerview.setAdapter(catagoryAdapter);

    }

    private void intialise() {
        binding.favouritRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        binding.trendingRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
//        binding.favouritRecyclerview.setItemAnimator(new DefaultItemAnimator());

    }

    public HomeFragment() {
    }


}
