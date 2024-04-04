package com.example.project_food.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_food.R;
import com.example.project_food.model.MyCartModel;
import com.example.project_food.model.OrderModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class MyOrderAdapter extends RecyclerView.Adapter<MyOrderAdapter.ViewHolder>
{
    Context context;
    List<OrderModel> orderModelList;
    FirebaseFirestore firestore;
    FirebaseAuth auth;

    public MyOrderAdapter(Context context, List<OrderModel> orderModelList)
    {
        this.context = context;
        this.orderModelList = orderModelList;
        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
    }

    public MyOrderAdapter(View inflate)
    {
    }

    @NonNull
    @Override
    public MyOrderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.my_order_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyOrderAdapter.ViewHolder holder, int position)
    {
        holder.name.setText(orderModelList.get(position).getProductName());
        holder.price.setText(orderModelList.get(position).getProductPrice() +" VNĐ");
        holder.quantity.setText(orderModelList.get(position).getTotalQuantity() +"");
        holder.totalPrice.setText(orderModelList.get(position).getTotalPrice() +" VNĐ");
    }

    @Override
    public int getItemCount()
    {
        return orderModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView name, price, quantity, totalPrice;
        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            name = itemView.findViewById(R.id.cart_product_name);
            price = itemView.findViewById(R.id.cart_product_price);
            quantity = itemView.findViewById(R.id.cart_total_quantity);
            totalPrice = itemView.findViewById(R.id.cart_total_price);
        }
    }
}
