package com.androidproject.iottechbazaar.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.androidproject.iottechbazaar.R;
import com.androidproject.iottechbazaar.databinding.BottomSheetLoginBinding;
import com.androidproject.iottechbazaar.otpedittext.OnCompleteListener;
import com.androidproject.iottechbazaar.retro.ApiClient;
import com.androidproject.iottechbazaar.retro.ApiInterface;
import com.androidproject.iottechbazaar.util.BaseActivity;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends BaseActivity {

    BottomSheetLoginBinding binding;
    String banner_1;
    String number;
    Context context;
    String Details;
    Animation animationleft;
    private static final int REQ_USER_CONSENT = 200;
    SmsBroadcastReceiver smsBroadcastReceiver;
    String code;
    SharedPreferences pref;


    @Override
    public void onStart() {
        super.onStart();
        registerBroadcastReceiver();
    }

    private void registerBroadcastReceiver() {
        smsBroadcastReceiver = new SmsBroadcastReceiver();
        smsBroadcastReceiver.smsBroadcastReceiverListener =
                new SmsBroadcastReceiver.SmsBroadcastReceiverListener() {
                    @Override
                    public void onSuccess(Intent intent) {
                        startActivityForResult(intent, REQ_USER_CONSENT);
                    }

                    @Override
                    public void onFailure() {

                    }
                };
        IntentFilter intentFilter = new IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION);
        registerReceiver(smsBroadcastReceiver, intentFilter);
    }

    @Override
    public void onStop() {
        super.onStop();
        unregisterReceiver(smsBroadcastReceiver);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        makeFullScreen();
        binding=DataBindingUtil.setContentView(LoginActivity.this,R.layout.bottom_sheet_login);
        changeStatusBarColor(ContextCompat.getColor(this, R.color.purple_500));
        hideToolbar();
        Intent intent=getIntent();
        banner_1=intent.getStringExtra("banner_1");

        context=LoginActivity.this;

        RequestOptions requestOptionowner = new RequestOptions();
        requestOptionowner.placeholder(R.drawable.image_gallery);
        requestOptionowner.error(R.drawable.image_gallery);

        Glide.with(LoginActivity.this)
                .setDefaultRequestOptions(requestOptionowner)
                .load(banner_1)
                .into(new SimpleTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        binding.ivBan.setBackgroundDrawable(resource);
                    }
                });

        binding.ivCross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        binding.cirLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.oevView.getVisibility() == View.GONE) {
                    number = binding.etNumber.getText().toString();
                    if (!isValid(number)) {

                            binding.etNumber.setError(getResources().getString(R.string.Invalid_Number));

                    } else {
                        hitOtpApi();
                    }
                }
                else
                {

                }



            }
        });

        binding.oevView.setOnCompleteListener(new OnCompleteListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onComplete(String value) {
                hitOtpVerifyApi();
            }
        });


    }

    private void hitOtpApi() {

        if (Utility.isNetworkAvailable(context)) {
           showProgressBar();
            RequestQueue requestQueue = Volley.newRequestQueue(context);
            String url = "https://2factor.in/API/V1/a94f102f-82db-11ea-9fa5-0200cd936042/SMS/" + number + "/AUTOGEN";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                   hideProgressBar();
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String Status = jsonObject.getString("Status");
                        if (Status.equalsIgnoreCase("Success")) {
                            Toast.makeText(context, getResources().getString(R.string.Otp_is_sent_on_your_phone), Toast.LENGTH_SHORT).show();
                            Details = jsonObject.getString("Details");
                            if (binding.oevView.getVisibility() == View.GONE) {
                                binding.oevView.setVisibility(View.GONE);
                                animationleft = AnimationUtils.loadAnimation(context, R.anim.anim_slide_in_right);
                                binding.txtNumber.setAnimation(animationleft);
                                binding.txtNumber.requestFocus();
                                binding.txtNumber.setVisibility(View.GONE);

                                otpAnimation();
                            }

                            startSmsUserConsent();

                        } else {
                            Toast.makeText(context, getResources().getString(R.string.Please_try_with_another_number), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new com.android.volley.Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(context, getResources().getString(R.string.slow_network_connection), Toast.LENGTH_SHORT).show();
                    hideProgressBar();
                }
            });

            requestQueue.add(stringRequest);

        } else {
            Toast.makeText(context,  getResources().getString(R.string.Please_Check_Your_Internet), Toast.LENGTH_SHORT).show();
        }
    }

    private void otpAnimation() {
        binding.oevView.setVisibility(View.VISIBLE);
        animationleft = AnimationUtils.loadAnimation(context, R.anim.down_to_up);
        binding.oevView.setAnimation(animationleft);
        binding.oevView.requestFocus();

    }

    private void startSmsUserConsent() {
        SmsRetrieverClient client = SmsRetriever.getClient(context);
        //We can add sender phone number or leave it blank
        // I'm adding null here
        client.startSmsUserConsent(null).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
//                Toast.makeText(getApplicationContext(), "On Success", Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
//                Toast.makeText(getApplicationContext(), "On OnFailure", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQ_USER_CONSENT) {
            if ((resultCode == RESULT_OK) && (data != null)) {
                //That gives all message to us.
                // We need to get the code from inside with regex
                String message = data.getStringExtra(SmsRetriever.EXTRA_SMS_MESSAGE);
//                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
//                textViewMessage.setText(
//                        String.format("%s - %s", getString(R.string.received_message), message));

                getOtpFromMessage(message);
            }
        }


    }

    private void getOtpFromMessage(String message) {
        // This will match any 6 digit number in the message
        Pattern pattern = Pattern.compile("(|^)\\d{6}");
        Matcher matcher = pattern.matcher(message);
        if (matcher.find()) {
            binding.oevView.setText(matcher.group(0));
        }
    }


    private void hitOtpVerifyApi() {

        if (checkValidation()) {
            if (Utility.isNetworkAvailable(context)) {
                showProgressBar();
                RequestQueue queue = Volley.newRequestQueue(context);
                String url = "https://2factor.in/API/V1/a94f102f-82db-11ea-9fa5-0200cd936042/SMS/VERIFY/" + Details + "/" + code;
                StringRequest postRequest = new StringRequest(Request.Method.GET, url,
                        new com.android.volley.Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                hideProgressBar();
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    String Status = jsonObject.getString("Status");
                                    if (Status.equalsIgnoreCase("Success")) {
                                        Toast.makeText(context, getResources().getString(R.string.OTP_is_verified_sucessfully), Toast.LENGTH_SHORT).show();
                                        
                                        hitAddUserApi();





                                    } else {
                                        binding.oevView.triggerErrorAnimation();
                                        Toast.makeText(context, getResources().getString(R.string.please_enter_the_valid_OTP), Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Toast.makeText(context,  getResources().getString(R.string.slow_network_connection), Toast.LENGTH_SHORT).show();
                                }
                            }
                        },
                        new com.android.volley.Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // error
                                binding.oevView.triggerErrorAnimation();
                                Toast.makeText(context, getResources().getString(R.string.please_enter_the_valid_OTP), Toast.LENGTH_SHORT).show();
                               hideProgressBar();
//                                        Log.d("Error.Response", error.toString());
                            }
                        }
                );
                queue.add(postRequest);
            } else {
                Toast.makeText(context, getResources().getString(R.string.Please_Check_Your_Internet), Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void hitAddUserApi() {

        Call<String> stringCall = null;
        if (Utility.isNetworkAvailable(context)) {
            showProgressBar();
            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
            stringCall = apiInterface.addUser(number);
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
                            String message = jsonObject.getString("message");
                            String user_id = jsonObject.getString("user_id");


                            if (type.equalsIgnoreCase("Success")) {


                                pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
                                SharedPreferences.Editor editor = pref.edit();
                                editor.putString("user_id", user_id);
                                editor.putString("number", number);
                                editor.commit();
                                finish();


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

    private boolean checkValidation() {
        boolean check = true;
        code = binding.oevView.getText().toString();
        if (TextUtils.isEmpty(code)) {
            binding.oevView.triggerErrorAnimation();
            Toast.makeText(context, getResources().getString(R.string.please_enter_the_valid_OTP), Toast.LENGTH_SHORT).show();
            check = false;
        }

        return check;
    }


    @Override
    public void onBackPressed() {
        if (binding.oevView.getVisibility() == View.VISIBLE) {
            binding.oevView.setVisibility(View.GONE);
            binding.txtNumber.setVisibility(View.VISIBLE);
            binding.oevView.setText(null);

            animationleft = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.up_to_down);
            binding.oevView.setAnimation(animationleft);
            binding.oevView.requestFocus();

            animationleft = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.anim_slide_in_left);
            binding.txtNumber.setAnimation(animationleft);
            binding.txtNumber.requestFocus();

        }
//        else {
//            super.onBackPressed();
//        }
    }


}
