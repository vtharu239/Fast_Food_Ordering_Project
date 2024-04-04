package com.example.project_food.activity;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.example.project_food.R;

import com.example.project_food.model.ViewAllModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class DetailActivity extends AppCompatActivity
{
    TextView quantity;
    int totalQuantity = 1;
    int totalPrice = 0;
    ImageView detailImg;
    TextView price, rating , description;
    Button addToCart;
    ImageView addItem, removeItem;
    FirebaseFirestore firestore;
    FirebaseAuth auth;
    ViewAllModel viewAllModel = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        final Object object = getIntent().getSerializableExtra("detail");
        if (object instanceof ViewAllModel )
        {
            viewAllModel = (ViewAllModel) object;
        }

        quantity = findViewById(R.id.quantity);
        detailImg = findViewById(R.id.detail_img);
        rating = findViewById(R.id.detail_rating);
        description = findViewById(R.id.detail_description);
        price = findViewById(R.id.detail_price);
        totalPrice = viewAllModel.getPrice() * totalQuantity;

        addItem = findViewById(R.id.add_item);
        removeItem = findViewById(R.id.remove_item);

        if (viewAllModel != null)
        {
            Glide.with(getApplicationContext()).load(viewAllModel.getImg_url()).into(detailImg);
            rating.setText(viewAllModel.getRating());
            description.setText(viewAllModel.getDescription());
            price.setText(viewAllModel.getPrice() +" ");
        }

        addItem.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (totalQuantity < 10)
                {
                    totalQuantity ++;
                    quantity.setText(String.valueOf(totalQuantity));
                    totalPrice = viewAllModel.getPrice() * totalQuantity;
                }
            }
        });
        removeItem.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (totalQuantity > 1)
                {
                    totalQuantity --;
                    quantity.setText(String.valueOf(totalQuantity));
                    totalPrice = viewAllModel.getPrice() * totalQuantity;
                }
            }
        });

        addToCart = findViewById(R.id.add_to_cart);
        addToCart.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (auth.getCurrentUser() == null)
                {
                    Toast.makeText(DetailActivity.this, "Vui lòng đăng nhập để thêm sản phẩm vào giỏ hàng của mình!", Toast.LENGTH_LONG).show();
                    showAlertDialog();
                    //startActivity(getIntent());
                }
                else
                {
                    addedToCart();
                }
            }
        });
    }

    private void addedToCart()
    {
        String saveCurrentDate, saveCurrentTime;
        Calendar calforDate = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MM dd, yyyy");
        saveCurrentDate = currentDate.format(calforDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calforDate.getTime());

        final HashMap<String, Object> cartMap = new HashMap<>();

        cartMap.put("productName", viewAllModel.getName());
        cartMap.put("productPrice", price.getText().toString());
        cartMap.put("currentDate", saveCurrentDate);
        cartMap.put("currentTime", saveCurrentTime);
        cartMap.put("totalQuantity", quantity.getText().toString());
        cartMap.put("totalPrice", totalPrice);

        firestore.collection("CurrentUser").document(auth.getCurrentUser().getUid())
                .collection("AddToCart").add(cartMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>()
                {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task)
                    {
                        if (task.isSuccessful())
                        {
                            Toast.makeText(DetailActivity.this, "Thêm sản phẩm vào giỏ hàng thành công!", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                        else
                        {
                            Toast.makeText(DetailActivity.this, "Thêm sản phẩm vào giỏ hàng thất bại!", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                });
    }


    private void showAlertDialog()
    {
        //Setup the Alert Builder
        AlertDialog.Builder builder = new AlertDialog.Builder(DetailActivity.this);
        builder.setTitle("Bạn chưa đăng nhập! Không thể thêm sản phẩm vào giỏ hàng!");
        builder.setMessage("Vui lòng đăng nhập để thực hiện thao tác!");

        // Open Email app if User click Continue Button
        builder.setPositiveButton("Đăng nhập", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                Intent intent = new Intent(DetailActivity.this, LoginActivity.class);
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
                finish();
            }
        });

        // Create the AlertDialog
        AlertDialog alertDialog = builder.create();

        // Show the AlertDialog
        alertDialog.show();

    }
}

