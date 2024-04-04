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

import com.bumptech.glide.Glide;
import com.example.project_food.R;
import com.example.project_food.model.ViewAllModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;


public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.ViewHolder>
{

    Context context;
    List<ViewAllModel> favModelList;
    FirebaseFirestore firestore;
    FirebaseAuth auth;

    //WishlistItemBinding binding;

    public FavoriteAdapter(Context context, List<ViewAllModel> favModelList)
    {
        this.context = context;
        this.favModelList = favModelList;
        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
    }



    @NonNull
    @Override

    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.wishlist_item,parent,false));
    }


    public void setData(List<ViewAllModel> foodList)
    {
        this.favModelList = foodList;
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
      // ViewAllModel food = favModelList.get(position);


        Glide.with(context).load(favModelList.get(position).getImg_url()).into(holder.imageView);
        holder.name.setText(favModelList.get(position).getName());
        holder.rating.setText(favModelList.get(position).getRating());

       holder.price.setText(favModelList.get(position).getPrice()+"");

        holder.iconFavorite.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                firestore.collection("CurrentUser").document(auth.getCurrentUser().getUid())
                        .collection("AddToWishList")
                        .document(favModelList.get(holder.getAdapterPosition()).getId())
                        .delete()
                        .addOnCompleteListener(new OnCompleteListener<Void>()
                        {
                            @Override
                            public void onComplete(@NonNull Task<Void> task)
                            {
                                if (task.isSuccessful())
                                {
                                    favModelList.remove(favModelList.get(holder.getAdapterPosition()));
                                    notifyDataSetChanged();
                                    Toast.makeText(context, "Xóa sản phẩm khỏi danh sách yêu thích thành công!", Toast.LENGTH_SHORT).show();
                                }
                                else
                                {
                                    Toast.makeText(context, "Không thể xóa sản phẩm khỏi danh sách yêu thích!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return favModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        ImageView iconFavorite;
        ImageView imageView;
        TextView name, price, rating;
        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            iconFavorite = itemView.findViewById(R.id.WishList_IconFavorite);
            imageView = itemView.findViewById(R.id.WishList_pro_img);
            name = itemView.findViewById(R.id.WishList_pro_name);
            price = itemView.findViewById(R.id.WishList_pro_price);
            rating = itemView.findViewById(R.id.WishList_rating);

        }
    }

}

