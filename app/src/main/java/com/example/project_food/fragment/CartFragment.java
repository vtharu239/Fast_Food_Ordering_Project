package com.example.project_food.fragment;


import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project_food.R;
import com.example.project_food.activity.PlacedOrderActivity;
import com.example.project_food.adapter.MyCartAdapter;
import com.example.project_food.model.MyCartModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class CartFragment extends Fragment
{
    FirebaseFirestore db;
    FirebaseAuth auth;
    TextView overTotalAmount;
    RecyclerView recyclerView;
    MyCartAdapter cartAdapter;
    List<MyCartModel> cartModelList;
    Button buyNow;

    public CartFragment()
    {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_cart, container, false);

        db =  FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        recyclerView = root.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        overTotalAmount = root.findViewById(R.id.textView6);

        cartModelList = new ArrayList<>();
        cartAdapter = new MyCartAdapter(getActivity(), cartModelList);
        recyclerView.setAdapter(cartAdapter);

        db.collection("CurrentUser").document(auth.getCurrentUser().getUid())
                .collection("AddToCart").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
                {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task)
                    {
                        if (task.isSuccessful())
                        {
                            for (DocumentSnapshot documentSnapshot : task.getResult().getDocuments())
                            {
                                String documentId = documentSnapshot.getId();

                                MyCartModel cartModel = documentSnapshot.toObject(MyCartModel.class);
                                cartModel.setDocumentId(documentId);

                                cartModelList.add(cartModel);
                                cartAdapter.notifyDataSetChanged();
                            }

                            calculateTotalAmount(cartModelList);
                        }

                    }
                });

        buyNow = root.findViewById(R.id.buy_now);
        buyNow.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (cartModelList.size() < 1)
                {
                    Toast.makeText(getContext(), "Chưa có sản phẩm nào để đặt hàng. Hãy thêm sản phẩm vào giỏ hàng để đặt hàng thành công nhé!", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Intent intent = new Intent(getContext(), PlacedOrderActivity.class);
                    intent.putExtra("itemList", (Serializable) cartModelList);
                    startActivity(intent);
                }
            }
        });

        return  root;
    }

    private  void  DateAndTimeOrder ()
    {
        String saveCurrentDate, saveCurrentTime;
        Calendar calforDate = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MM dd, yyyy");
        saveCurrentDate = currentDate.format(calforDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calforDate.getTime());

    }

    private void calculateTotalAmount(List<MyCartModel> cartModelList)
    {
        int totalAmount = 0;
        for (MyCartModel myCartModel : cartModelList)
        {
            totalAmount += myCartModel.getTotalPrice();
        }
        overTotalAmount.setText("Total Bill : "+totalAmount +" VNĐ");
    }
}