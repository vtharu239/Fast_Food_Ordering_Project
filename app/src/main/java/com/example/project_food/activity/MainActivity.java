package com.example.project_food.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.project_food.R;
import com.example.project_food.fragment.CartFragment;
import com.example.project_food.fragment.OrderFragment;
import com.example.project_food.fragment.HomeFragment;
import com.example.project_food.fragment.InfoFragment;
import com.example.project_food.fragment.WishFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener

{
    FirebaseAuth authProfile;
    DrawerLayout drawerLayout;
    BottomNavigationView bottomNavigationView;
    FragmentManager fragmentManager;
    Toolbar toolBar;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

       // navController = Navigation.findNavController(this, R.id.fragment_container);

        toolBar = findViewById(R.id.toolbar);
        setSupportActionBar(toolBar);

        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolBar,R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        bottomNavigationView= findViewById(R.id.bottom_navigation);
        bottomNavigationView.setBackground(null);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener()
        {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item)
            {
                int itemId = item.getItemId();
                if(itemId == R.id.bottom_home)
                {
                    openFragment(new HomeFragment());
                    return true;
                }
                else if (itemId == R.id.bottom_wish)
                {
                    openFragment(new WishFragment() );
                    return true;
                }
                else if (itemId == R.id.bottom_Cart)
                {
                    if (authProfile.getCurrentUser() == null)
                    {
                        Toast.makeText(MainActivity.this, "Bạn chưa đăng nhập! Không có giỏ hàng để hiển thị.", Toast.LENGTH_LONG).show();
                        showAlertDialog();
                        //startActivity(getIntent());
                    }
                    else
                    {
                        openFragment(new CartFragment() );
                        return true;
                    }
                }
                else if (itemId == R.id.bottom_listhistory)
                {
                    if (authProfile.getCurrentUser() == null)
                    {
                        Toast.makeText(MainActivity.this, "Bạn chưa đăng nhập! Không có đơn hàng để hiển thị.", Toast.LENGTH_LONG).show();
                        showAlertDialog();
                        //startActivity(getIntent());
                    }
                    else
                    {
                        openFragment(new OrderFragment() );
                        return true;
                    }
                }
                else if (itemId == R.id.bottom_account)
                {
                    if (authProfile.getCurrentUser() == null)
                    {
                        Toast.makeText(MainActivity.this, "Bạn chưa đăng nhập! Không có Profile để hiển thị.", Toast.LENGTH_LONG).show();
                        showAlertDialog();
                        //startActivity(getIntent());
                    }
                    else
                    {
                        openFragment(new InfoFragment() );
                        return true;
                    }
                }
                return false;
            }
        });
        fragmentManager = getSupportFragmentManager();
        openFragment(new HomeFragment());

        authProfile = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = authProfile.getCurrentUser();
        //showUserProfile(firebaseUser);


    }

    private void showAlertDialog()
    {
        //Setup the Alert Builder
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Bạn chưa đăng nhập! Không có thông tin để hiển thị.");
        builder.setMessage("Vui lòng đăng nhập để xem thông tin!");

        // Open Email app if User click Continue Button
        builder.setPositiveButton("Đăng nhập", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
        builder.setNegativeButton("Trở về", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                startActivity(getIntent());
            }
        });

        // Create the AlertDialog
        AlertDialog alertDialog = builder.create();

        // Show the AlertDialog
        alertDialog.show();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item)
    {
        int itemId = item.getItemId();

        if (itemId == R.id.bottom_home)
        {
            openFragment(new HomeFragment());
        }
        else if (itemId == R.id.bottom_wish)
        {
            openFragment( new WishFragment());
        }
        else if (itemId == R.id.bottom_Cart)
        {
            openFragment( new CartFragment());
        }
        else if (itemId == R.id.bottom_listhistory)
        {
            openFragment( new OrderFragment());
        }
        else if (itemId == R.id.bottom_account)
        {
            openFragment( new InfoFragment());
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed()
    {
        if (drawerLayout.isDrawerOpen(GravityCompat.START))
        {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else
        {
            super.onBackPressed();
        }
    }

    private void openFragment (Fragment fragment)
    {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container,fragment);
        transaction.commit();
    }

}