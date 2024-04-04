package com.example.project_food.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import android.widget.TextView;
import android.widget.Toast;

import com.example.project_food.R;

import com.example.project_food.activity.ChangePasswordActivity;
import com.example.project_food.activity.MainActivity;
import com.example.project_food.activity.ManageCategoryActivity;
import com.example.project_food.activity.ManageProActivity;
import com.example.project_food.activity.UserProfileActivity;
import com.example.project_food.model.ReadWriteUserDetails;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class InfoFragment extends Fragment
{
    Button btnManagerPro, btnManagerCate;
    TextView txtName;
    Button btnViewProfile, btnLogout, btnchangePass;
    String fullName;
    FirebaseAuth authProfile;
    SwitchCompat switchMode;
    boolean nightMode;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    Context context;

    public InfoFragment()
    {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_info, container, false);

//        switchMode = view.findViewById(R.id.switchMode);
//        sharedPreferences = context.getSharedPreferences("MODE", Context.MODE_PRIVATE);
//        nightMode = sharedPreferences.getBoolean("nightMode", false);
//
//        if(nightMode)
//        {
//            switchMode.setChecked(true);
//            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
//        }
//
//        switchMode.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v)
//            {
//                if (nightMode)
//                {
//                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
//                    editor = sharedPreferences.edit();
//                    editor.putBoolean("nightMode", false);
//                }
//                else
//                {
//                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
//                    editor = sharedPreferences.edit();
//                    editor.putBoolean("nightMode", true);
//                }
//                editor.apply();
//            }
//        });

        txtName = view.findViewById(R.id.setting_name);
        //imageView = view.findViewById(R.id.Info_Image);
        btnViewProfile = view.findViewById(R.id.viewProfile);
        btnLogout = view.findViewById(R.id.btnLogout);
        //btnRefresh = view.findViewById(R.id.btn_refresh);

        authProfile = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = authProfile.getCurrentUser();

        showUserProfile(firebaseUser);
        btnViewProfile.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(v.getContext(), UserProfileActivity.class);
                v.getContext().startActivity(intent);
            }
        });

        btnchangePass = view.findViewById(R.id.btnChangePassword);
        btnchangePass.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(v.getContext(), ChangePasswordActivity.class);
                v.getContext().startActivity(intent);
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                authProfile.signOut();
                Toast.makeText(getContext(), "Đăng xuất tài khoản thành công!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        if(authProfile==null)
        {
            Toast.makeText(getContext(), "Something went wrong! User's detail are not available at the moment.", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(getContext(), MainActivity.class);
            startActivity(intent);
        }
        else
        {
            //checkEmailVerified(firebaseUser);
            showUserProfile(firebaseUser);
        }
        btnManagerCate = view.findViewById(R.id.btn_managerCategory);
        btnManagerCate.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getContext(), ManageCategoryActivity.class);
                startActivity(intent);
            }
        });
        btnManagerPro = view.findViewById(R.id.btn_managerProduct);
        btnManagerPro.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getContext(), ManageProActivity.class);
                startActivity(intent);
            }
        });


        return view;
    }
    private void showUserProfile(FirebaseUser firebaseUser)
    {
        String userID = firebaseUser.getUid();

        // Extracting User Reference from Database for "Signup Users"
        DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("Users");
        referenceProfile.child(userID).addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                ReadWriteUserDetails readWriteUserDetails = snapshot.getValue(ReadWriteUserDetails.class);
                if (readWriteUserDetails != null)
                {
                    fullName = readWriteUserDetails.fullName;
                    txtName.setText("Welcome, " + fullName + " !");
                }
                else
                {
                    txtName.setText("Welcome, " + "let's update your profile" + " !");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {
            }
        });
    }


}