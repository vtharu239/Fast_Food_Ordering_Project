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
import com.example.project_food.model.HomeCategory;

import java.util.List;

public class CategoryEditDeleteAdapter extends RecyclerView.Adapter<CategoryEditDeleteAdapter.ViewHolder> {
    Context context;
    List<HomeCategory> categoryList;
    private Dialog dialog;

    public interface Dialog {
        void OnClick(int pos);
    }

    public void setDialog(Dialog dialog) {
        this.dialog = dialog;
    }

    public CategoryEditDeleteAdapter(Context context, List<HomeCategory> categoryList) {
        this.context = context;
        this.categoryList = categoryList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CategoryEditDeleteAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.home_category_item_deleteandedit, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(context).load(categoryList.get(position).getImg_url()).into(holder.catImg);
        holder.name.setText(categoryList.get(position).getName());

    }

    @Override
    public int getItemCount() {

        return categoryList.size();

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView catImg;
        TextView name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            catImg = itemView.findViewById(R.id.home_cat_img);
            name = itemView.findViewById(R.id.home_cat_name);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (dialog != null) {
                        dialog.OnClick(getLayoutPosition());
                    }
                }
            });
        }
    }
}