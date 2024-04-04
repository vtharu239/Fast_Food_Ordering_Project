package com.example.project_food.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_food.R;
import com.example.project_food.model.MyCartModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class MyCartAdapter extends RecyclerView.Adapter<MyCartAdapter.ViewHolder>
{
    Context context;
    List<MyCartModel> cartModelList;
    FirebaseFirestore firestore;
    FirebaseAuth auth;

    public MyCartAdapter(Context context, List<MyCartModel> cartModelList)
    {
        this.context = context;
        this.cartModelList = cartModelList;
        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.my_cart_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        holder.name.setText(cartModelList.get(position).getProductName());
        holder.productPrice.setText(cartModelList.get(position).getProductPrice() +" VNĐ");
        holder.totalquantity.setText(cartModelList.get(position).getTotalQuantity() +"");
        holder.totalPrice.setText(cartModelList.get(position).getTotalPrice() +" VNĐ");

        holder.deleteItem.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                firestore.collection("CurrentUser").document(auth.getCurrentUser().getUid())
                        .collection("AddToCart")
                        .document(cartModelList.get(holder.getAdapterPosition()).getDocumentId())
                        .delete()
                        .addOnCompleteListener(new OnCompleteListener<Void>()
                        {
                            @Override
                            public void onComplete(@NonNull Task<Void> task)
                            {
                                if (task.isSuccessful())
                                {
                                    cartModelList.remove(cartModelList.get(holder.getAdapterPosition()));
                                    notifyDataSetChanged();
                                    Toast.makeText(context, "Xóa sản phẩm thành công!", Toast.LENGTH_SHORT).show();
                                }
                                else
                                {
                                    Toast.makeText(context, "Xóa sản phẩm thất bại!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

    }

    @Override
    public int getItemCount()
    {
        return cartModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView name, productPrice, totalquantity, totalPrice;
        ImageView deleteItem;
        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            name = itemView.findViewById(R.id.product_name);
            productPrice = itemView.findViewById(R.id.product_price);
            totalquantity = itemView.findViewById(R.id.total_quantity);
            totalPrice = itemView.findViewById(R.id.total_price);
            deleteItem = itemView.findViewById(R.id.delete);
        }
    }
}
