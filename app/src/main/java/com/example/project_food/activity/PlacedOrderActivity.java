package com.example.project_food.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.project_food.R;
import com.example.project_food.model.MyCartModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PlacedOrderActivity extends AppCompatActivity
{
    FirebaseAuth auth;
    FirebaseFirestore firestore;
    Button btn_okay;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_placed_order);
        btn_okay = findViewById(R.id.btn_okay);
        btn_okay.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(PlacedOrderActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        List<MyCartModel> list = (ArrayList<MyCartModel>) getIntent().getSerializableExtra("itemList");

        if (list != null && list.size() > 0)
        {
            for (MyCartModel model : list)
            {
                final HashMap<String, Object> cartMap = new HashMap<>();

                cartMap.put("productName", model.getProductName());
                cartMap.put("productPrice", model.getProductPrice());
                cartMap.put("totalQuantity", model.getTotalQuantity());
                cartMap.put("totalPrice", model.getTotalPrice());

                firestore.collection("CurrentUser").document(auth.getCurrentUser().getUid())
                        .collection("MyOrder").add(cartMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>()
                        {
                            @Override
                            public void onComplete(@NonNull Task<DocumentReference> task)
                            {
                                if (task.isSuccessful())
                                {
                                    deleteAddToCartCollection();
                                    Toast.makeText(PlacedOrderActivity.this, "Đặt hàng thành công!", Toast.LENGTH_SHORT).show();
                                }
                                else
                                {
                                    Toast.makeText(PlacedOrderActivity.this, "Đặt hàng thất bại!", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            }
                        });
            }
        }
    }

    private void deleteAddToCartCollection()
    {
        firestore.collection("CurrentUser").document(auth.getCurrentUser().getUid())
                .collection("AddToCart")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
                {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task)
                    {
                        if (task.isSuccessful())
                        {
                            for (QueryDocumentSnapshot document : task.getResult())
                            {
                                document.getReference().delete();
                            }
                        }
                        else
                        {
                            Toast.makeText(PlacedOrderActivity.this, "Error...", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}
