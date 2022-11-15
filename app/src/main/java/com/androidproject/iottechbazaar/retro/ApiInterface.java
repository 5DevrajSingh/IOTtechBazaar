package com.androidproject.iottechbazaar.retro;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiInterface {

    @POST("get_dashboard")
    @FormUrlEncoded
    Call<String> getDashData(@Field("user_id")String user_id);

    @POST("product_detail")
    @FormUrlEncoded
    Call<String> getProductDetail(@Field("id") String id,@Field("user_id") String user_id);

    @POST("wishlist")
    @FormUrlEncoded
    Call<String> addWishList(@Field("user_id")String user_id,@Field("id") String id);

    @POST("add_user")
    @FormUrlEncoded
    Call<String> addUser(@Field("mobile")String number);

    @POST("get_wishlist")
    @FormUrlEncoded
    Call<String> getWishList(@Field("user_id")String user_id);

    @POST("add_cart")
    @FormUrlEncoded
    Call<String> addToCart(@Field("user_id")String user_id,@Field("product_id") String id,@Field("quantity") String quan, @Field("total_price")String totalPri);

    @POST("get_cartlist")
    @FormUrlEncoded
    Call<String> getCartList(@Field("user_id")String user_id);

    @POST("delete_cart")
    @FormUrlEncoded
    Call<String> deleteCart(@Field("user_id")String user_id,@Field("product_id") String id);
}
