package com.example.project_food.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project_food.R;
import com.example.project_food.activity.PlacedOrderActivity;
import com.example.project_food.adapter.MyCartAdapter;
import com.example.project_food.adapter.MyOrderAdapter;
import com.example.project_food.model.MyCartModel;
import com.example.project_food.model.OrderModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class OrderFragment extends Fragment
{
    FirebaseFirestore db;
    FirebaseAuth auth;
    RecyclerView recyclerView;
    MyOrderAdapter orderAdapter;
    List<OrderModel> orderModelList;

    public OrderFragment()
    {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_order, container, false);

        db =  FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        recyclerView = root.findViewById(R.id.order_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        orderModelList = new ArrayList<>();
        orderAdapter = new MyOrderAdapter(getActivity(), orderModelList);
        recyclerView.setAdapter(orderAdapter);

        db.collection("CurrentUser").document(auth.getCurrentUser().getUid())
                .collection("MyOrder").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
                {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task)
                    {
                        if (task.isSuccessful())
                        {
                            for (DocumentSnapshot documentSnapshot : task.getResult().getDocuments())
                            {
                                String documentId = documentSnapshot.getId();

                                OrderModel orderModel = documentSnapshot.toObject(OrderModel.class);
                                orderModel.setDocumentId(documentId);

                                orderModelList.add(orderModel);
                                orderAdapter.notifyDataSetChanged();
                            }
                        }

                    }
                });

        return  root;
    }
}