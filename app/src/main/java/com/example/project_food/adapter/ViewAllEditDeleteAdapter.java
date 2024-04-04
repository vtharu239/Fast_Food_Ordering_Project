package com.example.project_food.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.project_food.R;
import com.example.project_food.model.ViewAllModel;


import java.util.List;

public class ViewAllEditDeleteAdapter extends RecyclerView.Adapter<ViewAllEditDeleteAdapter.ViewHolder>{
    Context context;
    List<ViewAllModel> list;
    private Dialog dialog;
    public interface Dialog{
        void OnClick(int pos);
    }

    public void setDialog(Dialog dialog) {
        this.dialog = dialog;
    }



    public ViewAllEditDeleteAdapter(Context context, List<ViewAllModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewAllEditDeleteAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.home_product_item_edit_delete, parent, false ));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(context).load(list.get(position).getImg_url()).into(holder.imageView);
        holder.name.setText(list.get(position).getName());
        holder.description.setText(list.get(position).getDescription());
        holder.rating.setText(list.get(position).getRating());
        holder.price.setText(list.get(position).getPrice() + "VNĐ");
    }



    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView name, description, price, rating;
        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            imageView = itemView.findViewById(R.id.view_img);
            name = itemView.findViewById(R.id.view_name);
            description = itemView.findViewById(R.id.view_description);
            price = itemView.findViewById(R.id.view_price);
            rating = itemView.findViewById(R.id.view_rating);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(dialog!=null){
                        dialog.OnClick(getLayoutPosition());
                    }
                }
            });
        }
    }
}