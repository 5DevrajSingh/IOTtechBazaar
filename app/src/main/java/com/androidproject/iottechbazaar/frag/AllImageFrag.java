package com.androidproject.iottechbazaar.frag;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.androidproject.iottechbazaar.R;
import com.squareup.picasso.Picasso;


public class AllImageFrag extends Fragment {

    View view;
    ImageView iv_product;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.frag_all_img,container,false);

        iv_product=view.findViewById(R.id.iv_product);

        return view;

    }

    public void getCatId(String value) {

        Picasso.get().load(value).placeholder(R.drawable.imgload).into(iv_product);

//        RequestOptions requestOptions = new RequestOptions();
//        requestOptions.placeholder(R.drawable.logo_mart9_bag);
//        requestOptions.error(R.drawable.logo_mart9_bag);
//
//        Glide.with(getActivity())
//                .setDefaultRequestOptions(requestOptions)
//                .load(value).into(iv_product);
    }

    public ImageView refreshT() {
        iv_product=view.findViewById(R.id.iv_product);
        return iv_product;
    }
}
