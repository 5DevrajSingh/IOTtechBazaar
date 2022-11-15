package com.androidproject.iottechbazaar.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.androidproject.iottechbazaar.R;
import com.androidproject.iottechbazaar.databinding.ActivityMainBinding;
import com.androidproject.iottechbazaar.util.BaseActivity;
import com.androidproject.iottechbazaar.util.RxBus;
import com.simform.custombottomnavigation.Model;

import kotlin.Unit;
import rx.Subscriber;
import rx.Subscription;

public class MainActivity extends BaseActivity {

    ActivityMainBinding binding;

    private static final int ID_HOME = 0;
    private static final int ID_CART = 1;
    private static final int ID_WISH = 2;
    private static final int ID_PROFILE = 3;

    Subscription subscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
        binding= DataBindingUtil.setContentView(MainActivity.this,R.layout.activity_main);
                getSupportActionBar().hide();
        setBottomNavigationWithNavController(savedInstanceState);

        Intent intent=getIntent();
        String check=intent.getStringExtra("check");

        if (!TextUtils.isEmpty(check))
        {
            if (check.equalsIgnoreCase("wish"))
            {
                binding.bottomNavigation.onMenuItemClick(2);
            }
            if (check.equalsIgnoreCase("cart"))
            {
                binding.bottomNavigation.onMenuItemClick(1);
            }
        }



    }

    private void setBottomNavigationWithNavController(Bundle savedInstanceState) {
        int activeIndex = savedInstanceState != null ? savedInstanceState.getInt("activeIndex") : 0;
        NavController navController = Navigation.findNavController(MainActivity.this,R.id.nav_host_fragment);
        AppBarConfiguration appBarConfiguration =
                new AppBarConfiguration.Builder(R.id.navigation_home,
                        R.id.navigation_cart,
                        R.id.navigation_wish,
                        R.id.navigation_profile).build();

        NavigationUI.setupActionBarWithNavController(this,navController, appBarConfiguration);
        Model menuItems[] = new Model[]{
                new Model(R.drawable.home, R.id.navigation_home, 0, R.string.title_home, R.string.empty_value
                ),
                new Model(R.drawable.cart, R.id.navigation_cart, 1, R.string.title_favorite, R.string.empty_value
                ),
                new Model(R.drawable.wishlist, R.id.navigation_wish, 2, R.string.WishList, R.string.empty_value
                ),
                new Model(R.drawable.account, R.id.navigation_profile, 3, R.string.title_profile, R.string.empty_value)
        };






        binding.bottomNavigation.setMenuItems(menuItems,activeIndex);
        binding.bottomNavigation.setupWithNavController(navController);
        binding.bottomNavigation.setCount(ID_CART, R.string.count_update);





        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
//                Log.e(TAG, "onDestinationChanged: "+destination.getLabel());
//                Toast.makeText(MainActivity.this, ""+destination.getLabel(), Toast.LENGTH_SHORT).show();
                if (destination.getLabel().equals("Cart"))
                {
                    binding.bottomNavigation.setCount(ID_CART, R.string.empty_value);
                }
            }
        });



//        subscription = RxBus.getSubject()
//                .subscribe(new Subscriber<Object>() {
//
//
//                    @Override
//                    public void onCompleted() {
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//
//                    }
//
//                    @Override
//                    public void onNext(Object o) {
//
//Log.i("jdnvjd",o.toString());
//                        binding.bottomNavigation.setCount(ID_CART, R.string.count_update);
//                    }
//                });




    }




    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt("activeIndex", binding.bottomNavigation.getSelectedIndex());
        super.onSaveInstanceState(outState);
    }

    public void changeCartCount(String cart) {
        Log.i("sdnvnf",cart);
        int count= Integer.parseInt(cart);
        binding.bottomNavigation.setCount(ID_CART, R.string.count_update);
    }
}